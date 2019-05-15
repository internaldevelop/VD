package com.wnt.web.testdeplay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.wnt.core.log.MethodLog;
import org.wnt.core.uitl.JSONHelper;
import org.wnt.core.uitl.StringUtil;

import com.wnt.web.testdeplay.entry.DeplayEntry;
import com.wnt.web.testdeplay.service.TestDeplayService;




/**
 * 测试设置
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
@Controller
public class TestDeplayController {
	private final Logger log = Logger.getLogger(TestDeplayController.class.getName());
	@Resource
	private TestDeplayService testDeplayService;
	private JSONArray json;
	private ModelAndView modelAndView;
	
	/**
	 * 查询已选测试用例
	 * 
	 */
	@RequestMapping("/findforlist")
	public ModelAndView findforlist(HttpServletRequest request,HttpServletResponse response) {
		modelAndView = new ModelAndView();
		modelAndView.setViewName("testdeplay/list");
		List<Map<String, Object>> list=testDeplayService.findforlist();
		modelAndView.addObject("list", list);
		return modelAndView;
	}
	/**
	 * 查询树
	 * 
	 */
	@RequestMapping(value = "/ztree")
	@MethodLog(remark = "测试设置",operType="登陆")
	public void findztreeList(String type,HttpServletRequest request,HttpServletResponse response) throws IOException {
		List<DeplayEntry> ztreeList = testDeplayService.findztreeList(Integer.parseInt(type));
		//String result=[{"id":-1,"isParent":true,"name":"部门","open":true,"parentId":0},{"id":2,"isParent":true,"name":"技术部","open":false,"parentId":-1},{"id":3,"isParent":true,"name":"销售部","open":false,"parentId":-1},{"id":4,"isParent":true,"name":"车间","open":false,"parentId":-1},{"id":1,"isParent":true,"name":"财务部","open":false,"parentId":-1}];
		String str = JSONHelper.toJSONString(ztreeList);
		PrintWriter out = response.getWriter();
		out.print(str);
	}
	/**
	 * 保存选中用例
	 * 
	 */
	@RequestMapping(value = "/savedeplay")
	@MethodLog(remark = "保存用例",operType="保存")
	public void saveDeplay(@RequestBody DeplayEntry vo,HttpServletRequest request,HttpServletResponse response) throws IOException {	   
	    if(StringUtil.htmlCharRegex(vo.getName())
	            ||StringUtil.htmlCharRegex(vo.getRemark())) {
	        return;
	    }
        
	    if(!StringUtil.isNumeric(vo.getType()) || !StringUtil.isNumeric(vo.getInstalltype())){
	        return;
	    }
	    testDeplayService.saveDeplay(vo);
	}
}
