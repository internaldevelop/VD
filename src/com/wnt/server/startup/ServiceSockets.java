package com.wnt.server.startup;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.wnt.core.ehcache.EHCacheUtil;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.listener.DataListener;
import com.wnt.server.order.ConstantsServer;
import com.wnt.server.order.SealedSendMessage;
import com.wnt.server.thread.DelChartThread;
import com.wnt.server.thread.LogThread;
import com.wnt.server.thread.MonitorScanstatusThread;
import com.wnt.server.thread.ReturnResultThread;
import com.wnt.server.thread.ReturnSysInfoThread;
import com.wnt.server.thread.ReturnTestNumThread;
import com.wnt.server.thread.ServerPortThread;
import com.wnt.server.thread.ServerSocketThread;
import com.wnt.web.configuration.service.ConfigurationService;
import com.wnt.web.environment.service.EnvironmentService;
import com.wnt.web.login.contorller.LoginContorller;
import com.wnt.web.login.entity.UserEntity;
import com.wnt.web.login.service.LoginService;
import com.wnt.web.portscan.service.PortscanService;
import com.wnt.web.protocol.controller.ReturnProtocolIdentityThread;
import com.wnt.web.protocol.service.ProtocolIndetityService;
import com.wnt.web.socket.service.SocketService;
import com.wnt.web.system.service.SystemService;
import com.wnt.web.testexecute.controller.TestThread;
import com.wnt.web.testexecute.controller.TestThread;
import com.wnt.web.testexecute.service.ChartService;
import com.wnt.web.testexecute.service.TestExecuteService;
import com.wnt.web.testresult.service.PdfService;
import com.wnt.web.testsetup.service.TestSetupService;
import com.wnt.server.entry.ChartEntry;
import com.wnt.server.entry.SysInfoEntry;

import org.wnt.core.uitl.ApplicationContextUtil;
import org.wnt.core.uitl.LogUtil;
import org.wnt.core.uitl.PropertiesUtil;

import common.ConstantsDefs;

public class ServiceSockets implements InitializingBean {
	@Resource
	SocketService socketService;
	@Resource
	EnvironmentService environmentService;
	@Resource
	PortscanService portscanService;
	@Resource
	SystemService systemService;
	@Resource
	ChartService chartService;
	@Resource
	ConfigurationService configurationService;
	@Resource
	TestExecuteService testExecuteService;
	@Resource
	ProtocolIndetityService protocolIdentityService;
	@Resource
	TestSetupService testSetupService;
	@Resource
	PdfService pdfService;
	@Resource
    private LoginService loginService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
//		System.out.println("服务启动了");
		new Thread(new DelChartThread(systemService,chartService)).start();
		
		Thread handlerExecute = new Thread() {
			@Override
			public void run() {
//				try {
//					Thread.sleep(5000); // 等待spring等实例化完成，缓存完成。
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				String fileName = this.getClass().getResource("/ehcache.xml")
//						.getPath();
				// Ehcache启动，未停止，共用一个cache,后可考虑分布式与服务多重分隔
//				EHCacheUtil.initCacheManager(fileName);
//				EHCacheUtil.initCache("authCacheIn");

				//environmentService.updateStatus("2"); // 启动后将监视器置为未选中
				portscanService.deleteAllPort(); // 删除非手动添加
				List<Map<String, Object>> list = portscanService.findport();
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> m = list.get(i);
					SealedSendMessage.getPortUser((Integer) m.get("PORTTYPE"),
							(Integer) m.get("PORTNUM"));
				}

//				ConstantsServer.server.addEventListener("chatevent",
//						ChartEntry.class, new DataListener<ChartEntry>() {
//							@Override
//							public void onData(SocketIOClient client,
//									ChartEntry data, AckRequest ackRequest) {
//								ConstantsServer.server.getBroadcastOperations()
//										.sendEvent("chatevent", data);
//							}
//						});

				ConstantsServer.chatnamespace.addEventListener("message", ChartEntry.class, new DataListener<ChartEntry>() {
		            @Override
		            public void onData(SocketIOClient client, ChartEntry data, AckRequest ackRequest) {
		                // broadcast messages to all clients
		            	ConstantsServer.chatnamespace.getBroadcastOperations().sendEvent("message", data);
		            }
		            
		        });
				ConstantsServer.sysinfonamespace.addEventListener("sysinfo", SysInfoEntry.class, new DataListener<SysInfoEntry>() {
		            @Override
		            public void onData(SocketIOClient client, SysInfoEntry data, AckRequest ackRequest) {
		                // broadcast messages to all clients
		            	ConstantsServer.sysinfonamespace.getBroadcastOperations().sendEvent("sysinfo", data);
		            }
		            
		        });
				ConstantsServer.server.start();
				// 缓存监视器数据4
				// ARP监视器 超时时间
				Map<String, Object> m1 = environmentService.findMonitorById(1);
				EHCacheUtil.put(ConstantsDefs.MARP, m1.get("OVERTIME"));
				// icmp监视器 超时时间
				Map<String, Object> m2 = environmentService.findMonitorById(2);
				EHCacheUtil.put(ConstantsDefs.MICMP, m2.get("OVERTIME"));
				// 离散监视器 告警等级
				Map<String, Object> m4 = environmentService.findMonitorById(4);
				EHCacheUtil.put(ConstantsDefs.MLS, m4.get("ALARMLEVEL"));

				new Thread(new ServerPortThread(socketService)).start();
				new Thread(new ReturnResultThread(socketService)).start();
				new Thread(new LogThread(socketService)).start();
				new Thread(new MonitorScanstatusThread(configurationService,testExecuteService)).start();
				new Thread(new ReturnSysInfoThread(socketService)).start();
				new Thread(new ReturnProtocolIdentityThread(protocolIdentityService)).start();
				new Thread(new ReturnTestNumThread(socketService)).start();
				new Thread(new TestThread(testSetupService,testExecuteService,pdfService)).start();
				PropertiesUtil ph = new PropertiesUtil("sysConfig.properties");
				ConstantsServer.protocolResponseQueue = new LinkedBlockingQueue<byte[]>(
						Integer.valueOf(ph.readProperty("protocolResponseQueue").trim()));
				ConstantsServer.maxNumResponseQueue = new LinkedBlockingQueue<byte[]>(
						Integer.valueOf(ph.readProperty("maxNumResponseQueue").trim()));
				ConstantsServer.sendOrderQueue = new LinkedBlockingQueue<byte[]>(
						Integer.valueOf(ph.readProperty("sendOrderQueue").trim()));
				
				UserEntity userEntity = loginService.queryUser(1);
				if(userEntity != null) {
				    LoginContorller.maxErrorCount = userEntity.getMaxErrorCount();
				    LoginContorller.maxLockTime = userEntity.getMaxLockTime();
				}
				
			}
		};
		handlerExecute.start();

	}

}
