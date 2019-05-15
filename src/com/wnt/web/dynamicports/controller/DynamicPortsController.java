package com.wnt.web.dynamicports.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wnt.core.ehcache.EHCacheUtil;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.configuration.service.ConfigurationService;
import com.wnt.web.dynamicports.entry.DynamicPorts;
import com.wnt.web.dynamicports.service.DynamicPortsService;

/**
 * 动态端口的控制类
 * @author gyk
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 */
@Controller
@RequestMapping("/dynamicports")
public class DynamicPortsController {
	private final Logger log = Logger.getLogger(getClass());
	
	private static final String START_SCAN = "dynamicPortsStartScan";
	
	@Resource
	private DynamicPortsService dynamicPortsService;
	@Resource
	ConfigurationService configurationService;
	
	@RequestMapping("/init")
	public String init(HttpSession session,Model model){
		ServletContext context =  session.getServletContext();
		try {
			//判断扫描是否在进行中
			if(context.getAttribute(START_SCAN)!=null){
				//正在扫描
				model.addAttribute(START_SCAN,EHCacheUtil.get(START_SCAN));
			}
			Object tmpSt = session.getAttribute("scanType");
			Object tmpIp = session.getAttribute("ipAddr");
			Object tmpPortType = session.getAttribute("portType");
			Integer st = null;
			String ip = null;
			Integer portType = null;
			if(tmpSt != null){
				st = Integer.valueOf(tmpSt.toString());
				model.addAttribute("scanType",st);
			}
			if(tmpIp!= null){
				ip = tmpIp.toString();
				model.addAttribute("ipAddr", ip);
			}else{
				//查询数据库数据
				Map<String, Object> data = configurationService.queryData();
				tmpIp = data.get("HOSTIP");
				ip = tmpIp.toString();
				model.addAttribute("ipAddr", ip);
			}
			if(tmpPortType!=null){
				portType = Integer.valueOf(tmpPortType.toString());
			}
			if(st!= null && ip!=null){
				model.addAttribute("list", dynamicPortsService.scanPort(ip, st,portType));
			}
		} catch (Exception e) {
			log.error("初始化动态扫描异常",e);
		}
		return "dynamicports/list";
	}
	
	/**
	 * 开始扫描
	 * @param ip		ip 地址
	 * @param scanType	扫描类型
	 * @return
	 */
	@ResponseBody
	@RequestMapping("startScan")
	public Map<String,Object> startScan(HttpSession session,String ip,Integer scanType,Integer portType){
		HashMap<String,Object> map=new HashMap<String,Object>();
		ServletContext context =  session.getServletContext();
		try {
			if(context.getAttribute(START_SCAN) == null){
				
				session.setAttribute("ipAddr",ip);
				session.setAttribute("scanType",scanType);
				session.setAttribute("portType", portType);
				log.info("开始动态端口扫描+++++");
				dynamicPortsService.startScan(ip,scanType,portType);
				
				//设置开始扫描状态
				context.setAttribute(START_SCAN, "1");
				EHCacheUtil.put(START_SCAN,"1");
				map.put("status", "y");
				map.put("info", "开始动态扫描");
			}else{
				//结束扫描
				//@执行结束端口扫描
				dynamicPortsService.stopScan();
				
				session.removeAttribute("ipAddr");
				session.removeAttribute("scanType");
				session.removeAttribute("portType");
				EHCacheUtil.put(START_SCAN,"0");
				//设置结束扫描状态
				context.removeAttribute(START_SCAN);
				map.put("status", "y");
				map.put("info", "动态结束扫描");
			}
		} catch (Exception e) {
			map.put("status", "n");
			map.put("info", "开启动态扫描失败");
			log.error("开启动态扫描异常",e);
		}
		return map;
	}
	
	/**
	 * 停止动态扫描
	 * @param session
	 * @return
	 */
	@RequestMapping("stopScan")
	public Map<String,Object> stopScan(HttpSession session){
		HashMap<String,Object> map=new HashMap<String,Object>();
		ServletContext context =  session.getServletContext();
		try {
			dynamicPortsService.stopScan();
			session.removeAttribute("ipAddr");
			session.removeAttribute("scanType");
			session.removeAttribute("portType");
			context.removeAttribute(START_SCAN);
			EHCacheUtil.put(START_SCAN,"0");
			map.put("status", "y");
			map.put("info", "动态结束扫描");
			log.info("停止动态端口扫描");
			map.put("success", true);
		} catch (Exception e) {
			map.put("status", "n");
			map.put("info", "停止动态扫描失败");
			log.error("停止动态扫描异常",e);
		}
		return map;
	}
	
	/**
	 * 扫描端口
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("scanport")
	public Map<String,Object> scanPorts(HttpSession session){
		HashMap map=new HashMap();
		ServletContext context =  session.getServletContext();
		try {
			
			Object tmpO = EHCacheUtil.get("CONNECT_ERROR");
			if(tmpO!=null && "12".equals(tmpO.toString())){
				log.error("扫描失败，请检查设备或网络！");
				map.put("success", true);
				map.put("status", "n");
				map.put("info", "扫描失败，请检查设备或网络！");
				map.put("errorNum", tmpO);
				EHCacheUtil.remove("CONNECT_ERROR");
				return map;
			}
			
			Object iptmp = session.getAttribute("ipAddr");
			Object scantypeTmp = session.getAttribute("scanType");
			Object portType = session.getAttribute("portType");
			if(iptmp != null && scantypeTmp!=null){
				map.put("list", dynamicPortsService.scanPort(iptmp.toString(), (Integer)scantypeTmp,(Integer)portType));
				log.info("动态端口扫描正常");
			}
			//在缓存中取得当前的进度
			if( EHCacheUtil.get("r1819")!=null){
				map.put("progress", EHCacheUtil.get("r1819"));
			}else{
				map.put("progress", "0");
			}
			log.info("缓存中的扫描进度："+EHCacheUtil.get("r1819"));
			if(Integer.parseInt(map.get("progress").toString()) < 101){
				//扫描正在进行中
				map.put("status", "y");
			}else{
				//扫描结束
				map.put("status", "n");
				EHCacheUtil.remove("r1819");
				context.removeAttribute(START_SCAN);
			}
		} catch (Exception e) {
			log.error("动态扫描异常",e);
			map.put("status", "n");
			map.put("info", "扫描失败");
		}
		return map;
	}	
}
