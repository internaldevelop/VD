package com.wnt.web.testsetup.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.web.testsetup.dao.TestSetupDao;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL_CUSTOM;
import com.wnt.web.testsetup.service.TestSetupService;

@Component("testSetupService")
public class TestSetupServiceImpl implements TestSetupService {

	@Resource
	private TestSetupDao testSetupDao;

	@Override
	public List<Map<String, Object>> findAll(int type) {
		// TODO Auto-generated method stub
		return testSetupDao.findAll(type);
	}

	@Override
	public List<Map<String, Object>> findAllTemplate(int type, int parent) {
		String sql = " select id as id, parent as pId, name as name "
				+ " from LDWJ_TESTDEPLAYLIVE where (type=" + type
				+ " or type=3) and parent=" + parent+" ORDER BY SEQUENCE,ID asc";
		return testSetupDao.findByCondition(sql);
	}

	@Override
	public List<Map<String, Object>> findTestSuiteById(int id) {
		String sql = " select * from LDWJ_TESTDEPLAYLIVE where PARENT=" + id+" ORDER BY SEQUENCE,ID asc";
		return testSetupDao.findByCondition(sql);
	}

	/**
	 * 查询模板ID获取模板信息
	 * 
	 * @return
	 */
	public Map<String, Object> findTemplateById(int id) {
		String sql = " select * from LDWJ_TESTDEPLAYLIVE where id=" + id;
		List<Map<String, Object>> list = testSetupDao.findByCondition(sql);
		return list.get(0);
	}

	/**
	 * 保存未命名模板对应的 测试用例信息
	 * 
	 * @param treeId
	 *            测试用例名称
	 */
	@Override
//	public void insertNonameTemplate(String[] treeId, String[] type,
//			String[] installtype) {
		public void insertNonameTemplate(String[] treeId, String[] type,
				String[] installtype,String[] code,List<Integer> testNumList) {
		/**
		 * 查询测试 未命名 的模板的ID parentId:未命名模板的ID主键
		 * 
		 * 1.如果不存在则新添加 未命名 的模板信息(if) 2.如果存在的话,则取出 未命名 模板主键ID(else)
		 */
		int parentId = 0;
		String sql = " select ID from  LDWJ_TESTDEPLAYLIVE where NAME='未命名'";
		List<Map<String, Object>> list = testSetupDao.findByCondition(sql);
		if (null == list || list.size() == 0) {
			String insert_sql = " insert into LDWJ_TESTDEPLAYLIVE(parent, name, remark, type)"
					+ "	values(0,'未命名','模板添加数据', 3)";
			parentId = testSetupDao.insertTemplate(insert_sql);
		} else {
			parentId = (Integer) list.get(0).get("ID");
		}

		for (int i = 0; i < treeId.length; i++) {
			// SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss|SSS");
			 //System.out.println("外层for :"+simpleDateFormat.format(new Date()));
			sql = "INSERT INTO LDWJ_TESTDEPLAYLIVE(PARENT, TYPE, NAME, REMARK, DELTATUS, CREATETIME, CODE, INSTALLTYPE) "
					+ " SELECT "
					+ parentId
					+ ",3, NAME, REMARK, DELTATUS, NOW(), CODE, INSTALLTYPE from LDWJ_TESTDEPLAYLIVE  where id="
					+ treeId[i];
			int last_id = testSetupDao.insertTemplate(sql);

			if (type[i].equals("2")) {
				if (installtype[i].equals("3")) { // 用户自定义
					String uuid = UUIDGenerator.getUUID();
					sql = "INSERT INTO LDWJ_TESTSUITDETAIL_CUSTOM(ID,TESTDEPLAYID, TYPENAME, FIELDNAME, FIELDVALUE, FIELDTYPE, FIELDLEN, DELTATUS, CREATETIME,TYPE,SORTS,REMARK) "
							+ " select REPLACE(UUID(),'-',''),"
							+ last_id
							+ ",TYPENAME, FIELDNAME, FIELDVALUE, FIELDTYPE, FIELDLEN, DELTATUS, CREATETIME,TYPE,SORTS,REMARK FROM LDWJ_TESTSUITDETAIL_CUSTOM WHERE TESTDEPLAYID="
							+ treeId[i];
					testSetupDao.updateTemplate(sql);
				}
				sql = "INSERT INTO LDWJ_TESTSUITDETAIL(TESTDEPLAYID, TESTRATE, TESTTIME, STARTTESTCASE, ENDTESTCASE, REMARK, DELTATUS, CREATETIME,CODE,TRACEABILITY,HURRYUP,TARGET,POWER,SMESSAGE,EMESSAGEP,ISSEND,PORTSEND,STOTAL,TESTNUM) "
						+ " select "
						+ last_id
						+ ",TESTRATE, TESTTIME, STARTTESTCASE, ENDTESTCASE, REMARK, DELTATUS, CREATETIME,CODE,TRACEABILITY,HURRYUP,TARGET,POWER,SMESSAGE,EMESSAGEP,ISSEND,PORTSEND,STOTAL,TESTNUM FROM LDWJ_TESTSUITDETAIL WHERE TESTDEPLAYID="
						+ treeId[i];
				System.out.println("cha ru tao jian xiang xi :"+sql);
				testSetupDao.updateTemplate(sql);

			} else {
				
					//System.out.println("内层for :"+simpleDateFormat.format(new Date()));
					int testnum =testNumList.get(i);
					testSetupDao.insertDetail1(treeId[i], last_id,installtype[i],code[i],testnum);
				
//				testSetupDao.insertDetail1(treeId[i], last_id,installtype[i]);
			}
		}

		/**
		 * 根据(新插入)(查询的)的未命名主键ID 开始向 数据库批量插入数据
		 */
		// testSetupDao.batchInsertTemplate(treeId, parentId);
		/**
		 * 开始向 数据库添加 套件对应的 详情表
		 */
		// String detail_sql =
		// "select ID from LDWJ_TESTDEPLAYLIVE where PARENT = (select ID from LDWJ_TESTDEPLAYLIVE L where L.`NAME`='未命名') order by CREATETIME DESC limit "
		// + treeId.length;
		// List<Map<String, Object>> list_detail = testSetupDao
		// .findByCondition(detail_sql);

		// for (Map<String, Object> map_detail : list_detail) {
		// insertTestSuiteDetail(map_detail.get("ID") + "", "1");
		// }
	}

	@Override
	public void insertTemplate(String[] treeId, String[] type, String name) {
//		System.out.println("==========service==============");
//		for (int i = 0; i < treeId.length; i++) {
//			System.out.println("插入自定义模板treeId :" +treeId[i]);
//		}
//		System.out.println("插入自定义模板名称 :" +name);
//		for (int i = 0; i < type.length; i++) {
//			System.out.println("插入自定义模板type :" +type[i]);
//		}
//		System.out.println("++++++++++service++++++++++++++");
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = null;
		if ("未命名".equals(name)) {// zgh 判断无用，不会执行
			// 如果传过来的是未命名,则判断数据库是否存在该数据
			String sql = " select * from  LDWJ_TESTDEPLAYLIVE where NAME='"
					+ name + "'";
			list = testSetupDao.findByCondition(sql);
			/**
			 * 1.如果存在查询 未命名 的ID 为多少 2.查询 未命名 的子节点所有 ID
			 * 
			 */
			if (null != list && list.size() > 0) {
				// 1.如果存在查询 未命名 的ID 为多少
				String id = list.get(0).get("ID").toString();
				// 2.查询 未命名 的子节点所有 ID
				String childSql = " select * from LDWJ_TESTDEPLAYLIVE where PARENT="
						+ id;
				List<Map<String, Object>> childList = testSetupDao
						.findByCondition(childSql);
				// 3.页面的treeId数组为,新的 未命名 模板的数据
				// 4.删除旧的未命名模板数据,添加新的数据
				List<String> pageList = Arrays.asList(treeId);
				for (Map<String, Object> map : childList) {
					// 如果数据库的数据在集合中存在,则不动, 不在 则删除
					if (pageList.contains(map.get("ID"))) {
						// deleteList.add(map.get("ID"));
						testSetupDao
								.updateTemplate(" delete from LDWJ_TESTDEPLAYLIVE where ID="
										+ map.get("ID"));
					}
				}
				// 5.向数据库添加页面穿过的数据
				// 遍历出页面所有的用例库
				String ids = "";
				for (int i = 0; i < treeId.length; i++) {
					String query_sql = " select * from LDWJ_TESTDEPLAYLIVE where ID="
							+ treeId[i] + " and PARENT=" + id;
					List queryList = testSetupDao.findByCondition(query_sql);
					if (null == queryList || queryList.size() == 0) {
						// 1.向模板中添加数据
						// 获取新插入的ID,设置第二步骤的pid为ID
						String bach_sql = "insert into LDWJ_TESTDEPLAYLIVE(parent, type, NAME, REMARK, DELTATUS, CREATETIME, CODE, INSTALLTYPE) "
								+ " select "
								+ id
								+ ",2, NAME, REMARK, DELTATUS, CREATETIME, CODE, INSTALLTYPE from LDWJ_TESTDEPLAYLIVE  where id="
								+ treeId[i];
						int lastDetailId = testSetupDao
								.insertTemplate(bach_sql);
						// 2.向套件详情表中添加数据
						insertTestSuiteDetail(lastDetailId + "", type[i]);
					}
				}
			}
		} else {
			// 根据名称插入新的记录(name)
			String insert_sql = " insert into LDWJ_TESTDEPLAYLIVE(parent, name, remark, type)"
					+ "	values(0,'" + name + "','模板添加数据', 2)";
			int last_id = testSetupDao.insertTemplate(insert_sql);
			String sqlz = "UPDATE LDWJ_TESTDEPLAYLIVE SET PARENT=" + last_id
					+ ", TYPE=2 WHERE TYPE=3 AND PARENT != 0";
			// String sqlz =
			// "DELETE FROM　LDWJ_TESTDEPLAYLIVE　WHERE INSTALLTYPE=3";
			testSetupDao.updateTemplate(sqlz);
			// 遍历出页面所有的用例库
			// String ids = "";
			// for(int i=0;i<treeId.length;i++)
			// {
			// //1.向模板中添加数据
			// //获取新插入的ID,设置第二步骤的pid为ID
			// String bach_sql =
			// "insert into LDWJ_TESTDEPLAYLIVE(parent, type, NAME, REMARK, DELTATUS, CREATETIME, CODE, INSTALLTYPE) "
			// +
			// " select "+last_id+",2, NAME, REMARK, DELTATUS, CREATETIME, CODE, INSTALLTYPE from LDWJ_TESTDEPLAYLIVE  where id="+treeId[i];
			// int lastDetailId = testSetupDao.insertTemplate(bach_sql);
			// //2.向套件详情表中添加数据
			// insertTestSuiteDetail(lastDetailId+"", type[i]);
			// }
		}
	}

	// @Override
	// public void insertTemplate(String[] treeId, String[] type, String name) {
	// // TODO Auto-generated method stub
	// List<Map<String, Object>> list = null;
	// if("未命名".equals(name))
	// {
	// //如果传过来的是未命名,则判断数据库是否存在该数据
	// String sql = " select * from  LDWJ_TESTDEPLAYLIVE where NAME='"+name+"'";
	// list = testSetupDao.findByCondition(sql);
	// //如果bu存在的话,则直接做插入操作 直接操作第2步
	// }
	// //根据名称插入新的记录(name)
	// String insert_sql =
	// " insert into LDWJ_TESTDEPLAYLIVE(parent, name, remark, type)" +
	// "	values(0,'"+name+"','模板添加数据', 2)";
	// int last_id = testSetupDao.insertTemplate(insert_sql);
	//
	// //遍历出页面所有的用例库
	// String ids = "";
	// for(int i=0;i<treeId.length;i++)
	// {
	// //1.向模板中添加数据
	// //获取新插入的ID,设置第二步骤的pid为ID
	// String bach_sql =
	// "insert into LDWJ_TESTDEPLAYLIVE(parent, type, NAME, REMARK, DELTATUS, CREATETIME, CODE, INSTALLTYPE) "
	// +
	// " select "+last_id+",2, NAME, REMARK, DELTATUS, CREATETIME, CODE, INSTALLTYPE from LDWJ_TESTDEPLAYLIVE  where id="+treeId[i];
	// int lastDetailId = testSetupDao.insertTemplate(bach_sql);
	// //2.向套件详情表中添加数据
	// insertTestSuiteDetail(lastDetailId+"", type[i]);
	// }
	// //如果存在的话,则查询出来,则做删除操作了
	// if(null != list && list.size() > 0)
	// {
	// String id = list.get(0).get("ID").toString();
	// removeTemplate(id);
	// }
	// }

	// 遍历向套件详情表添加记录
	// 此数据是添加 模板及未命名 模板默认绑定的数据
	public void insertTestSuiteDetail(String treeId, String type) {
		Map<String, Object> map = findTemplateById(Integer.valueOf(treeId));

		LDWJ_TESTSUITDETAIL detail = new LDWJ_TESTSUITDETAIL();
		detail.setTESTDEPLAYID(new BigInteger(treeId));
		detail.setTESTRATE(1488000);// 测试速率：0为关闭 其他为包/秒
		detail.setTESTTIME(120);// 测试时间 0为全局 其他为秒数
		detail.setSTARTTESTCASE(0);// 起始测试用例 0为从起始位置开始 其他为从其他位置开始回溯
		detail.setENDTESTCASE(0);// 终止测试用例 0为从末尾位置结束 其他为从其他位置结束回溯
		detail.setTRACEABILITY(0);// 问题溯源：0为不允许1为允许
		detail.setHURRYUP(1);// 抓包 0为从不 1为总是
		detail.setTARGET(1);// 目标 ：1为测试网络1 ，2为测试网络2 ，0为全部

		detail.setREMARK((String) map.get("REMARK"));

		insertTestSuiteDetail(detail);
	}

	@Override
	public void removeTemplate(String treeId) {
		// TODO Auto-generated method stub
		// 根据treeId获取当前节点信息与所有子节点信息		
		String sql ="";
		sql = "DELETE FROM LDWJ_TESTSUITDETAIL WHERE TESTDEPLAYID IN "
				+ "	(SELECT a.id from (select * from LDWJ_TESTDEPLAYLIVE) a "
				+ "	where parent=" + treeId + " or id=" + treeId + ")";
		testSetupDao.updateTemplate(sql);
		
		sql = "delete from LDWJ_TESTSUITDETAIL_CUSTOM where TESTDEPLAYID in "
				+ "	(select a.id from (select * from LDWJ_TESTDEPLAYLIVE) a "
				+ "	where parent=" + treeId + " or id=" + treeId + ")";
		testSetupDao.updateTemplate(sql);
		
		sql = "delete from LDWJ_TESTDEPLAYLIVE where id in "
				+ "	(select a.id from (select * from LDWJ_TESTDEPLAYLIVE) a "
				+ "	where parent=" + treeId + " or id=" + treeId + ")";
		testSetupDao.updateTemplate(sql);
	}

	@Override
	public List<Map<String, Object>> findByName(String name) {
		// TODO Auto-generated method stub
		String sql = "select ID,NAME,installtype as TYPE from LDWJ_TESTDEPLAYLIVE where PARENT = (select ID from LDWJ_TESTDEPLAYLIVE L where L.`NAME`='未命名') order by ID ASC";
		return testSetupDao.findByCondition(sql);
	}

	@Override
	public void insertTestSuiteDetail(LDWJ_TESTSUITDETAIL detail) {
		String sql = "DELETE FROM LDWJ_TESTSUITDETAIL WHERE TESTDEPLAYID= '"
				+ detail.getTESTDEPLAYID() + "'";
		testSetupDao.updateTemplate(sql);
		testSetupDao.insertDetail(detail);
	}

	// @Override
	// public void insertTestSuiteDetail(LDWJ_TESTSUITDETAIL detail) {
	// // TODO Auto-generated method stub
	// // 如果套件ID存在的话,则直接跳过执行,修改操作
	// String qsql =
	// " SELECT * FROM LDWJ_TESTSUITDETAIL DETAIL WHERE DETAIL.TESTDEPLAYID= "
	// + detail.getTESTDEPLAYID();
	// List<Map<String, Object>> list = testSetupDao.findByCondition(qsql);
	//
	// // 拼接sql语句
	// String sql = "";
	// if (null != list && list.size() > 0) {
	// sql = "UPDATE LDWJ_TESTSUITDETAIL SET TESTRATE="
	// + detail.getTESTRATE() + ", TESTTIME="
	// + detail.getTESTTIME() + ", STARTTESTCASE="
	// + detail.getSTARTTESTCASE() + ", " + " ENDTESTCASE="
	// + detail.getENDTESTCASE() + ", TRACEABILITY="
	// + detail.getTRACEABILITY() + ", HURRYUP="
	// + detail.getHURRYUP() + ", TARGET=" + detail.getTARGET()
	// + ", POWER=" + detail.getPOWER() + ", SMESSAGE="
	// + detail.getSMESSAGE() + ", EMESSAGEP="
	// + detail.getEMESSAGEP() +
	// ", PORTSEND="+detail.getPORTSEND()+" WHERE TESTDEPLAYID="
	// + detail.getTESTDEPLAYID();
	// } else {
	// sql =
	// "INSERT INTO `LDWJ_TESTSUITDETAIL`(TESTDEPLAYID, TESTRATE, TESTTIME, STARTTESTCASE, ENDTESTCASE, TRACEABILITY, HURRYUP, TARGET,POWER,SMESSAGE,EMESSAGEP,REMARK) "
	// + "VALUES ("
	// + detail.getTESTDEPLAYID()
	// + ", "
	// + detail.getTESTRATE()
	// + ","
	// + detail.getTESTTIME()
	// + ","
	// + detail.getSTARTTESTCASE()
	// + ""
	// + ","
	// + detail.getENDTESTCASE()
	// + ", "
	// + detail.getTRACEABILITY()
	// + ", "
	// + detail.getHURRYUP()
	// + ", "
	// + detail.getTARGET()
	// + ", "
	// + detail.getPOWER()
	// + ", "
	// + detail.getSMESSAGE()
	// + ", "
	// + detail.getEMESSAGEP()
	// + ",'"
	// + detail.getREMARK()
	// + "');";
	// }
	// // 执行sql语句
	// testSetupDao.updateTemplate(sql);
	// }

	public void insertCustom(LDWJ_TESTSUITDETAIL detail,
			List<LDWJ_TESTSUITDETAIL_CUSTOM> ltcus) {
		for (int i = 0; i < ltcus.size(); i++) {
			ltcus.get(i).setTESTDEPLAYID(detail.getTESTDEPLAYID());
			ltcus.get(i).setREMARK(detail.getREMARK());
		}
		testSetupDao.insertCustom(ltcus);
	}

	public void deleteCustom(LDWJ_TESTSUITDETAIL detail) {
		String sql = "DELETE FROM LDWJ_TESTSUITDETAIL_CUSTOM WHERE TESTDEPLAYID="
				+ detail.getTESTDEPLAYID();
		testSetupDao.updateTemplate(sql);
	}

	public void updateDeplaylive(LDWJ_TESTSUITDETAIL detail, String proname) {
		String sql = "UPDATE LDWJ_TESTDEPLAYLIVE SET NAME ='" + proname
				+ "' WHERE ID='" + detail.getTESTDEPLAYID() + "'";
		testSetupDao.updateTemplate(sql);
	}

	@Override
	public List<Map<String, Object>> findTestSuiteDetail(String testDeplayId) {
		// TODO Auto-generated method stub
		String sql = "select * from LDWJ_TESTSUITDETAIL L where L.TESTDEPLAYID="
				+ testDeplayId;
		return testSetupDao.findByCondition(sql);
	}

	public List<Map<String, Object>> findCuston(String testDeplayId) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM LDWJ_TESTSUITDETAIL_CUSTOM L WHERE L.TESTDEPLAYID="
				+ testDeplayId + " ORDER BY TYPE ASC,SORTS ASC";
		return testSetupDao.findByCondition(sql);
	}

	/**
	 * 通过code查询测试用例的名称
	 * 
	 * @param code
	 * @return
	 */
	public String findNameByCode(String code) {
		return testSetupDao.findNameByCode(code);
	}

	/**
	 * 查询模板ID获取模板信息和类型
	 * 
	 * @return
	 */
	public Map<String, Object> findTestSuiteDetailAndType(int id) {
		String sql = "SELECT L.*,T.INSTALLTYPE FROM LDWJ_TESTSUITDETAIL L,LDWJ_TESTDEPLAYLIVE T WHERE L.TESTDEPLAYID=T.ID AND L.TESTDEPLAYID="
				+ id;
		List<Map<String, Object>> list = testSetupDao.findByCondition(sql);
		Map<String, Object> m = list.get(0);
		return m;
	}

	public Map findDetailList(int id) {
		return testSetupDao.findDetailList(id);
	}

	public List<Map<String, Object>> findDetailcustomList(int id) {
		return testSetupDao.findDetailcustomList(id);
	}

	@Override
	public void removeTemplateBySelft(String treeId) {
		// TODO Auto-generated method stub
		String sql = "";
		if ("all".equals(treeId)) {
			sql = " select ID from LDWJ_TESTDEPLAYLIVE where PARENT = (select ID from LDWJ_TESTDEPLAYLIVE L where L.`NAME`='未命名') ";
			List<Map<String, Object>> list = testSetupDao.findByCondition(sql);
			testSetupDao.batchDeleteTemplate(list);
			testSetupDao.batchDeleteDetail(list);
			testSetupDao.batchDeleteDetailCustom(list);
		} else {
			sql = " DELETE FROM LDWJ_TESTDEPLAYLIVE WHERE ID=" + treeId;
			testSetupDao.updateTemplate(sql);
			
			sql = " DELETE FROM LDWJ_TESTSUITDETAIL WHERE TESTDEPLAYID=" + treeId;
			testSetupDao.updateTemplate(sql);
			
			sql = " DELETE FROM LDWJ_TESTSUITDETAIL_CUSTOM WHERE TESTDEPLAYID=" + treeId;
			testSetupDao.updateTemplate(sql);
		}
		
	}

	@Override
	public List<Map<String, Object>> findDefaultTestSuiteId(String sql) {
		return testSetupDao.findByCondition(sql);
	}

	public void insertDetailResult(Map detail, String testResultId) {
		testSetupDao.insertDetailResult(detail, testResultId);
	}

	public void insertResultCustom(List<Map<String, Object>> ltc,
			String testResultId) {
		testSetupDao.insertResultCustom(ltc, testResultId);
	}

	/* (non Javadoc) 
	 * @Title: findCustomTemplate
	 * @Description: TODO 
	 * @see com.wnt.web.testsetup.service.TestSetupService#findCustomTemplate() 
	 */
	@Override
	public Integer findCustomTemplate() {
		
		
		return testSetupDao.findCustomTemplate();
	}
	@Override
	public void clearTemplate() {
		// TODO Auto-generated method stub
		// 根据treeId获取当前节点信息与所有子节点信息		
		String sql ="";
		sql = "DELETE FROM LDWJ_TESTSUITDETAIL WHERE TESTDEPLAYID IN "
				+ "	(SELECT a.id from (select * from LDWJ_TESTDEPLAYLIVE where type=2) a) ";
		testSetupDao.updateTemplate(sql);
		
		sql = "delete from LDWJ_TESTSUITDETAIL_CUSTOM where TESTDEPLAYID in "
				+ "	(select a.id from (select * from LDWJ_TESTDEPLAYLIVE where type=2) a) ";
		testSetupDao.updateTemplate(sql);
		
		sql = "delete from LDWJ_TESTDEPLAYLIVE where id in "
				+ "	(select a.id from (select * from LDWJ_TESTDEPLAYLIVE where type=2) a) ";
		testSetupDao.updateTemplate(sql);
	}
	@Override
	public Integer findNonameTemplate() {
		
		
		return testSetupDao.findNonameTemplate();
	}

	/* (non Javadoc) 
	 * @Title: findTestSuiteTestNum
	 * @Description: TODO
	 * @param remark
	 * @return 
	 * @see com.wnt.web.testsetup.service.TestSetupService#findTestSuiteTestNum(java.lang.String) 
	 */
	@Override
	public List<Map<String, Object>> findTestSuiteTestNum(String remark) {
		
	     return testSetupDao.findTestSuiteTestNum(remark);
		
	}

	/* (non Javadoc) 
	 * @Title: deleteCus
	 * @Description: TODO
	 * @param id 
	 * @see com.wnt.web.testsetup.service.TestSetupService#deleteCus(java.lang.String) 
	 */
	@Override
	public void deleteCus(String id) {
		testSetupDao.deleteCus(id);
	}
}
