<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<meta http-equiv="expires" CONTENT="0">
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/controller/My97DatePicker/WdatePicker.js"></script>
<link href="<%=request.getContextPath()%>/controller/My97DatePicker/skin/WdatePicker.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/commons/js/jquery.js" ></script>
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/commons/css/canvas.css" ></link>
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/commons/css/newcss.css" ></link>


<script type="text/javascript">
//选择的测试模板

var basePath='<%=basePath%>';

	Date.prototype.pattern = function (fmt) { //author: meizz
	    var o = {
	    "M+": this.getMonth() + 1, //月份
	    "d+": this.getDate(), //日
	    "h+": this.getHours(), //小时
	    "H+": this.getHours(), //小时
	    "m+": this.getMinutes(), //分
	    "s+": this.getSeconds(), //秒
	    "q+": Math.floor((this.getMonth() + 3) / 3), //季度
	    "S": this.getMilliseconds() //毫秒
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	}
    
    $(function() {
        function autoResize(){
            $("body").width($(window).width()-2); 
        }
        autoResize();
       $(window).resize(autoResize);
        var date = new Date();
        if($("#beginDate").val() == ""){
	        var pdate = new Date(date.getTime() -24 * 7 * 3600 * 1000);
	        $("#beginDate").val(pdate.pattern("yyyy-MM-dd HH:mm:ss"));
        }
        if($("#endDate").val() == ""){
		    $("#endDate").val(date.pattern("yyyy-MM-dd HH:mm:ss"));
        }
    });
    
    function query(){
        document.getElementById("current_page").value = 1;		

		$("#beginDate").val($("#beginDate").val().replace(/"|&|'|<|>/g," "));
		$("#endDate").val($("#endDate").val().replace(/"|&|'|<|>/g," "));
		
		$("#page_form").submit();
    }
    
    
</script>

<body class="main_body">
    <input type="hidden" id="testId" value="${testId }">
<!--    <div class="index_body02"> -->
    <div class="main_search">
       <div class="top_title02">
        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
        <div class="top_title_word">工业控制系统安全测试及漏洞挖掘工具</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">系统配置</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">系统日志</div>        
    </div>

	<!--table-->
	<div class="main_table"> 
	<div class="table_title">
	    <div>系统日志</div>
	    <div class="search_input">
	
	    </div>
	</div>
        
        
        <div class="main_search">
            <form id="page_form" action="<%=basePath%>operationlog/findPageOperationLogs.do" >
                
                <input type="hidden" id="sortId" name="sortId" value = "0">
	                <table width="100%" style="table-layout:fixed;" border=0>
	                    <tr>
	                        <td style="text-align:left;">
                                <div>                                  
		                            <div>
						                <!--middle search-->
			                            <ul class="search_ul"style="margin-left: 20px;">
			                                <li >开始时间：</li>
			                                <li class="search_input">
			                                   <input type='text' name='beginDate' id="beginDate" value="${beginDate }" class="form-control" 
			                                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\');}',isShowClear:false,readOnly:true})"/>
			                                </li>
			                            </ul>
			                            <ul class="search_ul">
			                                <li class="">结束时间：</li>
			                                <li class="search_input">
			                                   <input type='text' name='endDate' id="endDate" value="${endDate }" class="form-control" 
			                                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginDate\');}',isShowClear:false,readOnly:true})" />
			                                </li>
			                            </ul>
							        </div>
	                            </div>
                                <div class="search_btn">
                                    <input type="button" id="clearLogId" value="查询"  onclick="query()" style='margin-right:50px'/>
                                </div>
	                        </td>
	                    </tr>
	                </table>
                    <div>
                        <table cellpadding="0" cellspacing="0" class="table" width="850px">
                            <tr class="table_title02">                                
                                <td width="100px">用户名</td>
                                <td width="100px">IP</td>
                                <td width="100px">操作名称</td>
                                <td width="100px">操作结果</td>
                                <td width="250px">操作详情</td>
                                <td width="150px">操作时间</td>
                            </tr>
                        </table>
                    </div>
                    <div style="height: 640px; margin-left: 5px;" id="logdiv" name="logdiv">
                        <div style="height: 640px; overflow-y: auto; max-height: 640px;" id="logdiv" name="logdiv">
		                    <table id="logTable" name="logTable" cellpadding="0" cellspacing="0" class="table" width="850px" style="table-layout:fixed" >
                                <c:forEach var="m" items="${logList}" varStatus="index">
                                    <tr>
                                       <td width="100px">
                                            ${m.username}
                                        </td>
                                        <td width="100px">
                                            ${m.ip}
                                        </td>                                         
                                        <td width="100px" style="word-wrap:break-word;">
                                            ${m.operation}
                                        </td>
                                        <td width="100px">
                                            ${m.result}
                                        </td>
                                        <td width="250px" style="word-wrap:break-word;" >
                                            ${m.content}
                                        </td>
                                        <td width="150px">                                       	
                                        	<fmt:parseDate value="${m.createtime}" pattern="yyyy-MM-dd HH:mm:ss" var="showCreateTime"/>
                             				<fmt:formatDate value="${showCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>                                       	
                                        </td>                                      
                                    </tr>
                                </c:forEach>
		                    </table>
		                </div>
                        <div style="width: 1300px; font-size: 12px;" >
                            <jsp:include  page="/jsp/pager/page.jsp"/>
                        </div>
                    </div>
                    <div>
                        
                    </div>
            </form>
        </div>
    </div>
    
</body>
</html>