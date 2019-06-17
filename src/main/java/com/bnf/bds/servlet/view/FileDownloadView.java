package com.bnf.bds.servlet.view;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import com.bnf.bds.encode.StringEncoder;

/**
 * 
 * @author SJH
 */
public class FileDownloadView extends AbstractView implements InitializingBean {
	
	public static final String FILE = "file";
	public static final String FILENAME = "filename";
	
	
	private static final Logger log = LoggerFactory.getLogger(FileDownloadView.class);
	
	private boolean useCookie = false;
	private String cookieName  = "FileDownloadView";
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
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		// 처리도중 에러 발생해도 클라이언트까지 전달되어야 함.
		if (useCookie) {
			response.addCookie(cookie);
		}
		
		Object file = model.get(FILE);
		String filename = (String) model.get(FILENAME);
		
		try {
			FileInfo fileInfo = getInputStream(file);
			

			if (request.getHeader("User-Agent").contains("Windows")) {
				response.setHeader("Content-Disposition", "attachment; filename=\"" + StringEncoder.downloadIsoForWindows(filename) + "\";");
			} else {
				response.setHeader("Content-Disposition", "attachment; filename=\"" + StringEncoder.downloadIso(filename) + "\";");
			}
			

			
			response.setHeader("Content-Transfer-Encoding", "binary;");
			response.setContentType("text/html;");
			response.setContentLength(fileInfo.getFileSize());
			
			FileCopyUtils.copy(fileInfo.getInputStream(), response.getOutputStream());	//StreamUtils.copy(in, out);
		}
		catch (IOException ioe) {
			log.error("{}", ioe);
			
			String message = null;
			if (ioe instanceof FileNotFoundException) {
				message = "파일을 찾을 수 없습니다.";
			}
			else {
				message = "파일 다운로드중 에러가 발생하였습니다";
			}
			response.getWriter().write("<script>alert('" + StringEncoder.iso(message) + "');</script>");
		}
	}
	
	private FileInfo getInputStream(Object object) throws IOException {
		InputStream inputStream = null;
		int filesize = 0;
		
		/*
		 * File
		 */
		if (object instanceof File) {
			File file = (File) object;
			
			inputStream = new BufferedInputStream(new FileInputStream(file));
			filesize = (int) file.length();
		}
		/*
		 * InputStream
		 */
		else if (object instanceof InputStream) {
			inputStream = new BufferedInputStream((InputStream) object);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FileCopyUtils.copy(inputStream, baos);
			
			inputStream.reset();
			filesize = baos.size();
			
			IOUtils.closeQuietly(baos);
		}
		/*
		 * Workbook
		 */
		else if (object instanceof Workbook) {
			Workbook workbook = (Workbook) object;
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);
			
			inputStream = new ByteArrayInputStream(baos.toByteArray());
			filesize = baos.size();
			
			IOUtils.closeQuietly(workbook);
			IOUtils.closeQuietly(baos);
		}
		
		FileInfo fileInfo = new FileInfo();
		fileInfo.setInputStream(inputStream);
		fileInfo.setFileSize(filesize);
		return fileInfo;
	}
	
	
	/**
	 * 
	 */
	private class FileInfo {
		
		private InputStream inputStream;
		private int fileSize;
		
		public InputStream getInputStream() {
			return inputStream;
		}
		public void setInputStream(InputStream inputStream) {
			this.inputStream = inputStream;
		}
		public int getFileSize() {
			return fileSize;
		}
		public void setFileSize(int fileSize) {
			this.fileSize = fileSize;
		}
	}
	
	
	
}
