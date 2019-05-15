package com.wnt.web.testsetup.service;

import java.util.List;
import java.util.Map;

import com.wnt.web.testsetup.entry.LDWJ_TESTDEPLAYLIVE;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL_CUSTOM;

public interface TestSetupService {
	
	/**
	 * 查询全部的树形结构数据
	 * @return
	 */
	public List<Map<String, Object>> findAll(int type);
	
	/**
	 * 查询全部的模板数据
	 * @return
	 */
	public List<Map<String, Object>> findAllTemplate(int type, int parent);
	
	/**
	 * 查询模板ID获取测试用例
	 * @return
	 */
	public List<Map<String, Object>> findTestSuiteById(int id);
	
	/**
	 * 添加模板
	 * @param treeId
	 * @param name
	 */
	public void insertTemplate(String[] treeId, String[] type, String name);
	
	/**
	 * 删除数据(模板以及模板下子节点)
	 * @param treeId
	 */
	public void removeTemplate(String treeId);
	
	/**
	 * 删除数据(模板自己)
	 * @param treeId
	 */
	public void removeTemplateBySelft(String treeId);
	
	/**
	 * 查询 未命名模板 的数据
	 * @param
	 * @return
	 */
	public List<Map<String, Object>> findByName(String name);
	
	/**
	 * 添加测试套件详细
	 * @param treeId
	 * @param name
	 */
	public void insertTestSuiteDetail(LDWJ_TESTSUITDETAIL detail);
	
	/**
	 * 查询测试套件详细
	 * @param treeId
	 * @param name
	 */
	public List<Map<String, Object>> findTestSuiteDetail(String testDeplayId);
	/**
	 * 通过code查询测试用例的名称
	 * @param code
	 * @return
	 */
	public String findNameByCode(String code);
	
	/**
	 * 查询模板ID获取模板信息
	 * @return
	 */
	public Map<String, Object> findTemplateById(int id);
	
	/**
	 * 查询模板ID获取模板信息和类型
	 * @return
	 */
	public Map<String, Object> findTestSuiteDetailAndType(int id);
	
	/**
	 * 保存未命名的模板 对应 测试用例 信息
	 * @param treeId 测试用例
	 */
//	public void insertNonameTemplate(String[] treeId,String[] type,String[] installtype);
	public void insertNonameTemplate(String[] treeId,String[] type,String[] installtype,String[] code,List<Integer> testNumList);
	
	/**
	 * 根据条件查询sql
	 * @param treeId
	 * @param name
	 */
	public List<Map<String, Object>> findDefaultTestSuiteId(String sql);

	public List<Map<String, Object>> findCuston(String testDeplayId);
	
	public void deleteCustom(LDWJ_TESTSUITDETAIL detail);
	
	public void insertCustom(LDWJ_TESTSUITDETAIL detail,List<LDWJ_TESTSUITDETAIL_CUSTOM> ltcus);
	
	public Map findDetailList(int id);
	
	public void insertDetailResult(Map detail,String testResultId);
	
	public List<Map<String, Object>> findDetailcustomList(int id);
	
	public void insertResultCustom(List<Map<String, Object>> ltc,String testResultId);
	
	public void updateDeplaylive(LDWJ_TESTSUITDETAIL detail,String proname);
	/**
	 * 清空模版(模板以及模板下子节点)
	 * @param treeId
	 */
	public void clearTemplate();
	/** 
	 * @Title: findCustomTemplate 
	 * @Description: TODO
	 * @return: void
	 */
	public Integer findCustomTemplate();
	/** 
	 * @Title: 查询测试套件数量
	 * @Description: TODO
	 * @return: void
	 */
	public Integer findNonameTemplate();

	/** 
	 * @Title: findTestSuiteTestNum 
	 * @Description: TODO
	 * @param remark
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findTestSuiteTestNum(String remark);

	/** 
	 * @Title: deleteCus 
	 * @Description: TODO
	 * @param parameter
	 * @return: void
	 */
	public void deleteCus(String id);
}
