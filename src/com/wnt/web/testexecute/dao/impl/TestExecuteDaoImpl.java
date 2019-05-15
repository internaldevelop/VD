package com.wnt.web.testexecute.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.environment.entry.MonitorDetailEntry;
import com.wnt.web.testexecute.dao.TestExecuteDao;

@Repository("testExecuteDao")
public class TestExecuteDaoImpl implements TestExecuteDao {
	private final Logger log = Logger.getLogger(TestExecuteDaoImpl.class.getName());
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public List findLog(Long currentTime, String[] ms) {
		StringBuffer s = new StringBuffer();
		s.append("and (1=2 ");
		if (ms != null) {

			for (int i = 0; i < ms.length; i++) {
				if (ms[i].equals("5")) {
					s.append(" or SOURCETYPE>=" + ms[i]);
				} else {
					s.append(" or SOURCETYPE=" + ms[i]);
				}

			}

		}
		s.append(" or SOURCE = 'CPU状态告警' ");
		s.append(" or SOURCE = '内存状态告警' ");
		s.append(" or SOURCE = '硬盘状态告警' ");
		s.append(")");

		String sql = "select SOURCE,MESSAGE,CREATETIME,SOURCETYPE from LDWJ_INCIDENTLOG where DELTATUS=0 AND CREATETIME>? "
				+ s.toString() + " order by createtime";

		return jdbcTemplate.queryForList(sql, new Date(currentTime));
	}

	/**
	 * 执行测试用例后，查询监视器的状态
	 * 
	 * @param code
	 * @param sourceType
	 *            来源类型:1为ARP monitor 2为ICMP monitor 3为TCP monitor 4为离散数据 monitor
	 *            5为测试用例
	 * @return
	 */
	public String findLogByMonitor(String code, String sourceType, Date beginTime) {
		String sql = "SELECT MESSAGETYPE FROM LDWJ_INCIDENTLOG WHERE SOURCETYPE=? AND MESSAGETYPE>3 AND CREATETIME>=? order by MESSAGETYPE DESC limit 0,1";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, sourceType, beginTime);
		// log.info("执行测试用例后，查询监视器的状态:"+sql+","+code+","+sourceType);
		// log.info("执行测试用例后，查询监视器的状态 查询结果:"+list.size());
		if (list.size() == 1) {
			return list.get(0).get("MESSAGETYPE").toString();
		}
		return null;
	}

	/**
	 * 插入测试用例的执行时间日志
	 * 
	 * @param tempId
	 * @param caseId
	 * @param beginTime
	 */
	@Override
	public void inertTestCaseLog(String tempId, String caseId, Date beginTime) {
		String sql = "INSERT INTO LDWJ_TESTLOG(ID,TEMPID,TESTCASEID,BEGINTIME,ENDTIME,TYPE) VALUES(?,?,?,?,?,?)";
		jdbcTemplate.update(sql, UUIDGenerator.getUUID(), tempId, caseId, beginTime, new Date(), 2);
	}

	/**
	 * 插入模板的执行时间日志
	 * 
	 * @param tempId
	 * @param beginTime
	 */
	@Override
	public void inertTestTemplateLog(String tempId, Date beginTime) {
		String sql = "INSERT INTO LDWJ_TESTLOG(ID,TEMPID,BEGINTIME,ENDTIME,TYPE) VALUES(?,?,?,?,?)";
		jdbcTemplate.update(sql, UUIDGenerator.getUUID(), tempId, beginTime, new Date(), 1);

	}

	/**
	 * 向测试结果表中加入父节点信息
	 * 
	 * @param equipmentName
	 * @return
	 */
	public String insertTestResult(String equipmentName,EquipmentEntry env,List<MonitorDetailEntry> listMoni) {
		String id = UUIDGenerator.getUUID();
		MonitorDetailEntry md1 =null;
		MonitorDetailEntry md2 =null;
		MonitorDetailEntry md3 =null;
		MonitorDetailEntry md4 =null;
		for (int i=0;i<listMoni.size();i++) {
			MonitorDetailEntry md = listMoni.get(i);
			if(i==0) md1 = md;
			if(i==1) md2 = md;
			if(i==2) md3 = md;
			if(i==3) md4 = md;
		}
		//String sql = "INSERT INTO LDWJ_TESTRESULT(ID,EQUIPMENTNAME,CREATETIME,NAME,VERSION,EREMARK,IP,MAC,IP2,MAC2,MID1,OVERTIME1,CYCLEPERIOD1,INPUT1,ALARMLEVEL1,,MID2,OVERTIME2,CYCLEPERIOD2,INPUT2,ALARMLEVEL2,MID3,OVERTIME3,CYCLEPERIOD3,INPUT3,ALARMLEVEL3,,MID4,OVERTIME4,CYCLEPERIOD4,INPUT4,ALARMLEVEL4) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String sql = "INSERT INTO LDWJ_TESTRESULT(ID,EQUIPMENTNAME,CREATETIME,NAME,VERSION,EREMARK,IP,MAC,IP2,MAC2,MID1,OVERTIME1,SELECTSTATUS1,MID2,OVERTIME2,SELECTSTATUS2,MID3,SELECTSTATUS3,MID4,SELECTSTATUS4,CYCLEPERIOD,INPUT,ALARMLEVEL) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, id, equipmentName, new Date(),env.getName(),env.getVersion(),env.getRemark(),env.getIp(),env.getMac(),env.getIp2(),env.getMac2(),md1.getId(),md1.getOvertime(),md1.getSelectstatus(),md2.getId(),md2.getOvertime(),md2.getSelectstatus(),md3.getId(),md3.getSelectstatus(),md4.getId(),md4.getSelectstatus(),md4.getCyclePeriod(),md4.getInput(),md4.getAlarmLevel());
		return id;
	}

	/**
	 * 向测试结果表中加入测试案例节点信息
	 * 
	 * @param parentId
	 * @param testDeplayId
	 * @return
	 */
	public String insertTestCaseResult(String parentId, int testDeplayId, String testCaseName, String code,
			int installtype, String remark) {
		String id = UUIDGenerator.getUUID();
		String sql = "INSERT INTO LDWJ_TESTRESULT(ID,PARENTID,TESTDEPLAYID,EQUIPMENTNAME,CODE,INSTALLTYPE,CREATETIME,REMARK) VALUES(?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql, id, parentId, testDeplayId, testCaseName, code, installtype, new Date(), remark);
		return id;
	}
	
	@Override
	public void updateProgress(String parentId,Integer testDeplayId,Integer progress){
		String  sql = "UPDATE LDWJ_TESTRESULT SET PROGRESS = ? "
				+ "WHERE TESTDEPLAYID = ? AND PARENTID = ? "
				+ "AND CREATETIME = (SELECT maxCT.CT FROM (SELECT MAX(CREATETIME) AS CT FROM LDWJ_TESTRESULT lt WHERE lt.TESTDEPLAYID = ? AND lt.PARENTID = ?) AS maxCT )";
		jdbcTemplate.update(sql,progress,testDeplayId,parentId,testDeplayId,parentId);
	}

	/**
	 * 将数据copy
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param testResultId
	 */
	public void copyChart(Date beginTime, Date endTime, String testResultId) {
		String sql = "INSERT INTO LDWJ_CHART_RESULT (CHARTTYPE,NUM,REMARK,DELTATUS,CREATETIME,CODE,TESTRESULTID) "
				+ "(SELECT CHARTTYPE,NUM,REMARK,DELTATUS,CREATETIME,CODE,? FROM LDWJ_CHART WHERE CREATETIME>? AND CREATETIME<?) ";
		jdbcTemplate.update(sql, testResultId, beginTime, endTime);
	}

	@Override
	public void clearLog() {
		String sql = "UPDATE LDWJ_INCIDENTLOG SET DELTATUS=1 WHERE DELTATUS=0";
		jdbcTemplate.update(sql);
	}

	/*
	 * (non Javadoc)
	 * 
	 * @Title: insertTestCaseResult
	 * 
	 * @Description: 管理平台>应用安全>测试结果 插入数据
	 * 
	 * @param jsonObj
	 * 
	 * @return
	 * 
	 * @see
	 * com.wnt.web.testexecute.dao.TestExecuteDao#insertTestCaseResult(net.sf.
	 * json.JSONObject)
	 */
	@Override
	public String insertTestCaseResult(JSONObject jsonObj) {
		// 应测试组要求，增加一个默认的根节点
		// 此时定义根节点为“appsecurityTest”
		String sql_find = "select count(*) from LDWJ_TESTRESULT where id = 'appsecurityTest' and flag = 2";
		int parentNode = jdbcTemplate.queryForObject(sql_find, Integer.class);
		if(parentNode < 1){
			String sql_parent = "INSERT INTO LDWJ_TESTRESULT(ID,EQUIPMENTNAME,flag) VALUES('appsecurityTest','测试结果',2)";
			jdbcTemplate.update(sql_parent);
		}
		
		String id = UUIDGenerator.getUUID();
		String name = "";
		String fileUrl = "";
		if(null != jsonObj){
			String time = DataUtils.formatDateTime();
			name = time + "(" + (String) jsonObj.get("name") + ")";
			fileUrl = (String) jsonObj.get("fileUrl") + id;
		}
		String sql="INSERT INTO LDWJ_TESTRESULT(ID,PARENTID,FILEURL,EQUIPMENTNAME,FILENAME,flag,CREATETIME) VALUES(?,?,?,?,?,?,?)";
		jdbcTemplate.update(sql,new Object[]{id,"appsecurityTest",fileUrl,name,name,2,new Date()});
		return id;
	}
}
