package org.wnt.core.listener;

import java.io.IOException;
import java.security.SecureRandom;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.wnt.core.uitl.PasswordUtil;


public class LoginFilter implements Filter  {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request=(HttpServletRequest)arg0;  
		HttpServletResponse response=(HttpServletResponse)arg1;  
		HttpSession session = request.getSession();  
        String uri = request.getRequestURI();  
        //不过滤登录退出  
        if(session.getAttribute("userName") == null){
            if((!uri.contains("/login.jsp")) && (!uri.contains("/login.do"))){  
                response.sendRedirect(request.getContextPath()+"/jsp/main/login.jsp");  
                return;  
            }  
        }  
        try{  
            chain.doFilter(request, response);  
        }catch(IllegalStateException e){  
        }  
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}


}
