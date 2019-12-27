package com.scaler7.mapper;

import com.scaler7.entity.BlogArticle;
import com.scaler7.vo.BlogArticleVO;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
public interface BlogArticleMapper extends BaseMapper<BlogArticle> {
	
	public List<BlogArticleVO> selectCountGroupByCategory();
	
}
