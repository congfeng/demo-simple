package com.jisheng.peisong.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.entity.People;
import com.jisheng.peisong.entity.Storage;
import com.jisheng.peisong.entity.StorageDistrict;
import com.jisheng.peisong.exception.CacheException;
import com.jisheng.peisong.service.CacheService;
import com.jisheng.peisong.service.StorageDistrictService;
import com.jisheng.peisong.service.StorageService;
import com.jisheng.peisong.service.StorageStreetService;
import com.jisheng.peisong.util.SessionUtil;
import com.jisheng.peisong.util.StringUtil;
import com.jisheng.peisong.vo.PeopleProfile;
import com.jisheng.peisong.vo.PeopleStorageView;

import redis.clients.jedis.Jedis;

/**
 * @Filename: StorageController.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年8月13日
 */
@Controller
@RequestMapping("/storage")
public class StorageController {
	
	@Resource(name = "cacheService")
	CacheService cacheService;
	
	
	@Resource(name = "storageService")
	StorageService storageService;
	
	@Resource(name = "storageDistrictService")
	StorageDistrictService storageDistrictService;
	
	@Resource(name = "storageStreetService")
	StorageStreetService storageStreetService;
	
	@RequestMapping(value = {"list/people"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model list(Model model,HttpSession httpSession,HttpServletRequest request) {
//		People people = httpSession.getAttribute(Constant.LOGINED_PEOPLE);
		PeopleProfile peopleProfile = 	getLoginedPeople(request);
        if(peopleProfile == null){
        	model.addAttribute("s", "0");
        	model.addAttribute("m", "登录失效!");
        	return model;
        }
		People people = new People();
		people.setPeopleId(peopleProfile.getPeopleId());
		List<Storage> storageList = storageService.selectByPeopleId(people.getPeopleId());
		model.addAttribute("storageList", storageList);
        return model;
    }
	
	@RequestMapping(value = {"with/people"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model storageWithPeople(Model model,HttpSession httpSession,@RequestParam(required = true) String peopleId) {

		List<Storage> storageList = storageService.selectByPeopleId(peopleId);
		
        if(storageList != null ){
        	model.addAttribute("storageList", storageList);
        	model.addAttribute("s", 1);
        	model.addAttribute("m", "库房查询成功!");
        }else{
        	model.addAttribute("s", 0);
        	model.addAttribute("m", "库房查询失败!");
        }
        return model;
    }
	
	@RequestMapping(value = {"list/all"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model listAll(Model model,HttpSession httpSession) {
		List<Storage> storageList = storageService.selectAll();
		model.addAttribute("storageList", storageList);
        return model;
    }
	@RequestMapping(value = "/saveStorageDistrict", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model saveStorageDistrict(Model model,@RequestParam(required = true) String storageId,@RequestParam(required = true) String provinceId,@RequestParam(required = true) String cityId,@RequestParam(required = false) String[] regionIds, HttpSession httpSession) {
//		String[] districtIds = districtId.split(",");
		if(regionIds != null && regionIds.length > 0){
	        int success = storageDistrictService.txSaveStorageDistrict(Integer.parseInt(storageId),Integer.parseInt(provinceId),Integer.parseInt(cityId),regionIds);
	        if(success == regionIds.length){
	        	model.addAttribute("s", 1);
	        	model.addAttribute("m", "库房区域添加成功!");
	        }else{
	        	model.addAttribute("s", 0);
	        	model.addAttribute("m", "库房区域添加失败!");
	        }
		}else{
			int s = storageDistrictService.txCancelStorageDistrict(Integer.parseInt(storageId),Integer.parseInt(provinceId),Integer.parseInt(cityId));
			if(s > 0){
				
				model.addAttribute("s", 1);
	        	model.addAttribute("m", "库房区域取消成功!");
			}else{
				model.addAttribute("s", 0);
	        	model.addAttribute("m", "库房区域取消失败!");
			}
		}
		return model;
    }
	
	@RequestMapping(value = "/saveStorageStreet", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model saveStorageStreet(Model model,@RequestParam(required = true) String storageId,@RequestParam(required = true) String storageDistrictId,@RequestParam(required = true) String streetId, HttpSession httpSession) {
       
        String[] streetIds = streetId.split(",");
        int success = storageStreetService.txSaveStorageStreet(Integer.parseInt(storageId),Integer.parseInt(storageDistrictId),arrayConvert(streetIds));
        if(success == streetIds.length){
        	model.addAttribute("s", 1);
        	model.addAttribute("m", "库房街道添加成功!");
        }else{
        	model.addAttribute("s", 0);
        	model.addAttribute("m", "库房街道添加失败!");
        }
		return model;
    }
	
	private Integer[] arrayConvert(String[] str){

		Integer [] num=new Integer[str.length];

		for(int i=0;i<num.length;i++){

			num[i]=Integer.parseInt(str[i]);
		}
		return num;
	}
	
	//获得登录名
		private PeopleProfile getLoginedPeople(HttpServletRequest request){
			
			String token = SessionUtil.readCookies(request, "peopleToken");
			
			if(StringUtil.isNullOrEmpty(token))return null;
			
			Jedis jedis = null;
			
			try {
				
				jedis = cacheService.getJedis();
				
				PeopleProfile popleProfile = 	SessionUtil.getLoginedPeople(jedis, token);
				
				return popleProfile;
				
			} catch (CacheException e) {
				
				e.printStackTrace();
			}finally{
				cacheService.releaseJedis(jedis);
			}
			return null;
			
		}
}

