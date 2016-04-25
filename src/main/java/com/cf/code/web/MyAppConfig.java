/**
 * 
 */
package com.cf.code.web;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;

/**
 * @author congfeng
 *
 */
public class MyAppConfig extends JFinalConfig{

	@Override
	public void configConstant(Constants arg0) {
		arg0.setDevMode(true);
		arg0.setEncoding("utf-8");
//		arg0.setViewType(ViewType.JSP);
	}

	@Override
	public void configHandler(Handlers arg0) {
		//arg0.add(new ContextPathHandler("basePath"));
	}

	@Override
	public void configInterceptor(Interceptors arg0) {
		
	}

	@Override
	public void configPlugin(Plugins arg0) {
		
	}

	@Override
	public void configRoute(Routes arg0) {
		arg0.add("/", IndexController.class);
	}

}
