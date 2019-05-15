package com.wnt.web.selfcheck.service.impl;


import org.springframework.stereotype.Service;
import org.wnt.core.ehcache.EHCacheUtil;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.server.order.TypeCmd;
import com.wnt.web.selfcheck.service.SelfcheckService;

@Service("selfcheckService")
public class SelfcheckServiceImpl implements SelfcheckService {

	@Override
	public void startHardWareCheck() {
		SealedSendMessage.startHardWareCheck();
	}

	@Override
	public void stopHardWareCheck() {
		SealedSendMessage.stopHardWareCheck();	
	}
	
	@Override
	public int[] getHardWareCheckDIStatus(){
		 Object temp = EHCacheUtil.get("r"+TypeCmd.CMD_HARDWARE_CHECK_START);
		 if(temp!=null){
			 return (int[])temp;
		 }
		 return null;
	}

	@Override
	public void checkDoOutStatus(int[] ys) {
		SealedSendMessage.checkDoOutStatus(ys);
	}
	
}
