/**
 * 
 */
package com.cf.code.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cf.code.common.DateUtil;
import com.cf.code.common.Pager;
import com.cf.code.common.StringUtil;
import com.cf.code.dao.ProductDao;
import com.cf.code.entity.Product;
import com.cf.code.entity.Profile;
import com.cf.code.web.access.AccessVerifier;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Controller
@RequestMapping("/product")
public class ProductController {
	
	@Resource(name = "productDao")
	ProductDao productDao;
	
	@Resource(name = "productDaoRead")
	ProductDao productDaoRead;
	
	@Resource(name = "pom.image.folder")
	String ImageFolder;
	
	@AccessVerifier
	@RequestMapping(value = {"list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model list(@RequestParam(required = false)Profile profile,HttpSession session,
			Model model,
			@RequestParam(required = false) Integer pageNo,
			@RequestParam(required = false) Integer ptype,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String sku,
			@RequestParam(required = false) String createTimeStartText,
    		@RequestParam(required = false) String createTimeEndText){
		if(StringUtil.isNullOrEmpty(name)){
			name = null;
		}
		if(StringUtil.isNullOrEmpty(sku)){
			sku = null;
		}
		Date createTimeStart = null;
		if(!StringUtil.isNullOrEmpty(createTimeStartText)){
			createTimeStart = DateUtil.toParse(createTimeStartText);
		}
		Date createTimeEnd = null;
		if(!StringUtil.isNullOrEmpty(createTimeEndText)){
			createTimeEnd = DateUtil.toParse(createTimeEndText);
		}
		if(pageNo == null) pageNo = 1;
		Pager pager = new Pager();
		int userCount = this.productDaoRead.queryCount(ptype, name, sku, createTimeStart, createTimeEnd);
		pager.setCount(userCount);
		pager.setPageNo(pageNo);
		List<Product> products = this.productDaoRead.query(ptype, name, sku, createTimeStart, createTimeEnd,
				pager.getStartIndex(), pager.getPageSize()); 
		model.addAttribute("products", products);   
		model.addAttribute("pager", pager);
        return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/find"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Product find(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer id){
		return this.productDaoRead.find(id);
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/add"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public void add(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer ptype,
    		@RequestParam(required = true) String name,
    		@RequestParam(required = false) String sku,
    		@RequestParam(value = "image", required = false) Object imageObj) throws IllegalStateException, IOException {
		String image = null;
		if(imageObj instanceof MultipartFile){
			MultipartFile imageFile = (MultipartFile)imageObj;
			image = UUID.randomUUID().toString()+"."+getExtName(imageFile.getOriginalFilename());
			imageFile.transferTo(new File(ImageFolder+image));
		}
		Product product = new Product();
		product.setProductType(ptype);
		product.setName(name);
		product.setSku(sku);
		product.setImage(image);
		this.productDao.insert(product);
    }
	
	@AccessVerifier
	@RequestMapping(value = {"delete"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void delete(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer id) throws IOException{
		Product p = this.productDaoRead.find(id);
		if(p == null){
			return ;
		}
		if(this.productDao.delete(id)){
			FileUtils.forceDelete(new File(ImageFolder+p.getImage()));
		}
    }
	
	@AccessVerifier
	@RequestMapping(value = {"update"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void update(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer id,
    		@RequestParam(required = false) String name,
    		@RequestParam(required = false) String sku,
    		@RequestParam(required = false) String imageold,
    		@RequestParam(value = "image", required = false) Object imageObj) throws IllegalStateException, IOException{
		if(StringUtil.isNullOrEmpty(name)){
			name = null;
		}
		if(StringUtil.isNullOrEmpty(sku)){
			sku = null;
		}
		String image = null;
		if(imageObj instanceof MultipartFile){
			MultipartFile imageFile = (MultipartFile)imageObj;
			image = UUID.randomUUID().toString()+"."+getExtName(imageFile.getOriginalFilename());
			imageFile.transferTo(new File(ImageFolder+image));
		}
		boolean b = this.productDao.update(id, name, sku, image);
		if(b&&image != null){
			FileUtils.forceDelete(new File(ImageFolder+imageold));
		}
    }
	
	private String getExtName(String fileName){
		return fileName.substring(fileName.lastIndexOf(".")+1);
	}
}
