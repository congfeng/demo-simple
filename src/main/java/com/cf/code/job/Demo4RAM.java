/**
 * 
 */
package com.cf.code.job;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.cf.code.core.exception.BusinessException;
import com.cf.code.service.DemoService;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class Demo4RAM {
	
	private static final Logger log = LogManager.getLogger(Demo4RAM.class);

	@Resource(name = "demoService")
	DemoService demoService;

	private String sign;
	
	public Demo4RAM(){
		
	}
	
	public Demo4RAM(String sign){
		this.sign = sign;
	}
	
	public String doitA(){
		this.demoService.insert("doitA"+"-----"+this+"--------"+this.sign);
		return "doitA"+"-----"+this.sign;
	}
	
	public String doitB(){
		try {
			this.demoService.update(6,"doitB"+"-----"+this+"--------"+this.sign);
		} catch (BusinessException e) {
			this.demoService.insert("doitB-update-error"+"-----"+this+"--------"+this.sign);
		}
		return "doitB"+"-----"+this.sign;
	}
	
}
