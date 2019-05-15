package com.wnt.web.protocol.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.LogUtil;
import org.wnt.core.uitl.SocketEntityUtil;
import org.wnt.core.uitl.UUIDGenerator;

import com.itextpdf.text.log.SysoLogger;
import com.wnt.server.entry.ChartEntry;
import com.wnt.server.entry.PortEntry;
import com.wnt.server.order.ConstantsServer;
import com.wnt.server.order.TypeCmd;
import com.wnt.web.dynamicports.entry.DynamicPorts;
import com.wnt.web.protocol.entry.Protocol;
import com.wnt.web.protocol.service.ProtocolIndetityService;
import com.wnt.web.protocol.service.impl.ProtocolIdentityServiceImpl;
import com.wnt.web.socket.service.SocketService;
import com.wnt.web.system.entry.Sys;
import com.wnt.web.testexecute.service.TestExecuteService;

/**
 * 返回结果子线程
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class ReturnProtocolIdentityThread extends Thread implements Serializable{
	private final Logger log = Logger.getLogger(ReturnProtocolIdentityThread.class.getName());
	List<Protocol> listProtocol=new ArrayList<Protocol>();
	int count = 0;
	byte[] bt =null;
	private ProtocolIndetityService protocolIdentityService;
	// int returnn = 0;//自增
	public ReturnProtocolIdentityThread(ProtocolIndetityService protocolIdentityService) {
		this.protocolIdentityService = protocolIdentityService;
	}
	@Override
	public void run() {
//		synchronized (this) {
		try {
//			ConstantsServer.PROTOCOLFLAG++;
			while (true) {
//				if (EHCacheUtil.get("protocol"+ConstantsServer.PROTOCOLFLAG)!= null) {
				if(null != ConstantsServer.protocolResponseQueue){
					bt = ConstantsServer.protocolResponseQueue.poll();
//					System.out.println("----子线程接收数据------");
//					for(int i =0;i<bt.length;i++){
//						System.out.print(bt[i]+" ");
//						if(i%16==0){
//							System.out.println();
//						}
//					}
					//EHCacheUtil.remove("protocol"+ConstantsServer.PROTOCOLFLAG);
					//ConstantsServer.PROTOCOLFLAG++;
					if(bt!=null){
							Protocol protocol = new Protocol();
							int protocolId = SocketEntityUtil.byteToInt(bt, 16, 2);
		 					int port = SocketEntityUtil.byteToInt(bt, 18, 2);
							long srciptmp = SocketEntityUtil.byteToLong(bt, 20, 4);
							long dstipemp = SocketEntityUtil.byteToLong(bt, 24, 4);
							String srcip =null;
							String dstip =null;
							if(srciptmp ==0){
							   srcip ="-";
							}else{
							   srcip = SocketEntityUtil.ipString(srciptmp);
							}
							if(srciptmp ==0){
								dstip ="-";
							}else{
								dstip = SocketEntityUtil.ipString(dstipemp);
							}
							Long time = SocketEntityUtil.byteToLong(bt, 28, 8);
							time=time*1000;
							String identyTime = DataUtils.formatTime2(time);
							switch (protocolId) {
							case 0x02:
								protocol.setProtocolName("MMS");
								break;
							case 0x03:
								protocol.setProtocolName("GOOSE");				
								break;
							case 0x04:
								protocol.setProtocolName("SV");
								break;
							case 0x05:
								protocol.setProtocolName("IEC104");
								break;
							default:
								log.error("协议id值错误！");
								break;
							}
							protocol.setSrcIp(srcip);
							protocol.setDstIp(dstip);
							protocol.setPort(port);
							protocol.setIdentyTime(identyTime);
							listProtocol.add(protocol);
							
						}
					}
					if (listProtocol.size() > 0) {
						try {
						Protocol pro = listProtocol.get(0);
						System.out.println("pro :"+pro);
						protocolIdentityService.addProtocol(listProtocol);				
						} catch (Exception e) {
							e.printStackTrace();
						}
						listProtocol.clear();
						count = 0;
					}else if(count >= 30){
						count = 0;
					}
					count++;
	
					Thread.sleep(1000);
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			
		}
//	}
	}
}
