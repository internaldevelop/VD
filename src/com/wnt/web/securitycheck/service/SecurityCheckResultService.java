package com.wnt.web.securitycheck.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.commons.page.WntPage;
import com.wnt.web.securitycheck.entry.ResultBaseline;
import com.wnt.web.securitycheck.entry.SecurityCheckResult;


public interface SecurityCheckResultService {

	/**
	 * 批量插入安全检查结果和基线配置
	 * @param resultBaselines
	 * @return
	 */
	public int insartResultBaseline(List<ResultBaseline> resultBaselines);
	
	/**
	 * 批量插入安全检查结果
	 * @param securityCheckResults
	 * @return
	 */
	public int insartResult(List<SecurityCheckResult> securityCheckResults);
	
	/**
	 * 根据设备ID 和创建时间查询全部有效数据
	 * @param equipmentId
	 * @param createTime
	 * @param delStatus
	 * @return
	 */
	public List<SecurityCheckResult> findByEIdAndCreateTime(String equipmentId,Date createTime,int delStatus);
	
	/**
	 * 查询全部有效的安全检查结果
	 * @return
	 */
	public WntPage<SecurityCheckResult> findAllValid(WntPage<SecurityCheckResult> page);
	
	/**
	 * 根据查询删除状态查询全部的安全检查结果
	 * @param page
	 * @param delstatus
	 * @return
	 */
	public WntPage<SecurityCheckResult> findByDelStatus(WntPage<SecurityCheckResult> page,Integer delstatus);
	
	/**
	 * 根据结果ID 查询
	 * @param id
	 * @return
	 */
	public List<ResultBaseline> findByResultId(String resultId);
	
	/**
	 * 根据ID 删除结果
	 * @param id
	 */
	public void delResultById(String id);
}
