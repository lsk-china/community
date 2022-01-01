package com.lsk.community.back.auth.authc;

import com.lsk.community.back.auth.mapper.UserMapper;
import com.lsk.community.back.auth.model.User;
import com.lsk.community.back.common.redis.RedisComponent;
import com.lsk.community.back.common.response.StatusCode;
import com.lsk.community.back.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class Authc {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RedisComponent redis;

	public String token() {
		String token = UUID.randomUUID().toString();
		redis.set(token + "-STATUS", "Guest", 86400);     // 一个token的有效期为86400秒
		return token;
	}
	public void login(String token, String identity, String password) {
		if (!StringUtil.noEmpty(token, identity, password)) { // 任何参数不能为空
			throw new StatusCode(400, "Token, identity or password mustn't be null");
		}
		User user = userMapper.queryUserByUsernameOrEmail(identity);
		if (user == null) { // 找不到用户
			throw new StatusCode(404, "User not found");
		}
		if (!password.equals(user.getPassword())) { // 密码错误
			throw new StatusCode(403,"Incorrect password");
		}
		// 登录成功
		redis.set(token + "-STATUS", "LOGIN");     // token的状态设置为已登录
		redis.set(token + "-ID", user.getId());
		redis.set(token + "-OBJ", user);
	}
	public String loginWithNewToken(String identity, String password) {
		String token = token();
		login(token, identity, password);
		return token;
	}
	public User userinfo(String token) {
		Integer uid = redis.get(token + "-ID", Integer.class);
		User user = userMapper.queryUserById(uid);
		if (user == null) {
			throw new StatusCode(404, "User not found");
		}
		return user;
	}
}
