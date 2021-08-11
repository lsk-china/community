package com.lsk.community.back.common.redis;

import com.google.gson.Gson;
import com.lsk.community.back.common.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
@Configuration
@ComponentScan
@RequiredArgsConstructor
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfigure {
	private final RedisProperties redisProperties;

	@Bean
	@ConditionalOnMissingBean(JedisPool.class)
	public JedisPool jedisPool(){
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(5);
		jedisPoolConfig.setMaxTotal(10);
		jedisPoolConfig.setMaxWaitMillis(10000);
		String password = redisProperties.getPassword();
		if (StringUtil.isEmpty(password)) {
			return new JedisPool(jedisPoolConfig, redisProperties.getHost(), redisProperties.getPort(), 2000);
		} else {
			return new JedisPool(jedisPoolConfig, redisProperties.getHost(), redisProperties.getPort(), 2000, password);
		}
	}

	@Bean
	@ConditionalOnMissingBean(Gson.class)
	public Gson gson(){
		return new Gson();
	}
}
