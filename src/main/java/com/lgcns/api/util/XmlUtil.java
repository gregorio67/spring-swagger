package com.lgcns.api.util;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmlUtil {

	public static final Logger LOGGER = LoggerFactory.getLogger(XmlUtil.class);

	private XmlUtil() {
		/** Do nothing **/
	}
	/**
	 * Return XML with object
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static String  toXml(Object object) throws Exception {
		
		String json = JsonUtil.toJson(object);
		return toXml(json);

	}

	/**
	 * Convert JSON string to XML
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public static String toXml(String json) throws Exception {
		JSONObject jsonData = new JSONObject(json);
		String xml = XML.toString(jsonData);
		return xml;
	}
	
	/**
	 * Convert XML to Object
	 * @param xml
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T toObject( String xml, Class<T> clazz) throws Exception {
		/** Convert XML to JSON **/
		String json =  XML.toJSONObject(xml).toString();;
		
		/** Convert JSON to Object **/
		T result =JsonUtil.toObject(json, clazz);
		
		return result;
	}
}
