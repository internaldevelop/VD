package com.wnt.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.wnt.core.ehcache.EHCacheUtil;

import com.wnt.web.testexecute.controller.TestThread;

public class LogFilter implements Filter{
	private final Logger log = Logger.getLogger(LogFilter.class.getName());
	ServletContext app;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest)arg0;
		String uri=req.getRequestURI();
		if(uri.indexOf("findData")<0 && uri.indexOf("findLog")<0 ){
			log.info(uri);
		}
		TestThread t=(TestThread)EHCacheUtil.get("thread");
		if(t!=null){
//			log.info("测试执行状态："+t.getTestEntry().getStatus());
		}
		
		log.info("端口扫描状态："+app.getAttribute("startScan"));
		
		arg2.doFilter(arg0, arg1);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		app=arg0.getServletContext();
	}

}
