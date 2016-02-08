/**
 * 
 */
package com.jisheng.peisong.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.entity.CollectionDetail;
import com.jisheng.peisong.entity.DeliveryCart;
import com.jisheng.peisong.entity.DeliveryCartUpdateLog;
import com.jisheng.peisong.entity.DeliveryOrder;
import com.jisheng.peisong.entity.enums.DCartReceivables;
import com.jisheng.peisong.entity.enums.DCartState;
import com.jisheng.peisong.entity.enums.DeliveryTimeSlot;
import com.jisheng.peisong.entity.enums.OrderSource;
import com.jisheng.peisong.entity.enums.OrderType;
import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.service.CollectionDetailService;
import com.jisheng.peisong.service.DeliveryCartService;
import com.jisheng.peisong.service.DeliveryDriverService;
import com.jisheng.peisong.service.DeliveryOperateLogService;
import com.jisheng.peisong.service.DeliveryOrderService;
import com.jisheng.peisong.util.Constant;
import com.jisheng.peisong.util.DateUtil;
import com.jisheng.peisong.util.DeliveryUtil;
import com.jisheng.peisong.util.Pager;
import com.jisheng.peisong.util.StringUtil;
import com.jisheng.peisong.vo.CollectionDetailView;
import com.jisheng.peisong.vo.DCartReceivablesView;
import com.jisheng.peisong.vo.DCartView;
import com.jisheng.peisong.vo.DCartViewCountInfo;
import com.jisheng.peisong.vo.DOrderGroup;
import com.jisheng.peisong.vo.PeopleProfile;
import com.jisheng.peisong.web.service.AsyncService;
import com.jisheng.peisong.web.session.SessionCheckInject;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 * 
 * 派车单
 * 
 */
@Controller
@RequestMapping("/dcart")
public class DeliveryCartController {

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
	
	@SessionCheckInject
	@RequestMapping(value = {"allocate"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model allocate(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
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
		List<DOrderGroup> doGrouplist = deliveryOrderService.allocateDeliveryOrder(
				storageId, cityId, districtId, address,reqDeliveryDayStart,reqDeliveryDayEnd, timeSlot, orderSource, orderType);
		model.addAttribute("doGrouplist", doGrouplist);   
        return model;
    }
	
	@SessionCheckInject
	@RequestMapping(value = {"create"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model create(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		@RequestParam(value = "carts[]") String[] carts) {
		Date curTime = new Date();
		int i = 0;
		for(String cart:carts){
			String[] dOrderIds = cart.split(",");
			try{
				Integer.valueOf(dOrderIds[0]);
			}catch(Exception e){
				model.addAttribute(Constant.TransmitField.Status, 0); 
				model.addAttribute(Constant.TransmitField.Msg,"派车单生成异常："+StringUtil.toJson(carts)); 	
				return model;
			}
			DeliveryCart dCart = this.deliveryCartService.prepareDCart(Integer.valueOf(dOrderIds[0]),curTime);
			try {
				Integer dCartId = this.deliveryCartService.txCreateDCart(dCart, dOrderIds);
				this.deliveryOperateLogService.noteCreateDCart(profile.getPeopleName(), curTime, dCartId, dOrderIds);
			} catch (BusinessException e) {
				i++;
			}
		}
		if(i > 0){
			model.addAttribute(Constant.TransmitField.Status, 0); 
			model.addAttribute(Constant.TransmitField.Msg,"有"+i+"个派车单生成失败"); 	
		}
        return model;
    }
	
	@SessionCheckInject
	@RequestMapping(value = {"dorder/cancel"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model cancel(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,Model model, @RequestParam(value = "dorderId",required = true) Integer dorderId,@RequestParam(value = "dcartId",required = true) Integer dcartId) throws BusinessException {
		
		List<Integer> orderIds = deliveryOrderService.qryDOrderIds(dcartId);
		boolean success = false;
		if(orderIds.size() > 1 && orderIds.contains(dorderId)){
			success = deliveryOrderService.txCancelDeliveryOrder(profile, dorderId, dcartId,true);
		}else if(orderIds.size() == 1 && orderIds.contains(dorderId) ){
			success = deliveryOrderService.txCancelDeliveryOrder(profile, dorderId, dcartId,false);
		}else{
			model.addAttribute("s", 0);	
	    	model.addAttribute("m", "数据错误,无配送单可取消,请重试!");	
	    	return model;
		}
		
		if(success){
        	model.addAttribute("s", 1);	
        	model.addAttribute("m", "配送取消成功!");	
    	}else{
	    	model.addAttribute("s", 0);	
	    	model.addAttribute("m", "配送取消失败,请重试!");	
    	}
		return model;
	}
	@SessionCheckInject
	@RequestMapping(value = {"delete"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model delete(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		@RequestParam(required = true) Integer dCartId) throws BusinessException {
		Date curTime = new Date();
		List<Integer> dOrderIds = this.deliveryOrderService.qryDOrderIds(dCartId);
		this.deliveryCartService.txReleaseDCart(dCartId);
		this.deliveryOperateLogService.noteDeleteDOrder(profile.getPeopleName(), curTime, dCartId,dOrderIds);
        return model;
    }
	
	@SessionCheckInject
	@RequestMapping(value = {"unpickup/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model unpickupList(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
//    		@RequestParam(required = true) Integer pi,
//            @RequestParam(required = true) Integer ps,
    		@RequestParam(required = false) String createDay,
    		@RequestParam(required = false) String dCartStateText,
    		@RequestParam(required = false) String createDateStratText,
    		@RequestParam(required = false) String createDateEndText,
    		@RequestParam(required = false) Integer dCartId,
    		@RequestParam(required = false) String orderNo,
    		@RequestParam(required = false) Integer shipperId,
    		@RequestParam(required = false) String driverName,
    		@RequestParam(required = false) String driverPhone,
    		@RequestParam(required = false) Integer storageId,
    		@RequestParam(required = false) Integer cityId,
    		@RequestParam(required = false) Integer districtId,
    		@RequestParam(required = false) Integer streetId) throws BusinessException {
		if(!StringUtil.isNullOrEmpty(createDateStratText)&&!StringUtil.isNullOrEmpty(createDateEndText)
				&&createDateStratText.compareTo(createDateEndText) > 0){
			throw new BusinessException("起止时间应小于终止时间");
		}
		if(StringUtil.isNullOrEmpty(orderNo)){
			orderNo = null;
		}
		DCartState dCartState = null;
		if(!StringUtil.isNullOrEmpty(dCartStateText)){
			dCartState = DCartState.valueOf(dCartStateText);
		}
		DCartState[] dCartStates = null;
		if(dCartState == null){
			dCartStates = new DCartState[]{DCartState.Init,DCartState.AssignedDriver};
		}
		Date createDateStrat = null;
		if(!StringUtil.isNullOrEmpty(createDateStratText)){
			createDateStrat = DateUtil.toParse(createDateStratText+" 00:00:00");
		}
		Date createDateEnd = null;
		if(!StringUtil.isNullOrEmpty(createDateEndText)){
			createDateEnd = DateUtil.toParse(createDateEndText+" 23:59:59");
		}
		Integer[] storageIds = profile.getStorageIds(storageId);
//		Integer start = pi*ps;
//		Integer size = ps;
		List<DCartView> dCartList = this.deliveryCartService.qryList4View(storageId, storageIds, 
				cityId, districtId, streetId, createDay, createDateStrat, createDateEnd, dCartState, dCartStates, dCartId,
				orderNo, shipperId, driverName,driverPhone, null,null, 0, 10000);
//		int dCartCount = this.deliveryCartService.qryList4ViewCount(storageId, null, 
//				districtId, streetId, createDay, null, null, dCartState, dCartStates, dCartId, 
//		orderNo, null, driverName, null);
		model.addAttribute("dCartList", dCartList);   
//		model.addAttribute("dCartCount", dCartCount);   
        return model;
    }
	
	@RequestMapping(value = {"detail"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model detail(Model model,@RequestParam(required = true) Integer dCartId) {
		
		DeliveryCart dCart = this.deliveryCartService.getDCart(dCartId);
		
		if(dCart == null){
			model.addAttribute("s", 0);   
			model.addAttribute("t", 6);  
			model.addAttribute("m","派车单不存在,请重试!");
			
			return model;
		}
		
		List<DeliveryCartUpdateLog> dCartLogList = this.deliveryOperateLogService.qryDCartUpdateLog(dCartId);
		List<DeliveryOrder> dOrderList = this.deliveryOrderService.qryDOrderList(dCartId,true);
		int orderCount = 0;
		Set<Integer> buyers = new HashSet<Integer>();
		BigDecimal orderMoney = BigDecimal.ZERO;
		BigDecimal orderPayMoney = BigDecimal.ZERO;
		for(DeliveryOrder dOrder:dOrderList){
			orderCount++;
			buyers.add(dOrder.getBuyerId());
			orderMoney = orderMoney.add(dOrder.getOrderGoodsMoney());
			BigDecimal money = DeliveryUtil.calculateMoney(dOrder);
			dOrder.setMoney(money);
//			orderPayMoney = orderPayMoney.add(money);
		}
		
		orderPayMoney = 	collectionDetailService.countReceivablesByDCartId(dCartId);
        if(null == orderPayMoney)orderPayMoney = BigDecimal.ZERO;
		model.addAttribute("dCart", dCart);   
		model.addAttribute("dCartLogList", dCartLogList);  
		model.addAttribute("dOrderList", dOrderList);
		model.addAttribute("orderCount", orderCount);
		model.addAttribute("buyerCount", buyers.size());
		model.addAttribute("orderMoney", orderMoney);
		model.addAttribute("orderPayMoney", orderPayMoney);
		model.addAttribute("orderPayedMoney", dCart.isReceivables()?orderPayMoney:0);
        return model;
    }
	
	@RequestMapping(value = {"state/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model stateList(Model model,@RequestParam(required = true) Integer dCartId) {
		List<DeliveryCartUpdateLog> dCartLogList = this.deliveryOperateLogService.qryDCartUpdateLog(dCartId);
		model.addAttribute("dCartLogList", dCartLogList);   
        return model;
    }
	
	@SessionCheckInject
	@RequestMapping(value = {"receivables/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model receivablesList(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		@RequestParam(required = false) Integer pageNo,//显示页
            @RequestParam(required = false) String createDay,
    		@RequestParam(required = false) String dCartStateText,
    		@RequestParam(required = false) String createDateStratText,
    		@RequestParam(required = false) String createDateEndText,
    		@RequestParam(required = false) Integer dCartId,
    		@RequestParam(required = false) String orderNo,
    		@RequestParam(required = false) Integer shipperId,
    		@RequestParam(required = false) String driverName,
    		@RequestParam(required = false) String driverPhone,
    		@RequestParam(required = false) Integer storageId,
    		@RequestParam(required = false) Integer cityId,
    		@RequestParam(required = false) Integer districtId,
    		@RequestParam(required = false) Integer streetId,
    		@RequestParam(required = false) String dCartReceivablesText) throws BusinessException {
		if(!StringUtil.isNullOrEmpty(createDateStratText)&&!StringUtil.isNullOrEmpty(createDateEndText)
				&&createDateStratText.compareTo(createDateEndText) > 0){
			throw new BusinessException("起止时间应小于终止时间");
		}
		if(StringUtil.isNullOrEmpty(orderNo)){
			orderNo = null;
		}
		DCartState dCartState = null;
		if(!StringUtil.isNullOrEmpty(dCartStateText)){
			dCartState = DCartState.valueOf(dCartStateText);
		}
		DCartReceivables dCartReceivables = null;
		if(!StringUtil.isNullOrEmpty(dCartReceivablesText)){
			dCartReceivables = DCartReceivables.valueOf(dCartReceivablesText);
		}
		Date createDateStrat = null;
		if(!StringUtil.isNullOrEmpty(createDateStratText)){
			createDateStrat = DateUtil.toParse(createDateStratText+" 00:00:00");
		}
		Date createDateEnd = null;
		if(!StringUtil.isNullOrEmpty(createDateEndText)){
			createDateEnd = DateUtil.toParse(createDateEndText+" 23:59:59");
		}
		
		Pager pager = new Pager();
		if(pageNo == null) pageNo = 1;
		
		Integer[] storageIds = profile.getStorageIds(storageId);
	
		DCartViewCountInfo dCartCountInfo = this.deliveryCartService.qryList4ViewCountInfo(storageId, 
				storageIds, cityId, districtId, streetId, createDay, createDateStrat, createDateEnd, dCartState, 
				null, dCartId, orderNo, shipperId, driverName,driverPhone, null,dCartReceivables);
		
		pager.setCount(dCartCountInfo.getCount());
		pager.setPageNo(pageNo);
		
		List<DCartView> dCartList = this.deliveryCartService.qryList4View(storageId, 
				storageIds, cityId, districtId, streetId, createDay, createDateStrat, createDateEnd, dCartState, 
				null, dCartId, orderNo, shipperId, driverName,driverPhone, null,dCartReceivables, pager.getStartIndex(), pager.getPageSize());
		BigDecimal sumAmount = BigDecimal.ZERO;
		if(null != dCartList){
			for(DCartView dcartView : dCartList){
				if(null != dcartView.getCollectionAmount()){
				    sumAmount = sumAmount.add(dcartView.getCollectionAmount());
				}
			}
		}
		dCartCountInfo.setReceivablesMoney(sumAmount);
		
		model.addAttribute("dCartList", dCartList);
		model.addAttribute("dCartCountInfo", dCartCountInfo);
		model.addAttribute("pageIndex", pager.getPageIndexs());
		model.addAttribute("pageNo", pager.getPageNo());
		model.addAttribute("pageMaxNo", pager.getMaxPageNo());
		return model;
    }
	

	@SessionCheckInject
	@RequestMapping(value = {"receivables/export"}, method = { RequestMethod.GET,RequestMethod.POST})
    public void export(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile, 
    		HttpServletResponse response,
    		@RequestParam(required = false) String dCartStateText,//派车单状态
    		@RequestParam(required = false) String createDateStratText,//创建日期开始
    		@RequestParam(required = false) String createDateEndText,//创建日期结束
    		@RequestParam(required = false) Integer dCartId,//派车单编号
    		@RequestParam(required = false) String orderNo,//订单编号
    		@RequestParam(required = false) Integer shipperId,//司机单位
    		@RequestParam(required = false) String driverName,
    		@RequestParam(required = false) Integer storageId,//提货仓库ID
    		@RequestParam(required = false) Integer cityId,
    		@RequestParam(required = false) Integer districtId,//区
    		@RequestParam(required = false) Integer streetId,
    		@RequestParam(required = false) String dCartReceivablesText) throws IOException{//街道 
   
		if(StringUtil.isNullOrEmpty(orderNo)){
			orderNo = null;
		}
		DCartState dCartState = null;
		if(!StringUtil.isNullOrEmpty(dCartStateText)){
			dCartState = DCartState.valueOf(dCartStateText);
		}
		Date createDateStrat = null;
		if(!StringUtil.isNullOrEmpty(createDateStratText)){
			createDateStrat = DateUtil.toParse(createDateStratText+" 00:00:00");
		}
		Date createDateEnd = null;
		if(!StringUtil.isNullOrEmpty(createDateEndText)){
			createDateEnd = DateUtil.toParse(createDateEndText+" 23:59:59");
		}
		
		DCartReceivables dCartReceivables = null;
		if(!StringUtil.isNullOrEmpty(dCartReceivablesText)){
			dCartReceivables = DCartReceivables.valueOf(dCartReceivablesText);
		}
		
		List<Map<String, Object>> dCartList = this.deliveryCartService.qryList4Export(storageId, 
				cityId, districtId, streetId, createDateStrat, createDateEnd, dCartState, 
				dCartId, orderNo, shipperId, driverName, dCartReceivables);
		
		String filename = "dcart_order_sk_list.csv";
    	Workbook wb = new HSSFWorkbook();
        Sheet s = wb.createSheet();
        s.setColumnWidth(0, 4800);
        s.setColumnWidth(1, 4800);
        s.setColumnWidth(2, 4800);
        s.setColumnWidth(3, 4800);
        s.setColumnWidth(4, 4800);
        s.setColumnWidth(5, 4800);
        s.setColumnWidth(6, 4800);
        s.setColumnWidth(7, 4800);
        s.setColumnWidth(8, 4800);
        s.setColumnWidth(9, 4800);
        s.setColumnWidth(10, 4800);
        s.setColumnWidth(11, 4800);
        s.setColumnWidth(12, 4800);
        s.setColumnWidth(13, 4800);
        s.setColumnWidth(14, 4800);
        s.setColumnWidth(15, 4800);
        
        Row titleRow = s.createRow(0);
        titleRow.createCell(0).setCellValue("订单编号");
        titleRow.createCell(1).setCellValue("派车单编号");
        titleRow.createCell(2).setCellValue("派车单创建日期");
        titleRow.createCell(3).setCellValue("提货库房");
        titleRow.createCell(4).setCellValue("司机编号");
        titleRow.createCell(5).setCellValue("司机姓名");
        titleRow.createCell(6).setCellValue("司机单位");
        titleRow.createCell(7).setCellValue("车型");
        titleRow.createCell(8).setCellValue("订单签收类型");
        titleRow.createCell(9).setCellValue("订单订购金额");
        titleRow.createCell(10).setCellValue("订单出库金额");
        titleRow.createCell(11).setCellValue("订单签收金额");
        titleRow.createCell(12).setCellValue("订单应交款");
        titleRow.createCell(13).setCellValue("司机已交款");
        titleRow.createCell(14).setCellValue("收款人");
        titleRow.createCell(15).setCellValue("备注");
        
        for (int i = 0; i < dCartList.size(); i++) {
        	Map<String, Object> dCartMap = dCartList.get(i);
            Row dataRow = s.createRow(i + 1);
            dataRow.createCell(0).setCellValue(dCartMap.get("orderNo").toString());
            dataRow.createCell(1).setCellValue(dCartMap.get("dCartId").toString());
            dataRow.createCell(2).setCellValue(dCartMap.get("createTime").toString());
            dataRow.createCell(3).setCellValue(dCartMap.get("storageName").toString());
            dataRow.createCell(4).setCellValue(dCartMap.get("driverId") == null ? "" : dCartMap.get("driverId").toString());
            dataRow.createCell(5).setCellValue(dCartMap.get("driverName") == null ? "" : dCartMap.get("driverName").toString());
            dataRow.createCell(6).setCellValue(dCartMap.get("shipperName") == null ? "" : dCartMap.get("shipperName").toString());
            dataRow.createCell(7).setCellValue(dCartMap.get("driverModelName") == null ? "" : dCartMap.get("driverModelName").toString());
            dataRow.createCell(8).setCellValue(dCartMap.get("signType") == null ? "" : dCartMap.get("signType").toString());
            dataRow.createCell(9).setCellValue(dCartMap.get("orderGoodsMoney") == null ? "0" : dCartMap.get("orderGoodsMoney").toString());
            dataRow.createCell(10).setCellValue(dCartMap.get("outTakeMoney") == null ? "0" : dCartMap.get("outTakeMoney").toString());
            dataRow.createCell(11).setCellValue(dCartMap.get("signMoney") == null ? "0" : dCartMap.get("signMoney").toString());
            dataRow.createCell(12).setCellValue(dCartMap.get("ddyjk") == null ? "0" : dCartMap.get("ddyjk").toString());
            dataRow.createCell(13).setCellValue(dCartMap.get("sjyjk") == null ? "0" : dCartMap.get("sjyjk").toString());
            dataRow.createCell(14).setCellValue(dCartMap.get("skr") == null ? "" : dCartMap.get("skr").toString());
            dataRow.createCell(15).setCellValue(dCartMap.get("comment") == null ? "" : dCartMap.get("comment").toString());
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
	
	@SessionCheckInject
	@RequestMapping(value = {"receivables"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model receivables(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		@RequestParam(required = true) Integer dCartId) throws BusinessException {
		Date curTime = new Date();
		DeliveryCart dCart = this.deliveryCartService.findDCart(dCartId);
		if(null == dCart){
			model.addAttribute("s", 0);
			model.addAttribute("t", 6);
			model.addAttribute("m", "配送单不存在,请重试!");
			return model;
		}
		List<DeliveryOrder> dOrders = this.deliveryOrderService.qryDOrderList(dCartId, false);
		boolean isFinish = true;
		for(DeliveryOrder dOrder:dOrders){
			if(dOrder.getIsBackNotify() != null&&!dOrder.getIsBackNotify()){
				isFinish = false;
				break;
			}
		}
		this.deliveryCartService.txReceivablesFinishDCart(dCart,dOrders,profile.getPeopleName(),curTime,isFinish);
		this.deliveryOperateLogService.noteReceivablesFinishDCart(profile.getPeopleName(), curTime, dCartId,dOrders,isFinish);
		return model;
    }
	
	@SessionCheckInject
	@RequestMapping(value = {"/collection/detail"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model collectionDetail(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		@RequestParam(required = true) Integer dcartId) throws BusinessException {
		List<CollectionDetailView> detailList = collectionDetailService.selectCollectionByDCartId(dcartId);
		BigDecimal receivables = collectionDetailService.countReceivablesByDCartId(dcartId);
		if(receivables == null) receivables = BigDecimal.ZERO;
		
		DCartReceivablesView dcartReceivablesView = deliveryCartService.selectReceivablesForDCart(dcartId);
		
		model.addAttribute("collected", dcartReceivablesView.getCollected());
		model.addAttribute("money", dcartReceivablesView.getReceivables());
		model.addAttribute("collection", receivables.toString());
		
		model.addAttribute("detailList", detailList);
		return model;
    }
	
	@SessionCheckInject
	@RequestMapping(value = {"/collection"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model collection(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		@RequestParam(required = true) Integer dcartId,@RequestParam(required = true) Integer dorderId ,@RequestParam(required = true) BigDecimal amount) throws BusinessException {
		Date curTime = new Date();
		CollectionDetail cd = new CollectionDetail();
		cd.setDeliveryCartId(dcartId);
		cd.setDeliveryOrderId(dorderId);
		cd.setCollectionAmount(amount);
		cd.setOperatorId(Integer.parseInt(profile.getPeopleId()));
		cd.setOperatorName(profile.getPeopleName());
		cd.setCreateTime(curTime);
		
		int count = collectionDetailService.save(cd);
	    if(count == 1){
	    	model.addAttribute("s", 1);	
	    	model.addAttribute("m", "收款成功!");	
	    }else{
	    	model.addAttribute("s", 0);	
	    	model.addAttribute("m", "收款失败!");	
	    }
		return model;
    }
	
	@RequestMapping(value = {"/comment"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model updateComment(Model model,@RequestParam(required = true) Integer dorderId ,@RequestParam(required = true) String comment) throws BusinessException {
		DeliveryOrder deliveryOrder = new DeliveryOrder();
		deliveryOrder.setId(dorderId);
		deliveryOrder.setComment(comment);
		boolean success = deliveryOrderService.updateComment(deliveryOrder);
	    if(success){
	    	model.addAttribute("s", 1);	
	    	model.addAttribute("m", "添加备注成功!");	
	    }else{
	    	model.addAttribute("s", 0);	
	    	model.addAttribute("m", "添加备注失败!");	
	    }
		return model;
    }
}
