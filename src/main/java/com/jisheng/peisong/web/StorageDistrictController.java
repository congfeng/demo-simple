package com.jisheng.peisong.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.entity.StorageDistrict;
import com.jisheng.peisong.service.StorageDistrictService;
import com.jisheng.peisong.vo.StorageDistrictView;

/**
 * @Filename: StorageDistrictController.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年8月17日
 */
@Controller
@RequestMapping("/storageDistrict")
public class StorageDistrictController {
	

	@Resource(name = "storageDistrictService")
	StorageDistrictService storageDistrictService;
	
	@RequestMapping(value = "/newDeliveryTime", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model updateDeliveryTime(Model model,@RequestParam(required = true) String storageDistrictId,@RequestParam(required = true) String newDeliveryTime) {
       
		StorageDistrict storageDistrict  =  new  StorageDistrict();
		storageDistrict.setId(Integer.parseInt(storageDistrictId));
		storageDistrict.setDeliveryTime(newDeliveryTime);
		int success = storageDistrictService.updateDeliveryTime(storageDistrictId, newDeliveryTime);
        if(success == 1){
        	model.addAttribute("s", 1);
        	model.addAttribute("m", "库房提货时间修改成功!");
        }else{
        	model.addAttribute("s", 0);
        	model.addAttribute("m", "库房提货时间修改失败!");
        }
		return model;
    }
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model storageDistrictList(Model model,@RequestParam(required = false) Integer storageId,@RequestParam(required = false) Integer provinceId,@RequestParam(required = false) Integer cityId,@RequestParam(required = false) Integer districtId) {
       
		StorageDistrictView storageDistrictView = new StorageDistrictView();
		storageDistrictView.setStorageId(storageId);
		storageDistrictView.setProvinceId(provinceId);
		storageDistrictView.setCityId(cityId);
		storageDistrictView.setDistrictId(districtId);
		
		List<StorageDistrictView> storageList = storageDistrictService.selectStorageDistrictDetail(storageDistrictView);
		
        if(storageList != null){
        	model.addAttribute("list", storageList);
        	model.addAttribute("s", 1);
        	model.addAttribute("m", "库房区域查询成功!");
        }else{
        	model.addAttribute("s", 0);
        	model.addAttribute("m", "库房区域查询失败!");
        }
		return model;
    }
}

