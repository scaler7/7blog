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
import com.scaler7.entity.BlogRecord;
import com.scaler7.service.BlogRecordService;
import com.scaler7.vo.BlogRecordVO;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author scaler7
 * @since 2020-01-01
 */
@RestController
@RequestMapping("/blog/record")
public class BlogRecordController {
	
	@Autowired
	BlogRecordService blogRecordService;
	
	@GetMapping("page")
	@ApiOperation("分页查询记录")
	public Object findByPage(
			@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "10") Integer limit,
			BlogRecordVO blogRecordVO) {
		IPage<BlogRecord> pages = new Page<BlogRecord>(page,limit);
		IPage<BlogRecord> pageData = blogRecordService.findByPage(pages,blogRecordVO);
		return new Result(pageData);
	}
	
	@PostMapping
	@ApiOperation("新增记录")
	public Object save(@RequestBody BlogRecord blogRecord) {
		blogRecordService.save(blogRecord);
		return new Result();
	}
	
	@PutMapping
	@ApiOperation("修改记录状态")
	public Object update(@RequestBody BlogRecord blogRecord) {
		blogRecordService.updateById(blogRecord);
		return new Result();
	}
	
	@DeleteMapping("{recordId}")
	@ApiOperation("批量删除记录")
	public Object delete(@PathVariable(name = "recordId")Integer recordId) {
		blogRecordService.removeById(recordId);
		return new Result();
	}
	
}
