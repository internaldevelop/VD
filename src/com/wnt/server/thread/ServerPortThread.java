package com.wnt.server.thread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.wnt.core.uitl.LogUtil;

import com.wnt.server.order.ConstantsServer;
import com.wnt.web.socket.service.SocketService;

public class ServerPortThread implements Runnable {

	private SocketService socketService;

	public ServerPortThread(SocketService socketService) {
		super();
		this.socketService = socketService;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// 启动服务监听
//			ServerSocket ss = new ServerSocket(5345, 0,
//					InetAddress.getByName("1.1.1.1"));
			 ServerSocket ss = new ServerSocket(5345);
			while (true) {
				Socket s = ss.accept();
				s.setReceiveBufferSize(2 * 1024);
				ConstantsServer.BSTOPT = false;
				new Thread(new ServerSocketThread(s, socketService)).start();
				LogUtil.info("socket已连接");
			}
		} catch (IOException e) {
			LogUtil.info("接收服务异常：" + e.getMessage());
			e.printStackTrace();
		}
	}

}
