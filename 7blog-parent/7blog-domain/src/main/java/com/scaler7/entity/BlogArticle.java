package com.scaler7.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;

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
@ApiModel(value="BlogArticle对象", description="")
public class BlogArticle implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "article_id", type = IdType.AUTO)
    private Integer articleId;

    @ApiModelProperty(value = "发表人id")
    private Integer userId;

    @ApiModelProperty(value = "分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "标题")
    private String title;
    
    @ApiModelProperty(value = "文章链接")
    private String href;
    
    @ApiModelProperty(value = "文章图片链接")
    private String articleImg;

    @ApiModelProperty(value = "markdown内容")
    private String contentMd;
    
    @ApiModelProperty(value = "html内容")
    private String contentHtml;

    @ApiModelProperty(value = "浏览量")
    private Integer pageView;

    @ApiModelProperty(value = "评论量")
    private Integer commentCount;

    @ApiModelProperty(value = "发表时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime modifyTime;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "排序码")
    private Integer sort;
    
    @ApiModelProperty(value = "是否允许评论")
    private Integer allowComment = 0;

    @ApiModelProperty(value = "是否置顶 1置顶0不置顶")
    private Integer isTop = 0;

    @ApiModelProperty(value = "是否有效 1有效0无效")
    private Integer isValid = 0;
    
    @TableField(exist = false)
    @ApiModelProperty(value = "发布人名称")
    private String userName;
    
    @TableField(exist = false)
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
    

}
