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
public interface MsgSender<TargetDataType,SendMsgType,ReceiveMsgType> {
    
    public ReceiveMsgType send(TargetDataType target,SendMsgType msg)throws MsgSendException;
    
    public void send_async(final TargetDataType target,final SendMsgType msg)throws MsgSendException;
    
    public String getDefaultCharset();
}
