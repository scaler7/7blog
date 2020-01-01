package com.scaler7.service;

import com.scaler7.entity.BlogRecord;
import com.scaler7.vo.BlogRecordVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author scaler7
 * @since 2020-01-01
 */
public interface BlogRecordService extends IService<BlogRecord> {

	IPage<BlogRecord> findByPage(IPage<BlogRecord> pages, BlogRecordVO blogRecordVO);

}
