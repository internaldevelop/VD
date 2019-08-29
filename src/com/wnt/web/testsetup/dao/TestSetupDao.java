package com.wnt.web.testsetup.dao;

import java.util.List;
import java.util.Map;

import com.wnt.web.testsetup.entry.LDWJ_TESTDEPLAYLIVE;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL_CUSTOM;

public interface TestSetupDao {
	
	/**
	 * 查询全部的树形结构数据
	 * @return
	 */
	public List<Map<String, Object>> findAll(int type);
		
	//判断表LDWJ_TESTDEPLAYLIVE中当前PARENT是否已添加了NAME BY LVJZ
	public int checkTemplate(int parentId ,String treeid);
	
	/**
	 * 添加模板
	 * @param treeId
	 * @param name
	 */
	public int insertTemplate(String sql);
	
	/**
	 * 执行更改的操作
	 * @param sql
	 * @return
	 */
	public void updateTemplate(String sql);
	
	/**
	 * 根据传入的sql语句执行
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> findByCondition(String sql);
	
	
	/**
	 * 通过code查询测试用例的名称
	 * @param code
	 * @return
	 */
	public String findNameByCode(String code);
	
	/**
	 * 批量向数据库添加数据
	 * @param treeId 页面添加的数据
	 * @param parentId 父节点的ID
	 */
	public void batchInsertTemplate(String[] treeId, int parentId);
	
	/**
	 * 批量向数据库删除数据
	 * @param list 要删除的数据
	 */
	public void batchDeleteTemplate(List<Map<String, Object>> list);
	
	/**
	 * 批量插入自定义
	 * @param ltcus
	 */
	public void insertCustom(final List<LDWJ_TESTSUITDETAIL_CUSTOM> ltcus);
	
	/**
	 * 批量插入详细 type2
	 * @param treeId 页面添加的数据
	 * @param parentId 父节点的ID
	 */
	public void insertDetail2(List<Map<String, Object>> list_detail);
	
	/**
	 * 批量插入详细 type1
	 * @param treeId 页面添加的数据
	 * @param parentId 父节点的ID
	 */
	public void insertDetail1(String treeId,int last_id,String installtype,String code,int testnum);
	
	public Map findDetailList(int id);
	
	public void insertDetailResult(Map detail,String testResultId);
	public List<Map<String, Object>> findDetailcustomList(int id);
	public void insertResultCustom(List<Map<String, Object>> ltc,String testResultId);
	
	public void insertDetail(LDWJ_TESTSUITDETAIL detail);
	
	public void batchDeleteDetail(final List<Map<String, Object>> list);
	
	public void batchDeleteDetailCustom(final List<Map<String, Object>> list);

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
	 * @param id
	 * @return
	 * @return: Object
	 */
	public void deleteCus(String id);
}
