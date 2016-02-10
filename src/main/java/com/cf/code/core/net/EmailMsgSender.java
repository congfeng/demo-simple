/**
 * Copyright (c) mbaobao.com 2011 All Rights Reserved.
 */
package com.cf.code.core.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.cf.code.common.Constant;
import com.cf.code.common.StringUtil;
import com.cf.code.core.exception.MsgSendException;
import com.cf.code.core.net.EmailMsgSender.EmailSendMsgType;
import com.cf.code.core.net.EmailMsgSender.EmailTargetDataType;

/**
 * 
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class EmailMsgSender extends AbstractMsgSender<EmailTargetDataType, EmailSendMsgType, String>{
	
	public static enum SendHostEnum {
		SendHost1("ehaier","smtp.qiye.163.com",false), 
		SendHost2("qq","smtp.qq.com",false),
		SendHost3("foxmail","smtp.qq.com",false),
		SendHost4("163","smtp.163.com",false),
		SendHost6("sina", "smtp.sina.com",false),
		SendHost7("126", "smtp.126.com",false),
		SendHost8("yeah", "smtp.yeah.net",false),
		SendHost5("gmail","smtp.gmail.com",true),
//		SendHost9("139", "smtp.139.com",false),
//		SendHost12("sohu", "smtp.sohu.com",false),
//		SendHost11("yahoo", "smtp.mail.yahoo.com",false),
//		SendHost10("tom", "smtp.tom.com",false),
//		SendHost13("chinaren", "smtp.chinaren.com",false),
//		SendHost14("21cn", "smtp.21cn.com",false),
	    ;
		
		private SendHostEnum(String hostkey,String sendHost,boolean starttls){
			this.hostkey = hostkey;
	        this.sendHost = sendHost;
	        this.starttls = starttls;
	    };
	    
	    private String hostkey;
	    private String sendHost;
	    private boolean starttls;
	    
	    public static SendHostEnum findSendHostEnum(String hostkey){
	    	for(SendHostEnum sendHostEnum:SendHostEnum.values()){
	    		if(sendHostEnum.hostkey.equals(hostkey)){
	    			return sendHostEnum;
	    		}
	    	}
	    	return null;
	    }
	    
	}
	
    private SendHostEnum sendHostEnum;
    private String username;
    private String password;
    private String nick;
    
    public EmailMsgSender(String username,String password,String nick){
    	String hostkey = null;
		if(StringUtils.endsWithIgnoreCase(username, ".net")){
			hostkey = StringUtils.substringBetween(username,"@",".net").toLowerCase();
		}
		if(StringUtils.endsWithIgnoreCase(username, ".com")){
			hostkey = StringUtils.substringBetween(username,"@",".com").toLowerCase();
		}
		SendHostEnum sendHostEnum = SendHostEnum.findSendHostEnum(hostkey);
		if(sendHostEnum == null){
			throw new RuntimeException("发件服务器未知");
		}
    	if(StringUtil.isNullOrEmpty(nick)){
    		nick = username;
    	}
    	this.sendHostEnum = sendHostEnum;
        this.username = username;
        this.password = password;
        this.nick = nick;
    }
    
    public EmailMsgSender(String username,String password){
    	this(username, password, null);
    }

    @Override
    public String send(EmailTargetDataType target, EmailSendMsgType msg) throws MsgSendException {
    	this.sendemail(target, msg, false);
    	return null;
    }
    
    @Override
    public void send_async(EmailTargetDataType target, EmailSendMsgType msg) throws MsgSendException {
    	this.sendemail(target, msg, true);
    }
    
    private void sendemail(EmailTargetDataType target, EmailSendMsgType msg,boolean isAsync) throws MsgSendException{
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.timeout", "25000");
        if(this.sendHostEnum.starttls){
        	prop.setProperty("mail.smtp.starttls.enable", "true");
        }
//        prop.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//        prop.setProperty("mail.smtp.socketFactory.fallback", "false");
        mailSender.setJavaMailProperties(prop);
        mailSender.setHost(this.sendHostEnum.sendHost);
        mailSender.setUsername(this.username);
        mailSender.setPassword(this.password);
//        mailSender.setPort(465);
//        mailSender.setProtocol(protocol);
        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, Constant.Charset);
            mimeMessageHelper.setFrom(MimeUtility.encodeWord(this.nick)+ " <"+ this.username+ ">");
            mimeMessageHelper.setSubject(target.subject);
            mimeMessageHelper.setTo(target.toEmails.toArray(new String[0]));
            mimeMessageHelper.setCc(target.toCcEmails.toArray(new String[0]));
            mimeMessageHelper.setBcc(target.toBccEmails.toArray(new String[0]));
            mimeMessageHelper.setText(msg.msg,msg.isHtml);
            for(String attachmentName:msg.attachmentMap.keySet()){
            	mimeMessageHelper.addAttachment(MimeUtility.encodeWord(attachmentName),new ByteArrayResource(msg.attachmentMap.get(attachmentName)));
            }
            if(isAsync){
            	new Thread(new Runnable() {
        			@Override
        			public void run() {
        				mailSender.send(mimeMessage);
        			}
        		}).start();
            }else{
            	mailSender.send(mimeMessage);
            }
        } catch (MessagingException e) {
            throw new MsgSendException(e.getMessage());
        } catch (UnsupportedEncodingException e) {
        	throw new MsgSendException(e.getMessage());
		}
    }

    public static class EmailTargetDataType{
        
    	public EmailTargetDataType(String subject,String... toEmails){
            this.subject = subject;
            for(String toEmail:toEmails){
            	this.toEmails.add(toEmail);
            }
        }
        
        private String subject;
        private Set<String> toEmails = new HashSet<String>();
        private Set<String> toCcEmails = new HashSet<String>();
        private Set<String> toBccEmails = new HashSet<String>();
        
        public void setToCcEmails(String... toCcEmails){
        	for(String toCcEmail:toCcEmails){
        		this.toCcEmails.add(toCcEmail);
        	}
        }
        
        public void setToBccEmails(String... toBccEmails){
        	for(String toBccEmail:toBccEmails){
        		this.toBccEmails.add(toBccEmail);
        	}
        }
		
    }

    public static class EmailSendMsgType{
    	
    	public EmailSendMsgType(String msg){
            this(msg, false, null);
        }
    	
    	public EmailSendMsgType(String msg,boolean isHtml){
            this(msg, isHtml, null);
        }
    	
        public EmailSendMsgType(String msg,boolean isHtml,Map<String,byte[]> attachmentMap){
            this.msg = msg;
            this.isHtml = isHtml;
            if(attachmentMap != null){
            	this.attachmentMap = attachmentMap;
            }
        }
        
        private String msg;
        private boolean isHtml;
        private Map<String,byte[]> attachmentMap = new HashMap<String,byte[]>();
        
		public void putAttachment(String attachmentName,byte[] attachmentContent) {
			this.attachmentMap.put(attachmentName, attachmentContent);
		}
        
    }

	@Override
	public String getDefaultCharset() {
		return Constant.Charset;
	}
    
}
