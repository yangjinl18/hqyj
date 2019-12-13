package com.hqyj.demo.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.hqyj.demo.filter.ParamFilter;
import com.hqyj.demo.interceptor.UriInterceptor;

@Configuration
@AutoConfigureAfter({ WebMvcAutoConfiguration.class })
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private UriInterceptor uriInterceptor;
	
	
	/**
	 * 注册参数过滤器
	 */
	@Bean
	public FilterRegistrationBean<ParamFilter> filterRegist() {
		
		FilterRegistrationBean<ParamFilter> filterRegistrationBean = new FilterRegistrationBean<ParamFilter>();
		filterRegistrationBean.setFilter(new ParamFilter());
		return filterRegistrationBean;
	}


	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(uriInterceptor).addPathPatterns("/**");
	}
	
	
}
