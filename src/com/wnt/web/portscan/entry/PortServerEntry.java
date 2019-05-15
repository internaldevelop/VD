package com.wnt.web.portscan.entry;

import java.util.Date;
//端口服务的信息
public class PortServerEntry {
	private String id;
	private Integer equipmentId; // '设备ID ：0 为测试网络一 1为测试网络2 ',
	private Integer portNum; // '端口号',
	private String name; // '服务名称',
	private Integer portType; // '端口类型：6 TCP 17UDP',
	private Integer scanType; // '扫描类型 0为主动扫描 1为被动扫描',2为未知
	private String source; // '来源：1为scan 2为手动添加 3为被动扫描',
	private Integer delStatus; // '0：未删除 1：已删除',
	private String remark; // '备注',
	private Date createTime; // '创建时间',
	private String code;//'编码预留字段
	private Integer hportType;//'添加端口类型：6 TCP 17UDP',
	
	public Integer getHportType() {
		return hportType;
	}

	public void setHportType(Integer hportType) {
		this.hportType = hportType;
	}

	public static String getPortTypeStr(int portType){
		if(portType==6){
			return "TCP";
		}else{
			return "UDP";
		}
	}
	
	public static String getSourceStr(String source){
		if("1".equals(source)){
			return "scan";
		}else if("2".equals(source)){
			return "手动添加";
		}else{
			return "被动扫描";
		}
	}
	public static String getScanTypeStr(int st){
		if(st==0){
			return "主动扫描";
		}else if(st==1){
			return "被动扫描";
		}else{
			return "未知";
		}
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}
	public Integer getPortNum() {
		return portNum;
	}
	public void setPortNum(Integer portNum) {
		this.portNum = portNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPortType() {
		return portType;
	}
	public void setPortType(Integer portType) {
		this.portType = portType;
	}
	public Integer getScanType() {
		return scanType;
	}
	public void setScanType(Integer scanType) {
		this.scanType = scanType;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Integer getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
