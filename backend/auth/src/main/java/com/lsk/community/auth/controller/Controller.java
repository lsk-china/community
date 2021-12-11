package com.lsk.community.auth.controller;

import com.lsk.community.auth.authc.Authc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	@Autowired
	private Authc authc;

	@GetMapping("/token")
	public String token() {
		return authc.token();
	}
}
