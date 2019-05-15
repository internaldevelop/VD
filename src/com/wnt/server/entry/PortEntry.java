package com.wnt.server.entry;


/**
 * 端口数据
 * 
 * @author 张明远
 * @version 1.0
 * @company 威努特信息技术公司
 * @site http://www.winicssec.com
 * 
 */
public class PortEntry {
	private String id; 
	private int porttype;	//端口类型：8 TCP 9 UDP
	private int portnum;	//端口号
	private String name;		//服务名称
	private int scantype;
	private int source;
	public int getPorttype() {
		return porttype;
	}
	public void setPorttype(int porttype) {
		this.porttype = porttype;
	}
	public int getPortnum() {
		return portnum;
	}
	public void setPortnum(int portnum) {
		this.portnum = portnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getScantype() {
		return scantype;
	}
	public void setScantype(int scantype) {
		this.scantype = scantype;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	

}
