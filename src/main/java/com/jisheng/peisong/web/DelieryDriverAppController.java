/**
 * 
 */
package com.jisheng.peisong.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.entity.DeliveryCart;
import com.jisheng.peisong.entity.DeliveryOrder;
import com.jisheng.peisong.entity.DeliveryOrderGoods;
import com.jisheng.peisong.entity.DeliveryOrderReturnQueue;
import com.jisheng.peisong.entity.DeliveryOrderStateQueue;
import com.jisheng.peisong.entity.enums.SignType;
import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.service.CollectionDetailService;
import com.jisheng.peisong.service.DeliveryCartService;
import com.jisheng.peisong.service.DeliveryDriverService;
import com.jisheng.peisong.service.DeliveryOperateLogService;
import com.jisheng.peisong.service.DeliveryOrderService;
import com.jisheng.peisong.util.DateUtil;
import com.jisheng.peisong.util.DeliveryUtil;
import com.jisheng.peisong.util.StringUtil;
import com.jisheng.peisong.web.service.AsyncService;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Controller
@RequestMapping("/dcart/driverapp")
public class DelieryDriverAppController {
	
	@Resource(name = "deliveryCartService")
	DeliveryCartService deliveryCartService;
	
	@Resource(name = "deliveryOrderService")
	DeliveryOrderService deliveryOrderService;
	
	@Resource(name = "deliveryOperateLogService")
	DeliveryOperateLogService deliveryOperateLogService;
	
	@Resource(name = "deliveryDriverService")
	DeliveryDriverService deliveryDriverService;
	
	@Resource(name = "AsyncService")
	AsyncService asyncService;
	
	@Resource(name = "collectionDetailService")
	CollectionDetailService collectionDetailService;
	
	@RequestMapping(value = {"current/detail"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model currentDetail(Model model,@RequestParam(required = true) Integer driverId) {
		DeliveryCart dCart = this.deliveryDriverService.findDCartByDriver(driverId,true);
		if(dCart == null){
			model.addAttribute("dCart", null);
			model.addAttribute("dOrderList", null);
			return model;
		}
		List<DeliveryOrder> dOrderList = this.deliveryOrderService.qryDOrderList(dCart.getId(),true);
		int orderCount = 0;
		boolean isPickupAble = true;
		BigDecimal orderMoney = BigDecimal.ZERO;
		BigDecimal orderOutTakeMoney = BigDecimal.ZERO;
		BigDecimal orderPayMoney = BigDecimal.ZERO;
		Map<Integer,Map<String,Object>> dOrderBuyerMap = new HashMap<Integer,Map<String,Object>>();
		for(DeliveryOrder dOrder:dOrderList){
			orderCount++;
			if(isPickupAble&&dOrder.getOutTakeMoney() == null){
				isPickupAble = false;
			}
			if(dOrder.getOrderGoodsMoney() != null){
				orderMoney = orderMoney.add(dOrder.getOrderGoodsMoney());
			}
			if(dOrder.getOutTakeMoney() != null){
				orderOutTakeMoney = orderOutTakeMoney.add(dOrder.getOutTakeMoney());
			}
			BigDecimal money = DeliveryUtil.calculateMoney(dOrder);
			dOrder.setMoney(money);
			orderPayMoney = orderPayMoney.add(money);
			Map<String,Object> dOrderBuyer = dOrderBuyerMap.get(dOrder.getBuyerId());
			if(dOrderBuyer == null){
				dOrderBuyer = new HashMap<String,Object>();
				Date reqDeliveryTime = dOrder.getReqDeliveryTime();
				dOrderBuyer.put("reqDeliveryTimeSlot",DateUtil.format(reqDeliveryTime,"MM月dd日 ")+dOrder.getReqDeliveryTimeSlot());
				dOrderBuyer.put("buyerName", dOrder.getBuyerName());
				dOrderBuyer.put("receiverMobile", dOrder.getReceiverMobile());
				dOrderBuyer.put("receiverDistrictName", dOrder.getReceiverDistrictName());
				dOrderBuyer.put("receiverStreetName", dOrder.getReceiverStreetName());
				dOrderBuyer.put("receiverAddress", dOrder.getReceiverAddress());
				dOrderBuyerMap.put(dOrder.getBuyerId(), dOrderBuyer);
			}
			List<DeliveryOrder> os = (List<DeliveryOrder>) dOrderBuyer.get("dOrderList");
			if(os == null){
				os = new ArrayList<DeliveryOrder>();
				dOrderBuyer.put("dOrderList", os);
			}
			os.add(dOrder);
		}
		

		model.addAttribute("dCart", dCart);   
		model.addAttribute("dOrderBuyerList", dOrderBuyerMap.values());
		model.addAttribute("orderCount", orderCount);
		model.addAttribute("buyerCount", dOrderBuyerMap.size());
		model.addAttribute("orderMoney", orderMoney);
		model.addAttribute("orderOutTakeMoney", orderOutTakeMoney);
		model.addAttribute("orderPayMoney", orderPayMoney);
		model.addAttribute("orderPayedMoney", dCart.isReceivables()?orderPayMoney:0);
		model.addAttribute("isPickupAble", isPickupAble);
        return model;
    }
	
	@RequestMapping(value = {"history/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model historyList(Model model,
    		@RequestParam(required = true) Integer driverId,
    		@RequestParam(required = false) String historyDay) {
		if(StringUtil.isNullOrEmpty(historyDay)){
			historyDay = DateUtil.format(new Date(), "yyyy-MM-dd");
		}
		List<DeliveryCart> dCartList = this.deliveryDriverService.qryDCartsByDriver(driverId, historyDay);
		if(dCartList == null||dCartList.size() == 0){
			model.addAttribute("dCartMapList", dCartList);  
			return model;
		}
		List<Map<String,Object>> dCartMapList = new ArrayList<Map<String,Object>>();
		for(DeliveryCart dCart:dCartList){
			Map<String,Object> dCartMap = new HashMap<String,Object>();
			List<DeliveryOrder> dOrderList = this.deliveryOrderService.qryDOrderList(dCart.getId(),true);
			int orderCount = 0;
			boolean isPickupAble = true;
			BigDecimal orderMoney = BigDecimal.ZERO;
			BigDecimal orderOutTakeMoney = BigDecimal.ZERO;
			BigDecimal orderPayMoney = BigDecimal.ZERO;
			Map<Integer,Map<String,Object>> dOrderBuyerMap = new HashMap<Integer,Map<String,Object>>();
			for(DeliveryOrder dOrder:dOrderList){
				orderCount++;
				if(isPickupAble&&dOrder.getOutTakeMoney() == null){
					isPickupAble = false;
				}
				if(dOrder.getOrderGoodsMoney() != null){
					orderMoney = orderMoney.add(dOrder.getOrderGoodsMoney());
				}
				if(dOrder.getOutTakeMoney() != null){
					orderOutTakeMoney = orderOutTakeMoney.add(dOrder.getOutTakeMoney());
				}
				BigDecimal money = DeliveryUtil.calculateMoney(dOrder);
				dOrder.setMoney(money);
//				orderPayMoney = orderPayMoney.add(money);
				Map<String,Object> dOrderBuyer = dOrderBuyerMap.get(dOrder.getBuyerId());
				if(dOrderBuyer == null){
					dOrderBuyer = new HashMap<String,Object>();
					Date reqDeliveryTime = dOrder.getReqDeliveryTime();
					dOrderBuyer.put("reqDeliveryTimeSlot",DateUtil.format(reqDeliveryTime,"MM月dd日 ")+dOrder.getReqDeliveryTimeSlot());
					dOrderBuyer.put("buyerName", dOrder.getBuyerName());
					dOrderBuyer.put("receiverMobile", dOrder.getReceiverMobile());
					dOrderBuyer.put("receiverDistrictName", dOrder.getReceiverDistrictName());
					dOrderBuyer.put("receiverStreetName", dOrder.getReceiverStreetName());
					dOrderBuyer.put("receiverAddress", dOrder.getReceiverAddress());
					dOrderBuyerMap.put(dOrder.getBuyerId(), dOrderBuyer);
				}
				List<DeliveryOrder> os = (List<DeliveryOrder>) dOrderBuyer.get("dOrderList");
				if(os == null){
					os = new ArrayList<DeliveryOrder>();
					dOrderBuyer.put("dOrderList", os);
				}
				os.add(dOrder);
			}
			orderPayMoney = 	collectionDetailService.countReceivablesByDCartId(dCart.getId());
	        if(null == orderPayMoney)orderPayMoney = BigDecimal.ZERO;
			dCartMap.put("dCart", dCart);
			dCartMap.put("dOrderBuyerList", dOrderBuyerMap.values());
			dCartMap.put("orderCount", orderCount);
			dCartMap.put("buyerCount", dOrderBuyerMap.size());
			dCartMap.put("orderMoney", orderMoney);
			dCartMap.put("orderOutTakeMoney", orderOutTakeMoney);
			dCartMap.put("orderPayMoney", orderPayMoney);
			dCartMap.put("orderPayedMoney", dCart.isReceivables()?orderPayMoney:0);
			dCartMap.put("isPickupAble", isPickupAble);
			dCartMapList.add(dCartMap);
		}
		model.addAttribute("dCartMapList", dCartMapList);   
        return model;
    }
	
	@RequestMapping(value = {"goods/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model goodsList(Model model,@RequestParam(required = true) Integer dOrderId) {
		DeliveryOrder dOrder = this.deliveryOrderService.getDOrder(dOrderId);
		if( null == dOrder ){
			model.addAttribute("s", 0);
			model.addAttribute("t", 6);
			model.addAttribute("m", "数据错误,请重试!");
			return model;
		}
		model.addAttribute("signType", dOrder.getSignType());
		model.addAttribute("signMoney", dOrder.getSignMoney());
		model.addAttribute("orderType", dOrder.getOrderType());
		model.addAttribute("orderNo", dOrder.getOrderNo());
		model.addAttribute("outTakeNo", dOrder.getOutTakeNo());
		model.addAttribute("orderGoodsMoney", dOrder.getOrderGoodsMoney());
		model.addAttribute("outTakeMoney", dOrder.getOutTakeMoney());
		model.addAttribute("returnOrderNo", dOrder.getReturnOrderNo());
		BigDecimal money = DeliveryUtil.calculateMoney(dOrder);
		model.addAttribute("money", money);
		List<DeliveryOrderGoods> dOrderGoodsList = this.deliveryOrderService.qryDOrderGoodsList(dOrderId);
		if(dOrder.getSignType() == null||dOrder.getSignType().equals(SignType.Full)){
			model.addAttribute("goodsList", dOrderGoodsList); 
			return model;
		}
		List<DeliveryOrderGoods> goodsList = new ArrayList<DeliveryOrderGoods>();
		List<DeliveryOrderGoods> backGoodsList = new ArrayList<DeliveryOrderGoods>();
		for(DeliveryOrderGoods doGoods:dOrderGoodsList){
			if(doGoods.getSignNum().equals(doGoods.getNum())){
				goodsList.add(doGoods);
			}else if(doGoods.getSignNum().compareTo(BigDecimal.ZERO) == 0){
				backGoodsList.add(doGoods);
			}else{
				goodsList.add(doGoods);
				DeliveryOrderGoods backGoods = new DeliveryOrderGoods();
				backGoods.setGoodsName(doGoods.getGoodsName());
				backGoods.setGoodsUnitName(doGoods.getGoodsUnitName());
				backGoods.setGoodsUnitType(doGoods.getGoodsUnitType());
				backGoods.setSignBackType(doGoods.getSignBackType());
				backGoods.setNum(doGoods.getNum());
				backGoods.setSignNum(doGoods.getSignNum());
				backGoodsList.add(backGoods);
			}
		}
		model.addAttribute("goodsList", goodsList); 
		model.addAttribute("backGoodsList", backGoodsList); 
		return model;
    }
	
	@RequestMapping(value = {"pickup"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model pickup(Model model,@RequestParam(required = true) Integer dCartId) throws BusinessException {
		Date curTime = new Date();
		List<Integer> dOrderIds = this.deliveryOrderService.qryDOrderIds(dCartId);
		List<DeliveryOrderStateQueue> dOrderStateQueues = this.deliveryDriverService.txPickupDOrderGoods(dCartId,dOrderIds,curTime);
		this.deliveryOperateLogService.notePickupDOrderGoods("司机A", curTime, dCartId, dOrderIds);
        this.asyncService.sendDOrderState4Pickuped(dOrderStateQueues);
		return model;
    }
	@RequestMapping(value = {"check/pickup/orders"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model checkPickupOrder(Model model,@RequestParam(required = true) Integer dCartId) throws BusinessException {
		List<Integer> dOrderIds = this.deliveryOrderService.qryDOrderIds(dCartId);
		if(null != dOrderIds && dOrderIds.size() > 0 ){
			model.addAttribute("orderSize", dOrderIds.size());
		}else{
			model.addAttribute("orderSize", 0);
		}
		model.addAttribute("s", 1);
		model.addAttribute("m", "查询成功!");
		return model;
    }
	@RequestMapping(value = {"deliver"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model deliver(Model model,
    		@RequestParam(required = true) Integer dCartId,
    		@RequestParam(required = true) Integer dOrderId,
    		@RequestParam(required = false) Boolean isLast,
    		@RequestParam(value = "dOrderGoodsIds[]") Integer[] dOrderGoodsIds,
    		@RequestParam(value = "dOrderGoodsNums[]") BigDecimal[] dOrderGoodsNums) throws BusinessException {
		Date curTime = new Date();
		Map<Integer,BigDecimal> dOrderGoodsMap = new HashMap<Integer,BigDecimal>();
		for(int i=0;i<dOrderGoodsIds.length;i++){
			dOrderGoodsMap.put(dOrderGoodsIds[i], dOrderGoodsNums[i]);
		}
		List<DeliveryOrderGoods> dOrderGoodsList = this.deliveryOrderService.qryDOrderGoodsList(dOrderId);
		Object[] qs = this.deliveryDriverService.txDeliverDOrderGoods(dCartId, dOrderId, dOrderGoodsList, dOrderGoodsMap, curTime);
		int unSignCount = (Integer) qs[2];
		this.deliveryOperateLogService.noteDeliverDOrderGoods("司机B",curTime, dCartId, dOrderId,unSignCount);
		this.asyncService.sendDOrderState4Delivered((DeliveryOrderReturnQueue)qs[0],(DeliveryOrderStateQueue)qs[1]);
		return model;
    }
	
	@RequestMapping(value = {"lbs"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model lbs(Model model,
    		@RequestParam(required = true) Integer dCartId,
    		@RequestParam(required = true) Integer driverId,
    		@RequestParam(required = true) Double latitude,
    		@RequestParam(required = true) Double longitude) throws BusinessException {
		Date curTime = new Date();
		this.deliveryDriverService.noteDCartDriverLBS(dCartId, driverId, latitude, longitude, curTime);
        return model;
    }
	
}
