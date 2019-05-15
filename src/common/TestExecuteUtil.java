package common;

import com.wnt.web.testexecute.controller.TestThread;
import com.wnt.web.testexecute.entry.TestEntry;

public class TestExecuteUtil {
	public static TestThread t;
	public static TestEntry testEntry ;
	
	/**
	 * 判断测试用例是否在执行中
	 * @return
	 */
	public static boolean isRunning(){
		if(testEntry!=null && testEntry.getStatus()>=2 && testEntry.getStatus()<=4){
			return true;
		}else{
			return false;
		}
		
	}
}
