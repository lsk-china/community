package com.lsk.community.gateway.client;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient("auth")
public interface AuthClient {
	@RequestMapping("/token")
	public String token();
}
