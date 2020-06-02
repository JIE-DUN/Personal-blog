package com.blog.entity;

import java.io.Serializable;

/**
 * 博客类型
 *
 */
public class BlogType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/** 主键 */
	private Integer id;
	/** 类型名称 */
	private String typeName;
	/** 序号 */
	private Integer orderNo;
	/** 给类型下博客的的数量,这个变量在表上面其实是没有的，只是方便统计 */
	private Integer blogCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getBlogCount() {
		return blogCount;
	}

	public void setBlogCount(Integer blogCount) {
		this.blogCount = blogCount;
	}

}
