package com.wnt.web.testexecute.entry;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestCaseEntry implements Serializable {
	// 测试用例的id
	private int id;
	// 测试用例的状态：1未执行，2执行中，3暂停，4完成，5停止
	private int caseStatus =1;
	private String name;
	private String code;

	private String cssImg;

	private Date beginTime;

	private Integer type;
	private Integer installtype;
	private String remark;

	private String testResultId;
	// 保存监视器的查询结果
	private Map<String, String> monitorData = new HashMap();

	public int getCode1() {
		return Integer.valueOf(code.split("-")[0]);
	}

	public int getCode2() {
		return Integer.valueOf(code.split("-")[1]);
	}

	public void start() {
		caseStatus = 2;
	}

	public void end() {
		caseStatus = 5;
	}

	public String getCssImg() {
		if (caseStatus == 1) {
			return "a_ks";
		} else if (caseStatus == 2) {
			return "a_ing";
		} else if (caseStatus == 3) {
			return "a_continue";
		} else if (caseStatus == 4) {
			return "a_ok";
		} else if (caseStatus == 5) {
			return "a_ks";
		}
		return "";
	}

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

	public int getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(int caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Map<String, String> getMonitorData() {
		return monitorData;
	}

	public void setMonitorData(Map<String, String> monitorData) {
		this.monitorData = monitorData;
	}

	public void setCssImg(String cssImg) {
		this.cssImg = cssImg;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public String getTestResultId() {
		return testResultId;
	}

	public void setTestResultId(String testResultId) {
		this.testResultId = testResultId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getInstalltype() {
		return installtype;
	}

	public void setInstalltype(Integer installtype) {
		this.installtype = installtype;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
