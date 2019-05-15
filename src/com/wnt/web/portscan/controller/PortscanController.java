package com.wnt.web.portscan.controller;

import java.util.ArrayList;
import java.util.Date;
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
import org.wnt.core.uitl.StringUtil;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.server.entry.ChartEntry;
import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.operationlog.service.OperationLogService;
import com.wnt.web.portscan.entry.PortServerEntry;
import com.wnt.web.portscan.service.PortscanService;
import com.wnt.web.servlet.PlatformSynServlet;
import com.wnt.web.socket.dao.impl.SocketDaoImpl;
import com.wnt.web.socket.service.SocketService;

import common.PortScanDefs;


/**
 * 端口扫描控制类
 * 
 * @author 付强
 * @version 1.0
 * @company 汇才同飞
 * @site http://www.javakc.cn
 * 
 */
@Controller
@RequestMapping("/portscan")
public class PortscanController {
	private final Logger log = Logger.getLogger(PortscanController.class.getName());

	private ModelAndView modelAndView;

	private static final String SUCCESS_PAGE = "index";
	private static final String START_SCAN = "startScan";
	
	@Resource
	PortscanService portscanService;
	
	@Resource
    private OperationLogService operationLogService;
	//展示扫描端口页面
	@RequestMapping("/scan")
	public String scan(HttpServletRequest request) {
		ServletContext context=request.getSession().getServletContext();
		Map scanport=new HashMap();
		//取得上次的扫描参数
		scanport.put("beginPort",EHCacheUtil.get("beginPort"));
		scanport.put("endPort",EHCacheUtil.get("endPort"));
		scanport.put("portType",EHCacheUtil.get("portType"));
		scanport.put("scanType",EHCacheUtil.get("scanType"));
		
		//判断扫描是否在进行中
		if(context.getAttribute(START_SCAN)!=null){
			//正在扫描
			scanport.put(START_SCAN,EHCacheUtil.get(START_SCAN));
		}else{
			//没有扫描，取得上次扫描的所有数据
			List<Map<String,Object>> list=portscanService.findScanResult(0);
			request.setAttribute("list", list);
		}
		request.setAttribute("scanport", scanport);
		return "portscan/list";
	}
	//开始扫描
	@ResponseBody  
	@RequestMapping("/startScan")
	public Map startScan(HttpServletRequest request,@ModelAttribute("pse") PortServerEntry pse) {
		HashMap map=new HashMap();
		ServletContext context=request.getSession().getServletContext();
		String userName = request.getSession().getAttribute("userName").toString();
		try{
			Object equipStatus = context.getAttribute(PlatformSynServlet.EQUIP_CONN_STATUS);
			if(equipStatus == null || equipStatus == (Object)false){
				map.put("status", "n");
				map.put("info", "设备连接异常");
				operationLogService.addOperationLog(userName, request, "开启端口扫描", "失败", "设备连接异常");
				return map;
			}
			
			//判断是否可以开始扫描
			if(context.getAttribute(START_SCAN)==null){
				//点击开始扫描时，清空所有非手动添加的结果
				
				PortScanDefs.NUM_ORDER=10;//编号重置
				portscanService.deleteAllPort();
				//保存扫描条件
				EHCacheUtil.put("beginPort", request.getParameter("beginPort"));
				EHCacheUtil.put("endPort", request.getParameter("endPort"));
				EHCacheUtil.put("portType", request.getParameter("portType"));
				EHCacheUtil.put("scanType", request.getParameter("scanType"));
				EHCacheUtil.put(START_SCAN, "1");
				
				String strPortType = request.getParameter("portType");
				String strscanType = request.getParameter("scanType");
				
			    if(!StringUtil.isNumeric(strPortType)) {
                    map.put("status", "n");
                    map.put("info", "Input invalid");
                    return map;
                }
		
			    if(!StringUtil.isNumeric(strscanType)) {
                    map.put("status", "n");
                    map.put("info", "Input invalid");
                    return map;
                }
		
				int beginPort =0;
				int endPort =0;
				int portType= 0;
				int scanType= 0;
				if (request.getParameter("beginPort") ==null && request.getParameter("endPort") ==null) {
					beginPort =0;
					endPort =0;
					portType= Integer.valueOf(request.getParameter("portType"));
					scanType= Integer.valueOf(request.getParameter("scanType"));
				}else{
				    
				    String strbeginPort = request.getParameter("beginPort");
	                String strendPort = request.getParameter("endPort");
	                
	                if(!StringUtil.isNumeric(strbeginPort) || !StringUtil.isNumeric(strendPort)) {
	                    map.put("status", "n");
	                    map.put("info", "Input invalid");
	                    return map;
	                }
					beginPort= Integer.valueOf(request.getParameter("beginPort"));
					endPort= Integer.valueOf(request.getParameter("endPort"));
					portType= Integer.valueOf(request.getParameter("portType"));
					scanType= Integer.valueOf(request.getParameter("scanType"));
				}
				
				if(portType != 6 && portType != 17) {
				    map.put("status", "n");
                    map.put("info", "Input invalid");
                    return map;
				}
				
				if(scanType != 0 && scanType != 1) {
				    map.put("status", "n");
                    map.put("info", "Input invalid");
                    return map;
				}
				log.info("开始端口扫描+++++");
				//@执行开始端口扫描
				SealedSendMessage.getStartScan(scanType, portType, beginPort, endPort);
				
				//扫描端口业务处理(测试)
				//TestThread t=new TestThread();
				//t.start();
				
				//设置开始扫描状态
				context.setAttribute(START_SCAN, "1");
				
				map.put("status", "y");
				map.put("info", "开始扫描");
				
				
				String portTypeName = "TCP";
				if(portType == 17) {
				    portTypeName = "UDP";
				}

				if(scanType == 1) {
				    String content = "端口类型:" + portTypeName + ",扫描类型:被动扫描。";
	                operationLogService.addOperationLog(userName, request, "开启端口扫描", "成功", content);
				}else {
				    String content = "开始端口:" + beginPort + ",结束端口:" + endPort + ",端口类型:" + portTypeName
	                    + ",扫描类型:主动扫描";
	                operationLogService.addOperationLog(userName, request, "开启端口扫描", "成功", content);
				}
				
				return map; 
			}else{
				//结束扫描
				//@执行结束端口扫描
				SealedSendMessage.getStopScan();
				
				//设置结束扫描状态
				context.removeAttribute(START_SCAN);
				map.put("status", "y");
				map.put("info", "结束扫描");
				operationLogService.addOperationLog(userName, request, "结束端口扫描", "成功", "--");
			}
		
		}catch(Exception e){
			map.put("status", "n");
			map.put("info", "开启扫描失败");
			log.error("开启扫描异常",e);
			operationLogService.addOperationLog(userName, request, "开启端口扫描", "失败", "开启扫描异常");
		}
		return map;
	}
	
	//检测端口是否重复
	@ResponseBody  
	@RequestMapping("/checkPort")
	public Map checkPort(HttpServletRequest request,@ModelAttribute("pse") PortServerEntry pse) {
		HashMap map=new HashMap();
		try{
			
			String port_s=request.getParameter("portNum");
			String portType_s=request.getParameter("hportType");
			int port=Integer.valueOf(port_s);
			int portType=Integer.valueOf(portType_s);
			
			boolean b=portscanService.checkPort(port,portType);
			if(!b){
				map.put("status", "y");
			}else{
				map.put("status", "n");
				map.put("info", "端口重复");
			}
		}catch(Exception e){
			log.error("检测端口是否重复异常",e);
			map.put("status", "n");
			map.put("info", "检测失败");
		}
		return map;
	}
	//手动添加扫描端口
	@ResponseBody  
	@RequestMapping("/addPort")
	public Map addPort(HttpServletRequest request,@ModelAttribute("pse") PortServerEntry pse) {
		HashMap map=new HashMap();
		String userName = request.getSession().getAttribute("userName").toString();
		try{
			ServletContext context=request.getSession().getServletContext();
			if(pse.getHportType() != 6 && pse.getHportType() != 17) {
			    map.put("status", "n");
                map.put("info", "Input invalid");
                return map; 
			}

			pse.setPortType(pse.getHportType());
			//判断端口是否重复
			boolean b=portscanService.checkPort(pse.getPortNum(), pse.getPortType());
			if(!b){
				//判断扫描是否开始，开始后不可以添加扫描
				if(context.getAttribute(START_SCAN)==null){
					if(portscanService.checkPort2(pse.getPortNum(), pse.getPortType())){
						//收集页面数据
						pse.setSource("2");// 手动添加
						pse.setDelStatus(0);
						pse.setScanType(2);//未知
						pse.setId(UUIDGenerator.getUUID());
	
						System.out.println(portscanService.save(pse));
//						pse.setId(String.valueOf(id));
						//@执行手动添加端口
						SealedSendMessage.getPortUser(pse.getPortType(),pse.getPortNum());
						map.put("success", true);
						map.put("status", "y");
						map.put("obj", pse);
						map.put("info", "添加成功");
						if(pse.getPortType() == 6) {
						    operationLogService.addOperationLog(userName, request, "添加端口", "成功", "端口:"+pse.getPortNum()+",端口类型:TCP");
						}else {
						    operationLogService.addOperationLog(userName, request, "添加端口", "成功", "端口:"+pse.getPortNum()+",端口类型:UDP");
						}
					}else{
						map.put("success", false);
						map.put("status", "n");
						map.put("info", "添加失败，已添加");
						if(pse.getPortType() == 6) {
		                    operationLogService.addOperationLog(userName, request, "添加端口", "失败", "端口重复，端口:"+pse.getPortNum()+",端口类型:TCP");
		                }else {
		                    operationLogService.addOperationLog(userName, request, "添加端口", "失败", "端口重复，端口:"+pse.getPortNum()+",端口类型:UDP");
		                }
					}
				}
			}else{
				map.put("success", false);
				map.put("info", "端口重复");
				if(pse.getPortType() == 6) {
                    operationLogService.addOperationLog(userName, request, "添加端口", "失败", "端口重复，端口:"+pse.getPortNum()+",端口类型:TCP");
                }else {
                    operationLogService.addOperationLog(userName, request, "添加端口", "失败", "端口重复，端口:"+pse.getPortNum()+",端口类型:UDP");
                }
			}
			
			
		}catch(Exception e){
			log.error("手动添加扫描端口",e);
			map.put("success", false);
			map.put("status", "n");
			map.put("info", "添加失败");
		}
		return map;
	}
	//取得进度条进度数据和扫描结果
	@ResponseBody  
	@RequestMapping("/getProgress")
	public Map getProgress(HttpServletRequest request) {
		//扫描端口业务处理
		HashMap map=new HashMap();
		try{
			Object tmpO = EHCacheUtil.get("CONNECT_ERROR");
			if(tmpO!=null && "11".equals(tmpO.toString())){
				log.error("扫描失败，请检查设备或网络！");
				map.put("success", true);
				map.put("status", "n");
				map.put("info", "扫描失败，请检查设备或网络！");
				map.put("errorNum", tmpO);
				EHCacheUtil.remove("CONNECT_ERROR");
				return map;
			}
			//取得时间点
			long time=Long.valueOf(request.getParameter("time"));
			//查询时间点以后的数据
			List<Map<String,Object>> result = portscanService.findScanResult(time);
			//找到最后的时间
			if(result.size()>0){
				time=(Integer)result.get(0).get("NUM_ORDER");
			}
			//测试
			//EHCacheUtil.put("r187",TestThread.i);
			//System.out.println();
			log.info("缓存中的端口扫描进度："+EHCacheUtil.get("r187"));
			//在缓存中取得当前的进度
			//当主动扫描时,取得扫描进度
			if ("0".equals(EHCacheUtil.get("scanType"))) {
				if( EHCacheUtil.get("r187")!=null){
					if("101".equals(EHCacheUtil.get("r187").toString())){
						map.put("progress", "100");
						EHCacheUtil.remove("r187");
					}else{
						map.put("progress", EHCacheUtil.get("r187"));
					}
				}else{
					map.put("progress", "0");
				}
			}
			//当被动扫描时,不取扫描进度
			if ("1".equals(EHCacheUtil.get("scanType"))) {
				map.put("progress", "0");
			}
			log.info("缓存中的端口扫描进度："+EHCacheUtil.get("r187"));
			map.put("result", result);
			map.put("time",time );
			log.info("端口扫描进度："+map.get("progress"));
			if(!"100".equals(map.get("progress"))){
				//扫描正在进行中
				map.put("status", "y");
			}else{
				//扫描结束
				map.put("status", "n");
				ServletContext context=request.getSession().getServletContext();
				context.removeAttribute(START_SCAN);
			}
		}catch(Exception e){
			log.error("取得进度条进度数据和扫描结果异常"+e);
			map.put("status", "n");
			map.put("info", "扫描失败");
		}
		return map;
	}
	
	//删除端口
	@ResponseBody  
	@RequestMapping("/deletePort")
	public Map deletePort(HttpServletRequest request) {
		//扫描端口业务处理
		HashMap map=new HashMap();
		try{
			System.out.println(request.getParameter("id"));
			portscanService.deletePort(request.getParameter("id"));
			int pro_type = Integer.parseInt(request.getParameter("porttype"));
//			System.out.println(pro_type);
			int port = Integer.parseInt(request.getParameter("portnum"));
//			System.out.println(port);
			SealedSendMessage.getDelPort(port, pro_type, 0);
			map.put("status", "y");
		}catch(Exception e){
			log.error("删除端口异常",e);
			map.put("status", "n");
			map.put("info", "删除失败");
		}
		return map;
	}
	
	
	//停止扫描-测试使用
	@ResponseBody  
	@RequestMapping("/closeScan")
	public Map closeScan(HttpServletRequest request) {
		ServletContext context=request.getSession().getServletContext();
		//TestThread.i=100;
		context.removeAttribute(START_SCAN);
		HashMap map=new HashMap();
		map.put("success", true);
			
		return map; 
	}
	@Resource
	SocketService socketService;
	
	static int n=1;
	@RequestMapping("/test")
	public void test(HttpServletRequest request) {
		
//		List<LogEntry> listLog =new ArrayList();	
//		LogEntry e=new LogEntry();
//		e.setCode("1-1");
//		e.setMessagetype(1);
//		e.setSourcetype(1);
//		listLog.add(e);
//		
//		socketService.addLog(listLog);
		
		new Thread(){
			public void run(){
				while(true){
					List listChart=new ArrayList();
					
					ChartEntry c1=new ChartEntry();
					c1.setNum(PortscanController.n);
					c1.setMonitorid(1);
					
					ChartEntry c2=new ChartEntry();
					c2.setNum(PortscanController.n);
					c2.setMonitorid(2);
					
					ChartEntry c3=new ChartEntry();
					c3.setNum(PortscanController.n);
					c3.setMonitorid(3);
					
					ChartEntry c4=new ChartEntry();
					c4.setNum(PortscanController.n);
					c4.setMonitorid(4);
					
					ChartEntry c13=new ChartEntry();
					c13.setNum(PortscanController.n);
					c13.setMonitorid(13);
					
					ChartEntry c14=new ChartEntry();
					c14.setNum(PortscanController.n);
					c14.setMonitorid(14);
					
					ChartEntry c15=new ChartEntry();
					c15.setNum(PortscanController.n);
					c15.setMonitorid(15);
					
					ChartEntry c16=new ChartEntry();
					c16.setNum(PortscanController.n);
					c16.setMonitorid(16);
					listChart.add(c1);
					listChart.add(c2);
					listChart.add(c3);
					listChart.add(c4);
					listChart.add(c13);
					listChart.add(c14);
					listChart.add(c15);
					listChart.add(c16);
					
					socketService.addChart(listChart);
					
					PortscanController.n++;
					if(PortscanController.n==10){
						PortscanController.n=0;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						log.error(e);
					}
				}
				
			}
		}.start();
		
	}
}
