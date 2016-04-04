/**
 * 
 */
package com.cf.code.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cf.code.entity.Notice;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public interface NoticeDao {

	public Notice find(@Param("id") Integer id);

	public void insert(Notice notice);
	
	public boolean delete(@Param("id") Integer id);
	
	public boolean update(@Param("id") Integer id,@Param("title") String title,@Param("content") String content,@Param("richText") String richText);
	
	public List<Notice> query(@Param("noticeType") Integer noticeType,@Param("title") String title,
			@Param("createTimeStart") Date createTimeStart,@Param("createTimeEnd") Date createTimeEnd,
			@Param("start") Integer start,@Param("size") Integer size);

	public int queryCount(@Param("noticeType") Integer noticeType,@Param("title") String title,
			@Param("createTimeStart") Date createTimeStart,@Param("createTimeEnd") Date createTimeEnd);

}
