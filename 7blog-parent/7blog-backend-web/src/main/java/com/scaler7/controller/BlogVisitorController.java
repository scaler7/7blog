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
import com.scaler7.entity.BlogVisitor;
import com.scaler7.service.BlogVisitorService;
import com.scaler7.vo.BlogVisitorVO;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
@RestController
@RequestMapping("/blog/visitor")
public class BlogVisitorController {

	@Autowired
	BlogVisitorService blogVisitorService;
	
	@GetMapping("page")
	@ApiOperation("分页查询访客")
	public Object findByPage(
			@RequestParam(defaultValue = "1") Integer current,
			@RequestParam(defaultValue = "10") Integer size,
			BlogVisitorVO blogVisitorVO) {
		Page<BlogVisitor> page = new Page<BlogVisitor>(current, size);
		IPage<BlogVisitor> pageData = blogVisitorService.findByPage(page,blogVisitorVO);
		return new Result(pageData);
	}
	
	@PostMapping
	@ApiOperation("添加访客")
	public Object add(@RequestBody BlogVisitor blogVisitor) {
		blogVisitorService.save(blogVisitor);
		return new Result();
	}
	
	@PutMapping
	@ApiOperation("修改访客状态")
	public Object update(@RequestBody BlogVisitor blogVisitor) {
		blogVisitorService.updateById(blogVisitor);
		return new Result();
	}
	
	@DeleteMapping("{articleId}")
	@ApiOperation("删除访客")
	public Object delete(@PathVariable("articleId") Integer articleId) {
		blogVisitorService.removeById(articleId);
		return new Result();
	}
	
}
