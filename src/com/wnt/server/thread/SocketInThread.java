package com.wnt.server.thread;


import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.apache.commons.lang3.ArrayUtils;
import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.LogUtil;
import org.wnt.core.uitl.SocketEntityUtil;

import com.wnt.server.order.ConstantsServer;
import com.wnt.server.order.SealedSendMessage;
import com.wnt.server.order.TypeCmd;
import com.wnt.web.socket.service.SocketService;
import com.wnt.web.task.MonitorTask;

/**
 * 子线程，负责处理接收数据
 * 
 * @author zheng
 * 
 */
public class SocketInThread implements Runnable {

	private InputStream inStream;
	private String hostip;
	private Socket socket;
	private SocketService socketService;
	private SealedSendMessage s = new SealedSendMessage();
	// TypeCmd typeCmd = new TypeCmd();
	int realFileLen = 0;
	int cmdId = 0;
	int btn = 0;// 当前保存到了第几个
	boolean bool = false; // 判断是否为断数据
	int len = 0;// 字节长度
	long k = 0;
	String str = "";
	int total = 0;
	//private byte[] req = null;
	private final static int PACKATHEADER_TYPE_LENGTH=6;
	private final static int PACKATHEADER_DATALEN_LENGTH=2;
	private final static int PACKATHEADER_LENGTH=12;
	
	//监视器的类型
	public static Integer[] effective = new Integer[]{1,2,3,4,13,14,15,16};
	
	public SocketInThread(InputStream inStream, String hostip, Socket socket,
			SocketService socketService) {
		this.inStream = inStream;
		this.socket = socket;
		this.socketService = socketService;
	}

	@Override
	public void run() {
		try {
			//byte[] fileTemp = new byte[1024 * 2];
			while (true) {
				if (!ConstantsServer.BSTOPT) {
					recv();
				}else{
		  			break;
				}
			}
		} finally {
			LogUtil.info("异常断连");
//			System.out.println("第三次");
			try {
				ConstantsServer.BSTOPT = true;
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void recv() {
		byte[] temp = new byte[PACKATHEADER_LENGTH];
		recursionRead(temp, 0, PACKATHEADER_LENGTH);
		int dataLen = SocketEntityUtil.byteToInt(temp,
				PACKATHEADER_TYPE_LENGTH, PACKATHEADER_DATALEN_LENGTH);
		int size = dataLen;
		byte[] data = new byte[size];
		recursionRead(data, 0, size-12);
		
		byte[] fileTemp = new byte[temp.length+data.length];
		System.arraycopy(temp,0,fileTemp,0,temp.length);
	    System.arraycopy(data,0,fileTemp,temp.length,data.length);

		setEbyte(fileTemp,temp[10]);
	}

	/*
	private void recursionRead(byte[] data, int length, int gap) {
		try {
			int lengthTemp = inStream.read(data, length, gap);
			
			int gapTemp = gap - lengthTemp;
			if (gapTemp > 0) {
				recursionRead(data, length + lengthTemp, gapTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	private void recursionRead(byte[] data, int length, int gap) {
		if(length > -1 && gap > 0 && gap > length){
			try {
				int lengthTemp = 0;
				int lastLength = gap;//剩余长度
				//System.out.print("收到的数据：");
				while(lastLength > 0){
					lengthTemp = inStream.read(data, length, lastLength);
					/*String t = "";
					for(int i = 0;i<lengthTemp;i++){
						t += " "+data[i];
					}
					System.out.print(t);*/
					if(lengthTemp > 0 ){
						lastLength = lastLength - lengthTemp;//计算剩余长度
						length = length + lengthTemp;        //下一次的开始值
					}else if(lengthTemp == 0){ 
						lastLength = lastLength - 1;//计算剩余长度
						length = length + 1;        //下一次的开始值
					}else{
						break;
					}
				}
				//System.out.println();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			//System.out.println(this.getClass().getName()+".recursionRead  方法参数不合法！");
		}
	}
	
	public void setSbyte(byte[] fileTemp) {
		byte[] bt = new byte[fileTemp.length];
		str = "";
		for (int i = 0; i < fileTemp.length; i++) {
			str = str + fileTemp[i] + " ";
		}
		// LogUtil.info(str);
		 System.out.println(str);

		k = k + realFileLen;
		str = DataUtils.formatDateTime() + " 命令总数：" + k;
		// LogUtil.info(str);
		// System.out.println(str);
		for (int j = 0; j < fileTemp.length; j++) {
			// bt[btn] = fileTemp[j]; //取数据
			if (btn == 0) {// 新数据
				bt = new byte[fileTemp.length];
				// if(fileTemp[j] == 0) break;
				// //从头开始取，第一条为0，说明后续没有数据
				if (j + 6 >= fileTemp.length) { // 超过字节
					bool = true;
					bt[btn] = fileTemp[j];
					fileTemp[j] = 0;
					System.out.println("异常数据--" + bt[btn]);
				} else {
					len = SocketEntityUtil.byteToInt(fileTemp, j + 6, 2); // 取到包长度
					len--;
					bt[btn] = fileTemp[j];
					fileTemp[j] = 0;
					 System.out.println("数据" + bt[btn]);
				}
				btn++;
			} else {
				if (bool == true) {
					if (btn == 7) { // 取包长度
						len = SocketEntityUtil.byteToInt(bt, 6, 2) - 8;
						bool = false;
					}
					bt[btn] = fileTemp[j];
					fileTemp[j] = 0;
					System.out.println("异常数据++" + bt[btn]);
					btn++;
				} else {
					len--;
					bt[btn] = fileTemp[j];
					fileTemp[j] = 0;
					 System.out.println("数据" + bt[btn]);
					if (btn == 10) {
						cmdId = bt[btn]; // 获取命令
						 System.out.println("命令" + cmdId);
					}
					btn++;
					if (len == 0) {// 整包完整
						btn = 0;
						total = total + 1;
						// LogUtil.info("条数："+total+" start"+ConstantsDefs.STATERESPONSEN);
						 System.out.println("插入命令:"+cmdId);
						setEbyte(bt, cmdId);
					
					}
				}
			}
		}
	}

	public void setEbyte(byte[] bt, int cmdId) {
		switch (cmdId) {
		case TypeCmd.CONNECT_RESPONSE:
			// 使用一个子线程处理
			Thread connect = new Thread(new ConnectThread(bt, socketService));
			connect.start();
			break;
		/*
		 * 备份原来的代码
		 * case TypeCmd.STATE_RESPONSE:
			ConstantsServer.STATERESPONSEN++;
			EHCacheUtil.put("start" + ConstantsServer.STATERESPONSEN, bt);
			break;*/
		case TypeCmd.STATE_RESPONSE:
			if(ArrayUtils.contains(effective,SocketEntityUtil.byteToInt(bt, 16, 4))){
				MonitorTask.chartMap.put(SocketEntityUtil.byteToInt(bt, 16, 4),bt);
			}
			ConstantsServer.STATERESPONSEN++;
			EHCacheUtil.put("start" + ConstantsServer.STATERESPONSEN, bt);
			break;
		case TypeCmd.LOG_EVENT:
			ConstantsServer.LOGN++;
			EHCacheUtil.put("log" + ConstantsServer.LOGN, bt);
			break;
		case TypeCmd.CMD_DI_READ_TEST:
			//硬件检查
			int[] leds = new int[16];
			for(int i = 16;i < 32;i++){
				leds[i-16] = bt[i];
			}
			EHCacheUtil.put("r"+TypeCmd.CMD_HARDWARE_CHECK_START, leds);
			break;
		case TypeCmd.CMD_PROTO_IDENTI_RESULT: 
			ConstantsServer.protocolResponseQueue.offer(bt);
			break;
		case TypeCmd.CMD_UPLOAD_HARD_STATE:
			//系统信息CMD
			EHCacheUtil.put("sysinfo" + ConstantsServer.SYSINFOFLAG, bt);
			break;
		case TypeCmd.CMD_UPLOAD_CRT_VERSION:
			//版本号CMD
			EHCacheUtil.put("version", bt);
			String version =null;
			if (EHCacheUtil.get("version")!= null) {
				byte[] vbt = (byte[]) EHCacheUtil.get("version");
				long tempip=SocketEntityUtil.byteToLong(vbt, 16, 4);
				String managerip = SocketEntityUtil.ipString(tempip);
				long tempmack =SocketEntityUtil.byteToLong(vbt, 20, 4);
				String managerMask = SocketEntityUtil.ipString(tempmack);
				version = SocketEntityUtil.byteToString(vbt, 24, 32);
				EHCacheUtil.put("VERSION", version);
				EHCacheUtil.put("MANAGERIP", managerip);
				EHCacheUtil.put("MANAGERMASK", managerMask);
			}
//			PropertiesUtil ph = new PropertiesUtil("sysConfig.properties");
//			ph.writeProperty("version",version);
			break;
		case TypeCmd.CMD_TEST_MAX_NUM:
			ConstantsServer.maxNumResponseQueue.offer(bt);
			break;
		}
	}

}
