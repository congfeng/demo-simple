/**
 * 
 */
package com.cf.code.web;

import org.apache.commons.lang.StringUtils;

import com.jfinal.core.Controller;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class ProfileController extends Controller{
	
	public void login(){
		String username = this.getPara("username");
		String password = this.getPara("password");
		if(StringUtils.isEmpty(username)){
			this.renderJson("s", 0);
			this.renderJson("m", "用户名 不能为空");
			return ;
		}
		if(StringUtils.isEmpty(password)){
			this.renderJson("s", 0);
			this.renderJson("m", "密码不能为空");
			return ;
		}
		if(!(username.equals("admin")&&password.equals("admin"))){
			this.renderJson("s", 0);
			this.renderJson("m", "用户名或密码错误");
			return ;
		}
		this.renderJson("s", 1);
	}
	
}
