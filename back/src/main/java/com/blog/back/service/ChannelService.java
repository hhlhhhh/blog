package com.blog.back.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.Channel;
import com.blog.back.framework.resp.PageData;

import java.util.List;

public interface ChannelService extends IService<Channel> {

    public int create(Channel channel);

    public int delete(Integer id);

    public int update(Channel channel);

    public PageData<Channel> getChannelList(PageView pageView);

    public List<Channel> getNavChannel();

    public List<Channel> getShowChannel(String pos);
}
