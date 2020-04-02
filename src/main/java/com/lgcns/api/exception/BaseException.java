/**
 * @Project : 병행검증 솔루션(Simulator Module)
 * @Class : BaseException.java
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;


public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String message;
	private String code;
	private Object[] param;
	
	public BaseException() {
		super();
		message = "";
		code = "";
		param = null;
	}

	public BaseException(String message) {
		super(message);
		this.code = "";
		this.param = null;
		this.message = message;
	}

	
	public BaseException(Throwable cause) {
		super(cause);
		this.message = "";
		this.code = "";
		this.param = null;
	}
	
	public BaseException(String code, String message) {
		super(message);
		this.message = message;
		this.code = code;
		this.param = null;
	}

	public BaseException(String code, String message, Object[] param) {
		super(message);
		this.message = message;
		this.code = code;
		this.param = param;
	}
	
	
	public BaseException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.code = "";
		this.param = null;
	}

	public BaseException(String code, MessageSource messageSource) {
		this(messageSource.getMessage(code, null, LocaleContextHolder.getLocale()));
		this.code = code;
	}

	public BaseException(String code, MessageSource messageSource,	Object messageParameters[]) {
		this(messageSource.getMessage(code, messageParameters, 	LocaleContextHolder.getLocale()));
		this.code = code;
	}

	public BaseException(String code, MessageSource messageSource,	Object messageParameters[], Locale locale) {
		this(messageSource.getMessage(code, messageParameters, locale));
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	protected void setMessage(String message) {
		this.message = message;
	}

	protected void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public Object[] getParam() {
		return param;
	}
	
	public void setParam(Object[] param) {
		this.param = param;
	}

	public String getStackTraceString() {
		StringWriter s = new StringWriter();
		super.printStackTrace(new PrintWriter(s));
		return s.toString();
	}

	public void printStackTrace(PrintWriter log) {
		log.println(getStackTraceString());
	}

}
