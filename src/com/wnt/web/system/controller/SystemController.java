package com.wnt.web.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wnt.core.ehcache.EHCacheUtil;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.operationlog.service.OperationLogService;
import com.wnt.web.system.service.SystemService;
import common.SocketEntityUtil;

@Controller
@RequestMapping("/system")
public class SystemController {
	private Map map = new HashMap();
	@Resource
	private SystemService systemService;
	@Resource
	private OperationLogService operationLogService;
	
	@ResponseBody
	@RequestMapping("/interfaceipadd")
	public Map interfaceipadd(String ip, HttpServletRequest request) throws Exception
	{
	    if(!org.wnt.core.uitl.StringUtil.ipRegex(ip)) {
            map.put("info", "IP Address is invalid!");
            return map;
        }
	    
		long mIp=0L;
		long interIp=0L;
		long mMask=0L;
		if(null!=EHCacheUtil.get("MANAGERIP")){
			String managerIp =(String) EHCacheUtil.get("MANAGERIP");
			mIp=SocketEntityUtil.ipToLong(managerIp);
		}
		if(null!=EHCacheUtil.get("MANAGERMASK")){
			String mask =(String) EHCacheUtil.get("MANAGERMASK");
			mMask=SocketEntityUtil.ipToLong(mask);
		}
		if(StringUtil.isNotBlank(ip)){
			interIp=SocketEntityUtil.ipToLong(ip);
		}
		
		String userName = request.getSession().getAttribute("userName").toString();
        
		if(null==EHCacheUtil.get("MANAGERMASK")|| null==EHCacheUtil.get("MANAGERIP")){
			map.put("info", "配置失败");
			operationLogService.addOperationLog(userName, request, "配置管理口IP", "失败", "IP地址:" + ip);
		}else{
			//System.out.println("mIp & mMask:"+(mIp & mMask));
			//System.out.println("interIp & mMask:"+(interIp & mMask));
			if((mIp & mMask) == (interIp & mMask)){
				systemService.interfaceipadd(ip);
				map.put("info", "配置成功");
				operationLogService.addOperationLog(userName, request, "配置管理口IP", "成功", "IP地址:" + ip);
			}else{
				map.put("info", "配置失败");
				operationLogService.addOperationLog(userName, request, "配置管理口IP", "失败", "IP地址:" + ip);
			}
		}
		return map;
	}
	public static void main(String[] args) {
		long a1 = 123456L;
		long a2 = 123456L;
		long a3 = 345L;
		System.out.println((a1 & a3) == (a2 & a3));
	}
	@RequestMapping("/findinterface")
	public String findinterface(ModelMap model) throws Exception
	{
		//String ip = systemService.findinterface();
		String managerIp =(String) EHCacheUtil.get("MANAGERIP");
		model.put("iip", managerIp);
		return "system/main";
	}
	
	@RequestMapping("/setTestcsReboot")
	public Map setTestcsReboot(ModelMap model, HttpServletRequest request) throws Exception
	{
		System.out.println("重启");
		SealedSendMessage.getShutdown(0, 0);
		map.put("info", "success");
		String userName = request.getSession().getAttribute("userName").toString();
        operationLogService.addOperationLog(userName, request, "重启测试工具", "成功", "--");
		return map;
	}
	
	@RequestMapping("/setTestcsShutdown")
	public Map setTestcsShutdown(HttpServletRequest request) throws Exception
	{
		System.out.println("关闭");
		SealedSendMessage.getShutdown(0, 1);
		map.put("info", "success");
		String userName = request.getSession().getAttribute("userName").toString();
		operationLogService.addOperationLog(userName, request, "关闭测试工具", "成功", "--");
		return map;
	}
	
	@RequestMapping("/setTestbcReboot")
	public Map setTestbcReboot(HttpServletRequest request) throws Exception
	{
		System.out.println("重启bc");
		SealedSendMessage.getShutdown(1, 0);
		map.put("info", "success");
		String userName = request.getSession().getAttribute("userName").toString();
        operationLogService.addOperationLog(userName, request, "重启被测设备", "成功", "--");
		return map;
	}
}
