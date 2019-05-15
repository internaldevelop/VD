package com.wnt.server.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.LogUtil;

import com.wnt.server.order.ConstantsServer;
import com.wnt.server.order.Order;
import com.wnt.web.protocol.service.ProtocolIndetityService;
import com.wnt.web.socket.service.SocketService;

/**
 * 主线程，处理客户端发送数据
 * 
 * @author zheng
 * 
 */
public class ServerSocketThread implements Runnable {
	Socket socket;
	SocketService socketService;
	String strbt = "";
	// static int num=0;
	static int flag = 0;
	
	public ServerSocketThread(Socket socket, SocketService socketService) {
		super();
		this.socket = socket;
		this.socketService = socketService;
	}
	
	@Override
	public void run() {
		//LogUtil.info("ServerSocketThread  is running...");
		try {
			InputStream inStream = socket.getInputStream();
			OutputStream outStream = socket.getOutputStream();

			// 使用一个子线程接收数据
			Thread ti = new Thread(new SocketInThread(inStream, socket
					.getInetAddress().getHostAddress(), socket, socketService));
			ti.start();

			while (true) {
				try {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss|SSS");
					//System.out.println("thread out or not："+(!ConstantsServer.BSTOPT));
					if (!ConstantsServer.BSTOPT) {
//						byte[] bt = new byte[1024];
//						if(EHCacheUtil.get("s" + ConstantsServer.SENTFLAGW) != null){
//							LogUtil.info(simpleDateFormat.format(new Date()) +"  缓存命令key：" + "s" + ConstantsServer.SENTFLAGW);
//						}
//						LogUtil.info("缓存命令key：" + EHCacheUtil.get("s" + ConstantsServer.SENTFLAGW));
						//if (EHCacheUtil.get("s" + ConstantsServer.SENTFLAGW) != null) {
						if(null != ConstantsServer.sendOrderQueue){
//							byte[] bt = (byte[]) EHCacheUtil.get("s"
//									+ ConstantsServer.SENTFLAGW);
							byte[] bt = ConstantsServer.sendOrderQueue.poll();
							if(null!=bt){
								strbt = "";
								for (int i = 0; i < bt.length; i++) {
									strbt = strbt + bt[i] + " ";
									// System.out.print(bt[i]+" ");
								}
								//LogUtil.info(simpleDateFormat.format(new Date()) +"  发送命令：" + strbt);
								//LogUtil.info("发送命令：" + strbt);
								outStream.write(bt);
								//LogUtil.info(simpleDateFormat.format(new Date()) +"  outStream.write  all right");
								outStream.flush(); 
								//LogUtil.info(simpleDateFormat.format(new Date()) +"  outStream.flush()  all right");
								//ConstantsServer.SENTFLAGW++;
								// num++;
								// System.out.println(num);
							}
						} 
						//else {				
						if (flag >= 30) {
							byte[] bt = Order.getHeartbeat();
							strbt = "";
							for (int i = 0; i < bt.length; i++) {
								strbt = strbt + bt[i] + " ";
								// System.out.print(bt[i]+" ");
							}
							outStream.write(bt);
							outStream.flush();
							//LogUtil.info(simpleDateFormat.format(new Date()) +"  heartbeat："+strbt);
							//LogUtil.info(simpleDateFormat.format(new Date()) +" this run's key："+ConstantsServer.SENTFLAGW);
							//LogUtil.info(simpleDateFormat.format(new Date()) +" last run's key："+ConstantsServer.SENTFLAG);
							flag = 0;
						}
						//}
						try {
							flag++;
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						break;
					}
				} catch (IOException e) {
					LogUtil.info("异常断连");
					e.printStackTrace();
					// outStream.close();
					// socket.close();
					break;
				} finally {
					// if (socket != null) {
					// socket.close();
					// }
				}
			}
//			System.out.println("【服务端】返回数据完毕!");
			// socket.close();
		} catch (IOException e) {
			LogUtil.info("异常断连");
//			System.out.println("我是服务第一次");
			e.printStackTrace();
		} finally {
			LogUtil.info("异常断连");
			ConstantsServer.BSTOPT = true;
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
//			System.out.println("我是服务第二次");
		}
	}

}
