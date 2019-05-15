package com.wnt.server.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.LogUtil;
import org.wnt.core.uitl.SocketEntityUtil;
import org.wnt.core.uitl.UUIDGenerator;
import com.wnt.server.entry.ChartEntry;
import com.wnt.server.entry.LogEntry;
import com.wnt.server.entry.PortEntry;
import com.wnt.server.entry.SysInfoEntry;
import com.wnt.server.order.ConstantsServer;
import com.wnt.server.order.TypeCmd;
import com.wnt.web.dynamicports.entry.DynamicPorts;
import com.wnt.web.socket.service.SocketService;
import com.wnt.web.systeminfo.entry.SystemInfoRatioDto;

/**
 * 返回结果子线程
 * 
 *
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class ReturnSysInfoThread implements Runnable {
	List<LogEntry> listLog = new ArrayList<LogEntry>();
	private SocketService socketService;
	List<SysInfoEntry> listSysInfo = new ArrayList<SysInfoEntry>();
	public static ConcurrentHashMap<String, Object> sysInfoRatioMap = new ConcurrentHashMap<String, Object>(); 
	int count = 0;
	public static AtomicInteger cpuOverburdenCount =new AtomicInteger();
	public static AtomicInteger memOverburdenCount =new AtomicInteger();
	public static AtomicInteger diskOverburdenCount =new AtomicInteger();
	
	// int returnn = 0;//自增
	public ReturnSysInfoThread(SocketService socketService) {
		this.socketService = socketService;
	}

	@Override
	public void run() {
		try {
			ConstantsServer.SYSINFOFLAG++;
//			String str = "";
			// returnn++;
			while (true) {
//				str = "";
				if (EHCacheUtil.get("sysinfo" + ConstantsServer.SYSINFOFLAG) != null) {
//					System.out.println("return");
					// System.out.println("no");
					// 获取缓存中数据
					SysInfoEntry sysinfo =new SysInfoEntry();
					byte[] bt = (byte[]) EHCacheUtil.get("sysinfo" + ConstantsServer.SYSINFOFLAG);
					EHCacheUtil.remove("sysinfo" + ConstantsServer.SYSINFOFLAG);
					ConstantsServer.SYSINFOFLAG++;

//					long iptemp = SocketEntityUtil.byteToLong(bt, 16, 4);
//					String managerip = SocketEntityUtil.ipString(iptemp);
					 float cputemp = SocketEntityUtil.byteToInt(bt, 16, 4);
					 float cpu =cputemp/100;
					 float memtemp = SocketEntityUtil.byteToInt(bt, 20, 4);
					 float mem =memtemp/100;
					 float disktemp = SocketEntityUtil.byteToInt(bt, 24, 4);
					 float disk =disktemp/100;
					 byte[] net=new byte[12];
					 System.arraycopy(bt, 28, net, 0, 12);
					 int net1 =SocketEntityUtil.byteToInt(net,0,2);
					 int net2 =SocketEntityUtil.byteToInt(net,2,2);
					 int net3 =SocketEntityUtil.byteToInt(net,4,2);
					 int net4 =SocketEntityUtil.byteToInt(net,6,2);
					 int net5 =SocketEntityUtil.byteToInt(net,8,2);
					 int net6 =SocketEntityUtil.byteToInt(net,10,2);
					// sysinfo.setIp(managerip);
					 sysinfo.setCpu(cpu);
					 sysinfo.setDisk(disk);
					 sysinfo.setMem(mem);
					 sysinfo.setNet1(net1);
					 sysinfo.setNet2(net2);
					 sysinfo.setNet3(net3);
					 sysinfo.setNet4(net4);
					 sysinfo.setNet5(net5);
					 sysinfo.setNet6(net6);
					 ConstantsServer.sysinfonamespace.getBroadcastOperations().sendEvent("sysinfo", sysinfo);
					 //处理磁盘容量告警日志
					 SystemInfoRatioDto srd = new SystemInfoRatioDto();
					  	srd.setCpuRatio(cpu);
					  	srd.setMemRatio(mem);
					  	srd.setDiskRatio(disk);
//					  	AtomicInteger cpuOverburdenCount = srd.getCpuOverburdenCount();
//					  	AtomicInteger memOverburdenCount = srd.getMemOverburdenCount();
//					  	AtomicInteger diskOverburdenCount= srd.getDiskOverburdenCount();
					  	if (sysinfo.getCpu()>=80) {
					  		cpuOverburdenCount.getAndIncrement();
						}else{
							cpuOverburdenCount.set(0);
						}
					  	if (sysinfo.getMem()>=80) {
					  		memOverburdenCount.getAndIncrement();
						}else{
							memOverburdenCount.set(0);
						}
					  	if (sysinfo.getDisk()>=90) {
					  		diskOverburdenCount.getAndIncrement();
						}else{
							diskOverburdenCount.set(0);
						}
//					  	srd.setCpuOverburdenCount(cpuOverburdenCount);
//						srd.setMemOverburdenCount(memOverburdenCount);
//						srd.setMemOverburdenCount(diskOverburdenCount);
						//存入超负荷信息，待定时任务检查
						sysInfoRatioMap.put("sysInfoRatio", srd);
						sysInfoRatioMap.put("cpuOverburdenCount", cpuOverburdenCount.get());
						sysInfoRatioMap.put("memOverburdenCount", memOverburdenCount.get());
						sysInfoRatioMap.put("diskOverburdenCount", diskOverburdenCount.get());
				}
				Thread.sleep(1);
			}
		} catch (InterruptedException e) {
		e.printStackTrace();

		}
	}
}
