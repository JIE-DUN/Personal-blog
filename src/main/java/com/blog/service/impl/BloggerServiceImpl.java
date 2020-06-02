package com.blog.service.impl;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.blog.dao.BloggerDao;
import com.blog.entity.Blogger;
import com.blog.service.BloggerService;
import com.blog.util.Const;

@Service("bloggerService")
public class BloggerServiceImpl implements BloggerService{
	
	@Resource
	private BloggerDao bloggerDao;

	@Override
	public Blogger getByUserName(String userName) {
		return bloggerDao.getByUserName(userName);
	}

	@Override
	public Integer update(Blogger blogger) {
		//修改Session里的内容，使个人信息更新之后，刷新就能得到
		SecurityUtils.getSubject().getSession().setAttribute(Const.CURRENT_USER, blogger);
		return bloggerDao.update(blogger);
	}

	@Override
	public Blogger find() {
		return bloggerDao.find();
	}

}
