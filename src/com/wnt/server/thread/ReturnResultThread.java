package com.wnt.server.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.activation.CommandMap;

import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.LogUtil;
import org.wnt.core.uitl.SocketEntityUtil;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.server.entry.ChartEntry;
import com.wnt.server.entry.PortEntry;
import com.wnt.server.order.ConstantsServer;
import com.wnt.server.order.TypeCmd;
import com.wnt.web.dynamicports.entry.DynamicPorts;
import com.wnt.web.socket.service.SocketService;

/**
 * 返回结果子线程
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class ReturnResultThread implements Runnable {

	private SocketService socketService;
	List<ChartEntry> listChart = new ArrayList<ChartEntry>();
	List<PortEntry> listPort = new ArrayList<PortEntry>();
	List<DynamicPorts> listDps = new ArrayList<DynamicPorts>();
	
	int count = 0;
	
	// int returnn = 0;//自增
	public ReturnResultThread(SocketService socketService) {
		this.socketService = socketService;
	}

	@Override
	public void run() {
		try {
			byte[] bt = new byte[1024];
			ConstantsServer.STATERE++;
//			String str = "";
			// returnn++;
			while (true) {				
//				str = "";
				if (EHCacheUtil.get("start" + ConstantsServer.STATERE) != null) {			
					//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss|SSS");

			       
//					System.out.println("return");
					// System.out.println("no");
					// 获取缓存中数据
					ChartEntry chartEntry = new ChartEntry();
					PortEntry portEntry = new PortEntry();
					bt = (byte[]) EHCacheUtil.get("start"
							+ ConstantsServer.STATERE);
//					for (int i = 0; i < bt.length; i++) {
//						str = str + bt[i] + " ";
//					}
					// LogUtil.info("收到数据："+str+" start"+ConstantsDefs.STATERE);

					EHCacheUtil.remove("start" + ConstantsServer.STATERE);
					ConstantsServer.STATERE++;
					// bt=(byte[])EHCacheUtil.get("r"+TypeCmd.STATE_RESPONSE);
					// EHCacheUtil.remove("r"+TypeCmd.STATE_RESPONSE);

					// Type类型
					int type = SocketEntityUtil.byteToInt(bt, 12, 2);
					// System.out.println("type"+type);
					// 状态类型
					int statetype = SocketEntityUtil.byteToInt(bt, 16, 4);
					// System.out.println("statetype"+statetype);
					
					//LogUtil.info("chart 测试 \""+statetype+"\" 正在运行;\t"+ DataUtils.formatDateTime());
					
					/*
					 * if(statetype>100){ for(int i=0;i<30;i++){
					 * System.out.println(bt[i]);
					 * 
					 * } System.out.println(SocketEntityUtil.byteToString(bt, 0,
					 * 5)); System.out.println(SocketEntityUtil.byteToInt(bt, 5,
					 * 1)); System.out.println(SocketEntityUtil.byteToInt(bt, 6,
					 * 1)); System.out.println(SocketEntityUtil.byteToInt(bt, 7,
					 * 1)); System.out.println(SocketEntityUtil.byteToInt(bt, 8,
					 * 1)); System.out.println(SocketEntityUtil.byteToInt(bt, 9,
					 * 1)); System.out.println(SocketEntityUtil.byteToInt(bt,
					 * 10, 1));
					 * System.out.println(SocketEntityUtil.byteToInt(bt, 11,
					 * 1)); System.out.println(type);
					 * System.out.println(SocketEntityUtil.byteToInt(bt, 14,
					 * 2)); }
					 */
					// 状态值
					int statevalue = SocketEntityUtil.byteToInt(bt, 24, 4);
					// System.out.println("statevalue"+statevalue);
					// 状态说明

					// 判断状态类型分别处理
					switch (statetype) {
					/*// ARP时延
					case 1:
						chartEntry.setMonitorid(statetype);
//						System.out.println("ARP数值："+statevalue);
//						System.out.println("ARP缓存值："+EHCacheUtil.get("marp"));
						if (((float)statevalue/(float)100) < ((Integer) EHCacheUtil.get("marp"))) {
							chartEntry.setNum1((float)statevalue/100);
						}
						chartEntry.setTimestamp(DataUtils.getMillis());
						chartEntry.setCreatetime(DataUtils.formatDateTime());
						listChart.add(chartEntry);
						//System.out.println("arp:"+chartEntry+" "+simpleDateFormat.format(new Date()));
						//ConstantsServer.chatnamespace.getBroadcastOperations().sendEvent("message", chartEntry);
						break;
					// ICMP时延
					case 2:
						chartEntry.setMonitorid(statetype);
//						System.out.println("ICMP数值："+statevalue);
//						System.out.println("ICMP缓存值："+EHCacheUtil.get("micmp"));
						if (((float)statevalue/(float)100) < ((Integer) EHCacheUtil.get("micmp"))) {
							chartEntry.setNum1((float)statevalue/100);
						}			
						chartEntry.setTimestamp(DataUtils.getMillis());
						chartEntry.setCreatetime(DataUtils.formatDateTime());
						listChart.add(chartEntry);
						//System.out.println("icmp:"+chartEntry+" "+simpleDateFormat.format(new Date()));
						//ConstantsServer.chatnamespace.getBroadcastOperations().sendEvent("message", chartEntry);
						break;
					// TCP
					case 3:
						chartEntry.setMonitorid(statetype);
						chartEntry.setNum(statevalue);
						chartEntry.setTimestamp(DataUtils.getMillis());
						chartEntry.setCreatetime(DataUtils.formatDateTime());
						listChart.add(chartEntry);
						//System.out.println("tcp:"+chartEntry+" "+simpleDateFormat.format(new Date()));
						//ConstantsServer.chatnamespace.getBroadcastOperations().sendEvent("message", chartEntry);
						break;
					// 离散数据
					case 4:
						chartEntry.setMonitorid(statetype);
//						System.out.println("离散数值："+statevalue);
//						System.out.println("离缓存值："+EHCacheUtil.get("mls"));
						if (statevalue < ((Integer) EHCacheUtil.get("mls"))) {
							chartEntry.setNum(statevalue);
						}
						chartEntry.setNum(statevalue);
						chartEntry.setTimestamp(DataUtils.getMillis());
						chartEntry.setCreatetime(DataUtils.formatDateTime());
						listChart.add(chartEntry);
						//ConstantsServer.chatnamespace.getBroadcastOperations().sendEvent("message", chartEntry);
						break;*/
					// 测试进度
					case 5:
						EHCacheUtil.put("r" + TypeCmd.STATE_RESPONSE
								+ statetype, statevalue);
						LogUtil.info("返回测试进度=" + statevalue);
//						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:|SSS");
//						System.out.println("放入缓存的进度 :"+statevalue +"  "+simpleDateFormat.format(new Date()));	
						break;
					// 测试结束
					case 6:
						DynamicPorts dp = new DynamicPorts();
						dp.setPortType(statetype);
						dp.setPortNum(statevalue);
						listDps.add(dp);
						//把结果缓存
						EHCacheUtil.put("dynamicports", listDps);
						LogUtil.info("收到动态扫描数据 TCP协议");
						break;	
					// 端口扫描进度
					case 7:
						EHCacheUtil.put("r" + TypeCmd.STATE_RESPONSE
								+ statetype, statevalue);
//						System.out.println("端口进度=" + statevalue);
						LogUtil.info("端口进度=" + statevalue);
						break;
					// 端口
					case 8:
						// 协议类型
						portEntry.setPorttype(SocketEntityUtil.byteToInt(bt,
								20, 4));
						portEntry.setPortnum(statevalue);
						portEntry.setName(SocketEntityUtil.byteToString(bt, 28,
								40));
						portEntry.setId(UUIDGenerator.getUUID());
						//从内存中取得当前扫描类型,0为主动扫描,1为被动扫描
						int scantype = Integer.parseInt(EHCacheUtil.get("scanType").toString());
						portEntry.setScantype(scantype);	
						//查询端口是否已添加
						List<Map<String,Object>> listport =socketService.findPort(portEntry);
						if(listport.size()!=0&&listport!=null){
							socketService.deletePort(portEntry);
							listPort.add(portEntry);
						}else{
							listPort.add(portEntry);
						}
						// System.out.println(statevalue);
						
						break;
					// 端口
					case 9:

						// 端口扫描结束
					case 10:

						break;
					// 网络中断
					case 11:

						break;
					// 网络恢复
					case 12:

						break;
					// 端口0接受流量
					/*case 13:
						chartEntry.setMonitorid(statetype);
						chartEntry.setNum(statevalue);
						chartEntry.setTimestamp(DataUtils.getMillis());
						chartEntry.setCreatetime(DataUtils.formatDateTime());
						listChart.add(chartEntry);
						//System.out.println("eth0 接收:"+chartEntry+" "+simpleDateFormat.format(new Date()));
						//ConstantsServer.chatnamespace.getBroadcastOperations().sendEvent("message", chartEntry);
						break;
					// 端口0发送流量
					case 14:
//						System.out.println("发送数值："+statevalue);
						chartEntry.setMonitorid(statetype);
						chartEntry.setNum(statevalue);
						chartEntry.setTimestamp(DataUtils.getMillis());
						chartEntry.setCreatetime(DataUtils.formatDateTime());
						listChart.add(chartEntry);
						//System.out.println("eth0 发送:"+chartEntry+" "+simpleDateFormat.format(new Date()));
						//ConstantsServer.chatnamespace.getBroadcastOperations().sendEvent("message", chartEntry);
						break;
					// 端口1接受流量
					case 15:
						chartEntry.setMonitorid(statetype);
						chartEntry.setNum(statevalue);
						chartEntry.setTimestamp(DataUtils.getMillis());
						chartEntry.setCreatetime(DataUtils.formatDateTime());
						listChart.add(chartEntry);
						//System.out.println("eth01接收:"+chartEntry+" "+simpleDateFormat.format(new Date()));
						//ConstantsServer.chatnamespace.getBroadcastOperations().sendEvent("message", chartEntry);
						break;
					// 端口1发送流量
					case 16:
						chartEntry.setMonitorid(statetype);
						chartEntry.setNum(statevalue);
						chartEntry.setTimestamp(DataUtils.getMillis());
						chartEntry.setCreatetime(DataUtils.formatDateTime());
						listChart.add(chartEntry);
						//System.out.println("eth01发送:"+chartEntry+" "+simpleDateFormat.format(new Date()));
						//ConstantsServer.chatnamespace.getBroadcastOperations().sendEvent("message", chartEntry);
						break;*/
					//动态端口扫描	
					case 17: 
						dp = new DynamicPorts();
						dp.setPortType(statetype);
						dp.setPortNum(statevalue);
						LogUtil.info("收到动态扫描数据UDP ");
						listDps.add(dp);
						//把结果缓存
						EHCacheUtil.put("dynamicports", listDps);
						break;
						// 应用安全测试的进度
					case 18:
						EHCacheUtil.put("r" + TypeCmd.STATE_RESPONSE
								+ statetype, statevalue);
						LogUtil.info("应用安全测试的进度=" + statevalue);
						break;
					case 19:
						EHCacheUtil.put("r" + TypeCmd.STATE_RESPONSE
								+ statetype, statevalue);
						LogUtil.info("动态端口="+statevalue);
						break;
					}
				}
				if (count >= 30 && (listChart.size() > 0 || listPort.size() > 0)) {
					try {
						//socketService.addChart(listChart);				
						socketService.addPort(listPort);
					} catch (Exception e) {
						e.printStackTrace();
					}
					listPort.clear();
					//listChart.clear();
					count = 0;
				}else if(count >= 30){
					count = 0;
				}
				count++;

				Thread.sleep(1);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();

		}
	}
}
