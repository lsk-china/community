package com.lsk.community.back.auth.controller;

import com.lsk.community.back.auth.authc.Authc;
import com.lsk.community.back.common.response.aspect.annotation.JsonReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class Controller {
	@Autowired
	private Authc authc;

	@GetMapping("/token")
	public void token(HttpServletResponse resp) {
		String token = authc.token();
		Cookie tokenCookie = new Cookie("token", token);
		tokenCookie.setMaxAge(86400);
		resp.addCookie(tokenCookie);
	}

	@JsonReturn
	@PostMapping("/login")
	public Object login(@CookieValue("token") String token, String identity, String password) {
		authc.login(token, identity, password);
		return new Object();  // 实际会返回{code: 0, data:{}, message: "Success"}
	}
	@JsonReturn
	@PostMapping("/loginWithNewToken")
	public Object loginWithNewToken(HttpServletResponse resp, String identity, String password) {
		String token = authc.loginWithNewToken(identity, password);
		Cookie tokenCookie = new Cookie("token", token);
		tokenCookie.setMaxAge(86400);
		resp.addCookie(tokenCookie);
		return new Object();
	}
}
