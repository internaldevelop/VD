/**   
 * Copyright © 2016 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: ApptestResult.java 
 * @Prject: VD
 * @Package: com.wnt.web.appsecurity.testresult.controller 
 * @Description: 管理平台>应用安全>测试结果 控制类
 * @author: jfQiao  
 * @date: 2016年9月13日 上午11:11:16 
 * @version: V1.0   
 */
package com.wnt.web.appsecurity.testresult.controller;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wnt.core.uitl.SystemPath;

import com.wnt.web.appsecurity.testresult.service.ApptestResultService;
import com.wnt.web.testresult.service.PdfService;
import com.wnt.web.testresult.util.FileDownload;

import common.ConstantsDefs;
import jodd.util.StringUtil;
import net.sf.json.JSONObject;

/**
 * @ClassName: ApptestResult
 * @Description: 管理平台>应用安全>测试结果 控制类
 * @author: jfQiao
 * @date: 2016年9月13日 上午11:11:16
 */
@Controller
@RequestMapping("/apptestResult")
public class ApptestResultController {

	@Resource
	private ApptestResultService apptestResultService;

	@Resource
	private PdfService pdfService;

	/**
	 * 
	 * @Title: init
	 * @Description: 管理平台>应用安全>测试结果 ---左树跳转
	 * @return
	 * @return: String
	 */
	@RequestMapping("/init")
	public String init() {
		return "/appsecurity/testresult/main";
	}

	/**
	 * 
	 * @Title: findParentNode
	 * @Description: 左树测试结果树状展示
	 * @return
	 * @throws Exception
	 * @return: List<Map<String,Object>>
	 */
	@RequestMapping("/findParentNode")
	@ResponseBody
	public List<Map<String, Object>> findParentNode() throws Exception {
		try {
			List<Map<String, Object>> list = apptestResultService.findParent();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @Title: showPdf
	 * @Description: 根据PDF的文件路径，显示到页面上
	 * @param path
	 * @return
	 * @throws Exception
	 * @return: Map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/showPdf")
	@ResponseBody
	public Map showPdf(String path) throws Exception {
		Map result = new HashMap();
		URL serverUrl = null;
		HttpURLConnection urlcon = null;
		try {
			// 开启连接
			serverUrl = new URL(path);
			urlcon = (HttpURLConnection) serverUrl.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String message = urlcon.getHeaderField(0);
		result.put("success", true);
		// 文件存在‘HTTP/1.1 200 OK’ 文件不存在 ‘HTTP/1.1 404 Not Found’
		if (StringUtils.hasText(message) && message.startsWith("HTTP/1.1 404")) {
			// 不存在
			result.put("success", false);
		}
		return result;
	}

	/**
	 * 
	 * @Title: createPdf
	 * @Description: 更新数据
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 * @throws Exception
	 * @return: Map<String,Object>
	 */
	@RequestMapping("/createPdf")
	@ResponseBody
	public Map<String, Object> createPdf(HttpServletRequest request, HttpServletResponse response, String id)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String pdfpath = ConstantsDefs.TEST_FILE_PATH + id;
			pdfService.createPdf(id, response, pdfpath);
			// 将数据保存在数据库
			apptestResultService.updateFilePath(id, pdfpath, null);
			File pdffile = new File(pdfpath);
			if (pdffile.exists()) {
				result.put("success", true);
			}
		} catch (Exception e) {
			result.put("success", false);
		}
		return result;
	}

	/**
	 * 
	 * @Title: exportPdf
	 * @Description: 导出PDF文件
	 * @param request
	 * @param response
	 * @param id
	 * @throws Exception
	 * @return: void
	 */
	@RequestMapping("/exprotPdf")
	public void exportPdf(HttpServletRequest request, HttpServletResponse response, String id) {
		try {
			// 根据ID查询文件相关信息
			List<Map<String, Object>> map_list = apptestResultService.queryResultById(id, false);
			// 判断文件是否存在
			if (null != map_list && map_list.size() > 0) {
				String fileName = (String) map_list.get(0).get("EQUIPMENTNAME");
				fileName = escapeExprSpecialWord(fileName);
				String fileUrl = (String) map_list.get(0).get("FILEURL");
				if (null != fileUrl && !"".equals(fileUrl)) {
					String sysPath = SystemPath.getSysPath();
					if (StringUtil.isNotBlank(sysPath) && sysPath.contains("//")) {
						sysPath = "/" + sysPath.replace("//", "/");
					}
					String filePath = sysPath + fileUrl + ".pdf";
					File file = new File(filePath);
					if (file.exists()) {
						FileDownload.download(fileName + ".pdf", file, response);
					}
				} else {
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: deleteNode
	 * @Description: 管理平台>应用安全>测试结果 删除
	 * @param obj
	 * @return
	 * @return: String
	 */
	@RequestMapping(value = "/deleteNode", method = RequestMethod.POST)
	@ResponseBody
	public String deleteNode(@RequestBody JSONObject obj) {
		if (null != obj) {
			String id = obj.getString("id");
			String pdfPath = getProjectPath() + "pdf/appsecurity/" + id + ".pdf";
			if (org.apache.commons.lang3.StringUtils.isNotBlank(id)) {
				// 删除测试结果的记录
				int deleteCount = apptestResultService.deleteTestresultById(id);
				// 删除PDF文件
				if (deleteCount > 0) {
					File pdfFile = new File(pdfPath);
					if (pdfFile.exists()) {
						pdfFile.delete();
					}
					return "true";
				}
			}
		}
		return "false";
	}

	/**
	 * 
	 * @Title: escapeExprSpecialWord
	 * @Description: 特殊字符转为下划线
	 * @param keyword
	 * @return
	 * @return: String
	 */
	public String escapeExprSpecialWord(String keyword) {
		if (org.apache.commons.lang.StringUtils.isNotBlank(keyword)) {
			String[] fbsArr = { "\\", "$", "*", "?", "^", "{", "}", "|", "/" };
			for (String key : fbsArr) {
				if (keyword.contains(key)) {
					keyword = keyword.replace(key, "_");
				}
			}
		}
		return keyword;
	}

	/**
	 * 
	 * @Title: getProjectPath
	 * @Description: 获取工程路径，Windows，Linux都可
	 * @return
	 * @return: String
	 */
	private String getProjectPath() {
		String sysPath = SystemPath.getSysPath();
		if (StringUtil.isNotBlank(sysPath) && sysPath.contains("//")) {
			sysPath = "/" + sysPath.replace("//", "/");
		}
		return sysPath;
	}

}
