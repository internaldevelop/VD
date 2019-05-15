
/**   
 * Copyright © 2017 Beijing WINICSSEC Technologies Co.,Ltd. All rights reserved.
 * 
 * @Title: SystemFlagUtil.java 
 * @Prject: VD
 * @Package: common 
 * @Description: 
 * @author: jfQiao   
 * @date: 2017年3月27日 上午11:49:03 
 * @version: V1.0   
 */
package common;

/**
 * @ClassName: SystemFlagUtil
 * @Description: 判断是windows系统还是linux系统
 * @author: jfQiao
 * @date: 2017年3月27日 上午11:49:03
 */
public class SystemFlagUtil {

	/**
	 * 
	 * @Title: isWindows
	 * @Description: 是否是windows系统
	 * @return
	 * @return: boolean
	 */
	public static boolean isWindows() {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) { // windows
			return true;
		}

		return false;
	}

}
