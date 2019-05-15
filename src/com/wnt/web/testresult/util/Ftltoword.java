package com.wnt.web.testresult.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Ftltoword {
	
	private Configuration configuration = null;  
    
    public Ftltoword(){  
        configuration = new Configuration();  
        configuration.setDefaultEncoding("UTF-8");  
    }  
    
    public boolean createWord(ServletContext context,HttpServletResponse response, 
    		Map<String, Object> dataMap, String path,  String ftlPath){  
        configuration.setServletContextForTemplateLoading(context, path);  //FTL文件所存在的位置  
        Template t=null;  
        try {  
            t = configuration.getTemplate(ftlPath); //文件名  
        } catch (IOException e) {
            e.printStackTrace();
        }
        String realPath = context.getRealPath(path);
        File f = new File(realPath);
        if(!f.exists())
        {
        	f.mkdirs();
        }
        String name = ftlPath.split(".ftl")[0];
        File outFile = new File(realPath+"/"+name+".xml");
        Writer out = null;  
        try {  
            FileOutputStream fos = new FileOutputStream(outFile);
            OutputStreamWriter write = new OutputStreamWriter(fos,"UTF-8");
            out = new BufferedWriter(write);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
           
        try {  
            t.process(dataMap, out); 
        } catch (TemplateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {
        	try {
				out.close();
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
        }
    }
	
}
