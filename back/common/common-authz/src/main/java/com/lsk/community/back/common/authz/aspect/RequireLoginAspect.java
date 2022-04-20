package com.lsk.community.back.common.authz.aspect;

import com.lsk.community.back.common.authz.client.AuthClient;
import com.lsk.community.back.common.response.StatusCode;
import org.apache.commons.fileupload.RequestContext;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class RequireLoginAspect {
	@Autowired
	private AuthClient authClient;

	@Pointcut("@annotation(com.lsk.community.back.common.authz.aspect.annotation.RequireLogin)")
	public void pointcut() {}

	@Before("pointcut()")
	public void before() {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Cookie[] cookies = req.getCookies();
		String token = "";
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("token")) {
				token = cookie.getValue();
			}
		}
		if (token.equals("")) {
			throw new StatusCode(403, "Not login");
		}
		String status = authClient.status(token);
		if (!status.equals("LOGIN")) {
			throw new StatusCode(403, "Not login");
		}
	}
}
