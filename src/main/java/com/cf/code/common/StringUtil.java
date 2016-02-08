package com.cf.code.common;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.cf.code.common.Constant;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *                       
 * @Filename: DateUtil.java
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class StringUtil {
    /**
     * 判断字符串是否数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("([0-9]|\\.|\\-)*");
        return pattern.matcher(str).matches();
    }
    
    public static boolean isLong(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否null或者空
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static String toString(Exception e) {
        if(e == null){
            return null;
        }
        CharArrayWriter arrayWriter = new CharArrayWriter();
        e.printStackTrace(new PrintWriter(arrayWriter, true));
        arrayWriter.close();
        String errorMsg = arrayWriter.toString();
        if(errorMsg.getBytes().length>65535-100){
            errorMsg = errorMsg.substring(0,21845);
        }
        return errorMsg;
    }
    
    public static String toString(Throwable throwable,boolean isSimple) {
        if(throwable == null){
            return null;
        }
        CharArrayWriter arrayWriter = new CharArrayWriter();
        throwable.printStackTrace(new PrintWriter(arrayWriter, true));
        arrayWriter.close();
        String errorMsg = arrayWriter.toString();
        if(isSimple){
            String[] st = errorMsg.split("\r\n");
            if(st.length>=2){
            	errorMsg = st[0]+"\r\n"+st[1];
            }
        }
        if(errorMsg.getBytes().length>65535-100){
            errorMsg = errorMsg.substring(0,21845);
        }
        return errorMsg;
    }
    
    public static String toString(Throwable t,StringBuilder simpleStackTrace){
    	if(simpleStackTrace == null){
    		simpleStackTrace = new StringBuilder(); 
    	}
    	if(simpleStackTrace.length() > 200){
    		simpleStackTrace.replace(197, simpleStackTrace.length(), "...");
    		return simpleStackTrace.toString();
    	}
    	if(t == null){
    		simpleStackTrace.deleteCharAt(simpleStackTrace.length()-1);
            return simpleStackTrace.toString();
        }else{
        	simpleStackTrace.append(t.toString()+"|");
        	return toString(t.getCause(), simpleStackTrace);
        }
    }

    public static String getString(String... strings) {
        for (String str : strings) {
            if (!isNullOrEmpty(str)) {
                return str;
            }
        }
        return null;
    }

    public static String toJson(Object o) {
        if (o == null) {
            return null;
        }else if(o instanceof Throwable){
        	return toString((Throwable)o,new StringBuilder());
        }else{
        	try{
        		return new GsonBuilder().create().toJson(o);
        	}catch (Throwable e) {
        		return "json转换异常"+e+":"+e.getMessage();
			}
        }
    }

    public static String toString(Object o) {
        if (o == null) {
            return null;
        }
        String oStr = "";
        if(o instanceof String){
        	oStr = o.toString();
        }else{
        	oStr = toJson(o);
        }
        String info = o.getClass().getName() + "|" + oStr;
        if(info.getBytes().length>65535){
            info = info.substring(0,21845);
        }
        return info;
    }

    public static String nullSafeString(String value) {
        return value == null ? "" : value;
    }
    
    public static String textSafeString(String value) {
    	return textSafeString(value,65535);
    }
    
    public static String textSafeString(String value,int maxLength) {
    	if(value == null||value.length() < 3){
    		return nullSafeString(value);
    	}
    	int i = 0;
    	String end = "";
    	while(value.getBytes().length > maxLength-3){
        	value = new String(Arrays.copyOf(value.getBytes(),maxLength-3-(i++)));
        	end = "...";
    	}
        return value+end;
    }
    
    public static String parseXmlInfo(String xmlInfo,String key){
		String value = StringUtils.substringBetween(xmlInfo,"<"+key+">","</"+key+">");
		return StringUtil.nullSafeString(value).trim();
	}
    
    public static String parseJsonInfo(JsonObject jsonObject,String key){
		JsonElement je = jsonObject.get(key);
		if(je == null){
			return "";
		}
		return StringUtil.nullSafeString(je.getAsString()).trim();
	}
    
	public static String createHYDID() {
		SimpleDateFormat sfDateTime = new SimpleDateFormat(
				"yyMMddHHmmssSSS");
		String date = "HYD";
		try {
			date += sfDateTime.format(new Date());
		} catch (Exception e) {
			return date;
		}
		return date;
	}
	
	public static String getStringFromException(Throwable e) {
		String result = "";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bos);
		e.printStackTrace(ps);
		try {
			result = bos.toString(Constant.Charset);
		} catch (IOException ioe) {
			return ioe+"";
		}
		return result;
	}
	
	public static String parseNum(String str) {
		String regEx="[^0-9]";   
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	
	public static String charsetChange(String str,String oldCharset,String newCharset){
		try {
			return new String(str.getBytes(oldCharset),newCharset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("不支持的编码格式："+oldCharset);
		}
	}
	
	static BASE64Encoder encoder = new BASE64Encoder();
	
	public static String encoder(String str){
		try {
			return encoder.encode(str.getBytes(Constant.Charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("不支持的编码格式："+Constant.Charset);
		}
	}
	
	static BASE64Decoder decoder = new BASE64Decoder();
	
	public static String decoder(String str){
		try {
			return new String(decoder.decodeBuffer(str),Constant.Charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("不支持的解码格式："+Constant.Charset);
		} catch (IOException e) {
			throw new RuntimeException("解码输出异常");
		}
	}
    
    public static void main(String[] args) throws UnsupportedEncodingException {
    }
}
