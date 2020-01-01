package com.scaler7.service.impl;

import com.scaler7.entity.BlogRecord;
import com.scaler7.mapper.BlogRecordMapper;
import com.scaler7.service.BlogRecordService;
import com.scaler7.vo.BlogRecordVO;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author scaler7
 * @since 2020-01-01
 */
@Service
@Slf4j
public class BlogRecordServiceImpl extends ServiceImpl<BlogRecordMapper, BlogRecord> implements BlogRecordService {

	@Autowired
	BlogRecordMapper blogRecordMapper;

	@Override
	public IPage<BlogRecord> findByPage(IPage<BlogRecord> pages, BlogRecordVO blogRecordVO) {
		Assert.notNull(pages, "page不能为null");
		log.info("分页查询记录{},{}", pages.getCurrent(), pages.getSize());
		return blogRecordMapper.selectPage(pages,new LambdaQueryWrapper<BlogRecord>()
						.le(blogRecordVO.getEndTime() != null, BlogRecord::getRecordDate, blogRecordVO.getEndTime())
						.ge(blogRecordVO.getStartTime() != null, BlogRecord::getRecordDate, blogRecordVO.getStartTime())
						.like(StringUtils.hasText(blogRecordVO.getKeyWords()), BlogRecord::getDetails,
								blogRecordVO.getKeyWords()));
	}

	@Override
	public boolean save(BlogRecord entity) {
		Assert.notNull(entity, "新增记录不能为null");
		log.info("新增记录{}", entity);
		return super.save(entity);
	}

	@Override
	public boolean updateById(BlogRecord entity) {
		Assert.notNull(entity, "更新记录不能为null");
		Assert.notNull(entity.getRecordId(), "更新记录id不能为null");
		log.info("更新记录{}", entity);
		return super.updateById(entity);
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		Assert.notNull(idList, "删除记录id集合不能为null");
		log.info("删除记录{}", idList);
		return super.removeByIds(idList);
	}

	@Override
	public List<BlogRecord> list() {
		List<BlogRecord> allRecords = super.list(new LambdaQueryWrapper<BlogRecord>()
				.orderByDesc(BlogRecord::getRecordDate)
				);
		Set<LocalDate> recordDateSet = new HashSet<LocalDate>();
		for (BlogRecord blogRecord : allRecords) {
			recordDateSet.add(blogRecord.getRecordDate()); // 根据时间去重
		}
		List<BlogRecord> records = new ArrayList<BlogRecord>();
		for (LocalDate localDate : recordDateSet) {
			BlogRecord record = new BlogRecord();
			record.setRecordDate(localDate);
			records.add(record);  // 根据时间创建record对象，加入list
		}
		for (BlogRecord record : records) {
			List<String> detailList = new ArrayList<String>();
			for (BlogRecord blogRecord : allRecords) {
				if(blogRecord.getRecordDate().equals(record.getRecordDate())) { // 这里不能用 '==' 会有bug
					detailList.add(blogRecord.getDetails()); // 如果时间相等，则加入detaillist
				}
			}
			record.setDetailList(detailList);
		}
		return records;
	}

}
