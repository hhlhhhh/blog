package com.blog.back.framework.enums;

import lombok.Getter;

@Getter
public enum UserExistEnum {

    ACCOUNT("account"),EMAIL("email");

    UserExistEnum(String type) {
        this.type = type;
    }

    private String type;
}
