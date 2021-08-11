package com.lsk.community.back.common.response.aspect;

import com.google.gson.Gson;
import com.lsk.community.back.common.response.Response;
import com.lsk.community.back.common.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class JsonAspect {
	@Autowired
	private Gson gson;

	// 切点匹配所有带@JsonReturn的方法
	@Pointcut("@annotation(com.lsk.community.back.common.response.aspect.annotation.JsonReturn)")
	public void pointcut(){}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint pjp) {
		try {
			Object data = pjp.proceed();
			Response response = new Response(200, data, "Success");
			return gson.toJson(response);         // 正常执行直接返回数据
		} catch (Throwable t) {    // 发生异常
			if (t instanceof StatusCode) {        // 如果异常是StatusCode
				StatusCode statusCode = (StatusCode) t;
				if (statusCode.getCause() != null) {     // 如果StatusCode在包装异常，则StatusCode是由服务器错误引发的，对异常进行记录
					log.error("Error: ", statusCode.getCause());
				}
				int code = statusCode.getCode();
				Response response = new Response(code, statusCode.getCause(), statusCode.getMessage());
				return gson.toJson(response);
			} else {                               // 不是StatusCode则返回500
				log.error("Error: ", t);
				Response response = new Response(500, t, "Server error");
				return gson.toJson(response);
			}
		}
	}
}
