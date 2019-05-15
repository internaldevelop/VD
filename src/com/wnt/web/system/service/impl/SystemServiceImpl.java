package com.wnt.web.system.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.system.dao.SystemDao;
import com.wnt.web.system.entry.Sys;
import com.wnt.web.system.service.SystemService;

import common.SocketEntityUtil;

@Service("SystemService")
public class SystemServiceImpl implements SystemService{
	@Resource
	private SystemDao systemDao;
	
	public void interfaceipadd(String ip){
		SealedSendMessage.setIp(ip);
		systemDao.interfaceipadd(SocketEntityUtil.ipToLong(ip));
	}
	
	public String findinterface(){
		List<Sys> list = systemDao.findinterface();
		String ip = SocketEntityUtil.ipString(list.get(0).getInterfaceip());
		return ip;
	}

	@Override
	public String findgetDelchart() {
		Map<String, Object> m = systemDao.findgetDelchart();
		return m.get("DELTIME").toString();
	}

	@Override
	public void updateDelchart(String str) {
		// TODO Auto-generated method stub
		systemDao.updateDelchart(str);
	}
}
