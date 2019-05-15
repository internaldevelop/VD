package com.wnt.web.operationlog.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.wnt.core.uitl.Pager;

import com.wnt.web.operationlog.dao.OperationLogDao;
import com.wnt.web.operationlog.entity.OperationLogEntity;

@Repository("operationLogDao")
public class OperationLogDaoImpl implements OperationLogDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    
    @Override
    public void addOperationLog(String user, String ipAddr, String operation, String result, String content) {
        // TODO Auto-generated method stub
        String sql = "insert into ts_operation_log (USERNAME, CREATETIME, OPERATION, RESULT, CONTENT,IP) values (?,now(), ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user, operation, result, content, ipAddr);
    }

    @Override
    public List<OperationLogEntity> findPageOperationLogs(Pager<OperationLogEntity> page, String beginDate, String endDate){
        Map<String, String> paramesMap = addCondition(beginDate, endDate);
        
        String conditionSql = paramesMap.get("sql");
        String sql = "SELECT * FROM ts_operation_log WHERE 1=1 " + conditionSql +" ORDER BY CREATETIME DESC LIMIT ?,?";
        int begin = (page.getCurrent_page()-1) * page.getPage_size();//页数*每页显示数量=从第条数据开始
        int numPerPage = page.getPage_size();//每页展示多少行
        
        int size = paramesMap.size();
        int paramNum = size - 1 + 2; //Map中固定包含sql
        Object[] params = new Object[paramNum];
        int index = 0;
        if(paramesMap.get("beginDate") != null) {
            params[index++] = beginDate;
        }
        if(paramesMap.get("endDate") != null) {
            params[index++] = endDate;
        }
       
        params[index++] = begin;
        params[index++] = numPerPage;
        return jdbcTemplate.query(sql, new OperationLogEntity(), params);
    }
   
    @Override
    public int countOperationLog(Pager<OperationLogEntity> page, String beginDate, String endDate) {
        Map<String, String> paramesMap = addCondition(beginDate, endDate);
        String conditionSql = paramesMap.get("sql");
        String sql = "SELECT  COUNT(*) AS COUNT FROM ts_operation_log WHERE 1=1 " + conditionSql;
        
        int size = paramesMap.size();
        int paramNum = size - 1; //Map中固定包含sql
     
        List<Map<String, Object>> listCount = new ArrayList<Map<String, Object>>();
        if(paramNum != 0) {
            Object[] params = new Object[paramNum];
            int index = 0;
            if(paramesMap.get("beginDate") != null) {
                params[index++] = beginDate;
            }
            if(paramesMap.get("endDate") != null) {
                params[index++] = endDate;
            }             
            listCount = jdbcTemplate.queryForList(sql, params);
        }else {
            listCount = jdbcTemplate.queryForList(sql);
        }
        
        int logSize = Integer.parseInt(listCount.get(0).get("COUNT").toString());
        logSize = logSize == 0 ? 1 : logSize;
        int pageNum = logSize / page.getPage_size();
        if((page.getPage_size() * pageNum) < logSize){
            pageNum++;
        }
        page.setTotal_count(logSize);
        page.setTotal_page(pageNum);
        return pageNum;
    }
    
    private Map<String, String> addCondition(String beginDate, String endDate){        
        Map<String, String> paramesMap = new HashMap<String, String>();
        
        String sql = "";
        //初次进入界面查询所有日志
        if(!beginDate.equals("")){
            sql += " AND CREATETIME >= ?";
            //是否包含beginDate参数
            paramesMap.put("beginDate", "true");
        }
        if(!endDate.equals("")){
            sql += " AND CREATETIME <= ?";
            //是否包含endDate参数
            paramesMap.put("endDate", "true");
        }
     
        //要执行的SQL语句
        paramesMap.put("sql", sql);
        return paramesMap;
    }
}
