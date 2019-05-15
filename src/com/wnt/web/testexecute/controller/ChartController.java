package com.wnt.web.testexecute.controller;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.fop.svg.PDFTranscoder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.StringUtil;

import com.itextpdf.text.xml.xmp.DublinCoreSchema;
import com.wnt.web.environment.service.EnvironmentService;
import com.wnt.web.testexecute.entry.SearchDto;
import com.wnt.web.testexecute.service.ChartService;
import com.wnt.web.testexecute.service.TestExecuteService;

/**
 * 测试执行图表控制类
 * 
 * @author 付强
 * @version 1.0
 * @company 汇才同飞
 * @site http://www.javakc.cn
 * 
 */
@Controller
@RequestMapping("/chart")
public class ChartController {
	
	private final Logger log = Logger.getLogger(ChartController.class.getName());

	private ModelAndView modelAndView;

	private static final String SUCCESS_PAGE = "index";

	@Resource
	private ChartService chartService;
	@Resource
	private TestExecuteService testExecuteService;
	@Resource
	EnvironmentService environmentService;
	
	private Map map = new HashMap();
	
	
	/**
	 * 测试结果 图形数据 
	 * @param 1:ARP   2:ICMP 3:TCP 13:端口0  14:端口1
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/queryArp")
	public List<Object[]> queryArp(int type,long time) throws Exception
	{
		return chartService.queryArp(type,time);
	}
	
	@ResponseBody
	@RequestMapping("/findData")
	public Map findData(String[] chk_m,String lastId) throws Exception
	{
		try{
			if("-1".equals(lastId)){
				lastId = chartService.getMaxid();
			}
			List<Map<String, Object>> list=chartService.queryArp(chk_m, lastId);
			
			int num = list.size();
			//取得最后的时间
			if(num>0){
				String str = list.get(num-1).get("id").toString();
//				Object o=list.get(0).get("id");
				map.put("lastId", str);
			}else{
				map.put("lastId", lastId+"");
			}
			map.put("result", list);
			return map;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
//		if(chk_m!=null){
//			Arrays.sort(chk_m);
//			//ARP 散点图 1
//			if(Arrays.binarySearch(chk_m, "1")>=0){
//				List<Object[]> list=chartService.queryArp(1,time);
//				map.put("d1", list);
//			}
//			//ICMP 散点图 2
//			if(Arrays.binarySearch(chk_m, "2")>=0){
//				List<Object[]> list=chartService.queryArp(1,time);
//				map.put("d2", list);
//			}
//			//TCP 折线图 3
//			if(Arrays.binarySearch(chk_m, "3")>=0){
//				List<Object[]> list=chartService.queryArp(3,time);
//				map.put("d3", list);
//			}
//			//离散 方波图 4
//			//离散 散点图 4
//			if(Arrays.binarySearch(chk_m, "4")>=0){
//				List<Object[]> list=chartService.queryArp(4,time);
//				map.put("d4", list);
//			}
//			//Eth0 折线图 发送 eth0_1  14
//			if(Arrays.binarySearch(chk_m, "eth0")>=0){
//				List<Object[]> list=chartService.queryArp(14,time);
//				map.put("deth0_1", list);
//			}
//			//Eth0 折线图 接受 eth0_2 13
//			if(Arrays.binarySearch(chk_m, "eth0")>=0){
//				List<Object[]> list=chartService.queryArp(13,time);
//				map.put("deth0_2", list);
//			}
//			//Eth1 折线图 发送 eth1_1 16
//			if(Arrays.binarySearch(chk_m, "eth1")>=0){
//				List<Object[]> list=chartService.queryArp(16,time);
//				map.put("deth1_1", list);
//			}
//			//Eth1 折线图 接受 eth1_2 15
//			if(Arrays.binarySearch(chk_m, "eth1")>=0){
//				List<Object[]> list=chartService.queryArp(14,time);
//				map.put("deth1_2", list);
//			}	
//				
//		}
		
	}
	
	@ResponseBody
	@RequestMapping("/queryArpNew")
	public Map queryArpNew(int type,long time) throws Exception
	{
		try{
			List<Object[]> list=chartService.queryArp(type,time);
			map.put("list", list);
			if(list!=null && list.size()>0){
				//取得最后的数据
				Object[] arr=list.get(list.size()-1);
				time=((Date)arr[0]).getTime();
			}
			//设置最后的时间
			map.put("time", time);
			map.put("status", "y");
			//System.out.println("chart:"+new Date().toLocaleString()+":"+list.size());
		}catch(Exception e){
			e.printStackTrace();
			map.put("status", "n");
		}
		return map;
	}
	/**
	 * 测试结果 图形数据 TCP
	 * @param 3:TCP 13:端口0  14:端口1
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/findAlarmLevel")
	public List<Object> findAlarmLevel() throws Exception
	{
		List list=new ArrayList();
		try{
			Map<String,Object> map=environmentService.findMonitorById(4);
			Object o=map.get("ALARMLEVEL");
			if(o!=null){
				int alarmLevel=(Integer)o;
				list.add(-alarmLevel);
				list.add(alarmLevel);
			}else{
				list.add(-20);
				list.add(20);
				
			}
		}catch(Exception e){
			e.printStackTrace();
			list.add(-20);
			list.add(20);
		}
		return list;
	}
//	
//	//取得时间点后的数据
//	@ResponseBody
//	@RequestMapping("/queryTcpNew")
//	public Map queryTcpNew(int type,long time) throws Exception
//	{	
//		try{
//			List<String[]> list=chartService.queryArp(type,time);
//			map.put("list", list);
//			if(list!=null && list.size()>0){
//				//取得最后的数据
//				String[] arr=list.get(list.size()-1);
//				time=Long.valueOf(arr[0]);
//			}
//			//设置最后的时间
//			map.put("time", time);
//			map.put("status", "y");
//		}catch(Exception e){
//			e.printStackTrace();
//			map.put("status", "n");
//		}
//		return map;
//	}
	@ResponseBody
	@RequestMapping("/test")
	public void test(int type) throws Exception
	{
		try{
			chartService.insertBatch(type);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@ResponseBody
	@RequestMapping("/test2")
	public void test2(int type) throws Exception
	{
		try{
			chartService.insertBatchNull(type);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@ResponseBody
	@RequestMapping("/queryHistory")
	public Map<String,Object> queryHistory(HttpServletRequest request,SearchDto dto){
		Map<String,Object> m = new HashMap<String, Object>();
		//Map<Integer,List<Object[]>> map = null;
		if(StringUtils.isNotBlank(dto.getBeginDate()) && StringUtils.isNotBlank(dto.getEndDate())){
			String start = dto.getBeginDate();
			String end = dto.getEndDate();
			try {
				Map<Integer, List<Object[]>> data = chartService.queryHistory(start, end);
				m.put("status", "y");
				m.put("data",data);
			} catch (Exception e) {
				e.printStackTrace();
				m.put("status", "n");
				m.put("msg", "查询的开始时间或结束时间必须为有限的时间格式");
			}
//			String startTime = DataUtils.formatTime2(Long.valueOf(start));
//			String endTime = DataUtils.formatTime2(Long.valueOf(end));	
		}else{
			m.put("status", "n");
			m.put("msg", "查询的开始时间或结束时间不能为空");
		}
		return m;
//		if(dto.getBeginDate()==null && dto.getBeginDate() ==null){
//			Date date = new Date();//当前日期
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化对象
//			//String end = sdf.format(date);
//			Calendar calendar = Calendar.getInstance();//日历对象
//			calendar.setTime(date);//设置当前日期
//			calendar.add(Calendar.DAY_OF_MONTH, -1);//前一个月
//			String start = sdf.format(calendar.getTime());
//			map=chartService.queryPartOfHistory(start,sdf.format(new Date()),tableName);
//		}
	}

	/**
	 * 
	 * @param list		数据库查询的数据
	 * @param startTime 查询开始时间
	 * @param endTime	查询结束时间
	 * @param maxTime	最大时间
	 * @param minTime	最小时间
	 * @return
	 */
	private List<Object[]> fillupHitoryData(List<Object[]> list,Date startTime,Date endTime,Long maxTime,Long minTime){
		List<Object[]> newList = new ArrayList<Object[]>();
		//时间差值
		Long shortOf = 0l;
		//上一条记录的时间值
		Long oldTime = null;
		if(list != null && list.size()>0){
			for(Object[] data : list){
				if(oldTime != null){
					shortOf = (long)(((Long)data[0]-oldTime-1000)/1000);
					if(shortOf > 0){
						for(long i =0;i<shortOf;i++){
							oldTime = oldTime + 1000;
							newList.add(new Object[]{oldTime,null});
						}
						newList.add(data);
					}else{
						oldTime = (Long)data[0];
						newList.add(data);
					}
				}else{
					oldTime = (Long)data[0];
					newList.add(data);
				}
			}
		}else{
			Long stime = startTime.getTime();
			Long etime = endTime.getTime();
			if(maxTime==null || minTime == null || etime <= minTime || stime >= maxTime){
				return newList;
			}
			if(stime < minTime){
				stime = minTime;
			}
			if(etime > maxTime){
				etime = maxTime;
			}
			shortOf = (etime - stime)/1000;
			oldTime = startTime.getTime();
			for(int i=0;i<shortOf;i++){ 
				newList.add(new Object[]{oldTime,null});
				oldTime = oldTime + 1000;
			}
		}
		return newList;
	}
	

	
//	
//	@ResponseBody
//	@RequestMapping("/queryArpOrIcmpForAsync")
//	public Object queryArpOrIcmpForAsync(HttpServletRequest request,String start,String end ,String type){
//		int typeC =Integer.valueOf(type);
//		String tableName=null;
//		switch (typeC) {
//			case 1:
//				tableName="LDWJ_CHART_ARP";
//					break;
//			case 2:
//				tableName="LDWJ_CHART_ICMP";	
//					break;
//
//		}
//		List<Object[]> list=null;
//		if(StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)){
//			String startTime = DataUtils.formatTime2(Long.valueOf(start));
//			String endTime = DataUtils.formatTime2(Long.valueOf(end));
//				list=chartService.queryPartOfHistory(startTime,endTime,tableName);	
//		}
//		return list;
//	}

	/**
	 * 图形导出
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/saveAsImage")
	public void SaveAsImage(HttpServletRequest request,
			HttpServletResponse response, String type, String svg,
			String filename) throws IOException {
		request.setCharacterEncoding("utf-8");// 设置编码，解决乱码问题
//		type = request.getParameter("type");
//		svg = request.getParameter("svg");
//		filename = request.getParameter("filename");
		
		if(StringUtil.htmlCharRegex(type)
		        || StringUtil.htmlCharRegex(svg)
		        || StringUtil.htmlCharRegex(filename)) {
		    return;
		}	
		
		filename = filename == null ? "chart" : filename;
		ServletOutputStream out = response.getOutputStream();
		if (null != type && null != svg) {
			svg = svg.replaceAll(":rect", "rect");
			String ext = "";
			Transcoder t = null;
			if (type.equals("image/png")) {
				ext = "png";
				t = new PNGTranscoder();
			} else if (type.equals("image/jpeg")) {
				ext = "jpg";
				t = new JPEGTranscoder();
			} else if (type.equals("application/pdf")) {
				ext = "pdf";
				t = new PDFTranscoder();
			} else if (type.equals("image/svg+xml")) {
				ext = "svg";
			}
			
			response.addHeader("Content-Disposition","attachment;filename=\""+ new String((filename).getBytes("GB2312"),"iso8859-1")+  "." + ext+"\"");
			response.addHeader("Content-Type", type);

			if (null != t) {
				TranscoderInput input = new TranscoderInput(new StringReader(
						svg));
				TranscoderOutput output = new TranscoderOutput(out);

				try {
					t.transcode(input, output);
				} catch (TranscoderException e) {
					out.print("Problem transcoding stream. See the web logs for more details.");
					e.printStackTrace();
				}
			} else if (ext.equals("svg")) {
				OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
				writer.append(svg);
				writer.close();
			} else
				out.print("Invalid type: " + type);
		} else {
			response.addHeader("Content-Type", "text/html");
			out.println("Usage:\n\tParameter [svg]: The DOM Element to be converted."
					+ "\n\tParameter [type]: The destination MIME type for the elment to be transcoded.");
		}
		out.flush();
		out.close();
	}
}
