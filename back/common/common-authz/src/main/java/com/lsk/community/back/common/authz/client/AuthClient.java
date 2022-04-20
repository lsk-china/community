package com.lsk.community.back.common.authz.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "auth", contextId = "authz")
public interface AuthClient {
	@GetMapping("/status")
	public String status(String token);
}
