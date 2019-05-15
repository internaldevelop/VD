package com.wnt.web.testdeplay.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.wnt.core.uitl.StringUtil;

import com.wnt.web.testdeplay.dao.TestDeplayDao;
import com.wnt.web.testdeplay.entry.DeplayEntry;

@Repository
public class TestDeplayDaoImpl implements TestDeplayDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	public List<DeplayEntry> findztreeList(int type) {
    		
		String sql = "SELECT * FROM  LDWJ_TESTDEPLAYLIVE WHERE DELTATUS=0 AND TYPE="+type+"";
		    
		
		
		List<DeplayEntry> list = this.jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(DeplayEntry.class));
		return list;
	}

	@Override
	public void saveDeplay(DeplayEntry vo) {
		String sql = "INSERT INTO LDWJ_TESTDEPLAYLIVE ( PID,NAME,CREATETIME,TYPE,INSTALLTYPE ) VALUES(?,?,NOW(),3,?)";
		      
		jdbcTemplate.update(sql, new Object[] {vo.getpId(),vo.getName(),vo.getInstalltype() });
	}

	@Override
	public List<Map<String, Object>> findforlist() {
		String sql = "SELECT * FROM  LDWJ_TESTDEPLAYLIVE WHERE DELTATUS=0 AND TYPE=3";
		return jdbcTemplate.queryForList(sql, new Object[] {  });
	}

	
}
