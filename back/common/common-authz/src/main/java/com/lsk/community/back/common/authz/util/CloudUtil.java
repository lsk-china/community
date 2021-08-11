package com.lsk.community.back.common.authz.util;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestTemplate;

public class CloudUtil {
	private static LoadBalancerClient loadBalancerClient = SpringUtil.getBean(LoadBalancerClient.class);
	private static RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);

	private static String getServiceRootURL(String serviceName){
		ServiceInstance serviceInstance = loadBalancerClient.choose(serviceName);
		return "http://"+serviceInstance.getHost()+":"+serviceInstance.getPort();
	}

	public static boolean checkRequestKey(String requestKey, String clientIP, String targetURL) {
		String api = getServiceRootURL("gateway") + "/checkRequestKey";

	}
}
