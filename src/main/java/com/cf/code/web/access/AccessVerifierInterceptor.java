package com.cf.code.web.access;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cf.code.core.exception.AccessException;
import com.cf.code.entity.Profile;

public class AccessVerifierInterceptor implements MethodBeforeAdvice{
	
	@Override
	public void before(Method method, Object[] args, Object target)throws Throwable {
		AccessVerifier accessVerifier = method.getAnnotation(AccessVerifier.class);
		if(accessVerifier == null){
			return ;
		}
		HttpSession session = getSession(args, target);
		Profile profile = (Profile)session.getAttribute("profile");
		if(profile == null&&accessVerifier.check()){
			throw new AccessException("未登陆");
		}
		args[0] = profile;
	}
	
	private HttpSession getSession(Object[] args, Object target){
		if(args != null&&args.length > 0){
			for(int i=0;i<args.length;i++){
				if(args[i] instanceof HttpSession){
					return (HttpSession)args[i];
				}
			}
		}
		Field[] fs = target.getClass().getDeclaredFields();
		if(fs != null&&fs.length > 0){
			for(int i=0;i<fs.length;i++){
				Field f = fs[i];
				if("javax.servlet.http.HttpSession".equals(f.getType().getName())){
					try {
						f.setAccessible(true);
						return (HttpSession)f.get(target);
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						f.setAccessible(false);
					}
				}
			}
		}
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
	}
	
}
