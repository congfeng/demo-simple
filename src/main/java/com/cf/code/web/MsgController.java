/**
 * 
 */
package com.cf.code.web;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.code.common.DateUtil;
import com.cf.code.common.Pager;
import com.cf.code.common.StringUtil;
import com.cf.code.dao.MsgDao;
import com.cf.code.dao.MsgReceiverDao;
import com.cf.code.entity.Msg;
import com.cf.code.entity.MsgReceiver;
import com.cf.code.entity.Profile;
import com.cf.code.web.access.AccessVerifier;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Controller
@RequestMapping("/msg")
public class MsgController {
	
	@Resource(name = "msgDao")
	MsgDao msgDao;
	
	@Resource(name = "msgDaoRead")
	MsgDao msgDaoRead;
	
	@Resource(name = "msgReceiverDao")
	MsgReceiverDao msgReceiverDao;
	
	@Resource(name = "msgReceiverDaoRead")
	MsgReceiverDao msgReceiverDaoRead;
	
	@AccessVerifier
	@RequestMapping(value = {"/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model list(@RequestParam(required = false)Profile profile,HttpSession session,
			Model model,
			@RequestParam(required = false) Integer pageNo,
			@RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) String userName,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String createTimeStartText,
    		@RequestParam(required = false) String createTimeEndText){
		if(StringUtil.isNullOrEmpty(userName)){
			userName = null;
		}
		if(StringUtil.isNullOrEmpty(title)){
			title = null;
		}
		Date createTimeStart = null;
		if(!StringUtil.isNullOrEmpty(createTimeStartText)){
			createTimeStart = DateUtil.toParse(createTimeStartText);
		}
		Date createTimeEnd = null;
		if(!StringUtil.isNullOrEmpty(createTimeEndText)){
			createTimeEnd = DateUtil.toParse(createTimeEndText);
		}
		if(pageNo == null) pageNo = 1;
		Pager pager = new Pager();
		pager.setPageNo(pageNo);
		if(pageSize != null){
			pager.setPageSize(pageSize);
		}
		int count = this.msgDaoRead.queryCount(title, userName, createTimeStart, createTimeEnd);
		pager.setCount(count);
		List<Msg> msgs = this.msgDaoRead.query(title, userName, createTimeStart, createTimeEnd,
				pager.getStartIndex(), pager.getPageSize()); 
		model.addAttribute("msgs", msgs);   
		model.addAttribute("pager", pager);
        return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/receiver/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model receiverList(@RequestParam(required = false)Profile profile,HttpSession session,
			Model model,
			@RequestParam(required = false) Integer pageNo,
			@RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) String createTimeStartText,
    		@RequestParam(required = false) String createTimeEndText){
		Date createTimeStart = null;
		if(!StringUtil.isNullOrEmpty(createTimeStartText)){
			createTimeStart = DateUtil.toParse(createTimeStartText);
		}
		Date createTimeEnd = null;
		if(!StringUtil.isNullOrEmpty(createTimeEndText)){
			createTimeEnd = DateUtil.toParse(createTimeEndText);
		}
		if(pageNo == null) pageNo = 1;
		Pager pager = new Pager();
		pager.setPageNo(pageNo);
		if(pageSize != null){
			pager.setPageSize(pageSize);
		}
		int count = this.msgReceiverDaoRead.queryCount(createTimeStart, createTimeEnd);
		pager.setCount(count);
		List<MsgReceiver> msgreceivers = this.msgReceiverDaoRead.query(createTimeStart, createTimeEnd, 
				pager.getStartIndex(), pager.getPageSize()); 
		model.addAttribute("msgreceivers", msgreceivers);   
		model.addAttribute("pager", pager);
        return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/find"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model find(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) Integer id){
		model.addAttribute("msg",this.msgDaoRead.find(id));
		return model;
    }
	
	@RequestMapping(value = {"/add"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public void add(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) String userName,
    		@RequestParam(required = true) String userEmail,
    		@RequestParam(required = false) String title,
    		@RequestParam(required = false) String content){
		Msg msg = new Msg();
		msg.setUserName(userName);
		msg.setUserEmail(userEmail);
		msg.setTitle(title);
		msg.setContent(content);
		this.msgDao.insert(msg);
		//TODO 异步执行以下代码
		List<MsgReceiver> msgreceivers = this.msgReceiverDaoRead.query(null, null, 0, 100);
		for(MsgReceiver msgReceiver:msgreceivers){
			//TODO 发送邮件	
		}
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/receiver/add"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public void receiverAdd(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) String address){
		MsgReceiver msgReceiver = new MsgReceiver();
		msgReceiver.setAddress(address);
		this.msgReceiverDao.insert(msgReceiver);
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/receiver/delete"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void receiverDelete(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer id){
		this.msgReceiverDao.delete(id);
    }
	
	@AccessVerifier
	@RequestMapping(value = {"sender"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void sender(@RequestParam(required = false)Profile profile,HttpSession session,
			@RequestParam(required = true) Integer id,
			@RequestParam(required = false) String content) throws Exception{
		Msg msg = this.msgDaoRead.find(id);
		if(StringUtil.isNullOrEmpty(msg.getUserEmail())){
			throw new Exception("用户邮箱为空");
		}
		//TODO 发送邮件
    }
}
