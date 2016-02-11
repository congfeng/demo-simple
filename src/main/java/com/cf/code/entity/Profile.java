/**
 * 
 */
package com.cf.code.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class Profile implements Serializable{

	public Profile(String token,Integer relatedId,String name,List<String> menus){
		this.token = token;
		this.relatedId = relatedId;
		this.name = name;
		this.menus = menus;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5082406578385226552L;

	private Integer relatedId;
 
	private String token;
	
    private String name;

    private List<String> menus;
    
	public Integer getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(Integer relatedId) {
		this.relatedId = relatedId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMenus() {
		return menus;
	}

	public void setMenus(List<String> menus) {
		this.menus = menus;
	}

		
}
