package com.wnt.web.portscan.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jodd.util.StringUtil;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wnt.web.portscan.dao.PortscanDao;
import com.wnt.web.portscan.entry.PortServerEntry;

@Repository("portscanDao")
public class PortscanDaoImpl implements PortscanDao {
	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public int save(PortServerEntry pse) {
		String sql="insert into LDWJ_PORTSERVER(ID,EQUIPMENTID,PORTNUM,PORTTYPE,SCANTYPE,SOURCE,DELSTATUS,CREATETIME,NUM_ORDER) " +
				"values(?,?,?,?,?,?,?,now(),?)";
		
		jdbcTemplate.update(sql,pse.getId(),1,pse.getPortNum(),pse.getPortType(),pse.getScanType(),pse.getSource(),pse.getDelStatus(),1);
		
//		int last_id = jdbcTemplate.queryForObject(" SELECT LAST_INSERT_ID() ",null,Integer.class);
		int last_id = 0;
		return last_id;
//		ID                   int not null auto_increment,
//		   EQUIPMENTID          int comment '设备ID ：0 为测试网络一  1为测试网络2 ',
//		   PORTNUM              int comment '端口号',
//		   NAME                 varchar(64) comment '服务名称',
//		   PORTTYPE             int(2) comment '端口类型：0 TCP 1UDP',
//		   SCANTYPE             int(2) comment '扫描类型 0为主动扫描 1为被动扫描',
//		   SOURCE               varchar(100) comment '来源：1为scan 2为手动添加 3为被动扫描',
//		   DELSTATUS            int(2) default 0 comment '0：未删除 1：已删除',
//		   REMARK               varchar(256) comment '备注',
//		   CREATETIME           datetime default NULL comment '创建时间',
//		   CODE                 varchar(10) comment '编码预留字段',
		
	}

	@Override
	public List<Map<String, Object>> findScanResult(long num) {
		String sql = "SELECT * from LDWJ_PORTSERVER where NUM_ORDER>? and DELSTATUS=0 order by NUM_ORDER DESC,CREATETIME DESC";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, num);
		return list;
	}

	@Override
	public boolean checkPort(int port,int portType) {
		String sql = "SELECT * from LDWJ_PORTSERVER where DELSTATUS=0  and PORTNUM=? and PORTTYPE=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, port,portType);
		return list.size() >0;
	}

	@Override
	public boolean checkPort2(int port, int portType) {
		String sql = "SELECT * from LDWJ_PORTSERVER where DELSTATUS=0 and SOURCE=2 and PORTNUM=? and PORTTYPE=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, port,
				portType);
		return list.size() == 0;
	}

	@Override
	public void deletePort(String id) {
		String sql = "delete from LDWJ_PORTSERVER where id=? ";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public void deleteAllPort() {
		String sql =null;
		// 删除主动扫描的端口、被动扫描的端口
//		if (porttype == 0) {
//			sql = "delete from LDWJ_PORTSERVER where SOURCE=1 or SOURCE=3 ";
//		} else {
//			sql = "delete from LDWJ_PORTSERVER where (SOURCE=1 or SOURCE=3) and PORTTYPE="+porttype;
//		}
		sql = "delete from LDWJ_PORTSERVER ";
		jdbcTemplate.update(sql);
	}
	
	@Override
	public List findport() {
		String sql = "SELECT * from LDWJ_PORTSERVER where DELSTATUS=0 and SOURCE=2";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
}
