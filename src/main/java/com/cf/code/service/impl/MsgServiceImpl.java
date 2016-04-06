/**
 * 
 */
package com.cf.code.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cf.code.core.net.EmailMsgSender;
import com.cf.code.core.net.EmailMsgSender.EmailSendMsgType;
import com.cf.code.core.net.EmailMsgSender.EmailTargetDataType;
import com.cf.code.dao.MsgDao;
import com.cf.code.dao.MsgReceiverDao;
import com.cf.code.entity.Msg;
import com.cf.code.entity.MsgReceiver;
import com.cf.code.service.MsgService;

/**
 * @author congfeng
 *
 */
@Service("msgService")
public class MsgServiceImpl implements MsgService{

	@Resource(name = "msgDao")
	MsgDao msgDao;
	
	@Resource(name = "msgDaoRead")
	MsgDao msgDaoRead;
	
	@Resource(name = "msgReceiverDao")
	MsgReceiverDao msgReceiverDao;
	
	@Resource(name = "msgReceiverDaoRead")
	MsgReceiverDao msgReceiverDaoRead;
	
	EmailMsgSender msgSender = new EmailMsgSender("channel_warning@126.com","12345679","用户信件");
	
	@Override
	public void sender(Msg msg) {
		List<MsgReceiver> msgreceivers = this.msgReceiverDaoRead.query(null, null, 0, 100);
		Integer sendStatus = 0;
		try {
			for(MsgReceiver msgReceiver:msgreceivers){
				String content = msg.getContent().replaceAll("\r\n|\r|\n", "<br>").replaceAll(" ", "&nbsp;");
				EmailTargetDataType emailtarget = new EmailTargetDataType(msg.getTitle(), msgReceiver.getAddress());
				EmailSendMsgType emailMsg = new EmailSendMsgType(content,true);
				msgSender.send(emailtarget, emailMsg);
			}
			sendStatus = 1;
		} catch (Exception e) {
			e.printStackTrace();
			sendStatus = 2;
		}
		this.msgDao.updateSendStatus(msg.getId(), 0, sendStatus);
	}
	
}
