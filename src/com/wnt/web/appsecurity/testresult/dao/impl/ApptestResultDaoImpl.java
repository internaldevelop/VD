/**   
 * Copyright © 2016 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: ApptestResultDaoImpl.java 
 * @Prject: VD
 * @Package: com.wnt.web.appsecurity.testresult.dao.impl 
 * @Description: TODO
 * @author: jfQiao  
 * @date: 2016年9月13日 上午11:18:51 
 * @version: V1.0   
 */
package com.wnt.web.appsecurity.testresult.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wnt.web.appsecurity.testresult.dao.ApptestResultDao;

/**
 * @ClassName: ApptestResultDaoImpl
 * @Description:
 * @author: jfQiao
 * @date: 2016年9月13日 上午11:18:51
 */
@Repository("apptestResultDao")
public class ApptestResultDaoImpl implements ApptestResultDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	/*
	 * (non Javadoc)
	 * 
	 * @Title: findParent
	 * 
	 * @Description: 获取所有父节点
	 * 
	 * @return
	 * 
	 * @see com.wnt.web.appsecurity.testresult.dao.ApptestResultDao#findParent()
	 */
	@Override
	public List<Map<String, Object>> findParent() {
		String sql = "SELECT ts.ID AS id, ts.PARENTID AS pId, ts.EQUIPMENTNAME AS name,ts.DELSTATUS AS del "
				+ " FROM LDWJ_TESTRESULT ts  where flag = 2 ORDER BY ts.CREATETIME DESC";
		return jdbcTemplate.queryForList(sql);
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
	 * @see
	 * com.wnt.web.appsecurity.testresult.dao.ApptestResultDao#updateFilePath
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateFilePath(String testCaseId, String fileUrl, String fileName) {
		String sql = "";
		if (fileName != null) {
			sql = " UPDATE LDWJ_TESTRESULT SET FILEURL='" + fileUrl + "',FILENAME='" + fileName + "' WHERE ID='"
					+ testCaseId + "' ";
		} else {
			sql = " UPDATE LDWJ_TESTRESULT SET FILEURL='" + fileUrl + "',FILENAME=EQUIPMENTNAME WHERE ID='" + testCaseId
					+ "' ";
		}

		jdbcTemplate.update(sql);
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
	 * @see
	 * com.wnt.web.appsecurity.testresult.dao.ApptestResultDao#queryResultById
	 * (java.lang.String, boolean)
	 */
	@Override
	public List<Map<String, Object>> queryResultById(String id, boolean parent) {
		String sql = "";
		if (parent) {
			sql = " select *  from LDWJ_TESTRESULT where PARENTID='" + id + "'";
		} else {
			sql = " select *  from LDWJ_TESTRESULT where ID='" + id + "' ";
		}
		return jdbcTemplate.queryForList(sql);
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
	 * @see com.wnt.web.appsecurity.testresult.dao.ApptestResultDao#
	 * deleteTestresultById(java.lang.String)
	 */
	@Override
	public int deleteTestresultById(String id) {
		if (StringUtils.isNotBlank(id)) {
			String sql = "delete from ldwj_testresult where id = ? or PARENTID = ?";
			int update = jdbcTemplate.update(sql, new Object[] { id, id });
			String sql_find = "select count(*) from ldwj_testresult where PARENTID = 'appsecurityTest' and flag = 2"; 
			int children = jdbcTemplate.queryForObject(sql_find, Integer.class);
			if(children < 1){
				String sql_orphan = "delete from ldwj_testresult where id = 'appsecurityTest'";
				jdbcTemplate.update(sql_orphan);
			}
			return update;
		}
		return 0;
	}
}
