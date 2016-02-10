/**
 * 
 */
package com.cf.code.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cf.code.service.DemoService;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class Demo4Tx extends AbstractJob{
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		DemoService demoService = super.getBean(context, "demoService", DemoService.class);
		log.info("---------Demo4Tx----"
				+ demoService
				+ this+"---");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
		
}
