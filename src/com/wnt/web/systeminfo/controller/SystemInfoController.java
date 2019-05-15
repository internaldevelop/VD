
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: SystemInfo.java 
 * @Prject: VD
 * @Package: com.wnt.web.systeminfo.controller 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-5-3 上午10:34:11 
 * @version: V1.0   
 */ package com.wnt.web.systeminfo.controller; import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wnt.core.ehcache.EHCacheUtil;

import com.wnt.server.order.SealedSendMessage;

/** 
 * @ClassName: SystemInfo 
 * @Description: TODO
 * @author: gyj
 * @date: 2017-5-3 上午10:34:11  
 */
 
@Controller
@RequestMapping("/systemInfo")
public class SystemInfoController {
	
	@RequestMapping("systemInfo")
	public ModelAndView toSystemInfo(ModelAndView modelAndView,HttpServletRequest request){
		modelAndView.setViewName("systeminfo/systeminfo");
		String managerIp =(String) EHCacheUtil.get("MANAGERIP");
		request.setAttribute("managerIp", managerIp);
		return modelAndView;
	}
	
	@RequestMapping("versionInfo")
	public String toVersionInfo(HttpServletRequest request){
		if(EHCacheUtil.get("VERSION")!=null){
			String version =(String) EHCacheUtil.get("VERSION");
			request.setAttribute("version", version);
		}
		return "systeminfo/versioninfo";
	}
	
}

