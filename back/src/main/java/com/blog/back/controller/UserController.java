package com.blog.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.blog.back.POJO.VO.*;
import com.blog.back.POJO.entity.User;
import com.blog.back.framework.context.ReqInfo;
import com.blog.back.framework.context.ReqInfoContext;
import com.blog.back.framework.enums.EmailCodeType;
import com.blog.back.framework.enums.SendEmail;
import com.blog.back.framework.enums.UserExistEnum;
import com.blog.back.framework.resp.PageData;
import com.blog.back.framework.resp.Result;
import com.blog.back.service.UserService;
import com.blog.back.utils.JwtFactory;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Value("${blog.media_url}")
    String baseMediaUrl;


    /**
     * 登录
     * @param userLogin
     * @param response
     * @return
     */
    @PostMapping("/login/account")
    public Result login(@Validated @RequestBody UserLogin userLogin,
                        HttpServletResponse response){
        //登录操作，获得jwt
        String jwt = userService.login(userLogin);
        userService.setAuthorization(response,jwt);     //将jwt放在header里面

        return Result.success();
    }

    /**
     * 邮箱登录
     * @param userLoginEmail
     * @param response
     * @return
     */
    @PostMapping("/login/email")
    public Result loginByEmail(@Validated @RequestBody UserLoginEmail userLoginEmail,
                        HttpServletResponse response){
        boolean ver = userService.verifyEmailCode(EmailCodeType.Login, userLoginEmail.getEmail(), userLoginEmail.getCode());
        if(!ver) return Result.fail("验证码不存在！");
        //登录操作，获得jwt
        String jwt = userService.loginByEmail(userLoginEmail);
        userService.setAuthorization(response,jwt);     //将jwt放在header里面

        return Result.success();
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    public Result logout(){
        userService.logout();
        return Result.success();
    }

    /**
     * 注册
     * @param userRegister
     * @return
     */
    @PostMapping("/register")
    public Result register(@Validated @RequestBody UserRegister userRegister){
        if(userService.exits(UserExistEnum.EMAIL,userRegister.getEmail()))
            return Result.fail(null,"邮箱已注册！");
        if(!userService.verifyEmailCode(EmailCodeType.REGISTER, userRegister.getEmail(), userRegister.getCode()))
            return Result.fail(null,"验证码错误！");
        String account = userService.register(userRegister);
        return Result.success(account,"注册成功！");
    }

    @PostMapping("/email")
    public Result sendEmail(@Validated @RequestBody SendEmail sendEmail){
        userService.sendEmail(sendEmail);
        return Result.success();
    }

    /**
     * 邮箱更改密码
     * @param object
     * @return
     */
    @PutMapping("/resetPassword/email")
    public Result resetPassword(@Validated @RequestBody ResetPasswordByEmail object){
        ReqInfo reqInfo = ReqInfoContext.getReqInfo();
        Integer user_id = reqInfo.getUser().getId();
        //获取邮箱
        User user = userService.getOne(new QueryWrapper<User>().eq("id", user_id).select("email","account"));
        if(!userService.verifyEmailCode(EmailCodeType.RESETPASSWORD,user.getEmail(),object.getCode()))
            return Result.fail(null,"验证码错误！");
        user.setPassword(object.getPassword()).setId(user_id);
        //修改密码
        userService.resetPassword(user);
        userService.logout();   //退出登录
        return Result.success(null,"密码修改成功！");
    }

    @RequiresRoles({"admin"})
    @PutMapping("/info/admin")
    public Result updateUserInfo(@RequestBody User user){
        userService.saveOrUpdate(user);
        return Result.success();
    }

    /**
     * 更改用户信息
     * @param updateInfo
     * @return
     */
    @PutMapping("/info")
    public Result updateUserInfo(@RequestBody UpdateInfo updateInfo){
        if(userService.updateUserInfo(updateInfo))
            return Result.success();
        return Result.fail(null,"修改失败！");
    }


    /**
     * 获取用户列表
     * @param pageView
     * @return
     */
    @GetMapping("/list")
    public Result getUserList(@Validated PageView pageView){
        PageData<User> res = userService.getUserList(pageView);
        return Result.success(res);
    }

    /**
     * 获取个人信息
     * @return
     */
    @GetMapping
    public Result getUserInfo(){
        Integer user_id = ReqInfoContext.getReqInfo().getUser().getId();
        User user = userService.getById(user_id);
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 查询个人信息
     * @param uid
     * @return
     */
    @GetMapping("/{id}")
    public Result getUserInfo(@PathVariable("id") Integer uid){
        User userDetail = userService.getById(uid);
        if(userDetail==null)return Result.success(new User());
        userDetail.setStatus(0).setPassword(null);
        return Result.success(userDetail);
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable("id") Integer uid){
        userService.removeAll(uid);
        return Result.success();
    }

    @RequiresRoles({"admin"})
    @GetMapping("/admin/mes/id")
    public Result getUserMesOfAdmin(@PathVariable("id")Integer id){
        return Result.success(userService.getById(id));
    }

    //改变用户状态
    @RequiresRoles({"admin"})
    @PutMapping("/state/{id}")
    public Result changeState(@PathVariable("id")Integer uid,@RequestParam("state")Integer state){
        state = (state!=null&&state==0)?state:1;
        boolean flag = userService.update(new UpdateWrapper<User>().eq("id", uid).set("state", state));
        if(flag){
            if(0==state){
                userService.removeCacheInfo(uid);
            }
            return Result.success();
        }
        return Result.fail();
    }

    //验证token是否过期
    @GetMapping("/verify")
    public Result verify(@RequestParam(value = "token") String token){
        Boolean verify = JwtFactory.verify(token);
        if(verify)return Result.success();
        return Result.fail();
    }

    //用户页面的统计数据展示
    @GetMapping("/userinfo/show")
    public Result getUserInfoShow(@RequestParam(value = "id",required = false)Integer id){
        return Result.success(userService.getUserInfoShow(id));
    }

    @PostMapping("/upload")
    public Result uploadAvatar(MultipartFile file){
        String url = UUID.randomUUID().toString();
        File newFile=new File(baseMediaUrl+"/"+ url);
        try {
            if(!newFile.createNewFile()){
                return Result.fail("上传失败!");
            }
            file.transferTo(newFile);
        } catch (IOException e) {
            return Result.fail("上传失败!");
        }
        Integer user_id = ReqInfoContext.getReqInfo().getUser().getId();
        String viewUrl = "http://localhost:8090/media/"+url;
        userService.update(null,new UpdateWrapper<User>().set("avatar",viewUrl).eq("id",user_id));
        return Result.success(viewUrl);
    }


}
