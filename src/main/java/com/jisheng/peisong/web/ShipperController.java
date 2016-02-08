package com.jisheng.peisong.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.entity.Shipper;
import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.service.ShipperService;
import com.jisheng.peisong.vo.ShipperView;

/**
 * @Filename: ShipperController.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年8月18日
 */
@Controller
@RequestMapping("/shipper")
public class ShipperController {
	@Resource(name = "shipperService")
	ShipperService shipperService;
	
	
	@RequestMapping(value = "/save", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model saveShipper(Model model, @RequestParam(required = true) String shipperName) throws BusinessException{
		Shipper shipper = new  Shipper();
		shipper.setName(shipperName);
		
		Shipper sp = shipperService.selectByName(shipperName);
		if(sp != null){
			throw new BusinessException("创建失败，承运商已存在");
		}
		
		int success = shipperService.saveShipper(shipper);
		if(success == 1){
	    	model.addAttribute("s", 1);	
	    	model.addAttribute("m", "添加成功!");
		}else{
	    	model.addAttribute("s", 0);	
	    	model.addAttribute("m", "添加 失败!");
		}
        return model;
    }

	@RequestMapping(value = "/list", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model listShipper(Model model) {
		      List<ShipperView> shipperList = shipperService.selectShipperWithDriver();
		      model.addAttribute("shipperList", shipperList);
	    	    model.addAttribute("s", 1);	
	    	   model.addAttribute("m", "添加成功!");
        return model;
    }
}

