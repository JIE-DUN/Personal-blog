package com.blog.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 密码加密的工具类
 */
public class CryptographyUtil {

	/**
	 * md5加密
	 */
	public static String md5(String str,String salt){
		return new Md5Hash(str,salt).toString();
	}
	
	public static void main(String[] args) {
		System.out.println(md5("1", "java1234"));
	}
	
}
