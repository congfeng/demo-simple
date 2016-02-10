package com.cf.code.web.access;

import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.aop.MethodBeforeAdvice;

import com.cf.code.common.StringUtil;
import com.cf.code.common.WebUtil;
import com.cf.code.core.exception.AccessException;
import com.cf.code.entity.Profile;
import com.cf.code.service.SessionService;

public class AccessVerifierInterceptor implements MethodBeforeAdvice{

	@Resource(name = "SessionService")
	SessionService sessionService;
	
	@Override
	public void before(Method method, Object[] args, Object target)throws Throwable {
		AccessVerifier accessVerifier = method.getAnnotation(AccessVerifier.class);
		if(accessVerifier == null){
			return ;
		}
		HttpServletRequest request = (HttpServletRequest) args[0];
		String token = null;
		if(accessVerifier.token2Cookie()){
			token = WebUtil.getCookie(request, "token");
		}else{
			token = request.getParameter("token");
		}
		if(StringUtil.isNullOrEmpty(token)){
			throw new AccessException("未登陆或登录过期");
		}
		Profile profile = sessionService.getProfile(token);
		if(profile == null){
			throw new AccessException("未登陆");
		}
		args[1] = profile;
	}
	
}
