package com.scaler7.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scaler7.common.CodeMsg;
import com.scaler7.common.Constant;
import com.scaler7.common.Result;
import com.scaler7.entity.SysUser;
import com.scaler7.service.SysMenuService;
import com.scaler7.vo.Menu;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("")
public class LoginController {
	
	@Autowired
	SysMenuService sysMenuService;
	
	@PostMapping("login")
	@ApiOperation("用户登录")
	public Object login(
			@RequestParam(value = "username") String usernmae,
			@RequestParam(value = "password") String password,
			HttpSession session){
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(usernmae,password);
			//获取主体
			Subject subject = SecurityUtils.getSubject();
			//登陆验证
			subject.login(token);
			SysUser currentUser = (SysUser) subject.getPrincipal();
			System.out.println(currentUser);
			session.setAttribute(Constant.CURRENT_USER, currentUser);
		} catch (AccountException e) { // 账号错误
			return new Result(CodeMsg.LOGIN_ERROR);
		} catch (CredentialsException e) { // 密码错误
			return new Result(CodeMsg.LOGIN_ERROR);
		} catch (AuthenticationException e) { // 其他错误
			return new Result(CodeMsg.ERROR);
		}
		return new Result();
	}
	
	@GetMapping("loadMenus")
	@ApiOperation("拉取左侧菜单栏数据")
	public Object loadMenus(HttpSession session) {
		SysUser currentUser = (SysUser) session.getAttribute(Constant.CURRENT_USER); // 从session中获取当前登陆用户
		List<Menu> menus = sysMenuService.findMenusByUserId(currentUser.getUserId());
		return new Result(menus);
	}
	
}
