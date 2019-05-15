
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: ProtocolIdentityDaoImpl.java 
 * @Prject: VD
 * @Package: com.wnt.web.protocol.dao.impl 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-5-2 下午3:48:05 
 * @version: V1.0   
 */ package com.wnt.web.protocol.dao.impl; import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.web.protocol.dao.ProtocolIdentityDao;
import com.wnt.web.protocol.entry.Protocol;

/** 
 * @ClassName: ProtocolIdentityDaoImpl 
 * @Description: TODO
 * @author: gyj
 * @date: 2017-5-2 下午3:48:05  
 */
 @Repository("protocolIdentityDao")
public class ProtocolIdentityDaoImpl implements ProtocolIdentityDao{
	 @Resource
	private JdbcTemplate jdbcTemplate;
	 
	 @Override
	public List<Map<String, Object>> findProtocolResult(String time) {
		String sql = "SELECT * FROM LDWH_PROTOCOL WHERE  CREATETIME > "+"'"+time+"'"+" ORDER BY IDENTYTIME DESC";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}

	/* (non Javadoc) 
	 * @Title: deleteProtocol
	 * @Description: TODO
	 * @param id 
	 * @see com.wnt.web.protocol.dao.ProtocolIdentityDao#deleteProtocol(java.lang.String) 
	 */
	@Override
	public void deleteProtocol(String id) {
		String sql = "DELETE FROM LDWH_PROTOCOL WHERE ID=?";
		jdbcTemplate.update(sql, id);
	}
	/* (non Javadoc) 
	 * @Title: addProtocol
	 * @Description: TODO
	 * @param listProtocol 
	 * @see com.wnt.web.socket.dao.SocketDao#addProtocol(java.util.List) 
	 */
	@Override
	public void addProtocol(final List<Protocol> listProtocol) {
		String sql = "INSERT INTO LDWH_PROTOCOL (ID,PROTOCOLNAME,SRCIP,DSTIP,PORT,IDENTYTIME,CREATETIME,DELETESTATUS) VALUES(?,?,?,?,?,?,NOW(),0)";
		jdbcTemplate.batchUpdate(sql,
			      new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	Protocol protocol = listProtocol.get(i);
			        	ps.setString(1, UUIDGenerator.getUUID());
			        	ps.setString(2, protocol.getProtocolName());
			            ps.setString(3, protocol.getSrcIp());
			            ps.setString(4, protocol.getDstIp());
			            ps.setLong(5, protocol.getPort());
			            ps.setString(6, protocol.getIdentyTime());
			        }
			          public int getBatchSize() {
			            return listProtocol.size();
			          }
			        });
	
		
	}

	/* (non Javadoc) 
	 * @Title: deleteProtocol
	 * @Description: TODO 
	 * @see com.wnt.web.protocol.dao.ProtocolIdentityDao#deleteProtocol() 
	 */
	@Override
	public void deleteProtocol() {
		
		String sql = "DELETE  FROM LDWH_PROTOCOL";
		jdbcTemplate.update(sql);
	}

	/* (non Javadoc) 
	 * @Title: findAll
	 * @Description: TODO 
	 * @see com.wnt.web.protocol.dao.ProtocolIdentityDao#findAll() 
	 */
	@Override
	public List<Map<String,Object>> findAll() {
		String sql = "SELECT *   FROM LDWH_PROTOCOL";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
		return list;
		
	}

	/* (non Javadoc) 
	 * @Title: findLastTime
	 * @Description: TODO
	 * @return 
	 * @see com.wnt.web.protocol.dao.ProtocolIdentityDao#findLastTime() 
	 */
	@Override
	public List<Map<String, Object>> findLastTime() {
		String sql = "SELECT MAX(CREATETIME)  FROM LDWH_PROTOCOL ";
		return jdbcTemplate.queryForList(sql);
	}

}

