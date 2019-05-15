package com.wnt.web.operationlog.service.impl;

import java.net.InetSocketAddress;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.wnt.core.uitl.Pager;

import com.wnt.web.operationlog.dao.OperationLogDao;
import com.wnt.web.operationlog.entity.OperationLogEntity;
import com.wnt.web.operationlog.service.OperationLogService;

@Service("operationLogService")
public class OperationLogServiceImpl implements OperationLogService {

    @Resource
    OperationLogDao operationLogDao;

    @Override
    public void addOperationLog(String user, HttpServletRequest request, String operation, String result, String content) {
        // TODO Auto-generated method stub
        try {
            String ipAddr = getIpAddress(request);
            operationLogDao.addOperationLog(user, ipAddr, operation, result, content);
        }catch(Exception e) {
            
        }
    }

    private String getIpAddress(HttpServletRequest request) {          
        String ip = request.getRemoteAddr();
        if("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
     }  
    
    @Override
    public List<OperationLogEntity> findOperationLogs(Pager<OperationLogEntity> page, String beginDate, String endDate){
        List<OperationLogEntity> logList = null;
        operationLogDao.countOperationLog(page, beginDate, endDate);
        logList = operationLogDao.findPageOperationLogs(page, beginDate, endDate);

        page.paging();
        return logList;
    }
}
