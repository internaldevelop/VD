package com.wnt.web.testsetup.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wnt.core.uitl.StringUtil;

import com.wnt.web.operationlog.service.OperationLogService;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL;
import com.wnt.web.testsetup.entry.LDWJ_TESTSUITDETAIL_CUSTOM;
import com.wnt.web.testsetup.entry.LtCustomList;
import com.wnt.web.testsetup.service.TestSetupService;

import common.TestExecuteUtil;

/**
 * 测试设置控制类
 * 
 * @author 周宏刚
 * @version 1.0
 * @company 汇才同飞
 * @site http://www.javakc.cn
 * 
 */
@Controller
public class TestSetupController {

	@Resource
	private TestSetupService testSetupService;
	@Resource
    private OperationLogService operationLogService;
	
	private Map result = new HashMap();

	/**
	 * 查询测试测试用例库及我的模板库
	 * 
	 * @param type
	 *            1:用例库, 2:模板库
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testsetup/leftTree")
	public List<Map<String, Object>> findLeftTree(int type) throws Exception {
		return testSetupService.findAll(type);
	}

	/**
	 * 保存(未命名)模板信息
	 * 
	 * @param treeId
	 *            页面ID数组
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testsetup/insertNonameTemplate")
	public Map<String, Object> insertNonameTemplate(HttpServletRequest request, String treeId,
			String type, String installtype,String code,String testnum) throws Exception {
	    String userName = request.getSession().getAttribute("userName").toString();
	    try {
			if (treeId !=null && type !=null && installtype!=null && code!=null && testnum!=null) {
				// 当测试用例没有执行时可以操作
				if (!TestExecuteUtil.isRunning()) {
					// 当修改测试套件和测试用例时，清除测试执行的缓存数据
					clearTestEHCache();
					String[] treeIds = treeId.split(",");
					String[] types = type.split(",");
					String[] installtypes = installtype.split(",");	
					String[] codes = code.split(",");
					String[] testnums=null;
					List<Integer> testNumList = new ArrayList<Integer>();
					if(StringUtils.isNotBlank(testnum)){
						testnums = testnum.split(",");
						for(int i=0;i<testnums.length;i++){
							testNumList.add(Integer.valueOf(testnums[i]));
						}
					}else{
						int len =treeIds.length;
						for(int j=0;j<len;j++){
							testNumList.add(0);
						}
					}
					//查询测试套件条数超过200条不能添加
					Integer count = testSetupService.findNonameTemplate();	
					if (treeIds.length+count >201) {
						result.put("success", false);
						result.put("msg", "测试套件总数大于200个,不能继续添加测试用例!");
						operationLogService.addOperationLog(userName, request, "添加未命名测试套件", "失败", "测试套件总数大于200个,不能继续添加!");
						return result;
					}else{
						testSetupService.insertNonameTemplate(treeIds, types, installtypes,codes,testNumList);
//						testSetupService
//						.insertNonameTemplate(treeIds, types, installtypes);
						result.put("success", true);
						
						List<Map<String, Object>> list = testSetupService.findByName("未命名");
						result.put("list", list); 
						try {
    						String content = "用例名称:";
    						for(String  testCode : codes) {
    						    String testName = testSetupService.findNameByCode(testCode);
    						    content += testName + ";";
    						}
    						
    						operationLogService.addOperationLog(userName, request, "添加未命名测试套件", "成功", content);
						}catch(Exception e) {
						    e.printStackTrace();
						}
					}
				} else {
					result.put("success", false);
					operationLogService.addOperationLog(userName, request, "添加未命名测试套件", "失败", "--");
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
		}
		return result;
	}

	/**
	 * 保存我的模板信息
	 * 
	 * @param treeId
	 *            页面ID数组
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testsetup/saveTestSuite")
	public Map<String, Object> saveTestSuite(HttpServletRequest request, String[] treeId, String[] type,
			String name) throws Exception {
		try {
//			System.out.println("==========controller==============");
//			for (int i = 0; i < treeId.length; i++) {
//				System.out.println("插入自定义模板treeId :" +treeId[i]);
//			}
//			System.out.println("插入自定义模板名称 :" +name);
//			for (int i = 0; i < type.length; i++) {
//				System.out.println("插入自定义模板type :" +type[i]);
//			}
//			System.out.println("++++++++++controller++++++++++++++");
			// 当测试用例没有执行时可以操作
		    String userName = request.getSession().getAttribute("userName").toString();
		    
		    if(StringUtil.htmlCharRegex(name)) {
		        result.put("success", false);
                result.put("msg", "Input is invalid");
                return result;
		    }
		    
			if (!TestExecuteUtil.isRunning()) {
				// 当修改测试套件和测试用例时，清除测试执行的缓存数据
				clearTestEHCache();
				// 检查模板名称是否存在
				List list = testSetupService
						.findDefaultTestSuiteId(" SELECT NAME FROM LDWJ_TESTDEPLAYLIVE WHERE NAME='"
								+ name + "'");
				if (null != list && list.size() > 0) {
					result.put("success", false);
					result.put("msg", "当前模板名称已经存在!");
					operationLogService.addOperationLog(userName, request, "保存模板", "失败", "当前模板名称已经存在,模板名称:" + name);
					return result;
				}
				//模板总数大于50时,提示不能继续添加
				Integer count = testSetupService.findCustomTemplate();	
				if (count >=50) {
					result.put("success", false);
					result.put("msg", "模板总数大于50个,不能继续添加模板!");
					operationLogService.addOperationLog(userName, request, "保存模板", "失败", "模板总数大于50个,不能继续添加模板!");
					return result;
				}else{
					if (null != treeId) {
					
						testSetupService.insertTemplate(treeId, type, name);
					}
					result.put("success", true);
					operationLogService.addOperationLog(userName, request, "保存模板", "成功", "模板名称:" + name);
				}
			} else {
				result.put("success", false);
				operationLogService.addOperationLog(userName, request, "保存模板", "失败", "模板名称:" + name);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
		}
		return result;
	}

	/**
	 * 删除我的模板信息(页面中间部分)
	 * 
	 * @param treeId
	 *            页面执行删除ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testsetup/removeTemplate")
	public Map<String, Object> removeTemplate(HttpServletRequest request, String treeId) throws Exception {
		try {
		    String userName = request.getSession().getAttribute("userName").toString();        
	        
			// 当测试用例没有执行时可以操作
			if (!TestExecuteUtil.isRunning()) {
				// 当修改测试套件和测试用例时，清除测试执行的缓存数据
				clearTestEHCache();
				String deleteName="";
				if ("all".equals(treeId)) {
				    deleteName = "未命名";				    
				}else {
				    Map<String, Object> map = testSetupService.findTemplateById(Integer.valueOf(treeId));
				    deleteName = map.get("NAME").toString();				    
				}
				testSetupService.removeTemplateBySelft(treeId);
		
				if ("all".equals(treeId)) {
                    operationLogService.addOperationLog(userName, request, "删除测试套件", "成功", "套件名称:" + deleteName);
                }else {
                    operationLogService.addOperationLog(userName, request, "删除测试套件", "成功", "套件名称:未命名,用例名称:" + deleteName);              
                }
				
				
				result.put("success", true);
				
			} else {
				result.put("success", false);
				operationLogService.addOperationLog(userName, request, "删除测试套件", "失败", "--");
			}
		} catch (Exception e) {
			result.put("success", false);
		}
		return result;
	}

	/**
	 * 删除我的模板信息
	 * 
	 * @param treeId
	 *            页面执行删除ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testsetup/removeTestSuite")
	public Map<String, Object> removeTestSuite(HttpServletRequest request, String treeId) throws Exception {
	    String userName = request.getSession().getAttribute("userName").toString();
	    try {		    
			// 当测试用例没有执行时可以操作
			if (!TestExecuteUtil.isRunning()) {
				// 当修改测试套件和测试用例时，清除测试执行的缓存数据
				clearTestEHCache();
		
				Map<String, Object> map = testSetupService.findTemplateById(Integer.valueOf(treeId));
				String name = map.get("NAME").toString();
				String content = "";
				String operation = "";
				if(map.get("PARENT") == null || map.get("PARENT").toString().compareTo("0") == 0) {
				    content = "模板名称:" + name;
				    operation = "删除模板";
				}else {
				    map = testSetupService.findTemplateById(Integer.valueOf(map.get("PARENT").toString()));
				    String parentName = map.get("NAME").toString();
				    content = "模板名称:" + parentName + ",测试用例名称:" + name;
				    operation = "删除模板下的测试用例";
				}
				testSetupService.removeTemplate(treeId);
				
				operationLogService.addOperationLog(userName, request, operation, "成功", content);
				
				result.put("success", true);
			} else {
				result.put("success", false);
				operationLogService.addOperationLog(userName, request, "删除模板", "失败", "--");
			}
		} catch (Exception e) {
		    e.printStackTrace();
			result.put("success", false);
			operationLogService.addOperationLog(userName, request, "删除模板", "失败", "删除模板时异常");
		}
		return result;
	}

	/**
	 * 保存测试套件信息
	 * 
	 * @param treeId
	 *            页面ID数组
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testsetup/saveTestSuiteDetail")
	public Map<String, Object> saveTestSuiteDetail(HttpServletRequest request, LDWJ_TESTSUITDETAIL detail,
			String TESTRATES, String TESTTIMES, String STARTTESTCASES,
			String ENDTESTCASES, LtCustomList ltcus, String PRONAME)
			throws Exception {
		
	    String userName = request.getSession().getAttribute("userName").toString();
	    try {
		    if(TESTRATES != null) {
		        if(!StringUtil.isNumeric(TESTRATES)) {
		            result.put("success", false);
	                return result;
		        }
		    }
		    
		    if(TESTTIMES != null) {
                if(!StringUtil.isNumeric(TESTTIMES)) {
                    result.put("success", false);
                    return result;
                }
            }
		    
		    if(STARTTESTCASES != null) {
                if(!StringUtil.isNumeric(STARTTESTCASES)) {
                    result.put("success", false);
                    return result;
                }
            }
		    
		    if(ENDTESTCASES != null) {
                if(!StringUtil.isNumeric(ENDTESTCASES)) {
                    result.put("success", false);
                    return result;
                }
            }		    
		    
		    if(StringUtil.htmlCharRegex(PRONAME)
		            || StringUtil.htmlCharRegex(detail.getREMARK())
		            || StringUtil.htmlCharRegex(detail.getCODE())) {
		        result.put("success", false);
		    }
		    
		    
		    if(detail.getHURRYUP() != 0 && detail.getHURRYUP() != 1) {
		        result.put("success", false);
                return result;
		    }
		    
		    if(detail.getTRACEABILITY() != 0 && detail.getTRACEABILITY() != 1) {
                result.put("success", false);
                return result;
            }
            
		    if(detail.getPOWER() != 0 && detail.getPOWER() != 1) {
                result.put("success", false);
                return result;
            }
		    
		   
		    String testName = "用例名称:";
		    if(detail.getREMARK().indexOf("，") > 0) {
		        testName += detail.getREMARK().substring(0, detail.getREMARK().indexOf("，")) + ";";		    
		    }else {
		        testName += detail.getREMARK() + ";";
		    }
			// 当测试用例没有执行时可以操作
			if (!TestExecuteUtil.isRunning()) {
				// 当修改测试套件和测试用例时，清除测试执行的缓存数据
				clearTestEHCache();				
				String testRate = "测试速率(包/秒):";
				if (detail.getTESTRATE() == 1 && TESTRATES !=null) {
					detail.setTESTRATE(Integer.valueOf(TESTRATES));
					testRate += TESTRATES + ";";
				}
				if (detail.getTESTRATE() == 0 ) {
					if(detail.getTESTTYPEN().equals(1)){
						detail.setTESTRATE(1488000);
					}
					else if (detail.getTESTTYPEN().equals(3))
                    {
                    	detail.setTESTRATE(20);
                    }
					else{
						detail.setTESTRATE(0);
					}
					testRate += "默认;";
				}

				String testTime = "测试时间(s):";
				if (detail.getTESTTIME() == 1) {
				    if((null == TESTTIMES || "".equals(TESTTIMES))) {
				        detail.setTESTTIME(120);
				        testTime += "默认;";
				    }else {
				        detail.setTESTTIME(Integer.valueOf(TESTTIMES));
				        testTime += TESTTIMES + ";";
				    }					
				}else {
				    testTime += "默认;";
				}
				
				String startTestcase = "起始测试用例:";
				if (detail.getSTARTTESTCASE() == 1) {
				    if(null == STARTTESTCASES || "".equals(STARTTESTCASES)) {
				        detail.setSTARTTESTCASE(0);
				        startTestcase += "首条用例;";
				    }else {
				        detail.setSTARTTESTCASE(Integer.valueOf(STARTTESTCASES));
				        startTestcase += STARTTESTCASES + ";";
				    }
				}else {
				    startTestcase += "首条用例;";
				}
				String endTestcase = "终止测试用例:";
				
				if (detail.getENDTESTCASE() == 1) {	
					if (detail.getTESTTYPEN().equals(3)) {
	                    detail.setENDTESTCASE(detail.getSMESSAGE());
	                    endTestcase += detail.getSMESSAGE() + ";";
                    }else {
                        if(null == ENDTESTCASES || "".equals(ENDTESTCASES)) {
                            detail.setENDTESTCASE(0);
                            endTestcase += "尾条用例;";
                        }else {
                            detail.setENDTESTCASE(Integer.valueOf(ENDTESTCASES));
                            endTestcase += ENDTESTCASES + ";";
                        }
                    }
				}else {
				    endTestcase += "尾条用例;";
				}
				
				if (detail.getTESTTYPEN().equals(3)) { //自定义
					testSetupService.updateDeplaylive(detail, PRONAME);
					testSetupService.deleteCustom(detail);
					if (ltcus.getLtcus() != null) {
						testSetupService.insertCustom(detail, ltcus.getLtcus());
					}
				}
				testSetupService.insertTestSuiteDetail(detail);

				String power = "电源管理:";
				if(detail.getPOWER() == 0) {
				    power += "关闭;";
				}else {
				    power += "打开;";
				}
				
				String traceability = "问题溯源:";
				if(detail.getTRACEABILITY() == 0) {
				    traceability += "禁止;";
				}else {
				    traceability += "允许;";
				}
				
				String hurryUp = "抓包:";
				if(detail.getHURRYUP() == 0) {
				    hurryUp += "从不;";
				}else {
				    hurryUp += "总是;";
				}
				String portNo = "端口号:";
				if(detail.getPORTSEND() == null) {
				    portNo += "0;";
				}else {
				    portNo += detail.getPORTSEND() + ";";
				}
				String sendNo  = "发送报文个数:";
				if(detail.getSMESSAGE() == null) {
				    sendNo +="0;";  
				}else {
				    sendNo += detail.getSMESSAGE() + ";";
				}
				String errorNo = "错误报文比例:";
				if(detail.getEMESSAGEP() == null) {
				    errorNo += "0;"; 
				}else {
				    errorNo +=  detail.getEMESSAGEP()  + "%;";
				}
				String stotalNo = "总发包数:";
				if(detail.getSTOTAL() == null) {
				    stotalNo += "0;";
				}else {
				    stotalNo += detail.getSTOTAL() + ";";
				}

				String content = "";
				if(detail.getTESTTYPEN() == 1) {
				    content += testRate + testTime + power;
				}else if(detail.getTESTTYPEN() == 2) {                    
                    String remark = detail.getREMARK();
                    if(remark.indexOf("TCP")>-1 ||remark.indexOf("UDP")>-1 || remark.indexOf("IEC 104")>-1 || remark.indexOf("MMS")>-1 || remark.indexOf("Modbus")>-1||remark.indexOf("PROFINET")>-1){
                        if(remark.indexOf("PROFINET")>-1 && !remark.contains("PROFINET CL_RPC 语法测试") &&  !remark.contains("PROFINET IO连接请求语法测试"))   
                        {
                            portNo = null;
                        }
                    }
                    if(remark.indexOf("TCP")==-1 && remark.indexOf("UDP")==-1 && remark.indexOf("IEC 104")==-1 && remark.indexOf("MMS")==-1 && remark.indexOf("Modbus")==-1 && remark.indexOf("PROFINET")==-1){
                        portNo = null;
                    }
                    
                    if(remark.contains("MMS") ||remark.contains("IEC 104")|| remark.contains("Modbus")||remark.contains("TCP最大连接数测试")){                        
                        if(portNo == null) {
                            content += startTestcase + endTestcase + traceability + hurryUp + power;
                        }else {
                            content += startTestcase + endTestcase + traceability + hurryUp + power + portNo;
                        }
                      
                    }else{   
                        if(portNo == null) {
                            content += testRate + startTestcase + endTestcase + traceability + hurryUp + power;
                        }else {
                            content += testRate + startTestcase + endTestcase + traceability + hurryUp + power + portNo;
                        }
                        
                    }
                }else if(detail.getTESTTYPEN() == 3) {
                    content += testRate + traceability + hurryUp + power + sendNo + portNo + stotalNo;
                    if(ltcus != null) {
                        List<LDWJ_TESTSUITDETAIL_CUSTOM> list = ltcus.getLtcus();
                        if(list != null && !list.isEmpty()) {
                            for(LDWJ_TESTSUITDETAIL_CUSTOM cus : list) {
                                content += "参数名:" + cus.getFIELDNAME() + ",有效值:" + cus.getFIELDVALUE() + ";长度(bit):" + cus.getFIELDLEN() + ";";
                            }
                        }
                    }
                }else if(detail.getTESTTYPEN() == 4) {
                    content += testRate + traceability + hurryUp + power + sendNo + errorNo;
                    String remark = detail.getREMARK();
                    if(remark.indexOf("IP Fuzzer测试")>-1 || remark.indexOf("Ethernet Fuzzer测试")>-1 || remark.indexOf("ICMP Fuzzer测试")>-1)    {

                    }else{
                        if(detail.getPORTSEND() == null) {
                            content += "端口号:0;";
                        }else {
                            content += "端口号:" + detail.getPORTSEND() + ";";
                        }
                    }
                }else if(detail.getTESTTYPEN() == 5) {
                    content += traceability + hurryUp + power  + portNo;
                }
				operationLogService.addOperationLog(userName, request, "测试用例参数配置", "成功",  testName + content);
				result.put("success", true);
			} else {
				result.put("success", false);
				operationLogService.addOperationLog(userName, request, "测试用例参数配置", "失败",  testName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			operationLogService.addOperationLog(userName, request, "测试用例参数配置", "失败",  "配置过程出现异常");
		}
		return result;
	}

	/**
	 * 查询 未命名套件 信息
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testsetup/findDefaultTestSuiteId")
	public List<Map<String, Object>> findDefaultTestSuiteId() throws Exception {
		return testSetupService
				.findDefaultTestSuiteId("select ID from LDWJ_TESTDEPLAYLIVE L where L.`NAME`='未命名'");
	}

	/**
	 * 查询 未命名套件 信息
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testsetup/findDefaultTestSuite")
	public List<Map<String, Object>> findDefaultTestSuite() throws Exception {
		return testSetupService.findByName("未命名");
	}

	/**
	 * 根据 套件ID 查询详细信息
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testsetup/findTestSuiteDetail")
	public Map findTestSuiteDetail(String testDeplayId, String testtypen)
			throws Exception {
		Map map = new HashMap();
		List<Map<String, Object>> detail = testSetupService
				.findTestSuiteDetail(testDeplayId);
		List<Map<String, Object>> custom = null;
		if (testtypen.equals("3")) {
			custom = testSetupService.findCuston(testDeplayId);
		}
		map.put("detail", detail);
		map.put("custom", custom);
		return map;
	}

	// 当修改测试套件和测试用例时，清除测试执行的缓存数据
	public void clearTestEHCache() {
		// EHCacheUtil.remove("thread");
		TestExecuteUtil.t = null;
	}
	/**
	 * 清空模版信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testsetup/clearTemplate")
	public Map<String, Object> clearTemplate(HttpServletRequest request) throws Exception {
		try {
		    String userName = request.getSession().getAttribute("userName").toString();
			// 当测试用例没有执行时可以操作
			if (!TestExecuteUtil.isRunning()) {
				// 当修改测试套件和测试用例时，清除测试执行的缓存数据
				clearTestEHCache();
				testSetupService.clearTemplate();
				result.put("success", true);
				operationLogService.addOperationLog(userName, request, "清空我的模板库", "成功", "--");
			} else {
				result.put("success", false);
				operationLogService.addOperationLog(userName, request, "清空我的模板库", "失败", "--");
			}
		} catch (Exception e) {
			result.put("success", false);
		}
		return result;
	}
	
	/**
	 * 根据 参数查询用例最大抓包数
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/testsetup/findTestSuiteTestNum")
	public Map findTestSuiteTestNum(String remark)
			throws Exception {
		Map map = new HashMap();
		List<Map<String, Object>> detail = testSetupService
				.findTestSuiteTestNum(remark);
		List<Map<String, Object>> custom = null;
		map.put("detail", detail);
		return map;
	}

	
	@ResponseBody
	@RequestMapping("/testsetup/deleteCus")
	public Map deleteCus(HttpServletRequest request){
		Map map =new HashMap();
		 
		try{
			testSetupService.deleteCus(request.getParameter("id"));
			map.put("status", "y");
		}catch(Exception e){
			map.put("status", "n");
			map.put("info", "删除失败");
		}
		return map;
	}
}
