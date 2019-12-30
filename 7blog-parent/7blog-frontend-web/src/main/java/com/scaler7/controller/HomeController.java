package com.scaler7.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scaler7.entity.BlogArticle;
import com.scaler7.entity.BlogComment;
import com.scaler7.entity.BlogVisitor;
import com.scaler7.service.BlogArticleService;
import com.scaler7.service.BlogCommentService;
import com.scaler7.service.BlogVisitorService;

@RequestMapping("")
@Controller
public class HomeController {
	
	@Autowired
	BlogArticleService blogArticleService;
	@Autowired
	BlogCommentService blogCommentService;
	@Autowired
	BlogVisitorService blogVisitorService;

	@RequestMapping("/")
	public String home(Model model) {
		List<BlogArticle> articles = blogArticleService.findArticleList(6);
		List<BlogComment> comments = blogCommentService.findCommentsList(9);
		List<BlogVisitor> top3Visitors = blogVisitorService.findTop3VisitorList();
		List<BlogVisitor> recentVisitors = blogVisitorService.findRecentVisitorList(15);
		model.addAttribute("articles", articles);
		model.addAttribute("comments", comments);
		model.addAttribute("top3Visitors", top3Visitors);
		model.addAttribute("recentVisitors", recentVisitors);
		return "pages/Index";
	}
	
}
