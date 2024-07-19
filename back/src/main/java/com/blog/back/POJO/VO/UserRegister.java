package com.blog.back.POJO.VO;

import com.blog.back.utils.RegexpUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class UserRegister {

    @Pattern(regexp = RegexpUtil.userPasswordReg,message = "密码格式错误！")
    private String password;

    @Pattern(regexp = RegexpUtil.emailReg,message = "邮箱格式错误！")
    private String email;

    @Pattern(regexp = RegexpUtil.nicknameReg,message = "昵称格式错误！")
    private String nickname;

    @Pattern(regexp = RegexpUtil.registerEmailReg,message = "验证码格式错误！")
    private String code;


}
