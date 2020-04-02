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

public class SysException extends BaseException {

	private static final long serialVersionUID = 1L;

	public SysException(String message) {
		super(message);
	}
	
	public SysException(String code, String message) {
		super(code, message);
	}


	public SysException(String code, String message, Object[] param) {
		super(code, message, param);
	}

	
	public SysException(Throwable cause) {
		super(cause);
	}

	public SysException(String message, Throwable cause) {
		super(message, cause);
	}

	public String toString() {
		String s = getClass().getName();
		String message = super.getMessage();
		String code = super.getCode();
		StringBuilder stringBuilder = new StringBuilder(s);
		stringBuilder.append(message == null ? "" : (new StringBuilder()).append(": ").append(message).toString());
		stringBuilder.append(code == null || "".equals(code) ? " " : (new StringBuilder()).append("(").append(code).append(")").toString());
		return stringBuilder.toString();
	}

}
