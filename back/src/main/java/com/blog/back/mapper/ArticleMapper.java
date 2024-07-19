package com.blog.back.mapper;

import com.blog.back.POJO.entity.Article;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends MPJBaseMapper<Article> {
}
