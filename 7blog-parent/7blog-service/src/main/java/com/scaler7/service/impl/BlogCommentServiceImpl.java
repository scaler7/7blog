package com.scaler7.service.impl;

import com.scaler7.entity.BlogComment;
import com.scaler7.entity.BlogVisitor;
import com.scaler7.mapper.BlogCommentMapper;
import com.scaler7.mapper.BlogVisitorMapper;
import com.scaler7.service.BlogCommentService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
	
	@Override
	public List<BlogComment> findCommentsList(Integer limit) {
		Assert.notNull(limit, "查询条数不能为空！");
		log.info("查询文章列表,查询条数为{}",limit);
		List<BlogComment> comments = blogCommentMapper.selectList(new LambdaQueryWrapper<BlogComment>()
				.orderByDesc(BlogComment::getCreatedTime)
				.last(limit != null, "limit "+limit)
				);
		return comments;
	}
	
	public IPage<BlogComment> findByPageAndArticleId(IPage<BlogComment> page, Integer articleId) {
		Map<SFunction<BlogComment,?>, Object> params = new HashMap<SFunction<BlogComment,?>, Object>();
		//构造查询条件
		params.put(BlogComment::getArticleId, articleId);
		params.put(BlogComment::getIsCheck, 1);
		params.put(BlogComment::getIsRead, 1);
		params.put(BlogComment::getIsValid, 1);
		IPage<BlogComment> pageData = blogCommentMapper.selectPage(page, new LambdaQueryWrapper<BlogComment>()
				.allEq(params)
				.eq(BlogComment::getParentId, 0)
				.orderByDesc(BlogComment::getCreatedTime)
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
					return v2.getCreatedTime().compareTo(v1.getCreatedTime()); // 按评论时间排序
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
				}
			}
			for (BlogComment childComment : childComments) {
				if(childComment.getVisitorId() == visitor.getVisitorId()) {
					childComment.setVisitorName(visitor.getVisitorName()); // 注入访客名称
					childComment.setVisitorProfilePhoto(visitor.getProfilePhoto()); // 注入访客头像
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
				}
			}
		}
	}

}
