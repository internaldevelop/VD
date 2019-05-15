package com.wnt.web.configuration.service;

import java.util.Map;

import com.wnt.web.configuration.entry.ConfigurationEntry;

public interface ConfigurationService {
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
	/**
	 * 下发
	 * 
	 * @param entry
	 * @return
	 */
	public void sendData(ConfigurationEntry entry);
}
