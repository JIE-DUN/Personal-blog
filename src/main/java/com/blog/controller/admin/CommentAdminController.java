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

import com.blog.entity.Comment;
import com.blog.entity.PageBean;
import com.blog.service.CommentService;
import com.blog.util.DateJsonValueProcessor;
import com.blog.util.ResponseUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 评论管理
 *
 */
@Controller
@RequestMapping({"/admin/comment"})
public class CommentAdminController {
	
	@Resource
	private CommentService commentService;
	
	/**
	 * 查询评论列表
	 * @throws Exception 
	 */
	@RequestMapping({"/list"})
	private String list(@RequestParam(value = "page",required=false)String page,
						@RequestParam(value = "rows",required=false)String rows,
						@RequestParam(value = "state",required=false)String state,
						HttpServletResponse response) throws Exception{
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		//加载翻页、状态(state)参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", pageBean.getStart());
		map.put("size", pageBean.getPageSize());
		map.put("state", state);
		
		//查询评论列表
		List<Comment> commentList = commentService.list(map);
		//查询评论总数
		Long total = commentService.getTotal(map);
		
		JsonConfig config = new JsonConfig();
		//将时间转换格式，转换成"yyyy-MM-dd"
		//这个相当于格式化处理器，遇到适合类型(比如Date,时间戳)的数据就转换
		config.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));

		//把信息封装到JSON里
		JSONObject result = new JSONObject();
		JSONArray jsonArray = JSONArray.fromObject(commentList,config);
		result.put("rows", jsonArray);
		result.put("total", total);
		//把数据传给前端页面
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 删除评论
	 */
	@RequestMapping({"/delete"})
	private String delete(@RequestParam(value = "ids")String ids,HttpServletResponse response) throws Exception{
		String[] idsStr = ids.split(",");
		for(int id=0; id<idsStr.length; id++){
			commentService.delete(Integer.parseInt(idsStr[id]));
		}
		
		JSONObject result = new JSONObject();
		result.put("success", Boolean.valueOf(true));
		//把数据传给前端页面
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 审核评论
	 * @throws Exception 
	 */
	@RequestMapping({"/review"})
	private String review(@RequestParam(value = "ids")String ids,
						  @RequestParam(value = "state")String state,
							HttpServletResponse response) throws Exception{
		String[] idsStr = ids.split(",");
		for(int i=0; i<idsStr.length; i++){
			Comment comment = new Comment();
			comment.setId(Integer.parseInt(idsStr[i]));
			comment.setState(Integer.parseInt(state));
			commentService.update(comment);
		}
		
		JSONObject result = new JSONObject();
		result.put("success", Boolean.valueOf(true));
		//把数据传给前端页面
		ResponseUtil.write(response, result);
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
