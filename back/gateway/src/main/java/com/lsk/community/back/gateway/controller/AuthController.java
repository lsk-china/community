package com.lsk.community.back.gateway.controller;

import com.lsk.community.back.gateway.fegin.AuthApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class AuthController {
	// @Qualifier("com.lsk.community.back.gateway.fegin.AuthApi")
	@Autowired
	private AuthApi api;

	@GetMapping("/api/auth/token")
	public void token(HttpServletResponse resp) {
		resp.addCookie(new Cookie("token", api.token()));
	}
}
