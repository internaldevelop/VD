package com.wnt.web.testresult.service;

import javax.servlet.http.HttpServletResponse;

public interface PdfService {
	
	/**
	 * 根据测试结果的父id生成pdf，pdf文件直接下载
	 * @param testResultId
	 * @param response
	 */
	public String createPdf(String testResultId,HttpServletResponse response, String pdfpath) throws Exception;
	/**
	 * 根据测试案例的id生成pdf，pdf文件存入系统目录
	 * @param testCaseResultId
	 */
	public String createPdf(String testCaseResultId) throws Exception;
}
