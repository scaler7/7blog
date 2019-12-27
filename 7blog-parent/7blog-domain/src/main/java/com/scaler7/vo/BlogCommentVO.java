package com.scaler7.vo;

import com.scaler7.entity.BlogComment;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BlogCommentVO extends BlogComment {/**
	 * 
	 */
	private static final long serialVersionUID = -735892728782672636L;
	
	private Integer commentCount;
	
}
