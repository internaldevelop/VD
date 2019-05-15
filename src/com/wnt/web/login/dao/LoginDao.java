package com.wnt.web.login.dao;

import java.util.List;
import java.util.Map;

import com.wnt.web.login.entity.UserEntity;

public interface LoginDao {

	
	public int login(UserEntity loginEntity);
	
	public List<Map<String, Object>> queryUser(int id);
	
	public int updateUserPassword(UserEntity loginEntity);
	List<Map<String, Object>> queryUser(String userName);
	public int updateUserLockConfig(UserEntity loginEntity);
}
