package com.bnf.bds.util;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.HandlerMapping;

/**
 * 
 * @author SJH
 */
public abstract class RequestUtils {
	
	public static String getRequestURIFromHandlerMapping(HttpServletRequest request) {
		String uri = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		return uri != null ? uri : request.getRequestURI();
	}
	
	public static String getRequestURIFromForward(HttpServletRequest request) {
		String uri = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
		return uri != null ? uri : request.getRequestURI();
	}
	public static String getRequestURIFromInclude(HttpServletRequest request) {
		String uri = (String) request.getAttribute(RequestDispatcher.INCLUDE_REQUEST_URI);
		return uri != null ? uri : request.getRequestURI();
	}
	
	public static String getServletPathFromForward(HttpServletRequest request) {
		String uri = (String) request.getAttribute(RequestDispatcher.FORWARD_SERVLET_PATH);
		return uri != null ? uri : request.getServletPath();
	}
	
	public static String getServletPathFromInclude(HttpServletRequest request) {
		String uri = (String) request.getAttribute(RequestDispatcher.INCLUDE_SERVLET_PATH);
		return uri != null ? uri : request.getServletPath();
	}
	
	public static boolean isAjax(HttpServletRequest request) {
		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
	}
	
}
