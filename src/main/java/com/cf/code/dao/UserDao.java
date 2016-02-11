/**
 * 
 */
package com.cf.code.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cf.code.entity.User;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public interface UserDao {

	public User find(@Param("username") String username);

	public void insert(User user);
	
	public void updatePassword(@Param("id") Integer id,@Param("password") String password);
	
	public List<User> query(@Param("username") String username,
			@Param("createTimeStart") Date createTimeStart,@Param("createTimeEnd") Date createTimeEnd,
			@Param("start") Integer start,@Param("size") Integer size);

	public int queryCount(@Param("username") String username,
			@Param("createTimeStart") Date createTimeStart,@Param("createTimeEnd") Date createTimeEnd);

}
