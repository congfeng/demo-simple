/**
 * 
 */
package com.cf.code.job;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.cf.code.service.DemoService;

/**
 *                       
 * @Filename: DemoJob.java
 * @Version: 1.0
 * @Author: congfeng-nb
 * @Email: congfeng@91nongye.com
 *
 */
public class DemoJob{
	
	private static final Logger log = LogManager.getLogger(DemoJob.class);

	@Resource(name = "demoService")
	DemoService demoService;

	public DemoJob(){
		
	}
	
	public String doit(){
		log.info("-------------DemoJob-------------");
		this.demoService.insert("doit"+"-----"+this+"--------");
		return "doit"+"-----";
	}
	
}
