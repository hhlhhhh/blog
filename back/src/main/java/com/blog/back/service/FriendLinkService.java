package com.blog.back.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.FriendLink;
import com.blog.back.framework.resp.PageData;


public interface FriendLinkService extends IService<FriendLink> {

    public int create(FriendLink friendLink);

    public int delete(Integer id);

    public int update(FriendLink friendLink);

    public PageData<FriendLink> getFriendLinkList(PageView pageView);
}
