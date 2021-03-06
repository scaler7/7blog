package com.scaler7.service;

import com.scaler7.entity.BlogVisitor;
import com.scaler7.vo.BlogVisitorVO;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
public interface BlogVisitorService extends IService<BlogVisitor> {

	IPage<BlogVisitor> findByPage(Page<BlogVisitor> page, BlogVisitorVO blogVisitorVO);

	List<BlogVisitor> findTop3VisitorList();

	List<BlogVisitor> findRecentVisitorList(Integer limit);

	BlogVisitor findVisitorByOpenId(String openid);

}
