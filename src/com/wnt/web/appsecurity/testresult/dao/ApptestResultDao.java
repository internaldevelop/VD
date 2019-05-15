/**   
 * Copyright © 2016 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: ApptestResultDao.java 
 * @Prject: VD
 * @Package: com.wnt.web.appsecurity.testresult.dao 
 * @Description: TODO
 * @author: jfQiao  
 * @date: 2016年9月13日 上午11:18:20 
 * @version: V1.0   
 */
package com.wnt.web.appsecurity.testresult.dao;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ApptestResultDao
 * @Description:
 * @author: jfQiao
 * @date: 2016年9月13日 上午11:18:20
 */
public interface ApptestResultDao {

	/**
	 * 
	 * @Title: findParent
	 * @Description: 获取所有父节点
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	List<Map<String, Object>> findParent();

	/**
	 * 
	 * @Title: updateFilePath
	 * @Description: 修改导出文件的名称与路径
	 * @param testCaseId
	 * @param fileUrl
	 * @param fileName
	 * @return: void
	 */
	void updateFilePath(String testCaseId, String fileUrl, String fileName);

	/**
	 * 
	 * @Title: queryResultById
	 * @Description: 根据ID查询文件相关信息
	 * @param id
	 * @param parent
	 * @return
	 * @return: List<Map<String,Object>>
	 */
	List<Map<String, Object>> queryResultById(String id, boolean parent);

	/**
	 * 
	 * @Title: deleteTestresultById
	 * @Description: 根据ID删除测试结果
	 * @param id
	 * @return
	 * @return: int
	 */
	int deleteTestresultById(String id);

}
