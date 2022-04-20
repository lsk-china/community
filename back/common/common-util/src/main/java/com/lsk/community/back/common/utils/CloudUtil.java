package com.lsk.community.back.common.utils;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class CloudUtil {
	private static LoadBalancerClient loadBalancerClient = SpringUtil.getBean(LoadBalancerClient.class);
	private static RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);

	private static String getServiceRootURL(String serviceName){
		ServiceInstance serviceInstance = loadBalancerClient.choose(serviceName);
		return "http://"+serviceInstance.getHost()+":"+serviceInstance.getPort();
	}

	public static <T> T requestService(String service, String api, HttpMethod method, Map<String, Object> params, Class<T> type) {
		String url = getServiceRootURL(service) + api;
		HttpHeaders headers = new HttpHeaders();
		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			requestBody.add(entry.getKey(), entry.getValue());
		}
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(requestBody, headers);
		ResponseEntity<T> response = restTemplate.exchange(url, method, request, type);
		return response.getBody();
	}
}
