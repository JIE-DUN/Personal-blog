package com.blog.entity;

import java.util.Date;

public class Comment {
	/** 主键 */
	private Integer id;
	/** 用户ip */
	private String userIp;
	/** 博客，主要是为了得到blogId,对应表格里的blogId */
	private Blog blog;
	/** 评论 */
	private String content;
	/** 评论时间 */
	private Date commentDate;
	/** 评论状态，比如审核通过，未审核，不通过 */
	private Integer state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
