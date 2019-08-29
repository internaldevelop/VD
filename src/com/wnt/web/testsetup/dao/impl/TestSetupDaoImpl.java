package com.wnt.web.testsetup.dao.impl;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.web.testsetup.dao.TestSetupDao;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL_CUSTOM;

@Component("testSetupDao")
public class TestSetupDaoImpl implements TestSetupDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> findAll(int type) {
		// TODO Auto-generated method stub
		return jdbcTemplate
				.queryForList(" select id, parent as pId, name, installtype,type,code,testnum from LDWJ_TESTDEPLAYLIVE where type= "
						+ type + " ORDER BY SEQUENCE,ID asc");
	}


	@Override
	public int checkTemplate(int parentId ,String treeid){
//判断表LDWJ_TESTDEPLAYLIVE中当前PARENT是否已添加了NAME BY LVJZ
		String sql ="SELECT COUNT(*) from ldwj_testdeplaylive where PARENT =? and name = (SELECT NAME from LDWJ_TESTDEPLAYLIVE  where id=? )";		
		int icount = jdbcTemplate.queryForObject(sql, new Object[]{parentId,treeid}, Integer.class);
		return icount;
	}
	
	@Override
	public int insertTemplate(String sql) {
		// TODO Auto-generated method stub
		jdbcTemplate.update(sql);
		int last_id = jdbcTemplate.queryForObject(" SELECT LAST_INSERT_ID() ",null,Integer.class);
		return last_id;
	}

	@Override
	public void updateTemplate(String sql) {
		// TODO Auto-generated method stub
		jdbcTemplate.update(sql);
	}

	@Override
	public List<Map<String, Object>> findByCondition(String sql) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForList(sql);
	}

	public Map findDetailList(int id) {
		try {

			String sql = "SELECT * FROM LDWJ_TESTSUITDETAIL L WHERE L.TESTDEPLAYID="
					+ id;
			List<Map<String, Object>> ltcl = jdbcTemplate.queryForList(sql);
			Map ltm = ltcl.get(0);
			// Map ltm = jdbcTemplate.queryForMap(sql, new Object[] { id });
			// LDWJ_TESTSUITDETAIL ltd = new LDWJ_TESTSUITDETAIL();
			// ltd.setCODE(ltm.get("CODE").toString());
			// ltd.setCREATETIME((Timestamp)ltm.get("CREATETIME"));
			// ltd.setDELTATUS((Integer)ltm.get("DELTATUS"));
			// ltd.setEMESSAGEP((Integer)ltm.get("EMESSAGEP"));
			// ltd.setENDTESTCASE((Integer)ltm.get("EMESSAGEP"));
			// ltd.setHURRYUP((Integer)ltm.get("HURRYUP"));
			// ltd.setID((BigInteger)ltm.get("ID"));

			return ltm;
			// return (LDWJ_TESTSUITDETAIL) jdbcTemplate.queryForObject(sql,
			// new Object[] { id }, LDWJ_TESTSUITDETAIL.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Map<String, Object>> findDetailcustomList(int id) {
		String sql = "SELECT * FROM LDWJ_TESTSUITDETAIL_CUSTOM L WHERE L.TESTDEPLAYID="
				+ id+" ORDER BY SORTS ASC";
		List<Map<String, Object>> ltcl = jdbcTemplate.queryForList(sql);
		return ltcl;
		// return (LDWJ_TESTSUITDETAIL_CUSTOM) jdbcTemplate.queryForObject(sql,
		// new Object[] { id }, LDWJ_TESTSUITDETAIL_CUSTOM.class);
	}

	/**
	 * 通过code查询测试用例的名称
	 * 
	 * @param code
	 * @return
	 */
	public String findNameByCode(String code) {
		String sql = "SELECT NAME FROM LDWJ_TESTDEPLAYLIVE WHERE CODE=? AND TYPE=1";
		String name = jdbcTemplate.queryForObject(sql, String.class, code);
		return name;
	}

	public void batchInsertTemplate(final String[] treeId, int parentId) {
		String bach_sql = "insert into LDWJ_TESTDEPLAYLIVE(parent, type, NAME, REMARK, DELTATUS, CREATETIME, CODE, INSTALLTYPE) "
				+ " select "
				+ parentId
				+ ",3, NAME, REMARK, DELTATUS, NOW(), CODE, INSTALLTYPE from LDWJ_TESTDEPLAYLIVE  where id=?";

		jdbcTemplate.batchUpdate(bach_sql, new BatchPreparedStatementSetter() {
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return treeId.length;
			}

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, treeId[i]);
			}
		});
	}

	@Override
	public void batchDeleteTemplate(final List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		String bach_sql = "DELETE FROM LDWJ_TESTDEPLAYLIVE WHERE ID = ?";
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
				ps.setInt(1, (Integer) list.get(i).get("ID"));
			}
		});
	}
	
	@Override
	public void batchDeleteDetail(final List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		String bach_sql = "DELETE FROM LDWJ_TESTSUITDETAIL WHERE TESTDEPLAYID = ?";
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
				ps.setInt(1, (Integer) list.get(i).get("ID"));
			}
		});
	}
	
	@Override
	public void batchDeleteDetailCustom(final List<Map<String, Object>> list) {
		// TODO Auto-generated method stub
		String bach_sql = "DELETE FROM LDWJ_TESTSUITDETAIL_CUSTOM WHERE TESTDEPLAYID = ?";
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
				ps.setInt(1, (Integer) list.get(i).get("ID"));
			}
		});
	}

	public void insertCustom(final List<LDWJ_TESTSUITDETAIL_CUSTOM> ltcus) {
		String sql = "INSERT INTO LDWJ_TESTSUITDETAIL_CUSTOM (ID,TESTDEPLAYID,TYPENAME,FIELDNAME,FIELDVALUE,FIELDTYPE,FIELDLEN,DELTATUS,TYPE,SORTS,CREATETIME,REMARK) VALUES(?,?,?,?,?,?,?,?,?,?,now(),?)";
		try {
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					LDWJ_TESTSUITDETAIL_CUSTOM ltc = ltcus.get(i);
					ps.setString(1, UUIDGenerator.getUUID());
					ps.setObject(2, ltc.getTESTDEPLAYID());
					ps.setString(3, ltc.getTYPENAME());
					ps.setString(4, ltc.getFIELDNAME());
					ps.setObject(5, ltc.getFIELDVALUE());
					ps.setObject(6, ltc.getFIELDTYPE());
					ps.setObject(7, ltc.getFIELDLEN());
					ps.setObject(8, 0);
					ps.setObject(9, ltc.getTYPE());
					ps.setObject(10, ltc.getSORTS());
					ps.setObject(11, ltc.getREMARK());
				}

				public int getBatchSize() {
					// System.out.println(listChart.size());
					return ltcus.size();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertResultCustom(final List<Map<String, Object>> ltcus,
			final String testResultId) {

		String sql = "INSERT INTO LDWJ_RESULTDETAIL_CUSTOM (ID,TESTDEPLAYID,TYPENAME,FIELDNAME,FIELDVALUE,FIELDTYPE,FIELDLEN,DELTATUS,TYPE,SORTS,CREATETIME,RESULTID,REMARK) VALUES(?,?,?,?,?,?,?,?,?,?,now(),?,?)";
		try {
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i)
						throws SQLException {
					Map ltc = ltcus.get(i);
					ps.setString(1, UUIDGenerator.getUUID());
					ps.setObject(2, ltc.get("TESTDEPLAYID"));
					ps.setObject(3, ltc.get("TYPENAME"));
					ps.setObject(4, ltc.get("FIELDNAME"));
					ps.setObject(5, ltc.get("FIELDVALUE"));
					ps.setObject(6, ltc.get("FIELDTYPE"));
					ps.setObject(7, ltc.get("FIELDLEN"));
					ps.setObject(8, 0);
					ps.setObject(9, ltc.get("TYPE"));
					ps.setObject(10, ltc.get("SORTS"));
					ps.setString(11, testResultId);
					ps.setObject(12, ltc.get("REMARK"));
				}

				public int getBatchSize() {
					// System.out.println(listChart.size());
					return ltcus.size();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void insertDetail2(List<Map<String, Object>> list_detail) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertDetail1(String treeId, int last_id, String installtype,String code,int testnum) {
		// TODO Auto-generated method stub
		String sql = " select * from LDWJ_TESTDEPLAYLIVE where id=" + treeId;
		Map<String, Object> map = findByCondition(sql).get(0);

		LDWJ_TESTSUITDETAIL detail = new LDWJ_TESTSUITDETAIL();
		detail.setTESTDEPLAYID(new BigInteger(String.valueOf(last_id)));
		if (installtype.equals("1")) {
			detail.setTESTRATE(1488000);// 测试速率：0为关闭 其他为包/秒
		}else {
			detail.setTESTRATE(0);
		}
//		else if(installtype.equals("4")){
//			if (((String) map.get("REMARK")).contains("IP Fuzzer测试")) {
//				detail.setTESTRATE(100);
//			}
//			if (((String) map.get("REMARK")).contains("ICMP Fuzzer测试")) {
//				detail.setTESTRATE(100);
//			}
//			if (((String) map.get("REMARK")).contains("太网Fuzzer测试")){
//				detail.setTESTRATE(10);
//			}
//			if(((String) map.get("REMARK")).contains("UDP Fuzzer测试")){
//				detail.setTESTRATE(10);
//			}
//			if(((String) map.get("REMARK")).contains("TCP Fuzzer测试")){
//				detail.setTESTRATE(10);
//			}
		
		detail.setTESTTIME(120);// 测试时间 0为全局 其他为秒数
		detail.setSTARTTESTCASE(0);// 起始测试用例 0为从起始位置开始 其他为从其他位置开始回溯
		detail.setENDTESTCASE(0);// 终止测试用例 0为从末尾位置结束 其他为从其他位置结束回溯
		detail.setTRACEABILITY(0);// 问题溯源：0为不允许1为允许
		detail.setHURRYUP(1);// 抓包 0为从不 1为总是
		detail.setTARGET(1);// 目标 ：1为测试网络1 ，2为测试网络2 ，0为全部
		detail.setREMARK((String) map.get("REMARK"));
		detail.setDELTATUS(0);
		detail.setPOWER(0);
		detail.setSMESSAGE(50000);
		detail.setSTOTAL(0);
		detail.setEMESSAGEP(0);
		detail.setPORTSEND(0);

		sql = "INSERT INTO LDWJ_TESTSUITDETAIL(TESTDEPLAYID, TESTRATE, TESTTIME, STARTTESTCASE, ENDTESTCASE, REMARK, DELTATUS, CREATETIME,CODE,TRACEABILITY,HURRYUP,TARGET,POWER,SMESSAGE,EMESSAGEP,ISSEND,PORTSEND,STOTAL,TESTNUM) "
				+ "values(?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?)";

		jdbcTemplate.update(sql, last_id, detail.getTESTRATE(),
				detail.getTESTTIME(), detail.getSTARTTESTCASE(),
				detail.getENDTESTCASE(), detail.getREMARK(),
				detail.getDELTATUS(), code,
				detail.getTRACEABILITY(), detail.getHURRYUP(),
				detail.getTARGET(), detail.getPOWER(), detail.getSMESSAGE(),
				detail.getEMESSAGEP(), detail.getISSEND(),
				detail.getPORTSEND(), detail.getSTOTAL(),testnum);
	}

	@Override
	public void insertDetailResult(Map detail, String testResultId) {
		try {
			String sql = "INSERT INTO LDWJ_RESULTDETAIL(TESTDEPLAYID, TESTRATE, TESTTIME, STARTTESTCASE, ENDTESTCASE, REMARK, DELTATUS, CREATETIME,CODE,TRACEABILITY,HURRYUP,TARGET,POWER,SMESSAGE,EMESSAGEP,ISSEND,RESULTID,PORTSEND,STOTAL) "
					+ "values(?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?)";

			jdbcTemplate.update(sql, detail.get("TESTDEPLAYID"),
					detail.get("TESTRATE"), detail.get("TESTTIME"),
					detail.get("STARTTESTCASE"), detail.get("ENDTESTCASE"),
					detail.get("REMARK"), detail.get("DELTATUS"),
					detail.get("CODE"), detail.get("TRACEABILITY"),
					detail.get("HURRYUP"), detail.get("TARGET"),
					detail.get("POWER"), detail.get("SMESSAGE"),
					detail.get("EMESSAGEP"), detail.get("ISSEND"),
					testResultId, detail.get("PORTSEND"), detail.get("STOTAL"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override                     
	public void insertDetail(LDWJ_TESTSUITDETAIL detail) {
		String sql = "INSERT INTO LDWJ_TESTSUITDETAIL(TESTDEPLAYID, TESTRATE, TESTTIME, STARTTESTCASE, ENDTESTCASE, REMARK, DELTATUS, CREATETIME,CODE,TRACEABILITY,HURRYUP,TARGET,POWER,SMESSAGE,EMESSAGEP,ISSEND,PORTSEND,STOTAL,TESTNUM) "
				+ "values(?,?,?,?,?,?,?,now(),?,?,?,?,?,?,?,?,?,?,?)";

		jdbcTemplate.update(sql, detail.getTESTDEPLAYID(),
				detail.getTESTRATE(), detail.getTESTTIME(),
				detail.getSTARTTESTCASE(), detail.getENDTESTCASE(),
				detail.getREMARK(), detail.getDELTATUS(), detail.getCODE(),
				detail.getTRACEABILITY(), detail.getHURRYUP(),
				detail.getTARGET(), detail.getPOWER(), detail.getSMESSAGE(),
				detail.getEMESSAGEP(), detail.getISSEND(),
				detail.getPORTSEND(), detail.getSTOTAL(),detail.getTESTNUM());
	}

	/* (non Javadoc) 
	 * @Title: findCustomTemplate
	 * @Description: TODO 
	 * @see com.wnt.web.testsetup.dao.TestSetupDao#findCustomTemplate() 
	 */
	@Override
	public Integer findCustomTemplate() {
		
		String remark="模板添加数据";
		String sql ="SELECT COUNT(*) FROM LDWJ_TESTDEPLAYLIVE WHERE TYPE =? AND REMARK=? ";
		return jdbcTemplate.queryForObject(sql, new Object[]{2,remark}, Integer.class);
		
		//return jdbcTemplate.queryForInt(sql,new Object[]{2,remark});
		
	}
	@Override
	public Integer findNonameTemplate() {
		
		String sql ="SELECT COUNT(*) FROM LDWJ_TESTDEPLAYLIVE WHERE TYPE =? ";
		return jdbcTemplate.queryForObject(sql, new Object[]{3}, Integer.class);
	}

	/* (non Javadoc) 
	 * @Title: findTestSuiteTestNum
	 * @Description: TODO
	 * @param remark
	 * @return 
	 * @see com.wnt.web.testsetup.dao.TestSetupDao#findTestSuiteTestNum(java.lang.String) 
	 */
	@Override
	public List<Map<String, Object>> findTestSuiteTestNum(String remark) {
		
		String sql ="SELECT TESTNUM FROM LDWJ_TESTDEPLAYLIVE WHERE REMARK=? AND SEQUENCE IS NOT NULL";
		return jdbcTemplate.queryForList(sql,remark);
		
	}

	/* (non Javadoc) 
	 * @Title: deleteCus
	 * @Description: TODO
	 * @param id
	 * @return 
	 * @see com.wnt.web.testsetup.dao.TestSetupDao#deleteCus(java.lang.String) 
	 */
	@Override
	public void deleteCus(String id) {
		
		String sql ="DELETE FROM LDWJ_TESTSUITDETAIL_CUSTOM WHERE ID =?";
	     jdbcTemplate.queryForList(sql,id);
		
	}
}
