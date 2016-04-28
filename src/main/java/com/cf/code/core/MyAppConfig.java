/**
 * 
 */
package com.cf.code.core;

import com.cf.code.web.ProfileController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class MyAppConfig extends JFinalConfig{

	@Override
	public void configConstant(Constants arg0) {
		arg0.setDevMode(true);
		arg0.setEncoding("UTF-8");
	}

	@Override
	public void configHandler(Handlers arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configInterceptor(Interceptors arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configPlugin(Plugins arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void configRoute(Routes arg0) {
		arg0.add("/profile", ProfileController.class);
	}

}
