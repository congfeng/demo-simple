/**
 * 
 */
package com.cf.code.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.code.entity.Profile;
import com.cf.code.web.access.AccessVerifier;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {
	
	
	@RequestMapping(value = {"login"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Profile login(Model model,HttpSession session,
			@RequestParam(required = true) String username,
			@RequestParam(required = true) String password){
		Profile profile = new Profile(session.getId(), null, null,null);
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
    public Profile init(@RequestParam(required = false)Profile profile,HttpSession session) {
        return profile;
    }
	
}
