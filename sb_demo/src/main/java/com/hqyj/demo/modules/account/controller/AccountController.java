package com.hqyj.demo.modules.account.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.hqyj.demo.modules.account.entity.Resource;
import com.hqyj.demo.modules.account.entity.Role;
import com.hqyj.demo.modules.account.entity.User;
import com.hqyj.demo.modules.account.service.AccountService;
import com.hqyj.demo.modules.common.vo.Result;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	/**
	 * 跳转注册页面
	 */
	@RequestMapping("/register")
	public String register() {
		return "indexRegister";
	}
	
	/**
	 * 注册用户
	 */
	@PostMapping("/doRegister")
	@ResponseBody
	public Result doRegister(@ModelAttribute User user) {
		return accountService.doRegister(user);
	}
	
	/**
	 * 只跳转登录页面
	 */
	@RequestMapping("/login")
	public String toLogin() {
		return "indexLogin";
	}
	
	/**
	 * 登录
	 */
	@PostMapping("/doLogin")
	@ResponseBody
	public Result doLogin(@ModelAttribute User user) {
		return accountService.doLogin(user);
	}
	
	/**
	 * 跳转主页面
	 */
	@RequestMapping("/dashboard")
	public String dashboard() {
		return "index";
	}
	
	/**
	 * 登出
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "redirect:/account/login";
	}
	
	/**
	 * 查询allUser
	 * 
	 */
	@RequestMapping("/users")
	public String selectUsers(ModelMap modelMap) {
		List<Role> roles = accountService.selectRoles();
		List<User> users = accountService.selectUsers();
		modelMap.addAttribute("roles", roles);
		modelMap.addAttribute("users", users);
		return "index";
	}
	
	/**
	 * by userId查询roles
	 */
	@RequestMapping("/roles/user/{userId}")
	@ResponseBody
	public List<Role> selectRolesByUserId(@PathVariable("userId") int userId){
		return accountService.selectRolesByUserId(userId);
	}
	
	/**
	 * 编辑roles
	 */
	@RequestMapping(value="/editUser", 
			method=RequestMethod.POST, consumes="application/json")
	@ResponseBody
	@RequiresPermissions("editUser")
	public Result editUser(@RequestBody User user){
		return accountService.editUser(user);
	}
	
	
	/**
	 * by userId删除user并删除中间表roles
	 * 
	 * shiro常见注解
	 * @RequiresAuthentication : 表示当前 Subject 已经认证登录的用户才能调用的代码块。
	 * @RequiresUser : 表示当前 Subject 已经身份验证或通过记住我登录的。
	 * @RequiresGuest : 表示当前 Subject 没有身份验证，即是游客身份。
	 * @RequiresRoles(value={"admin", "user"}, logi-cal=Logical.AND)
	 *  @RequiresPermissions (value={"***","***"}, logical= Logical.OR) 
	 */
	@RequestMapping(value="deleteUser/{userId}")
	@RequiresPermissions("deleteUser")
	public String deleteUser(@PathVariable int userId, @RequestParam int current, @RequestParam int size){
		accountService.deleteUser(userId);
		if (current ==1) {
			return "redirect:/account/userPage/1";
		}else {
			if (size ==1) {
				return "redirect:/account/userPage/"+(current-1);
			}
		}
		return "redirect:/account/userPage/1";
	}
	
	
	/**
	 * 查询所有role
	 */
	@RequestMapping(value="/roles")
	public String selectRoles(ModelMap modelMap){
		List<Role> roles = accountService.selectRoles();
		modelMap.addAttribute("roles", roles);
		return "index";
	}
	
	/**
	 * 修改role
	 * id>0 ,修改role
	 * id=0，add role
	 */
	@RequestMapping(value="/editRole")
	@ResponseBody
	@RequiresPermissions("editRole")
	public Result editRole(@ModelAttribute Role role) {
		return accountService.editRole(role);
	}
	
	/**
	 * 删除role
	 */
	@RequestMapping(value="/deleteRole/{roleId}")
	public String deleteRole(@PathVariable int roleId) {
		accountService.deleteRole(roleId);
		return "redirect:/account/roles";
	}
	
	/**
	 * 跳转resources主页面
	 */
	@RequestMapping("/resources")
	public String selectResources(ModelMap modelMap) {
		List<Resource> resources = accountService.selectResources();
		List<Role> roles = accountService.selectRoles();
		modelMap.addAttribute("roles", roles);
		modelMap.addAttribute("resources", resources);
		return "index";
	}
	
	/**
	 * 编辑resource，
	 * id大于0执行update，
	 * id等于0执行add，指定角色，修改中间表m_role_resource；
	 */
	@RequestMapping(value = "/editResource", consumes="application/json")
	@ResponseBody
	@RequiresPermissions("editResource")
	public Result editResource(@RequestBody Resource resource) {
		return accountService.editResource(resource);
	}
	
	/**
	 * 删除resource
	 * 同时维护中间表
	 */
	@RequestMapping("/deleteResource/{resourceId}")
	public String deleteResource(@PathVariable int resourceId) {
		accountService.deleteResource(resourceId);
		return "redirect:/account/resources";
	}
	
	/**
	 * 通过resourceId
	 * 查询roles
	 */
	@RequestMapping("/roles/resource/{resourceId}")
	@ResponseBody
	public List<Role> selectRolesByResourceId(@PathVariable int resourceId) {
		return accountService.selectRolesByResourceId(resourceId);
	}	
	
	/**
	 * 分页查询
	 */
	@RequestMapping("/userPage/{currentPage}")
	public String userPage(ModelMap modelMap, @PathVariable(required = false) int currentPage) {
		int pageSize = 2;
		PageInfo<User> userPageInfo = accountService.userPage(currentPage,pageSize);
//		int pages = (int) userPageInfo.getPages();
		
//		int pageNum = userPageInfo.getPageNum();
		
//		List<User> users = userPageInfo.getList();
		List<Role> roles = accountService.selectRoles();
//		modelMap.addAttribute("pages", pages);
//		modelMap.addAttribute("pageNum", pageNum);
		modelMap.addAttribute("userPageInfo", userPageInfo);
		modelMap.addAttribute("roles", roles);
		modelMap.addAttribute("template", "/account/users");
		return "index";
	}
	
}

