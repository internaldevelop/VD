package common;

import java.io.File;

import org.apache.log4j.Logger;
import org.wnt.core.uitl.PropertiesUtil;

import com.wnt.web.portscan.controller.PortscanController;

/**
 * 
 * @author 付强
 * 
 */
public final class ConstantsDefs {
	private ConstantsDefs() {
	}
	
	private static final String START_SCAN = "startScan";
	/**
	 * arp监视器：超时时间
	 */
	public  static final String MARP = "marp";
	/**
	 * icmp监视器：超时时间
	 */
	public static final String MICMP = "micmp";
	/**
	 * 离散监视器：报警线
	 */
	public static final String MLS = "mls";
	
	/**
	 * 测试案例生成的pdf文件路径
	 */
	public static final String TEST_FILE_PATH ;
	
	public static final String TEST_DATA_PACK;
	//初始化数据
	static {
		PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
		TEST_FILE_PATH=p.readProperty("testPdfPath");
		TEST_DATA_PACK = p.readProperty("testDataPack");
		//判断pdf文件生成的路径是否存在
		File f=new File(TEST_FILE_PATH);
		if(!f.exists()){
			f.mkdirs();
			Logger log = Logger.getLogger(ConstantsDefs.class.getName());
			log.info("pdf文件生成路径创建成功了");
		}
	}
}
