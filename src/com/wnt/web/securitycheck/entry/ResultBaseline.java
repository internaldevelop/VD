package com.wnt.web.securitycheck.entry;

/**
 * 安全检查结果和基线配置
 * @author gyk
 *
 */
public class ResultBaseline {
	
	/**
	 * Id
	 */
	private String id;
	
	/**
	 * 基线Id
	 */
	private String baselineId;
	
	/**
	 * 结果ID
	 */
	private String resultId;
	
	/**
	 * 实际值
	 */
	private String val;
	
	/**
	 * 选项名称
	 */
	private String baselineName;
	
	
	/**
	 * 基线值
	 */
	private String baselineVal;

	/**
	 * 获取ID
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置ID
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * 获取基线ID
	 * @return
	 */
	public String getBaselineId() {
		return baselineId;
	}

	/**
	 * 设置基线ID
	 * @param baselineId
	 */
	public void setBaselineId(String baselineId) {
		this.baselineId = baselineId;
	}

	/**
	 * 获取检查结果ID
	 * @return
	 */
	public String getResultId() {
		return resultId;
	}

	/**
	 * 设置检查结果ID
	 * @param resultId
	 */
	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	/**
	 * 获取实际值
	 * @return
	 */
	public String getVal() {
		return val;
	}

	/**
	 * 设置实际值
	 * @param val
	 */
	public void setVal(String val) {
		this.val = val;
	}

	/**
	 * 获取基线选项名称
	 * @return
	 */
	public String getBaselineName() {
		return baselineName;
	}

	/**
	 * 设置基线选项名称
	 * @param baselineName
	 */
	public void setBaselineName(String baselineName) {
		this.baselineName = baselineName;
	}

	/**
	 * 获取基线值
	 * @return
	 */
	public String getBaselineVal() {
		return baselineVal;
	}

	/**
	 * 设置基线值
	 * @param baselineVal
	 */
	public void setBaselineVal(String baselineVal) {
		this.baselineVal = baselineVal;
	}
	
	
}
