package com.blog.back.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.ArticleTag;
import com.blog.back.POJO.entity.Tag;
import com.blog.back.framework.resp.PageData;
import com.blog.back.mapper.TagMapper;
import com.blog.back.service.ArticleTagService;
import com.blog.back.service.TagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private TagMapper tagMapper;

    @Override
    public PageData<Tag> getTagList(PageView pageView) {
        Page<Tag> page = new Page<>();
        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
        QueryWrapper<Tag> wrapper = new QueryWrapper<>();
        if(!StringUtils.isBlank(pageView.getField())){
            if(pageView.getField().equals("name"))
                wrapper.like(pageView.getField(),pageView.getKeyword());
        }
        Page<Tag> tagPage = tagMapper.selectPage(page,wrapper);
        return PageData.builder(tagPage.getRecords(),
                pageView.getSize(),pageView.getCurrent(),
                tagPage.getTotal());
    }

    @Transactional
    public void deleteTagById(Integer id){
        tagMapper.deleteById(id);
        articleTagService.remove(new QueryWrapper<ArticleTag>().eq("tag_id",id));
    }


}
