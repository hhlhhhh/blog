package com.blog.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.Announcement;
import com.blog.back.framework.resp.PageData;
import com.blog.back.framework.resp.Result;
import com.blog.back.service.AnnouncementService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 公告
 */
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {


    @Autowired
    private AnnouncementService announcementService;

    /**
     * 删除公告
     * @param id
     * @return
     */
    @RequiresRoles({"admin"})
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        announcementService.delete(id);
        return Result.success();
    }

    @RequiresRoles({"admin"})
    @PostMapping("/save")
    public Result update(@RequestBody Announcement announcement){
        announcementService.saveOrUpdate(announcement);
        return Result.success(announcement);
    }

    //分页查询公告
    @PostMapping("/query")
    public Result query(@RequestBody PageView pageView){
        PageData<Announcement> pageInfo = announcementService.query(pageView);
        return Result.success(pageInfo);
    }

    /**
     * 获取主页面展示的通知
     */
    @GetMapping("/head")
    public Result getHeadAnnouncement(){
        return Result.success(announcementService.list(new QueryWrapper<Announcement>()
                .orderByDesc("sort").orderByDesc("update_time").last("limit 3")));
    }

}
