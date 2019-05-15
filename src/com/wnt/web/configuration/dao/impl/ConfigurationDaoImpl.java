package com.wnt.web.configuration.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wnt.web.configuration.dao.ConfigurationDao;
import com.wnt.web.configuration.entry.ConfigurationEntry;
@Repository("configurationDao")
public class ConfigurationDaoImpl implements ConfigurationDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public Map<String, Object> queryData() {
		try {
			String sql = "SELECT HOSTIP,NAME,NOTES,TYPE FROM LDWJ_CONFIGURATION";
			
			return jdbcTemplate.queryForMap(sql);
			} catch (Exception e) {
				return null;
			}
	}

	@Override
	public void updateData(ConfigurationEntry entry) {
		String sql = "UPDATE  LDWJ_CONFIGURATION SET HOSTIP = ? ";
		

		jdbcTemplate.update(sql , new Object[]{entry.getHostip()});
	}

	@Override
	public void updateDataList2(ConfigurationEntry entry) {
		String sql = "UPDATE  LDWJ_CONFIGURATION SET NAME = ? ,NOTES=?,TYPE=?";
		

		jdbcTemplate.update(sql , new Object[]{entry.getName(),entry.getNotes(),entry.getType()});
	}
	
	
	
}
