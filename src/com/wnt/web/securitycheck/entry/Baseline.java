package com.wnt.web.securitycheck.entry;

/**
 * 安全检查配置基线
 * @author gyk
 *
 */
public class Baseline {
	
	/**
	 * 主键ID
	 */
	private String id;
	
	/**
	 * 选项名称
	 */
	private String name;
	
	/**
	 * 值
	 */
	private String value;
	
	/**
	 * 说明
	 */
	private String explain;

	/**
	 * 获取主键ID
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置主键ID
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取主键名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置主键名称
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取值
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置值
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 获取说明
	 * @return
	 */
	public String getExplain() {
		return explain;
	}

	/**
	 * 设置说明
	 * @param explain
	 */
	public void setExplain(String explain) {
		this.explain = explain;
	}
	
}
