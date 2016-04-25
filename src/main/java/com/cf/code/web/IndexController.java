/**
 * 
 */
package com.cf.code.web;

import com.jfinal.core.Controller;

/**
 * @author congfeng
 *
 */
public class IndexController extends Controller{

	public void index(){
		this.render("/index.jsp");
	}
	
	public void sayHello(){
		String userName1 = this.getPara("userName");
		String userName2 = this.getAttr("userName");
		String sayHello = "Hello " + userName1 + userName2+"，welcome to JFinal world.";
		this.setAttr("sayHello", sayHello);
		this.renderJson("cf",sayHello);
	}
	
}
