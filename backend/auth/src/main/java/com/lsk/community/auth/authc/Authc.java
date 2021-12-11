package com.lsk.community.auth.authc;

import com.lsk.community.common.redis.RedisComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Authc {
	@Autowired
	private RedisComponent redis;

	public String token() {
		String token = UUID.randomUUID().toString();
		redis.set(token + "-STATE", "GUEST");
		return token;
	}
}
