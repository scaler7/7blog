package com.scaler7.service.impl;

import com.scaler7.common.Constant;
import com.scaler7.entity.BlogArticle;
import com.scaler7.entity.SysUser;
import com.scaler7.mapper.BlogArticleMapper;
import com.scaler7.service.BlogArticleService;
import com.scaler7.utils.WebUtil;
import com.scaler7.vo.BlogArticleVO;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
@Service
@Slf4j
public class BlogArticleServiceImpl extends ServiceImpl<BlogArticleMapper, BlogArticle> implements BlogArticleService {

	@Autowired
	BlogArticleMapper blogArticleMapper;

	@Override
	public IPage<BlogArticle> findByPageBackend(Page<BlogArticle> page, BlogArticleVO blogArticleVO) {
		log.info("后端管理分页查询文章{},{}", page.getCurrent(), page.getSize());
		IPage<BlogArticle> pageData = blogArticleMapper.selectPage(page, new LambdaQueryWrapper<BlogArticle>()
				.like(StringUtils.hasText(blogArticleVO.getTitle()), BlogArticle::getTitle, blogArticleVO.getTitle())
				.le(blogArticleVO.getEndTime() != null, BlogArticle::getCreateTime, blogArticleVO.getEndTime())
				.ge(blogArticleVO.getStartTime() != null, BlogArticle::getCreateTime, blogArticleVO.getStartTime()));
		return pageData;
	}

	public List<BlogArticle> findArticleList(Integer limit) {
		Assert.notNull(limit, "查询条数不能为空！");
		log.info("查询文章列表,查询条数为{}",limit);
		List<BlogArticle> articles = blogArticleMapper.selectList(new LambdaQueryWrapper<BlogArticle>()
				.orderByDesc(BlogArticle::getPageView)
				.last(limit != null, "limit "+limit)
				);
		return articles;
	}

	@Override
	public boolean save(BlogArticle entity) {
		Assert.notNull(entity, "要添加的数据不能为null");
		log.info("添加文章");
		entity.setCreateTime(LocalDateTime.now());
		entity.setModifyTime(LocalDateTime.now());
		SysUser currentUser = (SysUser) WebUtil.getSession().getAttribute(Constant.CURRENT_USER);
		entity.setUserId(currentUser.getUserId());
		entity.setHref("#");
		return super.save(entity);
	}

	@Override
	public boolean updateById(BlogArticle entity) {
		Assert.notNull(entity, "要修改的数据不能为null");
		Assert.notNull(entity.getArticleId(), "articleId不能为null");
		log.info("修改id为{}的article数据", entity.getArticleId());
		entity.setModifyTime(LocalDateTime.now());
		return super.updateById(entity);
	}

	@Override
	public boolean removeById(Serializable articleId) {
		Assert.notNull(articleId, "要删除的articleId不能为null");
		log.info("删除id为{}的article", articleId);
		return super.removeById(articleId);
	}

	@Override
	public List<BlogArticle> findHotArticle(Integer limit) {
		log.info("查询热门文章,数量为{}",limit);
		return blogArticleMapper.selectList(new LambdaQueryWrapper<BlogArticle>()
				.orderByDesc(BlogArticle::getPageView)
				.last("limit "+limit)
				);
	}

	@Override
	public List<BlogArticle> findRecentArticle(Integer limit) {
		log.info("查询最近发布的文章，数量为{}",limit);
		return blogArticleMapper.selectList(new LambdaQueryWrapper<BlogArticle>()
				.orderByDesc(BlogArticle::getCreateTime)
				.last("limit "+limit)
				);
	}

	@Override
	public IPage<BlogArticle> findByPageFrontend(Page<BlogArticle> page, BlogArticle blogArticle) {
		log.info("博客前端分页查询文章{},{}",page.getCurrent(),page.getSize());
		return blogArticleMapper.selectPage(page, new LambdaQueryWrapper<BlogArticle>()
				.eq(null != blogArticle.getCategoryId(), BlogArticle::getCategoryId, blogArticle.getCategoryId())
				);
	}

}
