package com.lsk.community.common.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("common.redis")
public class RedisConfig {
	private String host = "localhost";
	private int port = 6379;
	private String password;
}
