package com.wnt.server.order;

import java.util.Arrays;

import net.sf.ehcache.pool.impl.FromLargestCachePoolEvictor;

import org.wnt.core.uitl.SocketEntityUtil;

import com.google.common.primitives.Bytes;
import com.wnt.web.system.entry.Sys;

/**
 * 发送信息
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */

public class Order {

	// 头部
	public static String head = "wmsgb";
	public static byte[] headbt = head.getBytes();

	// 尾部
	public static String end = "wmsge";
	public static byte[] endbt = end.getBytes();

	// 通用
	public static byte[] getProtocol(int hlen, int cmd, int type, int elen,
			byte[] temp) {

		byte[] bt = new byte[hlen]; // 定义字节
		int i = 0;
		for (i = 0; i < 5; i++) {
			bt[i] = headbt[i];
		}
		bt[5] = 0; // 版本号

		if (hlen < 128) {
			bt[6] = 0; // 长度的高字节
			bt[7] = (byte) hlen; // 长度的低字节
		} else {
			byte[] tp = SocketEntityUtil.intToBytes(hlen, 2);
			bt[6] = tp[0]; // 长度的高字节
			bt[7] = tp[1]; // 长度的低字节
		}
		bt[8] = 0; // 机器高字节
		bt[9] = 0; // 机器低字节
		bt[10] = (byte) cmd; // 命令
		bt[11] = 0; // 保留

		if (type != 0) { // 表明有体
			if (type < 128) { // TLV的类型
				bt[12] = 0;
				bt[13] = (byte) type;
			} else {
				byte[] tp = SocketEntityUtil.intToBytes(type, 2);
				bt[12] = tp[0];
				bt[13] = tp[1];
			}

			if (elen < 128) { // Value的长度
				bt[14] = 0;
				bt[15] = (byte) elen;
			} else {
				byte[] tp = SocketEntityUtil.intToBytes(elen, 2);
				bt[14] = tp[0];
				bt[15] = tp[1];
			}

			for (i = 0; i < hlen - 21; i++) { // 去除头部、尾部、体部前四个字节
				bt[16 + i] = temp[i];
			}

			i = 16 + i;
			for (int j = 0; j < 5; j++) {
				bt[i + j] = endbt[j];
			}
		} else {
			for (i = 0; i < 5; i++) {
				bt[12 + i] = endbt[i];
			}
		}
		return bt;
	}

	// 心跳
	public static byte[] getHeartbeat() {
		return getProtocol(17, TypeCmd.HELLO, 0, 0, null);
	}

	/**
	 * 发送端口ip
	 * 
	 * @param targeip
	 *            被测设备的IP
	 * @param localip
	 *            测试设备的IP
	 */
	public static byte[] getConnectIp(String localip, String targeip,int dev) {
		int i = 0;
		byte[] bt = new byte[12];

		byte[] ipb = SocketEntityUtil.ipTobyte(targeip);
		for (i = 0; i < 4; i++) {
			bt[0 + i] = ipb[i];
		}

		ipb = SocketEntityUtil.ipTobyte(localip);
		for (i = 0; i < 4; i++) {
			bt[4 + i] = ipb[i];
		}
		
		bt[8] = (byte)dev;
//		ipb = SocketEntityUtil.intToBytes(0, 4);
//		for (i = 0; i < 4; i++) {
//			bt[8 + i] = ipb[i];
//		}

		return getProtocol(33, TypeCmd.CONNECT, TypeTlv.T_CONNECT, 12, bt);
	}

	/**
	 * 执行测试用例
	 * 
	 * @param tgroup
	 *            测试组
	 * @param tcase
	 *            测试用例
	 */
	public static byte[] getStartTest(int tgroup, int tcase) {
		int i = 0;
		byte[] bt = new byte[4];

		byte[] ipb = SocketEntityUtil.intToBytes(tgroup, 2);
		for (i = 0; i < 2; i++) {
			bt[0 + i] = ipb[i];
		}

		ipb = SocketEntityUtil.intToBytes(tcase, 2);
		for (i = 0; i < 2; i++) {
			bt[2 + i] = ipb[i];
		}

		return getProtocol(25, TypeCmd.START_TEST, TypeTlv.T_START_TEST, 4, bt);
	}

	/**
	 * 暂停测试用例
	 */
	public static byte[] getPauseTest() {
		return getProtocol(17, TypeCmd.PAUSE_TEST, 0, 0, null);
	}

	/**
	 * 停止测试用例
	 */
	public static byte[] getStopTest() {
		return getProtocol(17, TypeCmd.STOP_TEST, 0, 0, null);
	}

	/**
	 * 继续测试用例
	 */
	public static byte[] getContinueTest() {
		return getProtocol(17, TypeCmd.CONTINUE_TEST, 0, 0, null);
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
	public static byte[] getStartMonitor(int monitor, int attr, int Idx,
			int level) {
		int i = 0;
		byte[] bt = new byte[12];

		byte[] ipb = SocketEntityUtil.intToBytes(monitor, 4);
		for (i = 0; i < 4; i++) {
			bt[0 + i] = ipb[i];
		}

		// attr = attr * 1000;// 转化为毫秒
		ipb = SocketEntityUtil.intToBytes(attr, 2);
		for (i = 0; i < 2; i++) {
			bt[4 + i] = ipb[i];
		}
		ipb = SocketEntityUtil.intToBytes(Idx, 2);
		for (i = 0; i < 2; i++) {
			bt[6 + i] = ipb[i];
		}

		ipb = SocketEntityUtil.intToBytes(level, 2);
		for (i = 0; i < 2; i++) {
			bt[8 + i] = ipb[i];
		}
		// ipb = SocketEntityUtil.intToBytes(0, 4);
		// for (i = 0; i < 4; i++) {
		// bt[8 + i] = ipb[i];
		// }
		return getProtocol(33, TypeCmd.START_MONITOR, TypeTlv.T_MONITOR, 12, bt);
	}

	/**
	 * 关闭监控器
	 * 
	 * @param monitor
	 *            监视器类型
	 */
	public static byte[] getStopMonitor(int monitor) {
		int i = 0;
		byte[] bt = new byte[4];

		byte[] ipb = SocketEntityUtil.intToBytes(monitor, 4);
		for (i = 0; i < 4; i++) {
			bt[0 + i] = ipb[i];
		}

		return getProtocol(25, TypeCmd.STOP_MONITOR, TypeTlv.T_MONITOR, 4, bt);
	}

	/**
	 * 开始端口扫描
	 * 
	 * @param Scan_type
	 *            扫描类型
	 * @param proto_type
	 *            协议类型
	 * @param Port_s
	 *            起始端口
	 * @param Port_e
	 *            结束端口
	 */
	public static byte[] getStartScan(int Scan_type, int proto_type,
			int Port_s, int Port_e) {
		int i = 0;
		byte[] bt = new byte[12];
		byte[] ipb = SocketEntityUtil.intToBytes(Scan_type, 2);
		for (i = 0; i < 2; i++) {
			bt[0 + i] = ipb[i];
		}
		ipb = SocketEntityUtil.intToBytes(proto_type, 2);
		for (i = 0; i < 2; i++) {
			bt[2 + i] = ipb[i];
		}
		ipb = SocketEntityUtil.intToBytes(Port_s, 2);
		for (i = 0; i < 2; i++) {
			bt[4 + i] = ipb[i];
		}
		ipb = SocketEntityUtil.intToBytes(Port_e, 2);
		for (i = 0; i < 2; i++) {
			bt[6 + i] = ipb[i];
		}
		ipb = SocketEntityUtil.intToBytes(0, 4);
		for (i = 0; i < 2; i++) {
			bt[8 + i] = ipb[i];
		}
		return getProtocol(33, TypeCmd.START_SCAN, TypeTlv.T_CMD_SCAN, 12, bt);
	}

	/**
	 * 停止端口扫描
	 */
	public static byte[] getStopScan() {
		 return getProtocol(17, TypeCmd.STOP_SCAN, 0, 0, null);
	}

	/**
	 * 日志信息
	 */
	public static byte[] getLogEvent(int Event_type, String Event_value) {
		int i = 0;
		byte[] bt = new byte[12];
		byte[] ipb = SocketEntityUtil.intToBytes(Event_type, 2);
		for (i = 0; i < 2; i++) {
			bt[0 + i] = ipb[i];
		}
		ipb = SocketEntityUtil.StringToBytes(Event_value, 4);
		for (i = 0; i < 4; i++) {
			bt[2 + i] = ipb[i];
		}
		ipb = SocketEntityUtil.intToBytes(0, 6);
		for (i = 0; i < 2; i++) {
			bt[6 + i] = ipb[i];
		}
		return getProtocol(17, TypeCmd.LOG_EVENT, TypeTlv.T_LOG_EVENT, 12, bt);
	}

	/**
	 * 手动添加端口
	 * 
	 * @param type
	 *            端口类型
	 * @param value
	 *            端口号
	 */
	public static byte[] getPortUser(int type, int value) {
		int i = 0;
		byte[] bt = new byte[4];
		byte[] ipb = SocketEntityUtil.intToBytes(type, 2);
		for (i = 0; i < 2; i++) {
			bt[0 + i] = ipb[i];
		}
		ipb = SocketEntityUtil.intToBytes(value, 2);
		for (i = 0; i < 2; i++) {
			bt[2 + i] = ipb[i];
		}
		return getProtocol(25, TypeCmd.CMD_PORT_USER, TypeTlv.T_PORT_USER, 4,
				bt);
	}

	/**
	 * 设置IP
	 * 
	 * @param ip
	 * @return
	 */
	public static byte[] getIp(String ip) {
		int i = 0;
		byte[] bt = new byte[20];

		byte[] ipb = SocketEntityUtil.intToBytes(5, 4);
		for (i = 0; i < 4; i++) {
			bt[0 + i] = ipb[i];
		}

		ipb = SocketEntityUtil.ipTobyte(ip);
		for (i = 0; i < 4; i++) {
			bt[4 + i] = ipb[i];
		}

		return getProtocol(17 + 4 + 20, TypeCmd.SET_LOCAL_IF,
				TypeTlv.T_CMD_RESULT, 20, bt);
	}

	/**
	 * 电源管理
	 * 
	 * @param Dev_type
	 * @param Action
	 * @return
	 */
	public static byte[] getShutdown(long Dev_type, long Action) {
		int i = 0;
		byte[] bt = new byte[12];

		int btl = 0;
		byte[] ipb = SocketEntityUtil.intToBytes(Dev_type, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(Action, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		return getProtocol(17 + 4 + 12, TypeCmd.SHUTDOWN, TypeTlv.T_SHUTDOWN,
				12, bt);
	}

	/**
	 * 用户自定义
	 * 
	 * @param name
	 * @param id
	 * @param maxlen
	 * @param num
	 * @return
	 */
	public static byte[] getCustomInfo(String name, long id, long maxlen,
			long num) {
		int i = 0;
		int btl = 0;
		byte[] bt = new byte[48];
		 
		bt[0] = 0;
		bt[1] = (byte) TypeTlv.T_CUSTOM_INFO;
		
		bt[2] = 0;
		bt[3] = 44;
		
		btl = 4;
		byte[] ipb = name.getBytes();
		for (i = 0; i < ipb.length; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = 32;
		ipb = SocketEntityUtil.intToBytes(id, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(maxlen, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(num, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}
		
		return bt;
//		return getProtocol(17 + 4 + 44, TypeCmd.CUSTOM, TypeTlv.T_CUSTOM_INFO,
//				44, bt);
	}

	public static byte[] getCustomRand(String name, int bits, long max, long min) {
		int i = 0;
		int btl = 0;
		byte[] bt = new byte[56];
		
		bt[0] = 0;
		bt[1] = (byte) TypeTlv.T_CUSTOM_ITEM_RAND;
		
		bt[2] = 0;
		bt[3] = 52;
		
		btl = 4;
		byte[] ipb = name.getBytes();
		for (i = 0; i < ipb.length; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = 32+btl;
		ipb = SocketEntityUtil.intToBytes(bits, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(max, 8);
		for (i = 0; i < 8; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(min, 8);
		for (i = 0; i < 8; i++) {
			bt[btl + i] = ipb[i];
		}
		
		return bt;
//		return getProtocol(17 + 4 + 52, TypeCmd.CUSTOM,
//				TypeTlv.T_CUSTOM_ITEM_RAND, 52, bt);
	}

	public static byte[] getCustomEnum(String name, int bits, int enumnum,
			Long[] value) {
		int i = 0;
		int btl = 0;
		byte[] bt = new byte[4+44 + value.length * 8];
		
		bt[0] = 0;
		bt[1] = (byte) TypeTlv.T_CUSTOM_ITEM_ENUM;
		
		btl = 2;
		byte[] ipb = SocketEntityUtil.intToBytes(44 + value.length * 8, 2);
		for (i = 0; i < 2; i++) {
			bt[btl + i] = ipb[i];
		}
		
		btl = btl+i;
		
		ipb = name.getBytes();
		for (i = 0; i < ipb.length; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl+32;
		ipb = SocketEntityUtil.intToBytes(bits, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(enumnum, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl+4;
		for (int j = 0; j < value.length; j++) {
			btl = btl + i;
			ipb = SocketEntityUtil.intToBytes(value[j], 8);
			for (i = 0; i < 8; i++) {
				bt[btl + i] = ipb[i];
			}
		}
		
		return bt;
//		return getProtocol(17 + 4 + 44 + value.length * 8, TypeCmd.CUSTOM,
//				TypeTlv.T_CUSTOM_ITEM_ENUM, 44 + value.length * 8, bt);
	}
	
	public static byte[] getCustomHead(int hlen){
		byte[] bt = new byte[12]; // 定义字节
		int i = 0;
		for (i = 0; i < 5; i++) {
			bt[i] = headbt[i];
		}
		bt[5] = 0; // 版本号

		if (hlen < 128) {
			bt[6] = 0; // 长度的高字节
			bt[7] = (byte) hlen; // 长度的低字节
		} else {
			byte[] tp = SocketEntityUtil.intToBytes(hlen, 2);
			bt[6] = tp[0]; // 长度的高字节
			bt[7] = tp[1]; // 长度的低字节
		}
		bt[8] = 0; // 机器高字节
		bt[9] = 0; // 机器低字节
		bt[10] = (byte) TypeCmd.CUSTOM; // 命令
		bt[11] = 0; // 保留
		return bt;
	}

	/**
	 * 风暴测试
	 * 
	 * @param type
	 *            端口类型
	 * @param value
	 *            端口号
	 */
	public static byte[] getTormSetup(int Test_rate, long Test_time,
			int Packet_cap, String uuid,int Power_m) {
		int i = 0;
		byte[] bt = new byte[76];
		byte[] ipb = SocketEntityUtil.intToBytes(Test_rate, 4);
		for (i = 0; i < 4; i++) {
			bt[0 + i] = ipb[i];
		}
		ipb = SocketEntityUtil.intToBytes(Test_time, 4);
		for (i = 0; i < 4; i++) {
			bt[4 + i] = ipb[i];
		}
		
		bt[8] = (byte) Power_m;

		bt[9] = (byte) Packet_cap;

		ipb = uuid.getBytes();
		for (i = 0; i < ipb.length; i++) {
			bt[10 + i] = ipb[i];
		}

		// ipb = SocketEntityUtil.intToBytes(0, 4);
		// for (i = 0; i < 3; i++) {
		// bt[73 + i] = ipb[i];
		// }
		return getProtocol(97, TypeCmd.CMD_STORM_SETUP, TypeTlv.T_STORM_SETUP,
				76, bt);
	}

	/**
	 * 语法测试
	 * 
	 * @param First_subtest
	 *            起始测试用例
	 * @param Last_subtest
	 *            终止测试用例
	 * @param Packet_rate
	 *            发包速率
	 * @Param Percent 正确包的比例
	 * @param Packet_total
	 *            总发包数
	 * @param Fault_isolation
	 *            问题溯源
	 * @param Packet_cap
	 *            抓包
	 * @param Target_dev
	 *            目标
	 * @param Power_m
	 *            电源管理
	 * @param uuid
	 *            文件名称
	 * @param port
	 *            端口号
	 */
	public static byte[] getGrammarSetup(long First_subtest, long Last_subtest,
			long Packet_rate, long Percent, long Packet_total,
			int Fault_isolation, int Packet_cap, int Target_dev, int Power_m,
			String uuid, int port) {
		int i = 0;
		int btl = 0;
		byte[] bt = new byte[92];
		byte[] ipb = SocketEntityUtil.intToBytes(First_subtest, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(Last_subtest, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(Packet_rate, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(Percent, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(Packet_total, 4);
		for (i = 0; i < 4; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(port, 2);
		for (i = 0; i < 2; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		bt[btl] = (byte) Fault_isolation;

		btl++;
		bt[btl] = (byte) Packet_cap;

		btl++;
		bt[btl] = (byte) Target_dev;

		btl++;
		bt[btl] = (byte) Power_m;

		btl++;
		ipb = uuid.getBytes();
		for (i = 0; i < ipb.length; i++) {
			bt[btl + i] = ipb[i];
		}

		return getProtocol(17 + 4 + 92, TypeCmd.CMD_GRAMMAR_SETUP,
				TypeTlv.T_GRAMMAR_SETUP, 92, bt);
	}
	
	/**
	 * 删除端口
	 * 
	 * @param port
	 * @param pro_type
	 * @return
	 */
	public static byte[] getDelPort(int port, int pro_type,int del) {
		int i = 0;
		byte[] bt = new byte[4];

		int btl = 0;
		byte[] ipb = SocketEntityUtil.intToBytes(port, 2);
		for (i = 0; i < 2; i++) {
			bt[btl + i] = ipb[i];
		}

		btl = btl + i;
		bt[btl] = (byte)pro_type;
		
		btl = btl + 1;
		bt[btl] = (byte)del;

		return getProtocol(17 + 4 + 4, TypeCmd.DEL_PORTS, TypeTlv.T_DEL_PORTS,
				4, bt);
	}
	
	/**
	 * 连接类型
	 * 
	 * @param port
	 * @param pro_type
	 * @return
	 */
	public static byte[] getLinkType(int type) {
		int i = 0;
		byte[] bt = new byte[4];

		int btl = 0;
		byte[] ipb = SocketEntityUtil.intToBytes(type, 2);
		for (i = 0; i < 2; i++) {
			bt[btl + i] = ipb[i];
		}
		
		return getProtocol(17 + 4 + 4, TypeCmd.CMD_NET_TOPOLOGY, TypeTlv.T_TOPOLOGY,
				4, bt);
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
	public static byte[] getAppReadyStartTest(String test_name, String target_ip, int scan_type) {
		int i = 0;
		int btl = 0;
		byte[] bt = new byte[76];
		byte[] ipb = SocketEntityUtil.StringToBytes(test_name, 64);
		for (i = 0; i < ipb.length; i++) {
			bt[btl + i] = ipb[i];
		}
		btl = btl + i;
		ipb = SocketEntityUtil.ipTobyte(target_ip);
		for (i = 0; i < ipb.length; i++) {
			bt[btl + i] = ipb[i];
		}
		btl = btl + i;
		ipb = SocketEntityUtil.intToBytes(scan_type, 2);
		for (i = 0; i < ipb.length; i++) {
			bt[btl + i] = ipb[i];
		}
		return getProtocol(17+76, TypeCmd.CMD_APP_READY_START_TEST, 21, 76, bt);
	}
	/**
	 * 下发开始应用安全测试命令
	 */
	public static byte[] getCmdAppStartTest() {
		return getProtocol(17, TypeCmd.CMD_APP_START_TEST, 0, 0, null);
	}
	/**
	 * 下发停止应用安全测试命令
	 */
	public static byte[] getCmdAppStopTest() {
		return getProtocol(17, TypeCmd.CMD_APP_STOP_TEST, 0, 0, null);
	}
	
	/**
	 * 硬件检查开始命令数据包获取
	 * @return
	 * @author gyk
	 * @data 2016年12月15日
	 */
	public static byte[] getStartHardWareCheck(){
		return getProtocol(17, TypeCmd.CMD_HARDWARE_CHECK_START, 0, 0, null);
	}
	
	/**
	 * 获取硬件检查停止命令数据包
	 * @return
	 * @author gyk
	 * @data 2016年12月15日
	 */
	public static byte[] getStopHardWareCheck(){
		return getProtocol(17,TypeCmd.CMD_HARDWARE_CHECK_STOP,0,0,null);
	}
	
	public static byte[] getCheckDoOutStatusData(int[] ys){
		byte[] bt = new byte[8];
		if(ys!=null && ys.length == 3){
			for(int i = 0;i<ys.length;i++){
				byte[] tb = SocketEntityUtil.intToBytes(ys[i],2);
				System.arraycopy(tb, 0, bt, i*2, 2);
			}
		}
		return getProtocol(29,TypeCmd.CMD_DO_WRITE_TEST,TypeTlv.T_DO_WRITE_RESULT,8,bt);
	}

	/** 
	 * @Title: getMulticastUser 
	 * @Description: TODO
	 * @param ipAddr
	 * @return
	 * @return: byte[]
	 */

	public static byte[] getMulticastUser(String ipAddr){
		int i = 0;
		byte[] bt = new byte[8];
		if (!"".equals(ipAddr)) {
			byte[] ipb = SocketEntityUtil.ipTobyte(ipAddr);
			for (i = 0; i < 4; i++) {
				bt[0 + i] = ipb[i];
			}
		}else{
			byte[] ipb = SocketEntityUtil.intToBytes(0,4);
			System.out.println(ipb);
			for (i = 0; i < 4; i++) {
				bt[0 + i] = ipb[i];
			}
		}
		return getProtocol(25, TypeCmd.CMD_MULTICAST_CONFIG, TypeTlv.T_MULTICAST_CONFIG, 4,
				bt);
	}

	/** 
	 * @Title: getStartIdentity 
	 * @Description: TODO
	 * @return
	 * @return: byte[]
	 */
	public static byte[] getStartIdentity() {
		 byte[] bt = getProtocol(21, TypeCmd.CMD_PRO_INDENT_START, 0, 0, null);
		return bt; 
	}

	/** 
	 * @Title: getStopIdentity 
	 * @Description: TODO
	 * @return
	 * @return: byte[]
	 */
	public static byte[] getStopIdentity() {
		return getProtocol(21, TypeCmd.CMD_PRO_INDENT_STOP, 0, 0, null);
	}

	/** 
	 * @Title: getStartGrabPacket 
	 * @Description: TODO
	 * @param name
	 * @param status
	 * @param delStatus
	 * @return
	 * @return: byte[]
	 */
	public static byte[] getGrabPacket(String name, int status,
			int delStatus) {
		int i = 0;
		int btl = 0;
		byte[] bt = new byte[40];
		byte[] ipb = SocketEntityUtil.intToBytes(status, 2);
		for (i = 0; i < 2; i++) {
			bt[0 + i] = ipb[i];
		}

		ipb = SocketEntityUtil.intToBytes(delStatus, 2);
		for (i = 0; i < 2; i++) {
			bt[2 + i] = ipb[i];
		}
		btl = btl + i;
		ipb = SocketEntityUtil.StringToBytes(name, 32);
		for (i = 0; i < ipb.length; i++) {
			bt[btl + i] = ipb[i];
		}
		return getProtocol(57, TypeCmd.CMD_BRIDGE_CAPTURE, TypeTlv.T_BRIDGE_CAPTURE, 0, bt);
		
	}
}
