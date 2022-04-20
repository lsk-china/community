package com.lsk.community.back.gateway.fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient("gateway")
public interface AuthApi {
	@GetMapping("/token")
	public String token();
}
