package com.wnt.web.multicast.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.multicast.service.MulticastService;
import com.wnt.web.operationlog.service.OperationLogService;


/**
 * 扫描设置（多播地址）控制类
 * 
 * @author 付强
 * @version 1.0
 * @company 汇才同飞
 * @site http://www.javakc.cn
 * 
 */
@Controller
@RequestMapping("/multicast")
public class MulticastController {
	
	@Resource
	MulticastService multicastService;
	
	@Resource
    private OperationLogService operationLogService;
	
	private final Logger log = Logger.getLogger(MulticastController.class.getName());

	private ModelAndView modelAndView;
	
	/**
	 * ##查询多播地址信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryMulticastList")
	public String queryMulticastList(HttpServletRequest request){
		try{
			// ##查询多播地址信息
			List<Map<String, Object>> multiCastsList = multicastService.queryMulticastList();
			request.setAttribute("multiCastsList", multiCastsList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "multicast/list";
	}
	
	/**
	 * ##添加多播地址
	 */
	@ResponseBody
	@RequestMapping("/createMulticast")
	public Map createMulticast(HttpServletRequest request,String ipAddr){
		try {
			HashMap map=new HashMap();
//			if(StringUtil.isEmpty(ipAddr)){
//				map.put("status", "n");
//				map.put("info", "ip地址不能为空!");
//			}else{
			//  Boolean bool = multicastService.findMulticast(ipAddr);
			//	if (bool) {
					multicastService.deleteMulticast();
					if (ipAddr !=null || ipAddr !="" ) {
					    
					    if(!org.wnt.core.uitl.StringUtil.ipRegex(ipAddr)) {
			                map.put("status", "n");
			                map.put("info", "MAC is invalid!");
			                return map;
			            }
					    
						multicastService.createMulticast(ipAddr);
					}
					List<Map<String, Object>> list = multicastService.findMulticast();
					if (ipAddr =="" || list.size()==0) {
						SealedSendMessage.getMulticastUser("");
					}else{
						SealedSendMessage.getMulticastUser(ipAddr);
					}
					// ##添加多播地址
					map.put("success", true);
					String userName = request.getSession().getAttribute("userName").toString();
					operationLogService.addOperationLog(userName, request, "添加多播地址", "成功", "地址:" + ipAddr);
					//map.put("status", "y");
					//map.put("info", "成功保存!");
			//	}else{
			//		map.put("success", false);
					//map.put("status", "n");
					//map.put("info", "IP重复!");
			//	}
//			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ##查重
	 */
	@ResponseBody
	@RequestMapping("/findMulticast")
	public Map findMulticast(HttpServletRequest request,String ipAddr){
		try {
			// ##添加多播地址
			Boolean bool = multicastService.findMulticast(ipAddr);
			HashMap map=new HashMap();
			if(bool){
			map.put("status", "n");
			map.put("info", "重复");
			}else{
				map.put("status", "y");
				map.put("info", "通过");
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ##删除多播地址
	 */
	@ResponseBody
	@RequestMapping("/deleteMulticast")
	public Map deleteMulticast(HttpServletRequest request, String id){
		try {
			// ##删除多播地址	
		    List<Map<String, Object>> list = multicastService.findMulticast();
		    String ipAddr = "";
		    if(list != null && !list.isEmpty()) {
		        ipAddr = list.get(0).get("IPADDR").toString();
		    }
			multicastService.deleteMulticast(id);
			SealedSendMessage.getMulticastUser("");
			HashMap map=new HashMap();
			map.put("success", "true");
			map.put("info", "成功删除");
			String userName = request.getSession().getAttribute("userName").toString();
            operationLogService.addOperationLog(userName, request, "删除多播地址", "成功", "地址:" + ipAddr);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
