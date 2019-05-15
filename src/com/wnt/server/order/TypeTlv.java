package com.wnt.server.order;

public class TypeTlv {
	public static int T_NO = 0;	//自定义无体
	public static int T_CONNECT = 1;
	public static int T_CONNECT_RESULT = 2;
	public static int T_START_TEST = 3;
	public static int T_CMD_RESULT = 4;
	public static int T_MONITOR = 5;
	public static int T_STATE_REQUEST = 6;
	public static int T_STATE_VALUE = 7;
	public static int T_REPLAY = 8;
	public static int T_CMD_SCAN = 9;
	public static int T_LOG_EVENT = 10;
	public static int T_PORT_USER = 11;
	public static int T_STORM_SETUP = 12;
	public static int T_GRAMMAR_SETUP = 13;
	public static int T_RATE_FLOW = 14;
	public static int T_TEST_INFO = 15;
	public static int T_SHUTDOWN = 16;
	public static int T_CUSTOM_INFO = 17;
	public static int T_CUSTOM_ITEM_RAND = 18;
	public static int T_CUSTOM_ITEM_ENUM = 19;
	public static int T_DEL_PORTS = 20;
	public static int T_TOPOLOGY = 21;
	public static int T_APP_READY_TEST = 22;
	public static int T_MULTICAST_CONFIG =39;
	public static int T_PROTO_IDENTI_RESULT=30;
	public static int T_BRIDGE_CAPTURE=38;
	/**
	 * 动态端口扫描开始类型
	 */
	public static int T_DYNAMIC_START = 24;
	
	/**
	 * 动态端口扫描结束类型
	 */
	public static int T_DYNAMIC_STOP = 25;
	
	/**
	 * 接收自检硬件检查数据
	 */
	public static int T_DI_READ_RESULT = 28;
	
	/**
	 * 硬件检查开始类型
	 */
	public static int T_HARD_CHECK_START = 30;
	
	/**
	 * 硬件检查停止类型
	 */
	public static int T_HARD_CHECK_STOP = 31;
	
	/**
	 * 下发DO输出状态
	 */
	public final static int  T_DO_WRITE_RESULT = 29;
}
