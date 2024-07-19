package com.blog.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.Comment;
import com.blog.back.framework.resp.PageData;
import com.blog.back.framework.resp.Result;
import com.blog.back.service.CommentService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    /**
     * 添加评论
     * @param comment
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Comment comment){
        return Result.success(commentService.add(comment));
    }

    /**
     *评论查询
     * @param pageView
     * @return
     */
    @RequiresRoles({"admin"})
    @PostMapping("/query")
    public Result query(@RequestBody PageView pageView){
        PageData<Comment> pageInfo = commentService.query(pageView);
        return Result.success(pageInfo);
    }

    /**
     * 删除评论，只有评论者和管理员可以删除评论
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        boolean f = commentService.deleteCommentById(id);
        return f?Result.success():Result.fail();
    }

    /**
     * 获取评论详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result query(@PathVariable("id") Integer id){
        return Result.success(commentService.getCommentById(id));
    }

    /**
     * 根据文章获取评论
     * @param id
     * @param pageView
     * @return
     */
    @PostMapping("/article/{id}")
    public Result getCommentListByArticleId(@PathVariable("id") Integer id,@RequestBody@Validated PageView pageView){
        return Result.success(commentService.getCommentListByArticleId(id,pageView));
    }

    /**
     * 根据父评论获取子评论
     * @param id
     * @param pageView
     * @return
     */
    @PostMapping("/parent/{id}")
    public Result getCommentListByParentId(@PathVariable("id") Integer id,@RequestBody@Validated PageView pageView){
        return Result.success(commentService.getCommentListByParentId(id,pageView));
    }

    /**
     * 根据文章id获取评论数量
     * @param id
     * @return
     */
    @GetMapping("/count/{id}")
    public Result count(@PathVariable("id")Integer id){
        //根据文章id获取评论数量
        long count = commentService.count(new QueryWrapper<Comment>().eq("article_id",id));
        return Result.success(count);
    }

    /**
     * 设置文章评论状态
     * @param id
     * @param status
     * @return
     */
    @RequiresRoles({"admin"})
    @PutMapping("/setStatus/{id}")
    public Result setStatusComment(@PathVariable("id") Integer id,@RequestParam Integer status){
        status = status==1?1:0;
        commentService.update(new UpdateWrapper<Comment>().eq("id",id).set("status",status));
        return Result.success();
    }

    /**
     * 获取自己的评论
     * @return
     */
    @GetMapping("/self")
    public Result getSelfCommentList(){
        return Result.success(commentService.getSelfCommentList());
    }

}
