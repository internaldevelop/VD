package com.wnt.web.login.service;

import com.wnt.web.login.entity.UserEntity;

public interface LoginService {

	public boolean login(UserEntity loginEntity);
	
	public UserEntity queryUser(int id);
	
	public boolean updateUserPassword(UserEntity loginEntity);
	UserEntity queryUser(String userName);
	void updateUserLockConfig(UserEntity loginEntity);
}
