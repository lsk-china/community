package com.lsk.community.back.common.response;

/**
 * StatusCode
 * 返回状态码
 */

public class StatusCode extends RuntimeException{
	private int code;
	public StatusCode(int code, String message, Throwable t) {
		super(message, t);
		this.code = code;
	}
	public StatusCode(int code, String message) {
		super(message);
		this.code = code;
	}
	public int getCode() {
		return this.code;
	}
}
