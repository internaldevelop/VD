import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.LogUtil;
import org.wnt.core.uitl.PropertiesUtil;
import org.wnt.core.uitl.UUIDGenerator;


public class Test {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
		System.out.println(DataUtils.formatDate());
		System.out.println(DataUtils.getDateadd(1));
		
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
//	        String str="20110823";
//	        Date dt=sdf.parse(str);
	        Calendar rightNow = Calendar.getInstance();
//	        rightNow.setTime(dt);
//	        rightNow.add(Calendar.YEAR,-1);//日期减1年
//	        rightNow.add(Calendar.MONTH,3);//日期加3个月
	        rightNow.add(Calendar.DAY_OF_YEAR,-1);//日期加10天
	        Date dt1=rightNow.getTime();
	        String reStr = sdf.format(dt1);
	        System.out.println(reStr);
	        
	        System.out.println(DataUtils.getDateadd(-1));
//	        System.out.println(DataUtils.formatDate());
//	        
//	        System.out.println(DataUtils.formatDateTime());
//	        UUIDGenerator.getUUID();
//	        
//	        PropertiesUtil p = new PropertiesUtil("sysConfig.properties");
//	        p.writeProperty("delchart", "2015-10-11");
//			System.out.println("读出来啦"+p.readProperty("delchart"));
//			LogUtil.info(p.readProperty("namess"));
	}

}
