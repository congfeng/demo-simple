/**
 * 
 */
package com.cf.code.service.impl;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.cf.code.dao.MsgDao;
import com.cf.code.service.ImService;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Service("imService")
public class ImServiceImpl implements ImService{

	private static Logger log = LogManager.getLogger(ImServiceImpl.class);
	
	@Resource(name = "msgDaoRead")
	MsgDao msgDaoRead;
	
	@Resource(name = "pom.im.host")
	String ImHost;
	
	@Resource(name = "pom.im.port")
	Integer ImPort;
	
	volatile SocketIOServer server;
	
	@PostConstruct
	public void myInit(){
		Configuration config = new Configuration();
        config.setHostname(ImHost);
        config.setPort(ImPort);
        server = new SocketIOServer(config);
		server.start();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				if(server == null){
					return ;
				}
				server.stop();
			}
		}));
		log.info("im启动成功");
	}
	
	@PreDestroy
	public void myDestroy(){
		if(server == null){
			return ;
		}
		server.stop();
		log.info("im关闭成功");
	}

	@Override
	public void sendMsg(String msg) {
		if(server == null){
			return ;
		}
		server.getBroadcastOperations().sendEvent("mychatevent", msg);
	}
	
	@Override
	public void pushMsgCount(final boolean isAdd) {
		final int msgCount = this.msgDaoRead.queryCount(null, null, null, 0, null, null);
		server.getBroadcastOperations().sendEvent("mychatevent", new HashMap<String,Object>(){{
			this.put("isAdd", isAdd);
			this.put("msgCount", msgCount);
		}});
	}
	
}
