package com.hqyj.demo.modules.account.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.hqyj.demo.modules.account.entity.Resource;
import com.hqyj.demo.modules.account.entity.Role;
import com.hqyj.demo.modules.account.entity.User;

@Repository
@Mapper
public interface AccountDao {

	/**
	 * 注册用户
	 * 插入用户（userName，password）
	 */
	@Insert("insert into m_user (user_name,password, create_date) values(#{userName}, #{password}, now())")
	int doRegister(User user);

	/**
	 * 通过username查询user
	 */
	@Select("select * from m_user where user_name = #{userName}")
	User selectUserByUserName(String userName);

	/**
	 * 登录查询user
	 */
	@Select("select * from m_user where user_name = #{userName} and password =#{password}")
	User selectUserByUserNameAndPassword(User user);

	/**
	 * 查询all user
	 */
	@Select("select * from m_user")
	List<User> selectUsers();

	/**
	 * 查询all Role
	 */
	@Select("select * from m_role")
	List<Role> selectRoles();

	/**
	 * By userId get roles
	 */
	@Select("SELECT m_role.role_id, m_role.role_name FROM m_user_role\r\n" + 
			"INNER JOIN m_role ON m_user_role.role_id = m_role.role_id\r\n" + 
			"WHERE m_user_role.user_id = #{userId}")
	List<Role> selectRolesByUserId(int userId);

	@Delete("delete from m_user_role where user_id = #{userId}")
	int deleteUserRole(int userId);

	@Insert("insert into m_user_role (user_id, role_id) values(#{userId}, #{roleId})")
	void insertUserRole(int userId, int roleId);

	@Delete("delete from m_user  where user_id =#{userId}")
	int deleteUserByUserId(int userId);

	@Insert("insert into m_role (role_name) values(#{roleName})")
	void insertRole(Role role);

	@Update("update m_role set role_name = #{roleName} where role_id =#{roleId}")
	void updateRole(Role role);

	@Delete("delete from m_role where role_id =#{roleId}")
	void deleteRole(int roleId);

	@Select("select * from m_resource")
	List<Resource> selectResources();

	@Insert("insert into m_resource (resource_uri, resource_name, permission)"
			+ " values(#{resourceUri}, #{resourceName}, #{permission})")
	@Options(keyProperty = "resourceId", keyColumn = "resource_id",useGeneratedKeys = true)
	void insertResource(Resource resource);

	@Insert("insert into m_role_resource (role_id, resource_id) values(#{roleId}, #{resourceId})")
	void insertRoleResource(int roleId, int resourceId);

	@Delete("delete from m_role_resource where resource_id=#{resourceId}")
	void deleteRoleResource(int resourceId);

	@Update("update m_resource set resource_uri=#{resourceUri}, "
			+ "resource_name=#{resourceName}, permission=#{permission}"
			+ " where resource_id=#{resourceId}")
	void updateResource(Resource resource);

	@Delete("delete from m_resource where resource_id =#{resourceId}")
	void deleteResource(int resourceId);

	@Select("SELECT m_role.role_id,m_role.role_name\r\n" + 
			"FROM m_role_resource\r\n" + 
			"INNER JOIN m_role ON m_role_resource.role_id = m_role.role_id\r\n" + 
			"WHERE m_role_resource.resource_id = #{resourceId}")
	List<Role> selectRolesByResourceId(int resourceId);

	@Select("select permission from m_resource INNER JOIN m_role_resource USING(resource_id)\r\n" + 
			"WHERE role_id = #{roleId}")
	List<Resource> selectResourcesByRoleId(int roleId);

}
