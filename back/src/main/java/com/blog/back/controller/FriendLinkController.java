package com.blog.back.controller;


import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.FriendLink;
import com.blog.back.framework.resp.Result;
import com.blog.back.service.FriendLinkService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/friendLink")
public class FriendLinkController {

    @Autowired
    private FriendLinkService friendLinkService;

    @RequiresRoles({"admin"})
    @PostMapping("/save")
    public Result save(@RequestBody FriendLink friendLink){
        friendLinkService.saveOrUpdate(friendLink);
        return Result.success(friendLink);
    }

    @RequiresRoles({"admin"})
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        friendLinkService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getFriendLinkById(@PathVariable("id") Integer id){
        return Result.success(friendLinkService.getById(id));
    }

    @PostMapping("/list")
    public Result getListLink(@Validated @RequestBody PageView pageView){
        return Result.success(friendLinkService.getFriendLinkList(pageView));
    }

    @GetMapping("/count")
    public Result count(){
        long count = friendLinkService.count();
        return Result.success(count);
    }

    /**
     * 获取主页面展示的友情链接
     */
    @GetMapping("/head")
    public Result getHeadFriendLink(){
        return Result.success(friendLinkService.list());
    }
}

