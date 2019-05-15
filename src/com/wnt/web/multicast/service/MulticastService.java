package com.wnt.web.multicast.service;

import java.util.List;
import java.util.Map;



/**
 * 扫描设置（多播地址）逻辑层
 * 
 * @author 付强
 * @version 1.0
 * @company 汇才同飞
 * @site http://www.javakc.cn
 * 
 */
public interface MulticastService {

	/**
	 * ##查询多播地址信息
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryMulticastList() throws Exception;
	
	/**
	 * ##添加多播地址
	 * @throws Exception
	 */
	public void createMulticast(String ipAddr) throws Exception;
	
	/**
	 * ##删除多播地址
	 * @throws Exception
	 */
	public void deleteMulticast(String id) throws Exception;
	
	public Boolean findMulticast(String ipAddr) throws Exception;

	/** 
	 * @Title: deleteMulticastByIp 
	 * @Description: TODO
	 * @param ipAddr
	 * @return: void
	 */
	public void deleteMulticast();

	/** 
	 * @Title: findMulticast 
	 * @Description: TODO
	 * @return: void
	 */
	public List<Map<String, Object>> findMulticast();
	
}
