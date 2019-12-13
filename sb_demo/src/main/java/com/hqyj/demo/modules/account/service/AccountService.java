package com.hqyj.demo.modules.account.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.hqyj.demo.modules.account.entity.Resource;
import com.hqyj.demo.modules.account.entity.Role;
import com.hqyj.demo.modules.account.entity.User;
import com.hqyj.demo.modules.common.vo.Result;

public interface AccountService {

	Result doRegister(User user);

	Result doLogin(User user);

	List<User> selectUsers();

	List<Role> selectRoles();

	List<Role> selectRolesByUserId(int userId);

	Result editUser(User user);

	boolean deleteUser(int userId);

	Result editRole(Role role);

	void deleteRole(int roleId);

	List<Resource> selectResources();

	Result editResource(Resource resource);

	void deleteResource(int resourceId);

	List<Role> selectRolesByResourceId(int resourceId);

	User selectUserByUserName(String userName);

	List<Resource> selectResourcesByRoleId(int roleId);

	PageInfo<User> userPage(int currentPage, int pageSize);

	
	
}
