package com.blog.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.Announcement;
import com.blog.back.framework.resp.PageData;

public interface AnnouncementService extends IService<Announcement> {

    public int create(Announcement announcement);

    public int delete(Integer id);

    public int update(Announcement announcement);

    public PageData<Announcement> query(PageView pageView);

}
