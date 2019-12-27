package com.scaler7.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scaler7.common.Result;
import com.scaler7.entity.BlogArticle;
import com.scaler7.entity.BlogCategory;
import com.scaler7.service.BlogArticleService;
import com.scaler7.service.BlogCategoryService;

@RequestMapping("article")
@Controller
public class ArticleController {

	@Autowired
	BlogArticleService blogArticleService;
	@Autowired
	BlogCategoryService blogCategoryService;
	
	@GetMapping("init")
	public String articleIndex(Model model) {
		
		List<BlogCategory> categories = blogCategoryService.findCategoryList();
		List<BlogArticle> hotArticles = blogArticleService.findHotArticle(8);
		List<BlogArticle> recentArticles = blogArticleService.findRecentArticle(10);
		model.addAttribute("categories", categories);
		model.addAttribute("hotArticles", hotArticles);
		model.addAttribute("recentArticles", recentArticles);
		
		return "pages/Article";
	}
	
	@GetMapping("page")
	@ResponseBody
	public Object page(
			@RequestParam(defaultValue = "1")Integer current,
			@RequestParam(defaultValue = "5")Integer size,
			Integer categoryId
			) {
		Page<BlogArticle> page = new Page<BlogArticle>(current, size);
		BlogArticle blogArticle = new BlogArticle();
		blogArticle.setCategoryId(categoryId);
		IPage<BlogArticle> pageData = blogArticleService.findByPageFrontend(page,blogArticle);
		return new Result(pageData);
	}
	
}
