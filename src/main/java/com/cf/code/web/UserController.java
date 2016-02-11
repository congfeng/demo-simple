/**
 * 
 */
package com.cf.code.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public void resetpassword(HttpServletRequest request,@RequestParam(required = false)Profile profile,
    		@RequestParam(required = true) String newpassword) throws BusinessException{
		this.userDao.updatePassword(profile.getRelatedId(), newpassword);
    }
	
}
