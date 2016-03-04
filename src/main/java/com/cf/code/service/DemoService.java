/**
 * 
 */
package com.cf.code.service;

import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Async;

import com.cf.code.core.db.DataSourceEnum;
import com.cf.code.core.exception.BusinessException;
import com.cf.code.entity.Demo;
import com.cf.code.entity.enums.DemoType;

/**
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public interface DemoService {

	public Integer insert(String name);
	
	public boolean delete(Integer id);
	
	public void update(Integer id,String name)throws BusinessException;
	
	public Demo find(Integer id);
	
	public List<Demo> query(Date time);
	
	public void txUpdate(Integer sign,DemoType dt,DataSourceEnum ds)throws BusinessException;
	
	public void tpTxUpdate(DemoType dt)throws BusinessException;
	
	@Async
	public void it4Async();
	
}
