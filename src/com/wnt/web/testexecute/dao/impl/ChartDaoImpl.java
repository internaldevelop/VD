package com.wnt.web.testexecute.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.wnt.web.testexecute.dao.ChartDao;
import com.wnt.web.testexecute.entry.ChartHistoryDataDO;
@Repository("ChartDao")
public class ChartDaoImpl implements ChartDao{
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public List findCharByType(int type,long time) {
		// TODO Auto-generated method stub
		String sql = " SELECT LC.CREATETIME, LC.NUM FROM LDWJ_CHART LC WHERE LC.CHARTTYPE=? and CREATETIME>? ORDER BY LC.CREATETIME";
		return jdbcTemplate.queryForList(sql,type,new Date(time));
//		String sql = " SELECT LC.CREATETIME, LC.NUM FROM LDWJ_CHART LC WHERE LC.CHARTTYPE=? ORDER BY LC.CREATETIME desc limit 0,10";
//		return jdbcTemplate.queryForList(sql,type);
	}
	@Override
	public List findCharByType(String typeParme, String lastId) {
		String sql = " SELECT LC.CREATETIME as createTime, LC.ID as id,LC.NUM as num,LC.CHARTTYPE as chartType FROM LDWJ_CHART LC WHERE "+typeParme+" and LC.ID>"+lastId+" ORDER BY LC.ID asc";
		//System.out.println(new Date()+","+sql+":"+new Date(time));
		return jdbcTemplate.queryForList(sql);
	}
	@Override
	public List findLastCharByType(String typeParme) {
		String sql = " SELECT LC.CREATETIME as createTime, LC.ID as id,LC.NUM as num,LC.CHARTTYPE as chartType FROM LDWJ_CHART LC WHERE "+typeParme+" ORDER BY LC.ID DESC limit 0,1";
		return jdbcTemplate.queryForList(sql);
	}
	@Override
	public void insertBatch(String sql) {
		jdbcTemplate.update(sql);
	}
	
	public Map<String,Object> getMaxid(){
		return jdbcTemplate.queryForMap("SELECT MAX(ID) as MID from LDWJ_CHART");
	}
	
	public void delCharts(String data){
		String sql = "DELETE FROM LDWJ_CHART WHERE CREATETIME <='"+data+"'";
		jdbcTemplate.update(sql);
	}

	@Override
	public Map<Integer,List<Object[]>> queryAllHistory(String typeParme,String now) {
		
		String sql = " SELECT CREATETIME, NUM, CHARTTYPE FROM LDWJ_CHART WHERE ? AND CREATETIME > ? ORDER BY CREATETIME asc";
		// 参数集合
		List<Object> paramsList = new ArrayList<Object>();
		paramsList.add(typeParme);
		paramsList.add(now);
		
		// resultMap
		final Map<Integer,List<Object[]>> tempMap = new HashMap<Integer, List<Object[]>>();
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Timestamp timestamp = rs.getTimestamp("CREATETIME");
				Long timeLong = 0L;
				if(null != timestamp){
					timeLong = timestamp.getTime();
				}
				int num = rs.getInt("NUM");
				int type = rs.getInt("CHARTTYPE");
				Object[] dataArray = {timeLong,num};
				// type数据整理
				if(tempMap.containsKey(type)){
					List<Object[]> dataList = tempMap.get(type);
					dataList.add(dataArray);
					tempMap.put(type, dataList);
				}else{
					List<Object[]> dataList = new ArrayList<Object[]>();
					dataList.add(dataArray);
					tempMap.put(type, dataList);
				}
				
			}
		},paramsList.toArray());
		
		return tempMap;
		
	}
	
	@Override
	public Map<String, Long> queryMaxAndMinTime(String tableName){
		return queryMaxAndMinTime(tableName,null,null);
	}
	
	@Override
	public Map<String, Long> queryMaxAndMinTime(String tableName,String start,String end) {
		String sql = "SELECT MAX(CREATETIME) max,MIN(CREATETIME) min FROM "+tableName+"  WHERE 1=1 ";
		// 参数集合
		List<Object> paramsList = new ArrayList<Object>();
		if(start != null ){
			sql += " AND  CREATETIME > ? ";
			paramsList.add(start);
		}
		if(end != null){
			sql += " AND  CREATETIME < ? ";
			paramsList.add(end);
		}
		final Map<String,Long> result = new HashMap<String, Long>();
		jdbcTemplate.query(sql,new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Timestamp max = rs.getTimestamp("max");
				Timestamp min = rs.getTimestamp("min");
				result.put("max",max!=null?max.getTime():null);
				result.put("min",min!=null?min.getTime():null);
			}
		},paramsList.toArray());
		return result;
	}

	@Override
	public List<Object[]> queryPartOfHistory(String start,String end,final String tableName) {
		
		String sql = " SELECT CREATETIME, NUM, CHARTTYPE FROM "+tableName+" WHERE CREATETIME > ? AND  CREATETIME < ? ORDER BY CREATETIME asc";
		// 参数集合
		List<Object> paramsList = new ArrayList<Object>();
		//paramsList.add(now);
		paramsList.add(start);
		paramsList.add(end);
		//paramsList.add(typeParme);
		
		// resultMap
		//final Map<Integer,List<Object[]>> tempMap = new HashMap<Integer, List<Object[]>>();
		final List<Object[]> dataList =new ArrayList<Object[]>();
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Timestamp timestamp = rs.getTimestamp("CREATETIME");
				Long timeLong = 0L;
				if(null != timestamp){
					timeLong = timestamp.getTime();
				}
				Object[] dataArray = new Object[2];
				dataArray[0] = timeLong;
				/*if("LDWJ_CHART_ARP".equals(tableName)||"LDWJ_CHART_ICMP".equals(tableName)){
					dataArray[1] = rs.getFloat("NUM");
				}else{
					dataArray[1] = rs.getInt("NUM");
				}*/
				dataArray[1] = rs.getObject("NUM");
				int type = rs.getInt("CHARTTYPE");
				
				// type数据整理
//				if(tempMap.containsKey(type)){
//					List<Object[]> dataList = tempMap.get(type);
//					dataList.add(dataArray);
//					tempMap.put(type, dataList);
//				}else{
					dataList.add(dataArray);
					//tempMap.put(type, dataList);
				}
				
//			}
		},paramsList.toArray());
		
		return dataList;
		
	}
	/* (non Javadoc) 
	 * @Title: delChartArp
	 * @Description: TODO
	 * @param data 
	 * @see com.wnt.web.testexecute.dao.ChartDao#delChartArp(java.lang.String) 
	 */
	@Override
	public void delChartArp(String data) {
		
		String sql = "DELETE FROM LDWJ_CHART_ARP WHERE CREATETIME <='"+data+"'";
		jdbcTemplate.update(sql);
		
	}
	/* (non Javadoc) 
	 * @Title: delChartIcmp
	 * @Description: TODO
	 * @param data 
	 * @see com.wnt.web.testexecute.dao.ChartDao#delChartIcmp(java.lang.String) 
	 */
	@Override
	public void delChartIcmp(String data) {
		String sql = "DELETE FROM LDWJ_CHART_ICMP WHERE CREATETIME <='"+data+"'";
		jdbcTemplate.update(sql);
		
	}
	/* (non Javadoc) 
	 * @Title: delChartTcp
	 * @Description: TODO
	 * @param data 
	 * @see com.wnt.web.testexecute.dao.ChartDao#delChartTcp(java.lang.String) 
	 */
	@Override
	public void delChartTcp(String data) {
		String sql = "DELETE FROM LDWJ_CHART_TCP WHERE CREATETIME <='"+data+"'";
		jdbcTemplate.update(sql);
		
	}
	/* (non Javadoc) 
	 * @Title: delChartDiscrete
	 * @Description: TODO
	 * @param data 
	 * @see com.wnt.web.testexecute.dao.ChartDao#delChartDiscrete(java.lang.String) 
	 */
	@Override
	public void delChartDiscrete(String data) {
		String sql = "DELETE FROM LDWJ_CHART_DISCRETE WHERE CREATETIME <='"+data+"'";
		jdbcTemplate.update(sql);
		
	}
	/* (non Javadoc) 
	 * @Title: delChartEth0_2
	 * @Description: TODO
	 * @param data 
	 * @see com.wnt.web.testexecute.dao.ChartDao#delChartEth0_2(java.lang.String) 
	 */
	@Override
	public void delChartEth0_2(String data) {
		String sql = "DELETE FROM LDWJ_CHART_ETH0_2 WHERE CREATETIME <='"+data+"'";
		jdbcTemplate.update(sql);
	}
	/* (non Javadoc) 
	 * @Title: delChartEth0_1
	 * @Description: TODO
	 * @param data 
	 * @see com.wnt.web.testexecute.dao.ChartDao#delChartEth0_1(java.lang.String) 
	 */
	@Override
	public void delChartEth0_1(String data) {
		
		String sql = "DELETE FROM LDWJ_CHART_ETH0_1 WHERE CREATETIME <='"+data+"'";
		jdbcTemplate.update(sql);
		
	}
	/* (non Javadoc) 
	 * @Title: delChartEth1_2
	 * @Description: TODO
	 * @param data 
	 * @see com.wnt.web.testexecute.dao.ChartDao#delChartEth1_2(java.lang.String) 
	 */
	@Override
	public void delChartEth1_2(String data) {
		
		String sql = "DELETE FROM LDWJ_CHART_ETH1_2 WHERE CREATETIME <='"+data+"'";
		jdbcTemplate.update(sql);
		
	}
	/* (non Javadoc) 
	 * @Title: delChartEth1_1
	 * @Description: TODO
	 * @param data 
	 * @see com.wnt.web.testexecute.dao.ChartDao#delChartEth1_1(java.lang.String) 
	 */
	@Override
	public void delChartEth1_1(String data) {
		String sql = "DELETE FROM LDWJ_CHART_ETH1_1 WHERE CREATETIME <='"+data+"'";
		jdbcTemplate.update(sql);
		
	}
	
}
