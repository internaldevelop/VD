package com.wnt.web.testexecute.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.environment.entry.MonitorDetailEntry;
import com.wnt.web.testexecute.dao.TestExecuteDao;
import com.wnt.web.testexecute.service.TestExecuteService;

@Service("testExecuteService")
public class TestExecuteServiceImpl implements TestExecuteService {
	@Resource
	TestExecuteDao testExecuteDao;

	@Override
	public List findLog(Long currentTime, String[] ms) {
		// TODO Auto-generated method stub
		return testExecuteDao.findLog(currentTime, ms);
	}

	@Override
	public String findLogByMonitor(String code, String sourceType,
			Date beginTime) {
		// TODO Auto-generated method stub
		return testExecuteDao.findLogByMonitor(code, sourceType, beginTime);
	}

	/**
	 * 插入模板的执行时间日志
	 * 
	 * @param tempId
	 * @param beginTime
	 */
	public void inertTestTemplateLog(String tempId, Date beginTime) {
		testExecuteDao.inertTestTemplateLog(tempId, beginTime);
	}

	/**
	 * 插入测试用例的执行时间日志
	 * 
	 * @param tempId
	 * @param caseId
	 * @param beginTime
	 */
	public void inertTestCaseLog(String tempId, String caseId, Date beginTime) {
		testExecuteDao.inertTestCaseLog(tempId, caseId, beginTime);

	}

	/**
	 * 向测试结果表中加入父节点信息
	 * 
	 * @param equipmentName
	 * @return
	 */
	public String insertTestResult(String equipmentName,EquipmentEntry env,List<MonitorDetailEntry> listMoni) {
		return testExecuteDao.insertTestResult(equipmentName,env,listMoni);
	}

	/**
	 * 向测试结果表中加入测试案例节点信息
	 * 
	 * @param parentId
	 * @param testDeplayId
	 * @return
	 */
	public String insertTestCaseResult(String parentId, int testDeplayId,
			String testCaseName, String code, int installtype, String remark) {
		return testExecuteDao.insertTestCaseResult(parentId, testDeplayId,
				testCaseName, code, installtype, remark);

	}
	
	/**
	 * 更新测试结果的实际测试进度
	 * @Title: updateProgress 
	 * @param parentId		模板ID
	 * @param testDeplayId	测试案例ID
	 * @param progress		实际测试进度
	 * @return: void
	 */
	@Override
	public void updateProgress(String parentId,Integer testDeplayId,Integer progress){
		testExecuteDao.updateProgress(parentId, testDeplayId, progress);
	}

	/**
	 * 将数据copy
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param testResultId
	 */
	public void copyChart(Date beginTime, Date endTime, String testResultId) {
		testExecuteDao.copyChart(beginTime, endTime, testResultId);
	}

	@Override
	public void clearLog() {
		testExecuteDao.clearLog();

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
	 * @see
	 * com.wnt.web.testexecute.service.TestExecuteService#insertTestCaseResult
	 * (net.sf.json.JSONObject)
	 */
	@Override
	public String insertTestCaseResult(JSONObject jsonObj) {
		return testExecuteDao.insertTestCaseResult(jsonObj);
	}
}
