package com.cf.code.web.access;

import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import org.springframework.aop.MethodBeforeAdvice;

import com.cf.code.core.exception.AccessException;
import com.cf.code.entity.Profile;

public class AccessVerifierInterceptor implements MethodBeforeAdvice{
	
	@Override
	public void before(Method method, Object[] args, Object target)throws Throwable {
		AccessVerifier accessVerifier = method.getAnnotation(AccessVerifier.class);
		if(accessVerifier == null){
			return ;
		}
		HttpSession session = (HttpSession) args[0];
		Profile profile = (Profile)session.getAttribute("profile");
		if(profile == null&&accessVerifier.check()){
			throw new AccessException("未登陆");
		}
		args[1] = profile;
	}
	
}
