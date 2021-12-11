package com.lsk.community.back.common.response;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan
// @ServletComponentScan
public class ResponseAutoConfigure {
	@Bean
	@ConditionalOnMissingBean(Gson.class)
	public Gson gson() {
		return new Gson();
	}
}
