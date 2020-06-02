package com.blog.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog.entity.Blog;
import com.blog.entity.PageBean;
import com.blog.lucene.BlogIndex;
import com.blog.service.BlogService;
import com.blog.util.DateJsonValueProcessor;
import com.blog.util.ResponseUtil;
import com.blog.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 博客信息管理
 */
@Controller
@RequestMapping({"/admin/blog"})
public class BlogAdminController {

	@Resource
	private BlogService blogService;
	
	private BlogIndex blogIndex = new BlogIndex();
	
	/**
	 * 保存一条博客信息,可能是新增，也可能是修改
	 * @throws Exception 
	 */
	@RequestMapping({"/save"})
	public String save(Blog blog,HttpServletResponse response) throws Exception{
		int resultTotal = 0;
		//如果为空，说明是一个新增的博客
		if (blog.getId()==null) {
			resultTotal = blogService.add(blog);
			//增加的对应的blog索引
			blogIndex.addIndex(blog);
		}else{		//修改博客
			resultTotal = blogService.update(blog);
			//修改的对应的blog索引
			blogIndex.updateIndex(blog);
		}
		
		JSONObject result = new JSONObject();
		if(resultTotal > 0){
			result.put("success", Boolean.valueOf(true));
		}else {
			result.put("success", Boolean.valueOf(false));
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 查询博客信息列表
	 * @throws Exception 
	 */
	@RequestMapping({"/list"})
	public String list(@RequestParam(value = "page",required=false)String page,
			@RequestParam(value = "rows",required=false)String rows,Blog blog,
			HttpServletResponse response) throws Exception{
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		//按照title进行模糊查询,要判断为不为空，使用了自己写的一个util
		map.put("title", StringUtil.formatLike(blog.getTitle()));
		//分页查询博客信息列表
		List<Blog> bloglist = blogService.list(map);
		//获取共有多少条博客信息
		Long total = blogService.getTotal(map);
		
		JsonConfig config = new JsonConfig();
		//将时间转换格式，转换成"yyyy-MM-dd"
		//这个相当于格式化处理器，遇到适合类型(比如Date,时间戳)的数据就转换
		config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));

		//得到上面两条信息后（total，config），就把信息封装到JSON里
		JSONObject result = new JSONObject();
		JSONArray jsonArray = JSONArray.fromObject(bloglist,config);
		result.put("rows", jsonArray);
		result.put("total", total);
		//把数据传给前端页面
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 根据主键查询一条博客信息
	 * 主要用于修改博客内容页面
	 * @throws Exception 
	 */
	@RequestMapping({"/findById"})
	public String findById(@RequestParam(value = "id")String id,HttpServletResponse response) throws Exception{
		Blog blog = blogService.findById(Integer.parseInt(id));
		
		JSONObject result = JSONObject.fromObject(blog);
		//把数据传给前端页面
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 根据主键删除一条或者多条博客信息
	 * @throws Exception 
	 */
	@RequestMapping({"/delete"})
	public String delete(@RequestParam(value = "ids")String ids,HttpServletResponse response) throws Exception{
		String[] idsStr = ids.split(",");
		for(int id=0; id<idsStr.length; id++){
			blogService.delete(Integer.parseInt(idsStr[id]));
			//删除对应的blog索引
			blogIndex.deleteIndex(idsStr[id]);
		}
		
		JSONObject result = new JSONObject();
		result.put("success", Boolean.valueOf(true));
		//把数据传给前端页面
		ResponseUtil.write(response, result);
		return null;
	}
}
