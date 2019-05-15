package com.wnt.web.servlet;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.environment.service.EnvironmentService;
import common.SocketEntityUtil;

public class PlatformSynServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	

	Logger log = Logger.getLogger(PlatformSynServlet.class.getName());
	

	/**
	 * 设备连接状态
	 */
	public static final String EQUIP_CONN_STATUS = "equip_conn_status";
	
	/**
	 * 使能连接状态
	 */
	public static final String ENABLE_CONN_STATUS = "enable_conn_status";
	
	/**
	 * 连接类型
	 */
	public static final String LINKETYPE = "linktype";
	
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		EnvironmentService environmentService = (EnvironmentService) ContextLoader.getCurrentWebApplicationContext().getBean("environmentService");
		ServletContext servletContext = config.getServletContext();
		Map<String,Object> env = environmentService.findEnvironmentByEquipmentId("1");
		//判断数据库有数据,并且漏洞挖掘平台iP或者被测设备IP 不为空
		if(env!=null && env.get("IP")!=null && env.get("IP2")!=null){
			//获取servletContext
			//将ip由数字类型转成字符串类型
			String ip=SocketEntityUtil.ipString((Long)env.get("IP"));
			env.put("IP",ip);
			String ip2=SocketEntityUtil.ipString((Long)env.get("IP2"));
			env.put("IP2",ip2);
			
			if(env.get("IP3")!=null){
				//判断Eth1 是否为空
				String ip3 = SocketEntityUtil.ipString((Long)env.get("IP3"));
				env.put("IP3", ip3);
			}
			log.info(env);
			
			//判断连接类型，1为桥接，2位点对点,如果为空
			Object linkType = env.get("LINKTYPE");
			if(linkType == null || "1".equals(linkType.toString())){
				SealedSendMessage.getLinkType(1);
				//端口扫描页面需要使用
				servletContext.setAttribute(LINKETYPE, "1");
				
				//获取使能是否选中 1为选中 2为未选中
				Object enable2 = env.get("ENABLE2");
				if(enable2 != null && "1".equals(enable2.toString()) && env.get("IP3")!=null ){
					//如果使能选中下发使能检测命令
					SealedSendMessage.getConnectIp(ip, env.get("IP3").toString(), 1);
				}
			}else{
				//
				SealedSendMessage.getLinkType(0);
				servletContext.setAttribute(LINKETYPE, "2");
			}
			//下发在测设备检测命令
			SealedSendMessage.getConnectIp(ip,ip2,0);

			//取得所有监视器
			List<Map<String, Object>> list = environmentService.findMonitorDetail((Integer)env.get("EQUIPMENTID"));
			if(list!=null && list.size()>0){
				//@执行开启监控器，按不同的监视器，传不同的参数
				for(Map<String,Object> m:list){
					//开启监控器
					if("1".equals(m.get("SELECTSTATUS").toString())){
						if(m.get("ID").equals(3)){//离散数据监视器
							SealedSendMessage.getStartMonitor((Integer)m.get("ID"), (Integer)m.get("CYCLEPERIOD"), (Integer)m.get("INPUT"),0);
						}else if(m.get("ID").equals(4)){//Tcp
							SealedSendMessage.getStartMonitor((Integer)m.get("ID"), (Integer)m.get("CYCLEPERIOD"), (Integer)m.get("INPUT"),(Integer)m.get("ALARMLEVEL"));
						}else{//1,2
							SealedSendMessage.getStartMonitor((Integer)m.get("ID"), (Integer)m.get("OVERTIME"), 0,0);
						}
					}else{
						//关闭监视器
						SealedSendMessage.getStopMonitor((Integer)m.get("ID"));
					}
				}
			}
		}
	}

}
