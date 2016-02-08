package com.jisheng.peisong.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.entity.AreaStreet;
import com.jisheng.peisong.entity.Region;
import com.jisheng.peisong.entity.Storage;
import com.jisheng.peisong.entity.StorageDistrict;
import com.jisheng.peisong.entity.enums.RegionLevel;
import com.jisheng.peisong.entity.enums.RegionStatus;
import com.jisheng.peisong.exception.CacheException;
import com.jisheng.peisong.service.AreaStreetService;
import com.jisheng.peisong.service.CacheService;
import com.jisheng.peisong.service.RegionService;
import com.jisheng.peisong.service.StorageDistrictService;
import com.jisheng.peisong.util.SessionUtil;
import com.jisheng.peisong.util.StringUtil;
import com.jisheng.peisong.vo.PeopleProfile;
import com.jisheng.peisong.vo.RegionStorage;
import com.jisheng.peisong.vo.RegionStorageTree;
import com.jisheng.peisong.web.session.SessionCheckInject;

import redis.clients.jedis.Jedis;

/**
 * @Filename: RegionController.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年8月13日
 */
@Controller
@RequestMapping("/region")
public class RegionController {
	
	@Resource(name = "regionService")
	RegionService regionService;
	
	@Resource(name = "cacheService")
	CacheService cacheService;
	
	@Resource(name = "storageDistrictService")
	StorageDistrictService storageDistrictService;
	
	@Resource(name = "areaStreetService")
	AreaStreetService areaStreetService;
	
	@RequestMapping(value = "/storage/{storageId}/{level}/{parentId}", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getRegionsByStorageId(Model model,@PathVariable String storageId,@PathVariable String level,@PathVariable String parentId) {
		RegionLevel regionLevel =RegionLevel.parseRegionLevel(Integer.parseInt(level));
		List<Region> regionList = new ArrayList<Region>();
		if(regionLevel != null){
			switch (regionLevel) {
			case PROVINCE:
				regionList = regionService.selectProvinceByStorageId(Integer.parseInt(storageId));
				break;
		    case CITY:
		    	regionList = regionService.selectCitysByStorageParent_Id(Integer.parseInt(storageId), Integer.parseInt(parentId));
				break;
		    case DISTRICT:
		    	regionList = regionService.selectDistrictsByStorageParent_Id(Integer.parseInt(storageId), Integer.parseInt(parentId));
		    	break;
		    case STREET:
		    	regionList = regionService.selectStreetsByStorageParent_Id(Integer.parseInt(storageId), Integer.parseInt(parentId));
		    	break;
			default:
				break;
			}
		}
		model.addAttribute("regionList", regionList);	
        return model;
    }
    
    @RequestMapping(value = "/province", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getProvince(Model model) {
    	List<Region> regionList = regionService.selectRegionsByParentId(0);
    	model.addAttribute("s", 1);	
    	model.addAttribute("m", "查询成功!");	
		model.addAttribute("regionList", regionList);	
        return model;
    }
    
    @RequestMapping(value = "/childs/{parentId}", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getRegionsByParentId(Model model,@PathVariable String parentId) {
    	List<Region> regionList = regionService.selectRegionsByParentId(Integer.parseInt(parentId));
    	model.addAttribute("s", 1);	
    	model.addAttribute("m", "查询成功!");	
		model.addAttribute("regionList", regionList);	
        return model;
    }
    
    @RequestMapping(value = "/childs/all/{parentId}", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getRegionsAllByParentId(Model model,@PathVariable String parentId) {
    	List<Region> regionList = regionService.selectRegionsAllByParentId(Integer.parseInt(parentId));
    	model.addAttribute("s", 1);	
    	model.addAttribute("m", "查询成功!");	
		model.addAttribute("regionList", regionList);	
        return model;
    }
    
    @RequestMapping(value = "/provinces/enabled", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getProvincesByParentId(Model model) {
    	Region region = new Region();
    	region.setParentId(0);
    	region.setStatus(RegionStatus.ENABLE);
    	List<Region> regionList = regionService.selectRegions(region);
    	model.addAttribute("s", 1);	
    	model.addAttribute("m", "查询成功!");	
		model.addAttribute("regionList", regionList);	
        return model;
    }
    
    @SessionCheckInject
    @RequestMapping(value = "/citys/storage/list", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getCitysForStorage(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,Model model) {
    	
		List<Storage> storageList = profile.getStorageList();
		List<Integer> storageIds = new ArrayList<Integer>();
		List<Region> citysForStorageList = new ArrayList<Region>();
		if(storageList != null && storageList.size() > 0){
			for(Storage storage : storageList){
				storageIds.add(storage.getId());
			}
			citysForStorageList = storageDistrictService.selectCityByStorageIds(storageIds);
		}
		
    	if(citysForStorageList != null){
        	model.addAttribute("s", 1);	
        	model.addAttribute("m", "查询成功!");	
    		model.addAttribute("citysForStorageList", citysForStorageList);
    	}else{
	    	model.addAttribute("s", 0);	
	    	model.addAttribute("m", "查询失败!");	
    	}
        return model;
    }
    @RequestMapping(value = "/citys/enabled/list", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getCitysEnabledList(Model model) {
    	Region region = new Region();
    	region.setStatus(RegionStatus.ENABLE);
    	region.setRegionLevel(RegionLevel.CITY);
    	List<Region> citysEnabledList = regionService.selectRegions(region);
    	if(citysEnabledList != null){
        	model.addAttribute("s", 1);	
        	model.addAttribute("m", "查询成功!");	
    		model.addAttribute("regionList", citysEnabledList);
    	}else{
	    	model.addAttribute("s", 0);	
	    	model.addAttribute("m", "查询失败!");	
    	}
        return model;
    }
    @RequestMapping(value = "/citys/enabled/{parentId}", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getCitysByParentId(Model model,@PathVariable String parentId) {
    	Region region = new Region();
    	region.setParentId(Integer.parseInt(parentId));
    	region.setStatus(RegionStatus.ENABLE);
    	List<Region> regionList = regionService.selectRegions(region);
    	if(regionList != null){
        	model.addAttribute("s", 1);	
        	model.addAttribute("m", "查询成功!");	
    		model.addAttribute("regionList", regionList);
    	}else{
	    	model.addAttribute("s", 0);	
	    	model.addAttribute("m", "查询失败!");	
    	}
        return model;
    }
    @RequestMapping(value = "/city/{id}", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getCityById(Model model,@PathVariable String id) {
    	Region region = new Region();
    	region.setId(Integer.parseInt(id));
    	region.setRegionLevel(RegionLevel.CITY);
    	region.setStatus(RegionStatus.ENABLE);
    	Region cityRegion = regionService.selectByPrimaryKey(Integer.parseInt(id));
    	model.addAttribute("s", 1);	
    	model.addAttribute("m", "查询成功!");	
		model.addAttribute("cityRegion", cityRegion);	
        return model;
    }
    @RequestMapping(value = "/city/all/{id}", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getCityAllById(Model model,@PathVariable String id) {
    	Region region = new Region();
    	region.setId(Integer.parseInt(id));
    	region.setRegionLevel(RegionLevel.CITY);
    	Region cityRegion = regionService.selectByPrimaryKey(Integer.parseInt(id));
    	model.addAttribute("s", 1);	
    	model.addAttribute("m", "查询成功!");	
		model.addAttribute("cityRegion", cityRegion);	
        return model;
    }
    @RequestMapping(value = "/district/storage/{parentId}", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getDistrictWithStorageByParentId(Model model,@PathVariable String parentId) {
    	 Region region = new Region();
    	 region.setParentId(Integer.parseInt(parentId));
    	List<RegionStorage> regionStorageList = regionService.selectDistrictWithStorage(region);
		model.addAttribute("regionStorageList", regionStorageList);	
        return model;
    }
    
    @RequestMapping(value = "/district/tree/storage/{parentId}", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getDistrictTreeWithStorageByParentId(Model model,@PathVariable String parentId) {
    	List<RegionStorageTree> regionStorageList = regionService.selectDistrictTreeWithStorageByParentId(Integer.parseInt(parentId));
      	 if(regionStorageList != null){
      		 model.addAttribute("districtTreeStorageList", regionStorageList);	
        	 model.addAttribute("s", 1);	
         	 model.addAttribute("m", "查询成功!");	
    	 }else{
    		 model.addAttribute("s", 0);	
         	 model.addAttribute("m", "查询失败!");
    	 }
		
        return model;
    }
    
    @RequestMapping(value = "/street/storage/{parentId}", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model getStreetWithStorageByParentId(Model model,@PathVariable String parentId) {
    	 Region region = new Region();
    	 region.setParentId(Integer.parseInt(parentId));
    	List<RegionStorage> regionStorageList = regionService.selectStreetWithStorage(region);
		model.addAttribute("regionStorageList", regionStorageList);	
        return model;
    }
    
    @RequestMapping(value = "/city/{cityId}/enabled", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model cityEnable(Model model,@PathVariable String cityId) {
    	 Region region = new Region();
    	 region.setId(Integer.parseInt(cityId));
    	 region.setStatus(RegionStatus.ENABLE);
    		 
    	 int success = regionService.updateByPrimaryKey(region);
    	 if(success == 1){
	    	 model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "启用成功!");	
    	 }else{
    		 model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "启用失败!");
    	 }
        return model;

    }
    
    @RequestMapping(value = "/city/{cityId}/disabled", method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model cityDisabled(Model model,@PathVariable String cityId) {
    	StorageDistrict storageDistrict = new StorageDistrict();
    	storageDistrict.setCityId(Integer.parseInt(cityId));
    	List<StorageDistrict> storageDistrictList = storageDistrictService.selectStorageDistrict(storageDistrict);
    	if(storageDistrictList != null && storageDistrictList.size() > 0){
    		 model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "城市被现用库房覆盖,无法禁用!");
	     	 return model;
    	}
    	int areaStreetSize = areaStreetService.selectAreaStreetByCityId(Integer.parseInt(cityId));
    	if(areaStreetSize > 0 ){
   		     model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "城市被现有合伙人覆盖,无法禁用!");
	     	 return model;
   	    }
    	
    	 Region region = new Region();
    	 region.setId(Integer.parseInt(cityId));
    	 region.setStatus(RegionStatus.DISABLE);
    	 int success = regionService.updateByPrimaryKey(region);
    	 if(success == 1){
	    	 model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "禁用成功!");	
    	 }else{
    		 model.addAttribute("s", 0);	
	     	 model.addAttribute("m", "禁用失败!");
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

