package com.lsk.community.back.common.redis;

import com.google.gson.Gson;
import com.lsk.community.back.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@Component
public class RedisComponent {
	@Autowired
	private JedisPool jedisPool;
	@Autowired
	private Gson gson;

	public <T> T get(String key, Class<T> type){
		try (Jedis conn = jedisPool.getResource()) {
			String str = conn.get(key);
			return gson.fromJson(str, type);
		}
	}
	public <T> void set(String key, T value, int expire){
		try (Jedis conn = jedisPool.getResource()) {
			String str = gson.toJson(value);
			if (StringUtil.isEmpty(key) || StringUtil.isEmpty(str)){
				log.error("Key or value must not be empty");
				return;
			}
			if (expire <= 0) {
				conn.set(key, str);
			} else {
				conn.setex(key, expire, str);
			}
		}
	}
	public <T> void set(String key, T value){
		try (Jedis conn = jedisPool.getResource()) {
			String str = gson.toJson(value);
			if (StringUtil.isEmpty(key) || StringUtil.isEmpty(str)){
				log.error("Key or value must not be empty");
				return;
			}
			conn.set(key, str);
		}
	}
	public void delete(String key) {
		try (Jedis conn = jedisPool.getResource()) {
			conn.del(key);
		}
	}
}
