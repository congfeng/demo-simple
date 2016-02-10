/**
 * 
 */
package com.cf.code.service;

import com.cf.code.entity.Profile;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public interface SessionService {

	public Profile getProfile(String token);
	
	public void saveProfile(String token,Profile profile);
	
	public void delProfile(String token);

}
