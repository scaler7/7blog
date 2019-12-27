package com.scaler7.service;

import com.scaler7.entity.BlogArticle;
import com.scaler7.vo.BlogArticleVO;
import java.util.List;
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

	IPage<BlogArticle> findByPageBackend(Page<BlogArticle> page, BlogArticleVO blogArticle);
	
	List<BlogArticle> findArticleList(Integer limit);

	List<BlogArticle> findHotArticle(Integer limit);

	List<BlogArticle> findRecentArticle(Integer limit);

	IPage<BlogArticle> findByPageFrontend(Page<BlogArticle> page, BlogArticle blogArticle);

}
