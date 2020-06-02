package com.blog.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.blog.entity.Blog;
import com.blog.lucene.BlogIndex;
import com.blog.service.BlogService;
import com.blog.service.CommentService;
import com.blog.util.StringUtil;

@Controller
@RequestMapping({"/blog"})
public class BlogController {
	
	@Resource
	private BlogService blogService;
	
	@Resource
	private CommentService commentService;
	
	private BlogIndex blogIndex = new BlogIndex();
	
	@RequestMapping({"/articles/{id}"})
	public ModelAndView details(@PathVariable("id")Integer id,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		//根据主键查询博客信息
		Blog blog = blogService.findById(id);
		mav.addObject("blog", blog);
		
		//点击数+1
		blog.setClickHit(blog.getClickHit()+1);
		//把包含更行后点击数的Blog更新
		blogService.update(blog);
		
		//按照blogId得到评论
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("blogId", blog.getId());
		paramMap.put("state", 1);			//查询已经审核过的评论
		commentService.list(paramMap);
		
		//设置mainPage参数，以及对应页面标题
		mav.addObject("mainPage", "foreground/blog/view.jsp");
		mav.addObject("pageTitle", blog.getTitle()+"_个人博客系统");
		//设置上一篇以及下一篇的链接
		mav.addObject("pageCode", getUpAndDownPageCode(blogService.getLastBlog(id), blogService.getNextBlog(id), request.getServletContext().getContextPath()));
		mav.addObject("commentList",commentService.list(paramMap));
		
		//处理关键字显示
		String keyWord = blog.getKeyWord();
		if (StringUtil.isEmpty(keyWord)) {
			mav.addObject("keyWords",null);
		}else {
			String[] arr = keyWord.split(" ");
			//Arrays.asList(keyWord)转换成list
			//再用StringUtil.filterWhiter,把空格去掉
			mav.addObject("keyWords",StringUtil.filterWhiter(Arrays.asList(arr)));
		}
		
		//设置ModelAndView要返回的jsp页面
		mav.setViewName("index");
		return mav;
	}
	
	/**
	 * 上一篇，下一篇
	 * @param lastBlog			上一篇Blog
	 * @param nextBlog			下一篇Blog
	 * @param projectContext	上下文
	 * @return
	 */
	public String getUpAndDownPageCode(Blog lastBlog,Blog nextBlog,String projectContext){
		StringBuffer pageCode = new StringBuffer();
		//设置上一篇对应的链接
		if (lastBlog==null||lastBlog.getId()==null) {
			pageCode.append("<p>上一篇，没有了！</p>");
		}else{
			pageCode.append("<p>上一篇:<a href='"+projectContext+"/blog/articles/"+ lastBlog.getId()+".html'>"
						+lastBlog.getTitle()+"</a></p>");
		}
		
		//设置下一篇对应的链接
		if (nextBlog==null||nextBlog.getId()==null) {
			pageCode.append("<p>下一篇，没有了！</p>");
		}else{
			pageCode.append("<p>下一篇:<a href='"+projectContext+"/blog/articles/"+ nextBlog.getId()+".html'>"
					+nextBlog.getTitle()+"</a></p>");
		}
		return pageCode.toString();
	}
	
	/**
	 * 根据关键字查询
	 * @param condition			查询条件
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping({"/condition"})
	public ModelAndView condition(@RequestParam(value = "condition",required = false)String condition,
						@RequestParam(value = "page",required=false)String page,
						HttpServletRequest request) throws Exception{
		if(StringUtil.isEmpty(page)){
			page="1";
		}
		
		ModelAndView mav = new ModelAndView();
		//因为只改博客列表，所以设置mainPage参数，以及对应页面标题
		mav.addObject("mainPage", "foreground/blog/result.jsp");
		//在lucene中查询,把空格去掉
		List<Blog> blogList = blogIndex.searchBlog(condition.trim());
		
		//计算传给前端关键字查询页面的blog列表，根据页数计算传递多少条数据，主要是计算最后一页
		int toIndex = 0;
		if(Integer.parseInt(page)*10 > blogList.size()){
			toIndex =  blogList.size();
		}else{
			toIndex = Integer.parseInt(page)*10 ;
		}
		mav.addObject("blogList", blogList.subList((Integer.parseInt(page)-1)*10, toIndex));
		
		mav.addObject("pageCode", pageUpAndDownPageCode(Integer.parseInt(page), condition, blogList.size(), 10, request.getServletContext().getContextPath()));
		//将关键字放进mav
		mav.addObject("condition", condition);
		//将查询结果数量放进mav
		mav.addObject("resultTotal", blogList.size());
		//加入标题
		mav.addObject("pageTitle", "搜索关键字'"+condition+"'结果页面_个人博客");
		
		mav.setViewName("index");
		return mav;
	}
	
	/**
	 * 查询的翻页功能
	 * @param page				当前页数
	 * @param condition					查询关键词
	 * @param totalNum			返回查询结果总数
	 * @param pageSize			每页显示数据条数
	 * @param projectContext	上下文
	 * @return
	 */
	public String pageUpAndDownPageCode(int page,String condition,int totalNum,int pageSize,String projectContext){
		StringBuffer pageCode = new StringBuffer();
		//totalPage总页数
		int totalPage = 0;
		if(totalNum%pageSize==0){
			totalPage = totalNum / pageSize;
		}else{
			totalPage = totalNum / pageSize + 1;
		}
		if (totalPage == 0) {
			return "";
		}
		
		//这个是传递到前端的翻页语句
		//加个头
		pageCode.append("<nav>");
		pageCode.append("<url class='pager' >");
		if(page > 1){		//如果当前页面大于1
			pageCode.append("<li><a href='" + projectContext + "/blog/condition.html?page=" + (page-1)
					+ "&condition="+condition+"'>上一页</a></li>");
		}else {				//如果当前页面是第一页
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		}
		
		if(page < totalPage){	//不是最后一页
			pageCode.append("<li><a href='" + projectContext + "/blog/condition.html?page=" + (page+1)
					+ "&condition="+condition+"'>下一页</a></li>");
		}else {				//如果当前页面是第一页
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		}
		//加个尾巴
		pageCode.append("</url>");
		pageCode.append("</nav>");
		
		return pageCode.toString();
	}
	
	
}
