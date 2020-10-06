package com.scaler7.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scaler7.common.Result;
import com.scaler7.entity.BlogComment;
import com.scaler7.service.BlogCommentService;
import com.scaler7.vo.BlogCommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
@RestController
@RequestMapping("/blog/comment")
public class BlogCommentController {
    @Autowired
    BlogCommentService blogCommentService;

    @GetMapping("page")
    @ApiOperation(("分页查询评论"))
    public Object findByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            BlogCommentVO blogCommentVO
    ){
        Page<BlogComment> pages = new Page<>(page, limit);
        IPage<BlogComment> pageData = blogCommentService.findCommentByPageBackend(pages,blogCommentVO);
        return new Result(pageData);
    }

    @PutMapping
    @ApiOperation("修改评论状态")
    public Object update(
            @RequestBody BlogComment blogComment
    ){
        blogCommentService.updateById(blogComment);

        return new Result();
    }

    @DeleteMapping("{commentId}")
    @ApiOperation(("删除评论"))
    public Object delete(@PathVariable("commentId") Integer commentId){
        blogCommentService.removeById(commentId);
        return new Result();
    }
}
