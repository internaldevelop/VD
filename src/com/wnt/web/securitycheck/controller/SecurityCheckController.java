package com.wnt.web.securitycheck.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wnt.web.ResultPage;
import com.wnt.web.configuration.service.ConfigurationService;
import com.wnt.web.environment.service.EnvironmentService;
import com.wnt.web.securitycheck.entry.BaselineDto;
import com.wnt.web.securitycheck.service.BaselineService;

import common.SocketEntityUtil;

/**
 * 
 * @author gyk
 *
 */
@Controller
@RequestMapping("/securityCheck")
public class SecurityCheckController {
	private final Logger log = Logger.getLogger(getClass());
	@Autowired
	EnvironmentService environmentService;
	
	@Autowired
	BaselineService baselineService;
	
	/**
	 * 初始化配置页面
	 * @param model
	 * @return
	 */
	@RequestMapping("config")
	public String initConfig(ModelMap model){
		//查询数据库数据
		Map<String, Object> data = environmentService.findEnvironmentByEquipmentId("1");
		if(data.get("IP2")!=null){
			String ip2 = SocketEntityUtil.ipString((Long)data.get("IP2"));
			model.put("ip",ip2);
		}
		if(data.get("NAME") != null){
			Object tmpName = data.get("NAME");
			model.put("name", tmpName);
		}
		return "securitycheck/config";
	}
	
	/**
	 * 初始化配置基线
	 * @param model
	 * @return
	 */
	@RequestMapping("baseline")
	public String initBaseline(ModelMap model){
		model.put("list", baselineService.findAll());
		return "securitycheck/baseline";
	}
	
	/**
	 * 保存基线信息
	 * @param dto
	 * @param model
	 * @return
	 */
	@RequestMapping("save")
	public String saveBaseline(BaselineDto dto,ModelMap model){
		try {
			baselineService.clearInsert(dto);
			model.addAttribute(ResultPage.REDIRECT_URL_KEY, "securityCheck/baseline.do");
			model.addAttribute(ResultPage.SUCCESS_MESSAGE_KEY, "配置基线保存成功！");
			return ResultPage.SUCCESS_URL;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			model.addAttribute("ex", e);
			model.addAttribute(ResultPage.ERROR_MESSAGE_KEY,"操作失败");
			return ResultPage.ERROR_URL;
		}
	}
	
	
}
