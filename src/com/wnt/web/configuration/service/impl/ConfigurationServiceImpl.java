package com.wnt.web.configuration.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.configuration.dao.ConfigurationDao;
import com.wnt.web.configuration.entry.ConfigurationEntry;
import com.wnt.web.configuration.service.ConfigurationService;


@Service("configurationService")
public class ConfigurationServiceImpl implements ConfigurationService{
	@Resource
	ConfigurationDao configurationDao;

	@Override
	public Map<String, Object> queryData() {
		return configurationDao.queryData();
	}

	@Override
	public void updateData(ConfigurationEntry entry) {
		configurationDao.updateData(entry);
	}

	@Override
	public void updateDataList2(ConfigurationEntry entry) {
		configurationDao.updateDataList2(entry);
	}

	@Override
	public void sendData(ConfigurationEntry entry) {
		SealedSendMessage.getAppReadyStartTest(entry.getName(), entry.getHostip(), entry.getType());
	}
	
}
