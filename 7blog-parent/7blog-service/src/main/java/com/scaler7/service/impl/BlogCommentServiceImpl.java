package com.scaler7.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scaler7.common.Constant;
import com.scaler7.entity.BlogComment;
import com.scaler7.entity.BlogVisitor;
import com.scaler7.mapper.BlogArticleMapper;
import com.scaler7.mapper.BlogCommentMapper;
import com.scaler7.mapper.BlogVisitorMapper;
import com.scaler7.service.BlogCommentService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.scaler7.vo.BlogCommentVO;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author scaler7
 * @param <V>
 * @since 2019-12-21
 */
@Service
@Slf4j
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements BlogCommentService {

	@Autowired
	BlogCommentMapper blogCommentMapper;
	@Autowired
	BlogVisitorMapper blogVisitorMapper;
	@Autowired
	BlogArticleMapper blogArticleMapper;
	
	@Override
	public List<BlogComment> findCommentsList(Integer limit) {
		Assert.notNull(limit, "查询条数不能为空！");
		log.info("查询文章列表,查询条数为{}",limit);
		List<BlogComment> comments = blogCommentMapper.selectList(new LambdaQueryWrapper<BlogComment>()
				.eq(BlogComment::getIsValid,1) // 只查询有效的评论
				.orderByDesc(BlogComment::getCreatedTime)
				.last(limit != null, "limit "+limit)
				);
		if(CollectionUtil.isEmpty(comments)) {
			return new ArrayList<BlogComment>();
		}
		Set<Integer> visitorIds = new HashSet<Integer>();
		for (BlogComment blogComment : comments) {
			visitorIds.add(blogComment.getVisitorId());
		}
		List<BlogVisitor> visitors = blogVisitorMapper.selectList(new LambdaQueryWrapper<BlogVisitor>()
				.eq(BlogVisitor::getIsValid, 1)
				.in(BlogVisitor::getVisitorId, visitorIds)
				);
		for (BlogVisitor visitor : visitors) {
			for (BlogComment comment : comments) {
				if(visitor.getVisitorId() == comment.getVisitorId()) {
					comment.setVisitorName(visitor.getVisitorName());
					comment.setVisitorProfilePhoto(visitor.getProfilePhoto());
					comment.setVisitorPersonalWebsite(visitor.getPersonalWebsite()); // 注入属性
				}
			}
		}
		return comments;
	}
	
	@Override
	public boolean save(BlogComment entity) {
		Assert.notNull(entity, "要添加的数据不能为null");
		log.info("添加评论");
		entity.setCreatedTime(LocalDateTime.now());
		entity.setIsRead(1);
		entity.setIsCheck(1);
		boolean isSuccess = super.save(entity);
		if(isSuccess) {
			blogArticleMapper.increCommentCount(entity.getArticleId()); // 更新文章的评论数量
		}
		return isSuccess;
	}
	
	@Override
	public IPage<BlogComment> findByPageAndArticleId(IPage<BlogComment> page, Integer articleId) {
		Map<SFunction<BlogComment,?>, Object> params = new HashMap<SFunction<BlogComment,?>, Object>();
		//构造查询条件
		params.put(BlogComment::getArticleId, articleId);
		params.put(BlogComment::getIsCheck, 1);
		params.put(BlogComment::getIsRead, 1);
		params.put(BlogComment::getIsValid, 1);
		params.put(BlogComment::getType, Constant.COMMENT_ARTILCE);
		IPage<BlogComment> pageData = blogCommentMapper.selectPage(page, new LambdaQueryWrapper<BlogComment>()
				.allEq(params)
				.eq(BlogComment::getParentId, 0)
				.orderByAsc(BlogComment::getCreatedTime)
				); // 查询所有符合条件的一级评论
		
		List<BlogComment> parentComments = pageData.getRecords();
		
		if(CollectionUtil.isEmpty(parentComments)) {
			return pageData; // 如果一级评论集合为空，则这篇文章没有评论，直接返回
		}
		
		List<Integer> parentIds = new ArrayList<Integer>(parentComments.size()); // 一级评论的id集合
		Set<Integer> visitorIds = new HashSet<Integer>(); // 评论人id集合
		
		for (BlogComment parentComment : parentComments) {
			parentIds.add(parentComment.getCommentId());
			visitorIds.add(parentComment.getVisitorId());
		}
		
		List<BlogComment> childComments = blogCommentMapper.selectList(new LambdaQueryWrapper<BlogComment>()
				.allEq(params)
				.in(BlogComment::getParentId, parentIds)
				); // 查询所有符合条件的子评论
		
		for (BlogComment childComment : childComments) {
			visitorIds.add(childComment.getVisitorId());
		}
		
		List<BlogVisitor> visitors = blogVisitorMapper.selectList(new LambdaQueryWrapper<BlogVisitor>()
				.in(BlogVisitor::getVisitorId, visitorIds)
				); // 该篇文章全部评论人集合
		
		//注入属性
		setAttr(visitors,parentComments,childComments);
		//注入子评论
		setChildComments(parentComments,childComments);
		
	 	return pageData.setRecords(parentComments);
	}

	/**
	 * 
	 * @Description:注入子评论
	 * @param parentComments
	 * @param childComments
	 */
	private void setChildComments(List<BlogComment> parentComments, List<BlogComment> childComments) {
		for (BlogComment parentComment : parentComments) {
			List<BlogComment> childrens = new ArrayList<BlogComment>();
			for (BlogComment childComment : childComments) {
				if(childComment.getParentId() == parentComment.getCommentId()) {
					childrens.add(childComment); //得到子评论
				}
			}
			Collections.sort(childrens, new Comparator<BlogComment>() {
				public int compare(BlogComment v1,BlogComment v2) {
					return v1.getCreatedTime().compareTo(v2.getCreatedTime()); // 按评论时间排序
				}
			});
			parentComment.setChildrens(childrens); // 注入子评论
		}
	}
	/**
	 * 
	 * @Description:注入属性
	 * @param visitors
	 * @param parentComments
	 * @param childComments
	 */
	private void setAttr(List<BlogVisitor> visitors, List<BlogComment> parentComments, List<BlogComment> childComments) {
		for (BlogVisitor visitor : visitors) {
			for (BlogComment parentComment : parentComments) {
				if(parentComment.getVisitorId() == visitor.getVisitorId()) {
					parentComment.setVisitorName(visitor.getVisitorName()); // 注入访客名称
					parentComment.setVisitorProfilePhoto(visitor.getProfilePhoto()); // 注入访客头像
					parentComment.setVisitorPersonalWebsite(visitor.getPersonalWebsite()); // 注入访客网站
				}
			}
			for (BlogComment childComment : childComments) {
				if(childComment.getVisitorId() == visitor.getVisitorId()) {
					childComment.setVisitorName(visitor.getVisitorName()); // 注入访客名称
					childComment.setVisitorProfilePhoto(visitor.getProfilePhoto()); // 注入访客头像
					childComment.setVisitorPersonalWebsite(visitor.getPersonalWebsite()); // 注入访客网站
				}
			}
		}
		
		List<BlogComment> allComments = new ArrayList<BlogComment>();
		allComments.addAll(parentComments);
		allComments.addAll(childComments);
		
		for (BlogComment comment1 : allComments) {
			for (BlogComment comment2 : allComments) {
				if(comment1.getReplyId() == comment2.getCommentId()) {
					comment1.setReplyVisitorName(comment2.getVisitorName()); // 注入回复评论的评论人名称
					comment1.setReplyPersonalWebsite(comment2.getVisitorPersonalWebsite()); // 注入回复评论的评论人网站
				}
			}
		}
	}

	@Override
	public IPage<BlogComment> findByPageBackend(Page<BlogComment> pages, BlogCommentVO blogCommentVO) {
		log.info("分页查询评论{},{}",pages.getCurrent(),pages.getSize());

		/*blogVisitorMapper.selectList(new LambdaQueryWrapper<>()
		.like(StringUtils.hasText(blogCommentVO.getVisitorName()),BlogVisitor::getVisitorName,blogCommentVO.getVisitorName())
		);*/

		IPage<BlogComment> pageData = blogCommentMapper.selectPage(pages, new LambdaQueryWrapper<BlogComment>()
				.eq(blogCommentVO.getArticleId() != null,BlogComment::getArticleId,blogCommentVO.getArticleId())
				.le(blogCommentVO.getStartTime() != null, BlogComment::getCreatedTime, blogCommentVO.getEndTime())
				.ge(blogCommentVO.getEndTime() != null, BlogComment::getCreatedTime, blogCommentVO.getStartTime())
		);

		List<BlogComment> blogComments = pageData.getRecords();
		ArrayList<Integer> visitorIds = new ArrayList<>();

		List<BlogVisitor> blogVisitors = blogVisitorMapper.selectList(new LambdaQueryWrapper<BlogVisitor>()
				.in(!CollectionUtils.isEmpty(visitorIds), BlogVisitor::getVisitorId, visitorIds)
		);

		for (BlogComment blogComment : blogComments) {
			for (BlogVisitor blogVisitor : blogVisitors) {
				if(blogComment.getVisitorId() == blogVisitor.getVisitorId())
					blogComment.setVisitorName(blogVisitor.getVisitorName());
			}
		}

		return pageData;
	}

	@Override
	public boolean updateById(BlogComment blogComment) {
		Assert.notNull(blogComment,"要修改的数据不能为null！");
		Assert.notNull(blogComment.getCommentId(),"commentId不能为null");
		log.info("修改id为{}的comment数据",blogComment.getCommentId());
		return super.updateById(blogComment);
	}

	@Override
	public boolean removeById(Serializable commentId) {
		Assert.notNull(commentId,"要删除的commentId不能为null！");
		log.info("删除id为{}的评论",commentId);
		return super.removeById(commentId);
	}
}
