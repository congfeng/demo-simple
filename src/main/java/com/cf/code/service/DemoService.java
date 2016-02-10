/**
 * 
 */
package com.cf.code.service;

import java.util.Date;
import java.util.List;

import com.jisheng.peisong.db.DataSourceEnum;
import com.jisheng.peisong.entity.Demo;
import com.jisheng.peisong.entity.enums.DemoType;
import com.jisheng.peisong.exception.BusinessException;

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
	
	public void ddddd();
}
