package com.blog.controller;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.blog.entity.Blogger;
import com.blog.service.BloggerService;
import com.blog.util.CryptographyUtil;

/**
 * 博主登录相关
 * @author LI
 *
 */
@Controller
@RequestMapping("/blogger")
public class BloggerController {
	
	@Resource
	private BloggerService bloggerService ;
	
	@RequestMapping("/login")
	public String login(Blogger blogger,HttpServletRequest request){
		/**用户名*/
		String userName = blogger.getUserName();
		/**密码*/
		String password = blogger.getPassword();
		/**加密后的密码*/
		String pw = CryptographyUtil.md5(password, "java1234");
		
		//Subject,shiro权限验证
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userName, pw);
		try {
			//传递token给shiro的realm
			//这里涉及到自定义的Myrealm --> com.blog.realm.MyRealm.java
			subject.login(token);
			return "redirect:/admin/main.jsp";
		} catch (Exception e) {
			//如果登录失败，则在后台反应信息
			e.printStackTrace();
			//这个作用是：如果用户名或密码错了，就把用户名和密码传回前端的jsp页面
			//用户可以直接修改，不用每次都重新填写
			request.setAttribute("blogger", blogger);
			request.setAttribute("erroInfo", "用户名或者密码错误！");
		}
		return "login";
	}
	
	/**
	 * 关于博主
	 */
	@RequestMapping("/aboutMe")
	public ModelAndView aboutMe(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("blogger",bloggerService.find());
		mav.addObject("mainPage","foreground/blogger/info.jsp");
		mav.addObject("pageTitle","关于博主_个人博客系统");
		mav.setViewName("index");
		return mav;
	}
}
