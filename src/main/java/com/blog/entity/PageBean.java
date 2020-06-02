package com.blog.entity;

/**
 * 这个bean主要是为了翻页这项业务
 */
public class PageBean {
	/**当前第几页，从1开始*/
	private int page;
	/**页面大小，即是一个页面能放多少条数据*/
	private int pageSize;
	/**
	 * 根据当前页面计算第一条数据是数据库里的第几条
	 * 比如第一页第一条是0，第二页第一条是5
	 */
	private int start;
	
	public PageBean(int page, int pageSize) {
		super();
		this.page = page;
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStart() {
		return (this.page - 1) * this.pageSize;
	}
	
}
