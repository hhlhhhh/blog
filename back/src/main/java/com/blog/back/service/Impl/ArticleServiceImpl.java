package com.blog.back.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.*;
import com.blog.back.framework.context.ReqInfoContext;
import com.blog.back.framework.resp.PageData;
import com.blog.back.mapper.*;
import com.blog.back.service.ArticleService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Transactional
    public int saveArticle(Article article){
        Integer id = article.getId();
        Integer uid = ReqInfoContext.getReqInfo().getUser().getId();
        article.setCreateUser(uid);
        if(id!=null&&id!=0){
            Article select = articleMapper.selectById(article.getId());
            if(!select.getCreateUser().equals(uid)) return 0;
            article.setStatus(select.getStatus());
            article.setTop(select.getTop());
            article.setCreateTime(null);
            article.setArticleView(select.getArticleView());
            articleTagMapper.delete(new UpdateWrapper<ArticleTag>().eq("article_id",article.getId()));
            articleMapper.updateById(article);
        }else{
            article.setStatus(1);
            article.setTop(0);
            article.setCreateTime(null);
            article.setArticleView(0);
            articleMapper.insert(article);
        }
        for (Tag tag : article.getTagList()) {
            if(!tagMapper.exists(new QueryWrapper<Tag>().eq("name",tag.getName()))){
                tagMapper.insert(new Tag().setName(tag.getName()));
            }
            Tag t = tagMapper.
                    selectOne(new QueryWrapper<Tag>()
                            .eq("name",tag.getName()));
            if(!articleTagMapper.exists(new QueryWrapper<ArticleTag>()
                    .eq("tag_id",t.getId())
                    .eq("article_id",article.getId())))
                articleTagMapper.insert(new ArticleTag()
                        .setArticleId(article.getId())
                        .setTagId(t.getId()));
        }

        return 1;
    }

    @Transactional
    public int delete(Integer id){
        int delete = articleMapper.deleteById(id);
        articleTagMapper.delete(new QueryWrapper<ArticleTag>().eq("article_id",id));
        commentMapper.delete(new QueryWrapper<Comment>().eq("article_id",id));
        return delete;
    }


    public PageData<Article> query(PageView pageView) {
        Page<Article> page = new Page<>();
        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
        MPJLambdaWrapper<Article> wrapper = new MPJLambdaWrapper<Article>();
        wrapper.selectAll(Article.class);

        if(StringUtils.isNotBlank(pageView.getField())){
            if("title".equals(pageView.getField())){
                wrapper.and(wr->wr.like(Article::getTitle,pageView.getKeyword()));
            }else if("summary".equals(pageView.getField())){
                wrapper.and(wr->wr.like(Article::getSummary,pageView.getKeyword()));
            }

        }else if(pageView.getKeyword().startsWith("##")){
            Tag tag = tagMapper.selectOne(new QueryWrapper<Tag>()
                    .eq("name", pageView.getKeyword().substring(2)));
            wrapper.innerJoin(ArticleTag.class,ArticleTag::getArticleId,Article::getId,
                    at->at.eq(ArticleTag::getTagId,tag.getId()));
        }else if(StringUtils.isNotBlank(pageView.getKeyword())){
            wrapper.and(wr->wr.like(Article::getTitle,pageView.getKeyword())
                    .or().like(Article::getSummary,pageView.getKeyword()));
        }else{
            wrapper.ne("top",1);  //非搜索不开启
        }

        Page<Article> articlePage = articleMapper.selectPage(page,wrapper);
        articlePage.getRecords().forEach(article -> {
            article.setChannel(channelMapper.selectById(article.getChannelId()));
            article.setUser(userMapper.selectById(article.getCreateUser()));
        });
        return PageData.builder(articlePage.getRecords(),
                pageView.getSize(),pageView.getCurrent(),
                articlePage.getTotal());
    }

    public PageData<Article> queryOfAdmin(PageView pageView) {
        Page<Article> page = new Page<>();
        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
        MPJLambdaWrapper<Article> wrapper = new MPJLambdaWrapper<Article>();
        wrapper.selectAll(Article.class);

        if(StringUtils.isNotBlank(pageView.getField())){
            if("title".equals(pageView.getField())){
                wrapper.and(wr->wr.like(Article::getTitle,pageView.getKeyword()));
            }else if("summary".equals(pageView.getField())){
                wrapper.and(wr->wr.like(Article::getSummary,pageView.getKeyword()));
            }

        }else if(pageView.getKeyword().startsWith("##")){
            Tag tag = tagMapper.selectOne(new QueryWrapper<Tag>()
                    .eq("name", pageView.getKeyword().substring(2)));
            wrapper.innerJoin(ArticleTag.class,ArticleTag::getArticleId,Article::getId,
                    at->at.eq(ArticleTag::getTagId,tag.getId()));
        }else if(StringUtils.isNotBlank(pageView.getKeyword())){
            wrapper.and(wr->wr.like(Article::getTitle,pageView.getKeyword())
                    .or().like(Article::getSummary,pageView.getKeyword()));
        }

        Page<Article> articlePage = articleMapper.selectPage(page,wrapper);
        articlePage.getRecords().forEach(article -> {
            article.setChannel(channelMapper.selectById(article.getChannelId()));
            article.setUser(userMapper.selectById(article.getCreateUser()));
        });
        return PageData.builder(articlePage.getRecords(),
                pageView.getSize(),pageView.getCurrent(),
                articlePage.getTotal());
    }


    @Override
    public List<Tag> getTagByArticleId(Integer id) {
        List<Tag> res = new ArrayList<Tag>();
        List<ArticleTag> tagList = articleTagMapper.selectList(new QueryWrapper<ArticleTag>().eq("article_id",id));
        tagList.forEach(articleTag -> {
            res.add(tagMapper.selectById(articleTag.getTagId()));
        });
        return res;
    }

//    @Override
//    public PageData<Article> getArticleByChannelId(Integer id, PageView pageView) {
//        Page<Article> page = new Page<>();
//        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
//        QueryWrapper<Article> wrapper = new QueryWrapper<Article>().eq("channel_id",id);
//        if(StringUtils.isNotBlank(pageView.getField())){
//            wrapper.like(pageView.getField(),pageView.getKeyword());
//        }else if(StringUtils.isNotBlank(pageView.getKeyword())){
//            wrapper.like("title",pageView.getKeyword())
//                    .or().like("summary",pageView.getKeyword());
//        }
//        Page<Article> articlePage = articleMapper.selectPage(page,wrapper);
//        articlePage.getRecords().forEach(article -> {
//            article.setChannel(channelMapper.selectById(article.getChannelId()));
//            article.setUser(userMapper.selectById(article.getCreateUser()));
//        });
//        return PageData.builder(articlePage.getRecords(),
//                pageView.getSize(),pageView.getCurrent(),
//                articlePage.getTotal());
//    }

    @Override
    public PageData<Article> getArticleByChannelId(Integer id, PageView pageView) {
        Page<Article> page = new Page<>();
        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
        MPJLambdaWrapper<Article> wrapper = new MPJLambdaWrapper<Article>();
        wrapper.selectAll(Article.class);

        if(StringUtils.isNotBlank(pageView.getField())){
            wrapper.eq(Article::getChannelId,id);
            if("title".equals(pageView.getField())){
                wrapper.and(wr->wr.like(Article::getTitle,pageView.getKeyword()));
            }else if("summary".equals(pageView.getField())){
                wrapper.and(wr->wr.like(Article::getSummary,pageView.getKeyword()));
            }

        }else if(pageView.getKeyword().startsWith("##")){
            Tag tag = tagMapper.selectOne(new QueryWrapper<Tag>()
                    .eq("name", pageView.getKeyword().substring(2)));
            wrapper.innerJoin(ArticleTag.class,ArticleTag::getArticleId,Article::getId,
                    at->at.eq(ArticleTag::getTagId,tag.getId()));
            wrapper.eq(Article::getChannelId,id);

        }else if(StringUtils.isNotBlank(pageView.getKeyword())){

            wrapper.eq(Article::getChannelId,id);
            wrapper.and(wr->wr.like(Article::getTitle,pageView.getKeyword())
                    .or().like(Article::getSummary,pageView.getKeyword()));
        }else{
            wrapper.eq(Article::getChannelId,id);
        }

        Page<Article> articlePage = articleMapper.selectPage(page,wrapper);
        articlePage.getRecords().forEach(article -> {
            article.setChannel(channelMapper.selectById(article.getChannelId()));
            article.setUser(userMapper.selectById(article.getCreateUser()));
        });
        return PageData.builder(articlePage.getRecords(),
                pageView.getSize(),pageView.getCurrent(),
                articlePage.getTotal());
    }

    @Override
    public List<Article> getTopArticle() {
        List<Article> res = articleMapper.selectList(new QueryWrapper<Article>()
                .eq("top",1)
                .orderByDesc("orderby"));
        res.forEach(article -> {
            article.setChannel(channelMapper.selectById(article.getChannelId()));
            article.setUser(userMapper.selectById(article.getCreateUser()));
        });
        return res;
    }

    @Override
    public List<Article> getRankArticleList() {
        List<Article> res = articleMapper.selectList(new QueryWrapper<Article>()
                .orderByDesc("article_view")
                .last("limit 10"));
        res.forEach(article -> {
            article.setChannel(channelMapper.selectById(article.getChannelId()));
            article.setUser(userMapper.selectById(article.getCreateUser()));
        });
        return res;
    }

    @Override
    public Article getArticleInfoById(Integer id) {
        Article article = articleMapper.selectById(id);
        article.setChannel(channelMapper.selectById(article.getChannelId()));
        article.setUser(userMapper.selectById(article.getCreateUser()));
        return article;
    }

    @Override
    public void articleView(Integer id) {
        User user = ReqInfoContext.getReqInfo().getUser();
        Object value = redisTemplate.opsForValue().get("view_"+user.getId()+"_"+"id");
        if(value!=null) return;
        redisTemplate.opsForValue().set("view_"+user.getId()+"_"+"id","1",86400);
        articleMapper.update(null,new UpdateWrapper<Article>()
                .set("article_view",articleMapper.selectById(id).getArticleView()+1).eq("id",id));
    }

    @Override
    public List<Article> getSelfArticleList(Integer id) {
        Integer uid = id;
        if(uid==null||uid.equals(0)){
            uid = ReqInfoContext.getReqInfo().getUser().getId();
        }
        List<Article> res = articleMapper.selectList(new QueryWrapper<Article>()
                        .eq("create_user",uid)
                        .orderByDesc("create_time"));
        res.forEach(article -> {
            article.setChannel(channelMapper.selectById(article.getChannelId()));
            article.setUser(userMapper.selectById(article.getCreateUser()));
        });
        return res;
    }
}

