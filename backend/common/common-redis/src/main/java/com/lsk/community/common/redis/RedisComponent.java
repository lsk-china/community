package com.lsk.community.common.redis;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Slf4j
@RequiredArgsConstructor
public class RedisComponent {
	private final JedisPool jedisPool;
	private final Gson gson = new Gson();

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
