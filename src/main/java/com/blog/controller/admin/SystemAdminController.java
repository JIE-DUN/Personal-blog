package com.blog.controller.admin;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.blog.entity.Blog;
import com.blog.entity.BlogType;
import com.blog.entity.Blogger;
import com.blog.entity.Link;
import com.blog.service.BlogService;
import com.blog.service.BlogTypeService;
import com.blog.service.BloggerService;
import com.blog.service.LinkService;
import com.blog.util.Const;
import com.blog.util.ResponseUtil;

import net.sf.json.JSONObject;

/**
 * 在写博客的页面，博客类型的下拉列表数据，是直接在服务器初始化的时候从缓存的
 * 所以添加新的类型时，缓存并没有更新，这个列表也不会更新
 * 所以这里做一个缓存操作，将类似这个列表里的缓存数据清除，并重新缓存一次
 */
@Controller
@RequestMapping({"/admin/system"})
public class SystemAdminController {

	@Resource
	private BlogTypeService blogTypeService;
	@Resource
	private BloggerService bloggerService;
	@Resource
	private BlogService blogService;
	@Resource
	private LinkService linkService;
	
	
	/**
	 * 刷新系统缓存
	 * @throws Exception 
	 */
	@RequestMapping({"/refreshSystem"})
	public String refreshSystem(HttpServletRequest request,HttpServletResponse response) throws Exception{
		//先获取上下文
		ServletContext application = RequestContextUtils.getWebApplicationContext(request).getServletContext();
		
		//获取博客类型的数据
		List<BlogType> blogTypeList = blogTypeService.countList();
		//获取博主数据
		Blogger blogger = bloggerService.find();
		//因为存到applicationContext，所以为了安全起见，密码就设置为空了
		blogger.setPassword(null);
		//获取博客列表
		List<Blog> blogCountList = blogService.countList();
		//获取友情链接
		List<Link> linkList = linkService.list(null);
		
		//放到application里，这里第一个参数的名字要和服务器一开始就初始化使用的名字相同
		//所以加入了一个公共参数类，防止写错
		application.setAttribute(Const.BLOG_TYPE_COUNT_LIST, blogTypeList);
		application.setAttribute(Const.BLOGGER, blogger);
		application.setAttribute(Const.BLOG_COUNT_LIST, blogCountList);
		application.setAttribute(Const.LINK_LIST, linkList);
		
		//告诉jsp已经刷新成功
		JSONObject result = new JSONObject();
		result.put("success", Boolean.TRUE);
		ResponseUtil.write(response, result);
		return null;
	}
	
	
	
	
	
	
	
	
	
	
}
