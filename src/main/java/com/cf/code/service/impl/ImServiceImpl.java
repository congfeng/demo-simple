/**
 * 
 */
package com.cf.code.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cf.code.core.MyContextLoader;
import com.cf.code.dao.MsgDao;
import com.cf.code.service.ImService;

/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
@Service("imService")
public class ImServiceImpl implements ImService{

	@Resource(name = "msgDaoRead")
	MsgDao msgDaoRead;

	@Override
	public void sendMsg(String msg) {
		if(MyContextLoader.imServer == null){
			return ;
		}
		MyContextLoader.imServer.getBroadcastOperations().sendEvent("mychatevent", msg);
	}
	
	@Override
	public void pushMsgCount(final boolean isAdd) {
		if(MyContextLoader.imServer == null){
			return ;
		}
		final int msgCount = this.msgDaoRead.queryCount(null, null, null, 0, null, null);
		MyContextLoader.imServer.getBroadcastOperations().sendEvent("mychatevent", new HashMap<String,Object>(){{
			this.put("isAdd", isAdd);
			this.put("msgCount", msgCount);
		}});
	}
	
}
