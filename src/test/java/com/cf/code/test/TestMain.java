/**
 * 
 */
package com.cf.code.test;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;


/**
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 */
public class TestMain {

	static int i = 100;
	
	public static void main(String[] args) {
		Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9093);
        final SocketIOServer server = new SocketIOServer(config);
//        server.addEventListener("chatevent", ChatObject.class, new DataListener<ChatObject>(){
//        	public void onData(SocketIOClient client, ChatObject data, ackSender arg2) throws Exception {
//        		
//        	};
//        });
        server.addEventListener("mychatevent", Bean.class, new DataListener<Bean>(){
			@Override
			public void onData(SocketIOClient client, Bean data, AckRequest ackSender)throws Exception {
				System.out.println("mychatevent-------------"+data.getName());
				//server.getBroadcastOperations().sendEvent("mychatevent", data);
			}
        });
        server.start();
        
        int i = 0;
        while(true){
        	try {
    			Thread.sleep(5000) ;
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
        	Bean bean = new Bean();
            bean.setName("ddddd"+i++);
            server.getBroadcastOperations().sendEvent("mychatevent", bean);
        }
        
       // server.stop();
	}
	
	
}
