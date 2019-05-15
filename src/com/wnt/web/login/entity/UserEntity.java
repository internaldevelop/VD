package com.wnt.web.login.entity;

public class UserEntity {

	private int id;
	private String name;
	private String password;
	private int maxErrorCount = 5;
	private int maxLockTime = 5; //minute
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    public int getMaxErrorCount() {
        return maxErrorCount;
    }
    public void setMaxErrorCount(int maxErrorCount) {
        this.maxErrorCount = maxErrorCount;
    }
    public int getMaxLockTime() {
        return maxLockTime;
    }
    public void setMaxLockTime(int maxLockTime) {
        this.maxLockTime = maxLockTime;
    }
    
	
}
