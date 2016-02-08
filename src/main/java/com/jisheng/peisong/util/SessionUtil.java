package com.jisheng.peisong.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jisheng.peisong.exception.CacheException;
import com.jisheng.peisong.vo.PeopleProfile;

import redis.clients.jedis.Jedis;

/**
 * @Filename: SesstionUtil.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年9月2日
 */
public class SessionUtil {
     public static PeopleProfile getLoginedPeople(Jedis jedis,String token){
    	 try {
    		 PeopleProfile peopleProfile = JedisUtil.getObjectValue(token, jedis);
    		 return peopleProfile;
		} catch (CacheException e) {
			e.printStackTrace();
		}
    	 return null;
     }
     
     public static void setLoginedPeople(Jedis jedis,HttpServletResponse response,String token,String name,PeopleProfile peopleProfile){
    	 JedisUtil.setValue(token, peopleProfile, jedis, 3*60*60);
//    	 writeCookies(response,token,name);
     }
     
     public static void writeCookies(HttpServletResponse response,String value , String name){
    	 Cookie tokenCookie = new Cookie(name,value); 
    	 tokenCookie.setMaxAge(2*60*60);
    	 tokenCookie.setPath("/");
//    	 tokenCookie.setDomain("/");
    	 response.addCookie(tokenCookie);
     }
     
     public static String readCookies(HttpServletRequest request,String name){
    	 Cookie[] cookies = request.getCookies();
    	 if(cookies != null){
	    	 for(Cookie cookie : cookies){
	    		 if(name.equals(cookie.getName())) return cookie.getValue();
	    	 }
    	 }
    	 return null;
     }
     public static void deleteCookies(HttpServletRequest request,HttpServletResponse response,String name){
    	 Cookie[] cookies = request.getCookies();
    	 if(cookies != null){
	    	 for(Cookie cookie : cookies){
	    		 if(name.equals(cookie.getName())) {
	    			 cookie.setMaxAge(0);
	    			 response.addCookie(cookie);
	    		 }
	    	 }
    	 }
     }
}

