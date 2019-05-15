package com.wnt.server.entry;


/**
 * 事件日志数据
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class LogEntry {
	
	//通讯传来以下三个字段
	//来源类型：1为ARP monitor 2为ICMP monitor 3为TCP monitor 4为离散数据 monitor 5为测试用例
	private int sourcetype;	
	//信息类型:1为开始 2为停止 3为正常 4 为警告 5为错误
	//监视器：状态更改为"开始"
	//测试用例：测试用例名称状态为""
	private int messagetype;	//数字（翻译）-->message
	private String code; 	//测试用例编码
	
	private String id;
	private int num;		//测试用例索引
	private int num2;		//测试用例索引结束
	//以下为转换后的字段
	private String source;
	private String message;
	
	//测试结果的父id
	private String parent;
	//测试结果id
	private String testResultId;
	
	
//	public static String convertSource(int s){
//		if(s==1){
//			return "ARP monitor";
//		}else if(s==2){
//			return "ICMP monitor";
//		}else if(s==3){
//			return "TCP monitor";
//		}else if(s==4){
//			return "离散数据 monitor";
//		}else if(s==5){
//			return "测试用例";
//		}
//		return null;
//	}
	
//	public static String convertMessage(String m,String sourceType){
//		if("5".equals(sourceType)){
//			//测试用例
//			
//			
//			
//		}
//		return null;
//	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getSourcetype() {
		return sourcetype;
	}
	public void setSourcetype(int sourcetype) {
		this.sourcetype = sourcetype;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getMessagetype() {
		return messagetype;
	}

	public void setMessagetype(int messagetype) {
		this.messagetype = messagetype;
	}


	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getTestResultId() {
		return testResultId;
	}

	public void setTestResultId(String testResultId) {
		this.testResultId = testResultId;
	}
	public int getNum2() {
		return num2;
	}
	public void setNum2(int num2) {
		this.num2 = num2;
	}

	
	
}
