package com.scaler7.mapper;

import com.scaler7.entity.BlogComment;
import com.scaler7.vo.BlogCommentVO;
import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
public interface BlogCommentMapper extends BaseMapper<BlogComment> {
	
	public List<BlogCommentVO> selectTop3VisitorsByCommentCount();

}
