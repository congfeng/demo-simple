package com.cf.code.core.monitor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.cf.code.common.Constant;
import com.cf.code.common.StringUtil;
import com.cf.code.common.WebUtil;
import com.cf.code.core.exception.AccessException;
import com.cf.code.core.exception.BaseException;

/**
 * 
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class BaseExceptionHandler implements HandlerExceptionResolver{

	private static final Logger log = LogManager.getLogger(BaseExceptionHandler.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		int errorType = 0;
		String errorInfo = null;
        Throwable factException = ex;
        if(ex.getCause() != null){
        	factException = ex.getCause();
        }
        if(factException instanceof AccessException){
        	errorType = 1;
        	errorInfo = factException.getMessage();
        	log.warn(errorInfo);
        	if(!WebUtil.isAjax(request)){
        		return new ModelAndView("redirect:/login.html");
        	}
        }else if(factException instanceof BaseException){
        	errorType = 2;
        	errorInfo = factException.getMessage();
        	log.warn(errorInfo);
        }else if(factException instanceof RuntimeException){
        	errorType = 3;
        	errorInfo = StringUtil.toString(factException, true);
        	log.error(errorInfo,factException);
        }else{
        	errorType = 4;
        	errorInfo = factException.toString();
        	log.error(errorInfo,factException);
        }
        ModelAndView model = new ModelAndView();
		MappingJacksonJsonView view = new MappingJacksonJsonView();
		view.addStaticAttribute(Constant.TransmitField.Status, 0);
		view.addStaticAttribute(Constant.TransmitField.Type,errorType);
		view.addStaticAttribute(Constant.TransmitField.Msg, errorInfo);
    	model.setView(view);
        return model;  
	}
	
}
