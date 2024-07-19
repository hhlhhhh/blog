package com.blog.back.framework.enums;

import lombok.Getter;

@Getter
public enum EmailCodeType {
    RESETPASSWORD("resetPassword","更改密码",5L),REGISTER("register","注册",10L),Login("login","邮箱登录",10L);

    private String type;

    private String name;

    private Long time;

    EmailCodeType(String type, String name, Long time) {
        this.type = type;
        this.name = name;
        this.time = time;
    }

}
