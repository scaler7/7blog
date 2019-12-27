package com.scaler7.service.impl;

import com.scaler7.entity.BlogCategory;
import com.scaler7.mapper.BlogArticleMapper;
import com.scaler7.mapper.BlogCategoryMapper;
import com.scaler7.service.BlogCategoryService;
import com.scaler7.vo.BlogArticleVO;

import lombok.extern.slf4j.Slf4j;

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
 * @since 2019-12-24
 */
@Service
@Slf4j
public class BlogCategoryServiceImpl extends ServiceImpl<BlogCategoryMapper, BlogCategory> implements BlogCategoryService {

	@Autowired
	BlogCategoryMapper blogCategoryMapper; 
	@Autowired
	BlogArticleMapper blogArticleMapper;
	
	@Override
	public List<BlogCategory> findCategoryList() {
		log.info("查询所有分类，并且统计每个分类下的文章数量");
		List<BlogArticleVO> articleVOs = blogArticleMapper.selectCountGroupByCategory();
		List<BlogCategory> categories = blogCategoryMapper.selectList(null);
		for (BlogCategory blogCategory : categories) {
			for (BlogArticleVO articleVO : articleVOs) {
				if(blogCategory.getCategoryId() == articleVO.getCategoryId()) {
					blogCategory.setArticleCount(articleVO.getArticleCount());
				}
			}
		}
		return categories;
	}

}
