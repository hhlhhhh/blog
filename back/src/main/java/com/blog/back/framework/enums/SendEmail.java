package com.blog.back.framework.enums;

import com.blog.back.utils.RegexpUtil;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SendEmail {

    @NotNull(message = "类型错误！")
    private EmailCodeType type;

    @Pattern(regexp = RegexpUtil.emailReg,message = "邮箱错误！")
    private String email;

}
