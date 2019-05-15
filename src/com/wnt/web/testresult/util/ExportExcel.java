package com.wnt.web.testresult.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

public class ExportExcel {
	
	public static List<Map<String, Object>> getStudent() throws Exception  
    {  
        List list = new ArrayList();  
  
        Map map1 = new HashMap();
        map1.put("id", "AAA（10:30:24）");
        map1.put("name", "10:30:24");
        map1.put("age", "ARP风暴测试");
        
        Map map2 = new HashMap();
        map2.put("id", "AAA（10:30:24）");
        map2.put("name", "10:31:01");
        map2.put("age", "ARP语法测试");
        
        Map map3 = new HashMap();
        map3.put("id", "BBB（11:30:24）");
        map3.put("name", "11:30:24");
        map3.put("age", "ARP风暴测试");
        
        Map map4 = new HashMap();
        map4.put("id", "BBB（11:30:24）");
        map4.put("name", "11:31:01");
        map4.put("age", "ARP语法测试");
        
        list.add(map1);
        list.add(map2); 
        list.add(map3); 
        list.add(map4);
        
        return list;  
    }
	
	public static void export(List list, HttpServletResponse response) throws Exception  
    {  
        // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("测试结果数据");
        sheet.setColumnWidth(0, 50 * 256);
        sheet.setColumnWidth(1, 25 * 256);
        sheet.setColumnWidth(2, 25 * 256);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
  
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("执行结果名称");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);
        cell.setCellValue("执行开始时间");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("测试用例名称");  
        cell.setCellStyle(style);
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，  
//        List list = ExportExcel.getStudent();
        
        int k = 0;
        
        for (int i = 0; i < list.size(); i++)  
        {
        	//style.setAlignment(HSSFCellStyle.VERTICAL_CENTER); // 创建一个居中格式
        	
        	//合並單元格,下標從0開始
           // sheet.addMergedRegion(new Region(1,(short)0  ,list.size(),(short)0));
            row = sheet.createRow((int) k + 1);
            //row.setRowStyle(style); 
//            sheet.mergeCells(0,0,5,0);
            Map stu = (Map) list.get(i);
            //如果 执行结果不同 添加空白行
            //name:模板 name1:测试用例 createtime:执行时间
            
//            if(i == 2)
//            {
//            	row = sheet.createRow((int) k + 2);
//            	//已经添加空白行
//            	//再添加一行 插入数据
//            	row.createCell((short) 0).setCellValue((String)stu.get("name1"));
//                row.createCell((short) 1).setCellValue((Timestamp)stu.get("createtime"));  
//                row.createCell((short) 2).setCellValue((String)stu.get("NAME"));
//                k++;
//            }
//            else
//            {
            	// 第四步，创建单元格，并设置值
                row.createCell((short) 0).setCellValue((i==0)?(String)stu.get("name1"):"");
                row.createCell((short) 1).setCellValue(((Timestamp)stu.get("time")).toString());
                row.createCell((short) 2).setCellValue((String)stu.get("NAME"));
//            }
            k++;
        }
        
        // 第六步，将文件存到指定位置
        try  
        {  
            
        	ByteArrayOutputStream os = new ByteArrayOutputStream();
        	wb.write(os);
        	byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
        	
            response.reset();
        	response.setContentType("application/vnd.ms-excel;charset=utf-8");
        	
        	response.setHeader("Content-Disposition", "attachment;filename="
                    + new String("导出excel数据".getBytes("GB2312"),"iso8859-1")+  ".xls");
            ServletOutputStream out = response.getOutputStream();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                bis = new BufferedInputStream(is);
                bos = new BufferedOutputStream(out);
                byte[] buff = new byte[2048];
                int bytesRead;
                // Simple read/write loop.
                while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                    bos.write(buff, 0, bytesRead);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
        }
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
    }
}
