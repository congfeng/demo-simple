package com.cf.code.dao;

import org.apache.ibatis.annotations.Param;

public interface DictionaryDao {

	public String get(@Param("code") String code);
	
	public boolean update(@Param("code") String code,@Param("value") String value,@Param("status") Integer status);
	
}
