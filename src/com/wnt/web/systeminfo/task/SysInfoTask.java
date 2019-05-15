
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: SysInfoTack.java 
 * @Prject: VD
 * @Package: com.wnt.web.systeminfo.task 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-5-17 上午10:35:13 
 * @version: V1.0   
 */ package com.wnt.web.systeminfo.task; import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.wnt.core.uitl.ApplicationContextUtil;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.server.entry.LogEntry;
import com.wnt.server.order.ConstantsDefs;
import com.wnt.server.thread.ReturnSysInfoThread;
import com.wnt.web.socket.service.SocketService;
import com.wnt.web.socket.service.impl.SocketServiceImpl;
import com.wnt.web.systeminfo.entry.SystemInfoRatioDto;


/** 
 * @ClassName: SysInfoTack 
 * @Description: TODO
 * @author: gyj
 * @date: 2017-5-17 上午10:35:13  
 */
@Component("SysInfoTask")
public class SysInfoTask {
//	private static SocketService socketService = (SocketService) ApplicationContextUtil.getContext()
//			.getBean(SocketServiceImpl.class);
	@Resource
	SocketService socketService;
	List<LogEntry> listLog = new ArrayList<LogEntry>();
	
	// 配置文件定时时间为60s
		@Scheduled
		public void checkRatio() {

			// System.out.println("checkRatio---");
			final ConcurrentHashMap<String, Object> sysInfoRatioMap = ReturnSysInfoThread.sysInfoRatioMap;

			if ((null != sysInfoRatioMap) && (sysInfoRatioMap.size() > 0)) {

					final SystemInfoRatioDto systemInfoRatioDto = (SystemInfoRatioDto) sysInfoRatioMap.get("sysInfoRatio");
					/* CPU总体使用率60s内连续超过70%的次数 */
					int cpuRatio =(Integer)sysInfoRatioMap.get("cpuOverburdenCount");
					/* 内存60s内连续超过80%的次数 */
					int memRatio = (Integer)sysInfoRatioMap.get("memOverburdenCount");
					/* 内存60s内连续超过80%的次数 */
					int diskRatio = (Integer)sysInfoRatioMap.get("diskOverburdenCount");
					// 报警日志准备
					LogEntry logEntry =null;
					if (cpuRatio >= 60) {
						logEntry =new LogEntry();
						logEntry.setId(UUIDGenerator.getUUID());
						logEntry.setSource("CPU状态告警");
						logEntry.setMessage("CPU使用率超过80%");
						listLog.add(logEntry);
					}
					if (memRatio >= 60) { 
						logEntry =new LogEntry();
						logEntry.setId(UUIDGenerator.getUUID());
						logEntry.setSource("内存状态告警");
						logEntry.setMessage("内存使用率超过80%");
						listLog.add(logEntry);
					}
					if (diskRatio >= 60) { 
						logEntry =new LogEntry();
						logEntry.setId(UUIDGenerator.getUUID());
						logEntry.setSource("硬盘状态告警");
						logEntry.setMessage("硬盘使用率超过90%");
						listLog.add(logEntry);
					}

				// 报警日志入库
				 if (listLog.size() > 0) {
						socketService.addSysInfoLog(listLog);
				 }
				 listLog.clear();
			}
				sysInfoRatioMap.put("cpuOverburdenCount",0);
				sysInfoRatioMap.put("memOverburdenCount",0);
				sysInfoRatioMap.put("diskOverburdenCount",0);
			
		}

}

