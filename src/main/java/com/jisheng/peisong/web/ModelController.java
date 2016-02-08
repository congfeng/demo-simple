package com.jisheng.peisong.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.entity.Driver;
import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.service.DriverService;
import com.jisheng.peisong.service.ModelService;

/**
 * @Filename: ModelController.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年8月26日
 */
@Controller
@RequestMapping("/model")
public class ModelController {
	@Resource(name = "modelService")
	ModelService modelService;
	
	@Resource(name = "driverService")
	DriverService driverService;
	
	@RequestMapping(value = {"/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model modelList(Model model) {
		List<com.jisheng.peisong.entity.Model> modelList = modelService.selectModelList(new com.jisheng.peisong.entity.Model());
		 if(modelList != null){
	        	model.addAttribute("modelList", modelList);
	        	model.addAttribute("s", 1);
	        	model.addAttribute("m", "车型查询成功!");
	        }else{
	        	model.addAttribute("s", 0);
	        	model.addAttribute("m", "车型查询失败!");
	        }
		return model;
    }
	
	@RequestMapping(value = {"/name/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model modelNameList(Model model) {
		List<String> nameList = modelService.selectModelNameList(new com.jisheng.peisong.entity.Model());
		 if(nameList != null){
	        	model.addAttribute("nameList", nameList);
	        	model.addAttribute("s", 1);
	        	model.addAttribute("m", "车型名称查询成功!");
	        }else{
	        	model.addAttribute("s", 0);
	        	model.addAttribute("m", "车型名称查询失败!");
	        }
		return model;
    }
	@RequestMapping(value = {"/add"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model addModel(Model model,@RequestParam(required = true) String name,@RequestParam(required = true) String load,@RequestParam(required = true) String volume) throws BusinessException {
		 com.jisheng.peisong.entity.Model localModel = new com.jisheng.peisong.entity.Model();
		 localModel.setName(name);
		
		 List<com.jisheng.peisong.entity.Model> modelList = modelService.selectModelList(localModel);
		 if(modelList != null && modelList.size() > 0){
			 throw new BusinessException("创建失败，车型名称已存在");
		 }
		 
		 localModel.setLoad(load);
		 localModel.setVolume(volume);
		
		int success = modelService.saveModel(localModel);		
		  if(success == 1){
	        	model.addAttribute("s", 1);
	        	model.addAttribute("m", "车型保存成功!");
	        }else{
	        	model.addAttribute("s", 0);
	        	model.addAttribute("m", "车型保存失败!");
	        }
		return model;
    }
	
	@RequestMapping(value = {"/update"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model updateModel(Model model,@RequestParam(required = true) Integer id,@RequestParam(required = false) String name,@RequestParam(required = false) String load,@RequestParam(required = false) String volume) {
		 com.jisheng.peisong.entity.Model localModel = new com.jisheng.peisong.entity.Model();
		 localModel.setId(id);
		 localModel.setName(name);
		 localModel.setLoad(load);
		 localModel.setVolume(volume);
		 
		int success = modelService.updateModelById(localModel);		
		  if(success == 1){
	        	model.addAttribute("s", 1);
	        	model.addAttribute("m", "车型修改成功!");
	        }else{
	        	model.addAttribute("s", 0);
	        	model.addAttribute("m", "车型修改失败!");
	        }
		return model;
    }
	@RequestMapping(value = {"/delete"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model deleteModel(Model model,@RequestParam(required = true) Integer modelId) {
		List<Driver> driverList = driverService.selectDriverByModelId(modelId);
		if(driverList != null && driverList.size() > 0 ){
			model.addAttribute("s", 0);
        	model.addAttribute("m", "车型被司机占用,无法删除!");
        	return model;
		}
		int success = modelService.deleteModelById(modelId);	
		  if(success == 1){
	        	model.addAttribute("s", 1);
	        	model.addAttribute("m", "车型删除成功!");
	        }else{
	        	model.addAttribute("s", 0);
	        	model.addAttribute("m", "车型删除失败!");
	        }
		return model;
    }
}

