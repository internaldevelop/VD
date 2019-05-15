package com.wnt.web.testresult.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wnt.core.uitl.LogUtil;
import org.wnt.core.uitl.SystemPath;

import com.wnt.web.operationlog.service.OperationLogService;
import com.wnt.web.testresult.service.PdfService;
import com.wnt.web.testresult.service.TestResultService;
import com.wnt.web.testresult.util.ExportExcel;
import com.wnt.web.testresult.util.FileDownload;

import common.ConstantsDefs;
import common.SystemFlagUtil;

/**
 * 测试结果控制类
 * 
 * @author 周宏刚
 * @version 1.0
 * @company 汇才同飞
 * @site http://www.javakc.cn
 * 
 */
@Controller
public class TestResultController {

	@Resource
	private TestResultService testResultService;
	private Map result = new HashMap();

	@Resource
	private PdfService pdfService;

	@Resource
    private OperationLogService operationLogService;

	/**
	 * 批量添加 测试结果 数据
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/testresult/insertBatch")
	public void insertBatch() throws Exception {
		// testResultService.insertBatch(null);
	}

	// 测试结果树
	@ResponseBody
	@RequestMapping("/testresult/findParentNode")
	public List<Map<String, Object>> findParentNode() throws Exception {
		try {
			List<Map<String, Object>> list = testResultService.findParent();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 测试结果 图形数据 ARP ICMP
	 * 
	 * @param 1:ARP
	 *            2:ICMP
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testresult/queryArp")
	public List<String[]> queryArp(int type, String parentId) throws Exception {
		return testResultService.queryArp(type, parentId);
	}

	/**
	 * 测试结果 图形数据 TCP
	 * 
	 * @param 3:TCP
	 *            13:端口0 14:端口1
	 * @return
	 * @throws Exception
	 */
	// @ResponseBody
	// @RequestMapping("/testresult/queryTcp")
	// public List<String[]> queryTcp(int type) throws Exception
	// {
	// return testResultService.queryArp(type);
	// }

	/**
	 * 1.freemaker生成word 2.word生成PDF 3.pdf执行下载操作
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testresult/exprotPdf")
	public void exportPdf(HttpServletRequest request, HttpServletResponse response, String id) throws Exception {
		try {
			ServletContext context = request.getSession().getServletContext();
			// String pdfpath =
			// context.getRealPath("/jsp/testresult/ftl/测试报告.pdf");
			// pdfService.createPdf(id,response, pdfpath);
			String type = request.getParameter("type");
			// 根据ID查询文件相关信息
			List<Map<String, Object>> map_list = testResultService.queryResultById(id, false);
			if (null != map_list && map_list.size() > 0) {
				File pdffile = null;
				String fileUrl = (String) map_list.get(0).get("FILEURL");
				if (org.apache.commons.lang3.StringUtils.isNotBlank(fileUrl)) {
					pdffile = new File(fileUrl);
				}
				if(pdffile == null || !pdffile.exists()){
					if("2".equals(type)){
						pdffile = new File(pdfService.createPdf(id, response, null));
					}else if("1".equals(type)){
						pdffile = new File(pdfService.createPdf(id));
					}
				}
				String fileName = (String) map_list.get(0).get("EQUIPMENTNAME");
				fileName = escapeExprSpecialWord(fileName);
				FileDownload.download(fileName + ".pdf", pdffile, response);
				
				String userName = request.getSession().getAttribute("userName").toString();
		        operationLogService.addOperationLog(userName, request, "导出PDF", "成功", "文件名称:"+fileName);
			}

		} catch (Exception e) {
			LogUtil.info("导出pdf异常" + e.getMessage());
			e.printStackTrace();
		}
	}

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
	 * 生成总的pdf文件
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testresult/createPdf")
	public Map<String, Object> createPdf(HttpServletRequest request, HttpServletResponse response, String id)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			// 判断如果是第一个设备测试生成,如果不是则不生成
			//boolean flag = testResultService.queryFirstDeviceById(id);
			//if (flag) {
				
				String pdfpath = pdfService.createPdf(id, response, null);
				// 将数据保存在数据库
				testResultService.updateFilePath(id, pdfpath, null);
				result.put("success", true);
			//}
		} catch (Exception e) {
			result.put("success", false);
		}
		return result;
	}

	/**
	 * 
	 * @Title: childCreatePdf
	 * @Description: 解决BUG3311，使用的暂时规避方法，不是最佳实践
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 * @throws Exception
	 * @return: Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping("/testresult/childCreatePdf")
	public Map<String, Object> childCreatePdf(HttpServletRequest request, HttpServletResponse response, String id)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			pdfService.createPdf(id);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
		}
		return result;
	}

	/**
	 * 验证当前文件是否存在
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/testresult/showPdf")
	public Map showPdf(String path) throws Exception {
		URL serverUrl = null;
		HttpURLConnection urlcon = null;
		try {
			// 判断系统
			boolean windows = SystemFlagUtil.isWindows();
			if (windows) {
				// 获取文件路径
				if (org.apache.commons.lang3.StringUtils.isNotBlank(path) && path.contains("/")) {
					int indexOf = path.lastIndexOf("/");
					if (indexOf > -1) {
						String resultId = path.substring(indexOf);
						result.put("url", "/VD/pdf/testsetting" + resultId);
						result.put("success", true);
					}
				}
			} else { // Linux
				// 供前台使用URL
				result.put("url", path);
				serverUrl = new URL(path);
				urlcon = (HttpURLConnection) serverUrl.openConnection();
				String message = urlcon.getHeaderField(0);
				result.put("success", true);
				// 文件存在‘HTTP/1.1 200 OK’ 文件不存在 ‘HTTP/1.1 404 Not Found’
				if (StringUtils.hasText(message) && message.startsWith("HTTP/1.1 404")) {
					// 不存在
					result.put("success", false);
				}
			}
		} catch (IOException e) {
			LogUtil.info("", e);
		}
		return result;
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：d:/uuid
	 * @param newPath
	 *            String 复制后路径 如：项目/../uuid.pdf
	 * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 导出excel文件
	 * 
	 * @param response
	 * @param id
	 * @param type
	 * @throws Exception
	 */
	@RequestMapping("/testresult/exprodExcel")
	public void exprodExcel(HttpServletResponse response, String id, int type) throws Exception {
		try {
			// 1.数据库查询数据库 返回List
			// List list = ExportExcel.getStudent();
			List list = testResultService.queryExcelById(id, type);
			// 2.调用生成EXCEL方法, 并导出下载
			ExportExcel.export(list, response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 导出数据包
	 * 
	 * @param response
	 * @param id
	 * @param type
	 * @throws Exception
	 */
	@RequestMapping("/testresult/exprotData")
	public void exprotData(HttpServletRequest request, HttpServletResponse response, String id, int type) throws Exception {
		try {
			System.out.println("id :"+id);
			// 1.根据ID路径查询当前文件是否存在
			String dataPath = ConstantsDefs.TEST_DATA_PACK;
			System.out.println("dataPath :"+dataPath);
			File dataFile = new File(dataPath + "/" + id + ".pcap"); //  /pcap/id.pcap
			System.out.println("dataFile :"+dataFile);
			if (!dataFile.exists()) {
				response.setHeader("Content-type", "text/html;charset=UTF-8"); 
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("<script type='text/javascript'> alert('您要下载的文件不存在，请检查!'); </script>");
				return;
			}
			// 2.如果存在则开始做下载操作
			// 2.1查询文件的真实名称,以及开始执行时间
			List<Map<String, Object>> list = testResultService.queryResultById(id, (type == 1) ? false : true);
			Map map = list.get(0);
			Timestamp ts = (Timestamp) map.get("CREATETIME");
			String tsStr = "";
			DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				tsStr = sdf.format(ts);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String fileName = (String) map.get("EQUIPMENTNAME") + "_" + tsStr + ".pcap";
			FileDownload.download(fileName, dataFile, response);
			String userName = request.getSession().getAttribute("userName").toString();
            operationLogService.addOperationLog(userName, request, "导出数据包", "成功", "文件名称:"+fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除数据包
	 * 
	 * @param response
	 * @param id
	 * @param type
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testresult/deleteData")
	public Map<String, Object> deleteData(HttpServletRequest request, String id, int type) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		String userName = request.getSession().getAttribute("userName").toString();
		String testname = getTestName(id, false);
		try {		    
			// 删除数据包
			deleteDataPackage(id, type);
			// 数据包删除成功后,修改状态为已删除
			testResultService.updateTestResult(id);
			result.put("success", true);
						
            operationLogService.addOperationLog(userName, request, "删除数据包", "成功", "测试名称:"+testname);
		} catch (Exception e) {
			result.put("success", false);
			e.printStackTrace();
			operationLogService.addOperationLog(userName, request, "删除数据包", "失败", "测试名称:"+testname);
		}
		return result;
	}

	/**
	 * 删除树节点
	 * 
	 * @param response
	 * @param id
	 *            当前要删除的节点ID
	 * @param parent
	 *            是否为父节点
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testresult/deleteParentNode")
	public Map<String,Object> deleteParentNode(HttpServletRequest request, String id, boolean parent) throws Exception {
		Map<String,Object> m = new HashMap<String, Object>();
		
		String userName = request.getSession().getAttribute("userName").toString();
        String testname = getTestName(id, false);
        
		try {
			// 2.然后删除数据包
			deleteDataPackage(id, parent ? 2 : 1);
			// 3.其次删除pdf
			deletePdfPackage(id, parent ? 2 : 1);

			// 1.首先删除节点
			testResultService.deleteResultById(id, parent);
			
			m.put("success", true);
			
			operationLogService.addOperationLog(userName, request, "删除测试", "成功", "测试名称:"+testname);
		} catch (Exception e) {
			e.printStackTrace();
			m.put("fail", true);
			operationLogService.addOperationLog(userName, request, "删除测试", "失败", "测试名称:"+testname);
		}
		return m;
	}

	/**
	 * 公共方法调用 1.删除数据包调用 2.删除数据执行用例调用
	 * 
	 * @param id
	 * @param type
	 */
	public void deleteDataPackage(String id, int type) {
		String dataPath = ConstantsDefs.TEST_DATA_PACK;
		// 1.如果type是1说明是删除单个数据包
		// if (type == 1) {
		File dataFile1 = new File(dataPath + "/" + id + ".pcap");
		deleteFile(dataFile1);
		LogUtil.info("删除ID：" + dataPath + "/" + id + ".pcap");
		// }
		// 2.如果type是2说明是删除全部数据包
		if (type == 2) {
			// 2.1开始查询 所有子节点
			List<Map<String, Object>> map_list = testResultService.queryResultById(id, true);
			for (Map<String, Object> map : map_list) {
				File dataFile = new File(dataPath + "/" + map.get("ID").toString() + ".pcap");
				deleteFile(dataFile);
				LogUtil.info("删除ID：" + dataPath + "/" + map.get("ID").toString() + ".pcap");
			}
		}
	}

	/**
	 * 公共方法调用 1.删除PDF调用 2.删除数据执行用例调用
	 * 
	 * @param id
	 * @param type
	 */
	public void deletePdfPackage(String id, int type) {
		String dataPath = ConstantsDefs.TEST_FILE_PATH;
		// 1.如果type是1说明是删除单个数据包
		// if (type == 1) {
		File dataFile1 = new File(dataPath + "/" + id);
		deleteFile(dataFile1);
		// }
		// 2.如果type是2说明是删除全部数据包
		if (type == 2) {
			// 2.1开始查询 所有子节点
			List<Map<String, Object>> map_list = testResultService.queryResultById(id, true);
			for (Map<String, Object> map : map_list) {
				File dataFile = new File(dataPath + "/" + map.get("ID").toString());
				deleteFile(dataFile);
			}
		}
	}

	/**
	 * 验证文件是否存在,存在则直接删除
	 * 
	 * @param file
	 * @return
	 */
	public boolean deleteFile(File file) {
		if (!file.exists()) {
			return false;
		} else {
			file.delete();
			return true;
		}
	}

	/**
	 * 图形导出
	 * 
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/testresult/SaveAsImage")
	public void SaveAsImage(HttpServletRequest request, HttpServletResponse response, String type, String svg,
			String filename) throws IOException {
		request.setCharacterEncoding("utf-8");// 设置编码，解决乱码问题
		type = request.getParameter("type");
		svg = request.getParameter("svg");
		filename = request.getParameter("filename");
		filename = filename == null ? "chart" : filename;
		ServletOutputStream out = response.getOutputStream();
		if (null != type && null != svg) {
			svg = svg.replaceAll(":rect", "rect");
			String ext = "";
			Transcoder t = null;
			if (type.equals("image/png")) {
				ext = "png";
				t = new PNGTranscoder();
			} else if (type.equals("image/jpeg")) {
				ext = "jpg";
				t = new JPEGTranscoder();
			} else if (type.equals("application/pdf")) {
				ext = "pdf";
				t = new PDFTranscoder();
			} else if (type.equals("image/svg+xml")) {
				ext = "svg";
			}
			response.addHeader("Content-Disposition", "attachment; filename=" + filename + "." + ext);
			response.addHeader("Content-Type", type);

			if (null != t) {
				TranscoderInput input = new TranscoderInput(new StringReader(svg));
				TranscoderOutput output = new TranscoderOutput(out);

				try {
					t.transcode(input, output);
				} catch (TranscoderException e) {
					out.print("Problem transcoding stream. See the web logs for more details.");
					e.printStackTrace();
				}
			} else if (ext.equals("svg")) {
				// out.print(svg);
				OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
				writer.append(svg);
				writer.close();
			} else
				out.print("Invalid type: " + type);
		} else {
			response.addHeader("Content-Type", "text/html");
			out.println("Usage:\n\tParameter [svg]: The DOM Element to be converted."
					+ "\n\tParameter [type]: The destination MIME type for the elment to be transcoded.");
		}
		out.flush();
		out.close();
	}

	private String getTestName(String id, Boolean parent) {
	    List<Map<String, Object>> map_list = testResultService.queryResultById(id, false);
        if (null != map_list && map_list.size() > 0) {            
           String testName = (String) map_list.get(0).get("EQUIPMENTNAME");
           return testName;
        }
        return "";
	}
	// 设置定时器执行生成PDF
	// public void timeVoid(final ServletContext context, final String path){
	// final Timer timer = new Timer();
	// TimerTask tt=new TimerTask() {
	// @Override
	// public void run() {
	// System.out.println("到点啦！");
	// Wordtopdf.wordtopdf(context.getRealPath(path)+"/测试报告模板副本.xml",
	// context.getRealPath(path)+"/测试报告模板副本.pdf");
	// System.out.println("执行结束!");
	// timer.cancel();
	// }
	// };
	// timer.schedule(tt, 2000);
	// }

	// @RequestMapping("/testresult/test")
	// public void test(HttpServletResponse response ) throws Exception{
	//
	// try{
	// pdfService.createPdf("1B6067B4C6B0488EA411289DB6F697C9",response);
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// }

}
