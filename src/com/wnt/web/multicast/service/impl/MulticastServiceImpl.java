package com.wnt.web.multicast.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wnt.web.multicast.dao.MulticastDao;
import com.wnt.web.multicast.service.MulticastService;

@Service("multicastService")
public class MulticastServiceImpl implements MulticastService{

	@Resource
	MulticastDao multicastDao;
	
	
	public List<Map<String, Object>> queryMulticastList() throws Exception {
		return multicastDao.queryMulticastList();
	}

	public void createMulticast(String ipAddr) throws Exception {
		multicastDao.createMulticast(ipAddr);
		
	}
	
	public Boolean findMulticast(String ipAddr) throws Exception {
		return multicastDao.findMulticast(ipAddr);
		
	}

	public void deleteMulticast(String id) throws Exception {
		multicastDao.deleteMulticast(id);
	}

	/* (non Javadoc) 
	 * @Title: deleteMulticastByIp
	 * @Description: TODO
	 * @param ipAddr 
	 * @see com.wnt.web.multicast.service.MulticastService#deleteMulticastByIp(java.lang.String) 
	 */
	@Override
	public void deleteMulticast() {
		
		multicastDao.deleteMulticast();
		
	}

	/* (non Javadoc) 
	 * @Title: findMulticast
	 * @Description: TODO 
	 * @see com.wnt.web.multicast.service.MulticastService#findMulticast() 
	 */
	@Override
	public List<Map<String, Object>> findMulticast() {
		return multicastDao.findMulticast();
	}

}
