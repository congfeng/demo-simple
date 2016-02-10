/**
 * 
 */
package com.cf.code.job;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cf.code.service.DemoService;

/**
 *                       
 * @Filename: DemoJob.java
 * @Version: 1.0
 * @Author: congfeng-nb
 * @Email: congfeng@91nongye.com
 *
 */
public class DemoJob extends QuartzJobBean{

	private static final Logger log = LogManager.getLogger(Demo4RAM.class);

	@Resource(name = "demoService")
	DemoService demoService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("job-"+demoService+"-----");
	}
	
}
