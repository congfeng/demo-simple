/**
 * 
 */
package com.cf.code.dao;

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

}
