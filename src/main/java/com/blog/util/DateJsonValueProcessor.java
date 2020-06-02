package com.blog.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * 将日期转换格式
 * 转换成Timestamp或者Date格式
 *
 */
public class DateJsonValueProcessor implements JsonValueProcessor {

	private String format;
	
	public DateJsonValueProcessor(String format){
		this.format = format;
	}
	
	@Override
	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		if(value==null){
			return "";
		}
		if(value instanceof Timestamp){
			String str = new SimpleDateFormat(this.format).format((Timestamp)value);
			return str;
		}
		if(value instanceof Date){
			String str = new SimpleDateFormat(this.format).format((Date)value);
			return str;
		}
		return value.toString();
	}

}
