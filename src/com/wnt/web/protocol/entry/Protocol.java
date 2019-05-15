
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: Protocol.java 
 * @Prject: VD
 * @Package: com.wnt.web.protocol.entry 
 * @Description: TODO
 * @author: gyj   
 * @date: 2017-5-2 上午10:39:27 
 * @version: V1.0   
 */ package com.wnt.web.protocol.entry; /** 
 * @ClassName: Protocol 
 * @Description: TODO
 * @author: gyj
 * @date: 2017-5-2 上午10:39:27  
 */
public class Protocol {

	private int id;
	private String protocolName;
	private String srcIp;
	private String dstIp;
	private long port;
	private String identyTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProtocolName() {
		return protocolName;
	}
	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}
	public String getSrcIp() {
		return srcIp;
	}
	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}
	public String getDstIp() {
		return dstIp;
	}
	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}
	public long getPort() {
		return port;
	}
	public void setPort(long port) {
		this.port = port;
	}
	public String getIdentyTime() {
		return identyTime;
	}
	public void setIdentyTime(String identyTime) {
		this.identyTime = identyTime;
	}
	@Override
	public String toString() {
		return "Protocol [id=" + id + ", protocolName=" + protocolName
				+ ", srcIp=" + srcIp + ", dstIp=" + dstIp + ", port=" + port
				+ ", identyTime=" + identyTime + "]";
	}
	
	
}

