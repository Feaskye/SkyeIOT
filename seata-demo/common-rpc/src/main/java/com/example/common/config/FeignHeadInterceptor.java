package com.example.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @ClassName FeignTokenInterceptor
 * @Description TODO
 
 * @DATE 2022/6/27 10:02
 * @VERSION V1.1.0
 ***/
@Slf4j
public class FeignHeadInterceptor implements RequestInterceptor {

	@Value("${spring.application.name:anonymity}")
	private String applicationName;

	@Override
	public void apply(RequestTemplate requestTemplate) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (Objects.nonNull(attributes)) {
			HttpServletRequest request = attributes.getRequest();
			Enumeration<String> headerNames = request.getHeaderNames();
			if (headerNames != null) {
				while (headerNames.hasMoreElements()) {
					String name = headerNames.nextElement();
					// 跳过 content-*
					if(StringUtils.equals("content-type",name)){
						continue;
					}
					if (StringUtils.equals("content-length",name)){
						continue;
					}
					String values = request.getHeader(name);
					requestTemplate.header(name, values);
				}
			}
			/**
			 * 设定发起调用模块的应用名
			 */
			requestTemplate.header("active-party",applicationName);
			requestTemplate.header(RootContext.KEY_XID, RootContext.getXID());
			log.info("feign-request-url {}:{}", request.getMethod(), request.getRequestURI());
		}
	}
}
