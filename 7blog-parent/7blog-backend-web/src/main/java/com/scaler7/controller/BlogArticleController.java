package com.scaler7.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scaler7.common.Result;
import com.scaler7.entity.BlogArticle;
import com.scaler7.service.BlogArticleService;
import com.scaler7.vo.BlogArticleVO;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
@RestController
@RequestMapping("blog/article")
public class BlogArticleController {
	
	@Autowired
	BlogArticleService blogArticleService;
	
	@GetMapping("page")
	@ApiOperation("分页查询文章")
	public Object findByPage(
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer limit,
			BlogArticleVO blogArticleVO) {
		Page<BlogArticle> pages = new Page<BlogArticle>(page, limit);
		IPage<BlogArticle> pageData = blogArticleService.findArticleByPageBackend(pages,blogArticleVO);
		return new Result(pageData);
	}
	
	@PostMapping
	@ApiOperation("添加文章")
	public Object add(@RequestBody BlogArticle blogArticle) {
		blogArticleService.save(blogArticle);
		return new Result();
	}
	
	@PutMapping
	@ApiOperation("修改文章")
	public Object update(@RequestBody BlogArticle blogArticle) {
		blogArticleService.updateById(blogArticle);
		return new Result();
	}
	
	@DeleteMapping("{articleId}")
	@ApiOperation("删除文章")
	public Object delete(@PathVariable("articleId") Integer articleId) {
		blogArticleService.removeById(articleId);
		return new Result();
	}

	@GetMapping("list")
	@ApiOperation("所有文章列表")
	public Object list(){
		List<BlogArticle> list = blogArticleService.list();
		return new Result(list);
	}
	
}
