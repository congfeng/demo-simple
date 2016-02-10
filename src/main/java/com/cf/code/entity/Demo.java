/**
 * 
 */
package com.cf.code.entity;

import java.io.Serializable;
import java.util.Date;

import com.cf.code.entity.enums.DemoType;

/**
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class Demo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8189815054254475144L;

	private Integer id;
	
	private String name;
	
	private DemoType type;
	
	private Date createTime;
	
	private String createTimeFormat;

	public String getCreateTimeFormat() {
		return createTimeFormat;
	}

	public void setCreateTimeFormat(String createTimeFormat) {
		this.createTimeFormat = createTimeFormat;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public DemoType getType() {
		return type;
	}

	public void setType(DemoType type) {
		this.type = type;
	}
	
	
}
