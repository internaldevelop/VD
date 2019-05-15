package com.wnt.web.main.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



/**
 * 用户登录控制类
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
@Controller
public class LoginController {
	private final Logger log = Logger.getLogger(LoginController.class.getName());

	

	private ModelAndView modelAndView;

	private static final String SUCCESS_PAGE = "index";

	@RequestMapping("/menu")
	public String leftcaidan(HttpServletRequest request) {
		return "main/left";
	}
	
	@RequestMapping("/mainwnt")
	public void mainwnt(HttpServletRequest request,HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    out.flush();
	    out.println("<script>");
	    out.println("window.open ('jsp/main/index.jsp','newwindow','width='+(window.screen.availWidth-10)+',height='+(window.screen.availHeight-30)+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');");
//	    out.println("window.close();");
	    out.println("</script>");
//	    return "redirect:''";
	}
}
