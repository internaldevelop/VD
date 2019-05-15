package com.wnt.web.securitycheck.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.commons.page.WntPage;
import com.wnt.web.securitycheck.dao.SecurityCheckResultDao;
import com.wnt.web.securitycheck.entry.ResultBaseline;
import com.wnt.web.securitycheck.entry.SecurityCheckResult;

@Repository("securityCheckResultDao")
public class SecurityCheckResultDaoImpl implements SecurityCheckResultDao {
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int insartResultBaseline(List<ResultBaseline> resultBaselines) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insartResult(List<SecurityCheckResult> securityCheckResults) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delResultById(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SecurityCheckResult get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SecurityCheckResult> findByEIdAndCreateTime(String equipmentId,
			Date createTime, int delStatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WntPage<SecurityCheckResult> findAllValid(
			WntPage<SecurityCheckResult> page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WntPage<SecurityCheckResult> findByDelStatus(
			WntPage<SecurityCheckResult> page, Integer delstatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ResultBaseline> findByResultId(String resultId) {
		// TODO Auto-generated method stub
		return null;
	}

}
