package com.wnt.web.selfcheck.service;

import java.util.Map;

/**
 * 自检服务
 * 
 * @author gyk
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * @date 2016年12月15日
 */
public interface SelfcheckService {
	
	/**
	 * 开始硬件检查
	 * 
	 * @author gyk
	 * @data 2016年12月15日
	 */
	public void startHardWareCheck();
	
	/**
	 * 停止硬件检查
	 * 
	 * @author gyk
	 * @data 2016年12月15日
	 */
	public void stopHardWareCheck();
	
	/**
	 * 获取硬件检查每个DI输入灯的状态
	 * @return
	 * @author gyk
	 * @data 2016年12月15日
	 */
	public int[] getHardWareCheckDIStatus();
	
	public void checkDoOutStatus(int... ys);
}
