package com.scaler7.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.scaler7.common.Result;
import com.scaler7.entity.BlogChat;
import com.scaler7.service.BlogChatService;
import com.scaler7.vo.BlogChatVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author scaler7
 * @since 2020-09-12
 */
@RestController
@RequestMapping("/blog/chat")
public class BlogChatController {

    @Autowired
    BlogChatService blogChatService;

    @GetMapping("page")
    @ApiOperation(("分页查询说说"))
    public Object findByPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit,
            BlogChatVO blogChatVO
    ){
        Page<BlogChatVO> pages = new Page<>(page, limit);
        IPage<BlogChat> pageData = blogChatService.findChatByPageBackend(pages, blogChatVO);
        return new Result(pageData);
    }

}
