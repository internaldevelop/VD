package com.wnt.web.socket.service;

import java.util.List;
import java.util.Map;

import com.wnt.server.entry.ChartEntry;
import com.wnt.server.entry.LogEntry;
import com.wnt.server.entry.PortEntry;
import com.wnt.web.protocol.entry.Protocol;
import com.wnt.web.testsetup.entry.LDWJ_TESTDEPLAYLIVE;




public interface SocketService {

	/**
	 * 更新设备MAC地址
	 * @param equipmentId 设备ID
	 * @param terraceMac 工控漏洞挖掘平台MAC地址
	 * @param equipmentMac 被测设备MAC地址
	 */
	public void updateMac(int equipmentId,String terraceMac,String equipmentMac);
	/**
	 * 新增图形数据
	 * @param statetype 监视器ID
	 * @param statevalue 数值
	 */
	public void addChart(List<ChartEntry> listChart);
	/**
	 * 新增端口
	 * @param statetype 监视器ID
	 * @param statevalue 数值
	 * @param stateexplain 服务名称
	 */
	public void addPort(List<PortEntry> listPort);
	/**
	 * 新增事件日志
	 * @param listLog
	 */
	public void addLog( List<LogEntry> listLog);
	/**
	 * 查询端口是否占用
	 * @param portEntry
	 */
	public List<Map<String,Object>> findPort(PortEntry portEntry);
	/**
	 * 更新端口
	 * @param portEntry
	 */
	public void updatePort(PortEntry portEntry);
	/**
	 * 删除端口
	 * @param portEntry
	 */
	public void deletePort(PortEntry portEntry);
	/** 
	 * @Title: addSysInfoLog 
	 * @Description: TODO
	 * @param listLog
	 * @return: void
	 */
	public void addSysInfoLog(List<LogEntry> listLog);
	/** 
	 * @Title: updateTestNum 
	 * @Description: TODO
	 * @param list
	 * @return: void
	 */
	public void updateTestNum(List<LDWJ_TESTDEPLAYLIVE> list);
}