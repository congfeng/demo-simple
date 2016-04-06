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
import com.cf.code.core.exception.BusinessException;
import com.cf.code.core.net.EmailMsgSender;
import com.cf.code.core.net.EmailMsgSender.EmailSendMsgType;
import com.cf.code.core.net.EmailMsgSender.EmailTargetDataType;
import com.cf.code.dao.MsgDao;
import com.cf.code.dao.MsgReceiverDao;
import com.cf.code.entity.Msg;
import com.cf.code.entity.MsgReceiver;
import com.cf.code.entity.Profile;
import com.cf.code.service.ImService;
import com.cf.code.service.MsgService;
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
	
	@Resource(name = "msgService")
	MsgService msgService;
	
	@Resource(name = "imService")
	ImService imService;
	
	@AccessVerifier
	@RequestMapping(value = {"/list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model list(@RequestParam(required = false)Profile profile,HttpSession session,
			Model model,
			@RequestParam(required = false) Integer pageNo,
			@RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer sendStatus,
			@RequestParam(required = false) Integer replyStatus,
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
		int count = this.msgDaoRead.queryCount(title, userName,sendStatus,replyStatus, createTimeStart, createTimeEnd);
		pager.setCount(count);
		List<Msg> msgs = this.msgDaoRead.query(title, userName,sendStatus,replyStatus, createTimeStart, createTimeEnd,
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
    public Model add(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) String userName,
    		@RequestParam(required = true) String userEmail,
    		@RequestParam(required = false) String title,
    		@RequestParam(required = false) String content){
		Msg msg = new Msg();
		msg.setUserName(userName);
		msg.setUserEmail(userEmail);
		msg.setTitle(title);
		msg.setContent(content);
		msg.setSendStatus(0);
		msg.setReplyStatus(0);
		this.msgDao.insert(msg);
		msgService.sender(msg);
		imService.pushMsgCount(true);
		return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/receiver/add"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model receiverAdd(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) String address){
		MsgReceiver msgReceiver = new MsgReceiver();
		msgReceiver.setAddress(address);
		this.msgReceiverDao.insert(msgReceiver);
		return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/receiver/delete"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model receiverDelete(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) Integer id){
		this.msgReceiverDao.delete(id);
		return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"sender"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model sender(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
			@RequestParam(required = true) Integer id,
			@RequestParam(required = true) String content) throws Exception{
		Msg msg = this.msgDaoRead.find(id);
		if(StringUtil.isNullOrEmpty(msg.getUserEmail())){
			throw new BusinessException("用户邮箱为空");
		}
		content = content.replaceAll("\r\n|\r|\n", "<br>").replaceAll(" ", "&nbsp;");
		EmailMsgSender msgSender = new EmailMsgSender("channel_warning@126.com","12345679","用户信件");
		try {
			EmailTargetDataType emailtarget = new EmailTargetDataType(msg.getTitle()+"【回复】", msg.getUserEmail());
			EmailSendMsgType emailMsg = new EmailSendMsgType(content,true);
			msgSender.send(emailtarget, emailMsg);
			if(this.msgDao.updateReplyStatus(id, 0, 1)){
				imService.pushMsgCount(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.msgDao.updateReplyStatus(id, 0, 2);
			throw new BusinessException("发送失败："+e.getMessage());
		}
		return model;
    }
	
}
