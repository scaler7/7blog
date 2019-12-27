package com.scaler7.service.impl;

import com.scaler7.entity.BlogComment;
import com.scaler7.mapper.BlogCommentMapper;
import com.scaler7.service.BlogCommentService;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements BlogCommentService {

	@Autowired
	BlogCommentMapper blogCommentMapper;
	
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

}
