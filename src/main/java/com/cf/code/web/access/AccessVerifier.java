package com.cf.code.web.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 * 
 * 
 * 标签profile注入功能生效条件：方法中第一个参数必须是@RequestParam(required = false) Profile
 * HttpSession注入方式选择：1.以参数形式传入 2.以成员变量传入 3.使用springmvc的RequestContextHolder方法
 * 
 * 为了提高效率，尽量将HttpSession以参数形式传入
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessVerifier {

	/**
	 * check表示，在未登陆情况下，是否跑出异常
	 * @return
	 */
	boolean check() default true;
	
}
