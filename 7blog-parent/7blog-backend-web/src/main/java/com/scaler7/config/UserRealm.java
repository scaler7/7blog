package com.scaler7.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.scaler7.entity.SysUser;
import com.scaler7.service.SysUserService;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private SysUserService loginService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		// 使用用户名查询用户
		String username = usernamePasswordToken.getUsername();
		SysUser user = loginService.findUserByName(username);
		if(null == user) {
			return null; // shiro会自动处理账号不存在的异常
		}
		ByteSource credentialsSalt = ByteSource.Util.bytes("scaler7");
		return new SimpleAuthenticationInfo(user,user.getPassword(),credentialsSalt,username);
	}

}
