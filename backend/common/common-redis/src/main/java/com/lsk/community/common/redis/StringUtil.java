package com.lsk.community.common.redis;

public class StringUtil {
	public static final boolean isEmpty(String s){
		return s == null || s.trim().equals("");
	}
}
