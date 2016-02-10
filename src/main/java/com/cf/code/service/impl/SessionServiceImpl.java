/**
 * 
 */
package com.cf.code.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.cf.code.entity.Profile;
import com.cf.code.service.SessionService;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class SessionServiceImpl implements SessionService{

	Map<String,Profile> profileMap = new HashMap<String,Profile>();
	
	Map<String,String> idMap = new HashMap<String,String>();
	
	public Profile getProfile(String token){
		Profile profile = profileMap.get(token); 
		return profile;
	}
	
	public void saveProfile(String token,Profile profile){
		if(profile == null){
			return ;
		}
		String oldToken = idMap.get(""+profile.getId());
		if(oldToken != null){
			profileMap.remove(oldToken);
		}
		idMap.put(""+profile.getId(), token);
		profileMap.put(token, profile);
	}

	public void delProfile(String token){
		Profile profile = profileMap.get(token);
		if(profile != null){
			idMap.remove(""+profile.getId());
		}
		profileMap.remove(token);
	}
}
