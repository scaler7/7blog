package com.scaler7.service.impl;

import com.scaler7.common.Constant;
import com.scaler7.entity.SysMenu;
import com.scaler7.entity.SysRoleMenu;
import com.scaler7.entity.SysUserRole;
import com.scaler7.mapper.SysMenuMapper;
import com.scaler7.mapper.SysRoleMenuMapper;
import com.scaler7.mapper.SysUserRoleMapper;
import com.scaler7.service.SysMenuService;
import com.scaler7.vo.Menu;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.ArrayList;
import java.util.List;
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
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

	@Autowired
	SysMenuMapper sysMenuMapper;
	@Autowired
	SysRoleMenuMapper sysRoleMenuMapper;
	@Autowired
	SysUserRoleMapper sysUserRoleMapper;
	
	@Override
	public List<Menu> findMenusByUserId(Integer userId) {
		Assert.notNull(userId, "用户id不能为空");
		log.info("根据id为{}的用户查询菜单",userId);
		List<Object> roleIds = sysUserRoleMapper.selectObjs(new LambdaQueryWrapper<SysUserRole>()
				.select(SysUserRole::getRoleId)
				); // 查询出该用户拥有的角色
		List<Object> menuIds = sysRoleMenuMapper.selectObjs(new LambdaQueryWrapper<SysRoleMenu>()
				.select(SysRoleMenu::getMenuId)
				.in(CollectionUtil.isNotEmpty(roleIds), SysRoleMenu::getRoleId, roleIds)
				); // 查询出菜单id集合
		List<SysMenu> menuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>()
				.in(CollectionUtil.isNotEmpty(menuIds), SysMenu::getMenuId, menuIds)
				.le(SysMenu::getType, 1)); // 只查询出菜单数据(type为0或1)
		
		List<Menu> menus = translateToMenu(menuList);
		
		return menus;
	}

	/**
	 * 
	 * @Description:将SysMenu集合转换为Menu集合，并设置子节点
	 * @param menuList
	 * @return
	 */
	private List<Menu> translateToMenu(List<SysMenu> menuList) {
		Assert.notNull(menuList, "需要转化的菜单集合不能为空");
		List<Menu> menus = new ArrayList<Menu>();
		List<Menu> treeMenus = new ArrayList<Menu>();
		for (SysMenu nav : menuList) {
			Menu menu = new Menu();
			menu.setMenuId(nav.getMenuId());
			menu.setParentId(nav.getParentId());
			menu.setTitle(nav.getMenuName());
			menu.setHref(nav.getUrl());
			menu.setFontFmaily(Constant.FONT_FAMILY);
			menu.setIcon(nav.getIcon());
			if("控制台".equals(nav.getMenuName())) {
				menu.setSpread(true);
				menu.setCheck(true);
			}
			menus.add(menu);
		}
		for (Menu nav : menus) {
			if(nav.getParentId() == 0) {
				treeMenus.add(nav); // parentId为0的是根节点
			}
		}
		for (Menu nav : treeMenus) {
			List<Menu> childList = getChirld(nav.getMenuId(),menus); // 递归获取子节点
			nav.setChildren(childList);
		}
		return treeMenus;
	}
	
	/**
	 * 
	 * @Description:递归获取子节点
	 * @param menuId
	 * @param menus
	 * @return
	 */
	private List<Menu> getChirld(Integer menuId, List<Menu> menus) {
		ArrayList<Menu> childList = new ArrayList<Menu>();
		for (Menu nav : menus) {
			if(nav.getParentId() == menuId) {
				childList.add(nav);
			}
		}
		//递归
		for (Menu nav: childList) {
			nav.setChildren(getChirld(nav.getMenuId(), menus));
		}
		if(childList.size() == 0) {
			return new ArrayList<Menu>(); // 如果没有子节点了，返回一个空list集合
		}
		
		return childList;
	}

}
