/**
 * 
 */
package com.cf.code.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


/**
 * @author congfeng
 *
 */
public class FileUtil {

	private static Logger log = LogManager.getLogger(FileUtil.class);
	
	public static void deleteFile(String filePath) throws IOException{
		File file = new File(filePath);
		if(!file.exists()){
			log.warn("文件不存在"+filePath);
			return ;
		}
		FileUtils.forceDelete(file);
	}
	
	public static String upload(Object fileObj,String uploadFolder,String prefix) throws IOException{
		if(fileObj == null || !(fileObj instanceof MultipartFile)){
			return null;
		}
		MultipartFile file = (MultipartFile)fileObj;
		String fileName = UUID.randomUUID()+"."+getExtName(file.getOriginalFilename());
		String filePath = getFilePath(prefix);
		File fileDirs = getFileDirs(uploadFolder+"/"+filePath);
		file.transferTo(new File(fileDirs,fileName));
		return filePath+"/"+fileName;
	}
	
	public static String uploadScrawl(Object fileData,String uploadFolder,String prefix) throws IOException{
		if(fileData == null){
			return null;
		}
		String fileName = UUID.randomUUID()+".jpg";
		String filePath = getFilePath(prefix);
		File fileDirs = getFileDirs(uploadFolder+"/"+filePath);
		FileUtils.writeByteArrayToFile(new File(fileDirs,fileName), Base64.decodeBase64(fileData.toString()));
		return filePath+"/"+fileName;
	}
	
	public static String uploadRichText(Object fileData,String uploadFolder,String prefix) throws IOException{
		if(fileData == null||StringUtil.isNullOrEmpty(fileData.toString())){
			return null;
		}
		String fileName = UUID.randomUUID()+".html";
		String filePath = getFilePath(prefix);
		File fileDirs = getFileDirs(uploadFolder+"/"+filePath);
		FileUtils.writeStringToFile(new File(fileDirs,fileName), fileData.toString());
		return filePath+"/"+fileName;
	}
	
	public static String uploadQrcode(String url,String uploadFolder,String prefix) throws IOException, WriterException{
		String fileName = UUID.randomUUID()+".png";
		String filePath = getFilePath(prefix);
		File fileDirs = getFileDirs(uploadFolder+"/"+filePath);
		Path path = FileSystems.getDefault().getPath(fileDirs.getPath(), fileName);
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); 
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url,BarcodeFormat.QR_CODE, 200, 200,hints);
        MatrixToImageWriter.writeToPath(bitMatrix, "png", path);// 输出图像  
		return filePath+"/"+fileName;
	}
	
	private static String getExtName(String fileName){
		return fileName.substring(fileName.lastIndexOf(".")+1);
	}
	
	private static String getFilePath(String prefix){
		return prefix+"/"+DateUtil.format(new Date(),"yyyy"+"/"+"MM"+"/"+"dd");
	}
	
	
	private static File getFileDirs(String fileDirsPath)throws IOException{
		File fileDirs = new File(fileDirsPath);
		if(!fileDirs.exists()){
			if(!fileDirs.mkdirs()){
				log.error("创建目录失败"+fileDirsPath);
				throw new IOException("创建目录失败");
			}
		}
		if(!fileDirs.canRead()||!fileDirs.canWrite()){
			log.error("文件目录权限不足"+fileDirsPath);
			throw new IOException("文件目录权限不足");
		}
		return fileDirs;
	}
	
}
