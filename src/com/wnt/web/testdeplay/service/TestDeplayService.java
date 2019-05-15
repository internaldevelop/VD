package com.wnt.web.testdeplay.service;

import java.util.List;
import java.util.Map;

import com.wnt.web.testdeplay.entry.DeplayEntry;



public interface TestDeplayService {
	
	/**
	 * 查询树
	 * @param type 1为测试用例 2为我的模版
	 * @return list
	 */
	public List<DeplayEntry> findztreeList(int type);
	/**
	 * 查询已选测试用例
	 * @return list
	 */
	public List<Map<String, Object>> findforlist();
	/**
	 * 保存选中用例
	 * 
	 */
	public void saveDeplay(DeplayEntry vo);
}