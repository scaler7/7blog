package com.scaler7.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scaler7.entity.BlogComment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlogCommentVO extends BlogComment {/**
	 * 
	 */
	private static final long serialVersionUID = -735892728782672636L;
	
	private Integer commentCount;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private LocalDateTime startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private LocalDateTime endTime;
	
}
