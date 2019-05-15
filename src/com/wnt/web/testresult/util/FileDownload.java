package com.wnt.web.testresult.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

public class FileDownload {
	
	/**
     * 开始下载文件
     * @param file
     * @param response
     * @throws Exception
     */
    public static void download(File file, HttpServletResponse response) throws Exception
    {
        // 取得文件名
        String filename = file.getName();
        // 取得文件的后缀名。
        InputStream fis = new BufferedInputStream(new FileInputStream(file));
//        byte[] buffer = new byte[fis.available()];
//        fis.read(buffer);
//        fis.close();
        // 清空response
        response.reset();
        response.setCharacterEncoding("UTF-8");
        // 设置response的Header
        response.addHeader("Content-Disposition", 
        		"attachment;filename=" + new String(filename.getBytes("GB2312"),"iso8859-1")+  "\"");
        response.addHeader("Content-Length", "" + file.length());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
//        toClient.write(buffer);
        byte[] buffer = new byte[1024 * 1024 * 4];   
        int i = -1;   
        while ((i = fis.read(buffer)) != -1) {   
            toClient.write(buffer, 0, i);
        }
        fis.close();
        toClient.flush();
        toClient.close();
    }
    
    /**
     * 开始下载文件
     * @param file
     * @param response
     * @throws Exception
     */
    public static void download(String fileName, File file, HttpServletResponse response) throws Exception
    {
        // 取得文件的后缀名。
        InputStream fis = new BufferedInputStream(new FileInputStream(file));
        
//        byte[] buffer = new byte[fis.available()];
//        byte[] buffer = new byte[1024 * 1024 * 4];   
//        int i = -1;   
//        while ((i = fis.read(buffer)) != -1) {   
//            toClient.write(buffer, 0, i);  
//        }
//        fis.read(buffer);
//        fis.close();
        
        // 清空response
        response.reset();
        response.setCharacterEncoding("UTF-8");
        // 设置response的Header
        response.addHeader("Content-Disposition", 
        		"attachment;filename=\"" + new String(fileName.getBytes("GB2312"),"iso8859-1")+  "\"");
        response.addHeader("Content-Length", "" + file.length());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        
        byte[] buffer = new byte[1024 * 1024 * 4];   
        int i = -1;   
        while ((i = fis.read(buffer)) != -1) {   
            toClient.write(buffer, 0, i);
        }
        fis.close();
        
        toClient.flush();
        toClient.close();
    }
    
}
