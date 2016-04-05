/**
 * 
 */
package com.cf.code.entity;

import java.util.Date;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class Msg {

	private Integer id;
	
	private Date createTime;
	
	private String createTimeFormat;
	
	private Date updateTime;
	
	private String userName;
	
	private String userEmail;
	
	private String title;
	
	private String content;
	
	private Integer sendStatus;
	
	private Integer replyStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeFormat() {
		return createTimeFormat;
	}

	public void setCreateTimeFormat(String createTimeFormat) {
		this.createTimeFormat = createTimeFormat;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Integer getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}

	public Integer getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(Integer replyStatus) {
		this.replyStatus = replyStatus;
	}
	
}
