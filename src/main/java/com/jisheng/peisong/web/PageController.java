/**
 * 
 */
package com.jisheng.peisong.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
@Controller
@RequestMapping("/")
public class PageController {

	@RequestMapping(value={"/","/login.html"})
    public String index(){       
        return "login.html";
    }

}
