package com.lsk.community.back.common.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("common.redis")
public class RedisProperties {
	private String host = "localhost";
	private int port = 6379;
	private String password;
}
