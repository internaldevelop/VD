package com.wnt.server.order;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.ServletContext;

import org.springframework.web.context.ContextLoader;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;

import common.SystemFlagUtil;

/**
 * 全局变量
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */

public class ConstantsServer {
	//自增全局变量
	public static int SENTFLAG=0;
	
	//自增全局变量
	public static int SENTFLAGW=0;
	
	//自增全局re#12
	public static int STATERESPONSEN = 0;
	
	//自增全局日志
	public static int LOGN = 0;
	
	public static int STATERE = 0;
	
	public static int LOGS = 0;
	
	public static int PROTOCOLFLAG;
	
	public static BlockingQueue<byte[]> protocolResponseQueue = null;
	
	public static BlockingQueue<byte[]> maxNumResponseQueue = null;
	
	public static LinkedBlockingQueue<byte[]> sendOrderQueue =null;
	
	public static int SYSINFOFLAG;
	
	public static int VERSIONFLAG;
	/**
	 * 线程退出标识
	 */
	public static boolean BSTOPT = false;

	public final static SocketIOServer server;
	public final static SocketIONamespace chatnamespace;
	public final static SocketIONamespace sysinfonamespace;

	
	static{
		Configuration config = new Configuration();
		config.setPort(9092);
		boolean windows = SystemFlagUtil.isWindows();
		if(windows) {
		    
		}else {
    		config.setKeyStorePassword("qwerty");
    		InputStream stream;
            try {
                stream = new FileInputStream(new File("/home/crt/crt/java/apache-tomcat-8.5.31/conf/shfqkeystore.jks"));
                config.setKeyStore(stream);
               
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            config.setSSLProtocol("TLS");
		}
		        
		server = new SocketIOServer(config);
		chatnamespace = ConstantsServer.server.addNamespace("/chat");
		sysinfonamespace = ConstantsServer.server.addNamespace("/sysinfo");

	} 

}
