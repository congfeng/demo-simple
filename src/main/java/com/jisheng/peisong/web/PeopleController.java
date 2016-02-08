package com.jisheng.peisong.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.entity.People;
import com.jisheng.peisong.entity.Storage;
import com.jisheng.peisong.exception.CacheException;
import com.jisheng.peisong.service.CacheService;
import com.jisheng.peisong.service.StorageService;
import com.jisheng.peisong.util.CodeCreator;
import com.jisheng.peisong.util.Constant;
import com.jisheng.peisong.util.EncryptData;
import com.jisheng.peisong.util.SessionUtil;
import com.jisheng.peisong.util.StringUtil;
import com.jisheng.peisong.vo.PeopleModuleTree;
import com.jisheng.peisong.vo.PeopleProfile;
import com.jisheng.peisong.web.service.SessionService;
import com.suniot.entity.system.User;
import com.suniot.people.dubbo.PeopleService;

import redis.clients.jedis.Jedis;
import sun.misc.BASE64Encoder;
/**
 * @Filename: PeopleModuleController.java
 * @version : 1.0
 * @author  : guiren 
 * @email   : juguiren@91nongye.com
 * @date    : 2015年8月27日
 */
@Controller
@RequestMapping("/people")
public class PeopleController {
	
	private static Logger log = LogManager.getLogger(PeopleController.class);
	
	@Resource(name = "cacheService")
	CacheService cacheService;
	
	@Resource(name = "storageService")
	StorageService storageService;
	
	@Resource(name = "peopleService")
	PeopleService peopleService;
	
	@Resource(name = "localPeopleService")
	com.jisheng.peisong.service.PeopleService localPeopleService;
	
	@Resource(name = "SessionService")
	SessionService sessionService;
	
	@RequestMapping(value = {"/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model listByName(Model model,@RequestParam(required = true) String peopleName) {
		List<User> userlist = peopleService.searchUsers(peopleName);
		if(userlist != null){
			model.addAttribute("userlist", userlist);
			model.addAttribute("s", 1);	
	     	model.addAttribute("m", "员工查询成功!");	
		}else{
			model.addAttribute("s", 0);	
	     	model.addAttribute("m", "员工查询失败!");	
		}
		return model;
    }
	
	@RequestMapping(value = {"/login"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model peopleLogin(Model model,HttpSession session,HttpServletRequest request,HttpServletResponse response,@RequestParam(required = true) String phone,@RequestParam(required = true) String password) {
		
		if("admin".equals(phone.trim().toLowerCase())){
			EncryptData ed = new EncryptData(Constant.PASSWORD_KEY);
	   	 	BASE64Encoder enc=new BASE64Encoder();
	   	 	byte[] bt = ed.encrypt(password);
	   	    password = enc.encode(bt);
	   	    
			People people = localPeopleService.localPeopleLogin(phone, password);
			if(people != null){
				PeopleProfile peopleProfile = new PeopleProfile();
				
				
				List<PeopleModuleTree> peopleModuleTreeList = localPeopleService.selectModuleWithPeopleTree("0");
				List<Storage> peopleStorageList = storageService.selectAll();
				
				peopleProfile.setStorageList(peopleStorageList);
				peopleProfile.setPeopleId(String.valueOf(people.getId()));
				peopleProfile.setPeopleName(people.getPeopleName());
				peopleProfile.setPeoplePhone("");
				saveLoginedPeople(peopleProfile, response);
				
//				PeopleProfile pp = getLoginedPeople(request);
//				System.out.println("当前登录用户:"+pp.getPeopleName());
				 model.addAttribute("peopleProfile", peopleProfile);
				 model.addAttribute("peopleModuleTree", peopleModuleTreeList);
				 model.addAttribute("peopleStorage", peopleStorageList);
			     model.addAttribute("s", 1);	
		     	 model.addAttribute("m", "员工登录成功!");
			}else{
				 model.addAttribute("s", 0);	
			     model.addAttribute("m", "员工登录失败!");	
			}
			return model;
		}
		
		User user = peopleService.login(phone, password);
		if(user != null){
//			session.setAttribute(Constant.LOGINED_PEOPLE, user);
			
			PeopleProfile peopleProfile = new PeopleProfile();
			
			
			List<PeopleModuleTree> peopleModuleTreeList = localPeopleService.selectPeopleModuleTree(String.valueOf(user.getUser_id()));
			List<Storage> peopleStorageList = storageService.selectByPeopleId(String.valueOf(user.getUser_id()));
			
			peopleProfile.setStorageList(peopleStorageList);
			peopleProfile.setPeopleId(String.valueOf(user.getUser_id()));
			peopleProfile.setPeopleName(user.getReal_name());
			peopleProfile.setPeoplePhone(user.getPhone());
			
			saveLoginedPeople(peopleProfile, response);
			
//			PeopleProfile pp = getLoginedPeople(request);
//			System.out.println("当前登录用户:"+pp.getPeopleName());
			  model.addAttribute("peopleProfile", peopleProfile);
			 model.addAttribute("peopleModuleTree", peopleModuleTreeList);
			 model.addAttribute("peopleStorage", peopleStorageList);
		     model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "员工登录成功!");	
		  }else{
			 model.addAttribute("s", 0);	
		     model.addAttribute("m", "员工登录失败!");	
		  }
		return model;
    }
	
	@RequestMapping(value = {"/logout"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model peopleLogout(Model model,HttpSession session,HttpServletRequest request,HttpServletResponse response) {
		   
		    Jedis jedis = null;
		    
            try{
            	String token = SessionUtil.readCookies(request, "peopleToken");
            	sessionService.deletePeopleProfile(token);
            	SessionUtil.deleteCookies(request, response, "peopleToken");
            	
           	     model.addAttribute("s", 1);	
		     	 model.addAttribute("m", "员工退出登录成功!");
		     	 
            	if(token != null && !"".equals(token)){
            		jedis = cacheService.getJedis();
                	jedis.del(token.getBytes());
            		
            	}

            }catch(CacheException ce){
                ce.printStackTrace();
            }catch(Exception e){
            	  model.addAttribute("s", 0);	
			      model.addAttribute("m", "员工退出登录失败!");	
			     e.printStackTrace();
            }finally{
    			cacheService.releaseJedis(jedis);
    		}
		
		return model;
    }
	@RequestMapping(value = {"/auth"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model authorization(Model model,@RequestParam(required = true) String peopleId,@RequestParam(required = true) String peopleName,@RequestParam(required = false) String storageIds,@RequestParam(required = false) String moduleIds) {
		int s  = localPeopleService.txAuthorization(peopleId, peopleName, storageIds, moduleIds);
		if(s == 1){
		     model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "员工授权成功!");	
		  }else{
			 model.addAttribute("s", 0);	
		     model.addAttribute("m", "员工授权失败!");	
		  }
		return model;
    }
	@RequestMapping(value = {"/module/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model moduleTree(Model model,HttpSession session) {
		User user = (User)session.getAttribute(Constant.LOGINED_PEOPLE);
		List<PeopleModuleTree> peopleModuleTreeList = localPeopleService.selectPeopleModuleTree("1");
		if(peopleModuleTreeList != null ){
			 model.addAttribute("peopleModuleTree", peopleModuleTreeList);
		     model.addAttribute("s", 1);	
	     	 model.addAttribute("m", "员工授权功能查询成功!");	
		  }else{
			 model.addAttribute("s", 0);	
		     model.addAttribute("m", "员工授权功能查询失败!");	
		  }
		return model;
    }
	
	//保存登录名
	private void saveLoginedPeople(PeopleProfile peopleProfile,HttpServletResponse response){
		String token = CodeCreator.generateCharArr(32);
		Jedis jedis = null;
		try {
			jedis = cacheService.getJedis();
			SessionUtil.setLoginedPeople(jedis, response, token, "peopleToken", peopleProfile);
		} catch (CacheException e) {
			log.warn(e.getMessage());
		}finally{
			cacheService.releaseJedis(jedis);
		}
		SessionUtil.writeCookies(response, token, "peopleToken");
		sessionService.addPeopleProfile(token, peopleProfile);
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

