package com.hqyj.demo.config.web.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

@Configuration
public class ShiroConfig {

	@Autowired
	private MyRealm myRealm;
	
	/**
	 *配置shiro安全管理器
	 */
	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myRealm);
		//添加记住我
		securityManager.setRememberMeManager(rememberMeManager());
		//添加session管理
		securityManager.setSessionManager(sessionManager());
		return securityManager;
	}
	
	/**
	 * 配置shiro过滤器工厂
    * -----------------
            * 拦截权限
     * anon：匿名访问，无需登录
     * authc：登录后才能访问
     * logout：登出
     * roles：角色过滤器
     * ------------------
     * URL匹配风格
     * ?：匹配一个字符，如 /admin? 将匹配 /admin1，但不匹配 /admin 或 /admin/
     * *：匹配零个或多个字符串，如 /admin* 将匹配 /admin 或/admin123，但不匹配 /admin/1
     * **：匹配路径中的零个或多个路径，如 /admin/** 将匹配 /admin/a 或 /admin/a/b
     */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager());
		factoryBean.setLoginUrl("/account/login");
//		factoryBean.setUnauthorizedUrl("/error/403");
		factoryBean.setSuccessUrl("/account/dashboard");
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("/static/**", "anon");
		map.put("/js/**", "anon");
		map.put("/css/**", "anon");
		map.put("/account/login", "anon");
		map.put("/account/doLogin", "anon");
		map.put("/account/register", "anon");
		map.put("/account/doRegister", "anon");
		map.put("/test/**", "anon");
		
		// 如果使用“记住我功能”，则采用user规则，如果必须要用户登录，则采用authc规则
//		map.put("/**", "authc");
		map.put("/**", "user");
		factoryBean.setFilterChainDefinitionMap(map);
		
		return factoryBean;
	}
	
    /**
                * 注册shiro方言，让thymeleaf支持shiro标签
     */
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}
	
	/**
	 * 自动代理类，支持Shiro的注解
	 */
	@Bean
	@DependsOn({"lifecycleBeanPostProcessor"})
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}
	
	/**
	 *  开启Shiro的注解
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor attributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		attributeSourceAdvisor.setSecurityManager(securityManager());
		return attributeSourceAdvisor;
	}
	
	/**
	 * SimpleCookie
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		//这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		 //如果httyOnly设置为true，则客户端不会暴露给客户端脚本代码，使用HttpOnly cookie有助于减少某些类型的跨站点脚本攻击；
		simpleCookie.setHttpOnly(true);
		//设置生效时长(单位：s)
		simpleCookie.setMaxAge(30*24*60*60);
		return simpleCookie;
	}
	
	
	/**
	 * cookie管理器;
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		//rememberme cookie加密的密钥 建议每个项目都不一样
		byte[] cipherKey =  Base64.decode("wGiHplamyXlVB11UXWol8g==");
		cookieRememberMeManager.setCipherKey(cipherKey);
		cookieRememberMeManager.setCookie(rememberMeCookie());
		return cookieRememberMeManager;
	}
	
	/**
	 * session管理器
	 */
	@Bean
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		//去掉url后的sessionId
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		return sessionManager;
	}
}
