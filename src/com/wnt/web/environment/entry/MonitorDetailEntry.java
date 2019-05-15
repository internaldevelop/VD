package com.wnt.web.environment.entry;

import java.util.Date;

//监视器明细
public class MonitorDetailEntry {
	private Long id;
	private Long monitorId;
	private Date createime;
	private String remark;
	private int delStatus;
	private String code;
	private int overtime;
	private int cyclePeriod;
	private int input;
	private int tcpports;
	private int alarmLevel;
	private int selectstatus;//1代表选中,2代表未选
	public int getAlarmLevel() {
		return alarmLevel;
	}
	public void setAlarmLevel(int alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(Long monitorId) {
		this.monitorId = monitorId;
	}
	public Date getCreateime() {
		return createime;
	}
	public void setCreateime(Date createime) {
		this.createime = createime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getOvertime() {
		return overtime;
	}
	public void setOvertime(int overtime) {
		this.overtime = overtime;
	}
	public int getCyclePeriod() {
		return cyclePeriod;
	}
	public void setCyclePeriod(int cyclePeriod) {
		this.cyclePeriod = cyclePeriod;
	}
	public int getInput() {
		return input;
	}
	public void setInput(int input) {
		this.input = input;
	}
	public int getTcpports() {
		return tcpports;
	}
	public void setTcpports(int tcpports) {
		this.tcpports = tcpports;
	}
	public int getSelectstatus() {
		return selectstatus;
	}
	public void setSelectstatus(int selectstatus) {
		this.selectstatus = selectstatus;
	}
	
	
}
