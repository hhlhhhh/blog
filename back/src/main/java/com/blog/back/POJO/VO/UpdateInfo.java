package com.blog.back.POJO.VO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class UpdateInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 42L;

    private String nickname;

    private String profile;

}
