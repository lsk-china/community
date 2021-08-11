package com.lsk.community.back.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
	private int code;
	private Object data;
	private String message;
}
