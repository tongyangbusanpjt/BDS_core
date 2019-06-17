package com.bnf.bds.servlet.view;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.bnf.bds.encode.StringEncoder;

import egovframework.rte.psl.dataaccess.util.EgovMap;

/**
 * 
 * @author SJH
 */
public class ExcelDownloadView extends AbstractExcelView implements InitializingBean {
	
	public static final String DATAS = "datas";
	public static final String HEADERS = "headers";
	public static final String FILENAME = "filename";
	
	
	private static final Logger log = LoggerFactory.getLogger(ExcelDownloadView.class);
	
	private static final String EXTENSION = ".xls";
	private static final String DEFAULT_FONT = "맑은 고딕";
	
	private boolean useCookie = false;
	private String cookieName  = "ExcelDownloadView";
	private String cookieValue = "ready";
	private Cookie cookie;
	
	public void setUseCookie(boolean useCookie) {
		this.useCookie = useCookie;
	}
	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}
	public void setCookieValue(String cookieValue) {
		this.cookieValue = cookieValue;
	}
	
	
	public void afterPropertiesSet() throws Exception {
		if (useCookie) {
			if (StringUtils.isEmpty(cookieName) || StringUtils.isEmpty(cookieValue)) {
				throw new IllegalArgumentException("cookieName and cookieValue must be not empty.");
			}
			cookie = new Cookie(cookieName, cookieValue);
			cookie.setPath("/");
		}
	}
	
	
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook,
			HttpServletRequest request,	HttpServletResponse response) throws Exception {
		
		// 처리도중 에러 발생해도 클라이언트까지 전달되어야 함.
		if (useCookie) {
			response.addCookie(cookie);
		}
		
		// get data model which is passed by the Spring container
		String[] headers = (String[]) model.get(HEADERS);
		List<?> datas = (List<?>) model.get(DATAS);
		String filename = (String) model.get(FILENAME);
		
		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet();
		workbook.setSheetName(0, filename);
	//	sheet.setDefaultColumnWidth(20);
		
		// create style for header cells
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
		headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font headerFont = workbook.createFont();
		headerFont.setFontName(DEFAULT_FONT);
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		headerFont.setColor(HSSFColor.WHITE.index);
		headerStyle.setFont(headerFont);
		
		// create style for data cells
		CellStyle dataStyle = workbook.createCellStyle();
		Font dataFont = workbook.createFont();
		dataFont.setFontName(DEFAULT_FONT);
		dataStyle.setFont(dataFont);
		
		// create header row
		HSSFRow header = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			header.createCell(i).setCellValue(headers[i]);
			header.getCell(i).setCellStyle(headerStyle);
		}
		
		// create data rows
		for (int i = 0; i < datas.size(); i++) {
			HSSFRow row = sheet.createRow(i + 1);
			EgovMap rowData = (EgovMap) datas.get(i);
			
			int j = 0;
			for (Object key : rowData.keySet()) {
				row.createCell(j).setCellValue((String) rowData.get(key));
				row.getCell(j++).setCellStyle(dataStyle);
			}
		}
		
		// response
		response.setHeader("Content-Disposition", "attachment; filename=\"" + StringEncoder.downloadIso(filename + EXTENSION) + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setContentType("text/html;");
		response.setContentLength(getFileSize(workbook));
	}
	
	private int getFileSize(Workbook workbook) {
		int size = 0;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			workbook.write(baos);
			size = baos.size();
		}
		catch (IOException ioe) {
			log.error("{}", ioe);
		}
		finally {
			IOUtils.closeQuietly(baos);
		}
		
		return size;
	}
}
