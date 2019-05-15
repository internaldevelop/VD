package com.wnt.web.testsetup.entry;

import javax.persistence.Entity;

@Entity
public class Ldwj_Testcustom {

	private String testdeplayid;
	private String type;
	private String typecode;
	private String name;
	private int min;
	private int max;
	private int len;
	private int parent; // 用于前端业务逻辑
	
	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public String getTestdeplayid() {
		return testdeplayid;
	}

	public void setTestdeplayid(String testdeplayid) {
		this.testdeplayid = testdeplayid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypecode() {
		return typecode;
	}

	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

}
