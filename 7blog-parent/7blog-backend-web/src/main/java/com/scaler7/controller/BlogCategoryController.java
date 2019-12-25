package com.scaler7.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.scaler7.common.Result;
import com.scaler7.entity.BlogCategory;
import com.scaler7.service.BlogCategoryService;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author scaler7
 * @since 2019-12-24
 */
@RestController
@RequestMapping("/blog/category")
public class BlogCategoryController {
	
	@Autowired
	BlogCategoryService blogCategoryService;
	
	@GetMapping("list")
	@ApiOperation("全查询分类")
	public Object list() {
		List<BlogCategory> categories = blogCategoryService.list();
		return new Result(categories);
	}
	
}
