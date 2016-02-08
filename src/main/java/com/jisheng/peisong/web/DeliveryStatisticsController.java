/**
 * 
 */
package com.jisheng.peisong.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.jisheng.peisong.entity.enums.DCartDelay;
import com.jisheng.peisong.entity.enums.DCartState;
import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.service.DeliveryCartService;
import com.jisheng.peisong.service.DeliveryOrderService;
import com.jisheng.peisong.service.RegionService;
import com.jisheng.peisong.util.DateUtil;
import com.jisheng.peisong.util.Pager;
import com.jisheng.peisong.util.StringUtil;
import com.jisheng.peisong.vo.DCartOrderView;
import com.jisheng.peisong.vo.DCartView;
import com.jisheng.peisong.vo.DCartViewCountInfo;
import com.jisheng.peisong.vo.PeopleProfile;
import com.jisheng.peisong.web.session.SessionCheckInject;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Controller
@RequestMapping("/dcart/statistics")
public class DeliveryStatisticsController {

	@Resource(name = "deliveryCartService")
	DeliveryCartService deliveryCartService;
	
	@Resource(name = "deliveryOrderService")
	DeliveryOrderService deliveryOrderService;
	
	@Resource(name = "regionService")
	RegionService regionService;
	
	
	@SessionCheckInject
	@RequestMapping(value = {"list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model list(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		@RequestParam(required = false) Integer pageNo,
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
    		@RequestParam(required = false) String dCartDelayText) throws BusinessException {
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
		DCartDelay dCartDelay = null;
		if(!StringUtil.isNullOrEmpty(dCartDelayText)){
			dCartDelay = DCartDelay.valueOf(dCartDelayText);
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
		
		if(pageNo == null) pageNo = 1;
		
		Pager pager = new Pager();
		
		DCartViewCountInfo dCartCountInfo = this.deliveryCartService.qryList4ViewCountInfo(storageId, 
				storageIds, cityId, districtId, streetId, createDay, createDateStrat, createDateEnd, dCartState, 
				null, dCartId, orderNo, shipperId, driverName,driverPhone, dCartDelay,null);
		
		pager.setCount(dCartCountInfo.getCount());
		pager.setPageNo(pageNo);
		
		List<DCartView> dCartList = this.deliveryCartService.qryList4View(storageId, 
				storageIds, cityId, districtId, streetId, createDay, createDateStrat, createDateEnd, dCartState, 
				null, dCartId, orderNo, shipperId, driverName,driverPhone, dCartDelay,null, pager.getStartIndex(), pager.getPageSize());
		
		  model.addAttribute("dCartList", dCartList);   
		  model.addAttribute("dCartCountInfo", dCartCountInfo);
		  model.addAttribute("pageIndex", pager.getPageIndexs());
		  model.addAttribute("pageNo", pager.getPageNo());
		  model.addAttribute("pageMaxNo", pager.getMaxPageNo());
        return model;
    }
	
	@SessionCheckInject
	@RequestMapping(value = {"export"}, method = { RequestMethod.GET,RequestMethod.POST})
    public void export(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model,
    		HttpServletResponse response,
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
    		@RequestParam(required = false) String dCartDelayText) throws IOException {
		if(StringUtil.isNullOrEmpty(orderNo)){
			orderNo = null;
		}
		DCartState dCartState = null;
		if(!StringUtil.isNullOrEmpty(dCartStateText)){
			dCartState = DCartState.valueOf(dCartStateText);
		}
		DCartDelay dCartDelay = null;
		if(!StringUtil.isNullOrEmpty(dCartDelayText)){
			dCartDelay = DCartDelay.valueOf(dCartDelayText);
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
		List<DCartOrderView> dCartOrderList = this.deliveryCartService.qryList4OrderView(storageId, 
				storageIds, cityId, districtId, streetId, createDay, createDateStrat, createDateEnd, dCartState, 
				null, dCartId, orderNo, shipperId, driverName,driverPhone, dCartDelay,null);
		String filename = "dcart_order_list.csv";
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
        s.setColumnWidth(16, 4800);
        s.setColumnWidth(17, 4800);
        s.setColumnWidth(18, 4800);
        s.setColumnWidth(19, 4800);
        s.setColumnWidth(20, 4800);
        s.setColumnWidth(21, 4800);
        Row titleRow = s.createRow(0);
        titleRow.createCell(0).setCellValue("订单编号");
        titleRow.createCell(1).setCellValue("派车单编号");
        titleRow.createCell(2).setCellValue("商户名称");
        titleRow.createCell(3).setCellValue("收货省份");
        titleRow.createCell(4).setCellValue("收货城市");
        titleRow.createCell(5).setCellValue("收货区域");
        titleRow.createCell(6).setCellValue("收货街道");
        titleRow.createCell(7).setCellValue("收货详细地址");
        titleRow.createCell(8).setCellValue("提货库房");
        titleRow.createCell(9).setCellValue("司机编号");
        titleRow.createCell(10).setCellValue("司机姓名");
        titleRow.createCell(11).setCellValue("司机手机号");
        
        titleRow.createCell(12).setCellValue("订单金额");
        titleRow.createCell(13).setCellValue("出库金额");
        titleRow.createCell(14).setCellValue("签收金额");
        titleRow.createCell(15).setCellValue("司机应交款金额");
        
        titleRow.createCell(16).setCellValue("签收类型");
        titleRow.createCell(17).setCellValue("提货时间");
        titleRow.createCell(18).setCellValue("送达时间");
        titleRow.createCell(19).setCellValue("交款时间");
        titleRow.createCell(20).setCellValue("最早要求送货时间");
        titleRow.createCell(21).setCellValue("库房要求提货时间");
        
        Map<Integer, String> provinceIdNameMap = new HashMap<Integer, String>();
        
        for (int i = 0; i < dCartOrderList.size(); i++) {
        	DCartOrderView dCartOrder = dCartOrderList.get(i);
        	
        	Integer provinceId = dCartOrder.getReceiverProvinceId();
			String provinceName = null;
			if(provinceIdNameMap.get(provinceId) == null){
				provinceName = regionService.selectByPrimaryKey(provinceId).getName();
				provinceIdNameMap.put(provinceId, provinceName);
			}else{
				provinceName = provinceIdNameMap.get(provinceId);
			}
        	
        	
            Row dataRow = s.createRow(i + 1);
            dataRow.createCell(0).setCellValue(dCartOrder.getOrderNo());
            dataRow.createCell(1).setCellValue(dCartOrder.getdCartId()+"");
            dataRow.createCell(2).setCellValue(dCartOrder.getBuyerName());
            dataRow.createCell(3).setCellValue(provinceName);
            dataRow.createCell(4).setCellValue(dCartOrder.getReceiverCityName());
            dataRow.createCell(5).setCellValue(dCartOrder.getReceiverDistrictName());
            dataRow.createCell(6).setCellValue(dCartOrder.getReceiverStreetName());
            dataRow.createCell(7).setCellValue(dCartOrder.getReceiverAddress());
            dataRow.createCell(8).setCellValue(dCartOrder.getStorageName());
            dataRow.createCell(9).setCellValue(dCartOrder.getDriverId()==null?"":dCartOrder.getDriverId()+"");
            dataRow.createCell(10).setCellValue(dCartOrder.getDriverName());
            dataRow.createCell(11).setCellValue(dCartOrder.getDriverMobile());
            
            dataRow.createCell(12).setCellValue(dCartOrder.getOrderGoodsMoney() + "");
            dataRow.createCell(13).setCellValue(dCartOrder.getOutTakeMoney()==null?"":dCartOrder.getOutTakeMoney()+"");
            dataRow.createCell(14).setCellValue(dCartOrder.getSignMoney()==null?"":dCartOrder.getSignMoney()+"");
            
            String dskMoneyStr = "";
            if(dCartOrder.getOutTakeMoney() != null){
            	BigDecimal money = BigDecimal.ZERO;
    			if(dCartOrder.getSignMoney() != null){
    				money = dCartOrder.getSignMoney().subtract(dCartOrder.getOutTakeMoney().subtract(dCartOrder.getMoney()));
    			}else if(dCartOrder.getMoney() != null){
    				money = dCartOrder.getMoney();
    			}
    			if(money.compareTo(BigDecimal.ZERO) < 0){
    				money = BigDecimal.ZERO;
    			}
    			
    			dskMoneyStr = money + "";
            }
            dataRow.createCell(15).setCellValue(dskMoneyStr);
            
            
            dataRow.createCell(16).setCellValue(dCartOrder.getSignType()==null?"":dCartOrder.getSignType().desc);
            dataRow.createCell(17).setCellValue(dCartOrder.getPickupTimeText());
            dataRow.createCell(18).setCellValue(dCartOrder.getDeliveryTimeText());
            dataRow.createCell(19).setCellValue(dCartOrder.getReceivablesTimeText());
            /**
            String delayTypeText = "";
            if(dCartOrder.isPickupDelay()&&dCartOrder.isDeliveryDelay()){
            	delayTypeText = "配送&提货延迟";
            }else if(dCartOrder.isPickupDelay()){
            	delayTypeText = "提货延迟";
            }else if(dCartOrder.isDeliveryDelay()){
            	delayTypeText = "配送延迟";
            }
            dataRow.createCell(16).setCellValue(delayTypeText);
            */
            dataRow.createCell(20).setCellValue(dCartOrder.getReqDeliveryTimeText());
            dataRow.createCell(21).setCellValue(dCartOrder.getReqPickupTimeText());
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
