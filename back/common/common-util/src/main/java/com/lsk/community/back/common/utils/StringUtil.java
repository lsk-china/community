package com.lsk.community.back.common.utils;

public class StringUtil {
	public static boolean isEmpty(String s){
		return s == null || s.trim().equals("");
	}
	public static boolean isNotEmpty(String s){
		return !isEmpty(s);
	}
	public static boolean noEmpty(String... ss) {
		boolean result = true;
		for (String s : ss) {
			result &= isNotEmpty(s);
		}
		return result;
	}
}
