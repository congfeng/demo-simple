/**
 * 
 */
package com.cf.code.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import com.cf.code.dao.UserDao;
import com.cf.code.entity.Profile;
import com.cf.code.entity.User;
import com.cf.code.web.access.AccessVerifier;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Resource(name = "userDao")
	UserDao userDao;
	
	@Resource(name = "userDaoRead")
	UserDao userDaoRead;

	@RequestMapping(value = {"register"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public void register(Model model,HttpServletResponse response,
			@RequestParam(required = true) String username,
			@RequestParam(required = true) String password) throws BusinessException{
		if(userDaoRead.find(username) != null){
			throw new BusinessException("用户已存在，不能重复注册");
		}
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		this.userDao.insert(user);
    }
	
	@AccessVerifier
	@RequestMapping(value = {"resetpassword"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void resetpassword(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) String newpassword){
		this.userDao.updatePassword(profile.getRelatedId(), newpassword);
    }
	
	@AccessVerifier
	@RequestMapping(value = {"list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model list(@RequestParam(required = false)Profile profile,HttpSession session,
			Model model,
			@RequestParam(required = false) Integer pageNo,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String createTimeStartText,
    		@RequestParam(required = false) String createTimeEndText){
		if(StringUtil.isNullOrEmpty(username)){
			username = null;
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
		int userCount = this.userDaoRead.queryCount(username,createTimeStart,createTimeEnd);
		pager.setCount(userCount);
		pager.setPageNo(pageNo);
		List<User> users = this.userDaoRead.query(username,createTimeStart,createTimeEnd,pager.getStartIndex(), pager.getPageSize()); 
		model.addAttribute("users", users);   
		model.addAttribute("pager", pager);
        return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"export"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void export(@RequestParam(required = false)Profile profile,HttpSession session,
			Model model,HttpServletResponse response,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String createTimeStartText,
    		@RequestParam(required = false) String createTimeEndText
			) throws IOException{
		if(StringUtil.isNullOrEmpty(username)){
			username = null;
		}
		Date createTimeStart = null;
		if(!StringUtil.isNullOrEmpty(createTimeStartText)){
			createTimeStart = DateUtil.toParse(createTimeStartText);
		}
		Date createTimeEnd = null;
		if(!StringUtil.isNullOrEmpty(createTimeEndText)){
			createTimeEnd = DateUtil.toParse(createTimeEndText);
		}
		List<User> users = this.userDaoRead.query(username,createTimeStart,createTimeEnd,0, 100000); 
		String filename = "user_list.xls";
    	Workbook wb = new HSSFWorkbook();
        Sheet s = wb.createSheet();
        s.setColumnWidth(0, 4800);
        s.setColumnWidth(1, 4800);
        s.setColumnWidth(2, 4800);
        s.setColumnWidth(3, 4800);
        Row titleRow = s.createRow(0);
        titleRow.createCell(0).setCellValue("ID");
        titleRow.createCell(1).setCellValue("用户名");
        titleRow.createCell(2).setCellValue("密码");
        titleRow.createCell(3).setCellValue("创建时间");
        for (int i = 0; i < users.size(); i++) {
        	User user = users.get(i);
            Row dataRow = s.createRow(i + 1);
            dataRow.createCell(0).setCellValue(user.getId()+"");
            dataRow.createCell(1).setCellValue(user.getUsername());
            dataRow.createCell(2).setCellValue(user.getPassword());
            dataRow.createCell(3).setCellValue(user.getCreateTimeFormat());
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        wb.write(bos);
        bos.flush();
        byte[] bs = bos.toByteArray();
		OutputStream os = response.getOutputStream();
		try {
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename="+filename);
			response.setContentType("application/octet-stream; charset=utf-8");
			os.write(bs);
			os.flush();
		} finally {
			if (os != null) {
				os.close();
			}
		}
    }
	
}
