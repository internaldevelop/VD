package com.wnt.server.thread;

import javax.servlet.ServletContext;
import org.springframework.web.context.ContextLoader;
import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.LogUtil;
import org.wnt.core.uitl.SocketEntityUtil;

import com.wnt.web.servlet.PlatformSynServlet;
import com.wnt.web.socket.service.SocketService;


/**
 * 接受端口MAC
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class ConnectThread implements Runnable {

	private byte[] fileTemp;
	private SocketService socketService;
	public ConnectThread(byte[] fileTemp,SocketService socketService) {
		this.fileTemp=fileTemp;
		this.socketService=socketService;
	}

	@Override
	public void run() {	
		try {
			if(fileTemp!=null&&fileTemp.length>0){
				//设备ID
				int equipmentId=SocketEntityUtil.byteToInt(fileTemp, 9, 1);				
				//被测设备MAC地址
				String equipmentMac=SocketEntityUtil.bytesToMac(fileTemp,16,6);
				//工控漏洞挖掘平台MAC地址
				String terraceMac=SocketEntityUtil.bytesToMac(fileTemp,22,6);
				
				int dev = fileTemp[28];
				
				String str = "";
				for (int i = 0; i < fileTemp.length; i++) {
					str = str + fileTemp[i] + " ";
				}
				LogUtil.info("环境检测："+str);
//				System.out.println("dev"+dev);
//				System.out.println(terraceMac);
				//信息放入缓存
				equipmentId = equipmentId+1;
				switch(dev){
				case 0:
					
					EHCacheUtil.put("r"+equipmentId+"001", terraceMac);
					EHCacheUtil.put("r"+equipmentId+"002", equipmentMac);
					//更新数据库
					socketService.updateMac(equipmentId, terraceMac, equipmentMac);
					ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
					if(!"00:00:00:00:00:00".equals(terraceMac) && !"00:00:00:00:00:00".equals(equipmentMac)){
						servletContext.setAttribute(PlatformSynServlet.EQUIP_CONN_STATUS, true);
					}else{
						servletContext.setAttribute(PlatformSynServlet.EQUIP_CONN_STATUS, false);
					}
					LogUtil.info("环境检测：设备编码"+equipmentId+",terraceMac:"+terraceMac+",equipmentMac:"+equipmentMac);
//					System.out.println("r"+equipmentId+"001"+"第一个"+terraceMac);
					break;
				case 1:
					EHCacheUtil.put("r"+equipmentId+"003", equipmentMac);
					//更新数据库
					socketService.updateMac(equipmentId, "0", equipmentMac);
					servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
					if(!"00:00:00:00:00:00".equals(terraceMac) && !"00:00:00:00:00:00".equals(equipmentMac)){
						servletContext.setAttribute(PlatformSynServlet.ENABLE_CONN_STATUS, true);
					}else{
						servletContext.setAttribute(PlatformSynServlet.ENABLE_CONN_STATUS, false);
					}
					LogUtil.info("环境检测：设备编码"+equipmentId+",terraceMac:"+",equipmentMac:"+equipmentMac);
//					System.out.println("r"+equipmentId+"003"+"第一个"+terraceMac);
					break;
				}
				
//				//如果全为0，则说明失败
//				if("00:00:00:00:00:00".equals(terraceMac)){
//					
//					EHCacheUtil.put("r"+equipmentId+"003", equipmentMac);
//					//更新数据库
//					socketService.updateMac(equipmentId, "0", equipmentMac);
//				}else{
//					EHCacheUtil.put("r"+equipmentId+"001", terraceMac);
//					EHCacheUtil.put("r"+equipmentId+"002", equipmentMac);
//					//更新数据库
//					socketService.updateMac(equipmentId, terraceMac, equipmentMac);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
