package com.wnt.web.securitycheck.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.wnt.web.securitycheck.dao.BaselineDao;
import com.wnt.web.securitycheck.entry.Baseline;
import com.wnt.web.securitycheck.entry.BaselineDto;
import com.wnt.web.securitycheck.service.BaselineService;

/**
 * 安全检查配置基线service接口实现
 * @author gyk
 */
@Service("baselineService")
public class BaselineServiceImpl implements BaselineService {
	
	@Resource
	private BaselineDao baselineDao;

	@Override
	public int insert(Baseline... baselines) {
		return baselineDao.insert(baselines);
	}

	@Override
	public void delete(String id) {
		baselineDao.delete(id);
	}

	@Override
	public void update(Baseline baseline) {
		baselineDao.update(baseline);
	}

	@Override
	public List<Baseline> findAll() {
		return baselineDao.findAll();
	}

	@Override
	public int clear() {
		return baselineDao.clear();
	}

	@Override
	public Baseline get(String id) {
		return baselineDao.get(id);
	}

	@Override
	public List<Baseline> findByName(String name) {
		return baselineDao.findByName(name);
	}

	@Override
	public List<Baseline> findByValue(String value) {
		return baselineDao.findByValue(value);
	}
	
	@Override
	public int clearInsert(BaselineDto dto){
		clear();
		if(dto==null || dto.getNames()==null){
			return 0;
		}
		int length =  dto.getNames().length;
		if(length==0){
			return 0;
		}
		Baseline[] bls = new Baseline[length];
		for(int i = 0;i < length;i++){
			Baseline bl = new Baseline();
			bl.setName(dto.getNames()[i]);
			bl.setValue(dto.getValues()[i]);
			if(dto.getExplains()!=null){
				bl.setExplain(dto.getExplains()[i]);
			};
			bls[i] = bl;
		}
		return insert(bls);
	};

}
