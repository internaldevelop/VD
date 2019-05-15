package com.wnt.web.testexecute.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.wnt.core.uitl.DataUtils;

import com.wnt.web.testexecute.dao.ChartDao;
import com.wnt.web.testexecute.service.ChartService;
@Service("chartService")
public class ChartServiceImpl implements ChartService {
	public static String[] tableNames = new String[]{"LDWJ_CHART_ARP","LDWJ_CHART_ICMP","LDWJ_CHART_TCP",
			"LDWJ_CHART_DISCRETE","LDWJ_CHART_ETH0_2","LDWJ_CHART_ETH0_1","LDWJ_CHART_ETH1_1","LDWJ_CHART_ETH1_2"};
	
	@Resource
	ChartDao chartDao;
	@Override
	public List<Object[]> queryArp(int type,long time) {
		//System.out.println("ChartServiceImpl"+type);
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = chartDao.findCharByType(type,time);
		List arpList = new ArrayList();
		for(Map map:list){
			arpList.add(map.values().toArray());
		}
		return arpList;
	}

	@Override
	public List<Map<String, Object>> queryArp(String[] type,String lastId) {
		//拼sql
		StringBuffer typeParam=new StringBuffer(100);
		typeParam.append("( 1=2 ");
		if(type!=null){
			Arrays.sort(type);
			//ARP 散点图 1
			if(Arrays.binarySearch(type, "1")>=0){
				typeParam.append(" OR LC.CHARTTYPE=1");
			}
			//ICMP 散点图 2
			if(Arrays.binarySearch(type, "2")>=0){
				typeParam.append(" OR LC.CHARTTYPE=2");
			}
			//TCP 折线图 3
			if(Arrays.binarySearch(type, "3")>=0){
				typeParam.append(" OR LC.CHARTTYPE=3");
			}
			//离散 方波图 4
			//离散 散点图 4
			if(Arrays.binarySearch(type, "4")>=0){
				typeParam.append(" OR LC.CHARTTYPE=4");
			}
			//Eth0 折线图 发送 eth0_1  14
			//Eth0 折线图 接受 eth0_2 13
			if(Arrays.binarySearch(type, "eth0")>=0){
				typeParam.append(" OR LC.CHARTTYPE=14 OR LC.CHARTTYPE=13");
			}
			
			//Eth1 折线图 发送 eth1_1 16
			//Eth1 折线图 接受 eth1_2 15
			if(Arrays.binarySearch(type, "eth1")>=0){
				typeParam.append(" OR LC.CHARTTYPE=16 OR LC.CHARTTYPE=15");
			}
		}
		typeParam.append(")");
		List<Map<String, Object>> list;
		if("-1".equals(lastId)){
			list = chartDao.findLastCharByType(typeParam.toString()); 
		}else{
			list = chartDao.findCharByType(typeParam.toString(),lastId); 
		}
		
		
		return list;
	}
	
	public String getMaxid(){
		return chartDao.getMaxid().get("MID").toString();
	}
	@Override
	public Map queryTcp(int type,long time) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = chartDao.findCharByType(type,time);
		List tcpxList = new ArrayList();
		List tcpyList = new ArrayList();
		Map result = new HashMap();
		for(Map map:list)
		{
			Date date = (Date)map.get("CREATETIME");
			tcpxList.add(date);
			int num = (Integer)map.get("NUM");
			tcpyList.add(num);
		}
		result.put("tcpx", tcpxList);
		result.put("tcpy", tcpyList);
		return result;
	}
	
	@Override
	public void insertBatch(int chartype) {
		// TODO Auto-generated method stub
		Random a = new Random();
		//图形类型 1为ARP 2为ICMP 3为TCP 4为离散数据 13为端口0流量 14为端口1流量
		for(int i=1;i<5;i++)
		{
			int rom = a.nextInt(100);
			int month = a.nextInt(9);
			int date = a.nextInt(29);
			//随机生成 1或2
			//int chartype = a.nextInt(2);
			//随机生成 13或1
			
			String uuid = UUID.randomUUID().toString().replace("-", "");
			
			String sql = " INSERT INTO `LDWJ_CHART`( CHARTTYPE, NUM, CREATETIME) " +
					"VALUES ("+chartype+", "+rom+", now()); ";
			//System.out.println(sql);
			try
			{
				chartDao.insertBatch(sql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	@Override
	public void insertBatchNull(int chartype) {
		// TODO Auto-generated method stub
		Random a = new Random();
		//图形类型 1为ARP 2为ICMP 3为TCP 4为离散数据 13为端口0流量 14为端口1流量
		for(int i=1;i<5;i++)
		{
			int rom = a.nextInt(100);
			int month = a.nextInt(9);
			int date = a.nextInt(29);
			//随机生成 1或2
			//int chartype = a.nextInt(2);
			//随机生成 13或1
			
			String uuid = UUID.randomUUID().toString().replace("-", "");
			
			String sql = " INSERT INTO `LDWJ_CHART`( CHARTTYPE, CREATETIME) " +
					"VALUES ("+chartype+", now()); ";
			//System.out.println(sql);
			try
			{
				chartDao.insertBatch(sql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	public void delCharts(String data){
		chartDao.delCharts(data);
	}

	/* (non Javadoc) 
	 * @Title: queryArpHistory
	 * @Description: TODO
	 * @param chk_m
	 * @return 
	 * @see com.wnt.web.testexecute.service.ChartService#queryArpHistory(java.lang.String[]) 
	 */
	@Override
	public Map<Integer,List<Object[]>> queryAllHistory(String now) {
		//拼sql
				StringBuffer typeParam=new StringBuffer(100);
				typeParam.append(" 1=1 ");

				typeParam.append(" OR CHARTTYPE=1");

				typeParam.append(" OR CHARTTYPE=2");

				typeParam.append(" OR CHARTTYPE=3");

				typeParam.append(" OR CHARTTYPE=4");

				typeParam.append(" OR CHARTTYPE=14 OR CHARTTYPE=13");

				typeParam.append(" OR CHARTTYPE=16 OR CHARTTYPE=15");

				return  chartDao.queryAllHistory(typeParam.toString(),now); 
	}

	
	 
	@Override
	public List<Object[]>  queryPartOfHistory(String start,String end,String tableName) {
		return  chartDao.queryPartOfHistory(start,end,tableName); 
	}
	
	@Override
	public Map<Integer,List<Object[]>> queryHistory(String start,String end){
		Map<Integer,List<Object[]>> result = new HashMap<Integer, List<Object[]>>();
		Map<String,Long> mmmap = getMaxAndMinTime(start,end);//区域时间内的最大时间值和最小时间值
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = DataUtils.str2Date(start,format);
		Date endTime = DataUtils.str2Date(end,format);
		
		Long maxTime = mmmap.get("max"),	//数据库的最大时间
			minTime = mmmap.get("min");		//数据库的最小时间
		for(String tname : tableNames){
			result.put(getMonitorVal(tname), fillupHitoryData(chartDao.queryPartOfHistory(start,end,tname),startTime,endTime,maxTime,minTime));
			 
		}
		queryMaxAndMinTime(start,end);
		return result;
	}
	
	/**
	 * 根据表面获取对应的值
	 * @param tableName
	 * @return
	 */
	public Integer getMonitorVal(String tableName){
		if("LDWJ_CHART_ARP".equals(tableName)){
			return 1;
		}else if("LDWJ_CHART_ICMP".equals(tableName)){
			return 2;
		}else if("LDWJ_CHART_TCP".equals(tableName)){
			return 3;
		}else if("LDWJ_CHART_DISCRETE".equals(tableName)){
			return 4;
		}else if("LDWJ_CHART_ETH0_2".equals(tableName)){
			return 13;
		}else if("LDWJ_CHART_ETH0_1".equals(tableName)){
			return 14;
		}else if("LDWJ_CHART_ETH1_1".equals(tableName)){
			return 15;
		}else if("LDWJ_CHART_ETH1_2".equals(tableName)){
			return 16;
		}
		return null;
	}
	
	/**
	 * 获取最大最小时间值
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<String,Long> getMaxAndMinTime(String start,String end){
		Long maxTime = null,	//最大时间
		minTime = null;		//最小时间
		if(start!=null && end!=null){
			Map<String,Long> areaMap = queryMaxAndMinTime(start,end);//区域时间内的最大时间值和最小时间值
			maxTime = areaMap.get("max");
			minTime = areaMap.get("min");
		}
		if(maxTime==null || minTime == null){
			Map<String,Long> mmmap = queryMaxAndMinTime();//数据库的最大最小值
			if(maxTime==null ){
				maxTime = mmmap.get("max");
			}
			if(minTime == null){
				minTime = mmmap.get("min");
			}
		}
		Map<String,Long> r = new HashMap<String, Long>();
		r.put("max",maxTime);
		r.put("min",minTime);
		return r;
	}
	
	/**
	 * 
	 * @param list		数据库查询的数据
	 * @param startTime 查询开始时间
	 * @param endTime	查询结束时间
	 * @param maxTime	最大时间
	 * @param minTime	最小时间
	 * @return
	 */
	private List<Object[]> fillupHitoryData(List<Object[]> list,Date startTime,Date endTime,Long maxTime,Long minTime){
		if(maxTime == null || minTime == null){
			return null;
		}
		LinkedList<Object[]> newList = new LinkedList<Object[]>();
		//时间差值
		Long shortOf = 0l;
		//上一条记录的时间值
		Long oldTime = null;
		if(list != null && list.size()>0){
			Long selfMin = null,selfMax = null;
			
			//获取第一条数据
			Object[] data = list.get(0);
			oldTime = selfMin = (Long)data[0];//取第一条数据的时间值是当前数据的最小时间值
			//比较如果大于最小时间值
			if(selfMin > minTime){
				shortOf = (long)((selfMin - minTime)/1000);
				oldTime = minTime;
				for(int i = 0;i<shortOf;i++){
					newList.add(new Object[]{oldTime,null});
					oldTime = oldTime + 1000;
				}
			}
			
			//循环实际的数据
			int length = list.size();
			int lastIndex = length -1;
			for(int i = 0;i<length;i++){
				data = list.get(i);
				Long t = (Long)data[0];
				if(i == lastIndex){
					selfMax = t;
				}
				shortOf = (long)((t-oldTime-1000)/1000);
				if(shortOf > 0){
					for(long j =0;j<shortOf;j++){
						oldTime = oldTime + 1000;
						newList.add(new Object[]{oldTime,null});
					}
					newList.add(data);
				}else{
					oldTime = t;
					newList.add(data);
				}	
			}
			
			//判断最大值
			if(selfMax < maxTime){
				shortOf = (long)((maxTime - selfMax)/1000);
				oldTime = selfMax;
				for(int i = 0;i<shortOf;i++){
					oldTime = oldTime + 1000;
					newList.add(new Object[]{oldTime,null});
				}
			}
		}else{
			Long stime = startTime.getTime();
			Long etime = endTime.getTime();
			if(maxTime==null || minTime == null || etime <= minTime || stime >= maxTime){
				return newList;
			}
			if(stime < minTime){
				stime = minTime;
			}
			if(etime > maxTime){
				etime = maxTime;
			}
			shortOf = (etime - stime)/1000;
			oldTime = stime;
			for(int i=0;i<shortOf;i++){ 
				newList.add(new Object[]{oldTime,null});
				oldTime = oldTime + 1000;
			}
		}
		return newList;
	}
	
	public Map<String,Long> queryMaxAndMinTime(String start,String end){
		Long minTime = null,maxTime = null;
		for(String tname : tableNames){
			Map<String,Long> map = chartDao.queryMaxAndMinTime(tname,start,end); 
			if(!map.isEmpty()){
				if(map.get("max")!=null){
					if(maxTime == null || maxTime < map.get("max")){
						maxTime = map.get("max");
					}
				}
				if( map.get("min")!=null){
					if(minTime == null || minTime > map.get("min")){
						minTime = map.get("min");
					}
				}
			}
		}
		Map<String,Long> result = new HashMap<String, Long>(); 
		result.put("max", maxTime);
		result.put("min", minTime);
		return  result; 
	}
	
	public Map<String,Long> queryMaxAndMinTime(){
		return queryMaxAndMinTime(null,null); 
	}

	/* (non Javadoc) 
	 * @Title: delChartArp
	 * @Description: TODO
	 * @param dateadd 
	 * @see com.wnt.web.testexecute.service.ChartService#delChartArp(java.lang.String) 
	 */
	@Override
	public void delChartArp(String data) {
		chartDao.delChartArp(data);
		
	}

	/* (non Javadoc) 
	 * @Title: delChartIcmp
	 * @Description: TODO
	 * @param dateadd 
	 * @see com.wnt.web.testexecute.service.ChartService#delChartIcmp(java.lang.String) 
	 */
	@Override
	public void delChartIcmp(String data) {
		
		chartDao.delChartIcmp(data);
		
	}

	/* (non Javadoc) 
	 * @Title: delChartTcp
	 * @Description: TODO
	 * @param dateadd 
	 * @see com.wnt.web.testexecute.service.ChartService#delChartTcp(java.lang.String) 
	 */
	@Override
	public void delChartTcp(String data) {
		
		chartDao.delChartTcp(data);
		
	}

	/* (non Javadoc) 
	 * @Title: delChartDiscrete
	 * @Description: TODO
	 * @param dateadd 
	 * @see com.wnt.web.testexecute.service.ChartService#delChartDiscrete(java.lang.String) 
	 */
	@Override
	public void delChartDiscrete(String data) {
		chartDao.delChartDiscrete(data);
		
	}

	/* (non Javadoc) 
	 * @Title: delChartEth0_2
	 * @Description: TODO
	 * @param dateadd 
	 * @see com.wnt.web.testexecute.service.ChartService#delChartEth0_2(java.lang.String) 
	 */
	@Override
	public void delChartEth0_2(String data) {
		chartDao.delChartEth0_2(data);
		
	}

	/* (non Javadoc) 
	 * @Title: delChartEth0_1
	 * @Description: TODO
	 * @param dateadd 
	 * @see com.wnt.web.testexecute.service.ChartService#delChartEth0_1(java.lang.String) 
	 */
	@Override
	public void delChartEth0_1(String data) {
		
		chartDao.delChartEth0_1(data);
		
	}

	/* (non Javadoc) 
	 * @Title: delChartEth1_2
	 * @Description: TODO
	 * @param dateadd 
	 * @see com.wnt.web.testexecute.service.ChartService#delChartEth1_2(java.lang.String) 
	 */
	@Override
	public void delChartEth1_2(String data) {
		chartDao.delChartEth1_2(data);
	}

	/* (non Javadoc) 
	 * @Title: delChartEth1_1
	 * @Description: TODO
	 * @param dateadd 
	 * @see com.wnt.web.testexecute.service.ChartService#delChartEth1_1(java.lang.String) 
	 */
	@Override
	public void delChartEth1_1(String data) {
		
		chartDao.delChartEth1_1(data);
		
	}

}
