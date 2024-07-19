package com.blog.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.back.POJO.VO.*;
import com.blog.back.POJO.entity.User;
import com.blog.back.framework.enums.EmailCodeType;
import com.blog.back.framework.enums.SendEmail;
import com.blog.back.framework.enums.UserExistEnum;
import com.blog.back.framework.resp.PageData;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface UserService extends IService<User> {


    public String login(UserLogin userLogin);  //登录

    public String loginByEmail(UserLoginEmail userLoginEmail);  //邮箱登录

    public void setAuthorization(HttpServletResponse response, String auth); //设置jwt

    public void logout();   //退出登录

    public boolean exits(UserExistEnum exitEnum, String value);   //判断是否存在用户

    public String register(UserRegister userRegister);  //注册并返回账号

    public boolean updateUserInfo(UpdateInfo updateInfo);

    public void sendEmail(SendEmail sendEmail);  //注册并返回账号

    public void removeAll(Integer uid);

    public PageData<User> getUserList(PageView pageView);

    /**
     * 验证邮箱
     * @param type  邮箱验证码类型
     * @param email 邮箱
     * @param code  验证码
     * @return  返回邮箱验证码是否正确
     */
    public boolean verifyEmailCode(EmailCodeType type, String email, String code);

    public void  resetPassword(User user);  //重新设置密码

    //清空缓存信息
    public void removeCacheInfo(Integer uid);

    public Map<String,Object> getUserInfoShow(Integer id);

}
