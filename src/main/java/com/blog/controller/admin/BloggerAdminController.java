package com.blog.controller.admin;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.blog.dao.BloggerDao;
import com.blog.entity.Blogger;
import com.blog.service.BloggerService;
import com.blog.util.Const;
import com.blog.util.CryptographyUtil;
import com.blog.util.DateUtil;
import com.blog.util.ResponseUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping({"/admin/blogger"})
public class BloggerAdminController {
	
	@Resource
	private BloggerService bloggerSevice;
	
	@RequestMapping({"/save"})
	public String save(@RequestParam(value="imageFile",required=false)MultipartFile imageFile,
						Blogger blogger,
						HttpServletRequest request,
						HttpServletResponse response) throws Exception{
		//判断有没有头像
		if(!imageFile.isEmpty()){
			//先获取当前项目的路径
			String filePath = request.getServletContext().getRealPath("/");
			//得到头像上传时间和对应拓展名的字符串
			String imageName = DateUtil.getCurrentDateStr() + "." + imageFile.getOriginalFilename().split("\\.")[1];
			//放到项目的“static/userImages/”文件夹下 
			imageFile.transferTo(new File(filePath + "static/userImages/" + imageName));
			//存入头像的路径
			blogger.setImageName(imageName);
		}
		//执行更新操作
		int resultTotal = bloggerSevice.update(blogger);
		StringBuffer result = new StringBuffer();
		if (resultTotal > 0) {
			//弹出一个对话框，修改成功
			result.append("<script lauguage='javascript'>alter('修改成功');</script>");
		}else {
			result.append("<script lauguage='javascript'>alter('修改失败');</script>");
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 获取博主的Json个数
	 * 使用这个方法得到Session里的图片
	 * 使之能放到个人简介上面
	 * @throws Exception 
	 */
	@RequestMapping({"/find"})
	public String find(HttpServletResponse response) throws Exception{
		//得到Session里的Blogger
		Blogger blogger = (Blogger) SecurityUtils.getSubject().getSession().getAttribute(Const.CURRENT_USER);
		JSONObject jsonObject = JSONObject.fromObject(blogger);
		ResponseUtil.write(response, jsonObject);
		return null;
		
	}
	
	/**
	 * 修改密码
	 */
	@RequestMapping({"/modifyPassword"})
	public String modifyPassword(@RequestParam(value="id")String id,
			@RequestParam(value="newPassword")String newPassword,
			HttpServletResponse response) throws Exception{
		Blogger blogger = new Blogger();
		blogger.setId(Integer.parseInt(id));
		blogger.setPassword(CryptographyUtil.md5(newPassword, "java1234"));
		
		//执行更新操作
		int resultTotal = bloggerSevice.update(blogger);
		JSONObject result = new JSONObject();
		if (resultTotal > 0) {
			// 弹出一个对话框，修改成功
			result.put("success", Boolean.TRUE);
		} else {
			result.put("success", Boolean.FALSE);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	/**
	 * 用户安全退出
	 * 借用shiro的logout方法
	 */
	@RequestMapping({"/logout"})
	public String logout(){
		SecurityUtils.getSubject().logout();
		return "redirect:/login.jsp";
	}
}
