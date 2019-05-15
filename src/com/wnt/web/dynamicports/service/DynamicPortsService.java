package com.wnt.web.dynamicports.service;
import java.util.List;

import com.wnt.web.dynamicports.entry.DynamicPorts;

/**
 * 动态端口服务接口
 * @author gyk
 *
 */
public interface DynamicPortsService {
	
	/**
	 * 开始扫描
	 * @param ipAddr   	ip 地址	
	 * @param scanType 	扫描类型
	 * @param portType  端口类型
	 * @return 扫描到的动态端口
	 */
	List<DynamicPorts> startScan(String ipAddr,Integer scanType,Integer portType) throws Exception;
	
	/**
	 * 停止扫描
	 */
	void stopScan() throws Exception;
	
	
	/**
	 * 扫描动态端口信息 
	 * @param ip		ip 地址
	 * @param scanType  扫描类型
	 * @param portType  端口类型
	 * @return 扫描到的所有数据
	 */
	public List<DynamicPorts> scanPort(String ip,Integer scanType,Integer portType) throws Exception;
	
}
