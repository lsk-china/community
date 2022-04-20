package com.lsk.community.back.common.authz.holder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReqAndRespHolder {
	private static ThreadLocal<HttpServletRequest> reqHolder = new ThreadLocal<>();
	private static ThreadLocal<HttpServletResponse> respHolder = new ThreadLocal<>();

	public static void setRequest(HttpServletRequest req) {
		reqHolder.set(req);
	}
	public static HttpServletRequest getRequest() {
		return reqHolder.get();
	}
	public static void setResponse(HttpServletResponse resp) {
		respHolder.set(resp);
	}
	public static HttpServletResponse getResponse() {
		return respHolder.get();
	}
}
