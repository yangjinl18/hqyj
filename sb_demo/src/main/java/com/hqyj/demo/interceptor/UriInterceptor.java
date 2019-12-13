package com.hqyj.demo.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 拦截器
 * @Component --注册为spring的组件--new
 */
@Component
public class UriInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	/** 
	 * 前置条件：页面放置位置和uri一一对应
	 * 拦截器需求：在控制器中，如果满足前置条件，则无需设置template，直接返回index
	 * 原理：获取uri，判断modelANdView对象中是否设置有template，如果没有，则以uri来作为template路径
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		//访问静态资源modelAndView==null，还有重定向就不做处理
		if (modelAndView == null || modelAndView.getViewName().startsWith("redirect")) {
			return;
		}
		
		String uri = request.getServletPath();
		String template = (String) modelAndView.getModelMap().get("template");
		if (StringUtils.isBlank(template)) {
			//uri以/开始将他切割掉
			if (uri.startsWith("/")) {
				uri = uri.substring(1);
			}
			//设置返回的template，习惯文件名都是小写
			modelAndView.getModelMap().addAttribute("template", uri.toLowerCase());
		}
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}

}
