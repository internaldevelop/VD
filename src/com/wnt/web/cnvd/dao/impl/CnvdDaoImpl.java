package com.wnt.web.cnvd.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.commons.page.PaginationHelper;
import com.commons.page.WntPage;
import com.wnt.web.cnvd.dao.CnvdDao;
import com.wnt.web.cnvd.entry.Cnvd;
import com.wnt.web.dynamicports.entry.DynamicPorts;

/**
 * 
 * 漏洞操作
 * @author gyk
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * @date 2016年10月26日
 */
@Repository("cnvdDao")
public class CnvdDaoImpl implements CnvdDao{
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Cnvd> findAll() {
		String sql = "SELECT * FROM cnvd ORDER BY LAST_TIME DESC";
		return jdbcTemplate.query(sql,new CnvdMapper());
	}
	
	@Override
	public WntPage<Cnvd> findPageAll(WntPage<Cnvd> page) {
		String sql = "SELECT * FROM cnvd ORDER BY LAST_TIME DESC";
		PaginationHelper paginationHelper = new PaginationHelper(page);
		return paginationHelper.queryForPageList(jdbcTemplate, sql, null,new CnvdMapper());
	}
	
	@Override
	public List<Cnvd> findByCnvdName(String cnvdName) {
		return jdbcTemplate.query("SELECT * FROM cnvd WHERE CNVD_NAME = ? ORDER BY LAST_TIME DESC",new CnvdMapper(), cnvdName);
	}

	@Override
	public List<Cnvd> findFuzzyByCnvdName(String cnvdName) {
		 return jdbcTemplate.query("SELECT * FROM cnvd WHERE CNVD_NAME LIKE ? ORDER BY LAST_TIME DESC",new CnvdMapper(), "%"+cnvdName+"%");
	} 
	
	@Override
	public WntPage<Cnvd> findFuzzyByCnvdName(WntPage<Cnvd> page, String cnvdName) {
		String sql = "SELECT * FROM cnvd WHERE CNVD_NAME LIKE ? ORDER BY LAST_TIME DESC";
		PaginationHelper paginationHelper = new PaginationHelper(page);
		return paginationHelper.queryForPageList(jdbcTemplate, sql, new Object[]{"%"+cnvdName+"%"},new CnvdMapper());
	}
	
	@Override
	public WntPage<Cnvd> findFuzzy(WntPage<Cnvd> page,String cnvdId, String cnvdName) {
		StringBuffer sqlbuf = new StringBuffer("SELECT * FROM cnvd WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(cnvdId)){
			sqlbuf.append(" AND CNVD_ID LIKE ? ");
			params.add("%"+cnvdId+"%");
		}
		if(StringUtils.isNotBlank(cnvdName)){
			sqlbuf.append(" AND CNVD_NAME LIKE ?");
			params.add("%"+cnvdName+"%");
		}
		sqlbuf.append(" ORDER BY LAST_TIME DESC");
		PaginationHelper paginationHelper = new PaginationHelper(page);
		return paginationHelper.queryForPageList(jdbcTemplate, sqlbuf.toString(),params.toArray(),new CnvdMapper());
	}

	@Override
	public Cnvd getCnvd(Integer id) {
		try {
			return jdbcTemplate.queryForObject("SELECT * FROM cnvd WHERE ID = ?",new CnvdMapper(),id);
		} catch (DataAccessException e) {
			return null;
		}
	}

	@Override
	public Cnvd getCnvd(String cnvdId) {
		try {
			return  jdbcTemplate.queryForObject("SELECT * FROM cnvd WHERE CNVD_ID = ?", new CnvdMapper(), cnvdId);
		} catch (DataAccessException e) {
			return null;
		}
	}

	
	
	

	@Override
	public List<Cnvd> findByCveId(String cveId) {
		return jdbcTemplate.query("SELECT * FROM cnvd WHERE CVE_ID = ? ORDER BY LAST_TIME DESC",new CnvdMapper(), cveId);
	}

	@Override
	public List<Cnvd> findByCnvdIdAndCveId(String cnvdId, String cveId) {
		return jdbcTemplate.query("SELECT * FROM cnvd WHERE CNVD_ID = ? AND CVE_ID = ? ORDER BY LAST_TIME DESC", new CnvdMapper(), cveId);
	}

	@Override
	public int save(Cnvd cnvd) {
		Date d = new Date();
		String sql = "INSERT INTO cnvd (CNVD_ID, RELEASE_TIME, HAZARD_LEVEL, AFFECT_GOODS, CVE_ID, DESCRIPTION, REFER_LINK, SOLUTION, FINDER, PATCH, VERIFY, REPORT_TIME, RECORD_TIME, RENEW_TIME,BUGTRAQ_ID,CREATE_TIME,LAST_TIME,CNVD_NAME,OTHER_ID,CNVD_TYPE) "
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[20];
		params[0] = cnvd.getCnvdId();
		params[1] = cnvd.getReleaseTime();
		params[2] = cnvd.getHazardLevel();
		params[3] = cnvd.getAffectGoods();
		params[4] = cnvd.getCveId();
		params[5] = cnvd.getDescription();
		params[6] = cnvd.getReferLink();
		params[7] = cnvd.getSolution();
		params[8] = cnvd.getFinder();
		params[9] = cnvd.getPatch();
		params[10] = cnvd.getVerify();
		params[11] = cnvd.getReportTime();
		params[12] = cnvd.getRecordTime();
		params[13] = cnvd.getRenewTime();
		params[14] = cnvd.getBugtraqId();
		params[15] = cnvd.getCreateTime()==null?d:cnvd.getCreateTime();
		params[16] = cnvd.getLastTime()==null?d:cnvd.getLastTime();
		params[17] = cnvd.getCnvdName();
		params[18] = cnvd.getOtherId();
		params[19] = cnvd.getCnvdType();
		return jdbcTemplate.update(sql, params);
	}

	@Override
	public int update(Cnvd cnvd) {
		String sql = "UPDATE cnvd set CNVD_ID = ?, RELEASE_TIME = ?, HAZARD_LEVEL = ?, AFFECT_GOODS = ?, CVE_ID = ?, DESCRIPTION = ?, REFER_LINK = ?, SOLUTION = ?, FINDER = ?, PATCH = ?, VERIFY = ?, REPORT_TIME = ?, RECORD_TIME = ?, RENEW_TIME = ?,BUGTRAQ_ID = ?,LAST_TIME = ?,CNVD_NAME = ?,OTHER_ID = ?,CNVD_TYPE = ? WHERE ID = ?";
		Object[] params = new Object[20];
		params[0] = cnvd.getCnvdId();
		params[1] = cnvd.getReleaseTime();
		params[2] = cnvd.getHazardLevel();
		params[3] = cnvd.getAffectGoods();
		params[4] = cnvd.getCveId();
		params[5] = cnvd.getDescription();
		params[6] = cnvd.getReferLink();
		params[7] = cnvd.getSolution();
		params[8] = cnvd.getFinder();
		params[9] = cnvd.getPatch();
		params[10] = cnvd.getVerify();
		params[11] = cnvd.getReportTime();
		params[12] = cnvd.getRecordTime();
		params[13] = cnvd.getRenewTime();
		params[14] = cnvd.getBugtraqId();
		params[15] = cnvd.getLastTime()==null?new Date():cnvd.getLastTime();
		params[16] = cnvd.getCnvdName();
		params[17] = cnvd.getOtherId();
		params[18] = cnvd.getCnvdType();
		params[19] = cnvd.getId();
		return jdbcTemplate.update(sql,params);
	}

	@Override
	public int delete(Integer id) {
		String sql = "DELETE FROM cnvd WHERE ID = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int deleteByCnvdId(String cnvdId) {
		String sql = "DELETE FROM cnvd WHERE CNVD_ID = ?";
		return jdbcTemplate.update(sql, cnvdId);
	}

	@Override
	public int deleteByCveId(String cveId) {
		String sql = "DELETE FROM cnvd WHERE CVE_ID = ?";
		return jdbcTemplate.update(sql, cveId);
	}

	
	/**
	 * sql 查询返回数据类型封装
	 * @author gyk
	 *
	 */
	 class CnvdMapper implements RowMapper<Cnvd> {  
		  public Cnvd mapRow(ResultSet rs, int rowNum) throws SQLException { 
			  Cnvd cnvd = new Cnvd();
			  cnvd.setId(rs.getInt("ID"));
			  cnvd.setCnvdId(rs.getString("CNVD_ID"));
			  cnvd.setAffectGoods(rs.getString("AFFECT_GOODS"));
			  cnvd.setCveId(rs.getString("CVE_ID"));
			  cnvd.setDescription(rs.getString("DESCRIPTION"));
			  cnvd.setFinder(rs.getString("FINDER"));
			  cnvd.setHazardLevel(rs.getString("HAZARD_LEVEL"));
			  cnvd.setPatch(rs.getString("PATCH"));
			  cnvd.setReferLink(rs.getString("REFER_LINK"));
			  cnvd.setSolution(rs.getString("SOLUTION"));
			  cnvd.setVerify(rs.getString("VERIFY"));
			  cnvd.setRecordTime(rs.getDate("RECORD_TIME"));
			  cnvd.setReleaseTime(rs.getDate("RELEASE_TIME"));
			  cnvd.setReportTime(rs.getDate("REPORT_TIME"));
			  cnvd.setRenewTime(rs.getDate("RENEW_TIME"));
			  cnvd.setBugtraqId(rs.getString("BUGTRAQ_ID"));
			  cnvd.setCreateTime(rs.getDate("CREATE_TIME"));
			  cnvd.setLastTime(rs.getDate("LAST_TIME")); 
			  cnvd.setCnvdName(rs.getString("CNVD_NAME"));
			  cnvd.setOtherId(rs.getString("OTHER_ID"));
			  cnvd.setCnvdType(rs.getString("CNVD_TYPE"));
			  return cnvd;
		  }    
	}


	
}
