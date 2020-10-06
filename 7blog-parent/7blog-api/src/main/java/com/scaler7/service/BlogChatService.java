package com.scaler7.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scaler7.entity.BlogChat;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scaler7.vo.BlogChatVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author scaler7
 * @since 2020-09-12
 */
public interface BlogChatService extends IService<BlogChat> {

   IPage<BlogChat> findChatByPageBackend(IPage page, BlogChatVO blogChatVO);

}
