package com.corundumstudio.socketio.demo;

import com.corundumstudio.socketio.listener.*;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.*;

public class ChatLauncher {

	public static void main(String[] args) throws InterruptedException {

		Configuration config = new Configuration();
//		config.setHostname("192.168.1.138");
		config.setPort(9092);

		final SocketIOServer server = new SocketIOServer(config);
		server.addEventListener("chatevent", ChatObject.class,
				new DataListener<ChatObject>() {
					@Override
					public void onData(SocketIOClient client, ChatObject data,
							AckRequest ackRequest) {
						// broadcast messages to all clients
						server.getBroadcastOperations().sendEvent("chatevent",
								data);
					}
				});

		server.start();
		Thread handlerExecute = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Packet pk = new Packet();
					// pk.setAckId(1232132l);
					// pk.setNsp(endpoint);
					// pk.setName("kkkkk");
					// pk.setData("23432432432");
					// server.getBroadcastOperations().send(pk);
					ChatObject co = new ChatObject();
					co.setMessage("成功");
					co.setUserName("地主");
					server.getBroadcastOperations().sendEvent("chatevent", co);
				}
			}
		};
		handlerExecute.start();

		Thread.sleep(Integer.MAX_VALUE);

		server.stop();
	}

}
