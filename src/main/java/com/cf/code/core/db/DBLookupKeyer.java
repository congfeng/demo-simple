/**
 * Copyright (c) mbaobao.com 2011 All Rights Reserved.
 */
package com.cf.code.core.db;

/**
 *                       
 * @Filename: DBLookupKeyer.java
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class DBLookupKeyer {

    private static final ThreadLocal<String> lookupKeyHolder = new ThreadLocal<String>();

    protected static String getLookupKey() {
        return lookupKeyHolder.get();
    }
    
    protected static void setLookupKey(String lookupKey){
        lookupKeyHolder.set(lookupKey);
    }
}
