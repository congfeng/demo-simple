/**
 * 
 */
package com.cf.code.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cf.code.entity.MsgReceiver;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public interface MsgReceiverDao {

	public void insert(MsgReceiver msgReceiver);
	
	public boolean delete(@Param("id") Integer id);
	
	public List<MsgReceiver> query(
			@Param("createTimeStart") Date createTimeStart,@Param("createTimeEnd") Date createTimeEnd,
			@Param("start") Integer start,@Param("size") Integer size);

	public int queryCount(
			@Param("createTimeStart") Date createTimeStart,@Param("createTimeEnd") Date createTimeEnd);
	
}
