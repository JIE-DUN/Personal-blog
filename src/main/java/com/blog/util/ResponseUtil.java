package com.blog.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * 写入response的工具类
 */
public class ResponseUtil {
	public static void write(HttpServletResponse response,Object o) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(o.toString());
		out.close();
	}
}
