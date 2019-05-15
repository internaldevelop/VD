package com.wnt.web.dynamicports.dao;

import java.util.Date;
import java.util.List;

import com.wnt.web.dynamicports.entry.DynamicPorts;

/**
 * 动态端口数据操作接口
 * @author gyk
 *
 */
public interface DynamicPortsDao {
	/**
	 * 保存动态端口 可以添加一个或多个
	 * @param dports 动态端口实体对象或数组
	 * @return 操作的条数
	 */
	public int save(DynamicPorts... dports); 
	
	/**
	 * 根据IP地址和扫描类型查询动态端口信息
	 * @param ip		ip 地址
	 * @param scanType	'扫描类型 0为主动扫描 1为被动扫描',2为未知
	 * @return 动态端口列表
	 */
	public List<DynamicPorts> findByIPAndScanType(String ip,Integer scanType);
	
	/**
	 * 根据IP地址和扫描类型查询动态端口信息
	 * @param ip		ip 地址
	 * @param scanType	'扫描类型 0为主动扫描 1为被动扫描',2为未知
	 * @param delStatus  删除状态 1 已删除,0未删除
	 * @return 动态端口列表
	 */
	public List<DynamicPorts> findByIPAndScanType(String ip,Integer scanType,Integer delStatus);
	
	
	/**
	 * 根据IP地址和扫描类型查询动态端口信息
	 * @param ip		ip 地址
	 * @param scanType	'扫描类型 0为主动扫描 1为被动扫描',2为未知
	 * @param startTime 开始时间
	 * @return 动态端口列表
	 */
	public List<DynamicPorts> findByIPAndScanType(String ip,Integer scanType,Date startTime);
	
	/**
	 * 根据IP 地址查找动态端口信息
	 * @param ip ip 地址
	 * @return 此IP 地址关联的动态端口
	 */
	public List<DynamicPorts> findByIp(String ip);
	
	/**
	 * 根据IP 地址查找动态端口信息
	 * @param ip ip 地址
	 * @param delStatus 删除状态
	 * @return 此IP 地址关联的动态端口
	 */
	public List<DynamicPorts> findByIp(String ip,int delStatus);
	
	/**
	 * 修改动态端口扫描  
	 * 注意根据id 修改只能修改 删除状态(delStatus),编码(code),排序(numOrder),备注(remark)
	 * 其他的不可以修改如：ip 地址，端口号，scanType 扫描类型，添加时间
	 * @param ports 动态端口
	 * @return 修改成功返回1，0 表示没有修改,大于1标识修改的行数 
	 */
	public int update(DynamicPorts ports);
	
	/**
	 * 根据ID 删除动态端口
	 * @param id 单个ID 或者 ID 数组
	 * @return 响应的条数
	 */
	public int deletePort(String... ids);
	
	/**
	 * 根据ip 地址删除动态端口
	 * @param ip 单个IP地址 或者 ip地址数组
	 * @return 响应的条数
	 */
	public int deletePortByIp(String... ips);
	
	/**
	 * 根据ip 地址删除动态端口
	 * @param ip IP地址 
	 * @param portNum 端口号
	 * @return 响应的条数
	 */
	public int deletePortByIp(String ip,Integer portNum);

	/**
	 * 根据IP，扫描类型，端口类型和删除状态查询动态端口
	 * @param ip		ip 地址
	 * @param scanType	扫描类型
	 * @param portType  端口类型
	 * @param delStatus	删除状态
	 * @return
	 * @author gyk
	 * @data 2016年11月1日
	 */
	List<DynamicPorts> findByIpScanTypePortType(String ip, Integer scanType,
			Integer portType, Integer delStatus);

	/**
	 * 根据IP，扫描类型，端口类型查询动态端口
	 * @param ip		ip 地址
	 * @param scanType	
	 * @param portType
	 * @return
	 * @author gyk
	 * @data 2016年11月1日
	 */
	List<DynamicPorts> findByIpScanTypePortType(String ip, Integer scanType,
			Integer portType);

	/**
	 * 清空动态端口的数据
	 * 
	 * @author gyk
	 * @data 2016年11月1日
	 */
	void clearDynamicPorts();
}
