/**
 * 
 */
package com.jisheng.peisong.web.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jisheng.peisong.exception.BusinessException;
import com.jisheng.peisong.exception.CacheException;
import com.jisheng.peisong.service.CacheService;
import com.jisheng.peisong.util.SessionUtil;
import com.jisheng.peisong.vo.PeopleProfile;

import redis.clients.jedis.Jedis;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Service("SessionService")
public class SessionService {

	private static Logger log = LogManager.getLogger(SessionService.class);
	
	Map<String,PeopleProfile> profileMap = new HashMap<String,PeopleProfile>();
	
	Map<String,String> idMap = new HashMap<String,String>();
	
	@Resource(name = "cacheService")
	CacheService cacheService;
	
	public PeopleProfile getPeopleProfile(String token) throws BusinessException{
		PeopleProfile profile = profileMap.get(token); 
		if(profile != null){
			return profile;
		}
		Jedis jedis = null;
		long begin = 0;
		try {
			begin = System.currentTimeMillis();
			jedis = cacheService.getJedis();
			log.info("获取jedis用时：" + (System.currentTimeMillis() - begin));
			begin = System.currentTimeMillis();
			profile = SessionUtil.getLoginedPeople(jedis, token);
			this.addPeopleProfile(token, profile);
			return profile;
		}catch (CacheException e) {
			throw new BusinessException("缓存异常："+e.getMessage());
		}finally{
			begin = System.currentTimeMillis();
			cacheService.releaseJedis(jedis);
			log.info("释放jedis用时：" + (System.currentTimeMillis() - begin));
		}
	}
	
	public void addPeopleProfile(String token,PeopleProfile profile){
		if(profile == null){
			return ;
		}
		String oldToken = idMap.get(profile.getPeopleId());
		if(oldToken != null){
			profileMap.remove(oldToken);
		}
		idMap.put(profile.getPeopleId(), token);
		profileMap.put(token, profile);
	}

	public void deletePeopleProfile(String token){
		PeopleProfile profile = profileMap.get(token);
		if(profile != null){
			idMap.remove(profile.getPeopleId());
		}
		profileMap.remove(token);
	}
}
