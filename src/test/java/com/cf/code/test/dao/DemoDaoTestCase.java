/**
 * 
 */
package com.cf.code.test.dao;

import java.util.List;

import com.cf.code.common.StringUtil;
import com.cf.code.dao.DemoDao;
import com.cf.code.entity.Demo;
import com.cf.code.entity.enums.DemoType;
import com.cf.code.service.DemoService;
import com.cf.code.test.AbstractTestCase;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class DemoDaoTestCase extends AbstractTestCase{

	public void testQuery(){
		DemoDao demoDao = context.getBean("demoDaoRead", DemoDao.class);
    	List<Demo> demoList = demoDao.query(null, DemoType.TypeOne);
    	log.info(StringUtil.toJson(demoList.size()));
	}
	

	public void testFind(){
		DemoDao demoDao = context.getBean("demoDaoRead", DemoDao.class);
    	Demo demo = demoDao.find(1);
    	log.info(""+demo.getType());
	}
	
	
	public void testUpdate(){
		DemoDao demoDao = context.getBean("demoDao", DemoDao.class);
		demoDao.update(1, "namesdf ", DemoType.TypeTwo);
	}
	
	public void testCreate(){
		DemoDao demoDao = context.getBean("demoDao", DemoDao.class);
		Demo demo = new Demo();
		demo.setName("dddd");
		demoDao.insert(demo);
		System.out.println("-------------"+demo.getId());
	}
}
