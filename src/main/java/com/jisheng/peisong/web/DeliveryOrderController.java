/**
 * 
 */
package com.jisheng.peisong.web;

import java.math.BigDecimal;
import java.util.List;

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
import com.jisheng.peisong.entity.DeliveryOrderUpdateLog;
import com.jisheng.peisong.service.DeliveryCartService;
import com.jisheng.peisong.service.DeliveryOperateLogService;
import com.jisheng.peisong.service.DeliveryOrderService;
import com.jisheng.peisong.util.DeliveryUtil;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 * 
 * 配送单
 * 
 */
@Controller
@RequestMapping("/dorder")
public class DeliveryOrderController {
	
	@Resource(name = "deliveryCartService")
	DeliveryCartService deliveryCartService;
	
	@Resource(name = "deliveryOrderService")
	DeliveryOrderService deliveryOrderService;
	
	@Resource(name = "deliveryOperateLogService")
	DeliveryOperateLogService deliveryOperateLogService;
	
	@RequestMapping(value = {"state/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model stateList(Model model,@RequestParam(required = true) Integer dOrderId) {
		List<DeliveryOrderUpdateLog> dOrderLogList = this.deliveryOperateLogService.qryDOrderUpdateLog(dOrderId);
		model.addAttribute("dOrderLogList", dOrderLogList);   
        return model;
    }

	@RequestMapping(value = {"detail"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model detail(Model model,@RequestParam(required = true) Integer dOrderId) {
		DeliveryOrder dOrder = this.deliveryOrderService.findDOrder(dOrderId);
		DeliveryCart dCart = this.deliveryCartService.findDCart(dOrder.getDeliveryCartId());
		List<DeliveryOrderGoods> dOrderGoodsList = this.deliveryOrderService.qryDOrderGoodsList(dOrderId);
		List<DeliveryOrderUpdateLog> dOrderLogList = this.deliveryOperateLogService.qryDOrderUpdateLog(dOrderId);
		BigDecimal money = DeliveryUtil.calculateMoney(dOrder);
		model.addAttribute("money", money);
		model.addAttribute("dCart", dCart);   
		model.addAttribute("dOrder", dOrder);   
		model.addAttribute("goodsList", dOrderGoodsList);
		model.addAttribute("dOrderLogList", dOrderLogList); 
		return model;
    }
	
}
