package org.wnt.core.uitl;

public enum ProtocolPort {
	TCP(6,"TCP",0),UDP(17,"UDP",0);
	
	/**
	 * 端口号
	 */
	private int port;
	/**
	 * 协议
	 */
	private String protocol;
	/**
	 * 级别
	 */
	private int level;
	
	private ProtocolPort(int port,String protocol,int level){
		this.port = port;
		this.protocol = protocol;
		this.level = level;
	}
	
	/**
	 * 根据端口号获取协议
	 * @param port
	 * @return
	 * @author gyk
	 * @data 2016年10月31日
	 */
	public static ProtocolPort getPort(int port){
		for(ProtocolPort pp : ProtocolPort.values()){
			if(pp.getPort() == port){
				return pp;
			}
		}
		return null;
	}
	
	/**
	 * 根据协议名称获取
	 * @param port
	 * @return
	 * @author gyk
	 * @data 2016年10月31日
	 */
	public static ProtocolPort getProtocol(String protocol){
		for(ProtocolPort pp : ProtocolPort.values()){
			if(pp.getProtocol().equalsIgnoreCase(protocol)){
				return pp;
			}
		}
		return null;
	}
	
	
	
	/**
	 * 获取端口号
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * 获取协议名称
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}
}
