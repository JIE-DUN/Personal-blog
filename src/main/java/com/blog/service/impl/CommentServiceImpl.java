package com.blog.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.blog.dao.CommentDao;
import com.blog.entity.Comment;
import com.blog.service.CommentService;

/**
 * 评论
 */
@Service("commentService")
public class CommentServiceImpl implements CommentService {

	@Resource
	private CommentDao commentDao;
	
	@Override
	public Integer add(Comment comment) {
		return commentDao.add(comment);
	}

	@Override
	public Integer update(Comment comment) {
		return commentDao.update(comment);
	}

	@Override
	public List<Comment> list(Map<String, Object> paramMap) {
		return commentDao.list(paramMap);
	}

	@Override
	public Long getTotal(Map<String, Object> paramMap) {
		return commentDao.getTotal(paramMap);
	}

	@Override
	public Integer delete(Integer id) {
		return commentDao.delete(id);
	}

}
