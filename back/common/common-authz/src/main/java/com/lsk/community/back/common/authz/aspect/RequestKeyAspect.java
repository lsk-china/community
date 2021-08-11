package com.lsk.community.back.common.authz.aspect;

import com.lsk.community.back.common.authz.util.ReflectionUtil;
import com.lsk.community.back.common.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

@Slf4j
@Aspect
@Component
public class RequestKeyAspect {
	@Pointcut("@annotation(com.lsk.community.back.common.authz.aspect.annotation.RequireRequestKey)")
	public void pointcut(){}

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
			MethodSignature targetMethodSignature = (MethodSignature) pjp.getSignature();
			Method targetMethod = targetMethodSignature.getMethod();
			List<String> parameterNames = ReflectionUtil.createParameterNamesList(targetMethod);
			int requestKeyIndex = parameterNames.indexOf("requestKey");
			int requestObjectIndex = parameterNames.indexOf("req");
			String requestKey = (String) pjp.getArgs()[requestKeyIndex];
			HttpServletRequest req = (HttpServletRequest) pjp.getArgs()[requestObjectIndex];
			String targetURL = getMappingURL(targetMethod);
			String clientIP = req.getRemoteAddr();
		} catch (StatusCode statusCode) {
			throw statusCode;           // 状态码继续抛出
		} catch (Throwable t) {
			throw new RuntimeException(t); // 其他包装为RuntimeException后抛出（不用写throws）
		}
	}
}
