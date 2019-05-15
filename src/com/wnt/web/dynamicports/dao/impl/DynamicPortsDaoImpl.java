package com.wnt.web.dynamicports.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.ws.WebEndpoint;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.web.dynamicports.dao.DynamicPortsDao;
import com.wnt.web.dynamicports.entry.DynamicPorts;

/**
 * 动态端口数据操作实现
 * @author gyk
 *
 */

@Repository("dynamicPortsDao")
public class DynamicPortsDaoImpl implements DynamicPortsDao {
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	//表名称
	private static final String TABLE_NAME = "ldwj_dynamicports";
	
	private static final String SQL_SELECT_ALL = "ID as id,IPADDR as ipAddr,PORTNUM as portNum,PORTTYPE as portType,SCANTYPE as scanType,CREATETIME as createTime,DELSTATUS as delStatus,CODE as code,REMARK as remark,NUM_ORDER as numOrder";
	
	@Override
	public int save(DynamicPorts... dports) {
		if(dports != null ){
			int l = dports.length;
			if( l > 0){
				//临时变量，保存sql 参数值
				List<Object> tmpList = new ArrayList<Object>();
				//sql 语句
				StringBuffer sqlbuf = new StringBuffer("insert into ");
				sqlbuf.append(TABLE_NAME);
				sqlbuf.append("(ID,IPADDR,PORTNUM,PORTTYPE,SCANTYPE,CREATETIME,DELSTATUS,CODE,REMARK,NUM_ORDER) VALUES");
				int maxi = l-1;
				//获取当前时间
				Date date = new Date();
				for(int i=0;i<l;i++){
					sqlbuf.append("(?,?,?,?,?,?,?,?,?,?)");
					if(i<maxi){
						sqlbuf.append(",");
					}
					DynamicPorts dp = dports[i];
					tmpList.add(dp.getId()==null?UUIDGenerator.generate():dp.getId());
					tmpList.add(dp.getIpAddr());
					tmpList.add(dp.getPortNum());
					tmpList.add(dp.getPortType());
					tmpList.add(dp.getScanType());
					//如果对象时间为空插入系统当前时间
					tmpList.add(dp.getCreateTime() == null?date:dp.getCreateTime());
					tmpList.add(dp.getDelStatus() == null?0:dp.getDelStatus());
					tmpList.add(dp.getCode());
					tmpList.add(dp.getRemark());
					tmpList.add(i+1);
				}
				
				int r = jdbcTemplate.update(sqlbuf.toString(),tmpList.toArray());
				System.out.println("保存了"+r+"条数据");
			}
		}
		return 0;
	}

	@Override
	public List<DynamicPorts> findByIPAndScanType(String ip, Integer scanType) {
		return findByIPAndScanType(ip,scanType,0);
	}

	@Override
	public List<DynamicPorts> findByIPAndScanType(String ip, Integer scanType,
			Integer delStatus) {
		String sql = "select * from "+TABLE_NAME+" where IPADDR = ? and SCANTYPE = ? and DELSTATUS = ? order by CREATETIME desc,NUM_ORDER desc";
		return jdbcTemplate.query(sql,new Object[]{ip,scanType,delStatus},new DynamicPortsMapper());
	}
	
	@Override
	public List<DynamicPorts> findByIpScanTypePortType(String ip,Integer scanType,Integer portType,Integer delStatus){
		String sql = "select * from "+TABLE_NAME+" where IPADDR = ? and SCANTYPE = ? and PORTTYPE = ? and DELSTATUS = ? order by PORTNUM ASC,CREATETIME desc,NUM_ORDER desc";
		return jdbcTemplate.query(sql,new Object[]{ip,scanType,portType,delStatus},new DynamicPortsMapper());
	}
	
	@Override
	public List<DynamicPorts> findByIpScanTypePortType(String ip,Integer scanType,Integer portType){
		return findByIpScanTypePortType(ip,scanType,portType,0);
	}
	
	@Override
	public List<DynamicPorts> findByIPAndScanType(String ip, Integer scanType,Date startTime) {
		String sql = "select * from "+TABLE_NAME+" where IPADDR = ? and SCANTYPE = ? and DELSTATUS = ? and CREATETIME <= ? order by CREATETIME desc,NUM_ORDER desc";
		return jdbcTemplate.query(sql, new Object[]{ip,scanType,0,startTime},new DynamicPortsMapper());
	}
	
	@Override
	public int update(DynamicPorts ports){
		if(ports!=null){
			StringBuffer sql = new StringBuffer("update "+TABLE_NAME+" set  ");
			//临时变量存储sql 参数
			List<Object> argList = new ArrayList<Object>();
			
			//不全部为空的临时变量
			boolean allNull = true;
			if(ports.getDelStatus()!=null){
				allNull = false;
				sql.append(" DELSTATUS = ?,");
				argList.add(ports.getDelStatus());
			}
			if(ports.getCode() != null){
				allNull = false;
				sql.append(" CODE = ?,");
				argList.add(ports.getCode());
			}
			if(ports.getNumOrder() != null){
				allNull = false;
				sql.append(" NUM_ORDER = ?,");
				argList.add(ports.getNumOrder());
			}
			if(ports.getRemark() != null){
				allNull = false;
				sql.append(" REMARK = ?,");
				argList.add(ports.getRemark());
			}
			//全部为空 不进行修改
			if(allNull){
				return 0;
			}else{
				String tempSql = sql.substring(0,sql.length()-1);
				tempSql += " where ID = ?";
				argList.add(ports.getId());
				return jdbcTemplate.update(tempSql, argList.toArray());
			}
		}
		return 0;
	}

	@Override
	public int deletePort(String... ids) {
		if(ids != null){
			int leng = ids.length;
			if(leng > 0){
				List<Object> tmpList = new ArrayList<Object>();
				StringBuffer sql = new StringBuffer("delete from ");
				sql.append(TABLE_NAME);
				sql.append(" where ");
				if(leng == 1){
					sql.append(" ID = ?");
					tmpList.add(ids[0]);
				}else{
					sql.append(" ID in(");
					int maxIndex = leng-1;
					for(int i=0;i<leng;i++){
						sql.append("?");
						tmpList.add(ids[i]);
						if(i < maxIndex){
							sql.append(",");
						}
					}
					sql.append(")");
				}
				System.out.println("删除重复的数据");
				System.out.print(sql+"\t");
				System.out.println(tmpList.toArray());
				int r = jdbcTemplate.update(sql.toString(), tmpList.toArray());
				System.out.println("删除了"+r+"条数据");
				return r;
			}
		}
		return 0;
	}

	@Override
	public int deletePortByIp(String... ips) {
		if(ips != null){
			int leng = ips.length;
			if(leng > 0){
				List<Object> tmpList = new ArrayList<Object>();
				StringBuffer sql = new StringBuffer("delete from ");
				sql.append(TABLE_NAME);
				sql.append(" where ");
				if(leng == 1){
					sql.append(" IPADDR = ?");
					tmpList.add(ips[0]);
				}else{
					sql.append(" IPADDR in(");
					int maxIndex = leng-1;
					for(int i=0;i<leng;i++){
						sql.append("?");
						tmpList.add(ips[i]);
						if(i < maxIndex){
							sql.append(",");
						}
					}
					sql.append(")");
				}
				jdbcTemplate.update(sql.toString(), tmpList.toArray());
			}
		}
		return 0;
	}

	@Override
	public int deletePortByIp(String ip, Integer portNum) {
		String sql = "delete from "+TABLE_NAME+" where IPADDR = ? and PORTNUM = ?";
		return jdbcTemplate.update(sql, ip,portNum);
	}
	
	
	public List<DynamicPorts> findByIp(String ip,int delStatus){
		String sql = "select * from "+TABLE_NAME+" where IPADDR = ? and DELSTATUS = ? order by CREATETIME desc,NUM_ORDER desc";
		return jdbcTemplate.query(sql, new Object[]{ip,delStatus}, new DynamicPortsMapper());
	}
	
	@Override
	public List<DynamicPorts> findByIp(String ip){
		return findByIp(ip,0);
	}
	
	@Override
	public void clearDynamicPorts(){
		String sql = "delete from "+TABLE_NAME;
		jdbcTemplate.update(sql);
	}
	
	
	/**
	 * sql 查询返回数据类型封装
	 * @author gyk
	 *
	 */
	protected class DynamicPortsMapper implements RowMapper<DynamicPorts> {  
		  public DynamicPorts mapRow(ResultSet rs, int rowNum) throws SQLException { 
			  DynamicPorts dp = new DynamicPorts();
			  dp.setId(rs.getString("ID"));
			  dp.setIpAddr(rs.getString("IPADDR"));
			  dp.setPortNum(rs.getInt("PORTNUM"));
			  dp.setPortType(rs.getInt("PORTTYPE"));
			  dp.setScanType(rs.getInt("SCANTYPE"));
			  dp.setCreateTime(rs.getDate("CREATETIME"));
			  dp.setDelStatus(rs.getInt("DELSTATUS"));
			  dp.setCode(rs.getString("CODE"));
			  dp.setRemark(rs.getString("REMARK"));
			  dp.setNumOrder(rs.getInt("NUM_ORDER"));
			  return dp;
		  }    
	}  

}
