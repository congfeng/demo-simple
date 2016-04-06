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
public class SocketIoTest {

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
        server.addEventListener("mychatevent", String.class, new DataListener<String>(){
			@Override
			public void onData(SocketIOClient client, String data, AckRequest ackSender)throws Exception {
				System.out.println("mychatevent-------------"+"");
				//server.getBroadcastOperations().sendEvent("mychatevent", data);
			}
        });
        server.start();
        try {
			Thread.sleep(5000) ;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("1");
        server.stop();
        try {
			Thread.sleep(5000) ;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("2");
        server.stop();
        int i = 0;
        while(true){
        	try {
    			Thread.sleep(5000) ;
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
            server.getBroadcastOperations().sendEvent("mychatevent", "dd");
        }
        
       // server.stop();
	}






}
