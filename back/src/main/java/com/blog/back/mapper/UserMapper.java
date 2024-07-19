package com.blog.back.mapper;

import com.blog.back.POJO.entity.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends MPJBaseMapper<User> {
}
