package com.wnt.server.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.SocketEntityUtil;

import com.wnt.server.entry.ChartEntry;
import com.wnt.server.entry.LogEntry;
import com.wnt.server.order.ConstantsDefs;
import com.wnt.server.order.ConstantsServer;
import com.wnt.server.order.TypeCmd;
import com.wnt.web.socket.service.SocketService;

import common.EquipmentDef;

/**
 * 接收事件日志
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class LogThread implements Runnable {

	
	private SocketService socketService;
	List<LogEntry> listLog = new ArrayList<LogEntry>();
	
	int count=0;
//	int logn = 0;
	public LogThread(SocketService socketService) {
		this.socketService=socketService;
	}

	@Override
	public void run() {
		try {
			byte[] bt =new byte[1024];
			ConstantsServer.LOGS++;
//			logn++;
			while (true) {
				if(EHCacheUtil.get("log"+ConstantsServer.LOGS)!=null){
//					System.out.println("log");
					LogEntry logEntry =new LogEntry();
					//获取缓存中数据
					bt=(byte[])EHCacheUtil.get("log"+ConstantsServer.LOGS);
					EHCacheUtil.remove("log"+ConstantsServer.LOGS);  
					ConstantsServer.LOGS++;
//					String str = "";
//					for (int i = 0; i < bt.length; i++) {
//						str = str + bt[i] + " ";
//					}
//					System.out.println(str);
//					bt=(byte[])EHCacheUtil.get("r"+TypeCmd.LOG_EVENT);
//					EHCacheUtil.remove("r"+TypeCmd.LOG_EVENT);
					//事件来源
					int srcType=SocketEntityUtil.byteToInt(bt, 16, 2);
//					System.out.println("srcType="+srcType);
					//事件信息
					int infoType=SocketEntityUtil.byteToInt(bt, 18, 2);
//					System.out.println("infoType="+infoType);
					//事件取值
					int EventValue1= SocketEntityUtil.byteToInt(bt, 20, 4);
					//事件取值
					int EventValue2= SocketEntityUtil.byteToInt(bt, 24, 4);
					//测试用例索引
					int testIdx = SocketEntityUtil.byteToInt(bt, 28, 4);					
					//测试用例索引
					int testIdx2 = SocketEntityUtil.byteToInt(bt, 32, 4);
//					System.out.println("testIdx2:"+bt[32]+" "+bt[33]+" "+bt[34]+" "+bt[35]+" ");
					
					logEntry.setNum2(testIdx2);
					
					//判断事件来源分别处理
					switch (srcType){
						//ARP monitor
						case 1:
							logEntry.setSourcetype(srcType);
							logEntry.setMessagetype(infoType);
							listLog.add(logEntry);
							break;
						//ICMP monitor
						case 2:
							logEntry.setSourcetype(srcType);
							logEntry.setMessagetype(infoType);
							listLog.add(logEntry);
							break;
						//TCP monitor
						case 3:
							logEntry.setSourcetype(srcType);
							logEntry.setMessagetype(infoType);
							if(infoType == 3){
								//logEntry.setCode(EventValue1+"-"+EventValue2);
								logEntry.setNum(testIdx);
							}else
							if(infoType == 4){
								//logEntry.setCode(EventValue1+"-"+EventValue2);
								logEntry.setNum(testIdx);
							}
							listLog.add(logEntry);
							break;
						//离散数据 monitor
						case 4:
							logEntry.setSourcetype(srcType);
							logEntry.setMessagetype(infoType);
							listLog.add(logEntry);
							break;
						//语法
						case 5:
							logEntry.setSourcetype(srcType);
							logEntry.setMessagetype(infoType);
							logEntry.setCode(EventValue1+"-"+EventValue2);
							logEntry.setNum(testIdx);
							listLog.add(logEntry);
							break;
						case 6://风暴
							logEntry.setSourcetype(srcType);
							logEntry.setMessagetype(infoType);
							logEntry.setCode(EventValue1+"-"+EventValue2);
							logEntry.setNum(testIdx);
							listLog.add(logEntry);
							break;
						case 7://自定义
							logEntry.setSourcetype(srcType);
							logEntry.setMessagetype(infoType);
							logEntry.setCode(EventValue1+"-"+EventValue2);
							logEntry.setNum(testIdx);
							listLog.add(logEntry);
							break;
						case 8://fuzzer
							logEntry.setSourcetype(srcType);
							logEntry.setMessagetype(infoType);
							logEntry.setCode(EventValue1+"-"+EventValue2);
							logEntry.setNum(testIdx);
							listLog.add(logEntry);
							break;
						case 9://用来显示回溯时候的状态		
							System.out.println("infoType:"+infoType);
							if(infoType == 6){
								if(testIdx2 ==0){
									EquipmentDef.logProgressDisplay = String.valueOf(testIdx);
								}else{
									EquipmentDef.logProgressDisplay = String.valueOf(testIdx)+"--"+String.valueOf(testIdx2);
								}
							}else{
								EquipmentDef.logProgressDisplay = ConstantsDefs.LogError[infoType];
							}
							if(infoType == 9){
								if(!(testIdx==0 && testIdx2 ==0 )){
								 logEntry.setSourcetype(srcType);
								 logEntry.setMessagetype(infoType);
								 logEntry.setCode(EventValue1+"-"+EventValue2);
								 logEntry.setNum(testIdx);
								 logEntry.setNum2(testIdx2);
								 listLog.add(logEntry);
								}
							}
							break;
						case 10: //测试执行异常
					        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					        Date date = new Date();
					        String res = simpleDateFormat.format(date);
							System.out.println("This is logthread timeStamp :"+res);
							EHCacheUtil.put("CONNECT_ERROR",infoType);
							break;
					}
				}
				if (count>=5&&listLog.size()>0) {
					try{
					//socketService.addChart(listChart);
					socketService.addLog(listLog);
					
					}catch(Exception e){
						e.printStackTrace();
					}
					listLog.clear();
//					listLog = null;
//					listLog = new ArrayList<LogEntry>();
					count=0;
				}else if(count>=5){
					count=0;
				}
				count++;
				Thread.sleep(1);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		
		}
	}
}
