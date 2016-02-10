package com.cf.code.test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

/**
 * 
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public abstract class AbstractTestCase extends TestCase {

    protected static Logger log = LogManager.getLogger(AbstractTestCase.class);

    protected static ClassPathXmlApplicationContext context = null;

    public void setUp() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext(new String[] {
            		"classpath:datasource.xml",
            		"classpath:dao.xml",
                    "classpath:service.xml"});
            context.start();
        }
        init();
    }

    protected void init() {

    }
    protected void tearDown() throws Exception {  
        super.tearDown();  
        context = null;  
    }  

}