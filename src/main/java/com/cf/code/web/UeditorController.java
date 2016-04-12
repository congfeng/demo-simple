/**
 * 
 */
package com.cf.code.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cf.code.common.FileUtil;
import com.cf.code.common.StringUtil;
import com.cf.code.core.MyContextLoader;

/**
 * @author congfeng
 *
 */
@Controller
@RequestMapping("/ueditor")
public class UeditorController{
	
	private static Logger log = LogManager.getLogger(UeditorController.class);
	
	@RequestMapping(value = {""}, method = { RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
    public Object route(
    		@RequestParam(required = true) String action,
    		@RequestParam(required = false) String callback,
    		@RequestParam(value = "upfile", required = false) Object upfileObj,
    		HttpServletRequest request, Model model) {
		try{
			if(action.equals("config")){
				config(model,request);
			}else if(action.equals("uploadimage")){
				this.uploadimage(model,request,upfileObj);
			}else if(action.equals("uploadscrawl")){
				this.uploadscrawl(model,request,upfileObj);
			}else if(action.equals("uploadvideo")){
				this.uploadvideo(model,request,upfileObj);
			}else if(action.equals("catchimage")){
//				this.catchimage(model,request);
				throw new Exception("系统暂不支持catchimage操作");
			}else if(action.equals("uploadfile")){
//				this.uploadfile(model,request);
				throw new Exception("系统暂不支持uploadfile操作");
			}else if(action.equals("listimage")){
//				this.listimage(model,request);
				throw new Exception("系统暂不支持listimage操作");
			}else if(action.equals("listfile")){
//				this.listfile(model,request);
				throw new Exception("系统暂不支持listfile操作");
			}else{
				throw new Exception("ueditor操作未知:"+action);
			}
			model.addAttribute("state", "SUCCESS");
		}catch(Exception e){
			log.error("ueditor处理异常:"+action, e);
			model.addAttribute("state", e.getMessage());
		}
		if(!StringUtil.isNullOrEmpty(callback)){
			return callback+"("+JSON.toJSONString(model.asMap())+")";
		}
        return model;
    }
	
	private void config(Model model,HttpServletRequest request) {
    	/* 上传图片配置项 */
    	model.addAttribute("imageActionName", "uploadimage");/* 执行上传图片的action名称 */
    	model.addAttribute("imageFieldName", "upfile");/* 提交的图片表单名称 */
    	model.addAttribute("imageMaxSize", 2048000);/* 上传大小限制，单位B */
    	model.addAttribute("imageAllowFiles", new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp"});/* 上传图片格式显示 */
    	model.addAttribute("imageCompressEnable", true);/* 是否压缩图片,默认是true */
    	model.addAttribute("imageCompressBorder", 1600);/* 图片压缩最长边限制 */
    	model.addAttribute("imageInsertAlign", "none");/* 插入的图片浮动方式 */
    	model.addAttribute("imageUrlPrefix", "");/* 图片访问路径前缀 */
//    	model.addAttribute("imagePathFormat", "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}");/* 上传保存路径,可以自定义保存路径和文件名格式 */
    	/* 涂鸦图片上传配置项 */
    	model.addAttribute("scrawlActionName", "uploadscrawl");/* 执行上传涂鸦的action名称 */
    	model.addAttribute("scrawlFieldName", "upfile");/* 提交的图片表单名称 */
    	model.addAttribute("scrawlMaxSize", 2048000);/* 上传大小限制，单位B */
    	model.addAttribute("scrawlUrlPrefix", "");/* 图片访问路径前缀 */
    	model.addAttribute("scrawlInsertAlign", "none");
//    	model.addAttribute("scrawlPathFormat", "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}");/* 上传保存路径,可以自定义保存路径和文件名格式 */
	    /* 上传视频配置 */
    	model.addAttribute("videoActionName", "uploadvideo");/* 执行上传视频的action名称 */
    	model.addAttribute("videoFieldName", "upfile");/* 提交的视频表单名称 */
    	model.addAttribute("videoUrlPrefix", "");/* 视频访问路径前缀 */
    	model.addAttribute("videoMaxSize", 1204800000);/* 上传大小限制，单位B，默认100MB */
    	model.addAttribute("videoAllowFiles", new String[]{".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",".ogg",
    			".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid"});/* 上传视频格式显示 */
//    	model.addAttribute("videoPathFormat", "/ueditor/jsp/upload/video/{yyyy}{mm}{dd}/{time}{rand:6}");/* 上传保存路径,可以自定义保存路径和文件名格式 */
    	/* 截图工具上传 */
    	model.addAttribute("snapscreenActionName", "uploadimage");/* 执行上传截图的action名称 */
    	model.addAttribute("snapscreenUrlPrefix", "");/* 图片访问路径前缀 */
    	model.addAttribute("snapscreenInsertAlign", "none");/* 插入的图片浮动方式 */
//    	model.addAttribute("snapscreenPathFormat", "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}");/* 上传保存路径,可以自定义保存路径和文件名格式 */
	    /* 抓取远程图片配置 */
    	model.addAttribute("catcherLocalDomain", new String[]{"127.0.0.1", "localhost", "image.baidu.com"});
    	model.addAttribute("catcherActionName", "catchimage");/* 执行抓取远程图片的action名称 */
    	model.addAttribute("catcherFieldName", "source");/* 提交的图片列表表单名称 */
    	model.addAttribute("catcherUrlPrefix", "");/* 图片访问路径前缀 */
    	model.addAttribute("catcherMaxSize", 2048000); /* 上传大小限制，单位B */
    	model.addAttribute("catcherAllowFiles", new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp"});/* 抓取图片格式显示 */
//    	model.addAttribute("catcherPathFormat", "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}");/* 上传保存路径,可以自定义保存路径和文件名格式 */
	    /* 上传文件配置 */
    	model.addAttribute("fileActionName", "uploadfile");/* controller里,执行上传视频的action名称 */
    	model.addAttribute("fileFieldName", "upfile");/* 提交的文件表单名称 */
    	model.addAttribute("fileUrlPrefix", "");/* 文件访问路径前缀 */
    	model.addAttribute("fileMaxSize", 51200000);/* 上传大小限制，单位B，默认50MB */
    	model.addAttribute("fileAllowFiles", new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp",
    			".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",
    			".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid",
    			".rar", ".zip", ".tar", ".gz", ".7z", ".bz2", ".cab", ".iso",
    			".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf", ".txt", ".md", ".xml"});/* 上传文件格式显示 */
//    	model.addAttribute("filePathFormat", "/ueditor/jsp/upload/file/{yyyy}{mm}{dd}/{time}{rand:6}");/* 上传保存路径,可以自定义保存路径和文件名格式 */
	    /* 列出指定目录下的图片 */
    	model.addAttribute("imageManagerActionName", "listimage");/* 执行图片管理的action名称 */
    	model.addAttribute("imageManagerListPath", "/ueditor/jsp/upload/image/");/* 指定要列出图片的目录 */
    	model.addAttribute("imageManagerListSize", 20);/* 每次列出文件数量 */
    	model.addAttribute("imageManagerUrlPrefix", "");/* 图片访问路径前缀 */
    	model.addAttribute("imageManagerInsertAlign", "none");/* 插入的图片浮动方式 */
    	model.addAttribute("imageManagerAllowFiles", new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp"}); /* 列出的文件类型 */
	    /* 列出指定目录下的文件 */
    	model.addAttribute("fileManagerActionName", "listfile");/* 执行文件管理的action名称 */
    	model.addAttribute("fileManagerListPath", "/ueditor/jsp/upload/file/");/* 指定要列出文件的目录 */
    	model.addAttribute("fileManagerUrlPrefix", "");/* 文件访问路径前缀 */
    	model.addAttribute("fileManagerListSize", 20);/* 每次列出文件数量 */
    	model.addAttribute("fileManagerAllowFiles", new String[]{".png", ".jpg", ".jpeg", ".gif", ".bmp",
    			".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",
    			".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid",
    			".rar", ".zip", ".tar", ".gz", ".7z", ".bz2", ".cab", ".iso",
    			".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf", ".txt", ".md", ".xml"});/* 列出的文件类型 */
    }
	
    private void uploadimage(Model model,HttpServletRequest request,Object fileObj) throws IOException {
		String image = FileUtil.upload(fileObj, getUploadFolder(request.getSession()), "image");
    	model.addAttribute("url",getUploadPath()+image);
		model.addAttribute("title",image);
		model.addAttribute("original",image);    
	}
    
    private void uploadscrawl(Model model,HttpServletRequest request,Object fileObj) throws IOException {
		String scrawl = FileUtil.uploadScrawl(fileObj, getUploadFolder(request.getSession()), "scrawl");
		model.addAttribute("url",getUploadPath()+scrawl);
		model.addAttribute("title",scrawl);
		model.addAttribute("original",scrawl);
    }

    private void catchimage(Model model,HttpServletRequest request) {
		String[] source = request.getParameterValues("source");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(){{
    		this.add(new HashMap<String,Object>(){{
    			this.put("url", "upload/1.jpg");
    			this.put("source", "http://b.com/1.jpg");
    			this.put("state", "SUCCESS");
    		}});
    		this.add(new HashMap<String,Object>(){{
    			this.put("url", "upload/2.jpg");
    			this.put("source", "http://b.com/2.jpg");
    			this.put("state", "SUCCESS");
    		}});
    	}};
    	model.addAttribute("list",list);
    }
    
    private void uploadvideo(Model model,HttpServletRequest request,Object fileObj) throws IOException {
		String video = FileUtil.upload(fileObj, getUploadFolder(request.getSession()), "video");
		model.addAttribute("url",getUploadPath()+video);
		model.addAttribute("title",video);
		model.addAttribute("original",video);
    }
    
    private void uploadfile(Model model,HttpServletRequest request) {
		model.addAttribute("url","upload/demo.zip");
		model.addAttribute("title","demo.zip");
		model.addAttribute("original","demo.zip");
    }
    
    private void listimage(Model model,HttpServletRequest request) {
		int start = Integer.valueOf(request.getParameter("start"));
    	int size = Integer.valueOf(request.getParameter("size"));
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(){{
    		this.add(new HashMap<String,Object>(){{
    			this.put("url", "/picture/00e562b3-5da9-4eb6-a809-4a1c54e9cc5e.JPG");
    		}});
    		this.add(new HashMap<String,Object>(){{
    			this.put("url", "/picture/6ba54702-8eef-47be-9463-097834587085.JPG");
    		}});
    	}};
    	model.addAttribute("list",list);
    	model.addAttribute("start",20);
		model.addAttribute("total",100);
    }
    
    private void listfile(Model model,HttpServletRequest request) {
		
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
