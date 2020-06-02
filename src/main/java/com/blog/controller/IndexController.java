package com.blog.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.blog.entity.Blog;
import com.blog.entity.PageBean;
import com.blog.service.BlogService;
import com.blog.util.PageUtil;
import com.blog.util.StringUtil;

/**
 * 首页
 */
@Controller
public class IndexController {
	
	@Resource
	private BlogService blogService;
	
	/**
	 * 首页内容
	 */
	@RequestMapping({"/index"})
	public ModelAndView index(@RequestParam(value="page",required=false)String page,
			@RequestParam(value="typeId",required=false)String typeId,
			@RequestParam(value="releaseDateStr",required=false)String releaseDateStr,
			HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		//设置title，如果对应jsp页面用了函数的的话就能设置
//		mav.addObject("title","个人博客系统");
		
		if (StringUtil.isEmpty(page)) {
			page = "1";
		}
		PageBean pageBean = new PageBean(Integer.parseInt(page),10);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		map.put("typeId", typeId);
		map.put("releaseDateStr", releaseDateStr);
		
		//查询blog
		List<Blog> bloglist = blogService.list(map);
		
		//设置翻页参数，判定是否有typeId、releaseDateStr等参数
		StringBuffer param = new StringBuffer();
		if (StringUtil.isNotEmpty(typeId)) {
			param.append("typeId="+typeId+"&");
		}
		if (StringUtil.isNotEmpty(releaseDateStr)) {
			param.append("releaseDateStr="+releaseDateStr+"&");
		}
		//将上面的参数放到工具类里拼串
		String pageCode = PageUtil.genPagination(request.getContextPath()+"/index.html",
				blogService.getTotal(map), 
				Integer.parseInt(page), 10, param.toString());
		
		
		//把bloglist传给指定页面
		mav.addObject("mainPage", "foreground/blog/list.jsp");
		//把bloglist传给前端
		mav.addObject("blogList", bloglist);
		//把pageCode传给前端
		mav.addObject("pageCode", pageCode);
		mav.addObject("pageTitle", "个人博客");
		return mav;
	}

}







