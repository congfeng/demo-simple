/**
 * 
 */
package com.cf.code.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.code.core.MyContextLoader;
import com.cf.code.core.exception.BusinessException;
import com.cf.code.dao.MenuDao;
import com.cf.code.dao.MsgDao;
import com.cf.code.dao.UserDao;
import com.cf.code.entity.Profile;
import com.cf.code.entity.User;
import com.cf.code.service.ImService;
import com.cf.code.web.access.AccessVerifier;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {
	
	@Resource(name = "userDaoRead")
	UserDao userDaoRead;
	
	@Resource(name = "menuDaoRead")
	MenuDao menuDaoRead;
	
	@Resource(name = "msgDaoRead")
	MsgDao msgDaoRead;
	
	@Resource(name = "imService")
	ImService imService;
	
	@RequestMapping(value = {"login"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Profile login(Model model,HttpSession session,
			@RequestParam(required = true) String username,
			@RequestParam(required = true) String password) throws BusinessException{
		User user = userDaoRead.find(username);
		if(user == null){
			throw new BusinessException("用户不存在");
		}
		if(!user.getPassword().equals(password)){
			throw new BusinessException("用户或密码错误");
		}
		List<String> menus = menuDaoRead.query(user.getId());
		if(menus.isEmpty()){
			throw new BusinessException("用户无访问权限");
		}
		Profile profile = new Profile(session.getId(), user.getId(), user.getUsername(),menus);
		session.setAttribute("profile", profile);
        return profile;
    }
	
	@RequestMapping(value = {"logout"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public void logout(HttpSession session){
		session.removeAttribute("profile");
	}
	
	@AccessVerifier
	@RequestMapping(value = {""}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model init(@RequestParam(required = false)Profile profile,HttpSession session,Model model) {
		int msgCount = this.msgDaoRead.queryCount(null, null, null, 0, null, null);
		model.addAttribute("profile",profile);
		model.addAttribute("imAddress",MyContextLoader.getImAddress());
		model.addAttribute("msgCount",msgCount);
        return model;
    }
	
}
