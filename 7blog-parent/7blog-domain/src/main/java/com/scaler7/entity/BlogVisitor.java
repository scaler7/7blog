package com.scaler7.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

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
@ApiModel(value="BlogVisitor对象", description="")
public class BlogVisitor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "访客ID")
    @TableId(value = "visitor_id", type = IdType.AUTO)
    private Long visitorId;

    @ApiModelProperty(value = "访客IP")
    private String ip;

    @ApiModelProperty(value = "访客名")
    private String visitorName;

    @ApiModelProperty(value = "访客网站")
    private String personalWebsite;

    @ApiModelProperty(value = "访客邮箱")
    private String email;

    @ApiModelProperty(value = "qq号")
    private String qqAccount;

    @ApiModelProperty(value = "微信账号")
    private String wechatAccount;

    @ApiModelProperty(value = "用户头像")
    private String profilePhoto;

    @ApiModelProperty(value = "注册时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime registTime;

    @ApiModelProperty(value = "访客上次登录时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "访客生日")
    private LocalDate birth;

    @ApiModelProperty(value = "访客年龄")
    private Integer age;

    @ApiModelProperty(value = "访客昵称")
    private String nickname;

    @ApiModelProperty(value = "身份id")
    private Integer identity;

    @ApiModelProperty(value = "是否允许发送通知")
    private Integer allowInform = 0;

    @ApiModelProperty(value = "是否有效")
    private Integer isValid = 0;


}
