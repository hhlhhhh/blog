package com.blog.back.framework.shiro.profile;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class ShiroBaseProfile implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    private Integer id;

    private String account;

    private String email;

    private String password;

    private Integer status;

    private String nickname;

    private String avatar;

}
