
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: ReturnTestNumThread.java 
 * @Prject: VD
 * @Package: com.wnt.server.thread 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-6-5 上午11:45:58 
 * @version: V1.0   
 */ package com.wnt.server.thread; import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.SocketEntityUtil;

import com.wnt.server.order.ConstantsServer;
import com.wnt.web.dynamicports.entry.DynamicPorts;
import com.wnt.web.socket.service.SocketService;
import com.wnt.web.testsetup.entry.LDWJ_TESTDEPLAYLIVE;


/** 
 * @ClassName: ReturnTestNumThread 
 * @Description: TODO
 * @author: gyj
 * @date: 2017-6-5 上午11:45:58  
 */
public class ReturnTestNumThread  implements Runnable{
	
	private final Logger log = Logger.getLogger(ReturnTestNumThread.class.getName());
	int count = 0;
	byte[] bt =null;
	List<LDWJ_TESTDEPLAYLIVE> list = new ArrayList<LDWJ_TESTDEPLAYLIVE>();
	private SocketService socketService;
	public ReturnTestNumThread(SocketService socketService) {
		this.socketService = socketService;
	}
		@Override
		public void run() {
//			synchronized (this) {
			try {
//				ConstantsServer.PROTOCOLFLAG++;
				while (true) {
//					if (EHCacheUtil.get("protocol"+ConstantsServer.PROTOCOLFLAG)!= null) {
					if(null != ConstantsServer.maxNumResponseQueue){
						bt = ConstantsServer.maxNumResponseQueue.poll();

						if(bt!=null){
								//测试用例索引
								int testIdx = SocketEntityUtil.byteToInt(bt, 16, 2);					
								//测试用例索引
								int testIdx2 = SocketEntityUtil.byteToInt(bt, 18, 2);
								int test_Num = SocketEntityUtil.byteToInt(bt, 20, 4);
								LDWJ_TESTDEPLAYLIVE  live = new LDWJ_TESTDEPLAYLIVE();
								live.setCode(testIdx+"-"+testIdx2);
								live.setTestnum(test_Num);
								list.add(live);
							}
						}
						if (list.size() > 0) {
							try {
								socketService.updateTestNum(list);				
							} catch (Exception e) {
								e.printStackTrace();
							}
							list.clear();
						}
		
						Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				
			}
		}
}

