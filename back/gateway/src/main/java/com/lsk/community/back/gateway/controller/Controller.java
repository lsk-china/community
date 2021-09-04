package com.lsk.community.back.gateway.controller;

import com.lsk.community.back.gateway.captcha.Captcha;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lsk.community.back.common.response.aspect.annotation.JsonReturn;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class Controller {
	@Autowired
	private Captcha captcha;

	@JsonReturn
	@GetMapping("/generateCaptcha")
	public Object generateCaptcha(HttpServletResponse resp, HttpServletRequest req) throws IOException {
		return captcha.generateCaptcha();
	}

	@JsonReturn
	@GetMapping("/checkCaptcha")
	public Object checkCaptcha(HttpServletResponse resp, HttpServletRequest req, String codeID, String codeText, String targetURL) {
		String requestKey = captcha.checkCaptcha(codeID, codeText, targetURL, req.getRemoteAddr());
		Cookie reqKeyCookie = new Cookie("reqKey", requestKey);
		resp.addCookie(reqKeyCookie);
		return new Object();
	}

	@GetMapping("/checkRequestKey") // 模块间请求，不需要返回json
	// ClientIP和RequestKey不能直接从请求取，因为这个接口由其他模块调用，请求不会携带这两个数据
	// 这两个数据将以参数的形式传递
	public boolean checkRequestKey(String reqKey, String targetURL, String clientIP) {
		return captcha.checkRequestKey(clientIP, reqKey, targetURL);
	}
}
