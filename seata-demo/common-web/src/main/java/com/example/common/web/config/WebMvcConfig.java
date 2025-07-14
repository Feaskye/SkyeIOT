package com.example.common.web.config;

import com.example.common.web.interceptor.LogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: luwg
 * @date 2021-10-9
 * @time 14:30
 * @desc
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.servlet.context-path:}")
    private String contextPath;
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor(applicationName,contextPath))
                .addPathPatterns("/**");
	}

}
