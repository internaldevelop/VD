package com.wnt.web.login.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wnt.web.login.dao.LoginDao;
import com.wnt.web.login.entity.UserEntity;
import com.wnt.web.login.service.LoginService;

@Service("loginService")
public class LoginServiceImpl implements LoginService{

	@Resource
	private LoginDao loginDao;
	@Override
	public boolean login(UserEntity loginEntity) {
		// TODO Auto-generated method stub
		int success = loginDao.login(loginEntity);
		if(success>0){
			return true;
		}
		return false;
	}
	@Override
	public UserEntity queryUser(int id) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> userList = loginDao.queryUser(id);
		if(userList.size()>0){
			UserEntity userEntity = new UserEntity();
			userEntity.setId(Integer.parseInt(userList.get(0).get("ID").toString()));
			userEntity.setName(userList.get(0).get("NAME").toString());
			userEntity.setPassword(userList.get(0).get("PASSWORD").toString());
			userEntity.setMaxErrorCount(Integer.valueOf(userList.get(0).get("MAXERRORCOUNT").toString()));
			userEntity.setMaxLockTime(Integer.valueOf(userList.get(0).get("MAXLOCKTIME").toString()));
			return userEntity;
		}
		return null;
	}
	@Override
	public boolean updateUserPassword(UserEntity loginEntity) {
		// TODO Auto-generated method stub
		int success = loginDao.updateUserPassword(loginEntity);
		if(success > 0){
			return true;
		}
		return false;
	}
	
	@Override
    public UserEntity queryUser(String userName) {
        // TODO Auto-generated method stub
        List<Map<String, Object>> userList = loginDao.queryUser(userName);
        if(userList.size()>0){
            UserEntity userEntity = new UserEntity();
            userEntity.setId(Integer.parseInt(userList.get(0).get("ID").toString()));
            userEntity.setName(userList.get(0).get("NAME").toString());
            userEntity.setPassword(userList.get(0).get("PASSWORD").toString());
            return userEntity;
        }
        return null;
    }
	
	@Override
    public void updateUserLockConfig(UserEntity loginEntity) {
        // TODO Auto-generated method stub
        loginDao.updateUserLockConfig(loginEntity);     
    }
}
