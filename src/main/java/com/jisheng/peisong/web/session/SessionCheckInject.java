/**
 * 
 */
package com.jisheng.peisong.web.session;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 * 
 * 标签session验证功能生效条件：方法中第一个参数必须是HttpServletRequest,且不为null
 * 标签session注入功能生效条件：方法中第二个参数必须是@RequestParam(required = false) PeopleProfile
 * 
 * 限制参数位置，是为了提高检索效率
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionCheckInject {

}
