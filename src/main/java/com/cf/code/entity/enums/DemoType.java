/**
 * 
 */
package com.cf.code.entity.enums;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public enum DemoType {

	TypeOne("1"),
	TypeTwo("2"),
	;
	
	private DemoType(String desc){
        this.desc = desc;
    };
	
	public final String desc;
	
}
