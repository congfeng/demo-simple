/**
 * 
 */
package com.cf.code.common;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author congfeng
 *
 */
public class FileUtil {

	public static String getExtName(String fileName){
		return fileName.substring(fileName.lastIndexOf(".")+1);
	}
	
	public static String upload(Object fileObj,String uploadFolder,String prefix) throws IllegalStateException, IOException{
		if(fileObj == null || !(fileObj instanceof MultipartFile)){
			return null;
		}
		MultipartFile file = (MultipartFile)fileObj;
		String fileName = UUID.randomUUID()+"."+getExtName(file.getOriginalFilename());
		String filePath = File.separator+prefix+File.separator+DateUtil.format(new Date(),"yyyy"+File.separator+"MM"+File.separator+"dd")+File.separator;
		File fileDirs = new File(uploadFolder+filePath);
		if(!fileDirs.exists()){
			if(!fileDirs.mkdirs()){
				throw new IOException("创建目录失败"+filePath);
			}
		}
		if(!fileDirs.canRead()||!fileDirs.canWrite()){
			throw new IOException("文件目录权限不足"+filePath);
		}
		file.transferTo(new File(fileDirs,fileName));
		return filePath+fileName;
	}
	
	public static String uploadScrawl(Object fileData,String uploadFolder,String prefix) throws IllegalStateException, IOException{
		if(fileData == null){
			return null;
		}
		String fileName = UUID.randomUUID()+".jpg";
		String filePath = File.separator+prefix+File.separator+DateUtil.format(new Date(),"yyyy"+File.separator+"MM"+File.separator+"dd"+File.separator);
		File fileDirs = new File(uploadFolder+filePath);
		if(!fileDirs.exists()){
			if(!fileDirs.mkdirs()){
				throw new IOException("创建目录失败"+filePath);
			}
		}
		if(!fileDirs.canRead()||!fileDirs.canWrite()){
			throw new IOException("文件目录权限不足"+filePath);
		}
		FileUtils.writeByteArrayToFile(new File(fileDirs,fileName), Base64.decodeBase64(fileData.toString()));
		return filePath+fileName;
	}
	
	public static String uploadRichText(Object fileData,String uploadFolder,String prefix) throws IllegalStateException, IOException{
		if(fileData == null){
			return null;
		}
		String fileName = UUID.randomUUID()+".html";
		String filePath = File.separator+prefix+File.separator+DateUtil.format(new Date(),"yyyy"+File.separator+"MM"+File.separator+"dd"+File.separator);
		File fileDirs = new File(uploadFolder+filePath);
		if(!fileDirs.exists()){
			if(!fileDirs.mkdirs()){
				throw new IOException("创建目录失败"+filePath);
			}
		}
		if(!fileDirs.canRead()||!fileDirs.canWrite()){
			throw new IOException("文件目录权限不足"+filePath);
		}
		FileUtils.writeStringToFile(new File(fileDirs,fileName), fileData.toString());
		return filePath+fileName;
	}
	
}
