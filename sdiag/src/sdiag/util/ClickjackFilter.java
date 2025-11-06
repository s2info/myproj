package sdiag.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.util.UrlPathHelper;

import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;

public class ClickjackFilter implements Filter {

	private String mode="SAMEORIGIN";
	//private String ajaxHeader = "AJAX";
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse)response;
		HttpServletRequest req = (HttpServletRequest)request;
		
		//res.addHeader("X-FRAME-OPTIONS", mode);
		res.addHeader("X-Content-Type-Options", "nosniff");
		res.addHeader("X-XSS-Protection","1;mode=block");
		//res.addHeader("Content-Security-Policy","script-src 'self';object-src 'self'");
		
		chain.doFilter(request, response);
		
		
	}
	/*
	private boolean isAjaxRequest(HttpServletRequest req){
		System.out.println(req.getHeader(ajaxHeader) + "][");
		return req.getHeader(ajaxHeader) != null && req.getHeader(ajaxHeader).equals(Boolean.TRUE.toString());
	}
*/
	public void init(FilterConfig fc) throws ServletException {
		String cfMode = fc.getInitParameter("mode");
		if(cfMode != null){
			mode = cfMode;
		}
	}

	public void destroy() {
		
	}
}
