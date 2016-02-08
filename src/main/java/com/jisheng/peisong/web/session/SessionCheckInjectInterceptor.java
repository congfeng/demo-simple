/**
 * 
 */
package com.jisheng.peisong.web.session;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.aop.MethodBeforeAdvice;

import com.jisheng.peisong.exception.AccessException;
import com.jisheng.peisong.util.SessionUtil;
import com.jisheng.peisong.util.StringUtil;
import com.jisheng.peisong.vo.PeopleProfile;
import com.jisheng.peisong.web.service.SessionService;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class SessionCheckInjectInterceptor implements MethodBeforeAdvice{
	
	@Resource(name = "SessionService")
	SessionService sessionService;
	
	@Override
	public void before(Method method, Object[] args, Object target)throws Throwable {
		Annotation sessionChkInject = method.getAnnotation(SessionCheckInject.class);
		if(sessionChkInject == null){
			return ;
		}
		HttpServletRequest request = (HttpServletRequest) args[0];
		String token = SessionUtil.readCookies(request, "peopleToken");
		if(StringUtil.isNullOrEmpty(token)){
			throw new AccessException("未登陆或登录过期");
		}
		PeopleProfile profile = sessionService.getPeopleProfile(token);
		if(profile == null){
			throw new AccessException("未登陆或登录过期");
		}
		args[1] = profile;
	}

}
