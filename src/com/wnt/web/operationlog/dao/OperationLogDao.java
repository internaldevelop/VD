package com.wnt.web.operationlog.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.wnt.core.uitl.Pager;

import com.wnt.web.operationlog.entity.OperationLogEntity;

public interface OperationLogDao {
    void addOperationLog(String user, String ipAddr, String operation, String result, String content);
    
    List<OperationLogEntity> findPageOperationLogs(Pager<OperationLogEntity> page, String beginDate, String endDate);
    int countOperationLog(Pager<OperationLogEntity> page, String beginDate, String endDate);
}
