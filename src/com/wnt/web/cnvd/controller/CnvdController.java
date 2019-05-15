package com.wnt.web.cnvd.controller;

import java.text.SimpleDateFormat;
import java.util.Date;







import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.commons.page.WntPage;
import com.wnt.web.ResultPage;
import com.wnt.web.cnvd.entry.Cnvd;
import com.wnt.web.cnvd.service.CnvdService;

/**
 * 
 * 
 * @author gyk
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * @date 2016年10月26日
 */
@Controller
@RequestMapping("cnvd")
public class CnvdController {
	private final Logger log = Logger.getLogger(getClass());
	@Autowired
	private CnvdService cnvdService;
	
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));//true:允许输入空值，false:不能为空值
	}
	
	/**
	 * 获取全部的漏洞
	 * @param model
	 * @return
	 * @author gyk
	 * @data 2016年10月27日
	 */
	@RequestMapping("list")
	public String query(String cnvdName,String cnvdId,WntPage<Cnvd> wntPage,ModelMap model){
		if((cnvdName == null || "".equals(cnvdName)) && (cnvdId == null || "".equals(cnvdId))){
			wntPage = cnvdService.findPageAll(wntPage);
		}else{
			wntPage = cnvdService.findFuzzy(wntPage,cnvdId,cnvdName);
		}
		model.put("cnvds", wntPage.getItems());
		model.put("cnvdName", cnvdName);
		model.put("cnvdId", cnvdId);
		return "cnvd/list";
	}
	
	/**
	 * 根据ID 获取 漏洞信息
	 * @param id
	 * @param model
	 * @return
	 * @author gyk
	 * @data 2016年10月27日
	 */
	@RequestMapping("{id}")
	public String get(@PathVariable Integer id,ModelMap model){
		model.put("cnvd",cnvdService.getCnvd(id));
		return "cnvd/detail";
	} 
	
	/**
	 * 验证漏洞Id 是否重复
	 * @param cnvdId
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/validCnvdId")
	public Map<String,Object> validCNVDID(String param,Integer id,ModelMap model){
		Map<String,Object> m = new HashMap<String, Object>();
		Cnvd oldCnvd = null;
		boolean valid = false;
		if(id!=null){
			oldCnvd = cnvdService.getCnvd(id);
			if(oldCnvd!=null){
				if(oldCnvd.getCnvdId().equals(param)){
					m.put("status", "y");
					m.put("info", "");
					return m;
				}
			}
		}
		Cnvd cnvd = cnvdService.getCnvd(param);
		if(cnvd != null){
			m.put("status", "n");
			m.put("info", "CNVD-ID 已经存在,请更换！");
		}else{
			m.put("status", "y");
			m.put("info", "CNVD-ID 可以使用");
		}
		return m;
	}
	
	/**
	 * 初始化漏洞编辑页面
	 * @param id
	 * @param model
	 * @return
	 * @author gyk
	 * @data 2016年10月27日
	 */
	@RequestMapping("edit")
	public String initEdit(Integer id,ModelMap model){
		if(id == null){
			model.put("cnvd", new Cnvd());
		}else{
			model.put("cnvd",cnvdService.getCnvd(id));
		}
		return "cnvd/edit";
	}
	
	/**
	 * 保存漏洞信息
	 * @param cnvd
	 * @param model
	 * @return
	 * @author gyk
	 * @data 2016年10月27日
	 */
	@RequestMapping("save")
	public String save(Cnvd cnvd,ModelMap model){
		String msg = "";
		try {
			if(cnvd.getId() == null || "".equals(cnvd.getId())){
				cnvdService.save(cnvd);
				msg = "漏洞“"+cnvd.getCnvdId()+"”添加成功";
			}else{
				cnvdService.update(cnvd);
				msg = "漏洞“"+cnvd.getCnvdId()+"”修改成功";
			}
			model.addAttribute(ResultPage.REDIRECT_URL_KEY, "cnvd/list.do");
			model.addAttribute(ResultPage.SUCCESS_MESSAGE_KEY, msg);
			return ResultPage.SUCCESS_URL;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			model.addAttribute("ex", e);
			msg = "操作失败";
			model.addAttribute(ResultPage.ERROR_MESSAGE_KEY,msg);
			return ResultPage.ERROR_URL;
		}
	}
	
	/**
	 * 根据ID删除漏洞信息
	 * @param id
	 * @param model
	 * @return
	 * @author gyk
	 * @data 2016年10月27日
	 */
	@RequestMapping("delete")
	public String delete(Integer id,ModelMap model){
		try {
			cnvdService.delete(id);
			model.addAttribute(ResultPage.REDIRECT_URL_KEY, "cnvd/list.do");
			model.addAttribute(ResultPage.SUCCESS_MESSAGE_KEY, "删除成功！");
			return ResultPage.SUCCESS_URL;
		} catch (Exception e) {
			log.error(e.getMessage());
			model.addAttribute("ex", e);
			model.addAttribute(ResultPage.ERROR_MESSAGE_KEY,"操作失败");
			return ResultPage.ERROR_URL;
		}
	}
	
	
}
