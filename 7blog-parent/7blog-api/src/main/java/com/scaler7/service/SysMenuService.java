package com.scaler7.service;

import com.scaler7.entity.SysMenu;
import com.scaler7.vo.Menu;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author scaler7
 * @since 2019-12-21
 */
public interface SysMenuService extends IService<SysMenu> {

	List<Menu> findMenusByUserId(Integer userId);

}
