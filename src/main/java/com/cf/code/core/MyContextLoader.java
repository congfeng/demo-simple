/**
 * 
 */
package com.cf.code.core;

import java.sql.SQLException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.h2.api.ErrorCode;
import org.h2.tools.Server;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class MyContextLoader {
	
	private static Logger log = LogManager.getLogger(MyContextLoader.class);
	
	public static volatile String uploadFolder;
	
	public static volatile String uploadPath;
	
	private static volatile String imHost;
	
	private static volatile Integer imPort;
	
	public static volatile SocketIOServer imServer;
	
	public void init(){
		try {
			Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "8088" ).start();
			log.info("h2 server tcp 启动成功");
		} catch (SQLException e) {
			if(e.getErrorCode() == ErrorCode.EXCEPTION_OPENING_PORT_2){
				log.warn("h2 server tcp 端口已占用，可能已经启动");
			}else{
				log.error("h2 server tcp 启动失败", e);
			}
		}
		try {
			Server.createWebServer("-webAllowOthers","-webPort","8082").start();
			log.info("h2 server web 启动成功");
		} catch (SQLException e) {
			if(e.getErrorCode() == ErrorCode.EXCEPTION_OPENING_PORT_2){
				log.warn("h2 server web 端口已占用，可能已经启动");
			}else{
				log.error("h2 server web 启动失败", e);
			}
		}
		try{
			Configuration config = new Configuration();
	        config.setHostname(imHost);
	        config.setPort(imPort);
	        imServer = new SocketIOServer(config);
	        imServer.start();
	        log.info("im 启动成功");
		}catch(Exception e){
			log.error("im 启动失败", e);
		}
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				destroy();
			}
		}));
	}

	public void destroy(){
//		if(ImServer != null){
//			ImServer.stop();
//			log.info("im 关闭成功");
//		}
	}

	public void setUploadFolder(String uploadFolder) {
		this.uploadFolder = uploadFolder;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public void setImHost(String imHost) {
		this.imHost = imHost;
	}

	public void setImPort(Integer imPort) {
		this.imPort = imPort;
	}
	
	public static String getImAddress(){
		return "http://"+imHost+":"+imPort;
	}
}
