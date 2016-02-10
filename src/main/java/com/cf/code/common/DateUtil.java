/**
 * Copyright (c) mbaobao.com 2011 All Rights Reserved.
 */
package com.cf.code.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *                       
 * @Filename: DateUtil.java
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class DateUtil {

	static final String pattern = "yyyy-MM-dd HH:mm:ss";
	
    public static Date addMinutes(Date theDate, int minutes) {
        long Time = (theDate.getTime() / 1000) + 60 * minutes;
        Date myDate = new Date();
        myDate.setTime(Time * 1000);
        return myDate;
    }
    
    public static Date toParse(String date,String pattern) {
        if (date == null)
            return null;
        if (StringUtil.isNullOrEmpty(pattern))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
    }
    
    public static Date toParse(String date) {
    	return toParse(date, pattern);
    }
    
    public static String format(Date date, String pattern) {
        if (date == null)
            return "";
        if (StringUtil.isNullOrEmpty(pattern)){
        	return date.toString();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    public static String format(Date date) {
    	if (date == null){
    		return "";
    	}
        return new SimpleDateFormat(pattern).format(date);
    }
    
	/**
	 * 计算两个日期相隔天数
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		String pattern = "yyyy-MM-dd";
		smdate = toParse(format(smdate,pattern),pattern);
		bdate = toParse(format(bdate,pattern),pattern);
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}  
	
	public static String convertTimeByMillisecods(long milliSecods,String pattern) throws ParseException {
		StringBuffer time = new StringBuffer("");
		long dayMillisecods = 28800000;
		 if(milliSecods/dayMillisecods>0){//满一天
			 time.append(milliSecods/dayMillisecods+"天");
			 long countD = milliSecods/dayMillisecods;
			 if((milliSecods-dayMillisecods*countD)/3600000>0){//满一个小时
				 time.append((milliSecods-dayMillisecods*countD)/3600000+"时");
				 long countH = (milliSecods-dayMillisecods*countD)/3600000;
				 if((milliSecods-dayMillisecods*countD-3600000*countH)/60000>0){//满一分钟
					 time.append((milliSecods-dayMillisecods*countD-3600000*countH)/60000+"分");
					 long countM = (milliSecods-dayMillisecods*countD-3600000*countH)/60000;
					 if((milliSecods-dayMillisecods*countD-3600000*countH-60000*countM)/1000>0){
						 time.append((milliSecods-dayMillisecods*countD-3600000*countH-60000*countM)/1000+"秒");
					 }
				 }else{
					 if((milliSecods-dayMillisecods*countD-3600000*countH)/1000>0)
						 time.append((milliSecods-dayMillisecods*countD-3600000*countH)/1000+"秒");
				 }
			 }else{
				 if((milliSecods-dayMillisecods*countD)/60000>0){
					 time.append((milliSecods-dayMillisecods*countD)/60000+"分");
					 long countM = (milliSecods-dayMillisecods*countD)/60000;
					 if((milliSecods-dayMillisecods*countD-60000*countM)/1000>0){
						 time.append((milliSecods-dayMillisecods*countD-60000*countM)/1000+"秒");
					 }
				 }else{
					 if((milliSecods-dayMillisecods*countD)/1000>0){
						 time.append((milliSecods-dayMillisecods*countD)/1000+"秒");
					 }
				 }
			 }
				 
		 }else{//不满一天
			 if(milliSecods/3600000>0){
				 time.append(milliSecods/3600000+"时");
				 long countH = milliSecods/3600000;
				 if((milliSecods-3600000*countH)/60000>0){
					 time.append((milliSecods-3600000*countH)/60000+"分");
					 long countM = (milliSecods-3600000*countH)/60000;
					 if((milliSecods-3600000*countH-60000)/1000>0){
						 time.append((milliSecods-3600000*countH-60000*countM)/1000+"秒");
					 }
				 }else{
					 if((milliSecods-3600000*countH)/1000>0)
						 time.append((milliSecods-3600000*countH)/1000+"秒");
				 }
			 }else{
				 if(milliSecods/60000>0){
					 time.append(milliSecods/60000+"分");
					 long countM = milliSecods/60000;
					 if((milliSecods-60000*countM)/1000>0){
						 time.append((milliSecods-60000*countM)/1000+"秒");
					 }
				 }else{
					 if(milliSecods/1000>0){
						 time.append(milliSecods/1000+"秒");
					 }
				 }
				
			 }
		 }
		return time.toString();
	}  
    
    public static void main(String[] args) throws ParseException {
    	
    }
}
