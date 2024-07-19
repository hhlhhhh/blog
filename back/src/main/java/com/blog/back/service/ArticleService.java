package com.blog.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.Article;
import com.blog.back.POJO.entity.Tag;
import com.blog.back.framework.resp.PageData;

import java.util.List;

public interface ArticleService extends IService<Article> {

    public int saveArticle(Article article);

    public int delete(Integer id);

    public PageData<Article> query(PageView pageView);

    public PageData<Article> queryOfAdmin(PageView pageView);

    public List<Tag> getTagByArticleId(Integer id);

    public PageData<Article> getArticleByChannelId(Integer id,PageView pageView);

    public List<Article> getTopArticle();

    public List<Article> getRankArticleList();

    public Article getArticleInfoById(Integer id);

    public void articleView(Integer id);

    public List<Article> getSelfArticleList(Integer id);

}
