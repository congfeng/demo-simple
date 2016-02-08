package com.jisheng.peisong.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.vo.PeopleModuleTree;

/**
 * @Filename: SystemModuleController.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年9月1日
 */
@Controller
@RequestMapping("/system/module")
public class SystemModuleController {
	
	@Resource(name = "localPeopleService")
	com.jisheng.peisong.service.PeopleService localPeopleService;
	
	@RequestMapping(value = {"/people"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model moduleWithPeopleTree(Model model,HttpSession session,@RequestParam(required = true) String peopleId) {
		List<PeopleModuleTree> peopleModuleTreeList = localPeopleService.selectModuleWithPeopleTree(peopleId);
		if(peopleModuleTreeList != null ){
			 model.addAttribute("peopleModuleTree", peopleModuleTreeList);
		     model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "系统模块查询成功!");	
		  }else{
			 model.addAttribute("s", 0);	
		     model.addAttribute("m", "系统模块查询失败!");	
		  }
		return model;
    }
}

