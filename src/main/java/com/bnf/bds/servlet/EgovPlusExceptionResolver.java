package com.bnf.bds.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.bnf.bds.EgovPlusException;
import com.bnf.bds.util.RequestUtils;

/**
 * 
 * @author SJH
 */
public class EgovPlusExceptionResolver implements HandlerExceptionResolver {
	private String viewLocation;

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		String message = null;
		if (ex instanceof EgovPlusException) {
			message = ex.getMessage();
		}
		else {
			/**
			 * 2017.03.28 장세환 수정 , 일반 익셉션인 경우 에러 메시지를 정제하여 보냄
			 */
			//message = ex.toString();
			message = "시스템 내부 오류가 발생 하였습니다";
		}
		
		return getModelAndView(request).addObject("message", message);
	}
	
	private ModelAndView getModelAndView(HttpServletRequest request) {
		if (RequestUtils.isAjax(request)) {
			return new ModelAndView("jsonView");
		}
		else {
			return new ModelAndView(this.viewLocation);
		}
	}

	public String getViewLocation() {
		return viewLocation;
	}

	public void setViewLocation(String viewLocation) {
		this.viewLocation = viewLocation;
	}
	
	
}
