package com.bnf.bds.encode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 
 * @author SJH
 */
public class StringEncoder {
	
	public static String iso(String src) {
		try {
			return new String(src.getBytes("UTF-8"), "ISO-8859-1");
		}
		catch (UnsupportedEncodingException uee) {
			return src;
		}
	}
	
	public static String downloadIso(String src) {
		try {
			return new String(src.getBytes("UTF-8"), "ISO-8859-1");
//			return new String(src.getBytes("EUC-KR"), "ISO-8859-1");
		}
		catch (UnsupportedEncodingException uee) {
			return src;
		}
	}
	
	public static String downloadIsoForWindows(String src) {
		try {
//			return new String(src.getBytes("UTF-8"), "ISO-8859-1");
			return new String(src.getBytes("EUC-KR"), "ISO-8859-1");
		}
		catch (UnsupportedEncodingException uee) {
			return src;
		}
	}
	
	public static String encodeURL(String src) {
		try {
			return URLEncoder.encode(src, "UTF-8");
		}
		catch (UnsupportedEncodingException uee) {
			return src;
		}
	}
}
