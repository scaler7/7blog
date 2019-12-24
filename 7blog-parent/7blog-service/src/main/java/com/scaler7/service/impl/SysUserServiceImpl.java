package com.scaler7.service.impl;

import com.scaler7.entity.SysUser;
import com.scaler7.mapper.SysUserMapper;
import com.scaler7.service.SysUserService;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
	
	@Autowired
	SysUserMapper sysUserMapper;

	@Override
	public SysUser findUserByName(String username) {
		Assert.notNull(username, "用户名不能为空");
		log.info("根据用户名{}查询单个用户",username);
		return sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
				.eq(SysUser::getUserName, username));
	}

}
