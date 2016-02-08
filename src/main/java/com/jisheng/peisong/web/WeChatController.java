/**
 * 
 */
package com.jisheng.peisong.web;

import java.net.URLEncoder;
import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.entity.Driver;
import com.jisheng.peisong.entity.TemplateParam;
import com.jisheng.peisong.entity.enums.WechatPubNumber;
import com.jisheng.peisong.entity.enums.WechatSystem;
import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.service.DriverService;
import com.jisheng.peisong.service.WeChatMsgQueueService;
import com.jisheng.peisong.service.WeChatService;
import com.jisheng.peisong.util.StringUtil;
import com.jisheng.peisong.util.WebUtil;
import com.jisheng.peisong.wx.AesException;
import com.jisheng.peisong.wx.Constant;
import com.jisheng.peisong.wx.MessageUtil;
import com.jisheng.peisong.wx.SHA1;
import com.jisheng.peisong.wx.resp.NewsMessage;
import com.jisheng.peisong.wx.resp.NewsMessage.Article;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Controller
@RequestMapping("/wechat")
public class WeChatController {
	
	private static final String TemplateRemark = "提货地点：%s\r\n配送商户数：%s家\r\n街道：%s";
	
	@Resource(name = "weChatService")
	WeChatService weChatService;
	
	@Resource(name = "weChatMsgQueueService")
	WeChatMsgQueueService weChatMsgQueueService;
	
	@Resource(name = "driverService")
	DriverService driverService;
	
	@RequestMapping("")
    public void receive(
    		@RequestParam(required = false) String signature,
    		@RequestParam(required = false) String timestamp,
    		@RequestParam(required = false) String nonce,
    		@RequestParam(required = false) String echostr,
    		@RequestParam(required = false) String encrypt_type,
    		@RequestParam(required = false) String msg_signature,
    		HttpServletRequest request,HttpServletResponse response)throws AccessException{
		String signature_ok = null;
		try {
			signature_ok = SHA1.getSHA1(WechatPubNumber.DriverTong.Token, timestamp, nonce, "");
		} catch (AesException e) {
			throw new AccessException("验证加密异常："+e);
		}
		if(!signature.equals(signature_ok)){
			throw new AccessException("验证失败");
		}
		if(!StringUtil.isNullOrEmpty(echostr)){
			WebUtil.setJsonRet(response, echostr,true);
	        return ;
		}
		Map<String, String> reqParams = null;
		if("aes".equalsIgnoreCase(encrypt_type)){
			try {
				String req = WebUtil.readResponseStringFromRequestBody(request);
				String reqXml = WechatPubNumber.DriverTong.wbmc.decryptMsg(msg_signature, timestamp, nonce, req);
				reqParams = MessageUtil.parseXml(reqXml);
			} catch (AesException e) {
				throw new AccessException("WXBizMsgCrypt解密异常：" + e);
			}
		}else{
			reqParams = MessageUtil.parseXml(request);
		}
		String title = null;
		String msgType = reqParams.get(Constant.ParamNames.MsgType);
		if(Constant.MsgType.Req_Text.equals(msgType)){
			String reqContent = reqParams.get(Constant.ParamNames.Content);
			if("绑定".equals(reqContent)){
				title = "微信号与账号绑定";
			}
		}else if(Constant.MsgType.Req_Event.equals(msgType)){
			String eventType = reqParams.get("Event"); 
			if (Constant.MsgType.Event_Subscribe.equals(eventType)) {  
				title = "感谢关注";
			}
		}
		if(StringUtil.isNullOrEmpty(title)){
	        return ;
		}
		String url = String.format(Constant.UserBindUrl, URLEncoder.encode(Constant.UserBindCallBackUrl),WechatPubNumber.DriverTong.AppId);
		NewsMessage message = new NewsMessage();
		message.setFromUserName(reqParams.get(Constant.ParamNames.ToUserName));
		message.setToUserName(reqParams.get(Constant.ParamNames.FromUserName));
		message.setCreateTime(System.currentTimeMillis()/1000);
		message.setMsgType(msgType);
		message.setFuncFlag(0);
		message.setArticleCount(1);  
		List<Article> articleList = new ArrayList<Article>();  
		Article article= new Article();  
        article.setTitle(title);  
        article.setDescription("点击此链接，完成司机账号绑定操作，绑定后：\r\n1. 司机可通过此公账号收取派车任务\r\n2. 司机进入司机通系统无需登录");
        article.setPicUrl("http://www.91nongye.com/images/logo.png?"+System.currentTimeMillis());  
        article.setUrl(url);  
        articleList.add(article);
		message.setArticles(articleList);  
		String respXml = MessageUtil.newsMessageToXml(message);
		if("aes".equalsIgnoreCase(encrypt_type)){
			try {
				respXml = WechatPubNumber.DriverTong.wbmc.encryptMsg(respXml, timestamp, nonce);
			} catch (AesException e) {
				throw new AccessException("WXBizMsgCrypt回复加密异常：" + e);
			}
		}
		WebUtil.setJsonRet(response, respXml ,true);
    }

	@RequestMapping(value = {"bind"}, method = { RequestMethod.GET,RequestMethod.POST})
    public String bind(Model model,@RequestParam(required = true) String code,@RequestParam(required = true) String state) throws BusinessException {
		String openId = this.weChatService.fetchOpenId(WechatPubNumber.DriverTong, code);
		return "redirect:/test_bind.html?openId="+openId;
    }
	
	@RequestMapping(value = {"sendmsg"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model sendmsg(Model model,
			@RequestParam(required = true) String driverMobile) throws BusinessException {
		Driver driver = this.driverService.selectDriverByPhone(driverMobile);
		if(driver == null){
			throw new BusinessException("司机不存在");
		}
		StringBuilder streetNames = new StringBuilder();
		streetNames.append("张江镇"+"，");
		streetNames.append("嘉定镇街道"+"，");
		streetNames.append("北蔡镇"+"，");
		TemplateParam msgParam = new TemplateParam("http://www.91nongye.com",null)
		          .addTextField("first", "您有新的订单需派送")
		          .addTextField("keyword1", "100001")
		          .addTextField("keyword2", "等待提货")
		          .addTextField("keyword3", "2015-08-31 05:00:00")
		          .addTextField("remark", String.format(TemplateRemark,"浦东成品库",5+"",streetNames.substring(0, streetNames.length()-1)));
		this.weChatService.sendMsg(WechatSystem.Delivery, WechatPubNumber.DriverTong, driver.getId(),
				"5kQ-0ZL2eVhy9-_4X6r6UJraDBi3lIQMOu0fVnGX66g", msgParam);
		model.addAttribute("m", "已发送消息"); 
		return model;
    }
	
}
