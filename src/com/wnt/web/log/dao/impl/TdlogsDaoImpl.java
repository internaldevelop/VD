package com.wnt.web.log.dao.impl;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.web.log.dao.TdlogsDao;
import com.wnt.web.log.entry.Tdlogs;

@Repository
public class TdlogsDaoImpl implements TdlogsDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	public void insertLog(Tdlogs tdlogs) {
		String sql = "insert into td_logs(createname,module,createtime,content,operation,ip,url,type) values(?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(
				sql,
				new Object[] { tdlogs.getUsername(), tdlogs.getModule(),
						tdlogs.getCreatetime(), tdlogs.getContent(),
						tdlogs.getOperation(), tdlogs.getIp(), tdlogs.getUrl(),
						tdlogs.getType() });
	}
}
