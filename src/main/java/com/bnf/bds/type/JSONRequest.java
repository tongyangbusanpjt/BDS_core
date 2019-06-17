package com.bnf.bds.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bnf.bds.type.mapper.ObjectMappingException;
import com.bnf.bds.type.mapper.ValueObjectMapper;

/**
 * MappingJacksonHttpMessageConverter 랑 사용
 * 컨트롤러 핸들러 메소드의 파라메터로
 * 어노테이션 @RequestBody 를 붙여서 사용
 * 
 * @author 송정헌
 */
public class JSONRequest extends HashMap<String, Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 파라메터를 String 형식으로 반환한다.
	 * 
	 * @param paramName	파라메터명
	 * @return
	 */
	public String getString(String paramName) {
		return String.valueOf(get(paramName));
	}
	
	/**
	 * 파라메터를 Integer 형식으로 반환한다.
	 * 
	 * @param paramName	파라메터명
	 * @return
	 */
	public int getInt(String paramName) {
		Object object = get(paramName);
		if (object instanceof Integer) {
			return (Integer) object;
		}
		else {
			try {
				return Integer.parseInt(String.valueOf(object));
			}
			catch (NumberFormatException nfe) {
				return 0;
			}
		}
	}
	
	/**
	 * 파라메터를 VO 객체 형식으로 반환한다.
	 * 
	 * @param paramName	파라메터명
	 * @param type		VO 타입 class
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public <_T> _T getCommand(String paramName, Class<_T> type) throws Exception {
		Object value = get(paramName);
		
		if (value == null) {
			throw new ObjectMappingException("Invalid Parameter Name: \"" + paramName + "\"");
		}
		
		return (_T) new ValueObjectMapper(type).toObject((Map) value);
	}
	
	/**
	 * 파라메터를 List&lt;VO&gt; 형식으로 반환한다.
	 * 
	 * @param paramName	파라메터명
	 * @param type		VO 타입 class
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public <_T> List<_T> getList(String paramName, Class<_T> type) throws Exception {
		Object value = get(paramName);
		
		if (value == null) {
			throw new ObjectMappingException("Invalid Parameter Name: \"" + paramName + "\"");
		}
		
		return (List<_T>) new ValueObjectMapper(type).toList(value);
	}
	
	@SuppressWarnings("unchecked")
	public <_T> Map<String, Object> getMap(String paramName, Class<_T> type) throws Exception {
		Object value = get(paramName);
		
		if (value == null) {
			throw new ObjectMappingException("Invalid Parameter Name: \"" + paramName + "\"");
		}
		
		return (Map<String, Object>) new ValueObjectMapper(type).toMap((Map)value);
	}
	
	
	
	// 2017.04.19 김재환 추가 
	@SuppressWarnings("unchecked")
	public List<String> getStringList(String paramName) throws Exception {
		Object value = get(paramName);
		
		if (value == null) {
			throw new ObjectMappingException("Invalid Parameter Name: \"" + paramName + "\"");
		}
		
		return (List<String>) value;
	}
	
	
}
