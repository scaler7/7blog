package com.scaler7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scaler7.common.Result;
import com.scaler7.entity.BlogComment;
import com.scaler7.service.BlogCommentService;

@RequestMapping("comment")
@Controller
public class CommentController {
	
	@Autowired
	BlogCommentService blogCommentService;

	@GetMapping("{articleId}")
	@ResponseBody
	public Object findByPage(
			@RequestParam(defaultValue = "1")Integer current,
			@RequestParam(defaultValue = "10")Integer size,
			@PathVariable(name = "articleId")Integer articleId) {
		IPage<BlogComment> page = new Page<BlogComment>(current, size);
		IPage<BlogComment> pageData = blogCommentService.findByPageAndArticleId(page,articleId);
		return new Result(pageData);
	}
	
	@PostMapping
	@ResponseBody
	public Object add(@RequestBody BlogComment comment) {
		blogCommentService.save(comment);
		return new Result();
	}
	
}
