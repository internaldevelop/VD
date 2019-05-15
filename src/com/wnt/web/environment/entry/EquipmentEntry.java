package com.wnt.web.environment.entry;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class EquipmentEntry {
	private Long id;
	private Long equipmentId;
	private String name;
	private String version;
	private Integer linkType;
	private String remark;
	private Date createTime;
	private Integer enable;
	private Long ip;
	private String mac;
	private String subnetMask;
	private Integer enable2;
	private Long ip2;
	private String mac2;
	private String subnetMask2;
	private Long ip3;
	private String mac3;
	private String subnetMask3;
	private Integer delStatus;
	private String code;
	private Boolean isEth0Exist = false; //被测设备 是否存在
	private Boolean isEth1Exist = false; //控制系统 是否存在
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Integer getLinkType() {
		return linkType;
	}
	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
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
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Long getIp() {
		return ip;
	}
	public void setIp(Long ip) {
		this.ip = ip;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getSubnetMask() {
		return subnetMask;
	}
	public void setSubnetMask(String subnetMask) {
		this.subnetMask = subnetMask;
	}
	public Integer getEnable2() {
		return enable2;
	}
	public void setEnable2(Integer enable2) {
		this.enable2 = enable2;
	}
	public Long getIp2() {
		return ip2;
	}
	public void setIp2(Long ip2) {
		this.ip2 = ip2;
	}
	public String getMac2() {
		return mac2;
	}
	public void setMac2(String mac2) {
		this.mac2 = mac2;
	}
	public String getSubnetMask2() {
		return subnetMask2;
	}
	public void setSubnetMask2(String subnetMask2) {
		this.subnetMask2 = subnetMask2;
	}
	public Long getIp3() {
		return ip3;
	}
	public void setIp3(Long ip3) {
		this.ip3 = ip3;
	}
	public String getMac3() {
		return mac3;
	}
	public void setMac3(String mac3) {
		this.mac3 = mac3;
	}
	public String getSubnetMask3() {
		return subnetMask3;
	}
	public void setSubnetMask3(String subnetMask3) {
		this.subnetMask3 = subnetMask3;
	}
	public Integer getDelStatus() {
		return delStatus;
	}
	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * 获取被测设备是否存在
	 * @Title: getIsEth0Exist 
	 * @Description: TODO
	 * @return Boolean
	 */
	public Boolean getIsEth0Exist() {
		return this.isEth0Exist;
	}
	/**
	 * 设置被测设备是否存在
	 * @Title: setIsEth0Exist 
	 * @Description: TODO
	 * @param isEth0Exist
	 * @return: void
	 */
	public void setIsEth0Exist(Boolean isEth0Exist) {
		this.isEth0Exist = isEth0Exist;
	}
	
	/**
	 * 获取控制系统是否存在
	 * @Title: getIsEth1Exist 
	 * @Description: TODO
	 * @return: Boolean
	 */
	public Boolean getIsEth1Exist() {
		return this.isEth1Exist;
	}
	
	/**
	 * 设置控制系统是否存在
	 * @Title: setIsEth1Exist 
	 * @Description: TODO
	 * @param isEth1Exist
	 * @return: void
	 */
	public void setIsEth1Exist(Boolean isEth1Exist) {
		this.isEth1Exist = isEth1Exist;
	}
	
}
