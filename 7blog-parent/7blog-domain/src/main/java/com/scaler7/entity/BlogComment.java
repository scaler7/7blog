package com.scaler7.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="BlogComment对象", description="")
public class BlogComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论id")
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer commentId;

    @ApiModelProperty(value = "评论者id")
    private Integer visitorId;

    @ApiModelProperty(value = "文章id")
    private Integer articleId;
    
    @ApiModelProperty(value = "文章id")
    private Integer chatId;

    @ApiModelProperty(value = "父评论id")
    private Integer parentId;
    
    @ApiModelProperty(value = "回复的评论id")
    private Integer replyId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "评论时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:SS",timezone = "GMT+8")
    private LocalDateTime createdTime;
    
    @ApiModelProperty(value = "1文章评论2说说评论3网站留言")
    private Integer type;

    @ApiModelProperty(value = "审核通过1通过0未通过")
    private Integer isCheck;

    @ApiModelProperty(value = "是否已读1已读0未读")
    private Integer isRead;

    @ApiModelProperty(value = "是否有效1有效0失效")
    private Integer isValid;

    @ApiModelProperty(value = "评论人名称")
    @TableField(exist = false)
    private String visitorName;
    
    @ApiModelProperty(value = "回复评论的评论人名称")
    @TableField(exist = false)
    private String replyVisitorName;
    
    @ApiModelProperty(value = "回复评论的评论人网站")
    @TableField(exist = false)
    private String replyPersonalWebsite;
    
    @ApiModelProperty(value = "评论人头像")
    @TableField(exist = false)
    private String visitorProfilePhoto;
    
    @ApiModelProperty(value = "评论人网站")
    @TableField(exist = false)
    private String visitorPersonalWebsite;
    
    @ApiModelProperty(value = "子评论集合")
    @TableField(exist = false)
    private List<BlogComment> childrens;

}
