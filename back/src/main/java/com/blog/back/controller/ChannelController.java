package com.blog.back.controller;


import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.Channel;
import com.blog.back.framework.resp.Result;
import com.blog.back.service.ChannelService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 专栏
 */
@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    /**
     * 新增、编辑专栏
     * @param channel
     * @return
     */
    @RequiresRoles({"admin"})
    @PostMapping("/save")
    public Result saveChannel(@RequestBody Channel channel){
        if (channel.getParentId()==null){
            channel.setParentId(0);
        }
        channelService.saveOrUpdate(channel);
        return Result.success(channel);
    }

    /**
     * 删除专栏
     * @param id
     * @return
     */
    @RequiresRoles({"admin"})
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id")Integer id){
        channelService.delete(id);
        return Result.success();
    }

    /**
     * 根据id获取专栏信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getChannelById(@PathVariable("id") Integer id){
        return Result.success(channelService.getById(id));
    }

    /**
     * 获取专栏列表
     * @param pageView
     * @return
     */
    @PostMapping("/list")
    public Result detail(@Validated @RequestBody PageView pageView){
        return Result.success(channelService.getChannelList(pageView));
    }

    /**
     * 获取导航栏
     * @return
     */
    @GetMapping("/list/nav")
    public Result getNavChannel(){
        return Result.success(channelService.getNavChannel());
    }

    //获取所有专栏
    @GetMapping("/list/all")
    public Result getAllChannel(){
        return Result.success(channelService.list());
    }

    /**
     * 获取某个位置的专栏
     * @param pos
     * @return
     */
    @GetMapping("/list/show")
    public Result getTopChannel(@RequestParam String pos){
        return Result.success(channelService.getShowChannel(pos));
    }

    /**
     * 获取专栏数量
     * @return
     */
    @PostMapping("/count")
    public Result count(){
        return Result.success(channelService.count());
    }
}

