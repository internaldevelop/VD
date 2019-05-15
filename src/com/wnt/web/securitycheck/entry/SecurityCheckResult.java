package com.wnt.web.securitycheck.entry;

import java.util.Date;

/**
 * 安全检查结果实体对象
 * @author gyk
 *
 */
public class SecurityCheckResult {
	
	/**
	 * 测试结果ID
	 */
	private String id;
	/**
	 * 设备名称
	 */
	private String deviceName;
	
	/**
	 * 设备ID
	 */
	private Integer equipmentId;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 全称
	 */
	private String fullName; 
	
	/**
	 * 删除状态
	 */
	private int delStatus = 0;

	/**
	 * 获取结果ID
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置结果ID
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取设备名称
	 * @return
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * 设置设备名称
	 * @param deviceName
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * 获取设备ID
	 * @return
	 */
	public Integer getEquipmentId() {
		return equipmentId;
	}

	/**
	 * 设置设备ID
	 * @param equipmentId
	 */
	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}

	/**
	 * 获取创建时间
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取全称
	 * @return
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * 设置全称
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * 获取删除状态 默认0标识未删除，
	 * @return
	 */
	public int getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(int delStatus) {
		this.delStatus = delStatus;
	}
}
