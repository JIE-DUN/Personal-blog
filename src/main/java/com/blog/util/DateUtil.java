package com.blog.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

/**
 * 日期工具类
 *
 */
public class DateUtil {
	
	/**
	 * 得到当前精确到秒的事件字符串
	 * 主要用于用户上传头像
	 */
	public static String getCurrentDateStr() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		return sdf.format(date);
	}
	
	public static String formatDate(Date date,String format) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if(date!=null){
			result = sdf.format(date);
		}

		return result;
	}
}
