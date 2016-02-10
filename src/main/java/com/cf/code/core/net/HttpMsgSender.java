package com.cf.code.core.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.cf.code.common.Constant;
import com.cf.code.common.StringUtil;
import com.cf.code.common.WebUtil;
import com.cf.code.core.exception.MsgSendException;
import com.cf.code.core.net.HttpMsgSender.SendMsgType;

/**
 * 
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class HttpMsgSender extends AbstractMsgSender<String, SendMsgType, String>  {

	@SuppressWarnings("unchecked")
	@Override
    public String send(String targetUrl, SendMsgType msg)throws MsgSendException{
        if(StringUtil.isNullOrEmpty(targetUrl)){
            throw new MsgSendException("信息发送地址错误");
        }
        HttpClient client = new HttpClient();
        client.getHttpConnectionManager().getParams().setConnectionTimeout(80000);
        HashMap<String,String> appParams = msg.appParams;
		HashMap<String,String> sysParams = msg.sysParams;
//		sysParams.put(Constant.TransmitField.Sign, SignUtil.signTopRequestNew(SignSecretKey,msg.getAllParams()));
		String url = targetUrl + "?" + WebUtil.buildQuery(sysParams);
		PostMethod postMethod = new PostMethod(url){};
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, getDefaultCharset());
//        postMethod.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset="+getDefaultCharset());
        if(appParams != null&&appParams.size() > 0){
        	for(String key:appParams.keySet()){
            	postMethod.setParameter(key,appParams.get(key));
            }
        }
        try {
             client.executeMethod(postMethod);
        } catch (HttpException e) {
            postMethod.releaseConnection();
            throw new MsgSendException("请求协议异常:"+e.getMessage());
        } catch (IOException e) {
            postMethod.releaseConnection();
            throw new MsgSendException("请求网络异常:"+e.getMessage());
        }
        int statusCode = postMethod.getStatusCode();
        String resText = null;
        InputStream is = null;
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            is = postMethod.getResponseBodyAsStream();
            byte[] buffer = new byte[1024];  
            int len = -1;  
            while ((len = is.read(buffer)) != -1) {  
                outSteam.write(buffer, 0, len);  
            }
            resText = new String(outSteam.toByteArray(), getDefaultCharset());
        } catch (IOException e) {
            throw new MsgSendException("读取响应信息异常:"+e.getMessage());
        } finally{
            postMethod.releaseConnection();
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
	
	public static class SendMsgType{
		
		public HashMap<String,String> appParams;
		
		public HashMap<String,String> sysParams;
		
		public HashMap<String,String> getAllParams(){
			HashMap<String,String> params = new HashMap<String,String>();
			if(appParams != null&&!appParams.isEmpty()){
				params.putAll(appParams);
			}
			if(sysParams != null&&!sysParams.isEmpty()){
				params.putAll(sysParams);
			}
			return params;
		}
		
	}
	
	@Override
	public String getDefaultCharset() {
		return Constant.Charset;
	}
}
