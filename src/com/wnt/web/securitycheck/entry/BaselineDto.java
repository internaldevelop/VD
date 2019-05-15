package com.wnt.web.securitycheck.entry;

/**
 * 安全检查基线配置
 * @author gyk
 */
public class BaselineDto {
	private String[] names;
	private String[] values;
	private String[] explains;
	public String[] getNames() {
		return names;
	}
	public void setNames(String[] names) {
		this.names = names;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
	public String[] getExplains() {
		return explains;
	}
	public void setExplains(String[] explains) {
		this.explains = explains;
	}
	
	
}
