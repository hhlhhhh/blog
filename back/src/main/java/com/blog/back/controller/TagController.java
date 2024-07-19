package com.blog.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.Tag;
import com.blog.back.framework.resp.PageData;
import com.blog.back.framework.resp.Result;
import com.blog.back.service.TagService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    TagService tagService;

    @GetMapping
    public Result getTagById(Integer id){
        Tag tag = tagService.getById(id);
        return Result.success(tag);
    }
    /**
     * 分页查询所有标签
     */
    @PostMapping("list")
    public Result getTagList(@Validated @RequestBody PageView pageView){
        PageData<Tag> res = tagService.getTagList(pageView);
        return Result.success(res);
    }

    @RequiresRoles({"admin"})
    @PostMapping("/save")
    public Result save(@RequestBody Tag tag){
        if(Objects.isNull(tag.getName()))return Result.fail("name不能为空！");
        Tag one = tagService.getOne(new QueryWrapper<Tag>().eq("name", tag.getName()));
        if(Objects.nonNull(one))return Result.fail("标签已存在！");
        tagService.saveOrUpdate(tag);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @RequiresRoles({"admin"})
    public Result delete(@PathVariable("id") Integer id){
       tagService.deleteTagById(id);
        return Result.success();
    }
    /**
     * 获取主页面展示的标签
     */
    @GetMapping("/head")
    public Result getHeadTag(){
        return Result.success(tagService.list());
    }
}
