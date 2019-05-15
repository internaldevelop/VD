/**   
 * Copyright © 2016 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: MonitorScanstatusThread.java 
 * @Prject: VD
 * @Package: com.wnt.server.thread 
 * @Description: TODO
 * @author: jfQiao  
 * @date: 2016年10月25日 下午3:05:16 
 * @version: V1.0   
 */
package com.wnt.server.thread;

import java.util.Map;

import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.LogUtil;
import org.wnt.core.uitl.SystemPath;

import com.wnt.web.configuration.service.ConfigurationService;
import com.wnt.web.testexecute.service.TestExecuteService;
import com.wnt.web.testresult.util.WriterPdfs;

import net.sf.json.JSONObject;

/**
 * @ClassName: MonitorScanstatusThread
 * @Description:
 * @author: jfQiao
 * @date: 2016年10月25日 下午3:05:16
 */
public class MonitorScanstatusThread implements Runnable {

	/** 表ldwj_configuration */
	private ConfigurationService configurationService;

	/** 表ldwj_testresult */
	private TestExecuteService testExecuteService;

	/**
	 * @Title:MonitorScanstatusThread
	 * @Description:缺省构造函数
	 */
	public MonitorScanstatusThread() {

	}

	/**
	 * @Title:MonitorScanstatusThread
	 * @Description:
	 * @param configurationService
	 * @param testExecuteService
	 */
	public MonitorScanstatusThread(ConfigurationService configurationService, TestExecuteService testExecuteService) {
		super();
		this.configurationService = configurationService;
		this.testExecuteService = testExecuteService;
	}

	/*
	 * (non Javadoc)
	 * 
	 * @Title: run
	 * 
	 * @Description:
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// 获取扫描进度
		try {
			while (true) {
				if (EHCacheUtil.get("rr1818") != null) {
					if ("101".equals(EHCacheUtil.get("rr1818").toString())) {
						// 扫描完成后移除扫描进度标识
						EHCacheUtil.remove("rr1818");
						// 向表中ldwj_testresult插入数据
						Map<String, Object> queryDataMap = configurationService.queryData();
						if (null != queryDataMap && queryDataMap.size() > 0) {
							String name = (String) queryDataMap.get("NAME");
							// String hostIp = (String)
							// queryDataMap.get("HOSTIP");
							JSONObject obj = new JSONObject();
							obj.put("name", name);
							obj.put("fileUrl", "/pdf/appsecurity/");
							// 等待XML文件生成
							Thread.sleep(2000);
							// 数据库插入测试结果报告
							String fileName = testExecuteService.insertTestCaseResult(obj) + ".pdf";
							String filePath = SystemPath.getProjectPath() + "pdf/appsecurity";
							// 读取测试结果的XML文件，并解析成JSON
							try {
								WriterPdfs.xmlToPdf(filePath, fileName);
							} catch (Exception e) {
								LogUtil.warning("", e);
							}
						}
					}
				}
				Thread.sleep(3000);
			}
		} catch (InterruptedException e) {
			LogUtil.warning("", e);
		}
	}

}
