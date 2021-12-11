package com.lsk.community.gateway.controller;

import com.lsk.community.gateway.client.AuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
	@Autowired
	// @Qualifier("com.lsk.community.gateway.client.AuthClient")
	private AuthClient client;

	@GetMapping("/api/auth/token")
	public void token(HttpServletResponse resp){
		resp.addCookie(new Cookie("token", client.token()));
	}
}
