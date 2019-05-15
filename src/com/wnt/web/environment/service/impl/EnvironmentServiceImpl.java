package com.wnt.web.environment.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.wnt.core.uitl.StringUtil;

import com.wnt.web.environment.dao.EnvironmentDao;
import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.environment.entry.MonitorDetailEntry;
import com.wnt.web.environment.service.EnvironmentService;
import com.wnt.web.portscan.service.PortscanService;

import common.EquipmentDef;
import common.TestExecuteUtil;


@Service("environmentService")
public class EnvironmentServiceImpl implements EnvironmentService{
	@Resource
	EnvironmentDao environmentDao;
	
	@Resource
	PortscanService portscanService;

	@Override
	public Map<String, Object> findEnvironmentByEquipmentId(String equipmentId) {
		return environmentDao.findEnvironmentByEquipmentId(equipmentId);
	}

	@Override
	public List<Map<String, Object>> findMonitor(Integer equipmentId) {
		return environmentDao.findMonitor(equipmentId);
	}

	@Override
	public void update1(EquipmentEntry env,String old_name) {
		
	    environmentDao.update1(env);
		//当修改设备名称时清空端口扫描数据
		if(!env.getName().equals(old_name)){
			//删除所有的扫描端口记录，不删除手动添加的
			portscanService.deleteAllPort();
			//标记修改过设备名称
			EquipmentDef.update=true;
			if(TestExecuteUtil.testEntry!=null){
				TestExecuteUtil.testEntry.setEquipName(env.getName());
			}else{
				
			}
		}
	}
	@Override
	public void update2(EquipmentEntry env) {
		environmentDao.update2(env);
		
	}
	@Override
	public void update3(EquipmentEntry env) {
		environmentDao.update3(env);
		
	}

	@Override
	public Map<String, Object> findMonitorById(Integer id) {
		// TODO Auto-generated method stub
		return environmentDao.findMonitorById(id);
	}

	@Override
	public void updateMonitorDetail(MonitorDetailEntry md) {
		environmentDao.updateMonitorDetail(md);
		
	}

	@Override
	public void selectMinitor(String selected, String envId_s, String mid_s) {
		//将id转换成数字类型
		int envId=Integer.valueOf(envId_s);
		int mid=Integer.valueOf(mid_s);
		environmentDao.selectMinitor(selected, envId, mid);
	}

	@Override
	public List<Map<String, Object>> findSelectMonitor(Integer equipmentId) {
		// TODO Auto-generated method stub
		return environmentDao.findSelectMonitor(equipmentId);
	}

	@Override
	public List<Map<String, Object>> findMonitorDetail(Integer equipmentId) {
		// TODO Auto-generated method stub
		return environmentDao.findMonitorDetail(equipmentId);
	}
	
	public void updateStatus(String status){
		environmentDao.updateStatus(status);
	}
	
	public void updateEth1(String ck){
		environmentDao.updateEth1(ck);
	}
}
