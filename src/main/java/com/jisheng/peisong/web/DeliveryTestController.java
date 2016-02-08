/**
 * 
 */
package com.jisheng.peisong.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.order.model.PrimaryOrder;
import com.jisheng.order.model.RoLine;
import com.jisheng.order.service.POService;
import com.jisheng.order.service.ROService;
import com.jisheng.peisong.dao.DeliveryCartFinishQueueMapper;
import com.jisheng.peisong.dao.DeliveryOrderMapper;
import com.jisheng.peisong.dao.DeliveryOrderReturnQueueMapper;
import com.jisheng.peisong.dao.DeliveryOrderStateQueueMapper;
import com.jisheng.peisong.entity.DeliveryCart;
import com.jisheng.peisong.entity.DeliveryCartFinishQueue;
import com.jisheng.peisong.entity.DeliveryOrder;
import com.jisheng.peisong.entity.DeliveryOrderGoods;
import com.jisheng.peisong.entity.DeliveryOrderReturnQueue;
import com.jisheng.peisong.entity.DeliveryOrderStateQueue;
import com.jisheng.peisong.entity.enums.DeliveryState;
import com.jisheng.peisong.entity.enums.DeliveryTimeSlot;
import com.jisheng.peisong.entity.enums.OrderSource;
import com.jisheng.peisong.entity.enums.OrderType;
import com.jisheng.peisong.entity.enums.SyncState;
import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.service.DeliveryCartService;
import com.jisheng.peisong.service.DeliveryOperateLogService;
import com.jisheng.peisong.service.DeliveryOrderService;
import com.jisheng.peisong.service.DeliveryQueueService;
import com.jisheng.peisong.service.DeliveryService;
import com.jisheng.peisong.service.WeChatMsgQueueService;
import com.jisheng.peisong.util.Constant;
import com.jisheng.peisong.util.DeliveryUtil;
import com.jisheng.peisong.util.StringUtil;
import com.jisheng.peisong.vo.PeopleProfile;
import com.jisheng.util.constant.ReturnOrderType;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Controller
@RequestMapping("/test")
public class DeliveryTestController {
	
	private static Logger log = LogManager.getLogger(DeliveryTestController.class);

	@Resource(name = "deliveryQueueService")
	DeliveryQueueService deliveryQueueService;
	
	@Resource(name = "deliveryCartService")
	DeliveryCartService deliveryCartService;
	
	@Resource(name = "deliveryOrderService")
	DeliveryOrderService deliveryOrderService;

	@Resource(name = "roService_dubbo")
	ROService roService;
	
	@Resource(name = "poService_dubbo")
	POService poService;
	
	@Resource(name = "deliveryService_dubbo")
	DeliveryService deliveryService;
	
	@Resource(name = "deliveryOrderStateQueueDao")
	DeliveryOrderStateQueueMapper deliveryOrderStateQueueDao;
	
	@Resource(name = "deliveryOrderStateQueueDaoRead")
	DeliveryOrderStateQueueMapper deliveryOrderStateQueueDaoRead;
	
	@Resource(name = "deliveryOrderReturnQueueDao")
	DeliveryOrderReturnQueueMapper deliveryOrderReturnQueueDao;
	
	@Resource(name = "deliveryOrderReturnQueueDaoRead")
	DeliveryOrderReturnQueueMapper deliveryOrderReturnQueueDaoRead;
	
	@Resource(name = "deliveryCartFinishQueueDao")
	DeliveryCartFinishQueueMapper deliveryCartFinishQueueDao;
	
	@Resource(name = "deliveryCartFinishQueueDaoRead")
	DeliveryCartFinishQueueMapper deliveryCartFinishQueueDaoRead;
	
	@Resource(name = "deliveryOperateLogService")
	DeliveryOperateLogService deliveryOperateLogService;
	
	@Resource(name = "weChatMsgQueueService")
	WeChatMsgQueueService weChatMsgQueueService;
	
	@Resource(name = "deliveryOrderDaoRead")
	DeliveryOrderMapper deliveryOrderDaoRead;
	
	@RequestMapping(value = {"dorder/{orderNo}"}, method = { RequestMethod.GET})
	@ResponseBody
    public Model dorder(Model model,@PathVariable String orderNo){
		DeliveryOrder dOrder = deliveryOrderService.findDOrderNo(orderNo);
		if(dOrder == null){
			model.addAttribute(Constant.TransmitField.Msg, "未接入");
			return model;
		}
		model.addAttribute("dOrder", dOrder);
		List<DeliveryOrderGoods> dOrderGoodsList = deliveryOrderService.qryDOrderGoodsList(dOrder.getId());
		model.addAttribute("dOrderGoodsList",dOrderGoodsList);
		if(dOrder.getDeliveryCartId() == null){
			model.addAttribute(Constant.TransmitField.Msg, "未分派车单");
			return model;
		}
		DeliveryCart dCart = deliveryCartService.findDCart(dOrder.getDeliveryCartId());
		List<DeliveryOrderStateQueue> dOrderStateQs = deliveryQueueService.qryDOrderStateQueue(dOrder.getId());
		DeliveryOrderReturnQueue dOrderReturnQ = deliveryQueueService.findDOrderReturnQueue(dOrder.getId());
		DeliveryCartFinishQueue dCartFinishQ = deliveryQueueService.findDCartFinishQueue(dOrder.getDeliveryCartId());
		model.addAttribute("dCart", dCart);
		model.addAttribute("dOrderStateQs", dOrderStateQs);
		model.addAttribute("dOrderReturnQ", dOrderReturnQ);
		model.addAttribute("dCartFinishQ", dCartFinishQ);
        return model;
    }
	
	@RequestMapping(value = {"dcart/{id}"}, method = { RequestMethod.GET})
	@ResponseBody
    public Model dcart(Model model,@PathVariable Integer id){
		DeliveryCart dCart = deliveryCartService.findDCart(id);
		if(dCart == null){
			model.addAttribute(Constant.TransmitField.Msg, "派车单不存在");
			return model;
		}
		List<DeliveryOrder> dOrderList = deliveryOrderService.qryDOrderList(id, true);
		DeliveryCartFinishQueue dCartFinishQ = deliveryQueueService.findDCartFinishQueue(id);
		model.addAttribute("dCart", dCart);
		model.addAttribute("dOrderList", dOrderList);
		model.addAttribute("dCartFinishQ", dCartFinishQ);
        return model;
    }
	
	@RequestMapping(value = {"queue/state/{id}"}, method = { RequestMethod.GET})
	@ResponseBody
    public Model queueState(Model model,@PathVariable Integer id,@RequestParam(required = false) Boolean now){
		DeliveryOrderStateQueue dOrderStateQ = deliveryOrderStateQueueDaoRead.selectByPrimaryKey(id);
		if(dOrderStateQ == null){
			model.addAttribute(Constant.TransmitField.Msg, "队列不存在");
			return model;
		}
		model.addAttribute("dOrderStateQ", dOrderStateQ);
		if(now == null||!now){
			deliveryQueueService.updateDOrderStateQueueSynced(id, SyncState.WaitSync, null);
			model.addAttribute("now", "更改成功，延迟执行同步");
			return model;
		}
		model.addAttribute("now", "立即执行同步");
		int state = 0;
		DeliveryState dState = dOrderStateQ.getDeliveryState();
		if(dState.equals(DeliveryState.Pickuped)){
			state = 1;
		}else if(dState.equals(DeliveryState.Delivered)){
			if(!deliveryQueueService.isSyncedDOrderReturn(dOrderStateQ.getDeliveryOrderId())){
				model.addAttribute(Constant.TransmitField.Msg, "退货单队列未完成同步，不能同步签收状态");
				return model;
			}
			state = 2;
		}
		if(state == 0){
			model.addAttribute(Constant.TransmitField.Msg, "状态未知");
			return model;
		}
		try{
			poService.updateDOStatus(dOrderStateQ.getDeliveryOrderId(),state+"");
			deliveryQueueService.updateDOrderStateQueueSynced(dOrderStateQ.getId(), SyncState.Synced, null);
			model.addAttribute(Constant.TransmitField.Msg, "同步成功");
		}catch(Exception e){
			deliveryQueueService.updateDOrderStateQueueSynced(dOrderStateQ.getId(), SyncState.SyncFailure, StringUtil.textSafeString(StringUtil.toString(e)));
			model.addAttribute(Constant.TransmitField.Msg, "同步失败"+e.getMessage());
		}
        return model;
    }
	
	@RequestMapping(value = {"queue/return/{id}"}, method = { RequestMethod.GET})
	@ResponseBody
    public Model queueReturn(Model model,@PathVariable Integer id,@RequestParam(required = false) Boolean now){
		DeliveryOrderReturnQueue dOrderReturnQ = deliveryOrderReturnQueueDaoRead.selectByPrimaryKey(id);
		if(dOrderReturnQ == null){
			model.addAttribute(Constant.TransmitField.Msg, "队列不存在");
			return model;
		}
		model.addAttribute("dOrderReturnQ", dOrderReturnQ);
		deliveryQueueService.updateSynced4DOrderReturn(id, SyncState.WaitSync, null);
		if(now == null||!now){
			model.addAttribute("now", "更改成功，延迟执行同步");
			return model;
		}
		model.addAttribute("now", "立即执行同步");
		if(!deliveryQueueService.txToggleSyncState4DOrderReturn(dOrderReturnQ.getId(),SyncState.WaitSync,SyncState.Syncing)){
			model.addAttribute(Constant.TransmitField.Msg, "退货单生成并发，跳过本次执行，dOrderId："+dOrderReturnQ.getDeliveryOrderId());
			return model;
		}
		StringBuilder resMsg = new StringBuilder("[");
		DeliveryOrder dOrder = deliveryOrderService.findDOrder(dOrderReturnQ.getDeliveryOrderId());
		DeliveryCart dCart = deliveryCartService.findDCart(dOrder.getDeliveryCartId());
		Integer driverId = dCart.getDriverId();
		List<DeliveryOrderGoods> dOrderGoodsList = deliveryOrderService.qryDOrderGoodsList(dOrderReturnQ.getDeliveryOrderId());
		PrimaryOrder porder = null;
		try{
			porder = poService.getPOById(dOrder.getOrderId());
			if(porder == null){
				deliveryQueueService.updateSynced4DOrderReturn(dOrderReturnQ.getId(), SyncState.SyncFailure, "无法获取订单系统PO："+dOrder.getOrderId());
				model.addAttribute(Constant.TransmitField.Msg, "无法获取订单系统PO："+dOrder.getOrderId());
				return model;
			}
		}catch(Exception e){
			deliveryQueueService.txToggleSyncState4DOrderReturn(dOrderReturnQ.getId(),SyncState.Syncing,SyncState.WaitSync);
			model.addAttribute(Constant.TransmitField.Msg, "调用订单系统查询po异常，orderId："+dOrder.getOrderId());
			return model;
		}
		resMsg.append(porder.getId()+","+dOrder.getId()+","+driverId+",roline:[");
		List<RoLine> rolist = new ArrayList<RoLine>();
		for(DeliveryOrderGoods dOrderGoods:dOrderGoodsList){
			BigDecimal backNum = dOrderGoods.getNum().subtract(dOrderGoods.getSignNum());
			if(backNum.compareTo(BigDecimal.ZERO) == 0){
				continue;
			}
			RoLine roLine = new RoLine();
			roLine.setGsId(dOrderGoods.getGoodsSnapId());
			roLine.setCreateTime(dOrderReturnQ.getOperateTime());
			roLine.setCreateBy(driverId);
			roLine.setGoodsNo(dOrderGoods.getGoodsSn());
			roLine.setStockInRequired("1");
			roLine.setUnitPrice(dOrderGoods.getPrice());
			roLine.setReturnQuantity(backNum);
			roLine.setReturnAmount(backNum.multiply(dOrderGoods.getPrice()));
			rolist.add(roLine);
			resMsg.append(dOrderGoods.getGoodsSnapId()+","+dOrderReturnQ.getOperateTime()+"|");
		}
		resMsg.append("]");
		model.addAttribute("porder", porder);
		model.addAttribute("rolist", rolist);
		int roId = 0;
		try{
			roId = roService.createROByDOrder(porder,dOrder.getId(), rolist, ReturnOrderType.ONSITERETURN, "1");
		}catch(Exception e){
			deliveryQueueService.txToggleSyncState4DOrderReturn(dOrderReturnQ.getId(),SyncState.Syncing,SyncState.WaitSync);
			model.addAttribute(Constant.TransmitField.Msg, "同步失败，调用订单系统生成退货单异常，dOrderId："+dOrder.getId());
			return model;
		}
		if(roId > 0){
			deliveryOrderService.updateReturnOrderNo(dOrder.getId(), "R"+roId);
			deliveryQueueService.updateSynced4DOrderReturn(dOrderReturnQ.getId(), SyncState.Synced,"请求："+resMsg);
			model.addAttribute(Constant.TransmitField.Msg, "同步成功，请求："+resMsg+"响应："+roId);
		}else if(roId == -1){
			deliveryQueueService.updateSynced4DOrderReturn(dOrderReturnQ.getId(), SyncState.SyncFailure,"请求："+resMsg+"响应：-1，订单不存在");
			model.addAttribute(Constant.TransmitField.Msg, "同步失败，请求："+resMsg+"响应：-1，订单不存在");
		}
		return model;
    }
	
	@RequestMapping(value = {"queue/finish/{id}"}, method = { RequestMethod.GET})
	@ResponseBody
    public Model queueFinish(Model model,@PathVariable Integer id,@RequestParam(required = false) Boolean now){
		DeliveryCartFinishQueue dCartFinishQ = deliveryCartFinishQueueDaoRead.selectByPrimaryKey(id);
		if(dCartFinishQ == null){
			model.addAttribute(Constant.TransmitField.Msg, "队列不存在");
			return model;
		}
		model.addAttribute("dCartFinishQ", dCartFinishQ);
		if(now == null||!now){
			deliveryQueueService.updateSynced4DCartFinish(id, SyncState.WaitSync, null);
			model.addAttribute("now", "更改成功，延迟执行同步");
			return model;
		}
		model.addAttribute("now", "立即执行同步");
		Integer dCartId = dCartFinishQ.getDeliveryCartId();
		DeliveryCart dCart = deliveryCartService.findDCart(dCartId);
		List<DeliveryOrder> dOrders = deliveryOrderService.qryDOrderList(dCartId, false);
		boolean isFinish = true;
		for(DeliveryOrder dOrder:dOrders){
			if(dOrder.getIsBackNotify() != null&&!dOrder.getIsBackNotify()){
				isFinish = false;
				break;
			}
		}
		if(!isFinish){
			model.addAttribute(Constant.TransmitField.Msg, "同步失败：存在未入库通知的配送单");
			return model;
		}
		Date time = new Date();
		try {
			deliveryCartService.txFinishDCart(dCartId,dCart.getDriverId(), time);
			deliveryOperateLogService.noteFinishDCart(time, dCartId, dOrders);
			deliveryQueueService.updateSynced4DCartFinish(dCartFinishQ.getId(), SyncState.Synced, null);
			model.addAttribute(Constant.TransmitField.Msg, "同步成功");
		} catch (BusinessException e) {
			deliveryQueueService.updateSynced4DCartFinish(dCartFinishQ.getId(), SyncState.SyncFailure, e.getMessage());
			model.addAttribute(Constant.TransmitField.Msg, "同步失败："+e.getMessage());
		}
        return model;
    }
	
	@RequestMapping(value = {"notify/return"}, method = { RequestMethod.GET})
	@ResponseBody
    public Model notifyReturn(Model model,@RequestParam(required = true) Integer dOrderId) throws BusinessException{
		DeliveryOrder dOrder = deliveryOrderService.findDOrder(dOrderId);
		if(dOrder == null){
			throw new BusinessException("模拟退货入库通知失败：配送单号不存在");
		}else if(dOrder.getIsBackNotify() == null){
			throw new BusinessException("模拟退货入库通知失败：配送单无需退货入库通知");
		}else if(dOrder.getIsBackNotify()){
			throw new BusinessException("模拟退货入库通知失败：配送单已退货入库通知");
		}
		deliveryService.notifyReturnStorage(dOrderId);
        return model;
    }
	
	@RequestMapping(value = {"find/po"}, method = { RequestMethod.GET})
	@ResponseBody
    public Model findPo(Model model,@RequestParam(required = true) Integer orderId){
		PrimaryOrder porder = this.poService.getPOById(orderId);
		model.addAttribute("po", porder);
        return model;
    }
	
	@RequestMapping(value = {"allocate"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model allocate(
    		Model model,
    		@RequestParam(required = true) Integer storageId,
    		@RequestParam(required = false) Integer cityId,
    		@RequestParam(required = false) String districtId,
    		@RequestParam(required = false) String address,
    		@RequestParam(required = false) String reqDeliveryDayStart,
    		@RequestParam(required = false) String reqDeliveryDayEnd,
    		@RequestParam(required = false) String timeSlot,
    		@RequestParam(required = false) String orderSourceText,
    		@RequestParam(required = false) String orderTypeText) throws BusinessException {
		if(!StringUtil.isNullOrEmpty(reqDeliveryDayStart)&&!StringUtil.isNullOrEmpty(reqDeliveryDayEnd)
				&&reqDeliveryDayStart.compareTo(reqDeliveryDayEnd) > 0){
			throw new BusinessException("起止时间应小于终止时间");
		}
		OrderSource orderSource = null;
		if(!StringUtil.isNullOrEmpty(orderSourceText)){
			orderSource = OrderSource.valueOf(orderSourceText);
		}
		OrderType orderType = null;
		if(!StringUtil.isNullOrEmpty(orderTypeText)){
			orderType = OrderType.valueOf(orderTypeText);
		}
		if(!StringUtil.isNullOrEmpty(timeSlot)){
			timeSlot = DeliveryTimeSlot.valueOf(timeSlot).desc;
		}
		List<DeliveryOrder> dOrderList = this.deliveryOrderDaoRead.qryWaitAllocateCart(storageId, cityId, districtId, address,reqDeliveryDayStart,reqDeliveryDayEnd, timeSlot, orderSource, orderType);
		if(dOrderList == null||dOrderList.size() == 0){
			return model;
		}
		List<List<DeliveryOrder>> dCartList = DeliveryUtil.buildDCarts4All(dOrderList);
		DeliveryUtil.dCart4Sort(dCartList);
		model.addAttribute("dCartList", dCartList);   
        return model;
    }
	
	@RequestMapping(value = {"allocate/export"}, method = { RequestMethod.GET,RequestMethod.POST})
    public void allocateExport(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		HttpServletResponse response,
    		@RequestParam(required = true) Integer storageId,
    		@RequestParam(required = false) Integer cityId,
    		@RequestParam(required = false) String districtId,
    		@RequestParam(required = false) String address,
    		@RequestParam(required = false) String reqDeliveryDayStart,
    		@RequestParam(required = false) String reqDeliveryDayEnd,
    		@RequestParam(required = false) String timeSlot,
    		@RequestParam(required = false) String orderSourceText,
    		@RequestParam(required = false) String orderTypeText) throws IOException, BusinessException {
		if(!StringUtil.isNullOrEmpty(reqDeliveryDayStart)&&!StringUtil.isNullOrEmpty(reqDeliveryDayEnd)
				&&reqDeliveryDayStart.compareTo(reqDeliveryDayEnd) > 0){
			throw new BusinessException("起止时间应小于终止时间");
		}
		OrderSource orderSource = null;
		if(!StringUtil.isNullOrEmpty(orderSourceText)){
			orderSource = OrderSource.valueOf(orderSourceText);
		}
		OrderType orderType = null;
		if(!StringUtil.isNullOrEmpty(orderTypeText)){
			orderType = OrderType.valueOf(orderTypeText);
		}
		if(!StringUtil.isNullOrEmpty(timeSlot)){
			timeSlot = DeliveryTimeSlot.valueOf(timeSlot).desc;
		}
		List<DeliveryOrder> dOrderList = this.deliveryOrderDaoRead.qryWaitAllocateCart(storageId, cityId, districtId, address,reqDeliveryDayStart,reqDeliveryDayEnd, timeSlot, orderSource, orderType);
		if(dOrderList == null||dOrderList.size() == 0){
			return ;
		}
		for(DeliveryOrder dOrder:dOrderList){
			DeliveryOrder dOrder2New = DeliveryUtil.getDOrder2New(dOrder);
			if(dOrder2New != null){
				dOrder.setLng(dOrder2New.getLng());
				dOrder.setLat(dOrder2New.getLat());
				dOrder.setDegrees(dOrder2New.getDegrees());
				dOrder.setDistance(dOrder2New.getDistance());
			}else{
				dOrder.setLng(BigDecimal.ZERO);
				dOrder.setLat(BigDecimal.ZERO);
				dOrder.setDegrees(BigDecimal.ZERO);
				dOrder.setDistance(BigDecimal.ZERO);
			}
		}
		Collections.sort(dOrderList, new Comparator<DeliveryOrder>() {
            public int compare(DeliveryOrder do1, DeliveryOrder do2) {
                return do1.getDegrees().compareTo(do2.getDegrees())>=0?1:-1;
            }
        });
		String filename = "dcart_order_list.xls";
    	Workbook wb = new HSSFWorkbook();
        Sheet s = wb.createSheet();
        s.setColumnWidth(0, 5200);
        s.setColumnWidth(1, 5200);
        s.setColumnWidth(2, 5200);
        s.setColumnWidth(3, 5200);
        Row titleRow = s.createRow(0);
        titleRow.createCell(0).setCellValue("角度");
        titleRow.createCell(1).setCellValue("距离");
        titleRow.createCell(2).setCellValue("订单");
        titleRow.createCell(3).setCellValue("地址");
        for (int i = 0; i < dOrderList.size(); i++) {
        	DeliveryOrder dOrder = dOrderList.get(i);
            Row dataRow = s.createRow(i + 1);
            dataRow.createCell(0).setCellValue(dOrder.getDegrees()+"");
            dataRow.createCell(1).setCellValue(dOrder.getDistance()+"");
            dataRow.createCell(2).setCellValue(dOrder.getOrderNo()+"");
            dataRow.createCell(3).setCellValue(dOrder.getReceiverAddress()+"");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wb.write(bos);
        bos.flush();
        byte[] bs = bos.toByteArray();
		OutputStream os = response.getOutputStream();
		try {
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename="+filename);
			response.setContentType("application/octet-stream; charset=utf-8");
			os.write(bs);
			os.flush();
		} finally {
			if (os != null) {
				os.close();
			}
		}
    }
	
}
