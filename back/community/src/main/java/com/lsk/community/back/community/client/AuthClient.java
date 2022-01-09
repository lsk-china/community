package com.lsk.community.back.community.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "auth", contextId = "community")
public interface AuthClient {
	@GetMapping("/userinfo")
	public String userinfo(@CookieValue("token") String token);
}
