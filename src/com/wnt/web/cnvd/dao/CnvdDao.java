package com.wnt.web.cnvd.dao;

import java.util.List;

import com.commons.page.WntPage;
import com.wnt.web.cnvd.entry.Cnvd;

public interface CnvdDao {
	
	/**
	 * 获取全部的漏洞
	 * @return
	 * @author gyk
	 * @data 2016年10月26日
	 */
	public List<Cnvd> findAll();
	
	/**
	 * 获取全部的漏洞
	 * @param page
	 * @return 分页
	 * @author gyk
	 * @data 2016年10月27日
	 */
	public WntPage<Cnvd> findPageAll(WntPage<Cnvd> page);
	
	/**
	 * 根据ID 获取
	 * @param id
	 * @return
	 * @author gyk
	 * @data 2016年10月26日
	 */
	public Cnvd getCnvd(Integer id);
	
	/**
	 * 根据cnvdId 获取
	 * @param cnvdId
	 * @return
	 * @author gyk
	 * @data 2016年10月26日
	 */
	public Cnvd getCnvd(String cnvdId);
	
	
	/**
	 * 根据cnvdId 获取
	 * @param cnvdName
	 * @return
	 * @author gyk
	 * @data 2016年10月26日
	 */
	public List<Cnvd> findByCnvdName(String cnvdName);
	
	/**
	 * 根据cnvdName 获取 模糊查询漏洞
	 * @param cnvdName
	 * @return
	 * @author gyk
	 * @data 2016年10月27日
	 */
	public List<Cnvd> findFuzzyByCnvdName(String cnvdName);
	
	/**
	 * 根据cnvdName 获取 模糊查询漏洞
	 * @param page 
	 * @param cnvdName
	 * @return
	 * @author gyk
	 * @data 2016年10月27日
	 */
	public WntPage<Cnvd> findFuzzyByCnvdName(WntPage<Cnvd> page,String cnvdName);
	
	/**
	 * 根据cnvdName 和 cnvdId 模糊查询漏洞
	 * @param page
	 * @param cnvdId
	 * @param cnvdName
	 * @return
	 * @author gyk
	 * @data 2016年11月9日
	 */
	public WntPage<Cnvd> findFuzzy(WntPage<Cnvd> page,String cnvdId,String cnvdName);
	
	/**
	 * 根据cveId 获取
	 * @param cveId
	 * @return
	 * @author gyk
	 * @data 2016年10月26日
	 */
	public List<Cnvd> findByCveId(String cveId);
	
	/**
	 * 根据cnvdId 和 cveId 查询
	 * @param cnvdId
	 * @param cveId
	 * @return
	 * @author gyk
	 * @data 2016年10月26日
	 */
	public List<Cnvd> findByCnvdIdAndCveId(String cnvdId,String cveId);
	
	/**
	 * 保存
	 * @param cnvd
	 * @return
	 * @author gyk
	 * @data 2016年10月26日
	 */
	public int save(Cnvd cnvd);
	
	/**
	 * 修改
	 * @param cnvd
	 * @return
	 * @author gyk
	 * @data 2016年10月26日
	 */
	public int update(Cnvd cnvd);
	
	/**
	 * 根据Id删除
	 * @param id
	 * @author gyk
	 * @data 2016年10月26日
	 */
	public int delete(Integer id);
	
	/**
	 * 根据cnvdId 删除
	 * @param cnvdId
	 * @return
	 * @author gyk
	 * @data 2016年10月26日
	 */
	public int deleteByCnvdId(String cnvdId);
	
	/**
	 * 根据cveId 删除
	 * @param cveId
	 * @return
	 * @author gyk
	 * @data 2016年10月26日
	 */
	public int deleteByCveId(String cveId);
}
