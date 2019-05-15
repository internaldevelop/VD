package com.wnt.web.socket.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.wnt.core.uitl.UUIDGenerator;

import com.wnt.server.entry.ChartEntry;
import com.wnt.server.entry.LogEntry;
import com.wnt.server.entry.PortEntry;
import com.wnt.server.order.ConstantsDefs;
import com.wnt.web.protocol.entry.Protocol;
import com.wnt.web.socket.dao.SocketDao;
import com.wnt.web.socket.service.SocketService;
import com.wnt.web.testexecute.controller.TestThread;
import com.wnt.web.testexecute.entry.TestCaseEntry;
import com.wnt.web.testsetup.entry.LDWJ_TESTDEPLAYLIVE;
import com.wnt.web.testsetup.service.TestSetupService;

import common.TestExecuteUtil;


@Service
public class SocketServiceImpl implements SocketService{
	private final Logger log = Logger.getLogger(SocketServiceImpl.class.getName());
	@Resource
	private SocketDao socketDao;
	
	@Resource
	private TestSetupService testSetupService;

	@Override
	public void updateMac(int equipmentId, String terraceMac,
			String equipmentMac) {
		 socketDao.updateMac(equipmentId,terraceMac,equipmentMac);
	}

	@Override
	public void addChart(List<ChartEntry> listChart) {
		List<ChartEntry> listArp = new ArrayList<ChartEntry>();
		List<ChartEntry> listIcmp = new ArrayList<ChartEntry>();
		List<ChartEntry> listTcp = new ArrayList<ChartEntry>();
		List<ChartEntry> listDiscrete = new ArrayList<ChartEntry>();
		List<ChartEntry> listEth0_1 = new ArrayList<ChartEntry>();
		List<ChartEntry> listEth0_2 = new ArrayList<ChartEntry>();
		List<ChartEntry> listEth1_1 = new ArrayList<ChartEntry>();
		List<ChartEntry> listEth1_2 = new ArrayList<ChartEntry>();
		for (ChartEntry chartEntry : listChart) {
			switch (chartEntry.getMonitorid()) {
			case 1:
				listArp.add(chartEntry);
				break;
			case 2:
				listIcmp.add(chartEntry);
				break;
			case 3:
				listTcp.add(chartEntry);
				break;
			case 4:
				listDiscrete.add(chartEntry);
				break;
			case 13:
				listEth0_2.add(chartEntry);
				break;
			case 14:
				listEth0_1.add(chartEntry);
				break;
			case 15:
				listEth1_2.add(chartEntry);
				break;
			case 16:
				listEth1_1.add(chartEntry);
				break;
			default:
				break;
			}
		}
		
		//socketDao.addChart(listChart);
		socketDao.addChartArp(listArp);
		socketDao.addChartIcmp(listIcmp);
		socketDao.addChartTcp(listTcp);
		socketDao.addChartDiscrete(listDiscrete);
		socketDao.addChartEth0_2(listEth0_2);
		socketDao.addChartEth0_1(listEth0_1);
		socketDao.addChartEth1_2(listEth1_2);
		socketDao.addChartEth1_1(listEth1_1);
	}

	@Override
	public void addPort(List<PortEntry> listPort) {
		socketDao.addPort(listPort);
	}
	
	//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public void addLog( List<LogEntry> listLog){
		//TestThread t=(TestThread) EHCacheUtil.get("thread");
		TestCaseEntry tce =null ;
		if(TestExecuteUtil.testEntry != null){
			//父节点id
			List<TestCaseEntry> doTestList=new ArrayList<TestCaseEntry>();;
			int index =0;
			if(TestExecuteUtil.testEntry.getType() ==2){
				doTestList =TestExecuteUtil.testEntry.getList();
				if(TestExecuteUtil.testEntry.getProgress()==101){
					index++;
					tce =doTestList.get(index);
				}else{
					index =TestExecuteUtil.testEntry.getIndex();
					tce =doTestList.get(index);
				}
			}else{
				index =TestExecuteUtil.testEntry.getIndex();
				tce =doTestList.get(index);
			}
			String parent=TestExecuteUtil.testEntry.getTestResultId();
			//if(TestExecuteUtil.testEntry.next()!=null){
			//测试用例id
			String testResultId = tce.getTestResultId();	
			String testResultname = tce.getName();
			for(LogEntry e:listLog){
				e.setId(UUIDGenerator.getUUID());
				//父节点id
				e.setParent(parent);
	            //测试结果id
	            e.setTestResultId(testResultId);
				e.setSource(ConstantsDefs.LogType[e.getSourcetype()]);
				String m=ConstantsDefs.LogError[e.getMessagetype()];
				
				if(e.getSourcetype()<5){
					//监视器
					e.setMessage("状态更改为\""+m+"\"");
					if(e.getSourcetype() == 3 ){
						if(e.getMessagetype() == 4){
							e.setMessage("端口“"+e.getNum()+"”异常");
						}else if( e.getMessagetype() == 3){
							e.setMessage("端口“"+e.getNum()+"”恢复正常");
						}
					}	
				}else{
					//测试用例
					//通过code查询测试用例的名称
					//String name = testResultname;
					//System.out.println("line 158:"+tce);
					String name = testSetupService.findNameByCode(e.getCode());
					//try{
					//	if (StringUtils.isBlank(name)) {
					//		name=testSetupService.findNameByCode(e.getCode());
					//	}
					//}catch(Exception exception){
					//	System.out.println("根据code没有找到测试用例的名称");
					//}
					if(e.getMessagetype() == 4){
						e.setMessage(name+"发现\""+m+"\",第"+e.getNum()+"条报文");
					}else if(e.getMessagetype() == 5){
						if(e.getNum2() ==0){
							e.setMessage(name+"发现\""+m+"\",第"+e.getNum()+"条报文");
						}else{
							e.setMessage(name+"发现\""+m+"\",从第"+e.getNum()+"条报文--第"+e.getNum2()+"条报文");
						}
					}else if(e.getMessagetype()== 9){
							e.setSource("测试回溯");
							e.setMessage(name+"的回溯结果"+e.getNum()+"-"+e.getNum2()+"条报文");
						
					}else{
						e.setMessage(name+",状态更改为\""+m+"\"");
					}
				}
			}
			//}
			socketDao.addLog(listLog);
		}else{
			//System.out.println("没有找到缓存的线程数据");
			for(LogEntry e:listLog){
				e.setId(UUIDGenerator.getUUID());
				e.setSource(ConstantsDefs.LogType[e.getSourcetype()]);
				String m=ConstantsDefs.LogError[e.getMessagetype()];
				
				if(e.getSourcetype()<5){
					//监视器
					e.setMessage("状态更改为\""+m+"\"");
					if(e.getSourcetype() == 3){
						if(e.getMessagetype() == 4){
							e.setMessage("端口“"+e.getNum()+"”异常");
						}else if(e.getMessagetype() == 3){
							e.setMessage("端口“"+e.getNum()+"”恢复正常");
						}
					}
				}else{
					//测试用例
					//通过code查询测试用例的名称
					String name = testSetupService.findNameByCode(e.getCode());
				
//					log.info("插入事件日志时，根据"+e.getCode()+"在LDWJ_TESTDEPLAYLIVE表中查询到数据是："+name);
					if(e.getMessagetype() == 4){
						e.setMessage(name+"发现\""+m+"\",第"+e.getNum()+"条报文");
					}else if(e.getMessagetype() == 5){
						if(e.getNum2() ==0){
							e.setMessage(name+"发现\""+m+"\",第"+e.getNum()+"条报文");
						}else{
							e.setMessage(name+"发现\""+m+"\",从第"+e.getNum()+"条报文--第"+e.getNum2()+"条报文");
						}
					}else if(e.getMessagetype()== 9){
						e.setSource("测试回溯");
						e.setMessage(name+"的回溯结果"+e.getNum()+"-"+e.getNum2()+"条报文");
					}else{
						e.setMessage(name+",状态更改为\""+m+"\"");
					}
				}
			}
			socketDao.addLog(listLog);
		}
	}

	/* (non-Javadoc)
	 * @see com.wnt.web.socket.service.SocketService#findPort(com.wnt.server.entry.PortEntry)
	 */
	@Override
	public List<Map<String,Object>> findPort(PortEntry portEntry) {
		return socketDao.findPort(portEntry);
	}

	/* (non-Javadoc)
	 * @see com.wnt.web.socket.service.SocketService#updatePort(com.wnt.server.entry.PortEntry)
	 */
	@Override
	public void updatePort(PortEntry portEntry) {
		socketDao.updatePort(portEntry);
	}

	/* (non-Javadoc)
	 * @see com.wnt.web.socket.service.SocketService#deletePort(com.wnt.server.entry.PortEntry)
	 */
	@Override
	public void deletePort(PortEntry portEntry) {
		socketDao.deletePort(portEntry);
	}

	/* (non Javadoc) 
	 * @Title: addSysInfoLog 
	 * @Description: TODO
	 * @param listLog 
	 * @see com.wnt.web.socket.service.SocketService#addSysInfoLog(java.util.List) 
	 */
	@Override
	public void addSysInfoLog(List<LogEntry> listLog) {
		socketDao.addSysInfoLog(listLog);
	}

	/* (non Javadoc) 
	 * @Title: updateTestNum
	 * @Description: TODO
	 * @param list 
	 * @see com.wnt.web.socket.service.SocketService#updateTestNum(java.util.List) 
	 */
	@Override
	public void updateTestNum(List<LDWJ_TESTDEPLAYLIVE> list) {
		socketDao.updateTestNum(list);
	}

	
}