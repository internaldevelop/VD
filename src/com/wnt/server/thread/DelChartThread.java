package com.wnt.server.thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.wnt.core.uitl.DataUtils;

import com.wnt.web.system.service.SystemService;
import com.wnt.web.testexecute.service.ChartService;

public class DelChartThread implements Runnable {

	private int st = 0;
	private SystemService systemService;
	private ChartService chartService;

	public DelChartThread(SystemService systemService,ChartService chartService) {
		this.systemService = systemService;
		this.chartService = chartService;
	}

	@Override
	public void run() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss|SSS");
		System.out.println("DelChartThread runtime :"+simpleDateFormat.format(new Date()));
		while (true) {
			//if (st <= 0) {
			//	st = 24*30;
				String str = systemService.findgetDelchart();
				System.out.println("NowDays：" + str);
				Calendar rightNow = null;
				try {
					rightNow = DataUtils.parseCalendar(str,
							"yyyy-MM-dd");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int df = DataUtils.dateDiff('d', DataUtils.getCalendar(),
						rightNow);
				System.out.println("HowManyDays：" + df);
				if (df >= 30) {
					System.out.println(DataUtils.formatDate());
					systemService.updateDelchart(DataUtils.formatDate());
					//chartService.delCharts(DataUtils.getDateadd(-30));
					chartService.delChartArp(DataUtils.getDateadd(-30));
					chartService.delChartIcmp(DataUtils.getDateadd(-30));
					chartService.delChartTcp(DataUtils.getDateadd(-30));
					chartService.delChartDiscrete(DataUtils.getDateadd(-30));
					chartService.delChartEth0_2(DataUtils.getDateadd(-30));
					chartService.delChartEth0_1(DataUtils.getDateadd(-30));
					chartService.delChartEth1_2(DataUtils.getDateadd(-30));
					chartService.delChartEth1_1(DataUtils.getDateadd(-30));
				}
			//}
			try {
				//Thread.sleep(30*60*1000);
				//Thread.sleep(30L*24L*60L*60L*1000L);
				Thread.sleep(24L*60L*60L*1000L);
				//Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//st = st -1;
		}
	}

}
