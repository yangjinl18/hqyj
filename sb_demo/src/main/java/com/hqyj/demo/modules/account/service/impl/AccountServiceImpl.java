package com.hqyj.demo.modules.account.service.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hqyj.demo.modules.account.dao.AccountDao;
import com.hqyj.demo.modules.account.entity.Resource;
import com.hqyj.demo.modules.account.entity.Role;
import com.hqyj.demo.modules.account.entity.User;
import com.hqyj.demo.modules.account.service.AccountService;
import com.hqyj.demo.modules.common.vo.Result;
import com.hqyj.demo.utils.MD5Util;


@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountDao accountDao;

	/**
	 * 注册user
	 * 是否存在该UserName
	 * 再注册
	 */
	@Override
	public Result doRegister(User user) {
		
		User user2 = accountDao.selectUserByUserName(user.getUserName());
		if (user2 !=null) {
			return new Result(500,"userName exist.");
		}
		User user3 = new User();
		user3.setUserName(user.getUserName());
		//进入数据库进行加密
		user3.setPassword(MD5Util.getMD5(user.getPassword()));
		int n = accountDao.doRegister(user3);
		if (n > 0) {
			//返回前台，密码要明文
			return new Result(200,"Register success.", user);
		}
		return new Result(500,"Register fail.");
	}

	@Override
	public Result doLogin(User user) {
		Result result = new Result(200, "login success");
		Subject subject = SecurityUtils.getSubject();
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), MD5Util.getMD5(user.getPassword()));
			token.setRememberMe(user.isRememberMe()); 
			// 登录验证，调用MyRealm中doGetAuthenticationInfo方法
			subject.login(token);
			
			// 授权，调用MyRealm中doGetAuthorizationInfo方法
			subject.checkRoles();
		} catch (Exception e) {
			return new Result(500, e.getMessage());
//			return new Result(500, "account or password not true.");
		}
		return result;
	}

	@Override
	public List<User> selectUsers() {
		return accountDao.selectUsers();
	}

	@Override
	public List<Role> selectRoles() {
		return accountDao.selectRoles();
	}

	@Override
	public List<Role> selectRolesByUserId(int userId) {
		
		return accountDao.selectRolesByUserId(userId);
	}

	@Override
	public Result editUser(User user) {
		int userId = user.getUserId();
		accountDao.deleteUserRole(userId);
		for (Role role : user.getRoles()) {
			int roleId = role.getRoleId();
			accountDao.insertUserRole(userId, roleId);
		}
		return new Result(200, "success");
	}

	@Transactional
	@Override
	public boolean deleteUser(int userId) {
		int n = accountDao.deleteUserRole(userId);
		int m = accountDao.deleteUserByUserId(userId);
		if (n>0 && m>0) {
			return true;
		}
		return false;
	}

	@Override
	public Result editRole(Role role) {
		if (role.getRoleId() == 0) {
			accountDao.insertRole(role);
			return new Result(200, "add success", "role");
		}
		if (role.getRoleId() > 0) {
			accountDao.updateRole(role);
			return new Result(200, "edit success", "role");
		}
		return new Result(500, "fail");
	}

	@Transactional
	@Override
	public void deleteRole(int roleId) {
		
		accountDao.deleteRole(roleId);
	}

	@Override
	public List<Resource> selectResources() {
	
		return accountDao.selectResources();
	}

	@Transactional
	@Override
	public Result editResource(Resource resource) {
		int resourceId = resource.getResourceId();
		if (resourceId == 0) {
			//添加resource
			accountDao.insertResource(resource);
			for (Role role : resource.getRoles()) {
				int roleId = role.getRoleId();
				//维护中间表
				accountDao.insertRoleResource(roleId, resource.getResourceId());
			}
			return new Result(200, "add success", resource);
		}
		if (resourceId >0) {
			//修改resource
			accountDao.updateResource(resource);
			//先删除中间表相关信息
			accountDao.deleteRoleResource(resource.getResourceId());
			for (Role role : resource.getRoles()) {
				int roleId = role.getRoleId();
				//再添加
				accountDao.insertRoleResource(roleId, resourceId);
			}
			return new Result(200, "update success", resource);
		}
		return new Result(500, "fail.");
	}

	/**
	 * 删除resource
	 * 同时删除中间表
	 */
	@Transactional
	@Override
	public void deleteResource(int resourceId) {
		accountDao.deleteResource(resourceId);
		accountDao.deleteRoleResource(resourceId);
	}

	@Override
	public List<Role> selectRolesByResourceId(int resourceId) {
		return accountDao.selectRolesByResourceId(resourceId);
	}

	@Override
	public User selectUserByUserName(String userName) {
		return accountDao.selectUserByUserName(userName);
	}

	@Override
	public List<Resource> selectResourcesByRoleId(int roleId) {
		return accountDao.selectResourcesByRoleId(roleId);
	}

	/**
	 * 分页
	 */
	@Override
	public PageInfo<User> userPage(int currentPage, int pageSize) {
		PageHelper.startPage(currentPage, pageSize);
		List<User> users = accountDao.selectUsers();
		return new PageInfo<User>(users);
	}
}
