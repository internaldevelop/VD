package com.wnt.web.dynamicports.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.wnt.core.ehcache.EHCacheUtil;
import org.wnt.core.uitl.LogUtil;

import com.wnt.server.order.SealedSendMessage;
import com.wnt.web.dynamicports.dao.DynamicPortsDao;
import com.wnt.web.dynamicports.entry.DynamicPorts;
import com.wnt.web.dynamicports.service.DynamicPortsService;

/**
 * 动态端口扫描业务实现
 * @author gyk
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 */
@Repository("dynamicPortsService")
public class DynamicPortsServiceImpl implements DynamicPortsService{

	@Resource
	private DynamicPortsDao dynamicPortsDao;

	@Override
	public List<DynamicPorts> startScan(String ipAddr, Integer scanType,Integer portType) throws Exception{
		dynamicPortsDao.clearDynamicPorts();
		SealedSendMessage.getStartDynamicScan(ipAddr, scanType,portType);
		return scanPort(ipAddr,scanType,portType);
	}

	@Override
	public void stopScan() throws Exception{
		SealedSendMessage.getStopDynamicScan();	
		EHCacheUtil.remove("dynamicports");
	}
	
	/**
	 * 去除从复的数据
	 * @param ip		ip 地址
	 * @param newDps	新的数据
	 * @return 去重后的新数据 返回null 没有要插入的数据
	 */
	public List<DynamicPorts> distinct(List<DynamicPorts> oldList,List<DynamicPorts> newDps){
		if(newDps == null ){
			return null;
		}
		if(null == oldList || oldList.isEmpty()){
			return newDps;
		}
		//临时变量存放要保存的数据
		Map<String,DynamicPorts> result = new HashMap<String,DynamicPorts>();
		List<String> repeat = new ArrayList<String>();
		//循环新数据
		for(DynamicPorts ndp : newDps){
			//原数据中不存在新数据
			boolean isNotExists = true;
			DynamicPorts tmp = null;
			for(DynamicPorts old : oldList){
				//如果旧数据中存在新数据 
				//删除条件是 旧数据的ip地址和端口与新数据相同，但是端口访问类型或者扫描类型不同 就标识为重复的数据要删除
				if(old.getIpAddr().equals(ndp.getIpAddr()) && old.getPortNum().equals(ndp.getPortNum())){
					//记录重复数据
					repeat.add(old.getId());		
					isNotExists = false;
					break;
				}
			}
			if(isNotExists){
				result.put(ndp.getIpAddr()+"_"+ndp.getPortNum(), ndp);
			}
		}
		for(DynamicPorts old : oldList){
			for(String oid : repeat){
				if(oid.equals(old.getId())){
					result.put(old.getId()+"_"+old.getPortNum(), old);
					break;
				}
			}
		}
		if(!repeat.isEmpty()){
			dynamicPortsDao.deletePort(repeat.toArray(new String[repeat.size()]));
		}
		return new ArrayList<DynamicPorts>(result.values());
	}
	
	/**
	 * 扫描动态端口信息 
	 * @param ip		ip 地址
	 * @param scanType  扫描类型
	 * @return 扫描到的所有数据
	 */
	public List<DynamicPorts> scanPort(String ip,Integer scanType,Integer portType) throws Exception{
		List<DynamicPorts> oldList = dynamicPortsDao.findByIpScanTypePortType(ip, scanType,portType);
		Object temp = EHCacheUtil.get("dynamicports");
		//如果新数据为空 返回原有的数据
		if(temp == null){
			return oldList;
		}
		List<DynamicPorts> newDps = (List<DynamicPorts>)temp;
		Set<DynamicPorts> newData = new LinkedHashSet<DynamicPorts>();
		//补全数据
		for(int i=0;i<newDps.size();i++){
			DynamicPorts dp = newDps.get(i);
			dp.setIpAddr(ip);
			boolean isExist = false;
			for(DynamicPorts d : oldList){
				if(d.equals(dp)){
					isExist = true;
					break;
				}
			}
			if(isExist){
				continue;
			}else{
				dp.setScanType(scanType);
				dp.setPortType(portType);
				try {
					newData.add(dp);
				} catch (Exception e) {
					continue;
				}
			}
		}
		dynamicPortsDao.save(newData.toArray(new DynamicPorts[newData.size()]));
		return dynamicPortsDao.findByIpScanTypePortType(ip, scanType,portType);
	}

}
