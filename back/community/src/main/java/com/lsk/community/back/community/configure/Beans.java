package com.lsk.community.back.community.configure;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
	@Bean
	@ConditionalOnMissingBean(Gson.class)
	public Gson gson() {return new Gson();}
}
