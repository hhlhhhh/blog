package com.blog.back.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.back.POJO.VO.PageView;
import com.blog.back.POJO.entity.FriendLink;
import com.blog.back.framework.exception.ParamException;
import com.blog.back.framework.resp.PageData;
import com.blog.back.mapper.FriendLinkMapper;
import com.blog.back.service.FriendLinkService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    @Autowired
    private FriendLinkMapper friendLinkMapper;


    public int create(FriendLink friendLink){
        return friendLinkMapper.insert(friendLink);
    }

    public int delete(Integer id){
        return friendLinkMapper.deleteById(id);
    }

    public int update(FriendLink friendLink){
        if(friendLink==null){
            throw new ParamException("未传入参数！");
        }else if(friendLink.getId()==null||friendLink.getId()==0){
            throw new ParamException("未指定友情链接ID！");
        }
        return friendLinkMapper.updateById(friendLink);
    }

    public PageData<FriendLink> getFriendLinkList(PageView pageView){
        Page<FriendLink> page = new Page<>();
        page.setCurrent(pageView.getCurrent()).setSize(pageView.getSize());
        QueryWrapper<FriendLink> wrapper = new QueryWrapper<>();
        if(!StringUtils.isBlank(pageView.getField())) {
            wrapper.like(pageView.getField(), pageView.getKeyword());
        }else if(StringUtils.isNotBlank(pageView.getKeyword())){
            wrapper.like("title", pageView.getKeyword()).or()
                    .like("url",pageView.getKeyword());
        }
        Page<FriendLink> friendLinkPage = friendLinkMapper.selectPage(page,wrapper);
        return PageData.builder(friendLinkPage.getRecords(),
                pageView.getSize(),pageView.getCurrent(),
                friendLinkPage.getTotal());
    }

}
