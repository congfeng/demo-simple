/**
 * 
 */
package com.jisheng.peisong.web.test;

import com.jisheng.peisong.util.Constant;
import com.jisheng.peisong.util.EncryptData;

import sun.misc.BASE64Encoder;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class LoginTestCase {

	public static void main(String[] args) {
		String password = "admin";
		EncryptData ed = new EncryptData(Constant.PASSWORD_KEY);
	 	BASE64Encoder enc=new BASE64Encoder();
	 	byte[] bt = ed.encrypt(password);
	    password = enc.encode(bt);
	    System.out.println(password);
	}

}
