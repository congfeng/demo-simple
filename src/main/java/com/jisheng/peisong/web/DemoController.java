/**
 * 
 */
package com.jisheng.peisong.web;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jisheng.peisong.entity.Demo;
import com.jisheng.peisong.entity.enums.DemoType;
import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.exception.CacheException;
import com.jisheng.peisong.service.CacheService;
import com.jisheng.peisong.service.DeliveryService;
import com.jisheng.peisong.service.DemoService;
import com.jisheng.peisong.util.Constant;
import com.jisheng.peisong.util.DateUtil;
import com.jisheng.peisong.util.StringUtil;
import com.jisheng.peisong.vo.PeopleProfile;
import com.jisheng.peisong.web.service.AsyncService;
import com.jisheng.peisong.web.service.SessionService;
import com.jisheng.peisong.web.session.SessionCheckInject;

import redis.clients.jedis.Jedis;

/**
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
	
	private static Logger log = LogManager.getLogger(DemoController.class);
	
	@Resource(name = "demoService")
	DemoService demoService;
	
	@Resource(name = "demoService_dubbo")
	DemoService demoService_dubbo;
	
//	@Resource(name = "redisTemplate")
	RedisTemplate redisTemplate;
	
	@Resource(name = "cacheService")
	CacheService cacheService;
	
	@Resource(name = "deliveryService_dubbo")
	DeliveryService deliveryService;
	
	@Resource(name = "AsyncService")
	AsyncService asyncService;
	
	@Resource(name = "SessionService")
	SessionService sessionService;
	
	@RequestMapping(value = {""}, method = { RequestMethod.GET})
	@ResponseBody
    public Model list(@RequestParam(required = false) String timeStr,Model model) {
		Date time = null;
		if(StringUtil.isNullOrEmpty(timeStr)){
			time = DateUtil.toParse(timeStr);
		}
//		成功
		model.addAttribute(Constant.TransmitField.Status, 1);
//		失败
		model.addAttribute(Constant.TransmitField.Status, 0);
		model.addAttribute(Constant.TransmitField.Msg, "失败提示信息");
		
        model.addAttribute("demoService", demoService.query(time) + "");  
        model.addAttribute("demoService_dubbo", demoService_dubbo.query(time)+"");
        return model;
    }
	
	@RequestMapping(value = {""}, method = { RequestMethod.POST})
	@ResponseBody
    public Model create(@RequestParam(required = true) String name,Model model) {
		Integer id1 = this.demoService.insert(name);
		Integer id2 = this.demoService_dubbo.insert("dubbo:"+name);
        model.addAttribute("create-id", id1+","+id2);  
        return model;
    }

	@RequestMapping(value = {"/{id}"}, method = { RequestMethod.GET})
	@ResponseBody
    public Model get(@PathVariable Integer id,Model model) {
		log.info("DemoController["+id+"]-"+this);
		Demo demo1 = this.demoService.find(id);
		Demo demo2 = this.demoService_dubbo.find(id);
        model.addAttribute("get-id-local", StringUtil.toJson(demo1));
        model.addAttribute("get-id-dubbo", StringUtil.toJson(demo2));
        return model;
    }
	
	@RequestMapping(value = {"/{id}"}, method = { RequestMethod.DELETE})
	@ResponseBody
    public Model delete(@PathVariable Integer id,Model model) {
		boolean b = this.demoService.delete(id);
        model.addAttribute("del-id", b+"");  
        return model;
    }
	
	@RequestMapping(value = {"/{id}"}, method = { RequestMethod.PUT})
	@ResponseBody
    public Model update(@PathVariable Integer id,@RequestParam(required = true) String name,Model model) {
		try {
			this.demoService_dubbo.update(id, name);
			 model.addAttribute("update-id", id+"");  
		} catch (BusinessException e) {
			 model.addAttribute("update-id-error", e.getMessage());
		}
        return model;
    }
	
	@RequestMapping(value = {"/txUpdate"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model txUpdate(@RequestParam(required = true) Integer sign,Model model) {
		try {
			this.demoService_dubbo.txUpdate(sign,DemoType.TypeOne,null);
			 model.addAttribute("txUpdate-sign", sign+"");  
		} catch (Exception e) {
			 model.addAttribute("txUpdate-sign-error", e.getMessage());
		}
        return model;
    }
	
	@RequestMapping(value = {"/testSrpingRedis"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model testSrpingRedis(Model model) {
		ValueOperations<String,Demo> valueOper = this.redisTemplate.opsForValue();
		Demo demo = new Demo();
		demo.setName("丛峰");
		valueOper.set("demo", demo);
		model.addAttribute("testSrpingRedis",StringUtil.toJson(valueOper.get("demo")));
        return model;
    }
	
	@RequestMapping(value = {"/testRedis"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model testJedis(Model model,@RequestParam(required = false) String sign) throws CacheException {
		this.cacheService.setValue("username", "缓存"+sign);
		model.addAttribute("testRedis",this.cacheService.getValue("username"));
        return model;
    }
	
	@RequestMapping(value = {"/empty"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model empty(Model model,@RequestParam(required = false) String sign) throws BusinessException {
		Jedis jedis = null;
		long begin = 0;
		try {
			begin = System.currentTimeMillis();
			jedis = cacheService.getJedis();
			log.info("----获取jedis用时：" + (System.currentTimeMillis() - begin));
			begin = System.currentTimeMillis();
		}catch (CacheException e) {
			throw new BusinessException("缓存异常："+e.getMessage());
		}finally{
			begin = System.currentTimeMillis();
			cacheService.releaseJedis(jedis);
			log.info("----释放jedis用时：" + (System.currentTimeMillis() - begin));
		}
        return model;
    }
	
	@SessionCheckInject
	@RequestMapping(value = {"/testProfile"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model testProfile(HttpServletRequest request,@RequestParam(required = false)PeopleProfile profile,
    		Model model){
		model.addAttribute("empty","测试");
		model.addAttribute("PeopleName",profile.getPeopleName());
		model.addAttribute("StorageList",profile.getStorageList());
        return model;
    }
	
	@RequestMapping(value = {"test/dubbo"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model testDubbo(Model model,
    		@RequestParam(required = false) Integer orderId,
    		@RequestParam(required = false) String goodsSn) throws BusinessException {
		String info = this.deliveryService.dubboTest(orderId,goodsSn);
		model.addAttribute("info", info);
        return model;
    }
	
	@RequestMapping(value = {"asyncService"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model asyncService(Model model) throws BusinessException {
		log.info("----------asyncService------");
		this.asyncService.test();
		model.addAttribute("info", this.asyncService);
        return model;
    }
	
	
}
