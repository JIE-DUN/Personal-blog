package com.blog.dao;

import java.util.List;
import java.util.Map;

import com.blog.entity.Comment;

/**
 * 评论
 *
 */
public interface CommentDao {
	/**添加一条评论*/
	public Integer add(Comment comment);
	
	/**更新一条评论,游客不能更新，但是后台博主能更新评论状态（这个字段state）*/
	public Integer update(Comment comment);
	
	/**评论查询,这个项目的Mapper只做了根据状态查询*/
	public List<Comment> list(Map<String,Object> paramMap);
	
	/**评论数*/
	public Long getTotal(Map<String,Object> paramMap);
	
	/**删除评论*/
	public Integer delete(Integer id);
	
	/**根据博客id删除评论*/
	public Integer deleteByBlogId(Integer id);
	
}
