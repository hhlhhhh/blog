package com.blog.back.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.Article;
import com.blog.back.POJO.entity.User;
import com.blog.back.framework.context.ReqInfoContext;
import com.blog.back.framework.resp.PageData;
import com.blog.back.framework.resp.Result;
import com.blog.back.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 博客添加
     * @param article
     * @return
     */
    @PostMapping("/save")
    public Result saveArticle(@RequestBody Article article){
        articleService.saveArticle(article);
        return Result.success(article);
    }

    /**
     * 博客删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        Article article = articleService.getById(id);
        User user = ReqInfoContext.getReqInfo().getUser();
        if((!user.getId().equals(article.getCreateUser())&&(!user.getAccount().equals("1111111111")))){
            return Result.fail();
        }
        articleService.delete(id);
        return Result.success();
    }

    //修改文章评论状态、置顶状态
    @PutMapping("/status/admin")
    public Result updateStatus(@RequestBody Article article){
        articleService.update(new UpdateWrapper<Article>()
                .set("comment_status",article.getCommentStatus())
                .set("top",(article.getTop()==1?1:0))
                .eq("id",article.getId()));
        return Result.success();
    }

    //前台专用
    @PostMapping("/query")
    public Result query(@RequestBody PageView pageView){
        PageData<Article> pageInfo = articleService.query(pageView);
        return Result.success(pageInfo);
    }

    //后台专用 与前台差距在会查询所有博客，前台非搜索不查询置顶，因为非搜索模式会有专门接口获取置顶博客
    @PostMapping("/query/admin")
    public Result queryOfAdmin(@RequestBody PageView pageView){
        PageData<Article> pageInfo = articleService.queryOfAdmin(pageView);
        return Result.success(pageInfo);
    }

    /**
     * 根据文章id获取该文章的tag
     * @param id
     * @return
     */
    @GetMapping("/tag/{id}")
    public Result getTagByArticleId(@PathVariable("id")Integer id){
        return Result.success(articleService.getTagByArticleId(id));
    }

    /**
     * 在专栏内搜索
     * @param id    专栏id
     * @param pageView
     * @return
     */
    @PostMapping("/query/{id}")
    public Result getArticleByChannelId(@PathVariable("id")Integer id,@RequestBody PageView pageView){
        PageData<Article> pageInfo = articleService.getArticleByChannelId(id,pageView);
        return Result.success(pageInfo);
    }

    /**
     * 获取置顶文章
     */
    @GetMapping("/top")
    public Result getTopArticle(){
        return Result.success(articleService.getTopArticle());
    }

    /**
     * 获取前十的博客
     * @return
     */
    @GetMapping("/rank")
    public Result getRankArticleList(){
        return Result.success(articleService.getRankArticleList());
    }

    /**
     * 获取某个博客的详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getArticleInfo(@PathVariable("id") Integer id){
        return Result.success(articleService.getArticleInfoById(id));
    }

    /**
     *  文章浏览数
     * @param id
     * @return
     */
    @PostMapping("/view/{id}")
    public Result articleView(@PathVariable("id") Integer id){
        articleService.articleView(id);
        return Result.success();
    }

    /**
     * 获取自己或者某个人的博客
     * @param id 需要查询的用户id
     * @return
     */
    @GetMapping("/self")
    public Result getSelfArticleList(@RequestParam(value = "id",required = false) Integer id){
        return Result.success(articleService.getSelfArticleList(id));
    }

}
