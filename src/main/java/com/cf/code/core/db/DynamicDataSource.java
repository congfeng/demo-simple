/**
 * Copyright (c) mbaobao.com 2011 All Rights Reserved.
 */
package com.cf.code.core.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 *                       
 * @Filename: DynamicDataSource.java
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class DynamicDataSource extends AbstractRoutingDataSource{

    @Override
    protected Object determineCurrentLookupKey() {
        return DBLookupKeyer.getLookupKey();
    }

}
