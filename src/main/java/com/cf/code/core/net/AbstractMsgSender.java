/**
 * Copyright (c) mbaobao.com 2011 All Rights Reserved.
 */
package com.cf.code.core.net;

import com.cf.code.core.exception.MsgSendException;

/**
 * 
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public abstract class AbstractMsgSender<TargetDataType, SendMsgType, ReceiveMsgType> implements MsgSender<TargetDataType, SendMsgType, ReceiveMsgType> {

	protected String Charset = "utf-8";

	@Override
	public void send_async(final TargetDataType target,final SendMsgType msg)throws MsgSendException {
		throw new MsgSendException(this.getClass().getName()+"暂不支持异步发送");
	}
	
}
