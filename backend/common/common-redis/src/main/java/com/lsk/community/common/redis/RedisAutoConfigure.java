package com.lsk.community.common.redis;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@ComponentScan
@RequiredArgsConstructor
@EnableConfigurationProperties
public class RedisAutoConfigure {
	private final RedisConfig config;

	@Bean
	@ConditionalOnMissingBean(RedisComponent.class)
	public RedisComponent redisComponent(){
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(5);
		jedisPoolConfig.setMaxTotal(10);
		jedisPoolConfig.setMaxWaitMillis(10000);
		String password = config.getPassword();
		if (StringUtil.isEmpty(password)) {
			return new RedisComponent(new JedisPool(jedisPoolConfig, config.getHost(), config.getPort(), 2000));
		} else {
			return new RedisComponent(new JedisPool(jedisPoolConfig, config.getHost(), config.getPort(), 2000, password));
		}
	}
}
