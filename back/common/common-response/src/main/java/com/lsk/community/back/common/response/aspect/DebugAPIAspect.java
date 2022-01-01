package com.lsk.community.back.common.response.aspect;

import com.lsk.community.back.common.response.StatusCode;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DebugAPIAspect {
	@Value("common.debug.enabled")
	public Boolean enableDebug;


	@Pointcut("@annotation(com.lsk.community.back.common.response.aspect.annotation.DebugAPI)")
	public void pointcut() {}

	@Before("pointcut()")
	public void before() {
		if (!enableDebug) {
			throw new StatusCode(400, "This api is debug-only!");
		}
	}
}
