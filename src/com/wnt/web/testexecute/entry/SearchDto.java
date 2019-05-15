
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: SearchDto.java 
 * @Prject: VD
 * @Package: com.wnt.web.testexecute.entry 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-6-21 下午4:07:12 
 * @version: V1.0   
 */ package com.wnt.web.testexecute.entry; /** 
 * @ClassName: SearchDto 
 * @Description: TODO
 * @author: gyj
 * @date: 2017-6-21 下午4:07:12  
 */
public class SearchDto {

	private String beginDate;
	private String endDate;
	private Integer historyType;
	
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getHistoryType() {
		return historyType;
	}
	public void setHistoryType(Integer historyType) {
		this.historyType = historyType;
	}

}

