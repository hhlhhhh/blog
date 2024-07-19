package com.blog.back.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.Tag;
import com.blog.back.framework.resp.PageData;

public interface TagService extends IService<Tag> {

    public PageData<Tag> getTagList(PageView pageView);

    public void deleteTagById(Integer id);

}
