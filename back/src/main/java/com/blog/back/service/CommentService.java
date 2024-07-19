package com.blog.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.VO.SelfCommentDto;
import com.blog.back.POJO.entity.Comment;
import com.blog.back.framework.resp.PageData;

import java.util.List;

public interface CommentService extends IService<Comment> {

    public Comment getCommentById(Integer id);

    public PageData<Comment> getCommentListByArticleId(Integer id,PageView pageView);

    public PageData<Comment> getCommentListByParentId(Integer id, PageView pageView);

    Comment add(Comment comment);

    public PageData<Comment> query(PageView pageView);

    boolean deleteCommentById(Integer id);

    List<SelfCommentDto> getSelfCommentList();

}
