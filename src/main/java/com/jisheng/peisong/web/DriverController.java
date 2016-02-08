package com.jisheng.peisong.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.common.SendSms;
import com.jisheng.peisong.entity.Driver;
import com.jisheng.peisong.entity.Region;
import com.jisheng.peisong.entity.enums.DriverState;
import com.jisheng.peisong.entity.enums.JobType;
import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.service.DeliveryCartService;
import com.jisheng.peisong.service.DriverService;
import com.jisheng.peisong.service.RegionService;
import com.jisheng.peisong.util.CodeCreator;
import com.jisheng.peisong.util.Constant;
import com.jisheng.peisong.vo.DriverView;

/**
 * @Filename: DriverController.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年8月14日
 */
@Controller
@RequestMapping("/driver")
public class DriverController {
	@Resource(name = "driverService")
	DriverService driverService;
	@Resource(name = "regionService")
	RegionService regionService;
	
	@Resource(name = "deliveryCartService")
	DeliveryCartService deliveryCartService;
	
	@RequestMapping(value = "/login", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model driverLogin(Model model,HttpSession session,
    		@RequestParam(required = true) String password,
    		@RequestParam(required = true) String phone) {
		  Driver driver  = driverService.driverLogin(phone, password);
		  if(driver != null){
			 session.setAttribute(Constant.LOGINED_DRIVER, driver);
		     model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "司机登录成功!");	
		  }else{
		     model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "司机登录失败!");
		  }
        return model;
    }
	
	@RequestMapping(value = "/update/password", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model updatePassword(Model model,HttpSession session,
    		@RequestParam(required = true) String password,
    		@RequestParam(required = true) String repassword,
    		@RequestParam(required = true) String verifyCode,
    		@RequestParam(required = true) String phone) {
		    String sendCode = (String)session.getAttribute(Constant.VERIFY_CODE);
		  if(sendCode == null || !sendCode.equals(verifyCode)){
			  model.addAttribute("s", 0);	
	     	  model.addAttribute("m", "验证码错误!");	
	     	 return model;
		  }
		  if(!repassword.equals(password)){
			  model.addAttribute("s", 0);	
	     	  model.addAttribute("m", "确认密码与输入密码不一致!");	
	     	 return model;
		  }
		  Driver driver =   driverService.selectDriverByPhone(phone);
		  if(driver != null){
			  Driver driverNew = new Driver(); 
			  driverNew.setId(driver.getId());
			  driverNew.setPassword(password);
			  
			 int s =  driverService.updateDriverById(driverNew);
			  if(s == 1){
			     model.addAttribute("s", 1);	
		     	 model.addAttribute("m", "司机密码修改成功!");	
			  }else{
				 model.addAttribute("s", 0);	
			     model.addAttribute("m", "司机密码修改失败!");	
			  }

		  }else{
		     model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "修改密码失败,用户不存在!");
		  }
        return model;
    }
	
	@RequestMapping(value = "/{driverId}", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getDriver(Model model,HttpSession session,@PathVariable Integer driverId) {
		Driver driver = driverService.selectDriverById(driverId);
		
		if(driver == null){
			model.addAttribute("s",0);
			model.addAttribute("m","司机不存在!");
			return model;
		}else{
			Region city = regionService.selectByPrimaryKey(driver.getCityId());
			Region province = regionService.selectByPrimaryKey(city.getParentId());
			List<Region> cityList = regionService.selectRegionsByParentId(province.getId());
			List<Region> provinceList = regionService.selectRegionsByParentId(0);
			Map<String,String> stateMap = new HashMap<String,String>();
			if(driver.getDriverState() == DriverState.Normal){
				stateMap.put(driver.getDriverState().name(), driver.getDriverState().getDesc());
				stateMap.put(DriverState.Freeze.name(), DriverState.Freeze.getDesc());
			}else if(driver.getDriverState() == DriverState.Freeze){
				stateMap.put(DriverState.Normal.name(), DriverState.Normal.getDesc());
				stateMap.put(DriverState.Freeze.name(), DriverState.Freeze.getDesc());
				stateMap.put(DriverState.Finish.name(), DriverState.Finish.getDesc());
			}else if(driver.getDriverState() == DriverState.Finish){
				stateMap.put(DriverState.Finish.name(), DriverState.Finish.getDesc());
				stateMap.put(DriverState.Freeze.name(), DriverState.Freeze.getDesc());
			}
			model.addAttribute("s",1);
			model.addAttribute("m","司机获得成功!");
			model.addAttribute("stateMap",stateMap);
			model.addAttribute("driver",driver);
			model.addAttribute("province",province);
			model.addAttribute("cityList",cityList);
			model.addAttribute("provinceList",provinceList);
		}
		
        return model;
    }
	
	@RequestMapping(value = "/unassign/list", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getUnassignDriver(Model model,
    		@RequestParam(required = false) Integer modelId,
    		@RequestParam(required = false) Integer shipperId,
    		@RequestParam(required = true)  Integer cityId,
    		@RequestParam(required = false) String driverName) {
		  List<Driver> driverList = driverService.selectUnassignDriverList(modelId, shipperId, cityId, driverName);
		  model.addAttribute("driverList", driverList);	
        return model;
    }
	@RequestMapping(value = "/assign/list", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getAssignDriver(Model model,
    		@RequestParam(required = false) Integer modelId,
    		@RequestParam(required = false) Integer shipperId,
    		@RequestParam(required = true)  Integer cityId,
    		@RequestParam(required = false) String driverName) {
		
		  List<Driver> driverList = driverService.selectAssignDriverList(modelId, shipperId, cityId, driverName);
		  model.addAttribute("driverList", driverList);	
        return model;
    }
	@RequestMapping(value = "/resetableAssign/list", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getResetableAssignDriver(Model model,
    		@RequestParam(required = false) Integer modelId,
    		@RequestParam(required = false) Integer shipperId,
    		@RequestParam(required = true)  Integer cityId,
    		@RequestParam(required = false) String driverName) {
		
		  List<Driver> driverList = driverService.selectResetableDriverList(modelId, shipperId, cityId, driverName);
		  model.addAttribute("driverList", driverList);	
        return model;
    }
	@RequestMapping(value = "/create", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model create(Model model,
    		@RequestParam(required = true) String name,
    		@RequestParam(required = true) String phone,
    		@RequestParam(required = true) Integer sex,
    		@RequestParam(required = true) String idNumber,
    		@RequestParam(required = true) Integer modelId,
    		@RequestParam(required = true) String modelName,
    		@RequestParam(required = false) String plateNumber,
    		@RequestParam(required = true) Integer cityId,
    		@RequestParam(required = true) String cityName,
    		@RequestParam(required = true) String jobType,
    		@RequestParam(required = true) Integer shipperId,
    		@RequestParam(required = true) String shipperName) throws BusinessException{
		Driver driver = this.driverService.selectDriverByPhone(phone);
		if(driver != null){
			throw new BusinessException("创建失败，司机已存在");
		}
		Driver driverIdNumber = this.driverService.selectDriverByIdNumber(idNumber);
		if(driverIdNumber != null){
			     model.addAttribute("s", 0);	
		     	 model.addAttribute("m", "创建司机失败,身份证号已注册!");
		     	 return model;
		}
		driver = new Driver();
		driver.setName(name);
		driver.setPhone(phone);
		driver.setSex(sex);
		driver.setIdNumber(idNumber);
		driver.setModelId(modelId);
		driver.setModelName(modelName);
		driver.setPlateNumber(plateNumber);
		driver.setCityId(cityId);
		driver.setCityName(cityName);
		driver.setJobType(JobType.valueOf(jobType));
		driver.setShipperId(shipperId);
		driver.setShipperName(shipperName);
		driver.setLockStatus(false);
		driver.setDriverState(DriverState.Normal);
		
		Integer s = this.driverService.createDriver(driver);
		
		model.addAttribute("driverId", driver.getId());	
		if(s == 1){
	    	 model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "保存司机成功!");	
  	     }else{
  		     model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "保存司机失败!");
  	     }
        return model;
    }
	@RequestMapping(value = "/update", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model update(Model model,@RequestParam(required = true) Integer driverId,
    		@RequestParam(required = true) String name,
    		@RequestParam(required = true) String phone,
    		@RequestParam(required = true) Integer sex,
    		@RequestParam(required = true) String idNumber,
    		@RequestParam(required = true) Integer modelId,
    		@RequestParam(required = true) String modelName,
    		@RequestParam(required = false) String plateNumber,
    		@RequestParam(required = true) Integer cityId,
    		@RequestParam(required = true) String cityName,
    		@RequestParam(required = true) String jobType,
    		@RequestParam(required = true) Integer shipperId,
    		@RequestParam(required = true) String shipperName,
    		@RequestParam(required = true) String driverState) throws BusinessException{
		
		Driver driverOld = driverService.selectDriverById(driverId);
		if(driverOld == null){
			 model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "更新司机失败,司机不存在!");
	     	 return model;
		}
		//停用司机时检查司机接单后是否已返回
		if(DriverState.Finish.name().equals(driverState) && !driverOld.getDriverState().name().equals(driverState)){
			Integer num = deliveryCartService.selectUnfinishByDriver(driverId);
			if(num !=null && num != 0){
				 model.addAttribute("s", 0);	
		     	 model.addAttribute("m", "司机有未完成的派车单,不能停用!");
		     	 return model;
			}
		}
		Driver driverPhone = this.driverService.selectDriverByPhone(phone);
		if(driverPhone != null && driverPhone.getId() != driverId){
		     model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "修改司机失败,手机号已注册!");
	     	 return model;
		}
		Driver driverIdNumber = this.driverService.selectDriverByIdNumber(idNumber);
		if(driverIdNumber != null && driverIdNumber.getId() != driverId){
		     model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "修改司机失败,身份证号已注册!");
	     	 return model;
		}
		
		Driver driver = new Driver();
		driver.setId(driverId);
		driver.setName(name);
		driver.setPhone(phone);
		
		driver.setSex(sex);
		driver.setIdNumber(idNumber);
		driver.setModelId(modelId);
		driver.setModelName(modelName);
		driver.setPlateNumber(plateNumber);
		driver.setCityId(cityId);
		driver.setCityName(cityName);
		driver.setJobType(JobType.valueOf(jobType));
		driver.setShipperId(shipperId);
		driver.setShipperName(shipperName);
		driver.setDriverState(DriverState.valueOf(driverState));
		
		int success =  this.driverService.updateDriverById(driver);
		 
		if(success == 1){
	    	 model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "更新司机成功!");	
   	     }else{
   		     model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "更新司机失败!");
   	     }
		
        return model;
    }
	@RequestMapping(value = "/detail", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getDriverDetail(Model model,@RequestParam(required = true) Integer driverId) {
		
		 DriverView driver = this.driverService.selectDriverDetailById(driverId);
		
		 if(driver != null){
			model.addAttribute("driver",driver);
	    	 model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "司机查询成功!");	
   	     }else{
   		     model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "司机查询失败!");
   	     }
        return model;
    }
	@RequestMapping(value = "/sendCode", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model sendCode(Model model,HttpSession session,@RequestParam(required = true) String phone) {
		
		String verifyCode = CodeCreator.generateNumberArr(6);
		boolean success = new SendSms().sendSms(phone, verifyCode);
		
		 if(success){
		     session.setAttribute(Constant.VERIFY_CODE, verifyCode);
	    	 model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "验证码发送成功!");	
   	     }else{
   		     model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "验证码发送失败!");
   	     }
        return model;
    }
}

