<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*" %>
<%@ page import="org.wnt.core.ehcache.*" %>
<%@ page import="com.wnt.web.testexecute.controller.*" %>
<%@ page import="common.*" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0"> 
<jsp:include  page="/commons/jsp/title.jsp"/>
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/jsp/environment/css/environment.css" ></link>
<script type="text/javascript" src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/kc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/environment/js/list.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>

<script>
function doNext(){
	var hostip=$("#hostip").val();
	var index=$("#index").val();
	window.location.href="<%=request.getContextPath() %>/configuration/querydata.do?hostip=" + hostip +"&index="+index ;
}
	
</script>
<body class="main_body">
<div class="index_body02">
<!--top-->
<div class="top_title02">
   <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
   <div class="top_title_word">工控协议漏洞挖掘工具</div>
   <div class="top_title_word">&gt;</div>
   <div class="top_title_word">应用安全</div>
   <div class="top_title_word">&gt;</div>
   <div class="top_title_word">任务设置</div>
</div>

	<!--table-->
	<div class="main_table">
		<div class="table_title">
			<div>任务设置</div>
		    <div class="right_input02">
		    </div>
		</div>
	<!--middle search-->
	<div class="main_search">
	<form name="form1" id="form1" action="<%=request.getContextPath() %>/configuration/querydata.do" class="demoform" method='post' >
		<input type='hidden' name='index' id='index' value='1' />
		<table width="100%" style="table-layout:fixed;" border=0>
	        <tr>
	            <td style="width:120px;" class="search_word">被扫描主机:</td>
	            <td style="width:300px;" class="search_input">
	            	<%-- <input type='text' name='hostip' id='hostip'  value='${data.hostip }' datatype="ip" errormsg="请输入合法的IP地址！"/>--%>
	            	<input type='text' name='hostip' id='hostip' readonly="readonly" value='${data.hostip }'/> 
	           		<span class="Validform_checktip"></span>
	            </td>
	        </tr>
        </table>
		<div  class="button_div">
     	<table cellpadding="0" cellspacing="0">
            <tr>
                <td><input type="button" value="下一步" onclick="doNext()"/></td>
            </tr>
        </table>
    	</div>
	</form>
	</div>
	

	
</div>
</div>
</body>
</html>