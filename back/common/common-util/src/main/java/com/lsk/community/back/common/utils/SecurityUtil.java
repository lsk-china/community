package com.lsk.community.back.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
	public static String md5(String str) {
		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(str.getBytes());
			return bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			// 此异常不可能发生
			return "";
		}
	}
	public static String sha1(String str) {
		try {
			MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
			mDigest.update(str.getBytes());
			return bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			// 此异常不可能发生
			return "";
		}
	}
	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
			//MD5加密后bytes长度16转换成32位16进制字符串
			for (int i = 0; i < bytes.length; i++) {
			/**
			 * 在32位的电脑中数字都是以32格式存放的，如果是一个byte(8位)类型的数字，
			 * 他的高24位里面都是随机数字，低8位才是实际的数据。
			 * java.lang.Integer.toHexString() 方法的参数是int(32位)类型.
			 * 如果输入一个byte(8位)类型的数字，这个方法会把这个数字的高24为也看作有效位，
			 * 这就必然导致错误，使用& 0XFF操作，可以把高24位置0以避免这样错误.
			 *
			 * 0xFF = 1111 1111　 低8位为1，高位都为0
			 * 故 &0xFF 可将数字的高位都置为0，低8位不变
			 *
			 * */
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}
