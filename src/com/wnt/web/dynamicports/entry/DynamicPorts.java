package com.wnt.web.dynamicports.entry;

import java.io.Serializable;
import java.util.Date;

/**
 * IP 动态端口
 * @author gyk
 *
 */
public class DynamicPorts implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;  //主键
	private String ipAddr;	//主机IP 地址
	private Integer portNum; // '端口号',
	private Integer portType; //端口类型 6 TCP 17UDP
	private Integer scanType; // '扫描类型 0为主动扫描 1为被动扫描',2为未知
	private Date createTime; // '创建时间',
	private Integer delStatus; // '0：未删除 1：已删除',
	private String code;//'编码预留字段
	private String remark; // '备注',
	private Integer numOrder; //排序字段
	
	
	/**
	 * 获取主键ID
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置主键ID
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 获取IP 地址
	 * @return the ipAddr
	 */
	public String getIpAddr() {
		return ipAddr;
	}
	
	/**
	 * 设置IP 地址
	 * @param ipAddr the ipAddr to set
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	
	
	/**
	 * 获取端口号
	 * @return the portNum
	 */
	public Integer getPortNum() {
		return portNum;
	}
	
	/**
	 * 设置端口号
	 * @param portNum the portNum to set
	 */
	public void setPortNum(Integer portNum) {
		this.portNum = portNum;
	}
	
	/**
	 * 获取端口类型 6 TCP 17UDP
	 * @return the portType
	 */
	public Integer getPortType() {
		return portType;
	}
	/**
	 * 设置端口类型  6 TCP 17UDP
	 * @param portType the portType to set
	 */
	public void setPortType(Integer portType) {
		this.portType = portType;
	}
	
	/**
	 * 获取扫描类型 '扫描类型 0为主动扫描 1为被动扫描',2为未知
	 * @return the scanType
	 */
	public Integer getScanType() {
		return scanType;
	}
	
	/**
	 * 设置扫描类型 '扫描类型 0为主动扫描 1为被动扫描',2为未知
	 * @param scanType the scanType to set
	 */
	public void setScanType(Integer scanType) {
		this.scanType = scanType;
	}
	
	/**
	 * 获取创建时间
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	
	/**
	 * 设置创建时间
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * 获取删除状态 '0：未删除 1：已删除',
	 * @return the delStatus
	 */
	public Integer getDelStatus() {
		return delStatus;
	}
	
	/**
	 * 设置删除状态 '0：未删除 1：已删除',
	 * @param delStatus the delStatus to set
	 */
	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	
	/**
	 * 获取编码
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * 设置编码
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * 获取备注信息
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	
	/**
	 * 设置备注信息
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取排序值
	 * @return the numOrder
	 */
	public Integer getNumOrder() {
		return numOrder;
	}
	/**
	 * 设置排序值
	 * @param numOrder the numOrder to set
	 */
	public void setNumOrder(Integer numOrder) {
		this.numOrder = numOrder;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ipAddr == null) ? 0 : ipAddr.hashCode());
		result = prime * result + ((portNum == null) ? 0 : portNum.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DynamicPorts other = (DynamicPorts) obj;
		if (ipAddr == null) {
			if (other.ipAddr != null)
				return false;
		} else if (!ipAddr.equals(other.ipAddr))
			return false;
		if (portNum == null) {
			if (other.portNum != null)
				return false;
		} else if (!portNum.equals(other.portNum))
			return false;
		return true;
	}
	
	
	
}
