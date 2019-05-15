
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: ProtocolIndetityService.java 
 * @Prject: VD
 * @Package: com.wnt.web.protocol.service 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-5-2 上午11:43:59 
 * @version: V1.0   
 */ package com.wnt.web.protocol.service; import java.util.List;
import java.util.Map;

import com.wnt.web.protocol.entry.Protocol;

/** 
 * @ClassName: ProtocolIndetityService 
 * @Description: TODO
 * @author: gyj
 * @date: 2017-5-2 上午11:43:59  
 */
public interface ProtocolIndetityService {

	/** 
	 * @Title: findProtocolResult 
	 * @Description: TODO
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findProtocolResult(String datetime);

	/** 
	 * @Title: deleteProtocol 
	 * @Description: TODO
	 * @param parameter
	 * @return: void
	 */
	public void deleteProtocol(String parameter);
	/** 
	 * @Title: addProtocol 
	 * @Description: TODO
	 * @param listProtocol
	 * @return: void
	 */
	public void addProtocol(List<Protocol> listProtocol);

	/** 
	 * @Title: deleteProtocol 
	 * @Description: TODO
	 * @return: void
	 */
	public void deleteProtocol();

	/** 
	 * @Title: findAll 
	 * @Description: TODO
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findAll();

	/** 
	 * @Title: findLastTime 
	 * @Description: TODO
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findLastTime();

	/** 
	 * @Title: addProtocol 
	 * @Description: TODO
	 * @param listProtocol
	 * @return: void
	 */
}

