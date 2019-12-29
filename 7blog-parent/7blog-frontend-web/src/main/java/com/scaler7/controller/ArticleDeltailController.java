package com.scaler7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scaler7.entity.BlogArticle;
import com.scaler7.service.BlogArticleService;

@RequestMapping("articleDetail")
@Controller
public class ArticleDeltailController {
	
	@Autowired
	BlogArticleService blogArticleService;

	@GetMapping("info/{articleId}")
	public String init(Model model,@PathVariable(name = "articleId")Integer articleId) {
		BlogArticle article = blogArticleService.getById(articleId);
		model.addAttribute("article", article);
		return "pages/ArticleDetails";
	}
	
}
