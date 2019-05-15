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

import com.wnt.web.login.contorller.LoginContorller;
import com.wnt.web.testexecute.controller.TestThread;

public class UserFilter implements Filter{
	private final Logger log = Logger.getLogger(UserFilter.class.getName());
	ServletContext app;
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
	    
	    String remoteAddr = arg0.getRemoteAddr();
	    String userName = LoginContorller.getLoginUser(remoteAddr);
	    if(userName != null) {
	        HttpServletRequest request = (HttpServletRequest)arg0;
	        request.getSession().setAttribute("userName", userName);
	    }
		
		arg2.doFilter(arg0, arg1);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		app=arg0.getServletContext();
	}

}
