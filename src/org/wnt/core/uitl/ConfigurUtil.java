package org.wnt.core.uitl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurUtil {
	private static Properties props = new Properties();
	static {
		//读取配置文件
		InputStream ips = ConfigurUtil.class.getClassLoader().getResourceAsStream("sysConfig.properties");
		try{
			props.load(ips);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static String getValue(String key){
		return props.getProperty(key);
	}
}
