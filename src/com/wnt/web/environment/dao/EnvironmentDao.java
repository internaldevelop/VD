package com.wnt.web.environment.dao;

import java.util.List;
import java.util.Map;

import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.environment.entry.MonitorDetailEntry;

public interface EnvironmentDao {
	/**
	 * 通过equipmentId查询环境设置信息
	 * @param equipmentId
	 * @return
	 */
	public Map<String, Object> findEnvironmentByEquipmentId(String equipmentId);
	/**
	 * 取得所有监视器
	 * @return
	 */
	public List<Map<String, Object>> findMonitor(Integer equipmentId);
	
	public void update1( EquipmentEntry env);
	public void update2( EquipmentEntry env);
	public void update3( EquipmentEntry env);
	
	/**
	 * 通过id得到监视器明细信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> findMonitorById(Integer id);
	/**
	 * 通过设备id取得相关监视器明细信息
	 * @param equipmentId
	 * @return
	 */
	public List<Map<String, Object>> findMonitorDetail(Integer equipmentId);
	
	/**
	 * 修改监视器明细
	 * @param md
	 */
	public void updateMonitorDetail(MonitorDetailEntry md);
	/**
	 * 当用户选择监视器的复选框后执行
	 * @param selected 1选择，2：未选中
	 * @param envId 设备id
	 * @param mid 监视器id
	 */
	public void selectMinitor(String selected, int envId, int mid);
	/**
	 * 取得设备选择了哪些监视器
	 * @return
	 */
	public List<Map<String, Object>> findSelectMonitor(Integer equipmentId);
	/**
	 * 插入测试模板
	 * @param templateName
	 */
	public void insertTemplate(String templateName);
	
	public void updateStatus(String status);
	
	public void updateEth1(String ck);
	
}
