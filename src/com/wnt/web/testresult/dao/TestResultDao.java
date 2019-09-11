package com.wnt.web.testresult.dao;

import java.util.List;
import java.util.Map;

import com.wnt.web.environment.entry.EquipmentEntry;

public interface TestResultDao {
	
	/**
	 * 批量添加测试结果数据
	 * @return
	 */
	public void insertBatch(String sql);
	
	/**
	 * 查询图形
	 * @return
	 */
	public List findCharByType(int type,String parentId);
	
	/**
	 * 根据SQL查询数据
	 * @param sql
	 * @return
	 */
	public List findListBysql(String sql);
	
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
	
	public List<Map<String, Object>> findParent();
	/**
	 * 得到最后一个父节点
	 * @return
	 */
	public List<Map<String, Object>> findLastParent();

	public void batchDeleteTemplate(List<Map<String, Object>> list);
	
	//获取错误数量
	public Map getErrorCount(String id);
	
	public List<Map<String, Object>> findDetailcustomList(String id);
	public Map findDetailList(String id);
	
	public Map findResultLog(String id);
	
	public void delAllresult(String ids);

	/** 
	 * @Title: queryCreatetimeByPdfpath 
	 * @Description: TODO
	 * @param pdfpath
	 * @return
	 * @return: Object
	 */
	public Integer queryExistByPdfpath(String pdfpath);

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
	

	public List<Map<String, Object>> statisFind(String beginDate, String endDate);
}
