
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: ProtocolIdentityDao.java 
 * @Prject: VD
 * @Package: com.wnt.web.protocol.dao 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-5-2 下午3:47:42 
 * @version: V1.0   
 */ package com.wnt.web.protocol.dao; import java.util.List;
import java.util.Map;

import com.wnt.web.protocol.entry.Protocol;

/** 
 * @ClassName: ProtocolIdentityDao 
 * @Description: TODO
 * @author: gyj
 * @date: 2017-5-2 下午3:47:42  
 */
public interface ProtocolIdentityDao {

	/** 
	 * @Title: findProtocolResult 
	 * @Description: TODO
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findProtocolResult(String time);

	/** 
	 * @Title: deleteProtocol 
	 * @Description: TODO
	 * @param id
	 * @return: void
	 */
	public void deleteProtocol(String id);

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
	 * @return: void
	 */
	public List<Map<String, Object>> findAll();

	/** 
	 * @Title: findLastTime 
	 * @Description: TODO
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String, Object>> findLastTime();

}

