package com.blog.back.framework.context;

import com.blog.back.POJO.entity.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReqInfo {

    private User user;

}