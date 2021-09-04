package com.lsk.community.back.common.utils;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
public class UtilAutoConfigure {
	@Bean
	@ConditionalOnMissingBean(RestTemplate.class)
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	@ConditionalOnMissingBean(DebugUtil.class)
	@ConditionalOnProperty(prefix = "lsk.debug", name = "enabled", havingValue = "true")
	public DebugUtil debugUtilImpl() {
		return new DebugUtilImpl();
	}

	@Bean
	@ConditionalOnMissingBean(DebugUtil.class)
	@ConditionalOnProperty(prefix = "lsk.debug", name = "enabled", havingValue = "false")
	public DebugUtil emptyDebugUtil() {
		return new EmptyDebugUtil();
	}
}
