package com.wnt.web.securitycheck.service;

import java.util.List;

import com.wnt.web.securitycheck.entry.Baseline;
import com.wnt.web.securitycheck.entry.BaselineDto;

/**
 * 安全检查配置基线service接口
 * @author gyk
 */
public interface BaselineService {
	/**
	 * 批量插入配置基线
	 * @param baselines
	 * @return 返回批量插入条数 0 表示没有插入
	 */
	public int insert(Baseline... baselines);
	
	/**
	 * 根据ID 删除配置基线
	 * @param id
	 */
	public void delete(String id);
	
	/**
	 * 修改配置基线
	 * @param baseline
	 */
	public void update(Baseline baseline);
	
	/**
	 * 查询全部的配置基线
	 * @return
	 */
	public List<Baseline> findAll();
	
	/**
	 * 清空全部的配置基线
	 * @return
	 */
	public int clear();
	
	/**
	 * 根据ID 获取配置基线
	 * @param id
	 * @return
	 */
	public Baseline get(String id);
	
	/**
	 * 根据选项名称查询配置基线
	 * @param name
	 * @return
	 */
	public List<Baseline> findByName(String name);
	
	/**
	 * 根据值查询配置基线
	 * @param value
	 * @return
	 */
	public List<Baseline> findByValue(String value);

	/**
	 * 先清空原来的配置基线，再插入配置基线
	 * @param dto
	 * @return
	 */
	public int clearInsert(BaselineDto dto);
}
