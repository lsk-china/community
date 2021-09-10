package com.lsk.community.back.common.response.aspect;

import com.lsk.community.back.common.response.holder.Holder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CorsAspect
 * 向被Cors注解标注的api添加CORS Headers.
 * @see com.lsk.community.back.common.response.aspect.annotation.Cors
 */

@Aspect
@Component
public class CorsAspect {
	// 匹配所有带Cors注解的方法.
	@Pointcut("@annotation(com.lsk.community.back.common.response.aspect.annotation.Cors)")
	public void pointcut() {}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable{
		Object result = pjp.proceed();
		// 获取HttpServletRequest和HttpServletResponse对象
		HttpServletRequest req = Holder.getRequest();
		HttpServletResponse resp = Holder.getResponse();
		// 添加Header
		String origin = req.getHeader("Origin");
		resp.addHeader("Access-Control-Allow-Headers", "*");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.addHeader("Access-Control-Allow-Origin", origin);
		resp.addHeader("Access-Control-Allow-Methods", "*");
		return result;
	}
}
