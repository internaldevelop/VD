/**   
 * Copyright © 2016 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: ApptestResultServiceImpl.java 
 * @Prject: VD
 * @Package: com.wnt.web.appsecurity.testresult.service.impl 
 * @Description: 
 * @author: jfQiao  
 * @date: 2016年9月13日 上午11:19:45 
 * @version: V1.0   
 */
package com.wnt.web.appsecurity.testresult.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wnt.web.appsecurity.testresult.dao.ApptestResultDao;
import com.wnt.web.appsecurity.testresult.service.ApptestResultService;

/**
 * @ClassName: ApptestResultServiceImpl
 * @Description:
 * @author: jfQiao
 * @date: 2016年9月13日 上午11:19:45
 */
@Service("apptestResultService")
public class ApptestResultServiceImpl implements ApptestResultService {

	@Resource
	private ApptestResultDao apptestResultDao;

	/*
	 * (non Javadoc)
	 * 
	 * @Title: findParent
	 * 
	 * @Description: 获取所有父节点
	 * 
	 * @return
	 * 
	 * @see com.wnt.web.appsecurity.testresult.service.ApptestResultService#
	 * findParent ()
	 */
	@Override
	public List<Map<String, Object>> findParent() {
		List<Map<String, Object>> list = apptestResultDao.findParent();
		String pid = "";
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			if (null == map) {
				continue;
			}
			if (map.get("c") != null) {
				pid = map.get("pId").toString();
			} else {
				if (map.get("pId") == null && map.get("id").toString().equals(pid)) {
					map.put("c", "1");
				}
			}
		}
		return list;
	}

	/*
	 * (non Javadoc)
	 * 
	 * @Title: updateFilePath
	 * 
	 * @Description: 修改导出文件的名称与路径
	 * 
	 * @param testCaseId
	 * 
	 * @param fileUrl
	 * 
	 * @param fileName
	 * 
	 * @see com.wnt.web.appsecurity.testresult.service.ApptestResultService#
	 * updateFilePath(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateFilePath(String testCaseId, String fileUrl, String fileName) {
		apptestResultDao.updateFilePath(testCaseId, fileUrl, fileName);
	}

	/*
	 * (non Javadoc)
	 * 
	 * @Title: queryResultById
	 * 
	 * @Description: 根据ID查询文件相关信息
	 * 
	 * @param id
	 * 
	 * @param parent
	 * 
	 * @return
	 * 
	 * @see com.wnt.web.appsecurity.testresult.service.ApptestResultService#
	 * queryResultById(java.lang.String, boolean)
	 */
	@Override
	public List<Map<String, Object>> queryResultById(String id, boolean parent) {
		return apptestResultDao.queryResultById(id, parent);
	}

	/*
	 * (non Javadoc)
	 * 
	 * @Title: deleteTestresultById
	 * 
	 * @Description: 根据ID删除测试结果
	 * 
	 * @param id
	 * 
	 * @return
	 * 
	 * @see com.wnt.web.appsecurity.testresult.service.ApptestResultService#
	 * deleteTestresultById(java.lang.String)
	 */
	@Override
	public int deleteTestresultById(String id) {
		return apptestResultDao.deleteTestresultById(id);
	}

}
