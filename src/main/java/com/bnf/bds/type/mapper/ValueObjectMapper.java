package com.bnf.bds.type.mapper;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;


/**
 * 
 * @author 송정헌
 */
public class ValueObjectMapper {

	private Class<?> objectType = null;
	private PropertyDescriptor[] propertyDescriptors = null;
	/**
	 * 
	 * @param objectType
	 */
	public <_T> ValueObjectMapper(Class<_T> objectType) {
		this.objectType = objectType;
		this.propertyDescriptors = BeanUtils.getPropertyDescriptors(objectType);
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Object toObject(@SuppressWarnings("rawtypes") Map map) throws Exception {
		Object voitem = objectType.newInstance();
		
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method setter = propertyDescriptor.getWriteMethod();
			if (setter != null) {
				Object mapval = map.get(propertyDescriptor.getName());
				Object voval = null;
				
				if (propertyDescriptor.getPropertyType().isArray()) {
					if (mapval instanceof Object[]) {
						voval = mapval;
						
					} else if (mapval instanceof Object) {
						voval = new Object[] {mapval};
					}
				
				} else {
					if (mapval instanceof Object[]) {
						if (((String[])mapval).length > 0) {
							voval = ((Object[])mapval)[0];
						}
					} else if (mapval instanceof Object) {
						voval = mapval;
					}
				}
				
				if (voval != null) {
					setterInvoke(setter, voitem, voval);
				}
			}
		}
		
		return voitem;
	}
	
	/**
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Object[] toArray(@SuppressWarnings("rawtypes") Map map) throws Exception {
		return toList(map).toArray();
	}
	
	/**
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<?> toList(Object object) throws Exception {
		if (object instanceof List) {
			return toListFromList((List) object);
		}
		else {
			return toListFromMap((Map) object);
		}
	}
	
	private List<?> toListFromMap(@SuppressWarnings("rawtypes") Map map) throws Exception {
		List<Object> volist = new ArrayList<Object>();
		int propertyMaxCount = getPropertyMaxCount(map);
		
		for (int index = 0; index < propertyMaxCount; index++) {
			Object voitem = objectType.newInstance();
			
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				Method setter = propertyDescriptor.getWriteMethod();
				if (setter != null) {
					Object mapval = map.get(propertyDescriptor.getName());
					Object voval = null;
					
					if (mapval instanceof Object[]) {
						if (((String[])mapval).length > index) {
							voval = ((Object[])mapval)[index];
						}
						
					} else if (mapval instanceof Object) {
						if (index == 0) {
							voval = mapval;
						}
					}
					
					if (voval != null) {
						setterInvoke(setter, voitem, voval);
					}
				}
			}
			
			volist.add(voitem);
		}
		
		return volist;
	}
	
	private List<?> toListFromList(@SuppressWarnings("rawtypes") List<Map> maps) throws Exception {
		List<Object> volist = new ArrayList<Object>();
		
		for (@SuppressWarnings("rawtypes") Map item : maps) {
			volist.add(toObject(item));
		}
		
		return volist;
	}
	
	private void setterInvoke(Method setter, Object vo, Object voval) throws Exception {
		Class<?> valueType = setter.getParameterTypes()[0];
		Object castValue = null;
		
		if (valueType == int.class) {
			try { castValue = Integer.parseInt(voval.toString()); } catch (NumberFormatException nfe) { castValue = 0; }
			
		} else if (valueType == long.class) {
			try { castValue = Long.parseLong(voval.toString()); } catch (NumberFormatException nfe) { castValue = 0L; }
		
		} else if (valueType == String.class) {
			castValue = voval.toString();
		}
		
		setter.invoke(vo, castValue != null ? castValue : voval);
	}
	
	private int getPropertyMaxCount(@SuppressWarnings("rawtypes") Map map) {
		int maxCount = 0;
		
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method setter = propertyDescriptor.getWriteMethod();
			
			if (setter != null) {
				int objslen = 0;
				Object value = map.get(propertyDescriptor.getName());
				
				if (value instanceof String[]) {
					objslen = ((String[])value).length;
					
				} else if (value instanceof List) {
					objslen = ((List<?>)value).size();
					
				} else if (value instanceof String) {
					objslen = 1;
				}
				
				maxCount = objslen > maxCount ? objslen : maxCount;
			}
		}
		
		return maxCount;
	}

	/** 
	 * <pre>
	 * 
	 * </pre>
	 *
	 * @param value
	 * @return
	 */
	public Map<String, Object> toMap(@SuppressWarnings("rawtypes") Map map) {
		
		
		return map;
	}
}
