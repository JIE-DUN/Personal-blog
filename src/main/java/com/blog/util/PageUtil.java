package com.blog.util;

/**
 * 翻页工具类
 */
public class PageUtil {
	
	
	/**
	 * 翻页方法
	 * @param targetUrl	地址
	 * @param totalNum	将要数据总数，比如多少条博客
	 * @param pageSize	每页多少条数据
	 * @param param		其他变量
	 * @return
	 */
	public static String genPagination(String targetUrl,long totalNum,
				int currentPage, int pageSize,String param){
		//总共页数
		if(totalNum==0){
			return "未查询到数据";
		}
		//计算应该有多少页
		long totalPage = 1L;
		if (totalNum%pageSize ==0 ) {
			totalPage = totalNum/pageSize;
		}else{
			totalPage = totalNum/pageSize + 1L;
		}
		
		StringBuffer pageCode = new StringBuffer();
		//首页和上一页显示点击
		pageCode.append("<li><a href='"+targetUrl+"?page=1&"+param+"'>首页</a></li>");
		if(currentPage>1){		//如果当前页不是首页,则"上一页"这个按钮能点击
			pageCode.append("<li><a href='"+targetUrl+"?page=" + (currentPage-1) + "&" + param + "'>上一页</a></li>");
		}else{			//当前页是首页，,则"上一页"这个按钮不能点击
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		}
		
		//显示页数
		for (int i = 1; i <= totalPage; i++) {
			if (i==currentPage) {		//如果是当前页就能点击
				pageCode.append("<li class='active'><a href='"+targetUrl+"?page="+i+"&"+param+"'>"+i+"</a></li>");
			}else{						//如果不是当前页就不能点击
				pageCode.append("<li><a href='"+targetUrl+"?page="+i+"&"+param+"'>"+i+"</a></li>");
			}
		}
		
		//下一页
		if(currentPage<totalPage){		//如果当前页不是最后一页,则"下一页"这个按钮能点击
			pageCode.append("<li><a href='"+targetUrl+"?page=" + (currentPage+1) + "&" + param + "'>下一页</a></li>");
		}else{			//当前页是最后一页，,则"下一页"这个按钮不能点击
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		}
		
		//尾页
		pageCode.append("<li><a href='" + targetUrl + "?page="+ totalPage +"&" + param + "'>尾页</a></li>");
		
		
		return pageCode.toString();
	}
}
