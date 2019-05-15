package com.wnt.web.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.SocketEntityUtil;

import com.wnt.server.entry.ChartEntry;
import com.wnt.server.order.ConstantsServer;
import com.wnt.web.socket.service.SocketService;

/**
 * 监视器
 * @author WNTM
 *
 */
@Component("monitorTask")
public class MonitorTask {
	public static Map<Integer,byte[]> chartMap = new ConcurrentHashMap<Integer,byte[]>();
	public static Map<Integer,ChartEntry> monitorMap = new ConcurrentHashMap<Integer,ChartEntry>();
	List<ChartEntry> listChart = new ArrayList<ChartEntry>();
	
	@Autowired
	SocketService socketService;
	
	@Scheduled
	public void executePush(){
		//System.out.print("定时器 "+DataUtils.formatDateTime());
		for(Integer key : chartMap.keySet()){
			ChartEntry chartEntry = new ChartEntry();
			byte[] bt = chartMap.get(key); //获取数据
			int statevalue = SocketEntityUtil.byteToInt(bt, 24, 4);  //获取值
			chartEntry.setMonitorid(key);
			chartEntry.setNum(statevalue);
			chartEntry.setTimestamp(DataUtils.getMillis());
			chartEntry.setCreatetime(DataUtils.formatDateTime());
			
			switch (key) {
				// ARP时延
				case 1:
					if (((float)statevalue/(float)100) < ((Integer) EHCacheUtil.get("marp"))) {
						chartEntry.setNum1((float)statevalue/(float)100);
					}
					break;
				// ICMP时延
				case 2:
					if (((float)statevalue/(float)100) < ((Integer) EHCacheUtil.get("micmp"))) {
						chartEntry.setNum1((float)statevalue/(float)100);
					}			
					break;
				/*// 离散数据
				case 4:
					if (!(statevalue < ((Integer) EHCacheUtil.get("mls")))) {
						chartEntry.setNum(null);
					}
					break;*/
			}
			monitorMap.put(key, chartEntry);
			listChart.add(chartEntry);
		}
		ConstantsServer.chatnamespace.getBroadcastOperations().sendEvent("message", monitorMap);
		if(listChart.size()>0){
			socketService.addChart(listChart);
			listChart.clear();
		}
	}
	
}
