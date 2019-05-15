package com.wnt.web.testexecute.service;

import java.util.List;
import java.util.Map;

public interface ChartService {
	/**
	 * 查询ARP散点图
	 * @return
	 */
	public List<Object[]> queryArp(int type,long time);
	/**
	 * 查询ARP散点图
	 * @return
	 */
	public List<Map<String, Object>> queryArp(String[] type,String lastId);
	
	/**
	 * 查询TCP折线图
	 * @return
	 */
	public Map<String, Object> queryTcp(int type,long time);
	
	/**
	 * 查询监视器的最大时间值和最小时间值
	 * @param tableName
	 * @return
	 */
	public Map<String, Long> queryMaxAndMinTime();
	
	/**
	 *  查询监视器的最大时间值和最小时间值
	 * @param start 开始时间
	 * @param end	结束时间
	 * @return
	 */
	public Map<String,Long> queryMaxAndMinTime(String start,String end);
	
	public void insertBatch(int chartype);
	public void insertBatchNull(int chartype);
	
	public String getMaxid();
	public void delCharts(String data);
	/** 
	 * @Title: queryArpHistory 
	 * @Description: TODO
	 * @param chk_m
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	public Map<Integer,List<Object[]>> queryAllHistory(String now);  
	/** 
	 * @Title: queryPartOfHistory 
	 * @Description: TODO
	 * @param chk_m
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Object[]> queryPartOfHistory(String start,String end,String tableName);
	
	/**
	 * 查询历史数据
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<Integer,List<Object[]>> queryHistory(String start,String end);
	
	/** 
	 * @Title: delChartArp 
	 * @Description: TODO
	 * @param dateadd
	 * @return: void
	 */
	public void delChartArp(String dateadd);
	/** 
	 * @Title: delChartIcmp 
	 * @Description: TODO
	 * @param dateadd
	 * @return: void
	 */
	public void delChartIcmp(String dateadd);
	/** 
	 * @Title: delChartTcp 
	 * @Description: TODO
	 * @param dateadd
	 * @return: void
	 */
	public void delChartTcp(String dateadd);
	/** 
	 * @Title: delChartDiscrete 
	 * @Description: TODO
	 * @param dateadd
	 * @return: void
	 */
	public void delChartDiscrete(String dateadd);
	/** 
	 * @Title: delChartEth0_2 
	 * @Description: TODO
	 * @param dateadd
	 * @return: void
	 */
	public void delChartEth0_2(String dateadd);
	/** 
	 * @Title: delChartEth0_1 
	 * @Description: TODO
	 * @param dateadd
	 * @return: void
	 */
	public void delChartEth0_1(String dateadd);
	/** 
	 * @Title: delChartEth1_2 
	 * @Description: TODO
	 * @param dateadd
	 * @return: void
	 */
	public void delChartEth1_2(String dateadd);
	/** 
	 * @Title: delChartEth1_1 
	 * @Description: TODO
	 * @param dateadd
	 * @return: void
	 */
	public void delChartEth1_1(String dateadd);

}
