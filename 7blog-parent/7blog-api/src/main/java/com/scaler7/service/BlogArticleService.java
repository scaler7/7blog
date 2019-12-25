package com.scaler7.service;

import com.scaler7.entity.BlogArticle;
import com.scaler7.vo.BlogArticleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
public interface BlogArticleService extends IService<BlogArticle> {

	IPage<BlogArticle> findByPage(Page<BlogArticle> page, BlogArticleVO blogArticle);

}
