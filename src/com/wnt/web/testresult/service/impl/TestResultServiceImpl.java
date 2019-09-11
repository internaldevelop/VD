package com.wnt.web.testresult.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.testresult.dao.TestResultDao;
import com.wnt.web.testresult.entry.TestResultEntry;
import com.wnt.web.testresult.service.TestResultService;

@Component("testResultService")
public class TestResultServiceImpl implements TestResultService {
	
	@Resource
	private TestResultDao testResultDao;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void insertBatch(Map param) {
		// TODO Auto-generated method stub
		Random a = new Random();
		//图形类型 1为ARP 2为ICMP 3为TCP 4为离散数据 13为端口0流量 14为端口1流量
		for(int i=1;i<500;i++)
		{
			int rom = a.nextInt(100);
			int month = a.nextInt(9);
			int date = a.nextInt(29);
			//随机生成 1或2
			//int chartype = a.nextInt(2);
			//随机生成 13或1
			int chartype=a.nextInt(2)+13;
			
			String uuid = UUID.randomUUID().toString().replace("-", "");
			
			String sql = " INSERT INTO `LDWJ_CHART`(ID, CHARTTYPE, NUM, CREATETIME) " +
					"VALUES ('"+uuid+"',"+chartype+", "+rom+",  '2016-0"+month+"-"+date+" 09:46:09'); ";
			System.out.println(sql);
			try
			{
				testResultDao.insertBatch(sql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public List queryArp(int type,String parentId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = testResultDao.findCharByType(type,parentId);
		List arpList = new ArrayList();
		for(Map map:list)
		{
//			Object[] obj = new Object[2];
//			Date date = (Date)map.get("CREATETIME");
//			String dates = format.format(date);
//			int num = (Integer)map.get("NUM");
//			obj[0] = "'"+dates+"'";
//			obj[1] = num;
//			arpList.add(obj);
			arpList.add(map.values());
		}
		return arpList;
	}
	
//	@Override
//	public Map queryTcp(int type) {
//		// TODO Auto-generated method stub
//		List<Map<String, Object>> list = testResultDao.findCharByType(type);
//		List tcpxList = new ArrayList();
//		List tcpyList = new ArrayList();
//		Map result = new HashMap();
//		for(Map map:list)
//		{
//			Date date = (Date)map.get("CREATETIME");
//			tcpxList.add(date);
//			int num = (Integer)map.get("NUM");
//			tcpyList.add(num);
//		}
//		result.put("tcpx", tcpxList);
//		result.put("tcpy", tcpyList);
//		return result;
//	}
	
	@Override
	public List queryExcelById(String id, int type)
	{
		//name:模板 name1:测试用例 createtime:执行时间
		//type:1 子节点 type:2 父节点
		String sql = "";
		if(type == 2)
		{
			sql = "select f.EQUIPMENTNAME as name1,c.EQUIPMENTNAME as name,c.CREATETIME as time " +
					"from LDWJ_TESTRESULT  c,LDWJ_TESTRESULT  f " +
					"where c.PARENTID=f.ID and c.PARENTID='"+id+"'";
		}
		else if(type == 1)
		{
			sql = "select f.EQUIPMENTNAME as name1,c.EQUIPMENTNAME as name,c.CREATETIME as time " +
					"from LDWJ_TESTRESULT  c,LDWJ_TESTRESULT  f " +
					"where c.PARENTID=f.ID and c.id='"+id+"'";
		}
		return testResultDao.findListBysql(sql);
	}
	
	/**
	 * pdf
	 * 测试总结：测试列表
	 * @param testResultParentId 测试结果的父节点id
	 * @return 测试用例名称NAME 异常数量 ERRORNUM
	 * 
	 * 	测试案例的开始时间BEGINTIME  测试案例的结束时间ENDTIME
	 * 
	 *  开始测试用例号STARTTESTCASE  结束测试用例号ENDTESTCASE  描述REMARK
	 *  问题溯源TRACEABILITY（0为不允许1为允许）  全局抓包HURRYUP（0为从不 1为总是）  全局测试目标TARGET（1为测试网络1 ，2为测试网络2 ，0为全部）
	 */
	public List<Map<String,Object>> findTestResult(String parentId){
		List<Map<String,Object>> list=null;
		String sql="SELECT ts.ID,ts.EQUIPMENTNAME AS NAME, " +
				"( SELECT count(1) FROM LDWJ_INCIDENTLOG log WHERE ts.id = log.TESTRESULTID AND log.MESSAGETYPE >=4 ) ERRORNUM, " +
				"log.BEGINTIME, log.ENDTIME, " +
				"td.STARTTESTCASE, td.ENDTESTCASE,td.REMARK, TRACEABILITY, HURRYUP, TARGET,PROGRESS " +
				"FROM LDWJ_TESTRESULT ts, LDWJ_TESTLOG log, LDWJ_TESTSUITDETAIL td " +
				"WHERE ts.id = log.TESTCASEID AND ts.TESTDEPLAYID = td.TESTDEPLAYID AND PARENTID = '"+parentId+"'";
		
		list=testResultDao.findListBysql(sql);
		return list;
	}
	
	public List<Map<String,Object>> findTestResultp5(String parentId){
		String sql = "SELECT * FROM LDWJ_TESTRESULT where PARENTID='"+parentId+"' ORDER BY CREATETIME ASC";
		List<Map<String,Object>> list=testResultDao.findListBysql(sql);
		return list;
	}
	
	
	public Map getErrorCount(String id){
		return testResultDao.getErrorCount(id);
	}
	 
	public List<Map<String,Object>> findTestCaseResult(String testCaseId){
		List<Map<String,Object>> list=null;
		String sql="SELECT ts.ID,ts.EQUIPMENTNAME AS NAME, " +
				"( SELECT count(1) FROM LDWJ_INCIDENTLOG log WHERE ts.id = log.TESTRESULTID AND log.MESSAGETYPE >=4 ) ERRORNUM, " +
				"log.BEGINTIME, log.ENDTIME, " +
				"td.STARTTESTCASE, td.ENDTESTCASE,td.REMARK, TRACEABILITY, HURRYUP, TARGET " +
				"FROM LDWJ_TESTRESULT ts, LDWJ_TESTLOG log, LDWJ_TESTSUITDETAIL td " +
				"WHERE ts.id = log.TESTCASEID AND ts.TESTDEPLAYID = td.TESTDEPLAYID AND ts.ID = '"+testCaseId+"'";
		
		list=testResultDao.findListBysql(sql);
		return list;
	}
	
	public List<Map<String,Object>> findTestCaseResulttp5(String testCaseId){
		String sql = "SELECT * FROM LDWJ_TESTRESULT WHERE ID='"+testCaseId+"'";
		List<Map<String,Object>> list=testResultDao.findListBysql(sql);
		return list;
	}
		
	public void updateFilePath(String testCaseId,String fileUrl,String fileName){
		
		String sql = null;
		if(fileName!=null){
			sql=" UPDATE LDWJ_TESTRESULT SET FILEURL='"+fileUrl+"',FILENAME='"+fileName+"' WHERE ID='"+testCaseId+"' ";
		}else{
			sql=" UPDATE LDWJ_TESTRESULT SET FILEURL='"+fileUrl+"',FILENAME=EQUIPMENTNAME WHERE ID='"+testCaseId+"' ";
		}
		
		
		testResultDao.insertBatch(sql);
	}
	/**
	 * pdf
	 * 测试总结：取得测试案例异常总结
	 * @param caseId 测试结果的测试案例id
	 * @return 时间CREATETIME  第几个NUM  测试案例名称NAME
	 */
	public List<Map<String,Object>> findErrorLogByResultId(String testResultId){
		List<Map<String,Object>> list=null;
		String sql="select log.CREATETIME,log.NUM,ts.EQUIPMENTNAME NAME,log.MESSAGE " +
				"from LDWJ_INCIDENTLOG log,LDWJ_TESTRESULT ts " +
				"where log.TESTRESULTID=ts.ID and log.TESTRESULTID='"+testResultId+"' and log.MESSAGETYPE>=4 ORDER BY log.CREATETIME ASC";
		
		list=testResultDao.findListBysql(sql);
		return list;
	}
	
	//替换上
	public List<Map<String,Object>> findErrorLogByResultIdnew(String testResultId){
		List<Map<String,Object>> list=null;
		String sql="select * from LDWJ_INCIDENTLOG where TESTRESULTID='"+testResultId+"' and MESSAGETYPE>=4 ORDER BY CREATETIME ASC";		
		list=testResultDao.findListBysql(sql);
		return list;
	}
	//重载
	public List<Map<String,Object>> findErrorLogByResultIdnew(){
		String sql="SELECT TESTRESULTID FROM LDWJ_INCIDENTLOG WHERE  MESSAGETYPE>=4  AND TESTRESULTID IS NOT NULL GROUP BY TESTRESULTID ";		
		return testResultDao.findListBysql(sql);
	}
	
	/**
	 * 取得测试结果的最小开始时间和最大结束时间
	 * @param testResultId
	 * @return
	 */
	public Map<String,Object> findResultTime(String testResultId){
		List<Map<String,Object>> list=null;
		String sql="SELECT min(BEGINTIME) as BEGINTIME,MAX(ENDTIME) as ENDTIME FROM LDWJ_TESTLOG where TEMPID='"+testResultId+"'";
		
		list=testResultDao.findListBysql(sql);
		if(list.size()>0){
			return list.get(0);
		}else{
			return new HashMap();
		}
	}
	
	
	public static void main(String[] args) {
//		Random random=new Random();
//		for(int i=0;i<10;i++)
//		{
//			int num=random.nextInt(2)+13;
//			System.out.println(num);
//		}
		System.out.println(UUID.randomUUID().toString());
	}

	@Override
	public Map<String, Object> resultdate() {
		// TODO Auto-generated method stub
		Map<String, Object> dataMap = new HashMap<String, Object>();
		//填充测试概数与测试环境数据
		List<Map<String, Object>> part1_list = testResultDao.testDescribe();
		for(Map<String, Object> map:part1_list)
		{
			//NAME,REMARK,IP,MAC,IP2,MAC2
			
			dataMap.put("part1_name", map.get("NAME"));
			dataMap.put("part1_remark", map.get("REMARK"));
			dataMap.put("part1_sdate", "爱叫啥叫啥3");
			dataMap.put("part1_edate", "爱叫啥叫啥4");
			dataMap.put("part1_pdate", format.format(new Date())); 
			
			//调用转IP的方法
			dataMap.put("part2_ip1", map.get("IP"));
			dataMap.put("part2_mac1", map.get("MAC"));
			dataMap.put("part2_ip2", map.get("IP2"));
			dataMap.put("part2_mac2", map.get("MAC2"));
		}
		
		List<Map<String, Object>> tcList = testResultDao.queryPortserverList(6);
		List<Map<String, Object>> tcpList = new ArrayList<Map<String,Object>>();
        for(int i=0;i<tcList.size();i++){
        	Map<String, Object> map=new HashMap<String, Object>();
        	Map<String, Object> map1=tcList.get(i);
        	map.put("port", map1.get("PORTNUM"));
        	map.put("service", (String)map1.get("NAME1"));
        	map.put("from1", map1.get("SOURCE"));
        	tcpList.add(map);
        }
        dataMap.put("newsList", tcpList);
		
        List<Map<String, Object>> udpList = testResultDao.queryPortserverList(17);
        List list2 = new ArrayList();
        for(Map map1:udpList){
        	Map map = new HashMap();
        	map.put("port", map1.get("PORTNUM"));
        	map.put("service", map1.get("NAME"));
        	map.put("from", map1.get("SOURCE"));
        	list2.add(map);
        }
        dataMap.put("udpList", list2);
        
		
		dataMap.put("port", "标题");
		dataMap.put("service", "内容");
		dataMap.put("from", "作者");
		
		return dataMap;
	}
	
	public List<Map<String, Object>> findParent(){
	
		List<Map<String, Object>> ls = testResultDao.findParent();
		List<Map<String, Object>> mls = findErrorLogByResultIdnew();
		//查询父id中的异常数据
		String pid = "";
//		String pc = "";
			/*for(int i=0;i<ls.size();i++){
				Map<String,Object> ms =ls.get(i);
				if(ls.get(i).get("c") != null){
					pid = ls.get(i).get("pId").toString();//记录这个父id 
				}else{	
					//如果本次循环的父节点id为空 并且 ls的id和上一个父节点相等								F3138979A288457E8C3D5AE923F54713
					if(ls.get(i).get("pId") == null && ls.get(i).get("id").toString().equals(pid)){
						ls.get(i).put("c", "1");
					}
				}
			}*/
		//存储父节点
		Map<String,Map<String,Object>> mapParent =new HashMap<String,Map<String,Object>>();
		Map<String,Integer> mapIndex =new HashMap<String,Integer>();
		//存储节点异常
		List<Map<String,Object>> mapError = new ArrayList<Map<String,Object>>();
			for(int i=0;i<ls.size();i++){
				Map<String,Object> ms =ls.get(i);
				if (mls !=null) {
					for (Map<String, Object> mlsMap : mls) {
						if (mlsMap.get("TESTRESULTID").equals(ms.get("id").toString())) {
							ms.put("c", "1");
							mapError.add(ms);
							break;
						}
					}
				}
				if (ms.get("pId") == null ) {
					mapParent.put(ms.get("id").toString(), ms);
					mapIndex.put(ms.get("id").toString(), i);
				}
			}
			for (Map<String, Object> errMap : mapError) {
				Object pId = errMap.get("pId");
				if(pId != null){
					 Map<String, Object> mapPare = mapParent.get(pId); 
					 mapPare.put("c", "1");
					ls.set(mapIndex.get(pId),mapPare);
				}
			}
//		for(int i=0;i<ls.size();i++){
////			Map<String,Object> ms =ls.get(i);
//			if(ls.get(i).get("c") != null){
//				ls.get(i).put("font", "{\"color\":\"red\"}");
//				pid = ls.get(i).get("pId").toString();
//			}else{
//				if(ls.get(i).get("pId") == null && ls.get(i).get("id").toString().equals(pid)){
//					ls.get(i).put("font", "{\"color\":\"red\"}");
//				}else{
//					ls.get(i).put("font", "{}");
//				}
//			}
//		}
		return ls;
		
	}
	
	/**
	 * 得到最后一个父节点
	 * @return
	 */
	public List<Map<String, Object>> findLastParent(){
		return testResultDao.findLastParent();
	}

	@Override
	public List queryMonitor() {
		// TODO Auto-generated method stub
		return testResultDao.queryMonitor();
	}

	@Override
	public List queryPortserverList(int porttype) {
		// TODO Auto-generated method stub
		return testResultDao.queryPortserverList(porttype);
	}

	@Override
	public List testDescribe() {
		// TODO Auto-generated method stub
		return testResultDao.testDescribe();
	}

	@Override
	public List<Map<String, Object>> queryResultById(String id, boolean parent) {
		// TODO Auto-generated method stub
		String sql = " select *  from LDWJ_TESTRESULT where ID='"+id+"' ";
		if(parent)
		{
			sql = " select *  from LDWJ_TESTRESULT where PARENTID='"+id+"'  ";
		
		}
		return testResultDao.findListBysql(sql);
	}

	@Override
	public void deleteResultById(String id, boolean parent) {
		// TODO Auto-generated method stub
		String sql = " select *  from LDWJ_TESTRESULT where ID='"+id+"' ";
		StringBuffer dlid = new StringBuffer();
		dlid.append("'"+id+"',");
		if(parent)
		{
			//判断是否是第一个父节点
			List<Map<String, Object>> list=this.findLastParent();
			if(list!=null && list.size()!=0){
				if(list.get(0).get("id").equals(id)){
					TestResultEntry.deleteStatus=true;
				}
			}
			
			sql = " select *  from LDWJ_TESTRESULT where PARENTID='"+id+"'  ";
			List<Map<String, Object>> map_list = testResultDao.findListBysql(sql);
			Map<String, Object> map = new HashMap<String, Object>();
			for(int i=0;i<map_list.size();i++){
				Map<String, Object> map1 = map_list.get(i);
				dlid.append("'"+map1.get("ID").toString()+"',");
			}
			map.put("ID", id);
			map_list.add(map);
			testResultDao.batchDeleteTemplate(map_list);
		}
		else
		{
			testResultDao.batchDeleteTemplate( testResultDao.findListBysql(sql) );
		}
		String str = dlid.toString();
		str = str.substring(0, str.length()-1);
		testResultDao.delAllresult(str);
	}

	@Override
	public boolean queryFirstDeviceById(String id) {
		// TODO Auto-generated method stub
		String sql = " SELECT ts.ID AS id, ts.PARENTID AS pId, ts.EQUIPMENTNAME AS name, sum(MESSAGETYPE) c, " +
				" ts.CREATETIME FROM LDWJ_TESTRESULT ts LEFT JOIN ( SELECT * FROM LDWJ_INCIDENTLOG WHERE MESSAGETYPE >=4 ) log " +
				" ON log.TESTRESULTID = ts.ID GROUP BY ts.ID, ts.PARENTID, ts.EQUIPMENTNAME, ts.CREATETIME ORDER BY ts.CREATETIME DESC limit 1 ";
		List<Map<String, Object>> list = testResultDao.findListBysql(sql);
		for(Map<String, Object> map:list)
		{
			if(id.equals(map.get("id")) || id.equals(map.get("pId")))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void updateTestResult(String id) {
		// TODO Auto-generated method stub
		String sql = " UPDATE LDWJ_TESTRESULT SET DELSTATUS = 1 WHERE ID='"+id+"' ";
		testResultDao.insertBatch(sql);
	}
	
	public List<Map<String, Object>> findDetailcustomList(String id){
		return testResultDao.findDetailcustomList(id);
	}
	public Map findDetailList(String id){
		return testResultDao.findDetailList(id);
	}
	
	public Map findResultLog(String id){
		return testResultDao.findResultLog(id);
	}

	/* (non Javadoc) 
	 * @Title: updateTestResult
	 * @Description: TODO
	 * @param testResultId
	 * @param env 
	 * @see com.wnt.web.testresult.service.TestResultService#updateTestResult(java.lang.String, com.wnt.web.environment.entry.EquipmentEntry) 
	 */
	@Override
	public void updateTestResult(String testResultId, EquipmentEntry env) {

		 testResultDao.updateTestResult(testResultId,env);
	}

	/* (non Javadoc) 
	 * @Title: queryEquipmentDetailInResult
	 * @Description: TODO
	 * @param testResultId
	 * @return 
	 * @see com.wnt.web.testresult.service.TestResultService#queryEquipmentDetailInResult(java.lang.String) 
	 */
	@Override
	public Map<String, Object> queryEquipmentDetailInResult(String testResultId) {
		
	   return  testResultDao.queryEquipmentDetailInResult( testResultId);
		
	}
	
	@Override
	public List<Map<String, Object>> statisFind(String beginDate, String endDate) {
		beginDate += " 00:00:00";
		endDate += " 23:59:59";
		List<Map<String, Object>> ls = testResultDao.statisFind(beginDate, endDate);
		return ls;
	}
	
}
