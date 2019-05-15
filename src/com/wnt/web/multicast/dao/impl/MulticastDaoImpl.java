package com.wnt.web.multicast.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.wnt.core.uitl.UUIDGenerator;

import com.sun.star.lib.uno.environments.remote.remote_environment;
import com.wnt.web.multicast.dao.MulticastDao;


@Repository("multicastDao")
public class MulticastDaoImpl implements MulticastDao{
	@Resource
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> queryMulticastList() throws Exception {
		String sql = "select * from LDWJ_MULTICAST";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		return list;
	}

	public void createMulticast(String ipAddr) throws Exception {
		String id = UUIDGenerator.getUUID();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		String sql = "insert into LDWJ_MULTICAST (ID,IPADDR,SOURCE,REMARK,CREATETIME) values ('"+id+"','"+ipAddr+"','手动添加','','"+sdf.format(date)+"')";
		String sql = "insert into LDWJ_MULTICAST (ID,IPADDR,SOURCE,REMARK,CREATETIME) values (?, ?,'手动添加','', ?)";
		jdbcTemplate.update(sql, id, ipAddr, sdf.format(date));
	}

	public void deleteMulticast(String id) throws Exception {
		String sql = "delete from LDWJ_MULTICAST where id='"+id+"'";
		jdbcTemplate.execute(sql);
	}
	
	public Boolean findMulticast(String ipAddr) throws Exception{
		String sql = "SELECT * from LDWJ_MULTICAST where IPADDR ='"+ipAddr+"'";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		if(list.size()>0){
			return true;
		}
		return false;
	}

	/* (non Javadoc) 
	 * @Title: deleteMulticastByIp
	 * @Description: TODO
	 * @param ipAddr 
	 * @see com.wnt.web.multicast.dao.MulticastDao#deleteMulticastByIp(java.lang.String) 
	 */
	@Override
	public void deleteMulticast() {
		String sql = "delete from LDWJ_MULTICAST";
		jdbcTemplate.update(sql);
	}

	/* (non Javadoc) 
	 * @Title: findMulticast
	 * @Description: TODO
	 * @return 
	 * @see com.wnt.web.multicast.dao.MulticastDao#findMulticast() 
	 */
	@Override
	public List<Map<String, Object>> findMulticast() {
		
		String sql = "SELECT * from LDWJ_MULTICAST";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		return list;
				
		
	}
	
}
