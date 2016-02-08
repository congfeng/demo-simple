/**
 * 
 */
package com.cf.code.job;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.service.DemoService;

/**
 *                       
 * @Filename: DemoJob.java
 * @Version: 1.0
 * @Author: congfeng-nb
 * @Email: congfeng@91nongye.com
 *
 */
public class DemoJob {
	
	private static final Logger log = LogManager.getLogger(DemoJob.class);

	@Resource(name = "demoService")
	DemoService demoService;
	
	@Resource(name = "demoService_dubbo")
	DemoService demoService_dubbo;
	
	private String sign;
	
	public DemoJob(){
		
	}
	
	public DemoJob(String sign){
		this.sign = sign;
	}
	
	public String doitA(){
		this.demoService.insert("doitA"+"-----"+this+"--------"+this.sign);
		log.info("doitA"+"-----"+this.sign);
		return "doitA"+"-----"+this.sign;
	}
	
	public String doitB(){
		try {
			this.demoService.update(6,"doitB"+"-----"+this+"--------"+this.sign);
		} catch (BusinessException e) {
			this.demoService.insert("doitB-update-error"+"-----"+this+"--------"+this.sign);
		}
		log.info("doitB"+"-----"+this.sign);
		return "doitB"+"-----"+this.sign;
	}
	
	
	
}
