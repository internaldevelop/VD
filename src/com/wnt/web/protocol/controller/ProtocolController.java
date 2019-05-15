
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: ProtocolController.java 
 * @Prject: VD
 * @Package: com.wnt.web.protocol.controller 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-5-2 上午10:21:15 
 * @version: V1.0   
 */
package com.wnt.web.protocol.controller;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.DataUtils;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.operationlog.service.OperationLogService;
import com.wnt.web.portscan.controller.PortscanController;
import com.wnt.web.protocol.entry.Protocol;
import com.wnt.web.protocol.service.ProtocolIndetityService;
import com.wnt.web.system.entry.Sys;
import com.wnt.web.testexecute.controller.TestThread;
import com.wnt.web.testexecute.entry.TestCaseEntry;
import com.wnt.web.testexecute.entry.TestEntry;
import common.TestExecuteUtil;

/** 
 * @ClassName: ProtocolController 
 * @Description: TODO
 * @author: gyj
 * @date: 2017-5-2 上午10:21:15  
 */
 
@Controller
@RequestMapping("/protocol")
public class ProtocolController {
	private final Logger log = Logger.getLogger(ProtocolController.class.getName());
	private static final String START_IDENTITY = "startIdentity";
	//private static  ReturnProtocolIdentityThread thread =null;
	
	@Resource
	private ProtocolIndetityService protocolIdentityService;
	@Resource
    private OperationLogService operationLogService;
	//展示协议识别页面
	@RequestMapping("/queryProtocolList")
	public String queryProtocolList(HttpServletRequest request) {
		ServletContext context=request.getSession().getServletContext();
		Map proMap=new HashMap();
		//正在识别
		if(context.getAttribute(START_IDENTITY)!=null){
			proMap.put(START_IDENTITY,EHCacheUtil.get(START_IDENTITY));
		}else{
			List<Map<String,Object>> list=protocolIdentityService.findAll();
			request.setAttribute("list", list);
		}
		long  time = new java.util.Date().getTime();
		if(null!=context.getAttribute("time")&& "1".equals(EHCacheUtil.get(START_IDENTITY))){
			context.setAttribute("time",time );
		}
		request.setAttribute("proMap", proMap);
		return "protocolIdentity/list";
	}
	@ResponseBody
	@RequestMapping("/startProtocolIdentity")
	public Map startProtocolIdentity(HttpServletRequest request){
		Map map =new HashMap();
		ServletContext context=request.getSession().getServletContext();
		String userName = request.getSession().getAttribute("userName").toString();
		try {
			if(context.getAttribute(START_IDENTITY)==null){
				EHCacheUtil.put(START_IDENTITY, "1");
				protocolIdentityService.deleteProtocol();
				long  time = new java.util.Date().getTime();
				SealedSendMessage.getStartIdentity();
				context.setAttribute(START_IDENTITY, "1");
				//thread.start();
				map.put("status", "y");
				map.put("info", "开始识别");
				map.put("time",time );
				map.put(START_IDENTITY,EHCacheUtil.get(START_IDENTITY));
				
				operationLogService.addOperationLog(userName, request, "开启协议识别", "成功", "--");
				return map;
			}else{
				SealedSendMessage.getStopIdentity();
				//thread.stop();
				context.removeAttribute(START_IDENTITY);
				map.put(START_IDENTITY,0);
				map.put("status", "s");
				map.put("info", "结束识别");
				operationLogService.addOperationLog(userName, request, "结束协议识别", "成功", "--");
			}
		} catch (Exception e) {
			map.put("status", "n");
			map.put("info", "开启识别失败");
			log.error("开启协议识别异常",e);
			operationLogService.addOperationLog(userName, request, "开启协议识别", "失败", "开启识别失败");
		}
		
	    return map;
	}
	//取得进度条进度数据和扫描结果
		@ResponseBody  
		@RequestMapping("/getProtocol")
		public Map getProtocol(HttpServletRequest request) {
			//扫描端口业务处理
			HashMap map=new HashMap();
			long time=Long.valueOf(request.getParameter("time"));
			try{
				String datetime = DataUtils.formatTime2(time);
				//取得时间点
				//查询时间点以后的数据
				//String datetime = TimeConvertUtil.parseDate(time);
				List<Map<String,Object>> list=protocolIdentityService.findProtocolResult(datetime);
//				//找到最后的时间
				if(list.size()>0){
					Timestamp timestamp = (Timestamp) list.get(0).get("CREATETIME");
					//time=TimeConvertUtil.parseTimeStamp(timestamp);
				    time =DataUtils.getMillis(timestamp);
				}
				map.put("time",time);
				map.put("result", list);
			}catch(Exception e){
				e.printStackTrace();
			}
			return map;
		}
	@ResponseBody
	@RequestMapping("/deleteProtocol")
	public Map deleteProtocol(HttpServletRequest request){
		Map map =new HashMap();
		String userName = request.getSession().getAttribute("userName").toString();
		try{
			protocolIdentityService.deleteProtocol(request.getParameter("id"));
			map.put("status", "y");
			operationLogService.addOperationLog(userName, request, "删除协议", "成功", "id:" + request.getParameter("id"));
		}catch(Exception e){
			log.error("删除协议异常",e);
			map.put("status", "n");
			map.put("info", "删除失败");
			operationLogService.addOperationLog(userName, request, "删除协议", "失败", "删除协议异常");
		}
		return map;
	}

}

