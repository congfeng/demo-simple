/**
 * 
 */
package com.cf.code.service;

import org.springframework.scheduling.annotation.Async;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public interface ImService {

	@Async
	public void sendMsg(String msg);
	
	@Async
	public void pushMsgCount(boolean isAdd);
	
}
