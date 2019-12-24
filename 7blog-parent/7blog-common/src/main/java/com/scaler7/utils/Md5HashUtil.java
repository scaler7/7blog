package com.scaler7.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

public class Md5HashUtil {
	
	public static String toHex(String oriStr,String salt,Integer count) {
		Md5Hash md5Hash = new Md5Hash(oriStr, salt, count);
		return md5Hash.toString();
	}
	
//	public static void main(String[] args) {
//		String md5Hash = toHex("123456", Constant.salt, Constant.hashIterations);
//		System.out.println(md5Hash);
//	}
	
}
