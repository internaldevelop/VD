package com.wnt.web.testexecute.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.LogUtil;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.environment.entry.MonitorDetailEntry;
import com.wnt.web.environment.service.EnvironmentService;
import com.wnt.web.operationlog.service.OperationLogService;
import com.wnt.web.testexecute.entry.TestCaseEntry;
import com.wnt.web.testexecute.entry.TestEntry;
import com.wnt.web.testexecute.service.TestExecuteService;
import com.wnt.web.testresult.entry.TestResultEntry;
import com.wnt.web.testresult.service.PdfService;
import com.wnt.web.testresult.service.TestResultService;
import com.wnt.web.testsetup.service.TestSetupService;

import common.ConstantsDefs;
import common.EquipmentDef;
import common.TestExecuteUtil;

/**
 * 测试执行控制类
 * 
 * @author 付强
 * @version 1.0
 * @company 汇才同飞
 * @site http://www.javakc.cn
 * 
 */
@Controller
@RequestMapping("/testexecute")
public class TestExecuteController {
	//EHCacheUtil.get("thread")
	
	
	private final Logger log = Logger.getLogger(TestExecuteController.class.getName());

	private ModelAndView modelAndView;

	private static final String SUCCESS_PAGE = "index";

	@Resource
	private TestSetupService testSetupService;
	@Resource
	private TestResultService testResultService;
	@Resource
	private TestExecuteService testExecuteService;
	@Resource
	private EnvironmentService environmentService;
	@Resource
	private PdfService pdfService;
	@Resource
    private OperationLogService operationLogService;
	
	static Object o=new Object();
	private Map map = new HashMap();
	
	private Map<String,Integer> statusMap = new HashMap<String,Integer>();
	private static Integer maxNum = 5;
	//到测试执行页面
	@RequestMapping("/execute")
	public String execute(HttpServletRequest request) {
		//1:用例库, 2:模板库
		List<Map<String, Object>> list=testSetupService.findAllTemplate(2, 0);
		for(int i=0;i<list.size();i++){
			Map m1=list.get(i);
			if(m1.get("name").equals("未命名")){
				if(i!=0){
					list.remove(i);
					list.add(0,m1);
				}
				break;
			}
		}
		//查询设备选择了哪些监视器(id,name)
		List<Map<String, Object>> monitorList=environmentService.findSelectMonitor(1);
		//复制一份监视器的list，供事件日志使用
		List<Map<String, Object>> logList=environmentService.findSelectMonitor(1);
		//复制一份监视器的list，供执行列表使用
		List<Map<String, Object>> exeList=environmentService.findSelectMonitor(1);
		//Eth0
		Map<String, Object> eth0=new HashMap();
		eth0.put("id", "eth0");
		eth0.put("name", "Eth0");
		
		monitorList.add(0,eth0);
		
		//查询设备是否选择了Eth1
		//取得设备信息
		Map env=environmentService.findEnvironmentByEquipmentId("1");
		if((Integer)env.get("ENABLE2")==1){
			//request.setAttribute("ENABLE2", true);
			Map<String, Object> eth1=new HashMap();
			eth1.put("id", "eth1");
			eth1.put("name", "Eth1");
			
			monitorList.add(1,eth1);
		}
		
		//事件日志多一个选项
		Map<String, Object> test=new HashMap();
		test.put("id", "5");
		test.put("name", "测试用例");
		logList.add(test);
		
		
		//读取缓存，查看用户是否选择了某些图表，在list中进行相关的标记
		Map<String,Boolean> executeChartMap=(Map<String,Boolean> )EHCacheUtil.get("executeChartMap");
		//判断缓存中是否存在
		if(executeChartMap==null){
			executeChartMap=new HashMap<String,Boolean>();
			//选择图表初始化
			executeChartMap.put("eth0", true);
			
			EHCacheUtil.put("executeChartMap", executeChartMap);
		}
		for(Map<String, Object> m:monitorList){
			//判断是否选中
			if(executeChartMap.get(m.get("id").toString())!=null){
				m.put("checked", executeChartMap.get(m.get("id").toString()));
			}else{
				m.put("checked", false);
			}
		}
		
		//读取缓存，查看用户是否选择了某些日志，在list中进行相关的标记
		Map<String,Boolean> executeLogMap=(Map<String,Boolean> )EHCacheUtil.get("executeLogMap");
		//判断缓存中是否存在
		if(executeLogMap==null){
			executeLogMap=new HashMap<String,Boolean>();
			executeLogMap.put("1", true);
			executeLogMap.put("2", true);
			executeLogMap.put("3", true);
			executeLogMap.put("4", true);
			executeLogMap.put("5", true);
			EHCacheUtil.put("executeLogMap", executeLogMap);
		}
		for(Map<String, Object> m:logList){
			//判断是否选中
			if(executeLogMap.get(m.get("id").toString())!=null){
				m.put("checked", executeLogMap.get(m.get("id").toString()));
			}
		}
		
		int monitorNum=exeList.size();
		
		request.setAttribute("exeList", exeList);
		request.setAttribute("monitorList", monitorList);
		request.setAttribute("logList", logList);
		//用于生成td的数量，有多少个监视器，就在表格中生成多少个td
		request.setAttribute("monitorNum", monitorNum);
		request.setAttribute("list", list);
		
		//判断是否有测试
		//TestThread t=TestExecuteUtil.t;
		//&& (TestExecuteUtil.testEntry.getStatus()==2 ||TestExecuteUtil.testEntry.getStatus()==3)
		if(TestExecuteUtil.testEntry!=null ){
//			LogUtil.info("进入页面=t不为空");
			//取得用户选择的tempId
			request.setAttribute("tempId", TestExecuteUtil.testEntry.getTempId());
			request.setAttribute("testStatus", TestExecuteUtil.testEntry.getStatus());
			request.setAttribute("testCaseList", TestExecuteUtil.testEntry.getList());
			request.setAttribute("progress", TestExecuteUtil.testEntry.getProgress());
			request.setAttribute("type", TestExecuteUtil.testEntry.getType());
		}else{
//			LogUtil.info("进入页面=t为空");
			request.setAttribute("testStatus",1);
			request.setAttribute("progress",0);
		}
		String testId=request.getParameter("testId");
		request.setAttribute("testId", testId);
		//日志后缀
		request.setAttribute("logProgressDisplay",EquipmentDef.logProgressDisplay);
		return "testexecute/list";
	}
	
	//根据模板id查找，测试案例
	@ResponseBody  
	@RequestMapping("/findTestSuite")
	public Map findTestSuite(HttpServletRequest request){
		try{
			String tempId=request.getParameter("tempId");
			List<Map<String, Object>> list=testSetupService.findTestSuiteById(Integer.valueOf(tempId));
			
			map.put("result",list);
			map.put("status", "y");
			map.put("info", "添加成功");
		}catch(Exception e){
			log.error("根据模板id查找，测试案例异常",e);
			map.put("success", true);
			map.put("status", "n");
			map.put("info", "添加失败");
		}
		return map;
	}
	
	//定时器取得事件日志
	@ResponseBody  
	@RequestMapping("/findLog")
	public Map findLog(HttpServletRequest request){
		try{
			String currentTime_s=request.getParameter("currentTime");
			String[] ms=request.getParameterValues("ms");
			Long currentTime=Long.valueOf(currentTime_s);
			
			List<Map<String, Object>> list=testExecuteService.findLog(currentTime,ms);
			if(list.size()==0){
				map.put("time", currentTime_s);
			}else{
				map.put("time", list.get(list.size()-1).get("CREATETIME"));
			}
			//将时间转换
			for(Map<String, Object> m:list){
				Timestamp t=(Timestamp) m.get("CREATETIME");
				m.put("CREATETIME", DataUtils.formatTime2(t.getTime()));
			}
			map.put("result",list);
			map.put("status", "y");
			map.put("info", "添加成功");
		}catch(Exception e){
			log.error("定时器取得事件日志异常",e);
			map.put("success", true);
			map.put("status", "n");
			map.put("info", "添加失败");
		}
		
		return map;
	}
	
	
	//保存图表复选框状态
	@ResponseBody  
	@RequestMapping("/saveExecuteChart")
	public Map saveExecuteChart(HttpServletRequest request){
		try{
			String id=request.getParameter("id");
			boolean checked=Boolean.valueOf(request.getParameter("checked"));
			//保存到缓存中
			Map<String,Boolean> executeChartMap=(Map<String,Boolean> )EHCacheUtil.get("executeChartMap");
			executeChartMap.put(id, checked);
			EHCacheUtil.put("executeChartMap", executeChartMap);
			map.put("status", "y");
			map.put("info", "添加成功");
		}catch(Exception e){
			log.error("保存图表复选框状态异常",e);
			map.put("success", true);
			map.put("status", "n");
			map.put("info", "添加失败");
		}
		
		return map;
	}
	
	//保存事件日志复选框状态
	@ResponseBody  
	@RequestMapping("/saveExecuteLog")
	public Map saveExecuteLog(HttpServletRequest request){
		try{
			String id=request.getParameter("id");
			boolean checked=Boolean.valueOf(request.getParameter("checked"));
			//保存到缓存中
			Map<String,Boolean> executeLogMap=(Map<String,Boolean> )EHCacheUtil.get("executeLogMap");
			executeLogMap.put(id, checked);
			
			map.put("status", "y");
			map.put("info", "添加成功");
		}catch(Exception e){
			log.error("保存事件日志复选框状态",e);
			map.put("success", true);
			map.put("status", "n");
			map.put("info", "添加失败");
		}
		return map;
	}
	
	//测试用例全部执行
	@ResponseBody  
	@RequestMapping("/startTest")
	public Map startTest(HttpServletRequest request){
		try{
			//判断是否有线程开启
			//checkThread();
			//创建线程
			TestEntry testEntryNew =createThread(request);
			TestExecuteUtil.testEntry =testEntryNew;
			//全部执行
			TestExecuteUtil.testEntry.setStatus(1);
			TestExecuteUtil.testEntry.setType(2);
			EHCacheUtil.remove("r185");
			//t.setName("全部测试线程-"+new Date().toLocaleString());
			//启动线程
			//t.start();
			EquipmentDef.logProgressDisplay = "";
			map.put("status", "y");
			
			String userName = request.getSession().getAttribute("userName").toString();
            operationLogService.addOperationLog(userName, request, "执行测试", "成功", "模板名称:"+testEntryNew.getTempName());
		}catch(Exception e){
			log.error("启动测试执行异常", e);
			map.put("success", true);
			map.put("status", "n");
			map.put("info", "添加失败");
			
			String userName = request.getSession().getAttribute("userName").toString();
            operationLogService.addOperationLog(userName, request, "执行测试", "失败", "启动测试执行异常");
		}
		return map;
	}
	
	//测试用例单个执行
	@ResponseBody  
	@RequestMapping("/startSingleTest")
	public Map startSingleTest(HttpServletRequest request){
		try{
			//判断是否有线程开启
			//checkThread();
			
			String tempId_s=request.getParameter("tempId");
			int tempId=Integer.valueOf(tempId_s);
			
			//TestThread t=(TestThread) EHCacheUtil.get("thread");
			//TestThread t=TestExecuteUtil.t;
			
			String s_index=request.getParameter("index");
			int index=Integer.valueOf(s_index);
			
			EquipmentDef.logProgressDisplay = "";
			//如果线程没有创建，或者用户更改了模板
//			if(t==null || TestExecuteUtil.testEntry.getTempId()!=tempId){
//				//创建线程
			//？？？
			TestEntry testEntryNew =createThread(request);
			testEntryNew.setStatus(1);
			//设置当前的测试用例未执行
			testEntryNew.getList().get(index).setCaseStatus(1);
		
			//设置为单个执行
			testEntryNew.setType(1);
			
			testEntryNew.setIndex(index);
			TestExecuteUtil.testEntry =testEntryNew;
//			}else{
				//用原有数据再创建线程
//				TestThread t2=new TestThread();
//				t2.setTestEntry(TestExecuteUtil.testEntry);
//				t2.setMonitorList(environmentService.findSelectMonitor(1));
				
				//EHCacheUtil.put("thread", t2);
//				TestExecuteUtil.t=t2;
//				t=t2;
				
				//
				EHCacheUtil.remove("r185");
				this.createTestResultNode(testEntryNew);
				
//				t.setTestSetupService(testSetupService);
//				t.setTestExecuteService(testExecuteService);
//				t.setPdfService(pdfService);
//			}
			//t.setName("单个测试用例线程-"+new Date().toLocaleString());
			//t.start();
			
			map.put("status", "y");
			
			TestCaseEntry tce = testEntryNew.getList().get(index);
			
			String userName = request.getSession().getAttribute("userName").toString();
            operationLogService.addOperationLog(userName, request, "执行测试", "成功", "模板名称:"+testEntryNew.getTempName() + ",用例名称:" + tce.getName());
			
		}catch(Exception e){
			log.error("单个测试用例线程执行异常"+e);
			map.put("success", true);
			map.put("status", "n");
			map.put("info", "添加失败");
			
			String userName = request.getSession().getAttribute("userName").toString();
            operationLogService.addOperationLog(userName, request, "执行测试", "失败", "单个测试用例线程执行异常");
		}
		return map;
	}
	//判断是否有线程开启
//	public void checkThread(){
//		//Object t=EHCacheUtil.get("thread");
//		//TestThread t=TestExecuteUtil.t;
//		if(t!=null){
//			//TestThread t=(TestThread)o;
//			if(TestExecuteUtil.testEntry.getStatus()==2 || TestExecuteUtil.testEntry.getStatus()==3 || TestExecuteUtil.testEntry.getStatus()==4){
//				throw new RuntimeException("已经有线程正在执行");
//			}
//		}
//	}
//	
	public TestEntry createThread(HttpServletRequest request){
		//保存数据的实体
		TestEntry testEntry=new TestEntry();
		
		//取得模板id
		String tempId_s=request.getParameter("tempId");
		//保存模板id
		int tempId=Integer.valueOf(tempId_s);
		testEntry.setTempId(tempId);
		
		//通过模板id查找模板，并缓存模板名称
		Map tm=testSetupService.findTemplateById(tempId);
		testEntry.setTempName( (String)tm.get("NAME"));
		
		//查询设备名称
		Map equipment=environmentService.findEnvironmentByEquipmentId("1");
		testEntry.setEquipName((String)equipment.get("NAME"));
		
		//查询设备选择了哪些监视器(id,name)
		//List<Map<String, Object>> monitorList=environmentService.findSelectMonitor(1);
		
		//设置测试模板执行的开始时间
		testEntry.setBeginTime(new Date());
		
		//通过模板id查找测试用例
		List<Map<String, Object>> list=testSetupService.findTestSuiteById(tempId);
		for(Map<String,Object> m:list){
			TestCaseEntry tce=new TestCaseEntry();
			tce.setId((Integer)m.get("ID"));
			tce.setName((String)m.get("NAME"));
			tce.setCode((String)m.get("CODE"));
			tce.setCaseStatus(1);
			tce.setType((Integer)m.get("TYPE"));
			tce.setInstalltype((Integer)m.get("INSTALLTYPE"));
			tce.setRemark(m.get("REMARK").toString());
			testEntry.getList().add(tce);
		}
		
		this.createTestResultNode(testEntry);
		
		return testEntry;
		//创建线程
		//TestThread t=new TestThread();
		//封装在线程中
//		TestExecuteUtil.testEntry = testEntry;
////		t.setTestEntry(testEntry);
//		t.setMonitorList(monitorList);
//		t.setTestSetupService(testSetupService);
//		t.setTestExecuteService(testExecuteService);
//		t.setPdfService(pdfService);
//		
//		//缓存线程对象
//		//EHCacheUtil.put("thread", t);
//		TestExecuteUtil.t=t;
//		return t;
	}
	
	public void createTestResultNode(TestEntry testEntry){
		//修改测试设备名称时  
		//点击自动检测
		//重启服务器后
		//删除最后父节点后
		if(TestEntry.first || EquipmentDef.check || EquipmentDef.update ||TestResultEntry.deleteStatus){
			/*if(TestEntry.first || EquipmentDef.check || EquipmentDef.update){
				//将旧的父节点生成pdf
				List<Map<String, Object>> lst=this.testResultService.findLastParent();
				if(lst!=null && lst.size()>0){
					String pdfpath = ConstantsDefs.TEST_FILE_PATH+lst.get(0).get("id");
					this.pdfService.createPdf((String)lst.get(0).get("id"), null, pdfpath);
				}
			}*/
			//向结果表中存入环境设置的设备信息部分信息
			Map<String, Object> mapEnv = (Map<String, Object>) this.testResultService.testDescribe().get(0);
			EquipmentEntry env = new EquipmentEntry();
			env.setName(mapEnv.get("NAME").toString());
			env.setVersion(mapEnv.get("VERSION").toString());
			env.setRemark(mapEnv.get("REMARK").toString());
			env.setIp((Long)mapEnv.get("IP"));
			env.setMac(mapEnv.get("MAC").toString());
			env.setIp2((Long)mapEnv.get("IP2"));
			env.setMac2(mapEnv.get("MAC2").toString());
			//向结果表中存入监视器信息
			List<Map<String, Object>> list=environmentService.findMonitorDetail(1);
			MonitorDetailEntry md = null;
			List<MonitorDetailEntry> listMoni = new ArrayList<MonitorDetailEntry>();
			for (Map<String, Object> m : list) {
				md = new MonitorDetailEntry();
				md.setId(((Integer)m.get("ID")).longValue());
				md.setOvertime(((Integer)m.get("OVERTIME")).intValue());
				md.setInput(((Integer)m.get("INPUT")).intValue());
				md.setCyclePeriod(((Integer)m.get("CYCLEPERIOD")).intValue());
				md.setAlarmLevel(((Integer)m.get("ALARMLEVEL")).intValue());
				md.setSelectstatus(((Integer)m.get("SELECTSTATUS")).intValue());
				listMoni.add(md);
			}
			
			//开始执行时，向测试结果表中加入数据，（父节点）
			String testResultId=this.testExecuteService.insertTestResult(DataUtils.formatDateTime()+"("+testEntry.getEquipName()+")",env,listMoni);
			testEntry.setTestResultId(testResultId);
			EHCacheUtil.put("testResultId", testResultId);
			
			
		}else{
			String testResultId=(String)EHCacheUtil.get("testResultId");
			testEntry.setTestResultId(testResultId);
		}
		if(TestEntry.first ){
			TestEntry.first=false;
		}
		if(EquipmentDef.check ){
			EquipmentDef.check=false;
		}
		if(EquipmentDef.update ){
			EquipmentDef.update=false;
		}
		if(TestResultEntry.deleteStatus ){
			TestResultEntry.deleteStatus=false;
		}
	}
	
	//测试开始,定时取得测试结果
	@ResponseBody  
	@RequestMapping("/findTest")
	public Map findTest(HttpServletRequest request){
		try{
			ServletContext context=request.getSession().getServletContext();
			Object tmpO = EHCacheUtil.get("CONNECT_ERROR");
			if(tmpO!=null && "10".equals(tmpO.toString())){
				log.error("测试失败，请检查设备或网络！");
				map.put("success", true);
				map.put("status", "n");
				map.put("info", "测试失败，请检查设备或网络！");
				map.put("errorNum", tmpO);
				EHCacheUtil.remove("CONNECT_ERROR");
				return map;
			}
			List<Map<String, Object>> monitorList=environmentService.findSelectMonitor(1);
			EHCacheUtil.put("monitorList", monitorList);
			//TestThread t=(TestThread) EHCacheUtil.get("thread");
			//TestThread t=TestExecuteUtil.t;
			if(TestExecuteUtil.testEntry!=null){
//				LogUtil.info("测试开始,定时取得测试结果=t不为空");
				//TestEntry testEntry=createThread(request);
				//TestExecuteUtil.testEntry =testEntry;
				int status = TestExecuteUtil.testEntry.getStatus();
				LogUtil.info("状态+"+TestExecuteUtil.testEntry.getStatus());
				map.put("status", "y");
				map.put("testEntry", TestExecuteUtil.testEntry);
				map.put("monitorList",monitorList);
				map.put("logProgressDisplay", EquipmentDef.logProgressDisplay);
				if(status == 4){
					Integer testStatus = statusMap.get("testStatus");
					if(testStatus == null ){
						testStatus = 0;
					}
					testStatus++;
					if(testStatus > maxNum){
						TestExecuteUtil.testEntry.setStatus(5);
						testStatus = null;	
					}
					statusMap.put("testStatus",testStatus);
				}
			}else{
				LogUtil.info("测试开始,定时取得测试结果testEntry为空");
			}
		}catch(Exception e){
			log.error("测试开始,定时取得测试结果异常",e);
			map.put("success", true);
			map.put("status", "n");
			map.put("info", "添加失败");
		}
		return map;
	}
	
	//测试暂停
//	@ResponseBody  
//	@RequestMapping("/pauseTest")
//	public Map pauseTest(HttpServletRequest request){
//		try{
//			ServletContext context=request.getSession().getServletContext();
//			//状态：：1未执行，2执行中，3暂停，4完成，5停止
//			//TestThread t=(TestThread) EHCacheUtil.get("thread");
//			TestThread t=TestExecuteUtil.t;
//			TestExecuteUtil.testEntry.setStatus(3);
//			
//			//执行单个用例时，更改单个用例状态
//			String s_index=request.getParameter("index");
//			if(s_index!=null){
//				int index=Integer.valueOf(s_index);
//				TestExecuteUtil.testEntry.getList().get(index).setCaseStatus(3);
//			}
//			
//			map.put("status", "y");
//			
//		}catch(Exception e){
//			log.error("测试暂停异常",e);
//			map.put("success", true);
//			map.put("status", "n");
//			map.put("info", "添加失败");
//		}
//		return map;
//		
//	}
	//测试暂停改变成继续
	@ResponseBody  
	@RequestMapping("/continueTest")
	public Map continueTest(HttpServletRequest request){
		try{
			ServletContext context=request.getSession().getServletContext();
			//状态：：1未执行，2执行中，3暂停，4完成，5停止
			//TestThread t=(TestThread) EHCacheUtil.get("thread");
			TestThread t=TestExecuteUtil.t;
			TestExecuteUtil.testEntry.setStatus(2);
			
			//执行单个用例时，更改单个用例状态
			String s_index=request.getParameter("index");
			if(s_index!=null){
				int index=Integer.valueOf(s_index);
				TestExecuteUtil.testEntry.getList().get(index).setCaseStatus(2);
			}
			
			synchronized(t){
				t.notifyAll();
			}
			
			//@执行暂停测试用例
			SealedSendMessage.getContinueTest();
			log.info("测试继续了++++++++++");
			
			map.put("status", "y");
			
		}catch(Exception e){
			log.error("测试继续异常",e);
			map.put("success", true);
			map.put("status", "n");
			map.put("info", "添加失败");
		}
		return map;
		
	}
	//测试停止
	@ResponseBody  
	@RequestMapping("/stopTest")
	public Map stopTest(HttpServletRequest request){
		//TestThread t=null;
		//TestThread t=TestExecuteUtil.t;
		try{
			log.info("用户点击了测试停止按钮");
			
			//t=(TestThread) EHCacheUtil.get("thread");
			
			//状态：：1未执行，2执行中，3暂停，4完成，5停止	
//			if(TestExecuteUtil.testEntry.getStatus()==3){
//				synchronized(t){
//					t.notifyAll();
//				}
//			} 
			TestExecuteUtil.testEntry.setStatus(4);
															
			if(null!=TestExecuteUtil.testEntry.next()){
				System.out.println("测试停止，保存用例，tempId="+TestExecuteUtil.testEntry.getTestResultId()+" caseId"+TestExecuteUtil.testEntry.next().getTestResultId());
				//保存测试用例执行时间段
				testExecuteService.inertTestCaseLog(TestExecuteUtil.testEntry.getTestResultId(), TestExecuteUtil.testEntry.next().getTestResultId(), TestExecuteUtil.testEntry.next().getBeginTime());
			}
			System.out.println("测试停止，保存模板，tempId="+TestExecuteUtil.testEntry.getTestResultId());
			//保存模板执行时间段
			testExecuteService.inertTestTemplateLog(TestExecuteUtil.testEntry.getTestResultId(), TestExecuteUtil.testEntry.getBeginTime());
			
			
			//将图形表中的数据加入到新图形表中
			//t.copyChart();
			
			//将所有测试用例都改成停止
			List<TestCaseEntry> list=TestExecuteUtil.testEntry.getList();
			//将正在运行的和暂停的修改成停止
			for(int i=0;i<list.size();i++){
				//if(list.get(i).getCaseStatus()==2 || list.get(i).getCaseStatus()==3){
					list.get(i).setCaseStatus(4);
				//}
			}
			
			map.put("status", "y");
			
			String userName = request.getSession().getAttribute("userName").toString();
            operationLogService.addOperationLog(userName, request, "停止测试", "成功", "--");
			
		}catch(Exception e){
			log.error("测试停止异常",e);
			map.put("success", true);
			map.put("status", "n");
			map.put("info", "添加失败");
			//if(t!=null){
				TestExecuteUtil.testEntry.setStatus(4);
			//}
				
			String userName = request.getSession().getAttribute("userName").toString();
            operationLogService.addOperationLog(userName, request, "停止测试", "失败", "测试停止异常");
		}
		//@执行停止测试用例
		SealedSendMessage.getStopTest();
		
		//将
		/*if(null!=TestExecuteUtil.testEntry.next()){
			//pdfService.createPdf(TestExecuteUtil.testEntry.next().getTestResultId());
		}*/
//		EHCacheUtil.put("r185", "101");//zgh调试用
//		TestExecuteUtil.testEntry.setStatus(5);
		return map;
		
	}
	
	
	
	@ResponseBody  
	@RequestMapping("/clearLog")
	public Map clearLog(HttpServletRequest request){
		try{
			testExecuteService.clearLog();
			map.put("status", "y");
			
			String userName = request.getSession().getAttribute("userName").toString();
            operationLogService.addOperationLog(userName, request, "清空测试日志", "成功", "--");
			
		}catch(Exception e){
			log.error("清空事件日志异常",e);
			map.put("status", "n");
			String userName = request.getSession().getAttribute("userName").toString();
            operationLogService.addOperationLog(userName, request, "清空测试日志", "失败", "清空事件日志异常");
		}
		return map;
	}
	
	@ResponseBody  
	@RequestMapping("/test")
	public void test(HttpServletRequest request){
		
			//TestThread t=(TestThread) EHCacheUtil.get("thread");
			TestThread t=TestExecuteUtil.t;
			log.info(t.isAlive());
	
	}
	
	//将map复制
	public Map<String, Object> copy(Map<String, Object> src){
		Map<String, Object> dist=new HashMap<String,Object>();
		dist.putAll(src);
		return dist;
	}
}
