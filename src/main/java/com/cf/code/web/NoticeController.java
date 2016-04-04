/**
 * 
 */
package com.cf.code.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
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
	
	@Resource(name = "pom.upload.folder")
	String UploadFolder;
	
	@Resource(name = "pom.upload.path")
	String UploadPath;

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
		model.addAttribute("UploadBasePath", UploadPath+File.separator);
        return model;
    }
	
//	@AccessVerifier
	@RequestMapping(value = {"/find"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model find(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) Integer id){
		model.addAttribute("notice",this.noticeDaoRead.find(id));
		model.addAttribute("UploadBasePath", UploadPath+File.separator);
		return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/add"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public void add(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer ntype,
    		@RequestParam(required = true) String title,
    		@RequestParam(required = false) String content,
    		@RequestParam(value = "richText", required = false) Object richTextObj) throws IllegalStateException, IOException{
		String richText = FileUtil.uploadRichText(richTextObj, UploadFolder, "richText");
		Notice notice = new Notice();
		notice.setNoticeType(ntype);
		notice.setTitle(title);
		notice.setContent(content);
		notice.setRichText(richText);
		this.noticeDao.insert(notice);
    }

	@AccessVerifier
	@RequestMapping(value = {"delete"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void delete(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer id) throws IOException{
		Notice n = this.noticeDaoRead.find(id);
		if(!this.noticeDao.delete(id)){
			return ;
		}
		if(!StringUtil.isNullOrEmpty(n.getRichText())){
			FileUtils.forceDelete(new File(UploadFolder+File.separator+n.getRichText()));
		}
    }
	
	@AccessVerifier
	@RequestMapping(value = {"update"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void update(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer id,
    		@RequestParam(required = false) String title,
    		@RequestParam(required = false) String content,
    		@RequestParam(value = "richText", required = false) Object richTextObj) throws IllegalStateException, IOException{
		Notice n = this.noticeDaoRead.find(id);
		String richText = FileUtil.uploadRichText(richTextObj, UploadFolder, "richText");
		boolean b = this.noticeDao.update(id, title, content, richText);
		if(b&&!StringUtil.isNullOrEmpty(n.getRichText())){
			FileUtils.forceDelete(new File(UploadFolder+File.separator+n.getRichText()));
		}
    }
	
}
