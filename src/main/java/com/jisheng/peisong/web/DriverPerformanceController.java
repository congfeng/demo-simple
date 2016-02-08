package com.jisheng.peisong.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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

import com.jisheng.peisong.entity.Region;
import com.jisheng.peisong.entity.Storage;
import com.jisheng.peisong.entity.enums.DriverState;
import com.jisheng.peisong.entity.enums.JobType;
import com.jisheng.peisong.exception.CacheException;
import com.jisheng.peisong.service.CacheService;
import com.jisheng.peisong.service.DriverPerformanceService;
import com.jisheng.peisong.service.StorageDistrictService;
import com.jisheng.peisong.util.Pager;
import com.jisheng.peisong.util.SessionUtil;
import com.jisheng.peisong.util.StringUtil;
import com.jisheng.peisong.vo.DriverPerformanceView;
import com.jisheng.peisong.vo.PeopleProfile;
import com.jisheng.peisong.web.session.SessionCheckInject;

import redis.clients.jedis.Jedis;

/**
 * @Filename: DriverPerformanceController.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年8月27日
 */
@Controller
@RequestMapping("/driver/performance")
public class DriverPerformanceController {
	@Resource(name = "driverPerformanceService")
	DriverPerformanceService driverPerformanceService;
	
	@Resource(name = "storageDistrictService")
	StorageDistrictService storageDistrictService;
	
	@Resource(name = "cacheService")
	CacheService cacheService;
	
	@SessionCheckInject
	@RequestMapping(value = "/list", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getDriverPerformanceList(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,Model model,
    		                                          @RequestParam(required=false) Integer pageNo,
    		                                          @RequestParam(required=false) Integer modelId,
    		                                          @RequestParam(required=false) Integer shipperId,
    		                                          @RequestParam(required=false) Integer cityId,
    		                                          @RequestParam(required=false) String  status,
    		                                          @RequestParam(required=false) String startTime,
    		                                          @RequestParam(required=false) String endTime,
    		                                          @RequestParam(required=false) String driverName) {
		DriverPerformanceView driverPerformanceView = new DriverPerformanceView();
		
		driverPerformanceView.setModelId(modelId);
		driverPerformanceView.setShipperId(shipperId);
		driverPerformanceView.setCityId(cityId);
		if(status !=null && !"".equals(status)){
		driverPerformanceView.setDriverState(status);
		}
		if(startTime !=null && !"".equals(startTime)){
			driverPerformanceView.setStartTime(startTime+" 00:00:00");
		}
		if(endTime !=null && !"".equals(endTime)){
			driverPerformanceView.setEndTime(endTime+" 59:59:59");
		}
		if(!StringUtil.isNullOrEmpty(driverName)){
			driverPerformanceView.setDriverName(driverName);
		}
		if(pageNo == null ){
			pageNo = 1;
		}
		

		List<Storage> storageList = profile.getStorageList();
		List<Integer> storageIds = new ArrayList<Integer>();
		List<Region> regionList = null;
		if(storageList != null && storageList.size() > 0){
			for(Storage storage : storageList){
				storageIds.add(storage.getId());
			}
			regionList = storageDistrictService.selectCityByStorageIds(storageIds);
		}
		List<Integer> regionIds = new ArrayList<Integer>();
		if(regionList != null && regionList.size() > 0){
			for(Region region : regionList){
				regionIds.add(region.getId());
			}
			if(driverPerformanceView.getCityId() ==  null && !"admin".equals(profile.getPeopleName().trim())){
			     driverPerformanceView.setCityIds(regionIds);
			}
		}
		
		Integer count = driverPerformanceService.selectDriverPerformanceCount(driverPerformanceView);
		
		Pager pager  = new Pager();
		pager.setCount(count);
		pager.setPageNo(pageNo);

			
		driverPerformanceView.setSize(pager.getPageSize());
		driverPerformanceView.setStartRow(pager.getStartIndex());
		
		  List<DriverPerformanceView> driverList = driverPerformanceService.selectDriverPerformanceList(driverPerformanceView);
		  model.addAttribute("driverList", driverList);
		  model.addAttribute("pageIndex", pager.getPageIndexs());
		  model.addAttribute("pageNo", pager.getPageNo());
		  model.addAttribute("pageMaxNo", pager.getMaxPageNo());
        return model;
    }
	
	
	@RequestMapping(value = "/down", method = { RequestMethod.GET,RequestMethod.POST})
    public void downDriverPerformanceList(Model model,HttpServletResponse response,HttpServletRequest request,
									            @RequestParam(required=false) Integer modelId,
									            @RequestParam(required=false) Integer shipperId,
									            @RequestParam(required=false) Integer cityId,
									            @RequestParam(required=false) String  status,
									            @RequestParam(required=false) String startTime,
									            @RequestParam(required=false) String endTime,
									            @RequestParam(required=false) String driverName) throws IOException {
		
		    DriverPerformanceView driverPerformanceView = new DriverPerformanceView();
		    driverPerformanceView.setModelId(modelId);
			driverPerformanceView.setShipperId(shipperId);
			driverPerformanceView.setCityId(cityId);
			if(status !=null && !"".equals(status)){
			   driverPerformanceView.setDriverState(status);
			}
			if(startTime !=null && !"".equals(startTime)){
				driverPerformanceView.setStartTime(startTime+" 00:00:00");
			}
			if(endTime !=null && !"".equals(endTime)){
				driverPerformanceView.setEndTime(endTime+" 59:59:59");
			}
			if(!StringUtil.isNullOrEmpty(driverName)){
				driverPerformanceView.setDriverName(driverName);
			}
			
			PeopleProfile peopleProfile  = getLoginedPeople(request);

			List<Storage> storageList = peopleProfile.getStorageList();
			List<Integer> storageIds = new ArrayList<Integer>();
			List<Region> regionList = null;
			if(storageList != null && storageList.size() > 0){
				for(Storage storage : storageList){
					storageIds.add(storage.getId());
				}
				regionList = storageDistrictService.selectCityByStorageIds(storageIds);
			}
			List<Integer> regionIds = new ArrayList<Integer>();
			if(regionList != null && regionList.size() > 0){
				for(Region region : regionList){
					regionIds.add(region.getId());
				}
				if(driverPerformanceView.getCityId() ==  null && !"admin".equals(peopleProfile.getPeopleName().trim())){
				     driverPerformanceView.setCityIds(regionIds);
				}
			}
			
		    List<DriverPerformanceView> driverList = driverPerformanceService.selectDriverPerformanceList(driverPerformanceView);

			String filename = "driver_performance_list.csv";
	    	Workbook wb = new HSSFWorkbook();
	        Sheet s = wb.createSheet();
	        s.setColumnWidth(0, 4800);
	        s.setColumnWidth(1, 4800);
	        s.setColumnWidth(2, 4800);
	        s.setColumnWidth(3, 4800);
	        s.setColumnWidth(4, 4800);
	        s.setColumnWidth(5, 4800);

	        Row titleRow = s.createRow(0);
	        titleRow.createCell(0).setCellValue("司机编号");
	        titleRow.createCell(1).setCellValue("司机姓名");
	        titleRow.createCell(2).setCellValue("手机号码");	        
	        titleRow.createCell(3).setCellValue("司机身份证号码");
	        titleRow.createCell(4).setCellValue("车牌号码");
	        titleRow.createCell(5).setCellValue("服务城市");	  
	        titleRow.createCell(6).setCellValue("所属单位");
	        titleRow.createCell(7).setCellValue("车型");
	        titleRow.createCell(8).setCellValue("司机类型");	  	        
	        titleRow.createCell(9).setCellValue("当前状态");
	        titleRow.createCell(10).setCellValue("常配送街道");	        
	        titleRow.createCell(11).setCellValue("累计已接商户数");
	        titleRow.createCell(12).setCellValue("已分配派车单数量");
	        titleRow.createCell(13).setCellValue("已送达派车单数量");
	        titleRow.createCell(14).setCellValue("已送达订单数量");
	       
	        for (int i = 0; i < driverList.size(); i++) {
	        	DriverPerformanceView driver = driverList.get(i);
	            Row dataRow = s.createRow(i + 1);
	            dataRow.createCell(0).setCellValue(driver.getDriverId());
	            dataRow.createCell(1).setCellValue(driver.getDriverName());
	            dataRow.createCell(2).setCellValue(driver.getPhone());
	            dataRow.createCell(3).setCellValue(driver.getIdNumber());
	            dataRow.createCell(4).setCellValue(driver.getPlateNumber());
	            dataRow.createCell(5).setCellValue(driver.getCityName());
	            dataRow.createCell(6).setCellValue(driver.getShipperName());
	            dataRow.createCell(7).setCellValue(driver.getModelName());
	            dataRow.createCell(8).setCellValue(JobType.valueOf(driver.getJobType()).getDesc());
	            dataRow.createCell(9).setCellValue(DriverState.valueOf(driver.getDriverState()).getDesc());
	            dataRow.createCell(10).setCellValue(driver.getStreetName());
	            dataRow.createCell(11).setCellValue(driver.getMerchantNum()+"");
	            dataRow.createCell(12).setCellValue(driver.getAssignmentBillNum()+"");
	            dataRow.createCell(13).setCellValue(driver.getCompletedAssignmentNum()+"");
	            dataRow.createCell(14).setCellValue(driver.getCompletedOrderNum()+"");
	           
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
	
	@RequestMapping(value = "/export", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model export(Model model,HttpServletResponse response,HttpServletRequest request,
									            @RequestParam(required=false) Integer modelId,
									            @RequestParam(required=false) Integer shipperId,
									            @RequestParam(required=false) Integer cityId,
									            @RequestParam(required=false) String  status,
									            @RequestParam(required=false) String startTime,
									            @RequestParam(required=false) String endTime,
									            @RequestParam(required=false) String driverName) throws IOException {
		
		    DriverPerformanceView driverPerformanceView = new DriverPerformanceView();
		    driverPerformanceView.setModelId(modelId);
			driverPerformanceView.setShipperId(shipperId);
			driverPerformanceView.setCityId(cityId);
			if(status !=null && !"".equals(status)){
			   driverPerformanceView.setDriverState(status);
			}
			if(startTime !=null && !"".equals(startTime)){
				driverPerformanceView.setStartTime(startTime+" 00:00:00");
			}
			if(endTime !=null && !"".equals(endTime)){
				driverPerformanceView.setEndTime(endTime+" 59:59:59");
			}
			if(!StringUtil.isNullOrEmpty(driverName)){
				driverPerformanceView.setDriverName(driverName);
			}
			
			PeopleProfile peopleProfile  = getLoginedPeople(request);
			if(peopleProfile == null){
				  model.addAttribute("s", 0);
				  model.addAttribute("m", "请登录后再操作!");
				  return model;
			}
			
			List<Storage> storageList = peopleProfile.getStorageList();
			List<Integer> storageIds = new ArrayList<Integer>();
			List<Region> regionList = null;
			if(storageList != null && storageList.size() > 0){
				for(Storage storage : storageList){
					storageIds.add(storage.getId());
				}
				regionList = storageDistrictService.selectCityByStorageIds(storageIds);
			}
			List<Integer> regionIds = new ArrayList<Integer>();
			if(regionList != null && regionList.size() > 0){
				for(Region region : regionList){
					regionIds.add(region.getId());
				}
				if(driverPerformanceView.getCityId() ==  null && !"admin".equals(peopleProfile.getPeopleName().trim())){
				     driverPerformanceView.setCityIds(regionIds);
				}
			}
			
		    List<DriverPerformanceView> driverList = driverPerformanceService.selectDriverPerformanceList(driverPerformanceView);
		    if(driverList == null || driverList.size() == 0){
		    	  model.addAttribute("s", 0);
				  model.addAttribute("m", "无数据可导出!");
				  
		    }else{
		    	  model.addAttribute("s", 1);
				  model.addAttribute("m", "导出数据成功!");
		    }
		    return model;
	}
	//获得登录名
		private PeopleProfile getLoginedPeople(HttpServletRequest request){
			
			String token = SessionUtil.readCookies(request, "peopleToken");
			
			if(StringUtil.isNullOrEmpty(token))return null;
			
			Jedis jedis = null;
			
			try {
				long start = System.currentTimeMillis();
				
				jedis = cacheService.getJedis();
				
				long end = System.currentTimeMillis();
				System.out.println("获得jedis时长:"+(end-start)+"s");
				PeopleProfile popleProfile = 	SessionUtil.getLoginedPeople(jedis, token);
				
				return popleProfile;
				
			} catch (CacheException e) {
				
				e.printStackTrace();
			}finally{
				long start = System.currentTimeMillis();
				cacheService.releaseJedis(jedis);
				long end = System.currentTimeMillis();
				System.out.println("释放jedis时长:"+(end-start+"s"));
				
			}
			return null;
			
		}
}

