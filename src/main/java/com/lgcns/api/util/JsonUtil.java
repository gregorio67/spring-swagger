package com.lgcns.api.util;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class JsonUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
	
	/** Initializing Gson **/
	private static Gson gson = null;
	static {
		GsonBuilder builder = new GsonBuilder();
		/**gson = builder.serializeNulls().setLenient().setPrettyPrinting().create();**/
		gson = builder.serializeNulls().setLenient().create();
	}
	
	private JsonUtil() {
		/** Do nothing **/
	}


	/** 
	 * Return class instance 
	 * @param json
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T>  T toObject(String json, Class<T> clazz) throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{} is converted {}", json, clazz.getName());
		}
		return gson.fromJson(json, clazz);
	}
	
	/**
	 * Return JSON string
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object clazz) throws Exception {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("{} is converted {}", clazz.getClass().getName());
		}
		return gson.toJson(clazz);
	}
	
	/**
	 * Convert JSON to XML
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static String json2Xml(String json) throws Exception {
		JSONObject object = new JSONObject(json);
		String xml = XML.toString(object);
		return xml;
	}
	
	/**
	 * Convert XML to JSON
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static String xml2Json(String xml) throws Exception {
		JSONObject object = XML.toJSONObject(xml);
		return object.toString();
	}
}
