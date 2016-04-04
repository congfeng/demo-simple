/**
 * 
 */
package com.cf.code.web;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cf.code.common.DateUtil;
import com.cf.code.common.FileUtil;
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
	
	@Resource(name = "pom.upload.folder")
	String UploadFolder;
	
	@Resource(name = "pom.upload.path")
	String UploadPath;
	
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
		model.addAttribute("UploadBasePath", UploadPath+File.separator);
        return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/find"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model find(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) Integer id){
		model.addAttribute("product",this.productDaoRead.find(id));
		model.addAttribute("UploadBasePath", UploadPath+File.separator);
		return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/add"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public void add(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer ptype,
    		@RequestParam(required = true) String name,
    		@RequestParam(required = false) String sku,
    		@RequestParam(value = "image", required = false) Object imageObj,
    		@RequestParam(value = "richText", required = false) Object richTextObj) throws IllegalStateException, IOException {
		String image = FileUtil.upload(imageObj, UploadFolder, "product/image");
		String richText = FileUtil.uploadRichText(richTextObj, UploadFolder, "product/richText");
		Product product = new Product();
		product.setProductType(ptype);
		product.setName(name);
		product.setSku(sku);
		product.setImage(image);
		product.setRichText(richText);
		this.productDao.insert(product);
    }
	
	@AccessVerifier
	@RequestMapping(value = {"delete"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void delete(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer id) throws IOException{
		Product p = this.productDaoRead.find(id);
		if(!this.productDao.delete(id)){
			return ;
		}
		if(!StringUtil.isNullOrEmpty(p.getImage())){
			FileUtils.forceDelete(new File(UploadFolder+File.separator+p.getImage()));
		}
		if(!StringUtil.isNullOrEmpty(p.getRichText())){
			FileUtils.forceDelete(new File(UploadFolder+File.separator+p.getRichText()));
		}
    }
	
	@AccessVerifier
	@RequestMapping(value = {"update"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void update(@RequestParam(required = false)Profile profile,HttpSession session,
    		@RequestParam(required = true) Integer id,
    		@RequestParam(required = true) Boolean imageChange,
    		@RequestParam(required = false) String name,
    		@RequestParam(required = false) String sku,
    		@RequestParam(value = "image", required = false) Object imageObj,
    		@RequestParam(value = "richText", required = false) Object richTextObj) throws IllegalStateException, IOException{
		String richText = FileUtil.uploadRichText(richTextObj, UploadFolder, "product/richText");
		Product p = this.productDaoRead.find(id);
		boolean b = false;
		if(imageChange){
			String image = FileUtil.upload(imageObj, UploadFolder, "product/image");
			b = this.productDao.update(id, name, sku, image,richText);
			if(b&&!StringUtil.isNullOrEmpty(p.getImage())){
				FileUtils.forceDelete(new File(UploadFolder+File.separator+p.getImage()));
			}
		}else{
			b = this.productDao.update(id, name, sku, p.getImage(),richText);
		}
		if(b&&!StringUtil.isNullOrEmpty(p.getRichText())){
			FileUtils.forceDelete(new File(UploadFolder+File.separator+p.getRichText()));
		}
    }
	
}
