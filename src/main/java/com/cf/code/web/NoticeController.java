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
import com.cf.code.dao.NoticeDao;
import com.cf.code.entity.Notice;
import com.cf.code.entity.Profile;
import com.cf.code.web.access.AccessVerifier;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	@Resource(name = "noticeDao")
	NoticeDao noticeDao;
	
	@Resource(name = "noticeDaoRead")
	NoticeDao noticeDaoRead;

	@AccessVerifier
	@RequestMapping(value = {"list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model list(@RequestParam(required = false)Profile profile,HttpSession session,
			Model model,
			@RequestParam(required = false) Integer pageNo,
			@RequestParam(required = false) Integer ntype,
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String createTimeStartText,
    		@RequestParam(required = false) String createTimeEndText){
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
		int userCount = this.noticeDaoRead.queryCount(ntype, title, createTimeStart, createTimeEnd);
		pager.setCount(userCount);
		pager.setPageNo(pageNo);
		List<Notice> notices = this.noticeDaoRead.query(ntype, title, createTimeStart, createTimeEnd,
				pager.getStartIndex(), pager.getPageSize()); 
		model.addAttribute("notices", notices);   
		model.addAttribute("pager", pager);
        return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/find"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Notice find(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer id){
		return this.noticeDaoRead.find(id);
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/add"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public void add(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer ntype,
    		@RequestParam(required = true) String title,
    		@RequestParam(required = false) String content){
		Notice notice = new Notice();
		notice.setNoticeType(ntype);
		notice.setTitle(title);
		notice.setContent(content);
		this.noticeDao.insert(notice);
    }

	@AccessVerifier
	@RequestMapping(value = {"delete"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void delete(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer id){
		this.noticeDao.delete(id);
    }
	
	@AccessVerifier
	@RequestMapping(value = {"update"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void update(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer id,
    		@RequestParam(required = false) String title,
    		@RequestParam(required = false) String content){
		if(StringUtil.isNullOrEmpty(title)){
			title = null;
		}
		if(StringUtil.isNullOrEmpty(content)){
			content = null;
		}
		this.noticeDao.update(id, title, content);
    }
	
}
