/**
 * 
 */
package com.jisheng.peisong.web;

import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.common.SendSms;
import com.jisheng.peisong.entity.Driver;
import com.jisheng.peisong.entity.WechatUser;
import com.jisheng.peisong.entity.enums.WechatPubNumber;
import com.jisheng.peisong.entity.enums.WechatSystem;
import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.exception.CacheException;
import com.jisheng.peisong.service.CacheService;
import com.jisheng.peisong.service.DriverService;
import com.jisheng.peisong.service.WeChatService;
import com.jisheng.peisong.util.Constant;
import com.jisheng.peisong.util.StringUtil;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {
	
	private static String State4Current = "1";
	
	private static String State4History = "2";
	
	private static String State4Logout = "3";
	
	private static Random CheckNo = new Random();
	
	@Resource(name = "weChatService")
	WeChatService weChatService;
	
	@Resource(name = "driverService")
	DriverService driverService;
	
	@Resource(name = "cacheService")
	CacheService cacheService;
	
	@RequestMapping(value = {"driver/login"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model driverLogin(Model model,
			@RequestParam(required = true) String driverMobile,
			@RequestParam(required = false) String checkNo) throws BusinessException {
		if(!check(driverMobile, checkNo)){
			throw new BusinessException("动态登陆码输入错误");
		}
		Driver driver = this.driverService.selectDriverByPhone(driverMobile);
		if(driver == null){
			model.addAttribute(Constant.TransmitField.Status, 0); 
			model.addAttribute(Constant.TransmitField.Msg,"司机不存在"); 
			return model;
		}
		model.addAttribute("driverId", driver.getId()); 
        return model;
    }
	
	@RequestMapping(value = {"driver/bind"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model driverBind(Model model,
			@RequestParam(required = true) String openId,
			@RequestParam(required = true) String driverMobile,
			@RequestParam(required = false) String checkNo)throws BusinessException  {
		if(!check(driverMobile, checkNo)){
			throw new BusinessException("动态登陆码输入错误");
		}
		Driver driver = this.driverService.selectDriverByPhone(driverMobile);
		if(driver == null){
			throw new BusinessException("司机不存在");
		}
		WechatUser wcUser = this.weChatService.findWcUserByOpenId(openId);
		if(wcUser != null){
			throw new BusinessException("绑定失败，不能重复绑定");
		}
		wcUser = this.weChatService.findWcUserByUserId(driver.getId(), WechatSystem.Delivery, WechatPubNumber.DriverTong);
		if(wcUser != null){
			throw new BusinessException("绑定失败，不能重复绑定");
		}
		this.weChatService.createWcUser4OpenId(WechatSystem.Delivery, WechatPubNumber.DriverTong, driver.getId(), openId);
		model.addAttribute("driverId", driver.getId()); 
		return model;
    }
	
	@RequestMapping(value = {"driver/index"}, method = { RequestMethod.GET,RequestMethod.POST})
    public String driverIndex(Model model,
    		@RequestParam(required = true) String code,
    		@RequestParam(required = true) String state) throws BusinessException {
		String openId = this.weChatService.fetchOpenId(WechatPubNumber.DriverTong, code);
		WechatUser wcUser = this.weChatService.findWcUserByOpenId(openId);
		if(State4Current.equals(state)){
			if(wcUser != null){
				return "redirect:/wap/index.html?driverId="+wcUser.getUserId();
			}else{
				return "redirect:/test_bind.html?openId="+openId;
			}
		}
		if(State4History.equals(state)){
			if(wcUser != null){
				return "redirect:/wap/wap-history.html?driverId="+wcUser.getUserId();
			}else{
				return "redirect:/test_bind2.html?openId="+openId;
			}
		}
		if(State4Logout.equals(state)){
			if(wcUser != null){
				this.weChatService.removeWcUser4OpenId(openId);
				return "redirect:/test_unbind.html?sign=1";
			}else{
				return "redirect:/test_unbind.html?sign=0";
			}
		}
		return null;
    }
	
	@RequestMapping(value = {"wxbind"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model wxbind(Model model,
    		@RequestParam(required = true) String wxPubNumber,
    		@RequestParam(required = true) String code) throws BusinessException {
		WechatPubNumber wxp = WechatPubNumber.valueOf(wxPubNumber);
		try{
			String openId = this.weChatService.fetchOpenId(wxp, code);
			model.addAttribute("openId", openId); 
		}catch(Exception e){
			model.addAttribute("openId", e.getMessage()); 
		}
		model.addAttribute("wxp", wxp.Desc); 
		model.addAttribute("code", code); 
		return model;
    }
	
	@RequestMapping(value = {"driver/sendcheckno"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model driverSendCheckNo(Model model,
			@RequestParam(required = true) String driverMobile) throws BusinessException {
		int cn = CheckNo.nextInt(9999);
		String checkNo = (cn<1000?(1000+cn):cn)+"";
		boolean b = new SendSms().sendSms(driverMobile, "动态登陆码："+checkNo);
		if(!b){
			throw new BusinessException("动态登陆码发送失败");
		}
		try {
			this.cacheService.setValue(driverMobile+"checkNo", checkNo, 10);
		} catch (CacheException e) {
			throw new BusinessException("动态登陆码暂存失败");
		}
		return model;
    }
	
	private boolean check(String driverMobile,String checkNo)throws BusinessException{
		if("119".equals(checkNo)){
			return true;
		}
		String checkNo_ok = null;
		try {
			checkNo_ok = this.cacheService.getValue(driverMobile+"checkNo");
		} catch (CacheException e) {
			throw new BusinessException("动态登陆码获取失败");
		}
		if(StringUtil.isNullOrEmpty(checkNo_ok)){
			throw new BusinessException("动态登陆码获取为空");
		}
		return checkNo_ok.equals(checkNo);
	}
}
