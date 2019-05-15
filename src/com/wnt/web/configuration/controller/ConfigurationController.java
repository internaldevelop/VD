package com.wnt.web.configuration.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wnt.core.ehcache.EHCacheUtil;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.configuration.entry.ConfigurationEntry;
import com.wnt.web.configuration.service.ConfigurationService;
import com.wnt.web.portscan.entry.PortServerEntry;

import common.PortScanDefs;

/**
 * 环境配置
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
@Controller
@RequestMapping("/configuration")
public class ConfigurationController {
	
	@Resource
	ConfigurationService configurationService;
	
	private final Logger log = Logger.getLogger(ConfigurationController.class.getName());

	private ModelAndView modelAndView;

	private static final String SUCCESS_PAGE = "index";
	private static final String C_START_SCAN = "configstartScan";
	/**
	 * 查询数据
	 * 
	 * @return
	 */
	@RequestMapping("/querydata")
	public ModelAndView querydata(HttpServletRequest request,ConfigurationEntry entry) {
		modelAndView= new ModelAndView();
		if(entry.getIndex()==0){
			modelAndView.setViewName("configuration/list");
		}else if(entry.getIndex()==1){
			modelAndView.setViewName("configuration/list2");
			//保存信息到数据库
			configurationService.updateData(entry);
		}else{
			modelAndView.setViewName("configuration/list3");
			//保存信息到数据库
			configurationService.updateDataList2(entry);
		}
		if(request.getSession().getServletContext().getAttribute(C_START_SCAN)!=null && EHCacheUtil.get("r1818")!=null){
			//正在扫描
			modelAndView.setViewName("redirect:senddata.do");
		}else{
			ServletContext context=request.getSession().getServletContext();
			context.removeAttribute(C_START_SCAN);
		}
		//查询数据库数据
		Map<String, Object> data = configurationService.queryData();
		modelAndView.addObject("data", data);
		return modelAndView;
	}
	/**
	 * 下发数据
	 * 
	 * @return
	 */
	@RequestMapping("/senddata")
	public ModelAndView sendData(HttpServletRequest request,ConfigurationEntry entry) {
		modelAndView= new ModelAndView();
		modelAndView.setViewName("configuration/list4");
		ServletContext context=request.getSession().getServletContext();
		Map cscanport=new HashMap();
		if(entry!=null && entry.getName()!=null){
			//下发数据
			configurationService.sendData(entry);
		}
		//判断扫描是否在进行中
		if(context.getAttribute(C_START_SCAN)!=null && EHCacheUtil.get("r1818")!=null){
			//正在扫描
			cscanport.put(C_START_SCAN,1);
		}else{
			context.removeAttribute(C_START_SCAN);
		}
		request.setAttribute("cscanport", cscanport);
		return modelAndView;
	}
	//开始扫描
	@ResponseBody  
	@RequestMapping("/startScanData")
	public Map startScanData(HttpServletRequest request,@ModelAttribute("pse") PortServerEntry pse) {
		HashMap map=new HashMap();
		ServletContext context=request.getSession().getServletContext();
		try{
			
			//判断是否可以开始扫描
			if(context.getAttribute(C_START_SCAN)==null){
				EHCacheUtil.remove("r1818");
				//@执行开始端口扫描
				SealedSendMessage.getCmdAppStartTest();
				//扫描端口业务处理(测试)
				//TestThread t=new TestThread();
				//t.start();
				//设置开始扫描状态
				context.setAttribute(C_START_SCAN, "1");
				
				map.put("status", "y");
				map.put("info", "开始扫描");
				
				return map; 
			}else{
				//结束扫描
				//@执行结束端口扫描
				SealedSendMessage.getCmdAppStopTest();
				context.removeAttribute(C_START_SCAN);
				map.put("status", "y");
				map.put("info", "扫描停止中");
			}
		
		}catch(Exception e){
			map.put("status", "n");
			map.put("info", "开启扫描失败");
			log.error("开启扫描异常",e);
			//设置结束扫描状态
			context.removeAttribute(C_START_SCAN);
		}
		return map;
	}
	//取得进度条进度数据和扫描结果
	@ResponseBody  
	@RequestMapping("/getProgress")
	public Map getProgress(HttpServletRequest request) {
		//扫描端口业务处理
		HashMap<String,Object> map=new HashMap<String,Object>();
		ServletContext context=request.getSession().getServletContext();
		try{
			Object tmpO = EHCacheUtil.get("CONNECT_ERROR");
			if(tmpO!=null && "13".equals(tmpO.toString())){
				log.error("扫描失败，请检查设备或网络！");
				map.put("error", "error");
				map.put("info", "扫描失败，请检查设备或网络！");
				map.put("errorNum", tmpO);
				EHCacheUtil.remove("CONNECT_ERROR");
				context.removeAttribute(C_START_SCAN);
				EHCacheUtil.remove("r1818");
			}
			//在缓存中取得当前的进度
			if( EHCacheUtil.get("r1818")!=null){
				EHCacheUtil.put("rr1818", EHCacheUtil.get("r1818"));
				map.put("progress", EHCacheUtil.get("r1818"));
			}else{
				map.put("progress", "0");
			}
			if(Integer.parseInt(map.get("progress").toString()) < 101){
				//扫描正在进行中
				map.put("status", "y");
			}else{
				//扫描结束
				map.put("status", "n");
				EHCacheUtil.remove("r1818");
				context.removeAttribute(C_START_SCAN);
			}
		}catch(Exception e){
			log.error("取得进度条进度数据和扫描结果异常"+e);
			map.put("status", "n");
			map.put("info", "扫描失败");
			context.removeAttribute(C_START_SCAN);
		}
		return map;
	}
	
	/**
	 * 停止扫描
	 * @param request
	 * @return
	 * @author gyk
	 * @data 2016年11月7日
	 */
	@ResponseBody
	@RequestMapping("/closeScan")
	public Map<String,Object> closeScan(HttpServletRequest request){
		//结束扫描
		//@执行结束端口扫描
		SealedSendMessage.getCmdAppStopTest();
		ServletContext context=request.getSession().getServletContext();
		context.removeAttribute(C_START_SCAN);
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("result", "closeScan");
		return m;
	}
}
