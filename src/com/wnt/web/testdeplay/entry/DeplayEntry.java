package com.wnt.web.testdeplay.entry;



public class DeplayEntry {
	
	private long id;
	private String name;
	private long pId;	//父节点
	private String type;	//类型：1为测试用例库 2为我的模版3为选中测试用例
	private String remark;
	private String installtype;//设置类型 ：1为风暴测试 2为语法测试
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public long getpId() {
		return pId;
	}
	public void setpId(long pId) {
		this.pId = pId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getInstalltype() {
		return installtype;
	}
	public void setInstalltype(String installtype) {
		this.installtype = installtype;
	}

}
