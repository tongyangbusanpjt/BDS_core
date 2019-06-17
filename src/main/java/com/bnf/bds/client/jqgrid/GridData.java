package com.bnf.bds.client.jqgrid;

import java.util.HashMap;
import java.util.List;

/**
 * 그리드 조회 결과를 저장할 데이터
 * 
 * @author 송정헌
 */
public class GridData extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private class GridResultKey {
		/**
		 * jsonReader.rows
		 */
		protected static final String DATAS	= "rows";
		/**
		 * jsonReader.records
		 */
		protected static final String TOTAL	= "records";
		/**
		 * jsonReader.total
		 */
		protected static final String PAGES	= "total";
	}
	
	/**
	 * 
	 * @param list
	 * @param total
	 * @param fetchcnt
	 */
	public GridData(List<?> list, int total, int fetchcnt) {
		int pages = (total / fetchcnt) + (total % fetchcnt > 0 ? 1 : 0);
		
		put(GridResultKey.DATAS, list);
		put(GridResultKey.TOTAL, total);
		put(GridResultKey.PAGES, pages);
	}
	
	/**
	 * 
	 * @param list
	 * @param total
	 * @param fetchcnt
	 */
	public GridData(List<?> list, int total, String fetchcnt) {
		this(list, total, Integer.parseInt(fetchcnt));
	}
	
	/**
	 * 페이징처리 하지 않을 경우
	 * @param list
	 */
	public GridData(List<?> list) {
		put(GridResultKey.DATAS, list);
	}
}
