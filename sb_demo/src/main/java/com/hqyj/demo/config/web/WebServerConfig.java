package com.hqyj.demo.config.web;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration ---- 配置类相当于SSM框架的<beans>标签
 * @AutoConfigureAfter ---- 控制配置类的加载顺序
 */
@Configuration
@AutoConfigureAfter({ WebMvcAutoConfiguration.class })
public class WebServerConfig {

	@Value("${server.other.port}")
	private int httpPort;

	/**
	 * http连接器
	 */
	@Bean
	public Connector connector() {
		Connector connector = new Connector();
		connector.setScheme("http");
		// 设置端口号
		connector.setPort(httpPort);
		return connector;
	}

	/**
	 * 1、重新注册ServletWebServerFactory bean
	 * 2、以TomcatServletWebServerFactory实现，重写postProcessContext方法
	 * 3、设置安全收集器SecurityCollection，匹配所有URL
	 * 4、设置安全约束SecurityConstraint，加入约束类型为CONFIDENTIAL(秘密的) 5、注入上下文 6、如果需要同时支持 http 和
	 * https，还需要添加 http 连接器
	 */
	@Bean
	public ServletWebServerFactory servletWebServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {

			@Override
			protected void postProcessContext(Context context) {
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/**");

				SecurityConstraint constraint = new SecurityConstraint();
				constraint.addCollection(collection);
				constraint.setUserConstraint("CONFIDENTIAL");
				context.addConstraint(constraint);
			}
		};
		factory.addAdditionalTomcatConnectors(connector());

		return factory;
	}
}
