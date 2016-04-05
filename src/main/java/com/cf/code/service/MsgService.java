/**
 * 
 */
package com.cf.code.service;

import org.springframework.scheduling.annotation.Async;

import com.cf.code.entity.Msg;

/**
 * @author congfeng
 *
 */
public interface MsgService {

	@Async
	public void sender(Msg msg);
	
}
