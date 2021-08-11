package com.lsk.community.back.common.authz;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
public class AuthzAutoConfigure {
	@Bean
	@ConditionalOnMissingBean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
