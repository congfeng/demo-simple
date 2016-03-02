/**
 * Copyright (c) mbaobao.com 2011 All Rights Reserved.
 */
package com.cf.code.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class WebUtil {
	
    private static final ThreadLocal<String> HttpResponseTextHolder = new ThreadLocal<String>();
    
    private static final ThreadLocal<String> HttpRequestTextHolder = new ThreadLocal<String>();

    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        Cookie cookie = null;
        for (int i = 0; i < cookies.length; i++) {
            cookie = cookies[i];
            if (cookie.getName().equalsIgnoreCase(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }
    
    public static void writeCookie(HttpServletResponse response,String name,String value){
   	 	Cookie cookie = new Cookie(name,value); 
   	 	cookie.setMaxAge(2*60*60);
   	 	cookie.setPath("/");
//   	cookie.setDomain("/");
   	 	response.addCookie(cookie);
    }
    
    public static void deleteCookie(HttpServletRequest request,HttpServletResponse response,String name){
    	Cookie cookie = new Cookie(name,null); 
    	cookie.setMaxAge(0);
    	cookie.setPath("/");
//   	cookie.setDomain("/");
   	 	response.addCookie(cookie);
    }

    public static String getCurrentHttpResponseText(){
        return HttpResponseTextHolder.get();
    }
    
    public static String getCurrentHttpRequestText(){
        return HttpRequestTextHolder.get();
    }
    
    public static void setJsonRet(HttpServletResponse response,String json,boolean isAjax){
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            if(isAjax){
                response.setContentType("application/x-json; charset="+Constant.Charset);
            }else{
                response.setContentType("application/json; charset="+Constant.Charset);
            }
            os.write(json.getBytes(Constant.Charset));
            os.flush();
            HttpResponseTextHolder.set(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void setJsonRet(HttpServletResponse response,String json){
    	setJsonRet(response,json,false);
    }
    
    public static String readResponseStringFromRequestBody(HttpServletRequest request){  
        StringBuilder json = new StringBuilder();  
        String line = null;  
        try {  
            BufferedReader reader = request.getReader();  
            while((line = reader.readLine()) != null) {  
                json.append(line);  
            }  
        } catch(Exception e) {  
            return null;  
        }
        HttpRequestTextHolder.set(json.toString());
        return json.toString();  
    }  

    public static String readJsonFromRequestBody(HttpServletRequest request){
        String msg = readResponseStringFromRequestBody(request);
        if(msg == null){
            return null;
        }
        return msg;  
    }
    
    public static Map<String, String> readJsonFromRequestBodyHTTP(HttpServletRequest request){
    	Map<String, String> _req = new HashMap<String, String>();
    	@SuppressWarnings("unchecked")
		Enumeration<String> _params = request.getParameterNames();
    	while (_params.hasMoreElements()) {
    		String _name = _params.nextElement();
    		_req.put(_name, request.getParameter(_name));
    	}
    	HttpRequestTextHolder.set(_req.toString());
    	return _req;
    }
    
    public static String getRealClientIp(HttpServletRequest request)
    {
	   if(request == null){
		   return null;
	   }
    	String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
		return ip;
    }
    
    public static String getResponseCharset(String contentType) {
		String charset = Constant.Charset;
		if (!StringUtil.isNullOrEmpty(contentType)) {
			String[] params = contentType.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!StringUtil.isNullOrEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}
		return charset;
	}
    
    public static String buildQuery(Map<String, String> params) {
		if (params == null || params.isEmpty()) {
			return null;
		}
		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;
		try {
			for (Entry<String, String> entry : entries) {
				String name = entry.getKey();
				String value = entry.getValue();
				if (!StringUtil.isNullOrEmpty(value)) {
					if (hasParam) {
						query.append("&");
					} else {
						hasParam = true;
					}
					query.append(name).append("=").append(URLEncoder.encode(value, Constant.Charset));
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("编码不支持："+Constant.Charset);
		}
		return query.toString();
	}
    
    public static Map<String, String> splitUrlQuery(HttpServletRequest request) {
    	String query = request.getQueryString();
		Map<String, String> result = new HashMap<String, String>();
		String[] pairs = query.split("&");
		if (pairs != null && pairs.length > 0) {
			for (String pair : pairs) {
				String[] param = pair.split("=", 2);
				if (param != null && param.length == 2) {
					result.put(param[0], param[1]);
				}
			}
		}
		return result;
	}
    
    public static boolean isMobileDevice(HttpServletRequest request){
    	String requestHeader = request.getHeader("user-agent");
        /**
         * android : 所有android设备
         * mac os : iphone ipad
         * windows phone:Nokia等windows系统的手机
         * 将iphone判断修改为iphone（否则mac book访问判断失误）
         */
        String[] deviceArray = new String[]{"android","iphone","windows phone"};
        if(requestHeader == null)
            return false;
        requestHeader = requestHeader.toLowerCase();
        for(int i=0;i<deviceArray.length;i++){
            if(requestHeader.indexOf(deviceArray[i])>0){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isAjax(HttpServletRequest request) { 
        String header = request.getHeader("X-Requested-With"); 
        if (header != null && "XMLHttpRequest".equals(header)) {
        	return true;
        }
        return false; 
    }
    
}
