package com.wnt.server.order;

public class TypeCmd {
	public static final int HELLO = 0x01;
	public static final int HELLO_RESPONSE = 0x02;
	public static final int BYE = 0x03;
	public static final int CONNECT = 0x05;
	public static final int CONNECT_RESPONSE = 0x06;
	public static final int START_TEST = 0x07;
	public static final int START_TEST_RESPONSE = 0x08;
	public static final int PAUSE_TEST = 0x09;
	public static final int PAUSE_TEST_RESPONSE = 0x0A;
	public static final int STOP_TEST = 0x0B;
	public static final int STOP_TEST_RESPONSE = 0x0C;
	public static final int START_MONITOR = 0x0D;
	public static final int START_MONITOR_RESPONSE = 0x0E;
	public static final int STOP_MONITOR = 0x0F;
	public static final int STOP_MONITOR_RESPONSE = 0x10;
	public static final int STATE_REQUEST = 0x11;
	public static final int STATE_RESPONSE = 0x12;
	public static final int REPLAY = 0x13;
	public static final int REPLAY_RESPONSE = 0x14;
	public static final int CONTINUE_TEST =  0x15;
	public static final int CONTINUE_TEST_RESPONSE = 0x16;
	public static final int START_SCAN =  0x17;
	public static final int START_SCAN_RESPONSE = 0x18;
	public static final int STOP_SCAN =  0x19;
	public static final int STOP_SCAN_RESPONSE  = 0x1A;
	public static final int GET_LOCAL_IF =  0x1B;
	public static final int GET_LOCAL_IF_RESPONSE  = 0x1C;
	public static final int SET_LOCAL_IF  = 0x1D;
	public static final int SET_LOCAL_IF_RESPONSE  = 0x1E;
	public static final int LOG_EVENT  = 0x20;
	public static final int CMD_PORT_USER  = 0x21;
	public static final int CMD_STORM_SETUP  = 0x22;
	public static final int CMD_GRAMMAR_SETUP  = 0x23;
	public static final int SHUTDOWN = 0x25;
	public static final int CUSTOM = 0x26;
	public static final int DEL_PORTS = 0x27;
	public static final int CMD_NET_TOPOLOGY = 0x28;
	public static final int CMD_APP_READY_START_TEST = 0x29;
	public static final int CMD_APP_START_TEST = 0x2a;
	public static final int CMD_APP_STOP_TEST = 0x2b;
	public static final int CMD_MULTICAST_CONFIG= 0x31;
	public static final int CMD_PROTO_IDENTI_RESULT=0x32;
	public static final int CMD_TEST_STOP_STATE =0x33;
	public static final int CMD_PRO_INDENT_START=0x36;
	public static final int CMD_PRO_INDENT_STOP=0x37;
	public static final int CMD_UPLOAD_HARD_STATE=0x38;
	public static final int CMD_UPLOAD_CRT_VERSION =0x39;
	public static final int CMD_BRIDGE_CAPTURE=0x40;
	public static final int CMD_TEST_MAX_NUM=0x41;
	
	
	/**
	 * 下发开始动态端口扫描测试命令
	 */
	public static final int CMD_DYNAMIC_START_TEST = 0x2d;
	
	/**
	 * 下发停止动态端口扫描测试命令
	 */
	public static final int CMD_DYNAMIC_STOP_TEST = 0x2e;
	
	/**
	 * 下发DO输出状态
	 */
	public static final int CMD_DO_WRITE_TEST = 0x31;
	
	/**
	 * 硬件检查开始命令
	 */
	public static final int CMD_HARDWARE_CHECK_START = 0x50;
	
	/**
	 * 硬件检查停止命令
	 */
	public static final int CMD_HARDWARE_CHECK_STOP = 0x51;
	
	/**
	 * 硬件检查
	 */
	public static final int CMD_DI_READ_TEST = 0x34;
}
