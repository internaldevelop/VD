package com.wnt.web.testexecute.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import net.sf.ehcache.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.wnt.core.ehcache.EHCacheUtil;

import com.itextpdf.text.log.SysoLogger;
import com.wnt.server.order.ConstantsServer;
import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.environment.service.EnvironmentService;
import com.wnt.web.socket.service.SocketService;
import com.wnt.web.system.entry.Sys;
import com.wnt.web.testexecute.entry.TestCaseEntry;
import com.wnt.web.testexecute.entry.TestEntry;
import com.wnt.web.testexecute.service.TestExecuteService;
import com.wnt.web.testresult.service.PdfService;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL_CUSTOM;
import com.wnt.web.testsetup.service.TestSetupService;

import common.TestExecuteUtil;

public class TestThread extends Thread implements Serializable {
	// private testEntry testEntry;
	private transient TestSetupService testSetupService;
	private transient TestExecuteService testExecuteService;
	private transient PdfService pdfService;
	private List<Map<String, Object>> monitorList=null;
	/** 
	 * @Title:TestThreadNew
	 * @Description:TODO 
	 * @param testSetupService2
	 * @param testExecuteService2
	 * @param pdfService2 
	 */
	public TestThread(TestSetupService testSetupService,
			TestExecuteService testExecuteService, PdfService pdfService) {
		this.testSetupService = testSetupService;
		this.testExecuteService = testExecuteService;
		this.pdfService = pdfService;
	}

	public static int pdf_create_wait = 10;
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss|SSS");
	
	// 选择了哪些监视器
	
	private final transient Logger log = Logger.getLogger(TestThread.class.getName());
	
	private ConcurrentMap<String,Integer> actualProgress = new ConcurrentHashMap<String,Integer>();
	
	// @测试
	int n = 0;
	public void run() {
		synchronized (this) {
			while (true) {
				//log.info("线程进度=====："+simpleDateFormat.format(new Date()));
				//存储待执行用例的list
				 List<TestCaseEntry> doTestList = new ArrayList<TestCaseEntry>();
				 TestEntry testEntry =TestExecuteUtil.testEntry;
			     if(testEntry!=null && testEntry.getStatus()==1){
			    	 //log.info("线程进度=====：" + EHCacheUtil.get("r185")+"  "+simpleDateFormat.format(new Date()) +"  "+"index:"+testEntry.getIndex());
			    	 monitorList= (List<Map<String, Object>>) EHCacheUtil.get("monitorList");
			    	 //如果是多个用例
			    	 if(testEntry.getType() ==2){	 
			    		 doTestList = testEntry.getList();
			    	 }
			    	 //如果是单个用例
			    	 else{
			    		 //获得要执行的单个用例在testentry中的index,根据index找到这个条要执行的用例
			    		 int index = testEntry.getIndex();
			    		 TestCaseEntry testCaseEntry1 = testEntry.getList().get(index);
			    		 //封装到预置存储测试用例的list中
			    		 doTestList.add(testCaseEntry1);
			    	 }
				    testEntry.setStatus(2);
			    	 //遍历测试用list  
				     for (int i =0;i<doTestList.size();i++) {
				    	 TestCaseEntry testCaseEntry =doTestList.get(i);
				    	 log.info(testCaseEntry);
				    	//执行下发命令
				    	 log.info("下发开始执行：" + testCaseEntry);
//				    	 if(testEntry.getStatus()==1){ 
				    		 startTest(testCaseEntry);
				    		 testCaseEntry.setCaseStatus(2);
//				    	 }
			    		int  testProgress = getProgress();
			    		testEntry.setIndex(i);
						while(testProgress <=104){
							//log.info("线程进度=====：" + testProgress+"  "+simpleDateFormat.format(new Date()) +"  "+"index:"+testEntry.getIndex());
							testProgress = getProgress();
							if(testProgress < 101){
								actualProgress.put(testEntry.getTestResultId()+"_"+testCaseEntry.getId(), testProgress);
							}
								this.findMonitorData(testCaseEntry);
								testEntry.setProgress(Integer.valueOf(testProgress));
								if (100==testProgress) {
									// 测试完成了
									testEntry.setProgress(100);
								} else if (101==testProgress) {
									do101Progress(testEntry,testCaseEntry);
									EHCacheUtil.remove("r185");
									break;
								}else if(102==testProgress){
									testEntry.setProgress(102);
							    }else if(103==testProgress){
									testEntry.setProgress(103);
								}else if(104==testProgress){
									do104Progress(testEntry,testCaseEntry);
									EHCacheUtil.remove("r185");
									break;
								}
								try {
									sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						if(104==testProgress)
						{
							break;
						}
				     }
				     testEntry.setStatus(5);
			     }
			     try {
			    	 sleep(1000);
			     } catch (InterruptedException e) { 	 
			    	e.printStackTrace(); 
			     }
			}
		}


	}
//  实时获得返回进度
	/**
	 * 
	 * @Title: getProgress 
	 * @Description: TODO
	 * @return
	 * @return: int
	 */
	private int getProgress(){
		 int  testProgress = 0;
		 if (EHCacheUtil.get("r185") != null) {
			 testProgress = Integer.valueOf(EHCacheUtil.get("r185").toString());
			 
		 }
		 return testProgress;
	}
	/** 
	 * @Title: do101Progress 
	 * @Description: TODO
	 * @param testEntry
	 * @param testCaseEntry
	 * @return
	 * @return: boolean
	 */
	private boolean do101Progress(TestEntry testEntry, TestCaseEntry testCaseEntry){
		System.out.println("101停止，保存用例，tempId="
				+ testEntry.getTestResultId()
				+ " caseId"
				+ testEntry.next()
						.getTestResultId());
		//同步测试结果中进度条
		actualProgress.put(testEntry.getTestResultId()+"_"+testCaseEntry.getId(), 100);
		// 保存测试用例执行时间段
		testExecuteService
				.inertTestCaseLog(testEntry
						.getTestResultId(),
						testCaseEntry
								.getTestResultId(),
								testCaseEntry
								.getBeginTime());
		//更新测试结果中的进度
		testExecuteService.updateProgress(testEntry.getTestResultId(),
				testCaseEntry.getId(),
		actualProgress.get(testEntry.getTestResultId()+"_"+testCaseEntry.getId()));
		
		log.info("进度："+ testEntry.getProgress());
		log.info("线程状态："+ testEntry.getStatus());
		// 设置当前测试用例结束
		testCaseEntry.end();
		
		log.info(testEntry.getTestResultId()+ "开始生成pdf");
		
		//这里的while 用于等待 pdf_create_wait 时长 后执行生成pdf 问题  用于解决pdf 生成时没有异常
		/*try {
			sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pdfService.createPdf(testEntry.next().getTestResultId());*/
		// 当前测试结束了
		testEntry.setProgress(101);
		// 设置测试用例状态为完成
		testCaseEntry.end();
		return true;
	}
	/** 
	 * @Title: do104Progress 
	 * @Description: TODO
	 * @param testEntry
	 * @param testCaseEntry
	 * @return
	 * @return: boolean
	 */
	private boolean do104Progress(TestEntry testEntry,
			TestCaseEntry testCaseEntry) {
		log.info("状态："
				+ testEntry.getStatus());
		//copyChart();
		System.out.println("101停止，保存用例，tempId="
				+ testEntry.getTestResultId()
				+ " caseId"
				+ testEntry.next()
						.getTestResultId());
		log.info("进度："
				+ testEntry.getProgress());
		//设置当前用例结束
		//testCaseEntry.end();
		//给前台返回测试进度  调整对应结束样式
		testEntry.setProgress(104);
		List<TestCaseEntry> lst = testEntry
				.getList();
		for (TestCaseEntry tce : lst) {
			tce.setCaseStatus(1);
		}
		//更新测试结果中的进度
		testExecuteService.updateProgress(testEntry.getTestResultId(),
			testCaseEntry.getId(),
			actualProgress.get(testEntry.getTestResultId()+"_"+testCaseEntry.getId()));
		//testEntry.setProgress(0);
		testEntry.setStatus(5);

		// 保存测试用例执行时间段
		testExecuteService.inertTestCaseLog(testEntry
						.getTestResultId(),
						testEntry.next()
								.getTestResultId(),
						testEntry.next()
								.getBeginTime());
		/*
		//这里的while 用于等待 pdf_create_wait 时长 后执行pdf 生成，   用于解决pdf 生成时没有异常
		try {
			sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info(testEntry.getTestResultId()
				+ "开始生成pdf");
		pdfService.createPdf(testCaseEntry.getTestResultId());*/
	
		return true;
		
	}
	// 执行测试用例
	public synchronized  void  startTest(TestCaseEntry tce) {
		// 开始执行时，向测试结果表中插入数据	    
		String testResultId = this.testExecuteService.insertTestCaseResult(
				TestExecuteUtil.testEntry.getTestResultId(), tce.getId(),
				tce.getName(), tce.getCode(), tce.getInstalltype(),
				tce.getRemark());

		tce.setTestResultId(testResultId);

		// 单个执行，先清除监视器数据
		if (TestExecuteUtil.testEntry.getType() == 1) {
			// 清除监视器数据
			tce.getMonitorData().clear();
		}
		// 设置这个测试用例正在运行中
		tce.start();
		// 创建这个测试用例开始的时间
		tce.setBeginTime(new Date());

		// findTestSuiteDetail根据测试用例的id查找测试用例的详细信息
		// Map<String, Object> m =
		// testSetupService.findTestSuiteDetailAndType(tce
		// .getId());
		// // 1为风暴测试 2为语法测试
		// if ("1".equals(m.get("INSTALLTYPE").toString())) {
		// SealedSendMessage.getTormSetup(
		// ((Long) m.get("TESTRATE")).intValue(),
		// (Long) m.get("TESTTIME"), "");
		// } else {
		// long First_subtest = isLong(m.get("STARTTESTCASE"));// 起始测试用例
		// long Last_subtest = isLong(m.get("ENDTESTCASE"));// 终止测试用例
		// int Fault_isolation = isInt(m.get("TRACEABILITY"));// 问题溯源
		// int Packet_cap = isInt(m.get("HURRYUP"));// 抓包
		// int Target_dev = isInt(m.get("TARGET"));// 目标
		// if (Packet_cap == 1) {// 总是抓包
		// SealedSendMessage.getGrammarSetup(First_subtest, Last_subtest,
		// Fault_isolation, Packet_cap, Target_dev, testResultId);
		// } else {// 从不抓包
		// SealedSendMessage.getGrammarSetup(First_subtest, Last_subtest,
		// Fault_isolation, Packet_cap, Target_dev, "");
		// }
		//
		// }

		Map ltdm = null;
		long First_subtest = 0l;// 起始测试用例
		long Last_subtest = 0l;// 终止测试用例
		int Fault_isolation = 0;// 问题溯源
		int Packet_cap = 0;// 抓包
		int Target_dev = 0;// 目标
		int Power_m = 0; // 电源管理
		long Packet_total = 0;// 发包数
		int port = 0;
		int rate = 0;
		Number raten = null;// 速率
		System.out.println(tce);
		System.out.println(tce.getId()+" id");
		switch (tce.getInstalltype()) {
		case 1:
			ltdm = testSetupService.findDetailList(tce.getId());
			if (ltdm == null)
				break;
			testSetupService.insertDetailResult(ltdm, testResultId);// 用于结果使用 
			raten = (Number) ltdm.get("TESTRATE");
			rate = raten.intValue();
			long time = Long.valueOf(String.valueOf(ltdm.get("TESTTIME")));
			Power_m = (Integer) ltdm.get("POWER");
			SealedSendMessage.getTormSetup(rate, time, "",Power_m);
			SealedSendMessage.getStartTest(tce.getCode1(), tce.getCode2());
			break;
		case 2:
			ltdm = testSetupService.findDetailList(tce.getId());
			if (ltdm == null)
				break;
			try {
				testSetupService.insertDetailResult(ltdm, testResultId);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			First_subtest = (Long) ltdm.get("STARTTESTCASE");// 起始测试用例
//			Last_subtest = (Long) ltdm.get("ENDTESTCASE");// 终止测试用例
			First_subtest = Long.parseLong(ltdm.get("STARTTESTCASE").toString());
			Last_subtest = Long.parseLong(ltdm.get("ENDTESTCASE").toString());
			if (First_subtest ==0 && Last_subtest == 0 ) {
				First_subtest =1;
				Last_subtest = -1;
			}else if(Last_subtest ==0){
				Last_subtest = -1;
			}else if(First_subtest ==0){
				First_subtest=1;
			}
			if(null!=ltdm.get("TESTRATE")){
				raten = (Number) ltdm.get("TESTRATE");
			}else{
				raten =0;
			}
			rate = raten.intValue();
			Fault_isolation = (Integer) ltdm.get("TRACEABILITY");// 问题溯源
			Packet_cap = (Integer) ltdm.get("HURRYUP");// 抓包
			Target_dev = (Integer) ltdm.get("TARGET");// 目标
			Power_m = (Integer) ltdm.get("POWER");
			String remark = (String) ltdm.get("REMARK");
			if((remark.contains("TCP") && !remark.contains("TCP最大连接数测试"))||remark.contains("UDP")||remark.contains("MMS")||remark.contains("IEC 104")||
			   remark.contains("Modbus")||remark.contains("PROFINET CL_RPC 语法测试")||remark.contains("PROFINET IO连接请求语法测试"))
			{
				port = (Integer) ltdm.get("PORTSEND");
			}else{
				port=0;
			}

			if (Packet_cap == 1) {// 总是抓包
				SealedSendMessage.getGrammarSetup(First_subtest, Last_subtest,
						rate, 0, 0, Fault_isolation, Packet_cap, Target_dev,
						Power_m, testResultId, port);
			} else {// 从不抓包
				SealedSendMessage.getGrammarSetup(First_subtest, Last_subtest,
						rate, 0, 0, Fault_isolation, Packet_cap, Target_dev,
						Power_m, "", port);
			}
			SealedSendMessage.getStartTest(tce.getCode1(), tce.getCode2());
			break;
		case 3:
			List<Map<String, Object>> ltcl = testSetupService
					.findDetailcustomList(tce.getId());
			testSetupService.insertResultCustom(ltcl, testResultId);

			ltdm = testSetupService.findDetailList(tce.getId());
			if (ltdm == null)
				break;
			try {
				testSetupService.insertDetailResult(ltdm, testResultId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Packet_total = Long.parseLong(ltdm.get("SMESSAGE").toString());
			raten = (Number) ltdm.get("TESTRATE");
			rate = raten.intValue();

			Fault_isolation = (Integer) ltdm.get("TRACEABILITY");// 问题溯源
			Packet_cap = (Integer) ltdm.get("HURRYUP");// 抓包
			Target_dev = (Integer) ltdm.get("TARGET");// 目标
			Power_m = (Integer) ltdm.get("POWER");
			port = (Integer) ltdm.get("PORTSEND");

//			long stotal = Long.parseLong(ltdm.get("STOTAL").toString());
			if (ltcl.size() > 0) {
				SealedSendMessage.getCustom(tce.getName(), 0, ltcl);
			}

			if (Packet_cap == 1) {// 总是抓包
				SealedSendMessage.getGrammarSetup(First_subtest, Last_subtest,
						rate, 0, Packet_total, Fault_isolation, Packet_cap,
						Target_dev, Power_m, testResultId, port);
			} else {
				SealedSendMessage.getGrammarSetup(First_subtest, Last_subtest,
						rate, 0, Packet_total, Fault_isolation, Packet_cap,
						Target_dev, Power_m, testResultId, port);
			}
			SealedSendMessage.getStartTest(tce.getCode1(), tce.getCode2());
			break;
		case 4:
			ltdm = testSetupService.findDetailList(tce.getId());
			if (ltdm == null)
				break;
			try {
				testSetupService.insertDetailResult(ltdm, testResultId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// First_subtest = (Long) ltdm.get("STARTTESTCASE");// 起始测试用例
			// Last_subtest = (Long) ltdm.get("ENDTESTCASE");// 终止测试用例
			Fault_isolation = (Integer) ltdm.get("TRACEABILITY");// 问题溯源
			Packet_cap = (Integer) ltdm.get("HURRYUP");// 抓包
			Target_dev = (Integer) ltdm.get("TARGET");// 目标
			Power_m = (Integer) ltdm.get("POWER");
			Packet_total = Long.parseLong(ltdm.get("SMESSAGE").toString());
			long Percent = Long.parseLong(ltdm.get("EMESSAGEP").toString());
			if(ltdm.get("REMARK").toString().contains("TCP Fuzzer测试")||ltdm.get("REMARK").toString().contains("UDP Fuzzer测试")){
				port = (Integer) ltdm.get("PORTSEND");
			}else{
				port =0;
			}
			Number raten1 = (Number) ltdm.get("TESTRATE");
			rate = raten1.intValue();

			if (Packet_cap == 1) {// 总是抓包
				SealedSendMessage.getGrammarSetup(First_subtest, Last_subtest,
						rate, Percent, Packet_total, Fault_isolation,
						Packet_cap, Target_dev, Power_m, testResultId, port);
			} else {// 从不抓包
				SealedSendMessage.getGrammarSetup(First_subtest, Last_subtest,
						rate, Percent, Packet_total, Fault_isolation,
						Packet_cap, Target_dev, Power_m, "", port);
			}
			SealedSendMessage.getStartTest(tce.getCode1(), tce.getCode2());
			break;
		case 5:
			ltdm = testSetupService.findDetailList(tce.getId());
			if (ltdm == null)
				break;
			try {
				testSetupService.insertDetailResult(ltdm, testResultId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			First_subtest = Long.parseLong(ltdm.get("STARTTESTCASE").toString());
			Last_subtest = Long.parseLong(ltdm.get("ENDTESTCASE").toString());
			Fault_isolation = (Integer) ltdm.get("TRACEABILITY");// 问题溯源
			Packet_cap = (Integer) ltdm.get("HURRYUP");// 抓包
			Target_dev = (Integer) ltdm.get("TARGET");// 目标
			Power_m = (Integer) ltdm.get("POWER");
			port = (Integer) ltdm.get("PORTSEND");
			if (Packet_cap == 1) {// 总是抓包
				SealedSendMessage.getGrammarSetup(First_subtest, Last_subtest,
						0, 0, 0, Fault_isolation, Packet_cap, Target_dev,
						Power_m, testResultId, port);
			} else {// 从不抓包
				SealedSendMessage.getGrammarSetup(First_subtest, Last_subtest,
						0, 0, 0, Fault_isolation, Packet_cap, Target_dev,
						Power_m, "", port);
			}
			SealedSendMessage.getStartTest(tce.getCode1(), tce.getCode2());
			break;
		}
		//SealedSendMessage.getStartTest(tce.getCode1(), tce.getCode2());
	}

	// 查找测试用例的监视器数据(页面中每个测试用例后都有监视器状态)
	public void findMonitorData(TestCaseEntry tce) {
		tce.getMonitorData().clear();
		// 循环监视器
		for (Map<String, Object> m : monitorList) {
			String data = testExecuteService.findLogByMonitor(tce.getCode(), m
					.get("ID").toString(), tce.getBeginTime());
			if (data == null) {
				data = "1";
			}
			tce.getMonitorData().put(m.get("ID").toString(), data);
		}
	}

	// 将图形表中的数据加入到新图形表中
//	public void copyChart() {
//		this.testExecuteService.copyChart(TestExecuteUtil.testEntry.next()
//				.getBeginTime(), new Date(), TestExecuteUtil.testEntry.next()
//				.getTestResultId());
//	}

	public Integer isInt(Object o) {
		if (o instanceof Integer) {
			return (Integer) o;
		}
		return 0;
	}

	public Long isLong(Object o) {
		if (o instanceof Long) {
			return (Long) o;
		}
		return 0L;
	}

	// public testEntry getTestEntry() {
	// return testEntry;
	// }
	//
	// public void setTestEntry(testEntry
	// testEntry) {
	// this.testEntry = testEntry;
	// }

	public TestSetupService getTestSetupService() {
		return testSetupService;
	}

	public void setTestSetupService(TestSetupService testSetupService) {
		this.testSetupService = testSetupService;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public List<Map<String, Object>> getMonitorList() {
		return monitorList;
	}

	public void setMonitorList(List<Map<String, Object>> monitorList) {
		this.monitorList = monitorList;
	}

	public TestExecuteService getTestExecuteService() {
		return testExecuteService;
	}

	public void setTestExecuteService(TestExecuteService testExecuteService) {
		this.testExecuteService = testExecuteService;
	}

	public PdfService getPdfService() {
		return pdfService;
	}

	public void setPdfService(PdfService pdfService) {
		this.pdfService = pdfService;
	}

}
