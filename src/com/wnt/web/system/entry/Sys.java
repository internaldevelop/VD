package com.wnt.web.system.entry;

import javax.persistence.Entity;

@Entity
public class Sys {

	private String id;
	private Long interfaceip;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getInterfaceip() {
		return interfaceip;
	}

	public void setInterfaceip(Long interfaceip) {
		this.interfaceip = interfaceip;
	}

}
