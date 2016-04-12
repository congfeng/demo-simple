/**
 * 
 */
package com.cf.code.web;

import java.io.IOException;
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
import com.cf.code.common.FileUtil;
import com.cf.code.common.Pager;
import com.cf.code.common.StringUtil;
import com.cf.code.core.MyContextLoader;
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

//	@AccessVerifier
	@RequestMapping(value = {"list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model list(@RequestParam(required = false)Profile profile,HttpSession session,
			Model model,
			@RequestParam(required = false) Integer pageNo,
			@RequestParam(required = false) Integer pageSize,
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
		pager.setPageNo(pageNo);
		if(pageSize != null){
			pager.setPageSize(pageSize);
		}
		int count = this.noticeDaoRead.queryCount(ntype, title, createTimeStart, createTimeEnd);
		pager.setCount(count);
		List<Notice> notices = this.noticeDaoRead.query(ntype, title, createTimeStart, createTimeEnd,
				pager.getStartIndex(), pager.getPageSize()); 
		model.addAttribute("notices", notices);   
		model.addAttribute("pager", pager);
		model.addAttribute("UploadBasePath", getUploadPath());
        return model;
    }
	
//	@AccessVerifier
	@RequestMapping(value = {"/find"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model find(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) Integer id){
		model.addAttribute("notice",this.noticeDaoRead.find(id));
		model.addAttribute("UploadBasePath", getUploadPath());
		return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/add"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model add(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) Integer ntype,
    		@RequestParam(required = true) String title,
    		@RequestParam(required = false) String content,
    		@RequestParam(value = "richText", required = false) Object richTextObj) throws IOException{
		String richText = FileUtil.uploadRichText(richTextObj, getUploadFolder(session), "richText");
		Notice notice = new Notice();
		notice.setNoticeType(ntype);
		notice.setTitle(title);
		notice.setContent(content);
		notice.setRichText(richText);
		this.noticeDao.insert(notice);
		return model;
    }

	@AccessVerifier
	@RequestMapping(value = {"delete"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model delete(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) Integer id) throws IOException{
		Notice n = this.noticeDaoRead.find(id);
		if(!this.noticeDao.delete(id)){
			return model;
		}
		if(!StringUtil.isNullOrEmpty(n.getRichText())){
			FileUtil.deleteFile(getUploadFolder(session)+"/"+n.getRichText());
		}
		return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"update"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model update(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) Integer id,
    		@RequestParam(required = false) String title,
    		@RequestParam(required = false) String content,
    		@RequestParam(value = "richText", required = false) Object richTextObj) throws IOException{
		Notice n = this.noticeDaoRead.find(id);
		String richText = FileUtil.uploadRichText(richTextObj, getUploadFolder(session), "richText");
		boolean b = this.noticeDao.update(id, title, content, richText);
		if(b&&!StringUtil.isNullOrEmpty(n.getRichText())){
			FileUtil.deleteFile(getUploadFolder(session)+"/"+n.getRichText());
		}
		return model;
    }
	
	private String getUploadFolder(HttpSession session){
		if(!StringUtil.isNullOrEmpty(MyContextLoader.uploadFolder)){
			return MyContextLoader.uploadFolder;
		}
		return session.getServletContext().getRealPath("/")+"/upload";
	}
	
	private String getUploadPath(){
		if(!StringUtil.isNullOrEmpty(MyContextLoader.uploadPath)){
			return MyContextLoader.uploadPath;
		}
		return "/upload/";
	}
	
}
