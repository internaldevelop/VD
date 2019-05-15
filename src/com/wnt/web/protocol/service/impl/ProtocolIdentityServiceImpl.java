
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: ProtocolIdentityServiceImpl.java 
 * @Prject: VD
 * @Package: com.wnt.web.protocol.service.impl 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-5-2 上午11:45:14 
 * @version: V1.0   
 */
package com.wnt.web.protocol.service.impl;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.wnt.web.protocol.dao.ProtocolIdentityDao;
import com.wnt.web.protocol.entry.Protocol;
import com.wnt.web.protocol.service.ProtocolIndetityService;

/** 
 * @ClassName: ProtocolIdentityServiceImpl 
 * @Description: TODO
 * @author: gyj
 * @date: 2017-5-2 上午11:45:14  
 */
 @Service("protocolIdentityService")
public class ProtocolIdentityServiceImpl implements ProtocolIndetityService {
	 @Resource
	 private  ProtocolIdentityDao protocolIdentityDao;
	/* (non Javadoc) 
	 * @Title: findProtocolResult
	 * @return 
	 * @see com.wnt.web.protocol.service.ProtocolIndetityService#findProtocolResult() 
	 */
	@Override
	public List<Map<String, Object>> findProtocolResult(String time) {
		return	protocolIdentityDao.findProtocolResult(time);
		
	}
	/* (non Javadoc) 
	 * @Title: deleteProtocol
	 * @Description: TODO
	 * @param parameter 
	 * @see com.wnt.web.protocol.service.ProtocolIndetityService#deleteProtocol(java.lang.String) 
	 */
	@Override
	public void deleteProtocol(String id) {
		protocolIdentityDao.deleteProtocol(id);
	}
	
	@Override
	public void addProtocol(List<Protocol> listProtocol) {
		protocolIdentityDao.addProtocol(listProtocol);
		
	}
	/* (non Javadoc) 
	 * @Title: deleteProtocol
	 * @Description: TODO 
	 * @see com.wnt.web.protocol.service.ProtocolIndetityService#deleteProtocol() 
	 */
	@Override
	public void deleteProtocol() {
		protocolIdentityDao.deleteProtocol();
		
	}
	/* (non Javadoc) 
	 * @Title: findAll
	 * @Description: TODO
	 * @return 
	 * @see com.wnt.web.protocol.service.ProtocolIndetityService#findAll() 
	 */
	@Override
	public List<Map<String, Object>> findAll() {
		
		return protocolIdentityDao.findAll();
		
	}
	/* (non Javadoc) 
	 * @Title: findLastTime
	 * @Description: TODO
	 * @return 
	 * @see com.wnt.web.protocol.service.ProtocolIndetityService#findLastTime() 
	 */
	@Override
	public List<Map<String, Object>> findLastTime() {
		return protocolIdentityDao.findLastTime();
		
	}
}

