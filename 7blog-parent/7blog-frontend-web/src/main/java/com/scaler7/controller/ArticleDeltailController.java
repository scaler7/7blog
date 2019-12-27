package com.scaler7.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("articleDetail")
@Controller
public class ArticleDeltailController {

	@GetMapping("init")
	public String init() {
		return "pages/ArticleDetails";
	}
	
}
