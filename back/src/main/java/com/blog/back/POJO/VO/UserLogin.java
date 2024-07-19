package com.blog.back.POJO.VO;

import com.blog.back.utils.RegexpUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class UserLogin {

    @Pattern(regexp = RegexpUtil.userAccountReg,message = "账户格式错误！")
    private String account;

    @Pattern(regexp = RegexpUtil.userPasswordReg,message = "密码格式错误！")
    private String password;

}
