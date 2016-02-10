/**
 * Copyright (c) mbaobao.com 2011 All Rights Reserved.
 */
package com.cf.code.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

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
public class HttpGetMsgSender extends AbstractMsgSender<String, Map<String,String>, String> {
    
    @Override
    public String send(String targetUrl, Map<String,String> msgMap)throws MsgSendException{
        if(StringUtil.isNullOrEmpty(targetUrl)){
            throw new MsgSendException("信息发送地址错误");
        }
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(80000);
        GetMethod getMethod = new GetMethod(targetUrl){};
        if(msgMap!=null&&!msgMap.isEmpty()){
            getMethod.setQueryString(buildQuery(msgMap, getDefaultCharset()));
        }
        try {
             client.executeMethod(getMethod);
        } catch (HttpException e) {
            getMethod.releaseConnection();
            throw new MsgSendException("请求协议异常:"+e.getMessage());
        } catch (IOException e) {
            getMethod.releaseConnection();
            throw new MsgSendException("请求网络异常:"+e.getMessage());
        }
        int statusCode = getMethod.getStatusCode();
        String resText = null;
        InputStream is = null;
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            is = getMethod.getResponseBodyAsStream();
            byte[] buffer = new byte[1024];  
            int len = -1;  
            while ((len = is.read(buffer)) != -1) {  
                outSteam.write(buffer, 0, len);  
            }
            resText = new String(outSteam.toByteArray(), getDefaultCharset());
        } catch (IOException e) {
            throw new MsgSendException("读取响应信息异常:"+e.getMessage());
        } finally{
            getMethod.releaseConnection();
            try {
                outSteam.close();
            } catch (IOException e) {
                outSteam = null;
            }  
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                }
            }
        }
        if(statusCode != 200){
        	throw new MsgSendException("返回状态错误:"+statusCode+",msg:"+resText);
        }
        return resText;
    }
    
    private String buildQuery(Map<String, String> params, String charset) throws MsgSendException {
		if (params == null || params.isEmpty()) {
			return null;
		}
		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;
		try {
			for (Entry<String, String> entry : entries) {
				String name = entry.getKey();
				String value = entry.getValue();
				if (!StringUtil.isNullOrEmpty(value)) {
					if (hasParam) {
						query.append("&");
					} else {
						hasParam = true;
					}
					
						query.append(name).append("=").append(URLEncoder.encode(value, charset));
					
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new MsgSendException("编码不支持："+charset);
		}
		return query.toString();
	}

	@Override
	public String getDefaultCharset() {
		return Constant.Charset;
	}

}
