package com.jisheng.peisong.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.entity.Area;
import com.jisheng.peisong.service.AreaService;

/**
 * @Filename: AreaController.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年9月23日
 */
@Controller
@RequestMapping("/area")
public class AreaController {
	
	@Resource(name="areaService")
	private AreaService areaService;
	
	@RequestMapping(value = {"/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
   public Model list(Model model){
	   List<Area> areaList = areaService.selectAll();
	   if(areaList != null){
		     model.addAttribute("areaList", areaList);	
		     model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "区域查询成功!");
	   }else{
		     model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "区域查询失败!");
	   }
	   return model;
   }
}

