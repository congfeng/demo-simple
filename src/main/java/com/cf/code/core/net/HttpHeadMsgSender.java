/**
 * Copyright (c) mbaobao.com 2011 All Rights Reserved.
 */
package com.cf.code.core.net;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.HeadMethod;

import com.cf.code.common.Constant;
import com.cf.code.common.StringUtil;
import com.cf.code.core.exception.MsgSendException;

/**
 * 
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class HttpHeadMsgSender extends AbstractMsgSender<String, String, String>{

	@Override
    public String send(String targetUrl, String queryString)throws MsgSendException{
        if(StringUtil.isNullOrEmpty(targetUrl)){
            throw new MsgSendException("信息发送地址错误");
        }
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(80000);
        HeadMethod headMethod = new HeadMethod(targetUrl);
        try {
             client.executeMethod(headMethod);
        } catch (HttpException e) {
        	headMethod.releaseConnection();
            throw new MsgSendException("请求协议异常:"+e.getMessage());
        } catch (IOException e) {
        	headMethod.releaseConnection();
            throw new MsgSendException("请求网络异常:"+e.getMessage());
        }
        int statusCode = headMethod.getStatusCode();
        String resText = headMethod.getStatusText();
        headMethod.releaseConnection();
        if(statusCode != 200){
        	throw new MsgSendException("返回状态错误:"+statusCode+",msg:"+resText);
        }
        return resText;
    }
	
	@Override
	public String getDefaultCharset() {
		return Constant.Charset;
	}
}
