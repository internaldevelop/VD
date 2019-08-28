package com.wnt.web.testresult.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.LogUtil;
import org.wnt.core.uitl.SocketEntityUtil;
import org.wnt.core.uitl.SystemPath;
import org.wnt.core.uitl.UUIDGenerator;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.environment.service.EnvironmentService;
import com.wnt.web.portscan.entry.PortServerEntry;
import com.wnt.web.testexecute.controller.TestExecuteController;
import com.wnt.web.testexecute.service.TestExecuteService;
import com.wnt.web.testresult.service.PdfService;
import com.wnt.web.testresult.service.TestResultService;
import com.wnt.web.testresult.util.WriterPdfs;

import common.ConstantsDefs;
import common.SystemFlagUtil;
import common.TestExecuteUtil;
import jodd.util.StringUtil;

@Service("pdfService")
public class PdfServiceImpl implements PdfService {

	private final Logger log = Logger.getLogger(PdfServiceImpl.class.getName());
	@Resource
	TestExecuteService testExecuteService;
	@Resource
	TestResultService testResultService;

	@Resource
	EnvironmentService environmentService;

	WriterPdfs pdf = new WriterPdfs();

	@Override
	public String createPdf(String testResultId, HttpServletResponse response, String pdfpath) throws Exception {

		try {
			if(pdfpath == null){
				pdfpath  = ConstantsDefs.TEST_FILE_PATH + testResultId;
				// 判断系统
				if (SystemFlagUtil.isWindows()) {
					String sysPath = SystemPath.getSysPath();
					pdfpath = sysPath + "pdf/testsetting/";
					// 没有路径创建路径
					File filePath = new File(pdfpath);
					if (!filePath.exists()) {
						filePath.mkdirs();
					}
					pdfpath += testResultId;
					pdfpath = pdfpath.replace("\\", "/");
				}
			}
			
//			Map<String, Object> map = (Map<String, Object>) this.testResultService.testDescribe().get(0);
//			EquipmentEntry env = new EquipmentEntry();
//			env.setName(isNull(map.get("NAME").toString()));
//			env.setVersion(isNull(map.get("VERSION").toString()));
//			env.setRemark(isNull(map.get("REMARK").toString()));
//			testResultService.updateTestResult(testResultId,env);
			//LogUtil.info("生成父pdf的id：" + testResultId);
			// 生成文档
			Document document = pdf.buildDocument(pdfpath);
			// response.setContentType("application/pdf");
			// response.addHeader("Content-Disposition",
			// "attachment;filename=" + URLEncoder.encode("测试报告.pdf","UTF-8"));
			PdfWriter.getInstance(document, new FileOutputStream(pdfpath));
			// 打开文档
			document.open();
			// 1 测试概述
			part1_2(document, testResultId);
			part3(document,testResultId);
			// part4(document, testResultId);
			part5(document, testResultId);

			document.close();
			
		} catch (Exception e) {
			LogUtil.info("生成pdf异常", e);
			throw e;
		}
		return pdfpath;
	}

	public void part1_2(Document document, String testResultId) throws DocumentException, IOException {
		// 1_1 测试概述
		pdf.buildTile(document, "1  测试概述 ");
		// 1_2 表格
		// 目标 描述
		// 测试平台： 漏洞挖掘测试平台 Version 001 R 001
		// 被测设备： ${part1_name}
		// 设备型号： ${part1_remark}
		// 测试时间： ${part1_sdate} 到 ${part1_edate}
		// 报告生成： ${part1_pdate}

		float[] widths = { 20f, 75f };
		String[] head = { "目标", "描述" };

		// NAME,VERSION,REMARK,IP,MAC,IP2,MAC2 
//		Map<String, Object> map = (Map<String, Object>) this.testResultService.testDescribe().get(0);
		Map<String, Object> map = (Map<String, Object>)this.testResultService.queryEquipmentDetailInResult(testResultId);
		Map<String, Object> map2 = (Map<String, Object>) this.testResultService.findResultTime(testResultId);

		List<Map<String, Object>> list = testResultService.findTestCaseResulttp5(testResultId);

		Map<String, Object> m = list.get(0);

		String str = isNull(m.get("EQUIPMENTNAME"));
		if (str.contains("(") && str.contains("")) {
			str = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
		}
		// String version =isNull(map.get("VERSION"));
		String name ="";
		String version = "";
		String remark = "";
		Timestamp  beginTime = null;
		Timestamp  endTime = null;
		if (null != map.get("NAME")) {
			name = map.get("NAME").toString();
		}
		if (null != map.get("VERSION")) {
			version = map.get("VERSION").toString();
		}
		if (null != map.get("EREMARK")) {
			remark = map.get("EREMARK").toString();
		}
		if (null != map2.get("BEGINTIME")) {
			beginTime = Timestamp.valueOf(map2.get("BEGINTIME").toString());
		}
		if (null != map2.get("ENDTIME")) {
			endTime = Timestamp.valueOf(map2.get("ENDTIME").toString());
		}
		//{ "测试平台：", "漏洞挖掘测试平台 Version 001 R 001" }
		String[][] content = { { "测试平台：", "通信规约模糊测试工具" }, { "被测设备：", name }, { "设备型号：", version },
				{ "备注信息：", remark }, { "测试时间：", datetime(beginTime) + "到" + datetime(endTime) },
				{ "报告生成：", DataUtils.formatDateTime() } };

		pdf.buildTable(document, widths, head, content);

		// 2_1 测试环境

		pdf.buildTile(document, "2  测试环境 ");
		// 1_2 表格
		// 域 Network1 Network2
		// CRT IP ${part2_ip1} None
		// CRT MAC ${part2_mac1} None
		// DUT IP ${part2_ip2} None
		// DUT MAC ${part2_mac2} None

		float[] widths2 = { 25f, 75f };
		String[] head2 = { "域", "Network" };
		String[][] content2 = { { "CRT IP", ip(map.get("IP")) }, { "CRT MAC", map.get("MAC").toString() },
				{ "DUT IP", ip(map.get("IP2")) }, { "DUT MAC", map.get("MAC2").toString() } };

		pdf.buildTable(document, widths2, head2, content2);
	}

	public void part3(Document document,String testResultId) throws DocumentException, IOException {
		// 3_1 测试方案
		pdf.buildTile(document, "3  测试方案 ");
		// 3_2 表格

		float[] widths = { 25f, 30f, 50f };
		String[] head = { "目标", "目的", "方案" };

		String[][] content = {
				{ "TCP/IP协议栈", "验证被测PLC的网络协议栈的安全性和健壮性",
						"通过向工控设备发送正确、错误、和畸形的TCP/IP报文来验证被测工控设备对TCP/IP协议栈的实现是否存在安全性和健壮性问题，通过测试仪的监视窗口来监视网络异常，从而判定在测试过程中被测工控设备是否存在网络安全性问题。" },
				{ "工控协议", "验证对工控协议实现的安全性和健壮性",
						"通过向PLC发送非正常工控协议命令字和工控协议畸形报文来观察对工控设备的控制逻辑是否有影响，并且还要观察这种报文对网络协议栈的安全性是否有影响。我们通过D/O输出和网络监控来判定在测试过程中是否出现了逻辑问题或者是网络问题" } };

		pdf.buildTable(document, widths, head, content);

		pdf.buildTile2(document, "端口扫描 ");
		pdf.buildTile3(document, "开启的TCP Port");

		// 端口 服务 来源
		// 502 ${service} 主动扫描
		// 11 Unknow 手动添加
		float[] widths2 = { 25f, 50f, 30f };
		String[] head2 = { "端口", "服务", "来源" };
		// PORTNUM ,NAME as NAME1,SOURCE
		List<Map<String, Object>> list = testResultService.queryPortserverList(6);
		String[][] c = new String[list.size()][3];
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> m = list.get(i);
			c[i][0] = isNull(m.get("PORTNUM"));
			c[i][1] = isNull(m.get("NAME1"));
			c[i][2] = PortServerEntry.getSourceStr(isNull(m.get("SOURCE")));
		}
		pdf.buildTable(document, widths2, head2, c);

		pdf.buildTile3(document, "开启的UDP Port");

		// 端口 服务 来源
		// 502 ${service} 主动扫描
		// 11 Unknow 手动添加
		// PORTNUM ,NAME as NAME1,SOURCE
		List<Map<String, Object>> list2 = testResultService.queryPortserverList(17);
		String[][] c2 = new String[list2.size()][3];
		for (int i = 0; i < list2.size(); i++) {
			Map<String, Object> m = list2.get(i);
			c2[i][0] = isNull(m.get("PORTNUM"));
			c2[i][1] = isNull(m.get("NAME1"));
			c2[i][2] = PortServerEntry.getSourceStr(isNull(m.get("SOURCE")));
		}
		pdf.buildTable(document, widths2, head2, c2);

		// 监控方法
		pdf.buildTile2(document, "监控方法");
		// LM.ID,LMD.CYCLEPERIOD, LMD.INPUT, LMD.ALARMLEVEL, LMD.TCPPORTS,
		// LMD.OVERTIME
		//List<Map<String, Object>> list3 = testResultService.queryMonitor();
		Map<String, Object> map = (Map<String, Object>)this.testResultService.queryEquipmentDetailInResult(testResultId);
		float[] widths3 = { 30f, 65f };
		String[] head3 = { "参数", "值" };
		//for (int i = 0; i < list3.size(); i++) {
			//Map<String, Object> m = list3.get(i);
			if ("1".equals(map.get("MID1").toString()) && "1".equals(map.get("SELECTSTATUS1").toString())) {
				// ARP
				pdf.buildTile3(document, "ARP Monitor");
				String s = "ARP监视器利用ARP Request/Response消息来确定被测设备网络协议栈是否可用。每个ARP Request大约每秒发送一次。通过获取回应报文来绘制监控图。如果长期没有ARP Response，那么就发送告警日志";
				pdf.buildString(document, s);
				String[][] c3 = { { "TimeOut（毫秒）", isNull(map.get("OVERTIME1")) } };
				pdf.buildTable(document, widths3, head3, c3);

			} 
			if ("2".equals(map.get("MID2").toString()) && "1".equals(map.get("SELECTSTATUS2").toString())) {
				// ICMP
				pdf.buildTile3(document, "ICMP Monitor");
				String s = "ICMP监视器利用ICMP Echo Request/Response消息去确定被测设备的网络协议栈是否可用。按照周期发送ICMP Echo Request。";
				pdf.buildString(document, s);
				String[][] c3 = { { "请求TimeOut（毫秒）", isNull(map.get("OVERTIME2")) }};
				pdf.buildTable(document, widths3, head3, c3);

			} 
			if ("3".equals(map.get("MID3").toString()) && "1".equals(map.get("SELECTSTATUS3").toString())) {
				// TCP
				pdf.buildTile3(document, "TCP Ports Monitor");
				String s = "TCP端口监视器获得指定的TCP端口，确定这些被测设备端口是开启还是关闭。此监视器每秒确定一次这些端口的状态。用TCP连接每个开启的端口，但不发送任何数据就关闭连接。如果任何一个端口在连接过程中被关闭就向事件日志发告警。";
				pdf.buildString(document, s);
				//String[][] c3 = { { "TCP Ports", isNull(map.get("TCPPORTS")) } };
				//String[][] c3 = { { "TCP Ports",""} };
				//pdf.buildTable(document, widths3, head3, c3);

			}
			if ("4".equals(map.get("MID4").toString()) && "1".equals(map.get("SELECTSTATUS4").toString())) {
				// 离散
				pdf.buildTile3(document, "Discrete Monitor");
				String s = "离散数据监控器从被测设备上按照周期来获取数字输出，从而判断被测设备的逻辑是否还处于工作状态。";
				pdf.buildString(document, s);
				String[][] c3 = { { "周期（毫秒）", isNull(map.get("CYCLEPERIOD")) },
						{ "监控单个输入", "Input " + isNull(map.get("INPUT")) }, { "告警范围", isNull(map.get("ALARMLEVEL")) } };
				pdf.buildTable(document, widths3, head3, c3);

			}
		//}
	}
	//父pdf测试总结
	public void part5(Document document, String testResultId) throws DocumentException, IOException {// 替换part4
		// 测试总结
		pdf.buildTile(document, "4  测试总结 ");

		pdf.buildTile2(document, "测试列表");

		float[] widths = { 55f, 25f, 25f };
		String[] head = { "测试用例名称", "完成度", "异常数量" };
		// 获取此有所有测试用例
		List<Map<String, Object>> list = testResultService.findTestResultp5(testResultId);
		//存储测试进度list

		String[][] c = new String[list.size()][3];
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> m = list.get(i);
			//从事件日志表LDWJ_INCIDENTLOG查询用例的异常数量
			Map cm = testResultService.getErrorCount(m.get("ID").toString());
			c[i][0] = isNull(m.get("EQUIPMENTNAME"));
			c[i][1] =  m.get("PROGRESS")==null?"0":m.get("PROGRESS")+"%";
			c[i][2] = isNull(cm.get("COUNTERR"));
		}

		pdf.buildTable(document, widths, head, c);

		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> m = list.get(i);

			pdf.buildTile2(document, "4." + (i + 1) + "  " + m.get("EQUIPMENTNAME"));
			insertRemark(document, m);
			if (m.get("INSTALLTYPE") != null) {
				switch ((Integer) m.get("INSTALLTYPE")) {
				case 1:
					insertPdfPara1(document, m);
					break;
				case 2:
					insertPdfPara2(document, m);
					break;
				case 3:
					insertPdfPara3(document, m);
					break;
				case 4:
					insertPdfPara4(document, m);
					break;
				case 5:
					insertPdfPara5(document, m);
					break;
				}
			}
			//父节点插入异常数据
			insertError(document, m);
		}
	}

	// 时间 异常描述
	// 2015-05-20 09:07:10 发现被测设备状态异常，第2672个【IP分片语法测试】为异常数据。
	// 2015-05-20 09:10:41 发现被测设备状态异常，第11195个【IP分片语法测试】为异常数据。

	/**
	 * pdf 测试总结：取得测试案例异常总结   父pdf
	 * 
	 * @param caseId
	 *            测试结果的测试案例id
	 * @return 时间CREATETIME 第几个NUM 测试案例名称NAME
	 * @throws DocumentException
	 */
	public void insertError(Document document, Map<String, Object> m) throws DocumentException {
		pdf.buildTile3(document, "异常总结");
		List<Map<String, Object>> list2 = testResultService.findErrorLogByResultIdnew(isNull(m.get("ID")));	
		float[] widths4 = { 30f, 65f };
		String[] head4 = { "时间", "异常描述" };
		List<String[]> c5 = new ArrayList<String[]>();
		// String[][] c4 = new String[list2.size()][2];
		// 临时变量存储TCP 端口扫描的端口号的临时列表
		List<String> tmpNums = new ArrayList<String>();
		for (int j = 0; j < list2.size(); j++) {
			Map<String, Object> m4 = list2.get(j);
			// 判断是TCP 端口扫描
			if (m4.get("SOURCETYPE").toString().equals("3") && m4.get("MESSAGETYPE").toString().equals("4")) {
				// 临时变量默认不重复
				boolean tmpExist = false;
				// 循环已经存在的端口号
				for (String tn : tmpNums) {
					// 如果新端口号中已经存在临时的端口列表中
					if (tn.equals(m4.get("NUM").toString())) {
						tmpExist = true;
						break;
					}
				}
				if (tmpExist) {
					// 跳过本次循环
					continue;
				}
				// 添加到临时的端口列表
				tmpNums.add(m4.get("NUM").toString());
			}
			String[] tmc = new String[] { datetime(m4.get("CREATETIME")), m4.get("MESSAGE").toString() };

			if (Integer.valueOf(m4.get("NUM2").toString()) > 0) {
				/*
				 * c4[j][1] = "发现被测设备状态异常，第" + isNull(m4.get("NUM")) + "个【" +
				 * isNull(m.get("EQUIPMENTNAME")) + "】为异常数据。"; }else{
				 */
				tmc[1] = "发现被测设备状态异常，从第" + isNull(m4.get("NUM")) + "个--第" + isNull(m4.get("NUM2")) + "个【"
						+ isNull(m.get("EQUIPMENTNAME")) + "】为异常数据。";
			}
			c5.add(tmc);
		}
		pdf.buildTable(document, widths4, head4, c5.toArray(new String[c5.size()][2]));
	}

	public void insertRemark(Document document, Map<String, Object> m) throws DocumentException {
		Map lm = testResultService.findResultLog(m.get("ID").toString());
		if (lm != null) {
			float[] widths2 = { 30f, 65f };
			String[][] c2 = { { "描述：", isNull(m.get("REMARK")) }, { "开始时间：", datetime(lm.get("BEGINTIME")) },
					{ "结束时间：", datetime(lm.get("ENDTIME")) } };

			pdf.buildTable(document, widths2, null, c2);
		}
	}
	//风暴测试结果参数配置显示   测试套件配置参数：测试速率   测试时间    电源管理   端口号置灰
	public void insertPdfPara1(Document document, Map<String, Object> m) throws DocumentException {
		Map lm = testResultService.findDetailList(m.get("ID").toString());
		pdf.buildTile3(document, "参数配置");

		float[] widths3 = { 30f, 65f };
		String[] head3 = { "参数", "值" };

		String s = "";
		String s1 = "";
		String s2 ="";
		if (lm != null) {
			if ("1488000".equals(isNull(lm.get("TESTRATE")))) {
				s = "线速";
			} else {
				s = lm.get("TESTRATE").toString();
			}

			if ("120".equals(isNull(lm.get("TESTTIME")))) {
				s1 = "120s";
			} else {
				s1 = lm.get("TESTTIME").toString();
			}
			if ("0".equals(isNull(lm.get("POWER")))) {
				s2 = "关闭";
			} else if ("1".equals(isNull(lm.get("POWER")))) {
				s2 = "开启";
			}
		}
		String[][] c3 = { { "测试速率 (包/秒)", s }, { "发包时间(s)", s1 }, { "电源管理", s2 }};

		pdf.buildTable(document, widths3, head3, c3);

	}

	// 语法测试结果参数配置显示    测试套件配置参数：测试速率  起始用例  终止用例    问题溯源   抓包   电源管理   端口号（部分可以配置）
	public void insertPdfPara2(Document document, Map<String, Object> m) throws DocumentException {
		Map lm = testResultService.findDetailList(m.get("ID").toString());
		pdf.buildTile3(document, "参数配置");
		String remark =isNull(lm.get("REMARK"));
		float[] widths3 = { 30f, 65f };
		String[] head3 = { "参数", "值" };

		String s = "";
		String s2 = "";
		String s3 = "";
		String s4 = "";
		String s5 = "";
		String s6 = "";
		if (lm != null) {
			/*
			 * if ("1".equals(isNull(lm.get("TARGET")))) { s = "测试网络1"; } else
			 * if ("2".equals(isNull(lm.get("TARGET")))) { s = "测试网络2"; } else {
			 * s = "全部"; }
			 */
			if ("0".equals(isNull(lm.get("TESTRATE")))) {
				s = "默认";
			} else {
				s = lm.get("TESTRATE").toString();
			}
			if ("0".equals(isNull(lm.get("TRACEABILITY")))) {
				s2 = "不允许";
			} else if ("1".equals(isNull(lm.get("TRACEABILITY")))) {
				s2 = "允许";
			}

			if ("0".equals(isNull(lm.get("HURRYUP")))) {
				s3 = "从不";
			} else if ("1".equals(isNull(lm.get("HURRYUP")))) {
				s3 = "总是";
			}

			if ("0".equals(isNull(lm.get("POWER")))) {
				s4 = "关闭";
			} else if ("1".equals(isNull(lm.get("POWER")))) {
				s4 = "开启";
			}
			if(remark.indexOf("TCP")>-1 ||remark.indexOf("UDP")>-1 || remark.indexOf("IEC 104")>-1 || remark.indexOf("MMS")>-1 || remark.indexOf("Modbus")>-1||remark.indexOf("PROFINET")>-1){
				if(remark.indexOf("PROFINET")>-1 && !remark.contains("PROFINET CL_RPC 语法测试") &&  !remark.contains("PROFINET IO连接请求语法测试"))	
				{
					s6="不可配置端口";
				}else{
					s6=isNull(lm.get("PORTSEND"));
				}
			}
			if(remark.indexOf("TCP")==-1 && remark.indexOf("UDP")==-1 && remark.indexOf("IEC 104")==-1 && remark.indexOf("MMS")==-1 && remark.indexOf("Modbus")==-1 && remark.indexOf("PROFINET")==-1){
				s6="不可配置端口";
			}
		}
		/*String[][] c3 = { { "开始测试用例号", isNull(lm.get("STARTTESTCASE")) }, { "结束测试用例号", isNull(lm.get("ENDTESTCASE")) },
				{ "问题溯源", s2 }, { "全局抓包", s3 },  { "全局测试目标", s },  { "电源管理", s4 } };*/
		if(remark.contains("MMS") ||remark.contains("IEC 104")|| remark.contains("Modbus")||remark.contains("TCP最大连接数测试")){
			String[][] c3 = {{ "起始测试用例", isNull(lm.get("STARTTESTCASE")) }, { "结束测试用例", isNull(lm.get("ENDTESTCASE")) },{ "问题溯源", s2 }, { "全局抓包", s3 }, /* { "全局测试目标", s }, */ { "电源管理", s4 } ,{"端口",s6}};
			pdf.buildTable(document, widths3, head3, c3);
		}else{
			String[][] c4 = {{ "测试速率", s },  { "起始测试用例", isNull(lm.get("STARTTESTCASE")) }, { "结束测试用例", isNull(lm.get("ENDTESTCASE")) },{ "问题溯源", s2 }, { "全局抓包", s3 }, /* { "全局测试目标", s }, */ { "电源管理", s4 },{"端口",s6}};
			pdf.buildTable(document, widths3, head3, c4);
		}
	}

	// 自定义   测试套件配置参数：协议名称  测试速率   问题溯源  抓包  电源管理  发送报文个数  端口号
	public void insertPdfPara3(Document document, Map<String, Object> m) throws DocumentException {
		List<Map<String, Object>> ltcs = testResultService.findDetailcustomList(m.get("ID").toString());
		Map lm = testResultService.findDetailList(m.get("ID").toString());

		pdf.buildTile3(document, "参数配置");

		float[] widths3 = { 30f, 65f };
		String[] head3 = { "参数", "值" };

		/*
		 * String s = ""; if ("1".equals(isNull(lm.get("TARGET")))) { s =
		 * "测试网络1"; } else if ("2".equals(isNull(lm.get("TARGET")))) { s =
		 * "测试网络2"; } else { s = "全部"; }
		 */
		String s2 = "";
		if ("0".equals(isNull(lm.get("TRACEABILITY")))) {
			s2 = "不允许";
		} else if ("1".equals(isNull(lm.get("TRACEABILITY")))) {
			s2 = "允许";
		}
		String s3 = "";
		if ("0".equals(isNull(lm.get("HURRYUP")))) {
			s3 = "从不";
		} else if ("1".equals(isNull(lm.get("HURRYUP")))) {
			s3 = "总是";
		}

		String s4 = "";
		if ("0".equals(isNull(lm.get("POWER")))) {
			s4 = "关闭";
		} else if ("1".equals(isNull(lm.get("POWER")))) {
			s4 = "开启";
		}

		String s5 = "";
		if ("1488000".equals(isNull(lm.get("TESTRATE")))) {
			s5 = "线速";
		} else {
			s5 = lm.get("TESTRATE").toString();
		}

		String[][] c3 = new String[ltcs.size() + 8][2];
		c3[0][0] = "测试速率";
		c3[0][1] = s5;

		c3[1][0] = "问题溯源";
		c3[1][1] = s2;

		c3[2][0] = "全局抓包";
		c3[2][1] = s3;

		/*
		 * c3[3][0] = "全局测试目标"; c3[3][1] = s;
		 */

		c3[3][0] = "电源管理";
		c3[3][1] = s4;

		c3[4][0] = "发送报文个数";
		c3[4][1] = isNull(lm.get("SMESSAGE"));

		c3[5][0] = "端口号";
		c3[5][1] = isNull(lm.get("PORTSEND"));

		c3[6][0] = "总发包数";
		c3[6][1] = isNull(lm.get("STOTAL"));

		for (int i = 8; i < c3.length; i++) {
			Map<String, Object> mt = ltcs.get(i - 8);
			c3[i][0] = mt.get("FIELDNAME").toString();
			c3[i][1] = "有效值:" + mt.get("FIELDVALUE").toString() + ";长度(bit):" + mt.get("FIELDLEN").toString();
		}

		pdf.buildTable(document, widths3, head3, c3);
	}

	// flusser测试结果参数配置显示   测试套件配置参数： 测试速率   问题溯源    抓包   电源管理    发送报文个数   错误报文比例  端口号（置灰）
	public void insertPdfPara4(Document document, Map<String, Object> m) throws DocumentException {
		Map lm = testResultService.findDetailList(m.get("ID").toString());
		pdf.buildTile3(document, "参数配置");
		String remark =isNull(lm.get("REMARK"));
		float[] widths3 = { 30f, 65f };
		String[] head3 = { "参数", "值" };

		/*
		 * String s = ""; if ("1".equals(isNull(lm.get("TARGET")))) { s =
		 * "测试网络1"; } else if ("2".equals(isNull(lm.get("TARGET")))) { s =
		 * "测试网络2"; } else { s = "全部"; }
		 */

		String s="";
		String s2 = "";
		if ("0".equals(isNull(lm.get("TRACEABILITY")))) {
			s2 = "不允许";
		} else if ("1".equals(isNull(lm.get("TRACEABILITY")))) {
			s2 = "允许";
		}
		String s3 = "";
		if ("0".equals(isNull(lm.get("HURRYUP")))) {
			s3 = "从不";
		} else if ("1".equals(isNull(lm.get("HURRYUP")))) {
			s3 = "总是";
		}

		String s4 = "";
		if ("0".equals(isNull(lm.get("POWER")))) {
			s4 = "关闭";
		} else if ("1".equals(isNull(lm.get("POWER")))) {
			s4 = "开启";
		}

		String s5 = "";
		if ("1488000".equals(isNull(lm.get("TESTRATE")))) {
			s5 = "线速";
		} else if("0".equals(isNull(lm.get("TESTRATE")))){
			s5 = "默认";
		}else{
			s5=isNull(lm.get("TESTRATE")).toString();
		}
		if(remark.indexOf("IP Fuzzer测试")>-1 || remark.indexOf("Ethernet Fuzzer测试")>-1 || remark.indexOf("ICMP Fuzzer测试")>-1)	
		{
			s="不可配置端口";
		}else{
			s=isNull(lm.get("PORTSEND"));
		}
		String[][] c3 = { { "测试速率", s5 }, { "问题溯源", s2 }, { "全局抓包", s3 },
				/* { "全局测试目标", s }, */ { "电源管理", s4 }, { "发送报文个数", isNull(lm.get("SMESSAGE")) },
				{ "错误报文比例(%)", isNull(lm.get("EMESSAGEP")) } ,{ "端口", s}};

		pdf.buildTable(document, widths3, head3, c3);
	}

	// 端口号
	public void insertPdfPara5(Document document, Map<String, Object> m) throws DocumentException {
		Map lm = testResultService.findDetailList(m.get("ID").toString());
		pdf.buildTile3(document, "参数配置");

		float[] widths3 = { 30f, 65f };
		String[] head3 = { "参数", "值" };

		/*
		 * String s = ""; if ("1".equals(isNull(lm.get("TARGET")))) { s =
		 * "测试网络1"; } else if ("2".equals(isNull(lm.get("TARGET")))) { s =
		 * "测试网络2"; } else { s = "全部"; }
		 */

		String s2 = "";
		if ("0".equals(isNull(lm.get("TRACEABILITY")))) {
			s2 = "不允许";
		} else if ("1".equals(isNull(lm.get("TRACEABILITY")))) {
			s2 = "允许";
		}
		String s3 = "";
		if ("0".equals(isNull(lm.get("HURRYUP")))) {
			s3 = "从不";
		} else if ("1".equals(isNull(lm.get("HURRYUP")))) {
			s3 = "总是";
		}

		String s4 = "";
		if ("0".equals(isNull(lm.get("POWER")))) {
			s4 = "关闭";
		} else if ("1".equals(isNull(lm.get("POWER")))) {
			s4 = "开启";
		}

		String[][] c3 = { { "问题溯源", s2 }, { "全局抓包", s3 }, /* { "全局测试目标", s }, */ { "电源管理", s4 },
				{ "端口号", isNull(m.get("PORTSEND")) } };

		pdf.buildTable(document, widths3, head3, c3);
	}

	public void part4(Document document, String testResultId) throws DocumentException, IOException {
		// 测试总结
		pdf.buildTile(document, "4  测试总结 ");

		pdf.buildTile2(document, "测试列表");

		float[] widths = { 55f, 25f, 25f };
		String[] head = { "测试用例名称", "完成度", "异常数量" };
		/**
		 * pdf 测试总结：测试列表
		 * 
		 * @param testResultParentId
		 *            测试结果的父节点id
		 * @return 测试用例名称NAME 异常数量 ERRORNUM
		 * 
		 *         测试案例的开始时间BEGINTIME 测试案例的结束时间ENDTIME
		 * 
		 *         开始测试用例号STARTTESTCASE 结束测试用例号ENDTESTCASE REMARK
		 *         问题溯源TRACEABILITY（0为不允许1为允许） 全局抓包HURRYUP（0为从不 1为总是）
		 *         全局测试目标TARGET（1为测试网络1 ，2为测试网络2 ，0为全部）
		 */
		List<Map<String, Object>> list = testResultService.findTestResult(testResultId);
		String[][] c = new String[list.size()][3];
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> m = list.get(i);

			c[i][0] = isNull(m.get("NAME"));
			c[i][1] = m.get("PROGRESS")==null?"0":m.get("PROGRESS")+"%";
			c[i][2] = isNull(m.get("ERRORNUM"));
		}

		pdf.buildTable(document, widths, head, c);

		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> m = list.get(i);

			pdf.buildTile2(document, "4." + (i + 1) + "  " + m.get("NAME"));

			float[] widths2 = { 30f, 65f };
			String[][] c2 = { { "描述：", isNull(m.get("REMARK")) }, { "开始时间：", datetime(m.get("BEGINTIME")) },
					{ "结束时间：", datetime(m.get("ENDTIME")) } };

			pdf.buildTable(document, widths2, null, c2);

			pdf.buildTile3(document, "参数配置");

			float[] widths3 = { 30f, 65f };
			String[] head3 = { "参数", "值" };

			/*
			 * String s = ""; if ("1".equals(isNull(m.get("TARGET")))) { s =
			 * "测试网络1"; } else if ("2".equals(isNull(m.get("TARGET")))) { s =
			 * "测试网络2"; } else { s = "全部"; }
			 */

			String s2 = "";
			if ("0".equals(isNull(m.get("TRACEABILITY")))) {
				s2 = "不允许";
			} else if ("1".equals(isNull(m.get("TRACEABILITY")))) {
				s2 = "允许";
			}
			String s3 = "";
			if ("0".equals(isNull(m.get("HURRYUP")))) {
				s3 = "从不";
			} else if ("1".equals(isNull(m.get("HURRYUP")))) {
				s3 = "总是";
			}

			/*String[][] c3 = { { "开始测试用例号", isNull(m.get("STARTTESTCASE")) },
					{ "结束测试用例号", isNull(m.get("ENDTESTCASE")) }, { "问题溯源", s2 },
					{ "全局抓包", s3 }  { "全局测试目标", s }  };*/
			String[][] c3 = { { "问题溯源", s2 },
					{ "全局抓包", s3 } /* { "全局测试目标", s } */ };
			pdf.buildTable(document, widths3, head3, c3);

			pdf.buildTile3(document, "异常总结");

			// 时间 异常描述
			// 2015-05-20 09:07:10 发现被测设备状态异常，第2672个【IP分片语法测试】为异常数据。
			// 2015-05-20 09:10:41 发现被测设备状态异常，第11195个【IP分片语法测试】为异常数据。

			/**
			 * pdf 测试总结：取得测试案例异常总结
			 * 
			 * @param caseId
			 *            测试结果的测试案例id
			 * @return 时间CREATETIME 第几个NUM 测试案例名称NAME
			 */
			List<Map<String, Object>> list2 = testResultService.findErrorLogByResultIdnew(isNull(m.get("ID")));
			float[] widths4 = { 30f, 65f };
			String[] head4 = { "时间", "异常描述" };
			String[][] c4 = new String[list2.size()][2];
			for (int j = 0; j < list2.size(); j++) {
				Map<String, Object> m4 = list2.get(j);

				c4[j][0] = datetime(m4.get("CREATETIME"));
				c4[j][1] = m4.get("MESSAGE").toString();
				if (m4.get("NUM2").toString().equals("0")) {
					c4[j][1] = "发现被测设备状态异常，第" + isNull(m4.get("NUM")) + "个【" + isNull(m.get("EQUIPMENTNAME"))
							+ "】为异常数据。";
				} else {
					c4[j][1] = "发现被测设备状态异常，从第" + isNull(m4.get("NUM")) + "个--第" + isNull(m4.get("NUM2")) + "个【"
							+ isNull(m.get("EQUIPMENTNAME")) + "】为异常数据。";
				}

				// c4[j][1] = "发现被测设备状态异常，第" + isNull(m4.get("NUM")) + "个【"
				// + isNull(m4.get("NAME")) + "】为异常数据。";
			}
			pdf.buildTable(document, widths4, head4, c4);
		}
	}
	//测试用例结束时创建pdf文件
	public String createPdf(String testCaseResultId) throws Exception{
		String filePath = ConstantsDefs.TEST_FILE_PATH + testCaseResultId;
		try {
			String fileName = null;
			if (SystemFlagUtil.isWindows()) {
				String sysPath = SystemPath.getSysPath();
				filePath = sysPath + "pdf/testsetting/";
				// 没有路径创建路径
				File file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				filePath += testCaseResultId;
				filePath = filePath.replace("\\", "/");
			}
			// 生成文档
			Document document = pdf.buildDocument(filePath);
			// 打开文档
			document.open();
			List<Map<String, Object>> list = testResultService.findTestCaseResulttp5(testCaseResultId);

			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> m = list.get(i);

				pdf.buildTile2(document, isNull(m.get("EQUIPMENTNAME")));

				insertRemark(document, m);

				if (m.get("INSTALLTYPE") != null) {
					switch ((Integer) m.get("INSTALLTYPE")) {
					case 1:
						insertPdfPara1(document, m);
						break;
					case 2:
						insertPdfPara2(document, m);
						break;
					case 3:
						insertPdfPara3(document, m);
						break;
					case 4:
						insertPdfPara4(document, m);
						break;
					}
				}
				//插入异常数据
				insertError(document, m);
			}

			document.close();

			// 将数据保存在数据库
			testResultService.updateFilePath(testCaseResultId, filePath, fileName);

		} catch (Exception e) {
			log.error("生成测试用力 pdf异常", e);
			throw e;
		}
		return filePath;
	}

	// public void createPdf(String testCaseResultId) {
	// try {
	// String fileName = null;
	// String filePath = ConstantsDefs.TEST_FILE_PATH + testCaseResultId;
	// // 生成文档
	// Document document = pdf.buildDocument(filePath);
	// // 打开文档
	// document.open();
	//
	// List<Map<String, Object>> list = testResultService
	// .findTestCaseResult(testCaseResultId);
	//
	// for (int i = 0; i < list.size(); i++) {
	// Map<String, Object> m = list.get(i);
	//
	// fileName = isNull(m.get("NAME"));
	//
	// pdf.buildTile2(document, isNull(m.get("NAME")));
	//
	// float[] widths2 = { 30f, 65f };
	// String[][] c2 = { { "描述：", isNull(m.get("REMARK")) },
	// { "开始时间：", datetime(m.get("BEGINTIME")) },
	// { "结束时间：", datetime(m.get("ENDTIME")) } };
	//
	// pdf.buildTable(document, widths2, null, c2);
	//
	// pdf.buildTile3(document, "参数配置");
	//
	// float[] widths3 = { 30f, 65f };
	// String[] head3 = { "参数", "值" };
	//
	// String s = "";
	// if ("1".equals(isNull(m.get("TARGET")))) {
	// s = "测试网络1";
	// } else if ("2".equals(isNull(m.get("TARGET")))) {
	// s = "测试网络2";
	// } else {
	// s = "全部";
	// }
	//
	// String s2 = "";
	// if ("0".equals(isNull(m.get("TRACEABILITY")))) {
	// s2 = "不允许";
	// } else if ("1".equals(isNull(m.get("TRACEABILITY")))) {
	// s2 = "允许";
	// }
	// String s3 = "";
	// if ("0".equals(isNull(m.get("HURRYUP")))) {
	// s3 = "从不";
	// } else if ("1".equals(isNull(m.get("HURRYUP")))) {
	// s3 = "总是";
	// }
	//
	// String[][] c3 = {
	// { "开始测试用例号", isNull(m.get("STARTTESTCASE")) },
	// { "结束测试用例号", isNull(m.get("ENDTESTCASE")) },
	// { "问题溯源", s2 }, { "全局抓包", s3 }, { "全局测试目标", s } };
	//
	// pdf.buildTable(document, widths3, head3, c3);
	//
	// pdf.buildTile3(document, "异常总结");
	//
	// // 时间 异常描述
	// // 2015-05-20 09:07:10 发现被测设备状态异常，第2672个【IP分片语法测试】为异常数据。
	// // 2015-05-20 09:10:41 发现被测设备状态异常，第11195个【IP分片语法测试】为异常数据。
	//
	// /**
	// * pdf 测试总结：取得测试案例异常总结
	// *
	// * @param caseId
	// * 测试结果的测试案例id
	// * @return 时间CREATETIME 第几个NUM 测试案例名称NAME
	// */
	// List<Map<String, Object>> list2 = testResultService
	// .findErrorLogByResultId(isNull(m.get("ID")));
	// float[] widths4 = { 30f, 65f };
	// String[] head4 = { "时间", "异常描述" };
	// String[][] c4 = new String[list2.size()][2];
	// for (int j = 0; j < list2.size(); j++) {
	// Map<String, Object> m4 = list2.get(j);
	//
	// c4[j][0] = datetime(m4.get("CREATETIME"));
	// c4[j][1] = "发现被测设备状态异常，第" + isNull(m4.get("NUM")) + "个【"
	// + isNull(m4.get("NAME")) + "】为异常数据。";
	// }
	// pdf.buildTable(document, widths4, head4, c4);
	//
	// }
	//
	// document.close();
	//
	// // 将数据保存在数据库
	// testResultService.updateFilePath(testCaseResultId, filePath,
	// fileName);
	//
	// } catch (Exception e) {
	// log.error("生成测试用力 pdf异常", e);
	// }
	// }

	public String isNull(Object o) {
		if (o != null) {
			return o.toString();
		} else {
			return "";
		}
	}

	// 将数字转换成ip
	public String ip(Object o) {
		if (o != null) {
			long ip = 0;
			if (o instanceof Long) {
				ip = (Long) o;
			} else if (o instanceof Integer) {
				ip = (Integer) o;
			} else {
				return "";
			}
			return SocketEntityUtil.ipString(ip);
		}
		return "";
	}

	// 将日期格式化yyyy-MM-dd hh:mm:ss
	public String datetime(Object o) {
		if (o != null) {
			if (o instanceof Timestamp ) {
				Timestamp t = (Timestamp) o;
				return DataUtils.formatTime2(t.getTime());
			} else {
				System.out.println("生成pdf时一个时间未转换成功");
				return "";
			}
		}
		return "";
	}

	public static void main(String[] args) {
		PdfServiceImpl p = new PdfServiceImpl();
		// p.createPdf("");

	}
}
