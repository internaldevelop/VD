package com.commons.page;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 分页处理帮助类
 * 
 * @author 赵俊鹏
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class PaginationHelper {

	private WntPage wntPage;

	public PaginationHelper(WntPage wntPage) {
		super();
		this.wntPage = wntPage;
	}

	/**
	 * 分页处理
	 * 
	 * @param jdbcTemplate
	 * @param vntPage
	 * @param sql
	 * @param param
	 * @return
	 */
	public WntPage queryForPageList(JdbcTemplate jdbcTemplate, String sql,
			Object[] param) {

		String countSql = "SELECT COUNT(1) FROM "
				+ sql.substring(sql.indexOf("FROM") + 4, sql.indexOf("ORDER"));
		int total_count = jdbcTemplate.queryForObject(countSql, param,
				Integer.class);
		wntPage.paging(total_count);

		String pageSql = sql + " LIMIT " + getFromPageCount(total_count) + ","
				+ wntPage.getPage_size();
		List<Map<String, Object>> items = jdbcTemplate.queryForList(pageSql,
				param);
		wntPage.setItems(items);

		return wntPage;
	}
	
	/**
	 * 分页处理
	 * 
	 * @param jdbcTemplate
	 * @param vntPage
	 * @param sql
	 * @param param
	 * @param rowMapper
	 * @author gyk
	 * @return
	 */
	public <T> WntPage<T> queryForPageList(JdbcTemplate jdbcTemplate, String sql,
			Object[] param,RowMapper<T> rowMapper) {

		String countSql = "SELECT COUNT(1) FROM "
				+ sql.substring(sql.indexOf("FROM") + 4, sql.indexOf("ORDER"));
		int total_count = jdbcTemplate.queryForObject(countSql, param,
				Integer.class);
		wntPage.paging(total_count);

		String pageSql = sql + " LIMIT " + getFromPageCount(total_count) + ","
				+ wntPage.getPage_size();
		List<T> items =  jdbcTemplate.query(pageSql,rowMapper,param);
		wntPage.setItems(items);
		return wntPage;
	}

	/**
	 * 
	 * @Title: queryForPageList
	 * @Description: 专用于系统配置下系统操作日志的全部查询与分页
	 * @param jdbcTemplate
	 * @param sql
	 * @param param
	 * @return
	 * @return: WntPage
	 */
	private WntPage queryOnlyForSyslog(JdbcTemplate jdbcTemplate,
			String beginTime, String endTime) {
		String all_sql = "select distinct *";
		String countSql = "select count(1) ";

		String pre_sql = " from (select CREATENAME,CREATETIME,CONTENT,type,IP ,URL,Source from ts_operation_log "
				+ " union all"
				+ " select CREATENAME,CREATETIME,CONTENT,type,IP ,URL,Source from wl_operation_log ) a ";
		// CREATETIME
		if (StringUtils.isNotBlank(beginTime)
				&& StringUtils.isNotBlank(endTime)) {
			pre_sql += " where CREATETIME >= '" + beginTime + " 00:00:00'";
			pre_sql += " AND CREATETIME <= '" + endTime + " 23:59:59'";
		}
		all_sql += pre_sql;
		countSql += pre_sql;
		// 排序
		countSql += " order by CREATETIME desc";
		all_sql += " order by CREATETIME desc";

		int total_count = jdbcTemplate.queryForObject(countSql, Integer.class);
		wntPage.paging(total_count);
		// 分页
		String pageSql = all_sql + " LIMIT " + getFromPageCount(total_count)
				+ "," + wntPage.getPage_size();
		List<Map<String, Object>> items = jdbcTemplate.queryForList(pageSql);

		wntPage.setItems(items);

		return wntPage;
	}

	/**
	 * 分页处理
	 * 
	 * @param jdbcTemplate
	 * @param vntPage
	 * @param sql
	 * @param param
	 * @return
	 */
	public WntPage queryList(JdbcTemplate jdbcTemplate, String sql,
			Object[] param) {

		// String countSql = "SELECT COUNT(1) FROM (" + getNoOrderBySql(sql) +
		// ") AS CSQL";
		String countSql = "SELECT  COUNT(distinct FullPath) FROM  wl_iawlwarn  where MACHINECODE=?";
		int total_count = jdbcTemplate.queryForObject(countSql, param,
				Integer.class);
		wntPage.paging(total_count);
		// String pageSql = "SELECT * FROM ( " + sql + " ) AS CSQL LIMIT " +
		// getFromPageCount(total_count) + "," + wntPage.getPage_size();
		String pageSql = sql + " LIMIT " + getFromPageCount(total_count) + ","
				+ wntPage.getPage_size();
		List<Map<String, Object>> items = jdbcTemplate.queryForList(pageSql,
				param);
		wntPage.setItems(items);

		return wntPage;
	}

	/**
	 * 
	 * @Title: queryPagedList
	 * @Description: 公共的分页工具方法
	 * @param jdbcTemplate
	 * @param sql
	 * @param param
	 * @return
	 * @return: WntPage
	 */
	public WntPage queryPagedList(JdbcTemplate jdbcTemplate, String sql,
			String countSql) {

		int total_count = 0;
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList(sql);
		if (null != queryForList && queryForList.size() > 0) {
			total_count = queryForList.size();
		}
		wntPage.paging(total_count);
		String pageSql = sql + " LIMIT " + getFromPageCount(total_count) + ","
				+ wntPage.getPage_size();
		List<Map<String, Object>> items = jdbcTemplate.queryForList(pageSql);
		wntPage.setItems(items);

		return wntPage;
	}

	/**
	 * 
	 * @Title: queryWhiteList
	 * @Description: 白名单列表的查询分页处理
	 * @param jdbcTemplate
	 * @param sql
	 * @param param
	 * @return
	 * @return: WntPage
	 */
	public WntPage queryWhiteList(JdbcTemplate jdbcTemplate, String sql,
			Object[] param) {
		if (null == param || param.length < 1) {
			return null;
		}
		// 分页处理
		String countSql = "SELECT  COUNT(distinct FullPath) FROM  wl_iawlwarn  where MACHINECODE=? AND FULLPATH LIKE '%"
				+ param[1] + "%' ORDER by id ";
		int total_count = jdbcTemplate.queryForObject(countSql,
				new Object[] { param[0] }, Integer.class);
		wntPage.paging(total_count);
		String pageSql = sql + " LIMIT " + getFromPageCount(total_count) + ","
				+ wntPage.getPage_size();
		// 查询处理
		List<Map<String, Object>> items = jdbcTemplate.queryForList(pageSql,
				new Object[] { param[0] });
		wntPage.setItems(items);

		return wntPage;
	}

	/**
	 * 分页处理单独处理总页数
	 * 
	 * @param jdbcTemplate
	 * @param vntPage
	 * @param sql
	 * @param param
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public WntPage queryForPageList(JdbcTemplate jdbcTemplate, String sql,
			Object[] param, String countString) {
		String countSql = countString;
		int total_count = jdbcTemplate.queryForObject(countSql, Integer.class,
				null);
		wntPage.paging(total_count);
		String pageSql = sql + "  LIMIT " + getFromPageCount(total_count) + ","
				+ wntPage.getPage_size();
		List<Map<String, Object>> items = jdbcTemplate.queryForList(pageSql,
				param);
		wntPage.setItems(items);
		return wntPage;
	}

	private String getNoOrderBySql(String sql) {
		return sql.substring(0, sql.indexOf("ORDER"));
	}

	private int getFromPageCount(int v) {

		int sSize = (wntPage.getCurrent_page() - 1) * wntPage.getPage_size();
		if (sSize > v) {
			sSize = 0;
			wntPage.setCurrent_page(1);
		}
		return sSize;
	}

	public WntPage getWntPage() {
		return wntPage;
	}

	public void setWntPage(WntPage wntPage) {
		this.wntPage = wntPage;
	}

}