package com.wnt.web.environment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.StringUtil;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.configuration.entry.ConfigurationEntry;
import com.wnt.web.configuration.service.ConfigurationService;
import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.environment.entry.MonitorDetailEntry;
import com.wnt.web.environment.service.EnvironmentService;
import com.wnt.web.login.service.LoginService;
import com.wnt.web.operationlog.service.OperationLogService;
import com.wnt.web.task.MonitorTask;

import common.ConstantsDefs;
import common.EquipmentDef;
import common.SocketEntityUtil;

/**
 * 环境设置（设置网络测试）控制类
 * 
 * @author 付强
 * @version 1.0
 * @company 汇才同飞
 * @site http://www.javakc.cn
 * 
 */
@Controller
@RequestMapping("/environment")
public class EnvironmentController {
	
	@Resource
	EnvironmentService environmentService;
	
	@Resource
	ConfigurationService configurationService;
	
	@Resource
	private OperationLogService operationLogService;
	
	private final Logger log = Logger.getLogger(EnvironmentController.class.getName());

	private ModelAndView modelAndView;

	private static final String SUCCESS_PAGE = "index";
	private static final String LINKETYPE = "linktype";

	@RequestMapping("/create")
	public String leftcaidan(HttpServletRequest request) {
		try{
			//取得设备信息
			Map env=environmentService.findEnvironmentByEquipmentId("1");
			//将ip由数字类型转成字符串类型
			if(env.get("IP")!=null){
				String ip=SocketEntityUtil.ipString((Long)env.get("IP"));
				env.put("IP",ip);
			}
			if(env.get("IP2")!=null){
				String ip2=SocketEntityUtil.ipString((Long)env.get("IP2"));
				env.put("IP2",ip2);
			}
			if(env.get("IP3")!=null){
				String ip3=SocketEntityUtil.ipString((Long)env.get("IP3"));
				env.put("IP3",ip3);
			}
			log.info(env);
			request.setAttribute("env", env);
			
			//取得显示器信息
			List<Map<String, Object>> monitors=environmentService.findMonitor((Integer)env.get("EQUIPMENTID"));
			request.setAttribute("monitors", monitors);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "environment/list";
	}
	
	//保存监视器信息（修改）
	@ResponseBody  
	@RequestMapping("/save")
	public Map save(HttpServletRequest request,@ModelAttribute("env") EquipmentEntry env) {
		ServletContext context=request.getSession().getServletContext();
		try{
		    HashMap map=new HashMap();
		    
		    if(StringUtil.htmlCharRegex(env.getName())		
		            || StringUtil.htmlCharRegex(env.getVersion())
		            || StringUtil.htmlCharRegex(env.getRemark())) {
		        map.put("status", "n");
                map.put("info", "Input is invalid");
                return map;
		    }

			//如果选择点对点，设置使能2为2
		    String lingTypeName = "点对点";
			if(env.getLinkType()==2){
				env.setEnable2(2);
				SealedSendMessage.getLinkType(0);
				context.setAttribute(LINKETYPE, "2");
			}else if(env.getLinkType()==1){
				env.setEnable2(1);
				SealedSendMessage.getLinkType(1);
				context.setAttribute(LINKETYPE, "1");
				lingTypeName = "桥接";
			}else {
			    map.put("status", "n");
	            map.put("info", "Input is invalid");
	            return map;
			}
			
			String old_name=request.getParameter("old_name");
			//修改设备信息
			environmentService.update1(env,old_name);
			
			map.put("status", "y");
			map.put("info", "成功保存");
			
			String userName = request.getSession().getAttribute("userName").toString();
			operationLogService.addOperationLog(userName, request, "修改被测设备", "成功", "被测设备名称："+ env.getName() 
					+ ",连接类型：" + lingTypeName + ",被测设备型号：" + env.getVersion() + ",备注：" + env.getRemark());
			return map;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	//启动开始查找mac地址(自动检测)
	@ResponseBody  
	@RequestMapping("/findMac")
	public Map findMac(HttpServletRequest request,@ModelAttribute("env") EquipmentEntry env) {
		try{
			//收集界面的ip
			String ip=request.getParameter("ips");
			String ip2=request.getParameter("ips2");
			/*System.out.println("ping tai ip"+ip);
			System.out.println("bei ce she bei ip"+ip2)*/;
			
			HashMap map=new HashMap();
			
			if(!StringUtil.ipRegex(ip) || !StringUtil.ipRegex(ip2)) {
                map.put("status", "n");
                map.put("info", "IP Address is invalid!");
                return map;
            }
			
			//@执行检测命令
			SealedSendMessage.getConnectIp(ip,ip2,0);
			if(env.getIp2()==null){
				ConfigurationEntry ce = new ConfigurationEntry();
				ce.setHostip(ip2);
				configurationService.updateData(ce);
			}						
			
			map.put("status", "y");
			map.put("info", "正在查找中");
			return map;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	//mac地址找到后，保存数据
	@ResponseBody  
	@RequestMapping("/save2")
	public Map save2(HttpServletRequest request,@ModelAttribute("env") EquipmentEntry env) {
		HashMap map=new HashMap();
		try{
			String id=request.getParameter("id");
			//在缓存中取得ip的mac地址
			Object o_mac1=EHCacheUtil.get("r"+id+"001");
			Object o_mac2=EHCacheUtil.get("r"+id+"002");
		/*	System.out.println("r"+id+"001"+":o_mac1"+o_mac1);
			System.out.println("r"+id+"002"+":o_mac2"+o_mac2);*/
			String cishu = request.getParameter("cishu");
			//收集界面的ip
			String ips=request.getParameter("ips");
			String ips2=request.getParameter("ips2");					
            
			if(!StringUtil.ipRegex(ips) || !StringUtil.ipRegex(ips2)) {
			    map.put("status", "n");
                map.put("info", "IP Address is invalid!");
                return map;
			}
			
			if(!StringUtil.ipRegex(env.getSubnetMask()) || !StringUtil.ipRegex(env.getSubnetMask2())) {
                map.put("status", "n");
                map.put("info", "IP Address Mask is invalid!");
                return map;
            }
			
			if(!StringUtil.macRegex(env.getMac()) || !StringUtil.macRegex(env.getMac2())) {
                map.put("status", "n");
                map.put("info", "MAC is invalid!");                                        
                return map;
            }
						
			Long ip1 = null,ip2 = null;
			if(StringUtils.isNotBlank(ips) && StringUtils.isNotBlank(ips2)){
				ip1=SocketEntityUtil.ipToLong(ips);
				ip2=SocketEntityUtil.ipToLong(ips2);
				env.setIp(ip1);
				env.setIp2(ip2);
			}
			//判断是否检测到ip的mac地址
			if(o_mac1!=null){
				//ip对应的mac
				String mac1=(String)o_mac1;
				String mac2=(String)o_mac2;
				env.setMac(mac1);
				env.setMac2(mac2);
				if("00:00:00:00:00:00".equals(o_mac1)||"00:00:00:00:00:00".equals(o_mac2)){
					System.out.println("未检测到在测设备");
					env.setIsEth0Exist(false);
					environmentService.update2(env);
					map.put("status", "n");
					map.put("info", "未检测到在测设备");
					
					String userName = request.getSession().getAttribute("userName").toString();
		            operationLogService.addOperationLog(userName, request, "Eth0端口自动检测", "失败", "测试工具IP地址："+ ip1 
		                    + ",被测设备IP地址：" + ip2);
				}else{
					env.setIsEth0Exist(true);
					environmentService.update2(env);
					EquipmentDef.check=true;
					//清除缓存中的数据
					EHCacheUtil.remove("r"+id+"001");
					EHCacheUtil.remove("r"+id+"002");
					//返回前台的数据
					map.put("status", "y");
					map.put("info", "保存成功");
					
					String userName = request.getSession().getAttribute("userName").toString();
		            operationLogService.addOperationLog(userName, request, "Eth0端口自动检测", "成功", "测试工具IP地址："+ ip1 
		                    + ",被测设备IP地址：" + ip2 + "测试工具MAC地址：" + o_mac1 + ",被测设备MAC地址：" + o_mac2);
				}
				map.put("mac", new String[]{mac1,mac2});
			}else{
				if("4".equals(cishu)){
					env.setMac("00:00:00:00:00:00");
					env.setMac2("00:00:00:00:00:00");
					env.setIsEth0Exist(false);
					environmentService.update2(env);
				}
				map.put("status", "n");
				String userName = request.getSession().getAttribute("userName").toString();
                operationLogService.addOperationLog(userName, request, "Eth0端口自动检测", "失败", "测试工具IP地址："+ ip1 
                        + ",被测设备IP地址：" + ip2);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	//启动开始查找mac地址（第三部分）
	@ResponseBody  
	@RequestMapping("/findCheck2")
	public Map findCheck2(HttpServletRequest request,@ModelAttribute("env") EquipmentEntry env) {
		try{
			//收集界面的ip
			String ip4=request.getParameter("ips4");
			String ip3=request.getParameter("ips3");
			
			HashMap map=new HashMap();
			
			if(!StringUtil.ipRegex(ip3) || !StringUtil.ipRegex(ip4)) {
                map.put("status", "n");
                map.put("info", "IP Address is invalid!");
                return map;
            }
			
			//@执行检测命令
			SealedSendMessage.getConnectIp(ip4, ip3,1);

			map.put("status", "y");
			map.put("info", "正在查找中");
			return map;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	@ResponseBody  
	@RequestMapping("/save3")
	public Map save3(HttpServletRequest request,@ModelAttribute("env") EquipmentEntry env) {
		HashMap map=new HashMap();
		try{
		    
		    if(!StringUtil.ipRegex(env.getSubnetMask3())) {
                map.put("status", "n");
                map.put("info", "IP Address Mask is invalid!");
                return map;
            }
            
            if(!StringUtil.macRegex(env.getMac3())) {
                map.put("status", "n");
                map.put("info", "MAC is invalid!");         
                return map;
            }
            
			String id=request.getParameter("id");
			//在缓存中取得ip的mac地址
			Object o_mac3=EHCacheUtil.get("r"+id+"003");
			/*System.out.println("r"+id+"003"+":o_mac3"+o_mac3);*/
			//判断是否检测到ip的mac地址
			//收集界面的ip
			String ips3=request.getParameter("ips3");    
			if(o_mac3!=null){						
				if(!StringUtil.ipRegex(ips3)) {
	                map.put("status", "n");
	                map.put("info", "IP Address is invalid!");
	                return map;
	            }

				String mac3=(String)o_mac3;
				if(StringUtils.isNotEmpty(ips3)){
					//将ip转换成数字
					long ip3=SocketEntityUtil.ipToLong(ips3);
					env.setIp3(ip3);
					//ip对应的mac
					env.setMac3(mac3);
					//判断使能，使能：1为选中 2为未选中
					String enable2=request.getParameter("enable2");
					if(enable2==null){
						env.setEnable2(2);
					}
				}
				if("00:00:00:00:00:00".equals(o_mac3)){
					env.setIsEth1Exist(false);
					environmentService.update3(env);
					map.put("status", "n");
					map.put("info", "未检测到在测设备");
					
					String userName = request.getSession().getAttribute("userName").toString();
		            operationLogService.addOperationLog(userName, request, "Eth1端口自动检测", "失败", "控制系统IP地址："+ ips3);
				}else{
					env.setIsEth1Exist(true);
					environmentService.update3(env);
					EquipmentDef.check=true;
					//清除缓存中的数据
					EHCacheUtil.remove("r"+id+"003");
					
					map.put("status", "y");
					map.put("info", "保存成功");
					map.put("mac", mac3);
					
					String userName = request.getSession().getAttribute("userName").toString();
		            operationLogService.addOperationLog(userName, request, "Eth1端口自动检测", "成功", "控制系统IP地址："+ ips3 
		                    + ",MAC地址：" + o_mac3);
				}
			}else{
				map.put("status", "n");
				String userName = request.getSession().getAttribute("userName").toString();
                operationLogService.addOperationLog(userName, request, "Eth1端口自动检测", "失败", "控制系统IP地址："+ ips3);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
		
	}
	//当用户选择监视器的复选框后执行
	@ResponseBody
	@RequestMapping("/selectMinitor")
	public Map selectMinitor(HttpServletRequest request,@ModelAttribute("env") EquipmentEntry env) {
		try{
			String selected=request.getParameter("selected");//选中为1，未选中为2 
			String envId=request.getParameter("envId");
			String mid=request.getParameter("mid");
			//取得设备信息
			environmentService.selectMinitor(selected, envId, mid);
			
			HashMap map=new HashMap();
			map.put("status", "y");
			map.put("info", "保存成功");
			map.put("success", true);
			return map;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping("/saveEth1")
	public void saveEth1(HttpServletRequest request) {
		try{
			String selected=request.getParameter("selected");//选中为1，未选中为2 
			
			environmentService.updateEth1(selected);	
			
			String userName = request.getSession().getAttribute("userName").toString();
			if("1".equals(selected)) {
			    operationLogService.addOperationLog(userName, request, "Eth1端口", "成功", "使能");
			}else {
			    operationLogService.addOperationLog(userName, request, "Eth1端口", "成功", "禁止");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//通过id得到监视器明细信息
	@ResponseBody  
	@RequestMapping("/findMonitorById")
	public Map findMonitorById(HttpServletRequest request,Integer id) {
		try{
			Map map=environmentService.findMonitorById(id);
			return map;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	//页面光标离开时，保存监视器明细
	@ResponseBody  
	@RequestMapping("/save4")
	public Map save4(HttpServletRequest request,@ModelAttribute("md") MonitorDetailEntry md) {
		try{
			if(request.getParameter("tcpports")==null){
				md.setTcpports(2);//未选中状态
			}		
			//更改缓存中的监视器信息
			if(md.getMonitorId()==1){
				EHCacheUtil.put(ConstantsDefs.MARP, md.getOvertime());
			}else if(md.getMonitorId()==2){
				EHCacheUtil.put(ConstantsDefs.MICMP, md.getOvertime());
			}else if(md.getMonitorId()==4){
				EHCacheUtil.put(ConstantsDefs.MLS, md.getAlarmLevel());
			}
			
			//取得设备信息
			environmentService.updateMonitorDetail(md);
			HashMap map=new HashMap();
			map.put("success", true);
			return map;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	//下发监视器设置(开启监控器/关闭监视器)
	@ResponseBody  
	@RequestMapping("/saveMonitor")
	public Map saveMonitor(HttpServletRequest request) {
		HashMap map=new HashMap();
		try{
			//取得所有监视器
			List<Map<String, Object>> list=environmentService.findMonitorDetail(1);
			String userName = request.getSession().getAttribute("userName").toString();
           
			String content = "";
			//@执行开启监控器，按不同的监视器，传不同的参数
			for(Map<String,Object> m:list){
				//开启监控器
			    int monitorId = (Integer)m.get("ID");
				if("1".equals(m.get("SELECTSTATUS").toString())){				    
					if(m.get("ID").equals(3)){//tcp
						SealedSendMessage.getStartMonitor((Integer)m.get("ID"), (Integer)m.get("CYCLEPERIOD"), (Integer)m.get("INPUT"),0);
						content += "tcpMonitor开启;";
					}else if(m.get("ID").equals(4)){//di
						SealedSendMessage.getStartMonitor((Integer)m.get("ID"), (Integer)m.get("CYCLEPERIOD"), (Integer)m.get("INPUT"),(Integer)m.get("ALARMLEVEL"));
						content += "discreteMonitor开启,周期:" + (Integer)m.get("CYCLEPERIOD") + "ms,单个输入:input " + (Integer)m.get("INPUT")
                        + ",告警等级:" + (Integer)m.get("ALARMLEVEL") + "%;";
					}else{//1,2
						SealedSendMessage.getStartMonitor((Integer)m.get("ID"), (Integer)m.get("OVERTIME"), 0,0);
						if(monitorId == 1) {
						    content += "arpMonitor开启,周期:" + (Integer)m.get("OVERTIME") + "ms;";
						}else if(monitorId == 2) {
                            content += "icmpMonitor开启,周期:" + (Integer)m.get("OVERTIME") + "ms;";
                        }else if(monitorId == 5) {
                            content += "aiMonitor开启;";
                        }else if(monitorId == 10) {
                            content += "udpMonitor开启;";
                        }
					}
				}else{
					//关闭监视器
					SealedSendMessage.getStopMonitor((Integer)m.get("ID"));
					MonitorTask.chartMap.remove(m.get("MONITORID"));
					if(monitorId == 1) {
                        content += "arpMonitor关闭;";
                    }else if(monitorId == 2) {
                        content += "icmpMonitor关闭;";
                    }else if(monitorId == 3) {
                        content += "tcpMonitor关闭;";
                    }else if(monitorId == 2) {
                        content += "discreteMonitor关闭;";
                    }else if(monitorId == 5) {
                        content += "aiMonitor关闭;";
                    }else if(monitorId == 10) {
                        content += "udpMonitor关闭;";
                    }
				}								
			}
			
			operationLogService.addOperationLog(userName, request, "监视器配置", "成功", content);
			
			map.put("status", "y");
		}catch(Exception e){
			map.put("status", "n");
			e.printStackTrace();
		}
		return map;
	}
	
}
