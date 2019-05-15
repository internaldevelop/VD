
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: ChartHistoryDataDO.java 
 * @Prject: VD
 * @Package: com.wnt.web.testexecute.entry 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-5-23 下午2:52:53 
 * @version: V1.0   
 */ package com.wnt.web.testexecute.entry; /** 
 * @ClassName: ChartHistoryDataDO 
 * @Description: 
 * @author: gyj
 * @date: 2017-5-23 下午2:52:53  
 */
public class ChartHistoryDataDO {

	private int type;
	private Object[] dataArray;
	
	/** 
	 * @Title:ChartHistoryDataDO
	 * @Description:TODO  
	 */
	public ChartHistoryDataDO() {

	}
	
	/** 
	 * @Title:ChartHistoryDataDO
	 * @Description:TODO 
	 * @param type
	 * @param dataArray 
	 */
	public ChartHistoryDataDO(int type, Object[] dataArray) {
		super();
		this.type = type;
		this.dataArray = dataArray;
	}





	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object[] getDataArray() {
		return dataArray;
	}
	public void setDataArray(Object[] dataArray) {
		this.dataArray = dataArray;
	}
	
	
	
}

