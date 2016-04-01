/**
 * 
 */
package com.cf.code.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cf.code.entity.Demo;
import com.cf.code.entity.enums.DemoType;

/**
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public interface DemoDao {

	public boolean insert(Demo demo);
	
	public boolean delete(@Param("id") Integer id);
	
	public boolean update(@Param("id") Integer id,@Param("name") String name,@Param("type") DemoType type);
	
	public Demo find(@Param("id") Integer id);
	
	public List<Demo> query(@Param("time") Date time,@Param("type") DemoType type);
	
}
