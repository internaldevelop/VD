package com.wnt.server.order;

import java.util.List;
import java.util.Map;

import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.SocketEntityUtil;

/**
 * 前端封装发送信息方法
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */

public class SealedSendMessage {
	/**
	 * 发送端口ip
	 * 
	 * @param targeip
	 *            被测设备的IP
	 * @param localip
	 *            测试设备的IP
	 */
	public static void getConnectIp(String localip, String targeip,int dev) {
		byte[] bt = Order.getConnectIp(localip, targeip,dev);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//System.out.println("将环境监测设置命令放入缓存的key："+"s" + ConstantsServer.SENTFLAG);
//		for(int i =0;i<bt.length;i++){
//			System.out.println(bt[i]+"  ");
//			if(i%16 ==0){
//				System.out.println();
//			}
//		}
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
//		System.out
//				.println("ConstantsDefs.SENTFLAG=" + ConstantsServer.SENTFLAG);
	}

	/**
	 * 执行测试用例
	 * 
	 * @param tgroup
	 *            测试组
	 * @param tcase
	 *            测试用例
	 */
	public static void getStartTest(int tgroup, int tcase) {
		byte[] bt = Order.getStartTest(tgroup, tcase);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//System.out.println("kai shi ce shi ming ling zu bao："+"s" + ConstantsServer.SENTFLAG);
//		for(int i =0;i<bt.length;i++){
//			System.out.println(bt[i]+"  ");
//			if(i%16 ==0){
//				System.out.println();
//			}
//		}
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 暂停测试用例
	 */
	public static void getPauseTest() {
		byte[] bt = Order.getPauseTest();
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 停止测试用例
	 */
	public static void getStopTest() {
		byte[] bt = Order.getStopTest();
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 继续测试用例
	 */
	public static void getContinueTest() {
		byte[] bt = Order.getContinueTest();
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 开启监控器
	 * 
	 * @param monitor
	 *            监视器类型
	 * @param attr
	 *            当monitor为非离散数据监视器时为超时时间，否则为离散数据的周期
	 * @param Idx
	 *            离散数据接口索引
	 */
	public static void getStartMonitor(int monitor, int attr, int Idx, int level) {
		byte[] bt = Order.getStartMonitor(monitor, attr, Idx, level);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 关闭监控器
	 * 
	 * @param monitor
	 *            监视器类型
	 */
	public static void getStopMonitor(int monitor) {
		byte[] bt = Order.getStopMonitor(monitor);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 开始端口扫描
	 * 
	 * @param scan_type
	 *            扫描类型 主动0 被动1
	 * @param proto_type
	 *            协议类型 TCP:6 UDP:17
	 * @param port_s
	 *            起始端口
	 * @param port_e
	 *            结束端口
	 */
	public static void getStartScan(int scan_type, int proto_type, int port_s,
			int port_e) {
		byte[] bt = Order.getStartScan(scan_type, proto_type, port_s, port_e);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 停止端口扫描
	 */
	public static void getStopScan() {
		byte[] bt = Order.getStopScan();
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}
	
	/**
	 * 开始动态端口扫描 
	 * @param ip		ip 地址
	 * @param scan_type 扫描类型
	 */
	public static void getStartDynamicScan(String ip,int scan_type,int portType){
		//获取IP 地址的byte 数组
		byte[] ipb = SocketEntityUtil.ipTobyte(ip);
		//获取扫描类型的byte 数组
		byte[] stb = SocketEntityUtil.intToBytes(scan_type, 2);
		
		byte[] pt = SocketEntityUtil.intToBytes(portType,2);
		
		//socket 传送的二进制数组 
		byte[] bt = new byte[14];
		int i = 0;
		int index = 0;
		for (i = 0; i < 4; i++) {
			bt[i] = ipb[i];
		}
		index = index + 4;
		for (i = 0; i < 2; i++) {
			bt[index + i] = stb[i];
		}
		index = index + 2;
		for(i = 0;i<2;i++){
			bt[index+i] = pt[i];
		}
		byte[] rb = Order.getProtocol(33, TypeCmd.CMD_DYNAMIC_START_TEST, TypeTlv.T_DYNAMIC_START, 14, bt);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, rb);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}
	
	/**
	 * 停止动态端口扫描
	 */
	public static void getStopDynamicScan(){
		byte[] rb = Order.getProtocol(17, TypeCmd.CMD_DYNAMIC_STOP_TEST, 0, 0, null);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, rb);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(rb);
	}
	
	
	

	/**
	 * 日志信息
	 */
	public static void getLogEvent(int Event_type, String Event_value) {
		byte[] bt = Order.getLogEvent(Event_type, Event_value);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 设置IP
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	public static void setIp(String ip) {
		byte[] bt = Order.getIp(ip);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 
	 * @param type
	 * @param value
	 */
	public static void getPortUser(int type, int value) {
		byte[] bt = Order.getPortUser(type, value);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 风暴测试
	 * 
	 * @param test_rate
	 *            测试速率： 关闭：取值为最大速率1488000 限速：1到1488000
	 * @param test_time
	 *            测试时间 全局：120秒 Duration：1秒到4294967295（一个ULONG）
	 */
	public static void getTormSetup(int test_rate, long test_time, String uuid,
			int Power_m) {
		byte[] bt = Order.getTormSetup(test_rate, test_time, 0, uuid, Power_m);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 语法测试
	 * 
	 * @param First_subtest
	 *            起始测试用例 First in set：设置为0
	 *            Production：最小值为1，最大值是设置为0到4294967295（一个ULONG）
	 * @param Last_subtest
	 *            终止测试用例
	 * @param Fault_isolation
	 *            问题溯源 开启：1 关闭：0
	 * @param Packet_cap
	 *            抓包 开启：1 关闭：0
	 * @param Target_dev
	 *            目标 全局：0 DUT1：1
	 */
	public static void getGrammarSetup(long First_subtest, long Last_subtest,
			long Packet_rate, long Percent, long Packet_total,
			int Fault_isolation, int Packet_cap, int Target_dev, int Power_m,
			String uuid, int port) {
		byte[] bt = Order.getGrammarSetup(First_subtest, Last_subtest,
				Packet_rate, Percent, Packet_total, Fault_isolation,
				Packet_cap, Target_dev, Power_m, uuid, port);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	/**
	 * 电源管理
	 * 
	 * @param Dev_type
	 * @param Action
	 * @return
	 */
	public static void getShutdown(long Dev_type, long Action) {
		byte[] bt = Order.getShutdown(Dev_type, Action);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}

	public static void getCustom(String name, long len,
			List<Map<String, Object>> list) {
		int hlen = 12;
		byte[] bt = new byte[1024 * 5];
		byte[] b1 = Order.getCustomInfo(name, 0, len, list.size());
		System.arraycopy(b1, 0, bt, hlen, b1.length);
		hlen = hlen + b1.length;
		int max = 0;
		int min = 0;

		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> m = list.get(i);
			StringBuffer tempstr = new StringBuffer();
			String[] tempv = m.get("FIELDVALUE").toString().split(",");
			for (int n = 0; n < tempv.length; n++) {
				String[] tempr = tempv[n].split("-");
				if (tempr.length != 1) {
					min = Integer.parseInt(tempr[0]);
					max = Integer.parseInt(tempr[1]);
					byte[] btm = Order.getCustomRand(m.get("FIELDNAME").toString(), (Integer) m.get("FIELDLEN"), max, min);
					System.arraycopy(btm, 0, bt, hlen, btm.length);
					hlen = hlen + btm.length;
				} else {
					tempstr.append(tempr[0] + ",");
				}
			}
			if (tempstr.length() != 0) {
				String rtp = tempstr.toString();
				rtp = rtp.substring(0, rtp.length() - 1);
				String[] temp = rtp.split(",");
				Long[] lv = new Long[temp.length];
				for (int j = 0; j < lv.length; j++) {
					lv[j] = Long.parseLong(temp[j]);
				}
				byte[] btm = Order.getCustomEnum(m.get("FIELDNAME").toString(),
						(Integer) m.get("FIELDLEN"), temp.length, lv);
				System.arraycopy(btm, 0, bt, hlen, btm.length);
				hlen = hlen + btm.length;
			}
		}
		

		// if(m.get("FIELDVALUE").toString().contains("-")){
		// String[] temp = m.get("FIELDVALUE").toString().split("-");
		// if(temp.length ==1){
		// min = Integer.parseInt(temp[0]);
		// max = min;
		// }else{
		// min = Integer.parseInt(temp[0]);
		// max = Integer.parseInt(temp[1]);
		// }
		// byte[] btm = Order.getCustomRand(m.get("FIELDNAME").toString(),
		// (Integer)m.get("FIELDLEN"), max, min);
		// System.arraycopy(btm,0,bt,hlen,btm.length);
		// hlen = hlen+btm.length;
		// }else{
		// String[] temp = m.get("FIELDVALUE").toString().split(",");
		// Long[] lv = new Long[temp.length];
		// for(int j=0;j<lv.length;j++){
		// lv[j] = Long.parseLong(temp[j]);
		// }
		// byte[] btm = Order.getCustomEnum(m.get("FIELDNAME").toString(),
		// (Integer)m.get("FIELDLEN"), temp.length, lv);
		// System.arraycopy(btm,0,bt,hlen,btm.length);
		// hlen = hlen+btm.length;
		// }

		byte[] b2 = Order.getCustomHead(hlen + 5);
		System.arraycopy(b2, 0, bt, 0, b2.length);
		System.arraycopy(Order.endbt, 0, bt, hlen, Order.endbt.length);
		hlen = hlen + 5;
		byte[] sbt = new byte[hlen];
		System.arraycopy(bt, 0, sbt, 0, hlen);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, sbt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(sbt);
	}
	
	public static void getDelPort(int port, int pro_type,int del){
		byte[] bt = Order.getDelPort(port, pro_type, del);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}
	
	public static void getLinkType(int type){
		byte[] bt = Order.getLinkType(type);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}
	/**
	 * 下发应用安全测试的配置
	 * 
	 * @param test_name
	 *            测试名称
	 * @param target_ip
	 *            被测主机ip
	 * @param scan_type
	 *            扫描类型0代表快速扫描1代表深度扫描
	 */
	public static void getAppReadyStartTest(String test_name, String target_ip, int scan_type) {
		byte[] bt = Order.getAppReadyStartTest(test_name, target_ip, scan_type);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		ConstantsServer.sendOrderQueue.offer(bt);
	}
	/**
	 * 下发开始应用安全测试命令
	 */
	public static void getCmdAppStartTest() {
		byte[] bt = Order.getCmdAppStartTest();
		ConstantsServer.sendOrderQueue.offer(bt);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
	}
	/**
	 * 下发停止应用安全测试命令
	 */
	public static void getCmdAppStopTest() {
		byte[] bt = Order.getCmdAppStopTest();
		ConstantsServer.sendOrderQueue.offer(bt);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
	}
	
	/**
	 * 下发硬件开始检查命令
	 * @author gyk
	 * @data 2016年12月15日
	 */
	public static void startHardWareCheck(){
		byte[] bt = Order.getStartHardWareCheck();
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		ConstantsServer.sendOrderQueue.offer(bt);
		EHCacheUtil.remove("r"+TypeCmd.CMD_HARDWARE_CHECK_START);
		//ConstantsServer.SENTFLAG++;
	}
	
	/**
	 * 下发硬件停止检查命令
	 * 
	 * @author gyk
	 * @data 2016年12月15日
	 */
	public static void stopHardWareCheck(){
		byte[] bt = Order.getStopHardWareCheck();
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		ConstantsServer.sendOrderQueue.offer(bt);
		EHCacheUtil.remove("r"+TypeCmd.CMD_HARDWARE_CHECK_START);
		//ConstantsServer.SENTFLAG++;
	}

	/**
	 * 
	 * @param ys
	 * @author gyk
	 * @data 2016年12月15日
	 */
	public static void checkDoOutStatus(int[] ys) {
		byte[] bt =  Order.getCheckDoOutStatusData(ys);
//		System.out.println("打印下发命令！");
//		for(byte b : bt){
//			System.out.print(b+"\t");
//		}
//		System.out.println();
		ConstantsServer.sendOrderQueue.offer(bt);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
	}

	/** 
	 * @Title: getMulticastUser 
	 * @Description: TODO
	 * @param ipAddr
	 * @return: void
	 */
	
	/**
	 * 
	 * @param type
	 * @param value
	 */
	public static void getMulticastUser(String ipAddr) {
		byte[] bt = Order.getMulticastUser(ipAddr);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		ConstantsServer.sendOrderQueue.offer(bt);
		//ConstantsServer.SENTFLAG++;
	}

	/** 
	 * @Title: getStartIdentity 
	 * @Description: TODO
	 * @return: void
	 */
	public static void getStartIdentity() {
		byte[] bt = Order.getStartIdentity();
		ConstantsServer.sendOrderQueue.offer(bt);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		
	}

	/** 
	 * @Title: getStopIdentity 
	 * @Description: TODO
	 * @return: void
	 */
	public static void getStopIdentity() {
		
		byte[] bt = Order.getStopIdentity();
		ConstantsServer.sendOrderQueue.offer(bt);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		
	}

	/** 
	 * @Title: getStartGrabPacket 
	 * @Description: TODO
	 * @param name
	 * @param status
	 * @param delStatus
	 * @return: void
	 */
	public static void getGrabPacket(String name, int status, int delStatus) {
		byte[] bt = Order.getGrabPacket(name,status,delStatus);
		ConstantsServer.sendOrderQueue.offer(bt);
		//EHCacheUtil.put("s" + ConstantsServer.SENTFLAG, bt);
		//ConstantsServer.SENTFLAG++;
		
	}
}
