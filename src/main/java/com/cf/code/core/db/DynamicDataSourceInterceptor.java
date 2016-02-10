/**
 * Copyright (c) mbaobao.com 2011 All Rights Reserved.
 */
package com.cf.code.core.db;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *                       
 * @Filename: DynamicDataSourceInterceptor.java
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class DynamicDataSourceInterceptor implements MethodInterceptor{

    private static Logger log = LogManager.getLogger(DynamicDataSourceInterceptor.class);

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        String lookupKey = null;
        Object[] arguments = mi.getArguments();
        if(arguments != null && arguments.length>0){
            for(Object argument:arguments){
                if(argument != null && DataSourceEnum.class.equals(argument.getClass())){
                    lookupKey = argument.toString();
                    break;
                }
            }
        }
        if(log.isDebugEnabled()){
            String miInfo = mi.getThis().getClass().getName()+"." + mi.getMethod().getName();
            log.debug(miInfo+"动态数据源切换至：" + lookupKey);
        }
        DBLookupKeyer.setLookupKey(lookupKey);
        try {
            return mi.proceed();
        } finally{
            DBLookupKeyer.setLookupKey(null);
        }
    }
}
