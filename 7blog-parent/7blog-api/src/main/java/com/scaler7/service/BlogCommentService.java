package com.scaler7.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scaler7.entity.BlogComment;
import java.util.List;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.scaler7.vo.BlogCommentVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
public interface BlogCommentService extends IService<BlogComment> {

	List<BlogComment> findCommentsList(Integer limit);

	IPage<BlogComment> findByPageAndArticleId(IPage<BlogComment> page, Integer articleId);

	IPage<BlogComment> findCommentByPageBackend(Page<BlogComment> pages, BlogCommentVO blogCommentVO);
}
