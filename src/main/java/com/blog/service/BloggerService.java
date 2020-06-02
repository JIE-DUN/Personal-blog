package com.blog.service;

import com.blog.entity.Blogger;

public interface BloggerService {
	/**根据登录名查询博主对象*/
	public Blogger getByUserName(String userName);
	
	/**更新博主对象*/
	public Integer update(Blogger blogger);

	/**查询博主,因为是个人博客，所以只有1个博主，这里就不设参数了*/
	public Blogger find();
}
