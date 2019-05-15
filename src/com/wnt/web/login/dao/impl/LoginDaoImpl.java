package com.wnt.web.login.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wnt.web.login.dao.LoginDao;
import com.wnt.web.login.entity.UserEntity;

@Repository("loginDao")
public class LoginDaoImpl implements LoginDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public int login(UserEntity loginEntity) {
		// TODO Auto-generated method stub
		String sql = "SELECT COUNT(*) FROM LDWJ_USER WHERE NAME = ? AND PASSWORD = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] {loginEntity.getName() , loginEntity.getPassword()}, Integer.class);
	}

	@Override
	public List<Map<String, Object>> queryUser(int id) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM LDWJ_USER WHERE ID = ?";
		return jdbcTemplate.queryForList(sql, id);
	}

	@Override
	public int updateUserPassword(UserEntity loginEntity) {
		// TODO Auto-generated method stub
		String sql = "UPDATE LDWJ_USER SET PASSWORD = ? WHERE NAME = ?";
		return jdbcTemplate.update(sql, loginEntity.getPassword(), loginEntity.getName());
	}
	
	@Override
    public List<Map<String, Object>> queryUser(String userName) {
        // TODO Auto-generated method stub
        String sql = "SELECT * FROM LDWJ_USER WHERE NAME = ?";
        return jdbcTemplate.queryForList(sql, userName);
    }
	
	@Override
    public int updateUserLockConfig(UserEntity loginEntity) {
        // TODO Auto-generated method stub
        String sql = "UPDATE LDWJ_USER SET MAXERRORCOUNT = ?, MAXLOCKTIME=?";
        return jdbcTemplate.update(sql, loginEntity.getMaxErrorCount(), loginEntity.getMaxLockTime());
    }
}
