package com.wnt.web.socket.dao;

import java.util.List;
import java.util.Map;

import com.wnt.server.entry.ChartEntry;
import com.wnt.server.entry.LogEntry;
import com.wnt.server.entry.PortEntry;
import com.wnt.web.protocol.entry.Protocol;
import com.wnt.web.testsetup.entry.LDWJ_TESTDEPLAYLIVE;



public interface SocketDao {
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
	public void addChart(final List<ChartEntry> listChart);
	/**
	 * 新增端口
	 * @param statetype 监视器ID
	 * @param statevalue 数值
	 * @param stateexplain 服务名称
	 */
	public void addPort(final List<PortEntry> listPort);
	
	
	public void addLog(final List<LogEntry> listLog);
	/**
	 * 查询端口是否占用
	 * @param statetype 监视器ID
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
	/** 
	 * @Title: addChartArp 
	 * @Description: TODO
	 * @param listArp
	 * @return: void
	 */
	public void addChartArp(List<ChartEntry> listArp);
	/** 
	 * @Title: addChartIcmp 
	 * @Description: TODO
	 * @param listIcmp
	 * @return: void
	 */
	public void addChartIcmp(List<ChartEntry> listIcmp);
	/** 
	 * @Title: addChartTcp 
	 * @Description: TODO
	 * @param listTcp
	 * @return: void
	 */
	public void addChartTcp(List<ChartEntry> listTcp);
	/** 
	 * @Title: addChartDiscrete 
	 * @Description: TODO
	 * @param listDiscrete
	 * @return: void
	 */
	public void addChartDiscrete(List<ChartEntry> listDiscrete);
	/** 
	 * @Title: addChartEth0_2 
	 * @Description: TODO
	 * @param listEth0_2
	 * @return: void
	 */
	public void addChartEth0_2(List<ChartEntry> listEth0_2);
	/** 
	 * @Title: addChartEth0_1 
	 * @Description: TODO
	 * @param listEth0_1
	 * @return: void
	 */
	public void addChartEth0_1(List<ChartEntry> listEth0_1);
	/** 
	 * @Title: addChartEth1_2 
	 * @Description: TODO
	 * @param listEth1_2
	 * @return: void
	 */
	public void addChartEth1_2(List<ChartEntry> listEth1_2);
	/** 
	 * @Title: addChartEth1_1 
	 * @Description: TODO
	 * @param listEth1_1
	 * @return: void
	 */
	public void addChartEth1_1(List<ChartEntry> listEth1_1);

}