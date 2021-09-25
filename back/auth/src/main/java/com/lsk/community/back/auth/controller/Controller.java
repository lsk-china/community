package com.lsk.community.back.auth.controller;

import com.lsk.community.back.auth.authc.Authc;
import com.lsk.community.back.common.authz.aspect.annotation.RequireRequestKey;
import com.lsk.community.back.common.response.aspect.annotation.Cors;
import com.lsk.community.back.common.response.aspect.annotation.JsonReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller
 * auth模块的控制器类
 */
@Slf4j
@RestController
public class Controller {
	@Autowired
	private Authc authc;

	/**
	 * token
	 * URL: /token
	 * 方法: GET
	 * 获取token
	 * @param resp 响应对象，由Spring注入
	 */

	@Cors
	@GetMapping("/token")
	public void token(HttpServletResponse resp) {
		String token = authc.token();
		Cookie tokenCookie = new Cookie("token", token);
		tokenCookie.setMaxAge(86400);
		resp.addCookie(tokenCookie);
	}

	/**
	 * login
	 * URL: /login
	 * 方法: POST
	 * 登录
	 * @param token 客户端token，对应Cookie token的值
	 * @param identity 用户名或邮箱
	 * @param password 密码
	 * @param requestKey 访问密钥，由gateway模块的checkCaptcha接口获得
	 * @param req 请求对象
	 * @return 返回登录成功与否及错误原因
	 * {code: 0, data: {}, message: "Success"} : 成功
	 * {code: 400, data: {}, message: "Token, identity or password mustn't be null"} : 失败，有参数为空
	 * {code: 403, data: {}, message: "Incorrect password"} : 失败，密码错误
	 * {code: 404, data: {}, message: "User not found"} : 失败，用户未找到
	 */

	@JsonReturn
	@RequireRequestKey
	@PostMapping("/login")
	public Object login(@CookieValue("token") String token, String identity, String password, @CookieValue("reqKey") String requestKey, HttpServletRequest req) {
		log.info("Identity: " + identity + " password: " + password + "token: " + token);
		authc.login(token, identity, password);
		return new Object();  // 实际会返回{code: 0, data:{}, message: "Success"}
	}

	/**
	 * 为/login接口处理CORS
	 * @param resp 请求对象
	 * @param resp 响应对象
	 */

//	@RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
//	public void loginCors(HttpServletRequest req, HttpServletResponse resp) {
//		resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
//		resp.addHeader("Access-Control-Allow-Credentials", "true");
//		resp.addHeader("Access-Control-Allow-Methods", "POST");
//		resp.addHeader("Access-Control-Allow-Headers", "*");
//	}
//	@JsonReturn
//	@PostMapping("/loginWithNewToken")
//	public Object loginWithNewToken(HttpServletResponse resp, String identity, String password) {
//		String token = authc.loginWithNewToken(identity, password);
//		Cookie tokenCookie = new Cookie("token", token);
//		tokenCookie.setMaxAge(86400);
//		resp.addCookie(tokenCookie);
//		return new Object();
//	}
}
