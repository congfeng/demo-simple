/**
 * Copyright (c) mbaobao.com 2011 All Rights Reserved.
 */
package com.cf.code.core.monitor;

import java.util.Calendar;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.cf.code.common.DateUtil;
import com.cf.code.common.StringUtil;
import com.cf.code.core.exception.BaseException;

/**
 *                       
 * @Filename: ExecuteInterceptor.java
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class ExecuteInterceptor implements MethodInterceptor{

    private static final Logger log = LogManager.getLogger(ExecuteInterceptor.class);

    private final ExecutionLog.Type executeType;
    
    public ExecuteInterceptor(String executeType){
        this.executeType = ExecutionLog.Type.valueOf(executeType);
    }
    
    public ExecuteInterceptor(ExecutionLog.Type type){
        this.executeType = type;
    }
    
    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
    	String miInfo = mi.getThis().getClass().getName()+"." + mi.getMethod().getName();
//        String resquestMsg = this.getRequestMsg(mi);
        Object responseObj = null;
        String errorMsg = "";
        String warnMsg = "";
        long begin = System.currentTimeMillis();
        try{
        	responseObj = mi.proceed(); 
            return responseObj;
        }catch (BaseException e) {
        	warnMsg = e.getMessage();
            throw e;
        }catch (Throwable e) {
        	errorMsg = StringUtil.toString(e);
            throw e;
        }finally{
            long end = System.currentTimeMillis();
//            log.info(this.getPrintInfo(miInfo, begin, end,"resquestMsg:"+resquestMsg+",responseObj:"+StringUtil.toString(responseObj)));
            if(log.isDebugEnabled()){
                log.debug(this.getPrintInfo(miInfo, begin, end, "execute-finish"));
            }
            if(end-begin > 10000){
            	warnMsg += "|execute-timeout";
            }
            if(!StringUtil.isNullOrEmpty(warnMsg)){
                log.warn(this.getPrintInfo(miInfo, begin, end, warnMsg));
            }
            if(!StringUtil.isNullOrEmpty(errorMsg)){
                log.error(this.getPrintInfo(miInfo, begin, end, errorMsg));
            }
        }
    }
    
    private String getPrintInfo(String miInfo,long begin,long end,String msg){
        Calendar cd = Calendar.getInstance();
        cd.setTimeInMillis(begin);
        String beginStr = DateUtil.format(cd.getTime());
        cd.setTimeInMillis(end);
        String endStr = DateUtil.format(cd.getTime());
        return miInfo+"["+beginStr+"---"+endStr+"]"+":" + msg;
    }
    
    protected String getRequestMsg(MethodInvocation mi){
    	return null;
    }
    
}
