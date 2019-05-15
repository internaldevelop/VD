package com.wnt.web.system.dao;

import java.util.List;
import java.util.Map;

import com.wnt.web.system.entry.Sys;

public interface SystemDao {

	public void interfaceipadd(long ip);
	public List<Sys> findinterface();
	public Map<String, Object> findgetDelchart();
	public void updateDelchart(String str);
}
