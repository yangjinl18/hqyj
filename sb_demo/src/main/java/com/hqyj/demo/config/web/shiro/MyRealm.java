package com.hqyj.demo.config.web.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hqyj.demo.modules.account.entity.Resource;
import com.hqyj.demo.modules.account.entity.Role;
import com.hqyj.demo.modules.account.entity.User;
import com.hqyj.demo.modules.account.service.AccountService;

@Component
public class MyRealm extends AuthorizingRealm {

	@Autowired
	private AccountService accountService;
	
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//授权类
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		
		//获得用户信息
		String userName = (String) principals.getPrimaryPrincipal();
		User user = accountService.selectUserByUserName(userName);
		if (user == null) {
			return null;
		}
		
		//获取用户的角色信息
		List<Role> roles = accountService.selectRolesByUserId(user.getUserId());
		for (Role role : roles) {
			simpleAuthorizationInfo.addRole(role.getRoleName());
			List<Resource> resources = accountService.selectResourcesByRoleId(role.getRoleId());
			for (Resource resource : resources) {
				simpleAuthorizationInfo.addStringPermission(resource.getPermission());
			}
		}
		
		return simpleAuthorizationInfo;
	}

	/**
	 * 登录验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		String userName = (String) token.getPrincipal();
		User user = accountService.selectUserByUserName(userName);
		//user为空抛出异常，controller的doLogin方法接收
		if (user == null) {
			throw new UnknownAccountException("The account do not exist.");
		}
		SimpleAuthenticationInfo simpleAuthenticationInfo =
				new SimpleAuthenticationInfo(userName, user.getPassword(),getName());
		return simpleAuthenticationInfo;
	}

	
}
