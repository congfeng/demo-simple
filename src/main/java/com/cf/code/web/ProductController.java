/**
 * 
 */
package com.cf.code.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.cf.code.common.WebUtil;
import com.cf.code.core.MyContextLoader;
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
	
//	@AccessVerifier
	@RequestMapping(value = {"list"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model list(@RequestParam(required = false)Profile profile,HttpSession session,
			Model model,
			@RequestParam(required = false) Integer pageNo,
			@RequestParam(required = false) Integer pageSize,
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
		pager.setPageNo(pageNo);
		if(pageSize != null){
			pager.setPageSize(pageSize);
		}
		int count = this.productDaoRead.queryCount(ptype, name, sku, createTimeStart, createTimeEnd);
		pager.setCount(count);
		List<Product> products = this.productDaoRead.query(ptype, name, sku, createTimeStart, createTimeEnd,
				pager.getStartIndex(), pager.getPageSize()); 
		model.addAttribute("products", products);   
		model.addAttribute("pager", pager);
		model.addAttribute("UploadBasePath", getUploadPath());
        return model;
    }
	
//	@AccessVerifier
	@RequestMapping(value = {"/find"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model find(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) Integer id){
		model.addAttribute("product",this.productDaoRead.find(id));
		model.addAttribute("UploadBasePath", getUploadPath());
		return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/add"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model add(@RequestParam(required = false)Profile profile,HttpSession session,
    		HttpServletRequest request,Model model,
    		@RequestParam(required = true) Integer ptype,
    		@RequestParam(required = true) String name,
    		@RequestParam(required = false) String sku,
    		@RequestParam(value = "image", required = false) Object imageObj,
    		@RequestParam(value = "richText", required = false) Object richTextObj) throws Exception {
		String image = FileUtil.upload(imageObj, getUploadFolder(session), "product/image");
		String richText = FileUtil.uploadRichText(richTextObj, getUploadFolder(session), "product/richText");
		Product product = new Product();
		product.setProductType(ptype);
		product.setName(name);
		product.setSku(sku);
		product.setImage(image);
		product.setRichText(richText);
		this.productDao.insert(product);
		String url = WebUtil.getWebRoot(request)+"/front/productinfo.html?id="+product.getId();
		String qrcode = FileUtil.uploadQrcode(url, getUploadFolder(session), "product/qrcode");
		this.productDao.updateQrcode(product.getId(), qrcode);
		return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"delete"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model delete(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) Integer id) throws IOException{
		Product p = this.productDaoRead.find(id);
		if(!this.productDao.delete(id)){
			return model;
		}
		if(!StringUtil.isNullOrEmpty(p.getImage())){
			FileUtil.deleteFile(getUploadFolder(session)+"/"+p.getImage());
		}
		if(!StringUtil.isNullOrEmpty(p.getRichText())){
			FileUtil.deleteFile(getUploadFolder(session)+"/"+p.getRichText());
		}
		if(!StringUtil.isNullOrEmpty(p.getQrcode())){
			FileUtil.deleteFile(getUploadFolder(session)+"/"+p.getQrcode());
		}
		return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"/updateQrcode"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Model updateQrcode(@RequestParam(required = false)Profile profile,HttpSession session,
    		HttpServletRequest request,Model model,
    		@RequestParam(required = true) Integer id) throws Exception {
		Product p = this.productDaoRead.find(id);
		String url = WebUtil.getWebRoot(request)+"/front/productinfo.html?id="+id;
		String qrcode = FileUtil.uploadQrcode(url, getUploadFolder(session), "product/rqcode");
		this.productDao.updateQrcode(id,qrcode);
		if(!StringUtil.isNullOrEmpty(p.getQrcode())){
			FileUtil.deleteFile(getUploadFolder(session)+"/"+p.getQrcode());
		}
		model.addAttribute("qrcode", qrcode);
		model.addAttribute("UploadBasePath", getUploadPath());
		return model;
    }
	
	@AccessVerifier
	@RequestMapping(value = {"update"}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Model update(@RequestParam(required = false)Profile profile,HttpSession session,Model model,
    		@RequestParam(required = true) Integer id,
    		@RequestParam(required = true) Boolean imageChange,
    		@RequestParam(required = false) String name,
    		@RequestParam(required = false) String sku,
    		@RequestParam(value = "image", required = false) Object imageObj,
    		@RequestParam(value = "richText", required = false) Object richTextObj) throws IOException{
		String richText = FileUtil.uploadRichText(richTextObj, getUploadFolder(session), "product/richText");
		Product p = this.productDaoRead.find(id);
		boolean b = false;
		if(imageChange){
			String image = FileUtil.upload(imageObj, getUploadFolder(session), "product/image");
			b = this.productDao.update(id, name, sku, image,richText);
			if(b&&!StringUtil.isNullOrEmpty(p.getImage())){
				FileUtil.deleteFile(getUploadFolder(session)+"/"+p.getImage());
			}
		}else{
			b = this.productDao.update(id, name, sku, p.getImage(),richText);
		}
		if(b&&!StringUtil.isNullOrEmpty(p.getRichText())){
			FileUtil.deleteFile(getUploadFolder(session)+"/"+p.getRichText());
		}
		return model;
    }
	
	private String getUploadFolder(HttpSession session){
		if(!StringUtil.isNullOrEmpty(MyContextLoader.uploadFolder)){
			return MyContextLoader.uploadFolder;
		}
		return session.getServletContext().getRealPath("/")+"/upload";
	}
	
	private String getUploadPath(){
		if(!StringUtil.isNullOrEmpty(MyContextLoader.uploadPath)){
			return MyContextLoader.uploadPath;
		}
		return "/upload/";
	}
	
}
