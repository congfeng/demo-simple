/**
 * 
 */
package com.cf.code.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.cf.code.common.DateUtil;
import com.cf.code.common.StringUtil;
import com.cf.code.core.exception.BusinessException;
import com.cf.code.core.exception.MsgSendException;
import com.cf.code.core.net.HttpPostMsgSender;
import com.cf.code.entity.Demo;
import com.cf.code.entity.Profile;
import com.cf.code.service.DemoService;
import com.cf.code.web.access.AccessVerifier;

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
	
	@RequestMapping(value = {""}, method = { RequestMethod.GET})
	@ResponseBody
    public List<Demo> list(@RequestParam(required = false) String timeStr) {
		Date time = null;
		if(StringUtil.isNullOrEmpty(timeStr)){
			time = DateUtil.toParse(timeStr);
		}
        return demoService.query(time);
    }
	
	@RequestMapping(value = {""}, method = { RequestMethod.POST})
	@ResponseBody
    public Model create(@RequestParam(required = true) String name,Model model) {
		Integer id = this.demoService.insert(name);
        model.addAttribute("create-id", id);  
        return model;
    }

	@RequestMapping(value = {"/{id}"}, method = { RequestMethod.GET})
	@ResponseBody
    public Demo get(@PathVariable Integer id) {
		log.info("DemoController["+id+"]-"+this);
		Demo demo1 = this.demoService.find(id);
        return demo1;
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
			this.demoService.update(id, name);
			 model.addAttribute("update-id", id+"");  
		} catch (BusinessException e) {
			 model.addAttribute("update-id-error", e.getMessage());
		}
        return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/testProfile"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Profile testProfile(@RequestParam(required = false)Profile profile,HttpSession session){
		return profile;
    }
	
	@Autowired  
	private HttpSession session1;
	
	@AccessVerifier(check=false)
	@RequestMapping(value = {"/testProfile2"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Profile testProfile2(@RequestParam(required = false)Profile profile,HttpSession session){
		return profile;
    }
	
	@RequestMapping(value = {"/testAsync"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model testAsync(HttpSession session,Model model) {
		this.demoService.it4Async();
		System.out.println("--------testAsync-------");
		HttpSession session2 = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		System.out.println(session.getId()+","+session1.getId()+","+session2.getId());
		System.out.println(session.getAttribute("profile"));
		System.out.println(session1.getAttribute("profile"));
		System.out.println(session2.getAttribute("profile"));
		return model;
    }
	
	@RequestMapping(value = {"/upload"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model upload(Model model,
    		@RequestParam(value = "file", required = false) Object fileObj,
    		@RequestParam(required = false) Integer sign) throws IllegalStateException, IOException {
		MultipartFile file = null;
		if(fileObj instanceof MultipartFile){
			file = (MultipartFile)fileObj;
			file.transferTo(new File("E:\\apache-tomcat-7.0.53-windows-x64\\image\\"+file.getOriginalFilename()));
		}
		log.info(file);
		log.info(sign);
        return model;
    }
	
	@RequestMapping(value = {"/crossdomain/convert"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public String crossdomainConvert(Model model,HttpServletRequest request,
    		@RequestParam(value = "remoteUrl", required = true) String remoteUrl) throws MsgSendException{
		HttpPostMsgSender sender = new HttpPostMsgSender();
        return sender.send(remoteUrl, null);
    }
	
}
