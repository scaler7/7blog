package com.scaler7.service.impl;

import com.scaler7.entity.BlogArticle;
import com.scaler7.entity.SysUser;
import com.scaler7.mapper.BlogArticleMapper;
import com.scaler7.service.BlogArticleService;
import com.scaler7.vo.BlogArticleVO;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.apache.shiro.SecurityUtils;
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
	public IPage<BlogArticle> findByPage(Page<BlogArticle> page, BlogArticleVO blogArticleVO) {
		log.info("分页查询文章{},{}", page.getCurrent(), page.getSize());
		IPage<BlogArticle> pageData = blogArticleMapper.selectPage(page, new LambdaQueryWrapper<BlogArticle>()
				.like(StringUtils.hasText(blogArticleVO.getTitle()), BlogArticle::getTitle, blogArticleVO.getTitle())
				.le(blogArticleVO.getEndTime() != null, BlogArticle::getCreateTime, blogArticleVO.getEndTime())
				.ge(blogArticleVO.getStartTime() != null, BlogArticle::getCreateTime, blogArticleVO.getStartTime()));
		return pageData;
	}

	@Override
	public boolean save(BlogArticle entity) {
		Assert.notNull(entity, "要添加的数据不能为null");
		log.info("添加文章");
		entity.setCreateTime(LocalDateTime.now());
		entity.setModifyTime(LocalDateTime.now());
		SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
		entity.setUserId(currentUser.getUserId());
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
		Assert.notNull(articleId,"要删除的articleId不能为null");
		log.info("删除id为{}的article",articleId);
		return super.removeById(articleId);
	}

}
