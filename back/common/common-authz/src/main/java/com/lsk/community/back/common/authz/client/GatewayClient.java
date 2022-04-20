package com.lsk.community.back.common.authz.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient("gateway")
public interface GatewayClient {
	@GetMapping("checkRequestKey")
	public boolean checkRequestKey(@RequestParam("reqKey") String requestKey, @RequestParam("targetURL") String targetURL, @RequestParam("clientIP") String clientIP);
}
