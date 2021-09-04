package com.lsk.community.back.common.authz.holder;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HolderFilter
 * 把请求和响应对象存入ReqAndRespHolder中，并在RequsetKeyAspect中使用。
 */

@WebFilter(filterName = "HolderFilter")
public class HolderFilter implements Filter {
	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		ReqAndRespHolder.setRequest((HttpServletRequest) request);
		ReqAndRespHolder.setResponse((HttpServletResponse) response);
		chain.doFilter(request, response);
	}
}
