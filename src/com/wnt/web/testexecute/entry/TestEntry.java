package com.wnt.web.testexecute.entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestEntry implements Serializable{
	//模板id
	private int tempId;
	//模板名称
	private String tempName;
	//设备名称
	private String equipName;
	//测试结果id
	private String testResultId;
	
	//测试用例
	List<TestCaseEntry> list=new ArrayList();
	//状态:1未执行，2执行中，3暂停，4完成100，5停止
	private int status=1;
	
	//执行到第几个用例
	private int index;
	
	//执行进度
	private int progress=0;
	//类型：0 无状态1单个执行，2多个执行
	private int type=0;

	//测试模板开始的时间
	private Date beginTime;
	
	//是否是第一次修改
	public static boolean first=true;
	//得到
	public TestCaseEntry next(){
		if(list.size()==0) return null;
		if(index==list.size()){
			return list.get(index-1);
		}
		return list.get(index);
	}
	public void add(){
		index++;
	}
	public boolean isOver(){
		return index==list.size();
	}
	
	
	
	public int getTempId() {
		return tempId;
	}

	public void setTempId(int tempId) {
		this.tempId = tempId;
	}

	public List<TestCaseEntry> getList() {
		return list;
	}

	public void setList(List<TestCaseEntry> list) {
		this.list = list;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getProgress() {
		if(progress==101){
			return 100;
		}
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public String getTempName() {
		return tempName;
	}
	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
	public String getEquipName() {
		return equipName;
	}
	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}
	public String getTestResultId() {
		return testResultId;
	}
	public void setTestResultId(String testResultId) {
		this.testResultId = testResultId;
	}
	
	
	
	
	
	
	
	
	
	
	
}
