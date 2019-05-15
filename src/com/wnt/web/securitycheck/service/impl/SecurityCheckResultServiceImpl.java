package com.wnt.web.securitycheck.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.commons.page.WntPage;
import com.wnt.web.securitycheck.dao.SecurityCheckResultDao;
import com.wnt.web.securitycheck.entry.ResultBaseline;
import com.wnt.web.securitycheck.entry.SecurityCheckResult;
import com.wnt.web.securitycheck.service.SecurityCheckResultService;

@Service("securityCheckResultService")
public class SecurityCheckResultServiceImpl implements
		SecurityCheckResultService {
	
	@Resource
	SecurityCheckResultDao securityCheckResultDao;

	@Override
	public int insartResultBaseline(List<ResultBaseline> resultBaselines) {
		return securityCheckResultDao.insartResultBaseline(resultBaselines);
	}

	@Override
	public int insartResult(List<SecurityCheckResult> securityCheckResults) {
		return securityCheckResultDao.insartResult(securityCheckResults);
	}

	@Override
	public List<SecurityCheckResult> findByEIdAndCreateTime(String equipmentId,
			Date createTime, int delStatus) {
		return securityCheckResultDao.findByEIdAndCreateTime(equipmentId, createTime, delStatus);
	}

	@Override
	public WntPage<SecurityCheckResult> findAllValid(
			WntPage<SecurityCheckResult> page) {
		return securityCheckResultDao.findAllValid(page);
	}

	@Override
	public WntPage<SecurityCheckResult> findByDelStatus(
			WntPage<SecurityCheckResult> page, Integer delstatus) {
		return securityCheckResultDao.findByDelStatus(page, delstatus);
	}

	@Override
	public List<ResultBaseline> findByResultId(String resultId) {
		return securityCheckResultDao.findByResultId(resultId);
	}

	@Override
	public void delResultById(String id) {
		securityCheckResultDao.delResultById(id);

	}

}
