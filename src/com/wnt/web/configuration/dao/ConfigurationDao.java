package com.wnt.web.configuration.dao;

import java.util.List;
import java.util.Map;

import com.wnt.web.configuration.entry.ConfigurationEntry;
import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.environment.entry.MonitorDetailEntry;

public interface ConfigurationDao {
	/**
	 * 查看
	 * 
	 * @return
	 */
	public Map<String, Object> queryData();
	/**
	 * 更新
	 * 
	 * @param entry
	 * @return
	 */
	public void updateData(ConfigurationEntry entry);
	/**
	 * 更新
	 * 
	 * @param entry
	 * @return
	 */
	public void updateDataList2(ConfigurationEntry entry);
}
