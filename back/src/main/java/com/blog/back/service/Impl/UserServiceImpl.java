package com.blog.back.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.back.POJO.VO.*;
import com.blog.back.POJO.entity.Article;
import com.blog.back.POJO.entity.ArticleTag;
import com.blog.back.POJO.entity.Comment;
import com.blog.back.POJO.entity.User;
import com.blog.back.framework.context.ReqInfo;
import com.blog.back.framework.context.ReqInfoContext;
import com.blog.back.framework.enums.EmailCodeType;
import com.blog.back.framework.enums.SendEmail;
import com.blog.back.framework.enums.UserExistEnum;
import com.blog.back.framework.resp.PageData;
import com.blog.back.framework.shiro.ShiroSession;
import com.blog.back.framework.shiro.profile.ShiroBaseProfile;
import com.blog.back.mapper.ArticleMapper;
import com.blog.back.mapper.ArticleTagMapper;
import com.blog.back.mapper.CommentMapper;
import com.blog.back.mapper.UserMapper;
import com.blog.back.service.UserService;
import com.blog.back.utils.JwtFactory;
import com.blog.back.utils.RandomGenerate;
import com.blog.back.utils.SendEmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    ShiroSession shiroSession;

    @Autowired
    SendEmailUtil sendEmailUtil;

    @Value("${blog.login.jwt-expire}")
    private int jwtExpireTime;

    @Value("${blog.user.password.iteration}")
    private int passwordIteration;

    @Override
    public String login(UserLogin userLogin) {
        boolean authed =false;
        Subject subject = SecurityUtils.getSubject();
        ShiroBaseProfile user = null;
        if(subject.isAuthenticated()){
            //如果当前处于登录状态，只需要看登录账号密码与当前用户是否一样
            user = (ShiroBaseProfile)subject.getPrincipal();
            Md5Hash md5Hash = new Md5Hash(userLogin.getPassword(),userLogin.getAccount(),passwordIteration);
            String credentialPassword = md5Hash.toString();
            if(user.getAccount().equals(userLogin.getAccount())&&user.getPassword().equals(credentialPassword))
                authed = true;
        }
        if(!authed){ //如果没认证，就去登录
            subject.login(new UsernamePasswordToken(userLogin.getAccount(),userLogin.getPassword()));
            user = (ShiroBaseProfile)subject.getPrincipal();
        }

        String jwt = JwtFactory.create(user.getId(),jwtExpireTime);
        ReqInfo reqInfo = ReqInfoContext.getReqInfo();  //设置登录上下文
        reqInfo.setUser(new User().setId(user.getId()));
        return jwt;
    }

    @Override
    public String loginByEmail(UserLoginEmail userLoginEmail) {
        User user = userMapper.selectOne(new QueryWrapper<User>()
                .eq("email",userLoginEmail.getEmail())
                .eq("status",1));
        if(user==null) throw new RuntimeException("用户不存在或已被禁用！");
        String jwt = JwtFactory.create(user.getId(),jwtExpireTime);
        ReqInfo reqInfo = ReqInfoContext.getReqInfo();  //设置登录上下文
        reqInfo.setUser(user.setPassword(null));
        return jwt;
    }

    @Override
    public void setAuthorization(HttpServletResponse response, String auth) {
        response.addHeader("Authorization",auth);
        response.setHeader("Access-Control-Expose-Headers","Authorization");
    }


    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()) return;
        System.out.println(subject.getPrincipal());
        ReqInfo reqInfo = ReqInfoContext.getReqInfo();
        String key = reqInfo.getUser().getId() + "-login-jwt";
        if(Boolean.TRUE.equals(redisTemplate.hasKey(key))){ //删除缓存
            redisTemplate.delete(key);
        }
        subject.logout(); //退出登录
    }

    @Override
    public boolean exits(UserExistEnum exitEnum, String value) {
        return userMapper.exists(new QueryWrapper<User>().eq(exitEnum.getType(),value));
    }

    @Override
    public String register(UserRegister userRegister) {
        String account = RandomGenerate.randomNString(10);
        //保证账号唯一
        while(userMapper.exists(new QueryWrapper<User>().eq("account",account))){
            account = RandomGenerate.randomNString(10);
        }
        Md5Hash md5Hash = new Md5Hash(userRegister.getPassword(),account,passwordIteration);

        String password = md5Hash.toString();
        User user = new User().setEmail(userRegister.getEmail())
                .setPassword(password)
                .setStatus(1)
                .setNickname(userRegister.getNickname())
                .setAvatar("https://p1-arco.byteimg.com/tos-cn-i-uwbnlip3yd/3ee5f13fb09879ecb5185e440cef6eb9.png~tplv-uwbnlip3yd-webp.webp")
                .setAccount(account);
        userMapper.insert(user);

        return account;
    }

    @Override
    public boolean updateUserInfo(UpdateInfo updateInfo) {
        Integer user_id = ReqInfoContext.getReqInfo().getUser().getId();

        return userMapper.update(null,new UpdateWrapper<User>()
                .set("profile",updateInfo.getProfile())
                .set("nickname",updateInfo.getNickname())
                .eq("id",user_id)) == 1;
    }

    @Override
    public void sendEmail(SendEmail sendEmail) {
        String key = sendEmail.getEmail()+sendEmail.getType().getType();
        String value = RandomGenerate.randomNString(6);
        redisTemplate.opsForValue().set(key,value,sendEmail.getType().getTime(), TimeUnit.MINUTES);
        System.out.println(sendEmail.getEmail()+" 邮箱验证码："+value);
        try{
            sendEmailUtil.sendMail(sendEmail.getEmail(), value, sendEmail.getType());
        }catch (RuntimeException exception){
            redisTemplate.delete(key);  //发送失败，需要删除redis中的缓存
            throw new RuntimeException("邮箱发送失败！");
        }
    }

    @Transactional
    @Override
    public void removeAll(Integer uid) {
        List<Article> articleList = articleMapper.selectList(new QueryWrapper<Article>().eq("create_user",uid));
        articleList.forEach(article->{
            articleMapper.deleteById(article.getId());
            articleTagMapper.delete(new QueryWrapper<ArticleTag>().eq("article_id",article.getId()));
            commentMapper.delete(new QueryWrapper<Comment>().eq("article_id",article.getId()));
        });
        commentMapper.delete(new QueryWrapper<Comment>().eq("user_id",uid));
        userMapper.deleteById(uid);
        removeCacheInfo(uid);
    }

    @Override
    public PageData<User> getUserList(PageView pageView) {
        Page<User> page = new Page<>();
        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
        QueryWrapper<User> wrapper = new QueryWrapper<User>().ne("account","1111111111");
        if(!StringUtils.isBlank(pageView.getField())){
            wrapper.like(pageView.getField(),pageView.getKeyword());
        }else if(StringUtils.isNotBlank(pageView.getKeyword())){
            wrapper.and(wr->wr.like("account",pageView.getKeyword())
                    .or().like("nickname",pageView.getKeyword()).or()
                    .like("email",pageView.getKeyword()).or()
                    .like("profile",pageView.getKeyword()));
        }
        Page<User> userPage = userMapper.selectPage(page,wrapper);
        return PageData.builder(userPage.getRecords(),
                pageView.getSize(),pageView.getCurrent(),
                userPage.getTotal());
    }

    @Override
    public boolean verifyEmailCode(EmailCodeType type, String email, String code) {
        String key = email+type.getType();
        if(Boolean.FALSE.equals(redisTemplate.hasKey(key)))return false;
        Object value = redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return code.equals(value);
    }

    @Override
    public void resetPassword(User user) {
        Md5Hash md5Hash = new Md5Hash(user.getPassword(),user.getAccount(),passwordIteration);
        String pwd = md5Hash.toString();
        userMapper.update(null,new UpdateWrapper<User>().eq("id",user.getId()).set("password",pwd));
    }

    @Override
    public void removeCacheInfo(Integer uid) {
        shiroSession.deleteSession(uid);
    }

    @Override
    public Map<String, Object> getUserInfoShow(Integer id) {
        Integer uid = id;
        if(uid==null||uid.equals(0)){
            uid = ReqInfoContext.getReqInfo().getUser().getId();
        }
        List<Article> articleList = articleMapper.selectList(new QueryWrapper<Article>().eq("create_user",uid)
                .select("article_view"));
        Map<String,Object> map = new HashMap<>();
        long view = 0;
        for (Article article : articleList) {
            view+=article.getArticleView();
        }
        map.put("view",view);
        map.put("articleCount",articleList.size());
        return map;
    }

}
