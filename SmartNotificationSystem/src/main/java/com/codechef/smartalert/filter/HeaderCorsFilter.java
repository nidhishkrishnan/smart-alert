package com.codechef.smartalert.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class HeaderCorsFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    	HttpServletResponse response = (HttpServletResponse)servletResponse;
		response.setHeader("Access-Control-Allow-Origin", "*");
    	response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    	response.setHeader("Access-Control-Max-Age", "3600");
    	response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
    	filterChain.doFilter(request, servletResponse);
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
