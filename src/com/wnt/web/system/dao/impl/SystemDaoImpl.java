package com.wnt.web.system.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.web.system.dao.SystemDao;
import com.wnt.web.system.entry.Sys;

@Repository("SystemDao")
public class SystemDaoImpl implements SystemDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	public void interfaceipadd(long ip) {
		String sql = "UPDATE LDWJ_SYS SET INTERFACEIP = ?";
		jdbcTemplate.update(sql, ip);
	}

	public List<Sys> findinterface() {
		String sql = "SELECT * FROM LDWJ_SYS";
		List<Sys> list = this.jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Sys>(Sys.class));
		return list;
	}
	public Map<String, Object> findgetDelchart(){
		String sql = "SELECT * FROM LDWJ_SYSDEL WHERE ID='1'";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list.get(0);
	}
	public void updateDelchart(String str){
		String sql = "UPDATE LDWJ_SYSDEL SET DELTIME = ? WHERE ID='1'";
		jdbcTemplate.update(sql, str);
	}
}
