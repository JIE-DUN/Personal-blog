package com.blog.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 操作字符串的工具类
 */
public class StringUtil {
	
	/**
	 * 在字符串前后加%
	 */
	public static String formatLike(String str){
		if(isNotEmpty(str)){
			//如果不为空，就在前后加%
			return "%"+str+"%";
		}
		return null;
	}
	
	/**
	 * 这个主要用于类似BlogAdminController.list方法
	 * 判断title是否不为空
	 */
	public static boolean isNotEmpty(String str){
		//判断是否为空
		//str.trim()把空格去掉
		if(str != null && !"".equals(str.trim())){
			//如果不为空
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String str){
		//判断是否为空
		//str.trim()把空格去掉
		if(str == null || "".equals(str.trim())){
			//如果不为空
			return true;
		}
		return false;
	}
	
	/**
	 * 对传入的list做处理，变成一个新的List，主要是为了过滤空格
	 */
	public static List<String> filterWhiter(List<String> list){
		List<String> resultList = new ArrayList<String>();
		for (String l : list) {
			if (isNotEmpty(l)) {
				resultList.add(l);
			}
		}
		return resultList;
	}
}
