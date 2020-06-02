package com.blog.dao;

import java.util.List;
import java.util.Map;

import com.blog.entity.Blog;


public interface BlogDao {
	/**无参数查询所有博客(提供首页使用)*/
	public List<Blog> countList();
	
	/**根据不固定参数查询博客列表*/
	public List<Blog> list(Map<String,Object> paramMap);
	
	/**不固定参数查询博客数量*/
	public Long getTotal(Map<String,Object> paramMap);
	
	/**根据id查询博客*/
	public Blog findById(Integer id);
	
	/**添加一条博客*/
	public Integer add(Blog blog);
	
	/**修改一条博客*/
	public Integer update(Blog blog);
	
	/**删除一条博客*/
	public Integer delete(Integer id);
	
	/**根据类型查询博客数量*/
	public Integer getBlogTypeById(Integer typeId);
	
	/**上一篇，主要是提供给博客页面快捷翻页的*/
	public Blog getLastBlog(Integer id);
	
	/**下一篇，主要是提供给博客页面快捷翻页的*/
	public Blog getNextBlog(Integer id);
}
