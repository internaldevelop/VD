package com.wnt.web.portscan.dao;

import java.util.List;
import java.util.Map;

import com.wnt.web.portscan.entry.PortServerEntry;

public interface PortscanDao {
	public int save(PortServerEntry pse);
	/**
	 * 取得扫描结果
	 * @return
	 */
	public List<Map<String,Object>> findScanResult(long time);
	/**
	 * 检测端口是否重复
	 * @param port
	 * @return
	 */
	public boolean checkPort(int port,int portType);
	public boolean checkPort2(int port,int portType);
	/**
	 * 删除端口号
	 * @param port
	 */
	public void deletePort(String id);
	/**
	 * 删除所有手动添加端口
	 */
	public void deleteAllPort();
	
	public List findport();
}
