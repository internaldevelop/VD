package com.wnt.server.order;
/**
 * 全局变量
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */

public class ConstantsDefs {
	//自增全局变量
//	public static int SENTFLAG=0;
	public final static String[] LogType = new String[10];
	public final static String[] LogError = new String[10];
	
	static{
		LogType[0] = "ARP monitor";
		LogType[1] = "ARP monitor";
		LogType[2] = "ICMP monitor";
		LogType[3] = "TCP monitor";
		LogType[4] = "离散数据 monitor";
		LogType[5] = "语法测试";
		LogType[6] = "风暴测试";
		LogType[7] = "用户自定义";
		LogType[8] = "FUZZER";
		LogType[9] = "用来显示回溯时候的状态";
		
		LogError[0] = "开始";
		LogError[1] = "开始";
		LogError[2] = "停止";
		LogError[3] = "正常";
		LogError[4] = "警告";
		LogError[5] = "错误";
		LogError[6] = "XX";
		LogError[7] = "测试验证中，请等待";
		LogError[8] = "被测设备重启中";
		LogError[9] = "";
	}
}
