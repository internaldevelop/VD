
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: SystemInfoRatioDto.java 
 * @Prject: VD
 * @Package: com.wnt.web.systeminfo.entry 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-5-17 上午9:38:26 
 * @version: V1.0   
 */ package com.wnt.web.systeminfo.entry; import java.util.concurrent.atomic.AtomicInteger;

/** 
 * @ClassName: SystemInfoRatioDto 
 * @Description: TODO
 * @author: gyj
 * @date: 2017-5-17 上午9:38:26  
 */
public class SystemInfoRatioDto {

	/** CPU使用率 */
	private float cpuRatio;

	/** 内存使用率 */
	private float memRatio;

	/** 硬盘使用率 */
	private float diskRatio;

	/** 连续60s内cpu超负荷的计数器 */
//	private AtomicInteger cpuOverburdenCount = new AtomicInteger();

	/** 连续60s内内存超负荷的计数器 */
//	private AtomicInteger memOverburdenCount = new AtomicInteger();
	
	/** 连续60s内内存超负荷的计数器 */
//	private AtomicInteger diskOverburdenCount = new AtomicInteger();

	private static SystemInfoRatioDto systemInfoRatioDto = new SystemInfoRatioDto();  
	public SystemInfoRatioDto() {
	}
    public static SystemInfoRatioDto getInstance() {  
    	return systemInfoRatioDto;  
    }  

	public SystemInfoRatioDto(int cpuRatio, int memRatio,  int diskRatio) {
		this.cpuRatio = cpuRatio;
		this.memRatio = memRatio;
		this.diskRatio = diskRatio;
//		this.cpuOverburdenCount = cpuOverburdenCount;
//		this.memOverburdenCount = memOverburdenCount;
//		this.diskOverburdenCount= diskOverburdenCount;
	}
	public float getCpuRatio() {
		return cpuRatio;
	}

	public void setCpuRatio(float cpuRatio) {
		this.cpuRatio = cpuRatio;
	}

	public float getMemRatio() {
		return memRatio;
	}

	public float getDiskRatio() {
		return diskRatio;
	}

	public void setDiskRatio(float diskRatio) {
		this.diskRatio = diskRatio;
	}


	public void setMemRatio(float memRatio) {
		this.memRatio = memRatio;
	}

}

