package com.lsk.community.back.common.response.holder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Holder
 *
 * As same as com.lsk.community.back.common.authz.holder.Holder.
 */
public class Holder {
	private static ThreadLocal<HttpServletRequest> req = new ThreadLocal<>();
	private static ThreadLocal<HttpServletResponse> resp = new ThreadLocal<>();

	public static HttpServletResponse getResponse() {
		return resp.get();
	}
	public static void setResponse(HttpServletResponse response) {
		resp.set(response);
	}
	public static HttpServletRequest getRequest() {
		return req.get();
	}
	public static void setRequest(HttpServletRequest request) {
		req.set(request);
	}
}
