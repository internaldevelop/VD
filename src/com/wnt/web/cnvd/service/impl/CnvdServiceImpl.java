package com.wnt.web.cnvd.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.commons.page.WntPage;
import com.wnt.web.cnvd.dao.CnvdDao;
import com.wnt.web.cnvd.entry.Cnvd;
import com.wnt.web.cnvd.service.CnvdService;

@Repository("cnvdService")
public class CnvdServiceImpl implements CnvdService {
	
	@Resource
	private CnvdDao cnvdDao;

	@Override
	public List<Cnvd> findAll() {
		return cnvdDao.findAll();
	}

	@Override
	public Cnvd getCnvd(Integer id) {
		return cnvdDao.getCnvd(id);
	}

	@Override
	public Cnvd getCnvd(String cnvdId) {
		return cnvdDao.getCnvd(cnvdId);
	}

	@Override
	public List<Cnvd> findByCnvdName(String cnvdId) {
		return cnvdDao.findByCnvdName(cnvdId);
	}
	
	@Override
	public List<Cnvd> findFuzzyByCnvdName(String cnvdId) {
		return cnvdDao.findFuzzyByCnvdName(cnvdId);
	}

	@Override
	public List<Cnvd> findByCveId(String cveId) {
		return cnvdDao.findByCveId(cveId);
	}

	@Override
	public List<Cnvd> findByCnvdIdAndCveId(String cnvdId, String cveId) {
		return cnvdDao.findByCnvdIdAndCveId(cnvdId, cveId);
	}

	@Override
	public int save(Cnvd cnvd) {
		return cnvdDao.save(cnvd);
	}

	@Override
	public int update(Cnvd cnvd) {
		return cnvdDao.update(cnvd);
	}

	@Override
	public int delete(Integer id) {
		return cnvdDao.delete(id);
	}

	@Override
	public int deleteByCnvdId(String cnvdId) {
		return cnvdDao.deleteByCnvdId(cnvdId);
	}

	@Override
	public int deleteByCveId(String cveId) {
		return cnvdDao.deleteByCveId(cveId);
	}

	@Override
	public WntPage<Cnvd> findPageAll(WntPage<Cnvd> page) {
		return cnvdDao.findPageAll(page);
	}

	@Override
	public WntPage<Cnvd> findFuzzyByCnvdName(WntPage<Cnvd> page, String cnvdName) {
		return cnvdDao.findFuzzyByCnvdName(page, cnvdName);
	}

	@Override
	public WntPage<Cnvd> findFuzzy(WntPage<Cnvd> page, String cnvdId,
			String cnvdName) {
		return cnvdDao.findFuzzy(page, cnvdId, cnvdName);
	}
}
