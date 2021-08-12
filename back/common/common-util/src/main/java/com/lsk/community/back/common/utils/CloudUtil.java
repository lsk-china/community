package com.lsk.community.back.common.utils;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class CloudUtil {
	private static LoadBalancerClient loadBalancerClient = SpringUtil.getBean(LoadBalancerClient.class);
	private static RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);

	private static String getServiceRootURL(String serviceName){
		ServiceInstance serviceInstance = loadBalancerClient.choose(serviceName);
		return "http://"+serviceInstance.getHost()+":"+serviceInstance.getPort();
	}

	public static <T> T requestService(String service, String api, Map<String, Object> params, Class<T> type) {
		
	}
}
