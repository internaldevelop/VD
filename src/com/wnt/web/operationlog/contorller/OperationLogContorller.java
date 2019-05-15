package com.wnt.web.operationlog.contorller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wnt.core.uitl.DataUtils;
import org.wnt.core.uitl.Pager;

import com.wnt.web.operationlog.entity.OperationLogEntity;
import com.wnt.web.operationlog.service.OperationLogService;
import org.springframework.web.servlet.ModelAndView;
@Controller
@RequestMapping("/operationlog")
public class OperationLogContorller {
    
    @Resource
    private OperationLogService operationLogService;
    
    private static Log log = LogFactory.getLog("web");
    
    private ModelAndView modelAndView;
    //条件分页
    @ResponseBody
    @RequestMapping("/findPageOperationLogs")
    public ModelAndView findPageOperationLogs(Pager<OperationLogEntity> pager, HttpServletRequest request, @RequestParam(value="beginDate", defaultValue="")String beginDate , @RequestParam(value="endDate", defaultValue="")String endDate){
        try {
            modelAndView = new ModelAndView();
            modelAndView.setViewName("operationlog/list");
            modelAndView.addObject("logList", operationLogService.findOperationLogs(pager, beginDate, endDate));
            modelAndView.addObject("wntPage", pager);
        
            request.getSession().setAttribute("beginDate", beginDate);
            request.getSession().setAttribute("endDate", endDate);

            log.info(new Date()+"----end");
        } catch (Exception e) {
            log.debug(new Date()+"----end" + e);
            // TODO: handle exception
        }
        return modelAndView;
    }
    
    //初始化
    @RequestMapping("/operationLogPage")
    public String operationLogPage(Pager<OperationLogEntity> pager, HttpServletRequest request){                  
        Date date = DataUtils.getDate();
        String endTime = DataUtils.formatTime2(date.getTime());
        String beginTime = DataUtils.formatTime2((date.getTime()-1000*60*60*24*7));
        request.getSession().setAttribute("logList", operationLogService.findOperationLogs(pager, beginTime, endTime));
        request.getSession().setAttribute("wntPage", pager);
        return "operationlog/list";
    }   
}
