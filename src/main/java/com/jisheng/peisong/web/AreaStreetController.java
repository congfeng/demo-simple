package com.jisheng.peisong.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.service.AreaStreetService;
import com.jisheng.peisong.vo.AreaStreetTree;

/**
 * @Filename: StorageDistrictController.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年9月23日
 */
@Controller
@RequestMapping("/area")
public class AreaStreetController {
	

	@Resource(name = "areaStreetService")
	AreaStreetService areaStreetService;

	@RequestMapping(value = "/saveAreaStreet", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model saveStorageStreet(Model model,@RequestParam(required = true) Integer cityId,@RequestParam(required = true) Integer areaId,@RequestParam(required = false) String streetIds) {
		 boolean success = false;
		if(streetIds != null && !"".equals(streetIds) && streetIds.split(",").length > 0){
			
    	    success = areaStreetService.txSaveAreaStreet(cityId,areaId,streetIds);
    	       if(success){
    	          	model.addAttribute("s", 1);
    	          	model.addAttribute("m", "区域街道添加成功!");
    	          }else{
    	          	model.addAttribute("s", 0);
    	          	model.addAttribute("m", "区域街道添加失败!");
    	          }
       }else{
    	   
    	   success = areaStreetService.txCancelAreaStreet(areaId);
    	   
	     	model.addAttribute("s", 1);
	     	model.addAttribute("m", "区域街道添加成功!");

       }

		return model;
    }
	
	@RequestMapping(value = "/areaStreet/list", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model areaStreet(Model model,@RequestParam(required = true) Integer cityId) {
    	   
    	   List<AreaStreetTree> areaStreetList = areaStreetService.selectAreaStreetTreeByParentId(cityId);
    	   
        if(areaStreetList != null){
    	    model.addAttribute("areaStreetList", areaStreetList);
          	model.addAttribute("s", 1);
          	model.addAttribute("m", "区域街道查询成功!");
          }else{
          	model.addAttribute("s", 0);
          	model.addAttribute("m", "区域街道查询失败!");
          }
		return model;
    }
}

