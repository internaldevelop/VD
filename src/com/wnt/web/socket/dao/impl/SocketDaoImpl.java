package com.wnt.web.socket.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wnt.server.entry.ChartEntry;
import com.wnt.server.entry.LogEntry;
import com.wnt.server.entry.PortEntry;
import com.wnt.web.protocol.entry.Protocol;
import com.wnt.web.socket.dao.SocketDao;
import com.wnt.web.testsetup.entry.LDWJ_TESTDEPLAYLIVE;

import common.PortScanDefs;

@Repository
public class SocketDaoImpl implements SocketDao {
	
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public void updateMac(int equipmentId, String terraceMac,
			String equipmentMac) {
		if("0".equals(terraceMac)){
			String sql = "UPDATE LDWJ_EQUIPMENT SET MAC3=? WHERE EQUIPMENTID=?";

			jdbcTemplate.update(sql, new Object[] {equipmentMac,equipmentId });
		}else{
			String sql = "UPDATE LDWJ_EQUIPMENT SET MAC=?,MAC2=? WHERE EQUIPMENTID=?";

			jdbcTemplate.update(sql, new Object[] {terraceMac,equipmentMac,equipmentId });
		}
		
	}

	@Override
	public void addChart(final List<ChartEntry> listChart) {
		try{
		String sql = "INSERT INTO LDWJ_CHART (CHARTTYPE,NUM,CREATETIME) VALUES(?,?,?)";
		jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	ChartEntry chartEntry = listChart.get(i);
			        	ps.setInt(1, chartEntry.getMonitorid());
			            ps.setObject(2, chartEntry.getNum());
			            ps.setObject(3, chartEntry.getCreatetime());
			          }

			          public int getBatchSize() {
//			        	  System.out.println(listChart.size());
			            return listChart.size();
			          }
			        });
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void addPort(final List<PortEntry> listPort) {
		String sql = "INSERT INTO LDWJ_PORTSERVER (ID,PORTTYPE,PORTNUM,NAME,SCANTYPE,SOURCE,CREATETIME,NUM_ORDER) VALUES(?,?,?,?,?,1,?,?)";
		jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	PortEntry portEntry = listPort.get(i);
			        	ps.setString(1, portEntry.getId());
			        	ps.setInt(2, portEntry.getPorttype());
			            ps.setInt(3, portEntry.getPortnum());
			            ps.setString(4, portEntry.getName());
			            ps.setInt(5, portEntry.getScantype());
			            ps.setTimestamp(6, new java.sql.Timestamp(new java.util.Date().getTime()));
			            PortScanDefs.NUM_ORDER++;
			            ps.setInt(7, PortScanDefs.NUM_ORDER);
			            
			        }

			          public int getBatchSize() {
			            return listPort.size();
			          }
			        });
	}

	@Override
	public void addLog(final List<LogEntry> listLog) {
		try{
			String sql = "INSERT INTO LDWJ_INCIDENTLOG (ID,SOURCETYPE,MESSAGETYPE,CODE,SOURCE,MESSAGE,PARENT,TESTRESULTID,NUM,NUM2,CREATETIME) VALUES(?,?,?,?,?,?,?,?,?,?,NOW())";
			jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	LogEntry logEntry = listLog.get(i);
			        	ps.setString(1,logEntry.getId());
			        	ps.setInt(2, logEntry.getSourcetype());
			            ps.setInt(3, logEntry.getMessagetype());
			            ps.setString(4, logEntry.getCode());
			            ps.setString(5, logEntry.getSource());
			            ps.setString(6, logEntry.getMessage());
			            //父节点id
			            ps.setString(7, logEntry.getParent());
			            //测试用例id
			            ps.setString(8, logEntry.getTestResultId());
			            
			            ps.setInt(9, logEntry.getNum());
			            ps.setObject(10, logEntry.getNum2());
			          }

			          public int getBatchSize() {
			            return listLog.size();
			          }
			 });
		}catch(Exception e){
			
		}
	}

	/* (non-Javadoc)
	 * @see com.wnt.web.socket.dao.SocketDao#findPort(com.wnt.server.entry.PortEntry)
	 */
	@Override
	public List<Map<String,Object>> findPort(PortEntry portEntry) {
		String sql = "select * from LDWJ_PORTSERVER where PORTNUM=? and PORTTYPE=?";
		return jdbcTemplate.queryForList(sql,new Object[]{portEntry.getPortnum(), portEntry.getPorttype()});
	}

	/* (non-Javadoc)
	 * @see com.wnt.web.socket.dao.SocketDao#updatePort(com.wnt.server.entry.PortEntry)
	 */
	@Override
	public void updatePort(PortEntry portEntry) {
		String sql = "update LDWJ_PORTSERVER set SOURCE=1 where PORTNUM=? and PORTTYPE=?";
		jdbcTemplate.update(sql,new Object[]{portEntry.getPortnum(), portEntry.getPorttype()});
	}

	/* (non-Javadoc)
	 * @see com.wnt.web.socket.dao.SocketDao#deletePort(com.wnt.server.entry.PortEntry)
	 */
	@Override
	public void deletePort(PortEntry portEntry) {
		String sql = "delete from  LDWJ_PORTSERVER  where PORTNUM=? and PORTTYPE=?";
		jdbcTemplate.update(sql,new Object[]{portEntry.getPortnum(), portEntry.getPorttype()});
	}

	/* (non Javadoc) 
	 * @Title: addSysInfoLog
	 * @Description: TODO
	 * @param listLog 
	 * @see com.wnt.web.socket.dao.SocketDao#addSysInfoLog(java.util.List) 
	 */
	@Override
	public void addSysInfoLog(final List<LogEntry> listLog) {
		try{
			String sql = "INSERT INTO LDWJ_INCIDENTLOG (ID,SOURCETYPE,MESSAGETYPE,CODE,SOURCE,MESSAGE,PARENT,TESTRESULTID,NUM,NUM2,CREATETIME) VALUES(?,?,?,?,?,?,?,?,?,?,NOW())";
			jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	LogEntry logEntry = listLog.get(i);
			        	ps.setString(1,logEntry.getId());
			        	ps.setInt(2, logEntry.getSourcetype());
			            ps.setInt(3, logEntry.getMessagetype());
			            ps.setString(4, null);
			            ps.setString(5, logEntry.getSource());
			            ps.setString(6, logEntry.getMessage());
			            ps.setString(7, null);
			            ps.setString(8, null);
			            ps.setInt(9, 0);
			            ps.setObject(10, null);
			          }

			          public int getBatchSize() {
			            return listLog.size();
			          }
			 });
		}catch(Exception e){
			
		}
		
	}

	/* (non Javadoc) 
	 * @Title: updateTestNum
	 * @Description: TODO
	 * @param list 
	 * @see com.wnt.web.socket.dao.SocketDao#updateTestNum(java.util.List) 
	 */
	@Override
	public void updateTestNum(List<LDWJ_TESTDEPLAYLIVE> list) {
		LDWJ_TESTDEPLAYLIVE live =list.get(0);
		//System.out.println(live);
	    String sql ="UPDATE LDWJ_TESTDEPLAYLIVE SET TESTNUM =? WHERE CODE=?";
	    jdbcTemplate.update(sql,live.getTestnum(),live.getCode());
	}

	/* (non Javadoc) 
	 * @Title: addChartArp
	 * @Description: TODO
	 * @param listArp 
	 * @see com.wnt.web.socket.dao.SocketDao#addChartArp(java.util.List) 
	 */
	@Override
	public void addChartArp(final List<ChartEntry> listArp) {
		
		try{
		String sql = "INSERT INTO LDWJ_CHART_ARP (CHARTTYPE,NUM,CREATETIME) VALUES(?,?,?)";
		jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	ChartEntry chartEntry = listArp.get(i);
			        	ps.setInt(1, chartEntry.getMonitorid());
			            ps.setObject(2, chartEntry.getNum1());
			            ps.setObject(3, chartEntry.getCreatetime());
			          }

			          public int getBatchSize() {
//			        	  System.out.println(listChart.size());
			            return listArp.size();
			          }
			        });
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/* (non Javadoc) 
	 * @Title: addChartIcmp
	 * @Description: TODO
	 * @param listIcmp 
	 * @see com.wnt.web.socket.dao.SocketDao#addChartIcmp(java.util.List) 
	 */
	@Override
	public void addChartIcmp(final List<ChartEntry> listIcmp) {
		
		try{
		String sql = "INSERT INTO LDWJ_CHART_ICMP (CHARTTYPE,NUM,CREATETIME) VALUES(?,?,?)";
		jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	ChartEntry chartEntry = listIcmp.get(i);
			        	ps.setInt(1, chartEntry.getMonitorid());
			            ps.setObject(2, chartEntry.getNum1());
			            ps.setObject(3, chartEntry.getCreatetime());
			          }

			          public int getBatchSize() {
//			        	  System.out.println(listChart.size());
			            return listIcmp.size();
			          }
			        });
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/* (non Javadoc) 
	 * @Title: addChartTcp
	 * @Description: TODO
	 * @param listTcp 
	 * @see com.wnt.web.socket.dao.SocketDao#addChartTcp(java.util.List) 
	 */
	@Override
	public void addChartTcp(final List<ChartEntry> listTcp) {
		
		try{
		String sql = "INSERT INTO LDWJ_CHART_TCP (CHARTTYPE,NUM,CREATETIME) VALUES(?,?,?)";
		jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	ChartEntry chartEntry = listTcp.get(i);
			        	ps.setInt(1, chartEntry.getMonitorid());
			            ps.setObject(2, chartEntry.getNum());
			            ps.setObject(3, chartEntry.getCreatetime());
			          }

			          public int getBatchSize() {
//			        	  System.out.println(listChart.size());
			            return listTcp.size();
			          }
			        });
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/* (non Javadoc) 
	 * @Title: addChartDiscrete
	 * @Description: TODO
	 * @param listDiscrete 
	 * @see com.wnt.web.socket.dao.SocketDao#addChartDiscrete(java.util.List) 
	 */
	@Override
	public void addChartDiscrete(final List<ChartEntry> listDiscrete) {
		
		try{
		String sql = "INSERT INTO LDWJ_CHART_DISCRETE (CHARTTYPE,NUM,CREATETIME) VALUES(?,?,?)";
		jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	ChartEntry chartEntry = listDiscrete.get(i);
			        	ps.setInt(1, chartEntry.getMonitorid());
			            ps.setObject(2, chartEntry.getNum());
			            ps.setObject(3, chartEntry.getCreatetime());
			          }

			          public int getBatchSize() {
//			        	  System.out.println(listChart.size());
			            return listDiscrete.size();
			          }
			        });
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/* (non Javadoc) 
	 * @Title: addChartEth0_2
	 * @Description: TODO
	 * @param listEth0_2 
	 * @see com.wnt.web.socket.dao.SocketDao#addChartEth0_2(java.util.List) 
	 */
	@Override
	public void addChartEth0_2(final List<ChartEntry> listEth0_2) {
		
		try{
		String sql = "INSERT INTO LDWJ_CHART_ETH0_2 (CHARTTYPE,NUM,CREATETIME) VALUES(?,?,?)";
		jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	ChartEntry chartEntry = listEth0_2.get(i);
			        	ps.setInt(1, chartEntry.getMonitorid());
			            ps.setObject(2, chartEntry.getNum());
			            ps.setObject(3, chartEntry.getCreatetime());
			          }

			          public int getBatchSize() {
//			        	  System.out.println(listChart.size());
			            return listEth0_2.size();
			          }
			        });
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/* (non Javadoc) 
	 * @Title: addChartEth0_1
	 * @Description: TODO
	 * @param listEth0_1 
	 * @see com.wnt.web.socket.dao.SocketDao#addChartEth0_1(java.util.List) 
	 */
	@Override
	public void addChartEth0_1(final List<ChartEntry> listEth0_1) {
		
		try{
		String sql = "INSERT INTO LDWJ_CHART_ETH0_1 (CHARTTYPE,NUM,CREATETIME) VALUES(?,?,?)";
		jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	ChartEntry chartEntry = listEth0_1.get(i);
			        	ps.setInt(1, chartEntry.getMonitorid());
			            ps.setObject(2, chartEntry.getNum());
			            ps.setObject(3, chartEntry.getCreatetime());
			          }

			          public int getBatchSize() {
//			        	  System.out.println(listChart.size());
			            return listEth0_1.size();
			          }
			        });
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/* (non Javadoc) 
	 * @Title: addChartEth1_2
	 * @Description: TODO
	 * @param listEth1_2 
	 * @see com.wnt.web.socket.dao.SocketDao#addChartEth1_2(java.util.List) 
	 */
	@Override
	public void addChartEth1_2(final List<ChartEntry> listEth1_2) {
		
		try{
		String sql = "INSERT INTO LDWJ_CHART_ETH1_2 (CHARTTYPE,NUM,CREATETIME) VALUES(?,?,?)";
		jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	ChartEntry chartEntry = listEth1_2.get(i);
			        	ps.setInt(1, chartEntry.getMonitorid());
			            ps.setObject(2, chartEntry.getNum());
			            ps.setObject(3, chartEntry.getCreatetime());
			          }

			          public int getBatchSize() {
//			        	  System.out.println(listChart.size());
			            return listEth1_2.size();
			          }
			        });
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/* (non Javadoc) 
	 * @Title: addChartEth1_1
	 * @Description: TODO
	 * @param listEth1_1 
	 * @see com.wnt.web.socket.dao.SocketDao#addChartEth1_1(java.util.List) 
	 */
	@Override
	public void addChartEth1_1(final List<ChartEntry> listEth1_1) {
		
		try{
		String sql = "INSERT INTO LDWJ_CHART_ETH1_1 (CHARTTYPE,NUM,CREATETIME) VALUES(?,?,?)";
		jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	ChartEntry chartEntry = listEth1_1.get(i);
			        	ps.setInt(1, chartEntry.getMonitorid());
			            ps.setObject(2, chartEntry.getNum());
			            ps.setObject(3, chartEntry.getCreatetime());
			          }

			          public int getBatchSize() {
//			        	  System.out.println(listChart.size());
			            return listEth1_1.size();
			          }
			        });
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
