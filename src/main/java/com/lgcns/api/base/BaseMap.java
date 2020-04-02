package com.lgcns.api.base;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;


public class BaseMap extends ListOrderedMap{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4425050002082897138L;


	@Override
	public Object put(Object key, Object value) {
		return super.put(convert2CamelCase((String) key), value);
	}
	
	/**
	 * Return String
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return super.get(key) != null ? String.valueOf(super.get(key)) : null; 
	}
	
	/**
	 * Return int
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return super.get(key) != null ? Integer.parseInt(String.valueOf(super.get(key))) : null;
	}
	
	/**
	 * Return long
	 * @param key
	 * @return
	 */
	public long getLong(String key) {
		return super.get(key) != null ? Long.parseLong(String.valueOf(super.get(key))) : null;
	}
	
	/**
	 * Return float
	 * @param key
	 * @return
	 */
	public float getFloat(String key) {
		return super.get(key) != null ? Float.parseFloat(String.valueOf(super.get(key))) : null;
	}
	
	/** 
	 * Return boolean
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key) {
		return  super.get(key) != null ? Boolean.getBoolean(String.valueOf(super.get(key))) : null;
	}
	
	/**
	 * Return BaseMap
	 * @param key
	 * @return
	 */
	public BaseMap getMap(String key) {
		BaseMap baseMap = null;
		if ( super.get(key) != null && super.get(key) instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) super.get(key);
			baseMap = new BaseMap();
			for (String k : map.keySet()) {
				baseMap.put(k, map.get(k));
			}
		}
		else if (super.get(key) != null && super.get(key) instanceof BaseMap) {
			baseMap = (BaseMap) super.get(key);
		}
		return baseMap;
	}
	
	/**
	 * Return list
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T>List<T> getList(String key) {
		List<T> result = null;
		
		if ( super.get(key) != null && super.get(key) instanceof List) {
			result = (List<T>) super.get(key);
		}
		return result;
	}
	/**
	 * Convert camel
	 * @param underScore
	 * @return
	 */
	public static String convert2CamelCase(String underScore) {

		// '_' 가 나타나지 않으면 이미 camel case 로 가정함.
		// 단 첫째문자가 대문자이면 camel case 변환 (전체를 소문자로) 처리가
		// 필요하다고 가정함. --> 아래 로직을 수행하면 바뀜
		if (underScore.indexOf('_') < 0 && Character.isLowerCase(underScore.charAt(0))) {
			return underScore;
		}
		StringBuilder result = new StringBuilder();
		boolean nextUpper = false;
		int len = underScore.length();

		for (int i = 0; i < len; i++) {
			char currentChar = underScore.charAt(i);
			if (currentChar == '_') {
				nextUpper = true;
			} else {
				if (nextUpper) {
					result.append(Character.toUpperCase(currentChar));
					nextUpper = false;
				} else {
					result.append(Character.toLowerCase(currentChar));
				}
			}
		}
		return result.toString();
	}
}
