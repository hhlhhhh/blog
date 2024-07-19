package com.blog.back.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.Announcement;
import com.blog.back.framework.resp.PageData;
import com.blog.back.mapper.AnnouncementMapper;
import com.blog.back.service.AnnouncementService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Autowired
    AnnouncementMapper announcementMapper;


    @Override
    public int create(Announcement announcement) {
        announcement.setId(null);
        announcement.setSort(0);
        announcementMapper.insert(announcement);
        return 0;
    }

    @Override
    public int delete(Integer id) {
        return announcementMapper.deleteById(id);
    }

    @Override
    public int update(Announcement announcement) {
        return announcementMapper.updateById(announcement);
    }

    @Override
    public PageData<Announcement> query(PageView pageView) {
        Page<Announcement> page = new Page<>();
        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
        QueryWrapper<Announcement> wrapper = new QueryWrapper<Announcement>().orderByDesc("update_time");
        if(!StringUtils.isBlank(pageView.getField())){
            wrapper.like(pageView.getField(),pageView.getKeyword());
        }else if(StringUtils.isNotBlank(pageView.getKeyword())) {
            wrapper.like("title", pageView.getKeyword())
                    .or().like("text", pageView.getKeyword());
        }
        Page<Announcement> announcementPage = announcementMapper.selectPage(page,wrapper);
        return PageData.builder(announcementPage.getRecords(),
                pageView.getSize(),pageView.getCurrent(),
                announcementPage.getTotal());
    }
}
