package com.wnt.web.testdeplay.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wnt.web.testdeplay.dao.TestDeplayDao;
import com.wnt.web.testdeplay.entry.DeplayEntry;
import com.wnt.web.testdeplay.service.TestDeplayService;


@Service
public class TestDeplayServiceImpl implements TestDeplayService{
	@Resource
	private TestDeplayDao testDeplayDao;

	@Override
	public List<DeplayEntry> findztreeList(int type) {
		return testDeplayDao.findztreeList(type);
	}

	@Override
	public void saveDeplay(DeplayEntry vo) {
		testDeplayDao.saveDeplay(vo);
	}

	@Override
	public List<Map<String, Object>> findforlist() {
		return testDeplayDao.findforlist();	}
	
	
}