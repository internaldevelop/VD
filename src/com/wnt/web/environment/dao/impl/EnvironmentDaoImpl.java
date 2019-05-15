package com.wnt.web.environment.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wnt.web.environment.dao.EnvironmentDao;
import com.wnt.web.environment.entry.EquipmentEntry;
import com.wnt.web.environment.entry.MonitorDetailEntry;
@Repository("environmentDao")
public class EnvironmentDaoImpl implements EnvironmentDao {
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	public Map<String, Object> findEnvironmentByEquipmentId(String equipmentId){
		String sql="select * from LDWJ_EQUIPMENT where EQUIPMENTID=?";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql, equipmentId);
		if(list.size()>0){
			return list.get(0);
		}else{
			System.out.println("未查询到设备信息");
		}
		return null;
		
	}
	public List<Map<String, Object>> findMonitor(Integer equipmentId){
		String sql="SELECT m.*, em.SELECTSTATUS FROM LDWJ_MONITOR m, LDWJ_EQUIPMENTANDMONITOR em WHERE m.ID = em.MONITORID AND equipmentid = ? order by id";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql,equipmentId);
		return list;
		
	}
	
	public List<Map<String, Object>> findMonitorDetail(Integer equipmentId){
		String sql="SELECT m.*, em.SELECTSTATUS,d.* FROM LDWJ_MONITOR m,LDWJ_MONITORDETAIL d, LDWJ_EQUIPMENTANDMONITOR em WHERE m.ID = em.MONITORID and m.id=d.MONITORID AND equipmentid = ?";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql,equipmentId);
		return list;
		
	}
	
	public void update1( EquipmentEntry env){
		String sql="update LDWJ_EQUIPMENT SET NAME=?,VERSION=?,LINKTYPE=?,REMARK=?,ENABLE2=? WHERE ID=?";
		jdbcTemplate.update(sql,env.getName(),env.getVersion(),env.getLinkType(),env.getRemark(),env.getEnable2(), env.getId());
		
	}
	public void update2( EquipmentEntry env){
		String sql="update LDWJ_EQUIPMENT SET ip=?,mac=?,subnetMask=?,ip2=?,mac2=?,subnetMask2=?,enable=?,ETH0_EXIST=? WHERE ID=?";
		jdbcTemplate.update(sql,env.getIp(),env.getMac(),env.getSubnetMask(),
				env.getIp2(),env.getMac2(),env.getSubnetMask2(),env.getEnable2(),env.getIsEth0Exist(),env.getId());
		
	}
	public void update3( EquipmentEntry env){
		String sql="update LDWJ_EQUIPMENT SET ip3=?,mac3=?,subnetMask3=?,enable2=?,ETH1_EXIST=? WHERE ID=?";
		jdbcTemplate.update(sql,
				env.getIp3(),env.getMac3(),env.getSubnetMask3(),env.getEnable2(),env.getIsEth1Exist(),env.getId());
		
	}
	@Override
	public Map<String, Object> findMonitorById(Integer id) { 
		String sql="select D.*,M.TYPE from LDWJ_MONITORDETAIL D,LDWJ_MONITOR M where M.ID=D.MONITORID AND D.MONITORID=?";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql, id);
		if(list.size()>0){
			return list.get(0);
		}else{
			System.out.println("未查询到监视器信息");
		}
		return null;
	}
	//修改监视器明细
	@Override
	public void updateMonitorDetail(MonitorDetailEntry md) {
		String sql="update LDWJ_MONITORDETAIL SET overtime=?,cyclePeriod=?,input=?,tcpports=?,alarmLevel=? WHERE monitorId=?";
		jdbcTemplate.update(sql,md.getOvertime(),md.getCyclePeriod(),md.getInput(),md.getTcpports(),md.getAlarmLevel(),md.getMonitorId());
	}
	@Override
	public void selectMinitor(String selected, int envId, int mid) {
		String sql="update LDWJ_EQUIPMENTANDMONITOR SET selectstatus=? WHERE EQUIPMENTID=? and MONITORID=?";
		jdbcTemplate.update(sql,selected,envId,mid);
	}
	/**
	 * 取得设备选择了哪些监视器
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findSelectMonitor(Integer equipmentId) {
		String sql="SELECT m.id, m. NAME FROM LDWJ_MONITOR m, LDWJ_EQUIPMENTANDMONITOR em WHERE m.ID = em.MONITORID AND equipmentid = ? AND SELECTSTATUS = 1";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql,equipmentId);
		
		return list;
	}
	/**
	 * 插入测试模板（废弃）
	 * @param templateName
	 */
	@Override
	public void insertTemplate(String templateName) {
		String sql="insert into LDWJ_TESTDEPLAYLIVE(NAME,CREATETIME,TYPE) values(?,now(),?);";
		jdbcTemplate.update(sql,templateName,3);
	}
	
	@Override
	public void updateStatus(String status) {
		String sql="UPDATE LDWJ_EQUIPMENTANDMONITOR SET SELECTSTATUS = '"+status+"'";
		jdbcTemplate.update(sql);
	}
	
	public void updateEth1(String ck){
		String sql="UPDATE LDWJ_EQUIPMENT SET ENABLE2 = "+ck;
		jdbcTemplate.update(sql);
	}
	
}
