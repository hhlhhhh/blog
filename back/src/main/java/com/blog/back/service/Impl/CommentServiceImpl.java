package com.blog.back.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.VO.SelfCommentDto;
import com.blog.back.POJO.entity.Article;
import com.blog.back.POJO.entity.Comment;
import com.blog.back.framework.context.ReqInfoContext;
import com.blog.back.framework.resp.PageData;
import com.blog.back.framework.shiro.profile.ShiroBaseProfile;
import com.blog.back.mapper.ArticleMapper;
import com.blog.back.mapper.ArticleTagMapper;
import com.blog.back.mapper.CommentMapper;
import com.blog.back.mapper.UserMapper;
import com.blog.back.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper,Comment> implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ArticleTagMapper articleTagMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Override
    public Comment getCommentById(Integer id) {
        Comment comment = commentMapper.selectById(id);
        return comment;
    }

    @Override
    public PageData<Comment> getCommentListByArticleId(Integer id,PageView pageView) {
        Page<Comment> page = new Page<>();
        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
        QueryWrapper<Comment> wrapper = new QueryWrapper<Comment>()
                .eq("article_id",id)
                .eq("parent_id",0)
                .orderByDesc("create_time");
        if(!StringUtils.isBlank(pageView.getField())){
            wrapper.like(pageView.getField(),pageView.getKeyword());
        }
        Page<Comment> commentPage = commentMapper.selectPage(page,wrapper);
        commentPage.getRecords().forEach(comment -> {   //获取评论的用户信息
            comment.setUser(userMapper.selectById(comment.getUserId()));
        });
        return PageData.builder(commentPage.getRecords(),
                pageView.getSize(),pageView.getCurrent(),
                commentPage.getTotal());
    }

    @Override
    public PageData<Comment> getCommentListByParentId(Integer id, PageView pageView) {
        Comment topComment = commentMapper.selectById(id);
        topComment.setUser(userMapper.selectById(topComment.getUserId()));
        Page<Comment> page = new Page<>();
        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
        QueryWrapper<Comment> wrapper = new QueryWrapper<Comment>()
                .eq("top_parent",id)
                .orderByDesc("create_time");
        if(!StringUtils.isBlank(pageView.getField())){
            wrapper.like(pageView.getField(),pageView.getKeyword());
        }
        Page<Comment> commentPage = commentMapper.selectPage(page,wrapper);

        commentPage.getRecords().forEach(comment -> {
            comment.setUser(userMapper.selectById(comment.getUserId())); //获取评论的用户信息
            if(comment.getParentId().equals(id)){   //如果再第二层就不需要数据库查询
                comment.setParentComment(topComment);
            }else{
                comment.setParentComment(commentMapper.selectById(comment.getParentId()));  //获取父评论的用户信息
                comment.getParentComment().setUser(userMapper.selectById(comment.getParentComment().getUserId()));
            }
        });

        return PageData.builder(commentPage.getRecords(),
                pageView.getSize(),pageView.getCurrent(),
                commentPage.getTotal());
    }

    @Override
    public Comment add(Comment comment) {
        Article article = articleMapper.selectById(comment.getArticleId());
        if(article==null)return new Comment();
        if(article.getCommentStatus()!=1)return new Comment();  //禁止评论

        Integer uid = ReqInfoContext.getReqInfo().getUser().getId();    //获取用户id
        comment.setUserId(uid);
        comment.setStatus(1);
        Comment parentComment = commentMapper.selectById(comment.getParentId());    //获取顶层comment
        while(parentComment!=null&&(parentComment.getParentId()!=0&&parentComment.getParentId()!=null)){
            //将被评论的评论的子评论数+1
            commentMapper.update(null,new UpdateWrapper<Comment>()
                    .set("count",parentComment.getCount()+1).eq("id",parentComment.getId()));
            parentComment = commentMapper.selectById(parentComment.getParentId());
        }
        if(parentComment!=null){    //更新顶层评论的子评论数
            commentMapper.update(null,new UpdateWrapper<Comment>()
                    .set("count",parentComment.getCount()+1).eq("id",parentComment.getId()));
            comment.setTopParent(parentComment.getId());
        }else{
            comment.setParentId(0); //如果是顶级评论则设置
            comment.setTopParent(0);
            comment.setCount(0);
        }
        commentMapper.insert(comment);  //添加评论
        if(comment.getParentId()!=null&&comment.getParentId()!=0){
            comment.setParentComment(commentMapper.selectById(comment.getParentId()));  //获取父评论的用户信息
            comment.getParentComment().setUser(userMapper.selectById(comment.getParentComment().getUserId()));
        }
        comment.setUser(userMapper.selectById(comment.getUserId()));
        return comment; //返回评论信息
    }


    @Override
    public PageData<Comment> query(PageView pageView) {
        Page<Comment> page = new Page<>();
        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
        QueryWrapper<Comment> wrapper = new QueryWrapper<Comment>();
        if(!StringUtils.isBlank(pageView.getField())){
            wrapper.like(pageView.getField(),pageView.getKeyword());
        }else if(StringUtils.isNotBlank(pageView.getKeyword())){
            wrapper.and(wr->wr.like("content",pageView.getKeyword()));
        }
        Page<Comment> commentPage = commentMapper.selectPage(page,wrapper);
        return PageData.builder(commentPage.getRecords(),
                pageView.getSize(),pageView.getCurrent(),
                commentPage.getTotal());
    }

    @Transactional
    @Override
    public boolean deleteCommentById(Integer id) {
        Comment comment = commentMapper.selectById(id);
        if (comment == null) return false;
        Subject subject = SecurityUtils.getSubject();
        ShiroBaseProfile profile = (ShiroBaseProfile) subject.getPrincipal();
        if (!comment.getUserId().equals(profile.getId())) {
            String account = profile.getAccount();
            if(!"1111111111".equals(account)) return false;
        }

        //如果是顶层就把子评论全部删除
        if(comment.getTopParent()==null||comment.getTopParent().equals(0)){
            if(comment.getCount()!=0)
                commentMapper.delete(new UpdateWrapper<Comment>().eq("top_parent",id));
        }else{
            //如果不是子评论，就只需要让父级评论的count减一
            Comment parentComment = commentMapper.selectById(comment.getParentId());    //获取顶层comment
            while(parentComment!=null){
                commentMapper.update(null,new UpdateWrapper<Comment>()
                        .set("count",parentComment.getCount()-1)
                        .eq("id",parentComment.getId()));
                parentComment = commentMapper.selectById(parentComment.getParentId());
            }
        }
        commentMapper.deleteById(id);
        return true;
    }

    @Override
    public List<SelfCommentDto> getSelfCommentList() {
        Integer uid = ReqInfoContext.getReqInfo().getUser().getId();

        List<SelfCommentDto> res = new ArrayList<>();

       List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>().eq("user_id",uid)
               .orderByDesc("create_time"));

       commentList.forEach(comment -> {
            res.add(new SelfCommentDto()
                    .setComment(comment)
                    .setArticle(articleMapper.selectById(comment.getArticleId())));
       });
        return res;
    }
}
