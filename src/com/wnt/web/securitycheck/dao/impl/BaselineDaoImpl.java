package com.wnt.web.securitycheck.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.web.securitycheck.dao.BaselineDao;
import com.wnt.web.securitycheck.entry.Baseline;

/**
 * 安全检查配置基线Dao接口实现
 * @author gyk
 */
@Repository("baselineDao")
public class BaselineDaoImpl implements BaselineDao{

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int insert(Baseline... baselines) {
		if(baselines==null || baselines.length == 0){
			return 0;
		}
		StringBuffer sqlbuf = new StringBuffer("INSERT INTO security_check_baseline(ID,BL_NAME,VALS,EXPLAINS) VALUES");
		int length = baselines.length;
		int maxIndex = length - 1;
		List<Object> params = new ArrayList<Object>();
		for(int i = 0;i < length; i++){
			if(i < maxIndex){
				sqlbuf.append("(?,?,?,?),");
			}else{
				sqlbuf.append("(?,?,?,?)");
			}
			Baseline bl = baselines[i];
			if(bl.getId()==null || "".equals(bl.getId())){
				params.add(UUIDGenerator.generate());
			}else{
				params.add(bl.getId());
			}
			params.add(bl.getName());
			params.add(bl.getValue());
			params.add(bl.getExplain());
		}
		return jdbcTemplate.update(sqlbuf.toString(),params.toArray());
	}

	@Override
	public void delete(String id) {
		jdbcTemplate.update("DELETE FROM security_check_baseline WHERE ID = ?",id);
	}

	@Override
	public void update(Baseline baseline) {
		jdbcTemplate.update("UPDATE security_check_baseline SET BL_NAME = ?,VALS=?,EXPLAINS=? WHERE ID = ?", baseline.getName(),baseline.getValue(),baseline.getExplain(),baseline.getId());
	}

	@Override
	public List<Baseline> findAll() {
		return jdbcTemplate.query("SELECT * FROM security_check_baseline", new BaselineMapper());
	}

	@Override
	public int clear() {
		return jdbcTemplate.update("DELETE FROM security_check_baseline");
	}

	@Override
	public Baseline get(String id) {
		try {
			return jdbcTemplate.queryForObject("SELECT * FROM security_check_baseline WHERE ID = ?", new BaselineMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}	
	}

	@Override
	public List<Baseline> findByName(String name) {
		return jdbcTemplate.query("SELECT * FROM security_check_baseline WHERE BL_NAME = ?",new BaselineMapper(),name);
	}

	@Override
	public List<Baseline> findByValue(String value) {
		return jdbcTemplate.query("SELECT * FROM security_check_baseline WHERE VALS = ?",new BaselineMapper(),value);
	}
	
	 class BaselineMapper implements RowMapper<Baseline>{
		@Override
		public Baseline mapRow(ResultSet rs, int arg1) throws SQLException {
			Baseline bl = new Baseline();
			bl.setId(rs.getString("ID"));
			bl.setName(rs.getString("BL_NAME"));
			bl.setValue(rs.getString("VALS"));
			bl.setExplain(rs.getString("EXPLAINS"));
			return bl;
		}
	 }

}
