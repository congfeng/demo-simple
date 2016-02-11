/**
 * 
 */
package com.cf.code.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public interface MenuDao {

	public List<String> query(@Param("userId") Integer userId);
	
}
