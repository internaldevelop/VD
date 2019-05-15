package com.wnt.web.cnvd.entry;

import java.util.Date;

/**
 * 漏洞实体对象
 * 
 * @author gyk
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * @date 2016年10月26日
 */
public class Cnvd{
	
	private Integer id;
	
	/**
	 * 漏洞ID
	 */
	private String cnvdId;
	
	/**
	 * 发布时间
	 */
	private Date releaseTime;
	
	/**
	 * 危害级别
	 */
	private String hazardLevel;
	
	/**
	 * 影响产品
	 */
	private String affectGoods;
	
	private String cveId;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 参考链接
	 */
	private String referLink;
	
	/**
	 * 漏洞解决方案
	 */
	private String solution;
	
	/**
	 * 发现者
	 */
	private String finder;
	
	/**
	 * 厂商补丁
	 */
	private String patch;
	
	/**
	 * 验证信息
	 */
	private String verify;
	
	/**
	 * 报送时间
	 */
	private Date reportTime;
	
	/**
	 * 收录时间
	 */
	private Date recordTime;
	
	/**
	 * 更新时间
	 */
	private Date renewTime;
	
	private String bugtraqId;
	
	/**
	 * 添加时间
	 */
	private Date createTime;
	
	/**
	 * 最后修改时间
	 */
	private Date lastTime;
	
	/**
	 * 漏洞名称
	 */
	private String cnvdName;
	
	/**
	 * 其他ID
	 */
	private String otherId;
	
	/**
	 * 漏洞类型
	 */
	private String cnvdType;
	
	/**
	 * 获取
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}


	/**
	 * 获取
	 * @return the cnvdId
	 */
	public String getCnvdId() {
		return cnvdId;
	}

	/**
	 * 设置
	 * @param cnvdId the cnvdId to set
	 */
	public void setCnvdId(String cnvdId) {
		this.cnvdId = cnvdId;
	}

	/**
	 * 获取
	 * @return the releaseTime
	 */
	public Date getReleaseTime() {
		return releaseTime;
	}

	/**
	 * 设置
	 * @param releaseTime the releaseTime to set
	 */
	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	/**
	 * 获取
	 * @return the hazardLevel
	 */
	public String getHazardLevel() {
		return hazardLevel;
	}

	/**
	 * 设置
	 * @param hazardLevel the hazardLevel to set
	 */
	public void setHazardLevel(String hazardLevel) {
		this.hazardLevel = hazardLevel;
	}

	/**
	 * 获取
	 * @return the affectGoods
	 */
	public String getAffectGoods() {
		return affectGoods;
	}

	/**
	 * 设置
	 * @param affectGoods the affectGoods to set
	 */
	public void setAffectGoods(String affectGoods) {
		this.affectGoods = affectGoods;
	}

	/**
	 * 获取
	 * @return the cveId
	 */
	public String getCveId() {
		return cveId;
	}

	/**
	 * 设置
	 * @param cveId the cveId to set
	 */
	public void setCveId(String cveId) {
		this.cveId = cveId;
	}

	/**
	 * 获取
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取
	 * @return the referLink
	 */
	public String getReferLink() {
		return referLink;
	}

	/**
	 * 设置
	 * @param referLink the referLink to set
	 */
	public void setReferLink(String referLink) {
		this.referLink = referLink;
	}

	/**
	 * 获取
	 * @return the solution
	 */
	public String getSolution() {
		return solution;
	}

	/**
	 * 设置
	 * @param solution the solution to set
	 */
	public void setSolution(String solution) {
		this.solution = solution;
	}

	/**
	 * 获取
	 * @return the finder
	 */
	public String getFinder() {
		return finder;
	}

	/**
	 * 设置
	 * @param finder the finder to set
	 */
	public void setFinder(String finder) {
		this.finder = finder;
	}

	/**
	 * 获取
	 * @return the patch
	 */
	public String getPatch() {
		return patch;
	}

	/**
	 * 设置
	 * @param patch the patch to set
	 */
	public void setPatch(String patch) {
		this.patch = patch;
	}

	/**
	 * 获取
	 * @return the verify
	 */
	public String getVerify() {
		return verify;
	}

	/**
	 * 设置
	 * @param verify the verify to set
	 */
	public void setVerify(String verify) {
		this.verify = verify;
	}

	/**
	 * 获取
	 * @return the reportTime
	 */
	public Date getReportTime() {
		return reportTime;
	}

	/**
	 * 设置
	 * @param reportTime the reportTime to set
	 */
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	/**
	 * 获取
	 * @return the recordTime
	 */
	public Date getRecordTime() {
		return recordTime;
	}

	/**
	 * 设置
	 * @param recordTime the recordTime to set
	 */
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	/**
	 * 获取
	 * @return the renewTime
	 */
	public Date getRenewTime() {
		return renewTime;
	}

	/**
	 * 设置
	 * @param renewTime the renewTime to set
	 */
	public void setRenewTime(Date renewTime) {
		this.renewTime = renewTime;
	}

	/**
	 * 获取
	 * @return the bugtraqId
	 */
	public String getBugtraqId() {
		return bugtraqId;
	}

	/**
	 * 设置
	 * @param bugtraqId the bugtraqId to set
	 */
	public void setBugtraqId(String bugtraqId) {
		this.bugtraqId = bugtraqId;
	}

	/**
	 * 获取
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取
	 * @return the lastTime
	 */
	public Date getLastTime() {
		return lastTime;
	}

	/**
	 * 设置
	 * @param lastTime the lastTime to set
	 */
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	/**
	 * 获取
	 * @return the cnvdName
	 */
	public String getCnvdName() {
		return cnvdName;
	}

	/**
	 * 设置
	 * @param cnvdName the cnvdName to set
	 */
	public void setCnvdName(String cnvdName) {
		this.cnvdName = cnvdName;
	}

	/**
	 * 获取
	 * @return the otherId
	 */
	public String getOtherId() {
		return otherId;
	}

	/**
	 * 设置
	 * @param otherId the otherId to set
	 */
	public void setOtherId(String otherId) {
		this.otherId = otherId;
	}

	/**
	 * 获取
	 * @return the cnvdType
	 */
	public String getCnvdType() {
		return cnvdType;
	}

	/**
	 * 设置
	 * @param cnvdType the cnvdType to set
	 */
	public void setCnvdType(String cnvdType) {
		this.cnvdType = cnvdType;
	}
	
	
	
}
