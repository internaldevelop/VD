package com.wnt.server.entry;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 图形数据
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class ChartEntry implements Serializable {
	private Integer monitorid; // 监视器ID,图形类型
	private Integer num; // 数值
	private Float num1;
	private Integer reserve;
	private String createtime;
	private long timestamp;

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Integer getMonitorid() {
		return monitorid;
	}

	public void setMonitorid(Integer monitorid) {
		this.monitorid = monitorid;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getReserve() {
		return reserve;
	}

	public void setReserve(Integer reserve) {
		this.reserve = reserve;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Float getNum1() {
		return num1;
	}

	public void setNum1(Float num1) {
		this.num1 = num1;
	}

	
	
}
