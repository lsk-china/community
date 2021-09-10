package com.lsk.community.back.common.response.holder;

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

@WebFilter(filterName = "HolderFilter")
public class HolderFilter implements Filter {
	public void init(FilterConfig config) throws ServletException {
	}

	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		Holder.setRequest((HttpServletRequest) request);
		Holder.setResponse((HttpServletResponse) response);
		chain.doFilter(request, response);
	}
}
