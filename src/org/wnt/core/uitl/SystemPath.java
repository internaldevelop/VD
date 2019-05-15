package org.wnt.core.uitl;

import jodd.util.StringUtil;

/**
 * 
 * @author wanye
 * @date Dec 14, 2008
 * @version v 1.0
 * @description 得到当前应用的系统路径
 */
public class SystemPath {

	public static String getSysPath()
	{
		String path= Thread.currentThread().getContextClassLoader().getResource("").toString();
		String temp=path.replaceFirst("file:/", "").replaceFirst("WEB-INF/classes/", "");
		String separator= System.getProperty("file.separator");
		String resultPath=temp.replaceAll("/", separator+separator);
		return resultPath;
	}
	public static String getClassPath()
	{
		String path= Thread.currentThread().getContextClassLoader().getResource("").toString();
		String temp=path.replaceFirst("file:/", "");
		String separator= System.getProperty("file.separator");
		String resultPath=temp.replaceAll("/", separator+separator);
		return resultPath;
	}
	public static String getSystempPath()
	{
		return System.getProperty("java.io.tmpdir");
	}
	public static String getSeparator()
	{
		return System.getProperty("file.separator");
	}
	
	public static void main(String[] args){
		LogUtil.info(getSysPath());
		LogUtil.info(System.getProperty("java.io.tmpdir"));
		LogUtil.info(getSeparator());
		LogUtil.info(getClassPath());
	}
	
	/**
	 * 
	 * @Title: getProjectPath 
	 * @Description: 获取工程路径，Windows，Linux都可
	 * @return
	 * @return: String
	 */
	public static String getProjectPath(){
		String sysPath = SystemPath.getSysPath();
		if (StringUtil.isNotBlank(sysPath) && sysPath.contains("//")) {
			sysPath = "/" + sysPath.replace("//", "/");
		}
		return sysPath;
	}
}
