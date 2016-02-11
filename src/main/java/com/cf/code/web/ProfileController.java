/**
 * 
 */
package com.cf.code.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.code.common.CodeCreator;
import com.cf.code.common.StringUtil;
import com.cf.code.common.WebUtil;
import com.cf.code.core.exception.BusinessException;
import com.cf.code.dao.MenuDao;
import com.cf.code.dao.UserDao;
import com.cf.code.entity.Profile;
import com.cf.code.entity.User;
import com.cf.code.service.SessionService;

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
	
	@Resource(name = "sessionService")
	SessionService sessionService;
	
	@RequestMapping(value = {"login"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Profile login(Model model,HttpServletResponse response,
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
		String token = CodeCreator.generateCharArr(32);
		WebUtil.writeCookie(response, "token", token);
		Profile profile = new Profile(token, user.getId(), user.getUsername(),menus);
		sessionService.saveProfile(token, profile);
        return profile;
    }
	
	@RequestMapping(value = {"logout"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public void logout(HttpServletRequest request,HttpServletResponse response,Model model,
    		@RequestParam(required = false) String token){
		WebUtil.deleteCookie(request, response, "token");
		if(!StringUtil.isNullOrEmpty(token)){
			sessionService.delProfile(token);	
		}
    }
	
}
