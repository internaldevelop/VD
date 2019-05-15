package com.wnt.web.testexecute.dao;

import java.util.List;
import java.util.Map;

public interface ChartDao {
	/**
	 * 查询图形
	 * @return
	 */
	public List findCharByType(int type,long time);
	
	/**
	 * 查询图形
	 * @return
	 */
	public List findCharByType(String typeParme,String lastId); 
	
	public List findLastCharByType(String typeParme);
	
	public void insertBatch(String sql);
	
	public Map<String,Object> getMaxid();
	
	public void delCharts(String data);

	/** 
	 * @Title: queryAllHistory 
	 * @Description: 
	 * @param string
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	public  Map<Integer,List<Object[]>> queryAllHistory(String string,String now);
	/** 
	 * @Title: queryPartOfHistory 
	 * @Description: 
	 * @param string
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Object[]>  queryPartOfHistory(String start,String end,String tableName);

	/** 
	 * @Title: delChartArp 
	 * @Description: TODO
	 * @param data
	 * @return: void
	 */
	public void delChartArp(String data);

	/** 
	 * @Title: delChartIcmp 
	 * @Description: TODO
	 * @param data
	 * @return: void
	 */
	public void delChartIcmp(String data);

	/** 
	 * @Title: delChartTcp 
	 * @Description: TODO
	 * @param data
	 * @return: void
	 */
	public void delChartTcp(String data);

	/** 
	 * @Title: delChartDiscrete 
	 * @Description: TODO
	 * @param data
	 * @return: void
	 */
	public void delChartDiscrete(String data);

	/** 
	 * @Title: delChartEth0_2 
	 * @Description: TODO
	 * @param data
	 * @return: void
	 */
	public void delChartEth0_2(String data);

	/** 
	 * @Title: delChartEth0_1 
	 * @Description: TODO
	 * @param data
	 * @return: void
	 */
	public void delChartEth0_1(String data);

	/** 
	 * @Title: delChartEth1_2 
	 * @Description: TODO
	 * @param data
	 * @return: void
	 */
	public void delChartEth1_2(String data);

	/** 
	 * @Title: delChartEth1_1 
	 * @Description: TODO
	 * @param data
	 * @return: void
	 */
	public void delChartEth1_1(String data);

	public Map<String, Long> queryMaxAndMinTime(String tableName);
	
	public Map<String, Long> queryMaxAndMinTime(String tableName,String start,String end);

}
