package com.wnt.web.testexecute.service;

import java.util.Date;
import java.util.List;

import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.environment.entry.MonitorDetailEntry;

import net.sf.json.JSONObject;

public interface TestExecuteService {
	public List findLog(Long currentTime, String[] ms);

	/**
	 * 执行测试用例后，查询监视器的状态
	 * 
	 * @param code
	 * @param sourceType
	 *            来源类型:1为ARP monitor 2为ICMP monitor 3为TCP monitor 4为离散数据 monitor
	 *            5为测试用例
	 * @return
	 */
	public String findLogByMonitor(String code, String sourceType,
			Date beginTime);

	/**
	 * 插入模板的执行时间日志
	 * 
	 * @param tempId
	 * @param beginTime
	 */
	public void inertTestTemplateLog(String tempId, Date beginTime);

	/**
	 * 插入测试用例的执行时间日志
	 * 
	 * @param tempId
	 * @param caseId
	 * @param beginTime
	 */
	public void inertTestCaseLog(String tempId, String caseId, Date beginTime);

	/**
	 * 向测试结果表中加入父节点信息
	 * 
	 * @param equipmentName
	 * @return
	 */
	public String insertTestResult(String equipmentName,EquipmentEntry env,List<MonitorDetailEntry> listMoni);

	/**
	 * 向测试结果表中加入测试案例节点信息
	 * 
	 * @param parentId
	 * @param testDeplayId
	 * @return
	 */
	public String insertTestCaseResult(String parentId, int testDeplayId,
			String testCaseName, String code, int installtype, String remark);

	/**
	 * 将数据copy
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param testResultId
	 */
	public void copyChart(Date beginTime, Date endTime, String testResultId);

	/**
	 * 清除日志
	 */
	public void clearLog();

	/**
	 * 
	 * @Title: insertTestCaseResult
	 * @Description: 管理平台>应用安全>测试结果 插入数据
	 * @param jsonObj
	 * @return
	 * @return: void
	 */
	public String insertTestCaseResult(JSONObject jsonObj);

	/** 
	 * 更新测试结果的测试进度
	 * @Title: updateProgress 
	 * @Description: TODO
	 * @param parentId		模板Id
	 * @param testDeplayId	测试案例ID
	 * @param progress		实际进度
	 * @return: void
	 */
	void updateProgress(String parentId, Integer testDeplayId, Integer progress);
}
