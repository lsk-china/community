package com.lsk.community.back.common.response.holder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HolderFilter
 *
 * As same as com.lsk.community.back.common.authz.holder.HolderFilter
 */

@Slf4j
// @WebFilter(filterName = "ResponseHolderFilter")
public class HolderFilter implements Filter {
	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}

	private void processCORS(HttpServletRequest req, HttpServletResponse resp) {
		if (resp.getHeader("Access-Control-Allow-Origin") == null) { // 因为会有多个Filter尝试处理CORS，所以在添加CORS的header前检查header是否已经被设置，否则会在前端导致错误
			resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
			resp.setHeader("Access-Control-Allow-Credentials", "true");
			resp.setHeader("Access-Control-Allow-Methods", "*");
			resp.setHeader("Access-Control-Allow-Headers", "*");
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		if (req.getMethod() == "OPTIONS") { // 请求方法为OPTIONS则处理跨域
			log.info("Received Options");
			processCORS(req, resp);
			return;
		}
		Holder.setRequest(req);
		Holder.setResponse(resp);
		chain.doFilter(request, response);
	}
}
