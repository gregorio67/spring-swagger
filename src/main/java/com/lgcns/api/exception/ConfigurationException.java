/**
 * @Project : 병행검증 솔루션(Simulator Module)
 * @Class : BizException.java
 * @Description : 
 * @Author : 69800
 * @Since : 2019. 11. 1.
 * @Copyright ⓒ LG CNS
 *-------------------------------------------------------
 * Modification Information
 *-------------------------------------------------------
 * Date            Modifier             Reason 
 *-------------------------------------------------------
 * 2019. 11. 1.         69800             initial
 *-------------------------------------------------------
 */ 

package com.lgcns.api.exception;

import java.util.Locale;

import org.springframework.context.MessageSource;


public class ConfigurationException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException(String message) {
		super(message);
	}
	
	public ConfigurationException(String code, String message) {
		super(code, message);
	}

	public ConfigurationException(String code, String message, Object[] param) {
		super(code, message, param);
	}

	public ConfigurationException(Throwable cause) {
		super(cause);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigurationException(String code, MessageSource messageSource) {
		super(code, messageSource);
	}

	public ConfigurationException(String code, MessageSource messageSource,
			Object messageParameters[]) {
		super(code, messageSource, messageParameters);
	}

	public ConfigurationException(String code, MessageSource messageSource,
			Object messageParameters[], Locale locale) {
		super(code, messageSource, messageParameters, locale);
	}

	public String toString() {
		String s = getClass().getName();
		String message = super.getMessage();
		String code = super.getCode();
		StringBuilder stringBuilder = new StringBuilder(s);
		stringBuilder.append(message == null ? "" : (new StringBuilder())
				.append(": ").append(message).toString());
		stringBuilder.append(code == null || "".equals(code) ? " "
				: (new StringBuilder()).append("(").append(code).append(")")
						.toString());
		return stringBuilder.toString();
	}

}
