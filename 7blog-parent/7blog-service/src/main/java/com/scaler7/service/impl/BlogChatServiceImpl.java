package com.scaler7.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.scaler7.entity.BlogChat;
import com.scaler7.mapper.BlogChatMapper;
import com.scaler7.service.BlogChatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.scaler7.vo.BlogChatVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author scaler7
 * @since 2020-09-12
 */
@Service
@Slf4j
public class BlogChatServiceImpl extends ServiceImpl<BlogChatMapper, BlogChat> implements BlogChatService {

    @Autowired
    BlogChatMapper blogChatMapper;

    @Override
    public IPage<BlogChat> findChatByPageBackend(IPage pages, BlogChatVO blogChatVO) {
        log.info("后台管理系统分页查询说说{},{}",pages.getCurrent(),pages.getSize());
        IPage pageData = blogChatMapper.selectPage(pages, new LambdaQueryWrapper<BlogChat>()
                .le(blogChatVO.getStartTime() != null, BlogChat::getCreateTime, blogChatVO.getStartTime())
                .ge(blogChatVO.getEndTime() != null, BlogChat::getCreateTime, blogChatVO.getEndTime())
        );
        return pageData;
    }
}
