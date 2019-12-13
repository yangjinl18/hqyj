package com.hqyj.demo.modules.common.controller;


import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hqyj.demo.modules.common.vo.Result;

@ControllerAdvice
@Controller
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorController {
	/**
	 *跳转没有权限错误页面
	 */
	@RequestMapping("/error/403")
	public String error() {
		return "index";
	}
	@RequestMapping("/error/404")
	@ResponseBody
	public Result error2() {
		return new Result(500, "你没有权限");
	}
	
	
	/**
     * 集中处理 controller 层 AuthorizationException 异常
     * 用户登录情况下访问被保护资源 ---- 403错误码
     */
    @ExceptionHandler(value=AuthorizationException.class)
    public String handlerAccessDeniedException(HttpServletRequest reuqest, 
    		AuthorizationException exception) {
    	String requestedWith = reuqest.getHeader("x-requested-with");
    	if(requestedWith !=null && requestedWith.equals("XMLHttpRequest")) {
    		//ajax请求返回一个借口
    		return "redirect:/error/404";
    	}
    	//传统请求跳转页面
        return "redirect:/error/403";
    }
    
}
