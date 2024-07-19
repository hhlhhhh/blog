package com.blog.back.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.Article;
import com.blog.back.POJO.entity.ArticleTag;
import com.blog.back.POJO.entity.Channel;
import com.blog.back.POJO.entity.Comment;
import com.blog.back.framework.resp.PageData;
import com.blog.back.mapper.ArticleMapper;
import com.blog.back.mapper.ArticleTagMapper;
import com.blog.back.mapper.ChannelMapper;
import com.blog.back.mapper.CommentMapper;
import com.blog.back.service.ChannelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, Channel> implements ChannelService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ChannelMapper channelMapper;


    public int create(Channel channel){
        return channelMapper.insert(channel);
    }

    @Transactional
    public int delete(Integer id){
        List<Article> articleList = articleMapper.selectList(new QueryWrapper<Article>().eq("channel_id", id));
        articleList.forEach(article -> {
            articleMapper.deleteById(article.getId());
            articleTagMapper.delete(new QueryWrapper<ArticleTag>().eq("article_id",article.getId()));
            commentMapper.delete(new QueryWrapper<Comment>().eq("article_id",article.getId()));
        });
        return channelMapper.deleteById(id);
    }

    public int update(Channel channel){
        return channelMapper.updateById(channel);
    }

    public PageData<Channel> getChannelList(PageView pageView){
        Page<Channel> page = new Page<>();
        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
        QueryWrapper<Channel> wrapper = new QueryWrapper<>();
        if(!StringUtils.isBlank(pageView.getField())){
            wrapper.like(pageView.getField(),pageView.getKeyword());
        }
        Page<Channel> channelPage = channelMapper.selectPage(page,wrapper);
        List<Channel> list = channelPage.getRecords();

        return PageData.builder(list,
                pageView.getSize(),pageView.getCurrent(),
                channelPage.getTotal());
    }

    @Override
    public List<Channel> getNavChannel() {
        List<Channel> channelList = channelMapper.selectList(new QueryWrapper<Channel>().eq("pos", "A").eq("parent_id",0).last("limit 5"));
        channelList.forEach(channel->{
            channel.setChannelList(channelMapper.selectList(new QueryWrapper<Channel>().eq("parent_id",channel.getId())));
        });
        return channelList;
    }

    @Override
    public List<Channel> getShowChannel(String pos) {
        if(!isChannelPosExit(pos)) return new ArrayList<>();
        List<Channel> channelList = channelMapper.selectList(new QueryWrapper<Channel>().eq("pos", pos));
        return channelList;
    }

    private static List<String> channelPos = Arrays.asList("A","B","C");

    private boolean isChannelPosExit(String pos){
        return channelPos.contains(pos);
    }


}
