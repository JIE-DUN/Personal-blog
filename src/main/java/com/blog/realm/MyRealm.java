package com.blog.realm;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.blog.entity.Blogger;
import com.blog.service.BloggerService;
import com.blog.util.Const;

/**
 * 用于用户登录后权限管理
 */
public class MyRealm extends AuthorizingRealm{

	@Resource
	private BloggerService bloggerSevice;
	
	/**
	 * 获取授权信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 登录验证
	 * token:令牌，基于用户名和密码的令牌
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//从令牌中取出用户名
		String userName = (String) token.getPrincipal();
		//让Shiro去验证账号密码
		Blogger blogger = bloggerSevice.getByUserName(userName);
		if(blogger != null){
			SecurityUtils.getSubject().getSession().setAttribute(Const.CURRENT_USER, blogger);
			AuthenticationInfo authenInfo = new SimpleAuthenticationInfo(blogger.getUserName(), blogger.getPassword(),getName());
			return authenInfo;
		}
		return null;
	}


}
