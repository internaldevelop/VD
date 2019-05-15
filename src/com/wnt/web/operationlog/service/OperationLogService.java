package com.wnt.web.operationlog.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.wnt.core.uitl.Pager;
import com.wnt.web.operationlog.entity.OperationLogEntity;

public interface OperationLogService {
    void addOperationLog(String user, HttpServletRequest request, String operation, String result, String content);
    List<OperationLogEntity> findOperationLogs(Pager<OperationLogEntity> page, String beginDate, String endDate);
}
