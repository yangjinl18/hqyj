package com.hqyj.demo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@WebFilter(filterName = "paramFilter", urlPatterns = "/**")
public class ParamFilter implements Filter {

	private final static Logger LOGGER = LoggerFactory.getLogger(ParamFilter.class);
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.debug("this is paramFilter init");
		Filter.super.init(filterConfig);
	}

	@Override
	public void destroy() {
		LOGGER.debug("this is paramFilter destroy");
		Filter.super.destroy();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(req) {

			//处理直接用request.getParameter()获取的参数
			@Override
			public String getParameter(String name) {
				String value = req.getParameter(name);
				if (StringUtils.isNotBlank(value)) {
					return value.replace("fuck", "*");
				}
				return super.getParameter(name);
			}
			
			//处理用@requestParam注解获取的参数
			@Override
			public String[] getParameterValues(String name) {
				String[] values = req.getParameterValues(name);
				if (values != null && values.length >0) {
					for (int i = 0; i < values.length; i++) {
						values[i]= values[i].replace("fuck", "*");
					}
					return values;
				}
				return super.getParameterValues(name);
			}
		};
		
		chain.doFilter(wrapper, response);
	}

}
