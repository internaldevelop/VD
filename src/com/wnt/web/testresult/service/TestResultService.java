package com.wnt.web.testresult.service;

import java.util.List;
import java.util.Map;

import com.wnt.web.environment.entry.EquipmentEntry;

public interface TestResultService {
	
	/**
	 * 批量添加测试结果数据
	 * @return
	 */
	public void insertBatch(Map param);
	
	/**
	 * 查询ARP散点图
	 * @return
	 */
	public List<String[]> queryArp(int type,String parentId);
	
//	/**
//	 * 查询TCP折线图
//	 * @return
//	 */
//	public Map<String, Object> queryTcp(int type);
	
	/**
	 * 根据节点类型导出excel数据
	 * @param id 树节点ID
	 * @param type 1:子节点 2:父节点
	 * @return
	 */
	public List queryExcelById(String id, int type);
	
	/**
	 * 生成模版,数据填充
	 */
	public Map<String, Object> resultdate();
	
	public List<Map<String, Object>> findParent();
	/**
	 * 得到最后一个父节点
	 * @return
	 */
	public List<Map<String, Object>> findLastParent();
	/**
	 * pdf
	 * 测试总结：测试列表
	 * @param testResultParentId 测试结果的父节点id
	 * @return 测试用例名称NAME 异常数量 ERRORNUM
	 * 
	 * 	测试案例的开始时间BEGINTIME  测试案例的结束时间ENDTIME
	 * 
	 *  开始测试用例号STARTTESTCASE  结束测试用例号ENDTESTCASE  
	 *  问题溯源TRACEABILITY（0为不允许1为允许）  全局抓包HURRYUP（0为从不 1为总是）  全局测试目标TARGET（1为测试网络1 ，2为测试网络2 ，0为全部）
	 */
	public List<Map<String,Object>> findTestResult(String parentId);

	public List<Map<String,Object>> findTestCaseResult(String testCaseId);
	/**
	 * 创建pdf并修改文件名称
	 * @param testCaseId
	 */
	public void updateFilePath(String testCaseId,String fileUrl,String fileName);
	/**
	 * pdf
	 * 测试总结：取得测试案例异常总结
	 * @param caseId 测试结果的测试案例id
	 * @return 时间CREATETIME  第几个NUM  测试案例名称NAME
	 */
	public List<Map<String,Object>> findErrorLogByResultId(String testResultId);
	/**
	 * 取得测试结果的最小开始时间和最大结束时间
	 * @param testResultId
	 * @return
	 */
	public Map<String,Object> findResultTime(String testResultId);
	
	/**
	 * 查询测试概述与测试环境信息//设备
	 * @return
	 */
	public List testDescribe();
	
	/**
	 * 根据PORTTYPE字段查询端口数据,端口类型：6 TCP 17UDP 端口
	 * @param porttype
	 * @return
	 */
	public List queryPortserverList(int porttype);
	
	/**
	 * 查询监视器数据 监控
	 * @return
	 */
	public List queryMonitor();
	
	/**
	 * 根据ID查询数据详细信息
	 * @param id	当前查询节点主键
	 * @param parent 是否为父节点
	 * @return 查询数据的详细信息
	 */
	public List<Map<String, Object>> queryResultById(String id, boolean parent);
	
	public List<Map<String,Object>> findErrorLogByResultIdnew(String testResultId);
	/**
	 * 根据ID删除数据详细信息
	 * @param id	当前查询节点主键
	 * @param parent 是否为父节点
	 * @return 查询数据的详细信息
	 */
	public void deleteResultById(String id, boolean parent);
	
	/**
	 * 判断如果是第一个设备测试生成,如果不是则不生成
	 * @param id
	 * @return true:执行生成  false:不生成
	 */
	public boolean queryFirstDeviceById(String id);
	
	/**
	 * 修改测试结果数据
	 */
	public void updateTestResult(String id);
	
	public List<Map<String,Object>> findTestResultp5(String parentId);
	
	public Map getErrorCount(String id);
	
	public List<Map<String, Object>> findDetailcustomList(String id);
	public Map findDetailList(String id);
	public List<Map<String,Object>> findTestCaseResulttp5(String testCaseId);
	
	public Map findResultLog(String id);

	/** 
	 * @Title: updateTestResult 
	 * @Description: TODO
	 * @param testResultId
	 * @param env
	 * @return: void
	 */
	public void updateTestResult(String testResultId, EquipmentEntry env);

	/** 
	 * @Title: queryEquipmentDetailInResult 
	 * @Description: TODO
	 * @param testResultId
	 * @return
	 * @return: Map<String,Object>
	 */
	public Map<String, Object> queryEquipmentDetailInResult(String testResultId);
}
