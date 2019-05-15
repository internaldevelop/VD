package com.wnt.web.portscan.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.web.portscan.dao.PortscanDao;
import com.wnt.web.portscan.entry.PortServerEntry;
import com.wnt.web.portscan.service.PortscanService;
@Service("portscanService")
public class PortscanServiceImpl implements PortscanService {
	@Resource
	PortscanDao portscanDao;

	@Override
	public int save(PortServerEntry pse) {
		
		return portscanDao.save(pse);
	}

	@Override
	public List<Map<String, Object>> findScanResult(long num) {
		List<Map<String, Object>> list=portscanDao.findScanResult(num);
		for(Map<String,Object> map:list){
			map.put("PORTTYPEY", map.get("PORTTYPE"));
			map.put("PORTTYPE", PortServerEntry.getPortTypeStr((Integer)map.get("PORTTYPE")));
			map.put("SOURCE", PortServerEntry.getSourceStr((String)map.get("SOURCE")));
			map.put("SCANTYPE", PortServerEntry.getScanTypeStr((Integer)map.get("SCANTYPE")));
		}
		return list;
	}

	@Override
	public boolean checkPort(int port,int portType) {
		// TODO Auto-generated method stub
		return portscanDao.checkPort(port,portType);
	}
	public boolean checkPort2(int port,int portType){
		return portscanDao.checkPort2(port, portType);
	}

	@Override
	public void deletePort(String id) {
		portscanDao.deletePort(id);
	}

	@Override
	public void deleteAllPort() {
		portscanDao.deleteAllPort();
		
	}
	
	@Override
	public List findport(){
		return portscanDao.findport();
	}
	
}
