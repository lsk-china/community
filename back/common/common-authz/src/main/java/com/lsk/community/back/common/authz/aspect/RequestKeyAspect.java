package com.lsk.community.back.common.authz.aspect;

import com.lsk.community.back.common.authz.client.GatewayClient;
import com.lsk.community.back.common.authz.util.ReflectionUtil;
import com.lsk.community.back.common.response.StatusCode;
import com.lsk.community.back.common.utils.CloudUtil;
import com.lsk.community.back.common.utils.DebugUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class RequestKeyAspect {
	@Pointcut("@annotation(com.lsk.community.back.common.authz.aspect.annotation.RequireRequestKey)")
	public void pointcut(){}

	@Autowired
	private GatewayClient client;

	// 解析方法映射的URL
	private String getMappingURL(Method method) {
		if (method.isAnnotationPresent(GetMapping.class)) {    // 使用GetMapping注解映射
			GetMapping getMapping = method.getAnnotation(GetMapping.class);
			return getMapping.value()[0];
		} else if (method.isAnnotationPresent(PostMapping.class)) {  // 使用PostMapping映射
			PostMapping postMapping = method.getAnnotation(PostMapping.class);
			return postMapping.value()[0];
		} else {    // 未检测到任何支持的映射注解，抛出异常
			throw new RuntimeException("No mapping annotation detected!");
		}
	}
	@Around("pointcut()")
	public Object around(ProceedingJoinPoint pjp){
		try {
			// 获取被拦截方法的Method对象
			MethodSignature targetMethodSignature = (MethodSignature) pjp.getSignature();
			Method targetMethod = targetMethodSignature.getMethod();
			// 解析方法的参数列表
			List<String> parameterNames = ReflectionUtil.createParameterNamesList(targetMethod);
			int requestKeyIndex = parameterNames.indexOf("requestKey");  // requestKey参数的位置
			int requestObjectIndex = parameterNames.indexOf("req");      // request参数的位置
			if (requestKeyIndex == -1 || requestObjectIndex == -1) {     // 有-1说明有参数未找到
                throw new StatusCode(500, "Parameters error");
			}
			// 获取参数值
			String requestKey = (String) pjp.getArgs()[requestKeyIndex];
			HttpServletRequest req = (HttpServletRequest) pjp.getArgs()[requestObjectIndex];
			// 根据方法解析映射的URL
			String targetURL = getMappingURL(targetMethod);
			String clientIP = req.getRemoteAddr();
			// 构建请求参数
			Map<String, Object> params = new HashMap<>();
			params.put("requestKey", requestKey);
			params.put("clientIP", clientIP);
			params.put("targetURL", targetURL);
			DebugUtil.printMap("params: ", params);
			// 请求gateway模块检查requestKey
			boolean pass = client.checkRequestKey(requestKey, targetURL, clientIP);

			if (!pass) {
				// 检查失败，抛出异常
				throw new StatusCode(403, "RequestKey failed");
			}
			// 成功，继续执行。
			return pjp.proceed();
		} catch (StatusCode statusCode) {
			throw statusCode;           // 状态码继续抛出
		} catch (Throwable t) {
			throw new RuntimeException(t); // 其他包装为RuntimeException后抛出（不用写throws）
		}
	}
}
