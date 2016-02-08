/**
 * 
 */
package com.jisheng.peisong.web;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.dao.DriverMapper;
import com.jisheng.peisong.entity.DeliveryCart;
import com.jisheng.peisong.entity.Driver;
import com.jisheng.peisong.entity.enums.DCartState;
import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.service.DeliveryCartService;
import com.jisheng.peisong.service.DeliveryDriverService;
import com.jisheng.peisong.service.DeliveryOperateLogService;
import com.jisheng.peisong.service.DeliveryOrderService;
import com.jisheng.peisong.service.DriverService;
import com.jisheng.peisong.util.Constant;
import com.jisheng.peisong.vo.PeopleProfile;
import com.jisheng.peisong.web.session.SessionCheckInject;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 * 
 * 派车单与司机相关操作
 * 
 */
@Controller
@RequestMapping("/dcart/driver")
public class DeliveryDriverController {

	@Resource(name = "deliveryCartService")
	DeliveryCartService deliveryCartService;
	
	@Resource(name = "deliveryOrderService")
	DeliveryOrderService deliveryOrderService;
	
	@Resource(name = "deliveryOperateLogService")
	DeliveryOperateLogService deliveryOperateLogService;
	
	@Resource(name = "deliveryDriverService")
	DeliveryDriverService deliveryDriverService;
	
	@Resource(name = "driverMapperRead")
	DriverMapper driverMapperRead;
	
	@Resource(name = "driverService")
	DriverService driverService;
	
	@SessionCheckInject
	@RequestMapping(value = {"assign"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model assign(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		@RequestParam(required = true) Integer dCartId,
    		@RequestParam(required = true) Integer driverId) throws BusinessException {
		Date curTime = new Date();
		//TODO	使用service代替dao
		Driver driver = this.driverMapperRead.selectByPrimaryKey(driverId);		
		if(driver.getLockStatus() != null&&driver.getLockStatus()){
			model.addAttribute(Constant.TransmitField.Status, 0);
			model.addAttribute(Constant.TransmitField.Msg, "司机已分配其他派车单");
			return model;
		}
		DeliveryCart dCart = this.deliveryCartService.findDCart(dCartId);
		if(!dCart.getCityId().equals(driver.getCityId())){
			model.addAttribute(Constant.TransmitField.Status, 0);
			model.addAttribute(Constant.TransmitField.Msg, "司机与派车单不属于同一城市");
			return model;
		}
		this.deliveryDriverService.txAssignDriver(dCartId, driver);
		this.deliveryOperateLogService.noteAssignDriver(profile.getPeopleName(), curTime, dCartId,driver);
        return model;
    }
	
	@SessionCheckInject
	@RequestMapping(value = {"cancel"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model cancel(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		@RequestParam(required = true) Integer dCartId,
    		@RequestParam(required = true) Integer driverId) throws BusinessException {
		Date curTime = new Date();
		this.deliveryDriverService.txCancelAssignDriver(dCartId, driverId);
		this.deliveryOperateLogService.noteCancelAssignDriver(profile.getPeopleName(), curTime, dCartId);
		return model;
    }
	
	@SessionCheckInject
	@RequestMapping(value = {"dorder/alter"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model dorderAlter(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		@RequestParam(required = true) Integer dCartId,
    		@RequestParam(required = true) Integer dOrderId,
    		@RequestParam(required = true) Integer driverId) throws BusinessException {
		DeliveryCart relatedDCart = this.deliveryDriverService.findDCartByDriver(driverId,false);
		if(relatedDCart == null){
			model.addAttribute(Constant.TransmitField.Status, 0);
			model.addAttribute(Constant.TransmitField.Msg, "当前改派司机没有已分配且未提货的派车单");
			return model;
		}
		if(!relatedDCart.getState().equals(DCartState.AssignedDriver)){
			model.addAttribute(Constant.TransmitField.Status, 0);
			model.addAttribute(Constant.TransmitField.Msg, "当前改派司机所在的派车单状态不允许改派");
			return model;
		}
		DeliveryCart dCart = this.deliveryCartService.findDCart(dCartId);
		if(!dCart.getState().equals(DCartState.AssignedDriver)){
			model.addAttribute(Constant.TransmitField.Status, 0);
			model.addAttribute(Constant.TransmitField.Msg, "派车单当前状态不允许改派");
			return model;
		}
		if(!dCart.getStorageId().equals(relatedDCart.getStorageId())){
			model.addAttribute(Constant.TransmitField.Status, 0);
			model.addAttribute(Constant.TransmitField.Msg, "当前配送单与改派司机所在的派车单不在同一个提货仓库");
			return model;
		}
		if(!dCart.getCityId().equals(relatedDCart.getCityId())){
			model.addAttribute(Constant.TransmitField.Status, 0);
			model.addAttribute(Constant.TransmitField.Msg, "当前配送单与改派司机所在的派车单不在同一个城市");
			return model;
		}
		if(dCart.getDriverId() == null){
			model.addAttribute(Constant.TransmitField.Status, 0);
			model.addAttribute(Constant.TransmitField.Msg, "当前派车单未分配司机");
			return model;
		}
		if(driverId.equals(dCart.getDriverId())){
			model.addAttribute(Constant.TransmitField.Status, 0);
			model.addAttribute(Constant.TransmitField.Msg, "改派司机与当前派车单司机是同一个人");
			return model;
		}
		List<Integer> dOrderIds = this.deliveryOrderService.qryDOrderIds(dCartId);
		if(!dOrderIds.remove(dOrderId)){
			model.addAttribute(Constant.TransmitField.Status, 0);
			model.addAttribute(Constant.TransmitField.Msg, "当前派车单已不存在此配送订单");
			return model;
		}
		boolean isReleaseDCart = dOrderIds.size() == 0;
		Date curTime = new Date();
		this.deliveryDriverService.txAlterAssignDriver(dCart, dOrderId, relatedDCart.getId(),isReleaseDCart);
		this.deliveryOperateLogService.noteAlterAssignDriver(profile.getPeopleName(), curTime, dOrderId, dCart, relatedDCart,isReleaseDCart);
		return model;
    }
	
}
