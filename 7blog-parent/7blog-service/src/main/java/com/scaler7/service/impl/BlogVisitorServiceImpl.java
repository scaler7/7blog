package com.scaler7.service.impl;

import com.scaler7.entity.BlogVisitor;
import com.scaler7.mapper.BlogVisitorMapper;
import com.scaler7.service.BlogVisitorService;
import com.scaler7.vo.BlogVisitorVO;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
@Service
@Slf4j
public class BlogVisitorServiceImpl extends ServiceImpl<BlogVisitorMapper, BlogVisitor> implements BlogVisitorService {
	
	@Autowired
	BlogVisitorMapper blogVisitorMapper;

	@Override
	public IPage<BlogVisitor> findByPage(Page<BlogVisitor> page, BlogVisitorVO blogVisitorVO) {
		log.info("分页查询访客{},{}", page.getCurrent(), page.getSize());
		IPage<BlogVisitor> pageData = blogVisitorMapper.selectPage(page, new LambdaQueryWrapper<BlogVisitor>()
				.like(StringUtils.hasText(blogVisitorVO.getVisitorName()), BlogVisitor::getVisitorName, blogVisitorVO.getVisitorName())
				.le(blogVisitorVO.getEndTime() != null, BlogVisitor::getLastLoginTime, blogVisitorVO.getEndTime())
				.ge(blogVisitorVO.getStartTime() != null, BlogVisitor::getLastLoginTime, blogVisitorVO.getStartTime())
				);
		return pageData;
	}
	
	@Override
	public boolean save(BlogVisitor entity) {
		Assert.notNull(entity, "要添加的数据不能为null");
		log.info("添加访客");
		entity.setRegistTime(LocalDateTime.now());
		return super.save(entity);
	}

	@Override
	public boolean updateById(BlogVisitor entity) {
		Assert.notNull(entity, "要修改的数据不能为null");
		Assert.notNull(entity.getVisitorId(), "visitorId不能为null");
		log.info("修改id为{}的visitor数据", entity.getVisitorId());
		return super.updateById(entity);
	}

	@Override
	public boolean removeById(Serializable visitorId) {
		Assert.notNull(visitorId,"要删除的visitorId不能为null");
		log.info("删除id为{}的visitor",visitorId);
		return super.removeById(visitorId);
	}

}
