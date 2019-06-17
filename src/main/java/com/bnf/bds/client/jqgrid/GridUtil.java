package com.bnf.bds.client.jqgrid;

import java.util.List;

public class GridUtil {
	
	private GridUtil() {
		
	}
	
	/**
	 * 조회목록을 그리드 응답데이터 형태로 변환한다.
	 * 페이징처리시
	 * 
	 * @param list		데이터리스트
	 * @param total		전체건수
	 * @param fetchcnt	페치건수
	 * @return
	 */
	public static final GridData toGrid(List<?> list, int total, int fetchcnt) {
		return new GridData(list, total, fetchcnt);
	}
	
	/**
	 * 조회목록을 그리드 응답데이터 형태로 변환한다.
	 * 페이징처리시
	 * 
	 * @param list		데이터리스트
	 * @param total		전체건수
	 * @param fetchcnt	페치건수
	 * @return
	 */
	public static final GridData toGrid(List<?> list, int total, String fetchcnt) {
		return new GridData(list, total, fetchcnt);
	}
	
	/**
	 * 조회목록을 그리드 응답데이터 형태로 변환한다.
	 * 페이징미처리시
	 * 
	 * @param list		데이터리스트
	 * @return
	 */
	public static final GridData toGrid(List<?> list) {
		return new GridData(list);
	}
}
