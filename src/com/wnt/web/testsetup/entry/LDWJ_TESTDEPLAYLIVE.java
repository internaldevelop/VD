package com.wnt.web.testsetup.entry;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Entity;

@Entity
public class LDWJ_TESTDEPLAYLIVE {
	private BigInteger id;
	private BigInteger parent;//父节点
	private int name;//测试库名称
	private String remark;//备注
	private int deltatus;//删除状态
	private Timestamp createtime;//创建时间
	private String code;//编码
	private int type;//类型
	private int installtype;//设置类型.3为用户自定义
	private int testnum;
	public BigInteger getId() {
		return id;
	}
	public void setId(BigInteger id) {
		this.id = id;
	}
	public BigInteger getParent() {
		return parent;
	}
	public void setParent(BigInteger parent) {
		this.parent = parent;
	}
	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getDeltatus() {
		return deltatus;
	}
	public void setDeltatus(int deltatus) {
		this.deltatus = deltatus;
	}
	public Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getInstalltype() {
		return installtype;
	}
	public void setInstalltype(int installtype) {
		this.installtype = installtype;
	}
	public int getTestnum() {
		return testnum;
	}
	public void setTestnum(int testnum) {
		this.testnum = testnum;
	}
	
}
