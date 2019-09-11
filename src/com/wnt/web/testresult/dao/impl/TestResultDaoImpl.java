package com.wnt.web.testresult.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.testresult.dao.TestResultDao;

@Component("testResultDao")
public class TestResultDaoImpl implements TestResultDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public void insertBatch(String sql) {
		// TODO Auto-generated method stub
		jdbcTemplate.update(sql);
	}

	@Override
	public List findCharByType(int type, String parentId) {
		// TODO Auto-generated method stub
		String sql = "SELECT LC.CREATETIME, LC.NUM FROM LDWJ_CHART_RESULT LC, LDWJ_TESTRESULT t WHERE LC.TESTRESULTID = t.ID AND LC.CHARTTYPE = ? AND LC.TESTRESULTID = ? ORDER BY LC.CREATETIME ";
		return jdbcTemplate.queryForList(sql, type, parentId);
	}

	@Override
	public List findListBysql(String sql) {
		return jdbcTemplate.queryForList(sql);
	}

	// 设备
	@Override
	public List testDescribe() {
		String sql = "SELECT NAME,VERSION,REMARK,IP,MAC,IP2,MAC2 FROM LDWJ_EQUIPMENT";
		return jdbcTemplate.queryForList(sql);
	}

	// 端口
	@Override
	public List queryPortserverList(int porttype) {
		String sql = "SELECT PORTNUM ,NAME as NAME1,SOURCE FROM LDWJ_PORTSERVER WHERE PORTTYPE = "
				+ porttype;
		return jdbcTemplate.queryForList(sql);
	}

	// 监控
	@Override
	public List queryMonitor() {
		String sql = "SELECT LM.ID,LMD.CYCLEPERIOD, LMD.INPUT, LMD.ALARMLEVEL, LMD.TCPPORTS, LMD.OVERTIME FROM LDWJ_MONITOR LM, LDWJ_MONITORDETAIL LMD, LDWJ_EQUIPMENTANDMONITOR LEM WHERE LM.ID=LMD.MONITORID AND LM.ID=LEM.MONITORID AND LEM.SELECTSTATUS=1";
		return jdbcTemplate.queryForList(sql);
	}
/*	@Override
	public List<Map<String, Object>> findParent() {
		// TODO Auto-generated method stub
		// return
		// jdbcTemplate.queryForList(" select ID as id, 'true' as isParent, EQUIPMENTNAME as name from LDWJ_TESTRESULT where PARENTID IS NULL");
		// return
		// jdbcTemplate.queryForList(" select ID as id, PARENTID as pId, EQUIPMENTNAME as name from LDWJ_TESTRESULT");
		//
		return jdbcTemplate
				.queryForList("SELECT ts.ID AS id, ts.PARENTID AS pId, ts.EQUIPMENTNAME AS name, sum(MESSAGETYPE) c, ts.DELSTATUS AS del FROM LDWJ_TESTRESULT ts LEFT JOIN ( SELECT * FROM LDWJ_INCIDENTLOG WHERE MESSAGETYPE  >=4) log ON log.TESTRESULTID = ts.ID where flag != 2 GROUP BY ts.ID, ts.PARENTID, ts.EQUIPMENTNAME, ts.CREATETIME ORDER BY ts.CREATETIME DESC");
	}*/
	@Override
	public List<Map<String, Object>> findParent() {
		// TODO Auto-generated method stub
		// return
		// jdbcTemplate.queryForList(" select ID as id, 'true' as isParent, EQUIPMENTNAME as name from LDWJ_TESTRESULT where PARENTID IS NULL");
		// return
		// jdbcTemplate.queryForList(" select ID as id, PARENTID as pId, EQUIPMENTNAME as name from LDWJ_TESTRESULT");
		return jdbcTemplate
				.queryForList("SELECT ts.ID AS id, ts.PARENTID AS pId, ts.EQUIPMENTNAME AS name, ts.DELSTATUS AS del FROM LDWJ_TESTRESULT ts ORDER BY ts.CREATETIME DESC ");
	}

	public List<Map<String, Object>> findLastParent() {
		return jdbcTemplate
				.queryForList(" select ID as id, PARENTID as pId, EQUIPMENTNAME as name from LDWJ_TESTRESULT where PARENTID is null ORDER BY CREATETIME DESC limit 0,1 ");
	}

	@Override
	public void batchDeleteTemplate(final List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		String bach_sql = "DELETE FROM LDWJ_TESTRESULT WHERE ID = ? OR ID = ?";
		jdbcTemplate.batchUpdate(bach_sql, new BatchPreparedStatementSetter() {
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return list.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, (String) list.get(i).get("ID"));
				ps.setString(2, (String) list.get(i).get("ID"));
			}
		});
	}

	public Map getErrorCount(String id) {
		String sql = "SELECT COUNT(*) as COUNTERR FROM LDWJ_INCIDENTLOG WHERE MESSAGETYPE>=4 AND TESTRESULTID='"
				+ id + "'";
		return jdbcTemplate.queryForMap(sql);
	}

	public Map findDetailList(String id) {
		try {
			String sql = "SELECT * FROM LDWJ_RESULTDETAIL L WHERE L.RESULTID='"
					+ id + "'";
			List<Map<String, Object>> ltcl = jdbcTemplate.queryForList(sql);
			// Map ltm = jdbcTemplate.queryForMap(sql);
			if(ltcl.size()!=0){
			  return ltcl.get(0);
			}else{
			  return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Map<String, Object>> findDetailcustomList(String id) {
		String sql = "SELECT * FROM LDWJ_RESULTDETAIL_CUSTOM L WHERE NOT ISNULL(SORTS) and L.RESULTID='"
				+ id + "' ORDER BY TYPE";
		List<Map<String, Object>> ltcl = jdbcTemplate.queryForList(sql);
		return ltcl;
	}

	public Map findResultLog(String id) {
		try {
//			System.out.println("findResultLog:" + id);
			String sql = "SELECT * FROM LDWJ_TESTLOG WHERE TESTCASEID='" + id
					+ "'";
			List<Map<String, Object>> ltcl = jdbcTemplate.queryForList(sql);
			if (ltcl.size()>0) {
				Map ltm = ltcl.get(0);
				return ltm;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void delAllresult(String ids){
		try{
		String sql = "DELETE FROM LDWJ_CHART_RESULT WHERE TESTRESULTID IN ("+ids+")";
		jdbcTemplate.update(sql);
		
		sql = "DELETE FROM LDWJ_INCIDENTLOG WHERE TESTRESULTID IN ("+ids+")";
		jdbcTemplate.update(sql);
		
		sql = "DELETE FROM LDWJ_RESULTDETAIL WHERE RESULTID IN ("+ids+")";
		jdbcTemplate.update(sql);
		
		sql = "DELETE FROM LDWJ_RESULTDETAIL_CUSTOM WHERE RESULTID IN ("+ids+")";
		jdbcTemplate.update(sql);
		
		sql = "DELETE FROM LDWJ_TESTLOG WHERE TEMPID IN ("+ids+")";
		jdbcTemplate.update(sql);
		}catch(Exception e){
			
		}
	}

	/* (non Javadoc) 
	 * @Title: queryCreatetimeByPdfpath
	 * @Description: TODO
	 * @param pdfpath
	 * @return 
	 * @see com.wnt.web.testresult.dao.TestResultDao#queryCreatetimeByPdfpath(java.lang.String) 
	 */
	@Override
	public Integer queryExistByPdfpath(String pdfpath) {
		String sql ="select count(CREATETIME)  from LDWJ_TESTRESULT where FILEURL =? ";
	
		return jdbcTemplate.queryForObject(sql,new Object[]{ pdfpath}, Integer.class);
	}

	/* (non Javadoc) 
	 * @Title: updateTestResult
	 * @Description: TODO
	 * @param testResultId
	 * @param env 
	 * @see com.wnt.web.testresult.dao.TestResultDao#updateTestResult(java.lang.String, com.wnt.web.environment.entry.EquipmentEntry) 
	 */
	@Override
	public void updateTestResult(String testResultId, EquipmentEntry env) {
		String sql ="update LDWJ_TESTRESULT SET NAME=?,VERSION=?,EREMARK=?,IP=?,MAC=?,IP2=?,MAC2=? WHERE TESTDEPLAYID=?";
		jdbcTemplate.update(sql,env.getName(),env.getVersion(),env.getRemark(),env.getIp(),env.getMac(),env.getIp2(),env.getMac2(),testResultId);
		
	}

	/* (non Javadoc) 
	 * @Title: queryEquipmentDetailInResult
	 * @Description: TODO
	 * @param testResultId
	 * @return 
	 * @see com.wnt.web.testresult.dao.TestResultDao#queryEquipmentDetailInResult(java.lang.String) 
	 */
	@Override
	public Map<String, Object> queryEquipmentDetailInResult(String testResultId) {
		String sql ="SELECT *  FROM LDWJ_TESTRESULT WHERE ID=?";
		return jdbcTemplate.queryForMap(sql, testResultId);
	}
	
	@Override
	public List<Map<String, Object>> statisFind(String beginDate, String endDate) {
		
		String sql = "SELECT\n" +
				"	a.date_name,\n" +
				"	COUNT( 1 ) num \n" +
				"FROM\n" +
				"	( SELECT DATE_FORMAT( ts.CREATETIME, '%Y-%m-%d' ) date_name FROM LDWJ_TESTRESULT ts WHERE ts.CREATETIME > ? AND	ts.CREATETIME < ? AND ts.PARENTID IS NULL ) a \n" +
				"GROUP BY\n" +
				"	a.date_name";
		return jdbcTemplate.queryForList(sql, beginDate, endDate);
	}

}
