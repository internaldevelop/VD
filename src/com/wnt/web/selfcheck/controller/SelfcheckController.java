package com.wnt.web.selfcheck.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wnt.core.ehcache.EHCacheUtil;

import com.wnt.server.order.TypeCmd;
import com.wnt.web.selfcheck.service.SelfcheckService;

/**
 * 设备自我检测
 * 
 * @author gyk
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * @date 2016年12月15日
 */
@Controller
@RequestMapping("selfcheck")
public class SelfcheckController {
	
	@Autowired
	private SelfcheckService selfcheckService;
	
	/**
	 * 开始硬件检测
	 * @return
	 * @author gyk
	 * @data 2016年12月15日
	 */
	@ResponseBody
	@RequestMapping("startHardWareCheck")
	public Map<String,Object> startHardWareCheck(){
		Map<String,Object> m = new HashMap<String, Object>();
		selfcheckService.startHardWareCheck();
		m.put("status", "y");
		return m;
	}
	
	/**
	 * 停止硬件检查
	 * @return
	 * @author gyk
	 * @data 2016年12月15日
	 */
	@ResponseBody
	@RequestMapping("stopHardWareCheck")
	public Map<String,Object> stopHardWareCheck(){
		Map<String,Object> m = new HashMap<String, Object>();
		selfcheckService.stopHardWareCheck();
		m.put("status", "y");
		return m;
	}
	
	/**
	 * 获取硬件检查数据
	 * @return
	 * @author gyk
	 * @data 2016年12月15日
	 */
	@ResponseBody
	@RequestMapping("getHardWareCheckData")
	public Map<String,Object> getHardWareCheckData(){
		Map<String,Object> m = new HashMap<String, Object>();
		m.put("leds",selfcheckService.getHardWareCheckDIStatus());
		return m;
	}
	
	/**
	 * 检查DO输出状态
	 * @param y0
	 * @param y1
	 * @param y2
	 * @return
	 * @author gyk
	 * @data 2016年12月15日
	 */
	@ResponseBody
	@RequestMapping("checkDoOutStatus")
	public Map<String,Object> checkDoOutStatus(int y0,int y1,int y2){
		Map<String,Object> m = new HashMap<String, Object>();
		selfcheckService.checkDoOutStatus(new int[]{y0,y1,y2});
		return m;
	}
	
}
