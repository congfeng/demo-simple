/**
 * 
 */
package com.cf.code.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cf.code.entity.Msg;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public interface MsgDao {

	public Msg find(@Param("id") Integer id);

	public void insert(Msg msg);
	
	public List<Msg> query(@Param("title") String title,@Param("userName") String userName,
			@Param("createTimeStart") Date createTimeStart,@Param("createTimeEnd") Date createTimeEnd,
			@Param("start") Integer start,@Param("size") Integer size);

	public int queryCount(@Param("title") String title,@Param("userName") String userName,
			@Param("createTimeStart") Date createTimeStart,@Param("createTimeEnd") Date createTimeEnd);
	
}
