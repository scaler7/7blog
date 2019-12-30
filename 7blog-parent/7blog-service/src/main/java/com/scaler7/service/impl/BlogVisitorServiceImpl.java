package com.scaler7.service.impl;

import com.scaler7.entity.BlogComment;
import com.scaler7.entity.BlogVisitor;
import com.scaler7.mapper.BlogCommentMapper;
import com.scaler7.mapper.BlogVisitorMapper;
import com.scaler7.service.BlogVisitorService;
import com.scaler7.vo.BlogCommentVO;
import com.scaler7.vo.BlogVisitorVO;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
	@Autowired
	BlogCommentMapper blogCommentMapper;

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

	@Override
	public List<BlogVisitor> findTop3VisitorList() {
		log.info("查询评论数最高的top3访客");
		List<BlogCommentVO> commentVOs = blogCommentMapper.selectTop3VisitorsByCommentCount();
		List<Integer> visitorIds = new ArrayList<Integer>();
		for (BlogComment blogComment  : commentVOs) {
			visitorIds.add(blogComment.getVisitorId());
		}
		List<BlogVisitor> top3Visitors = blogVisitorMapper.selectList(new LambdaQueryWrapper<BlogVisitor>()
				.in(CollectionUtil.isNotEmpty(visitorIds), BlogVisitor::getVisitorId, visitorIds)
				);
		for (BlogVisitor blogVisitor : top3Visitors) {
			for (BlogCommentVO commentVO : commentVOs) {
				if(blogVisitor.getVisitorId().equals(commentVO.getVisitorId())) {
					blogVisitor.setCommentCount(commentVO.getCommentCount());
				}
			}
		}
		Collections.sort(top3Visitors, new Comparator<BlogVisitor>() {
			public int compare(BlogVisitor v1,BlogVisitor v2) {
				if(v1.getCommentCount() == null || v2.getCommentCount() == null) {
					return 0;
				}
				return v2.getCommentCount() - v1.getCommentCount();
			}
		}); // 将集合按照评论数量降序排列
		return top3Visitors;
	}

	@Override
	public List<BlogVisitor> findRecentVisitorList(Integer limit) {
		Assert.notNull(limit, "limit不能为null");
		log.info("按照访问时间查询最近{}个访客",limit);
		return blogVisitorMapper.selectList(new LambdaQueryWrapper<BlogVisitor>()
				.orderByDesc(BlogVisitor::getLastLoginTime)
				.last("limit "+limit)
				);
	}

	@Override
	public BlogVisitor findVisitorByOpenId(String openid) {
		Assert.notNull(openid, "openID不能为null");
		log.info("根据openId:{}查询用户",openid);
		return blogVisitorMapper.selectOne(new LambdaQueryWrapper<BlogVisitor>()
				.eq(BlogVisitor::getOpenId, openid)
				);
	}

}
