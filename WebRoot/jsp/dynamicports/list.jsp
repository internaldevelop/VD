<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.wnt.web.testexecute.entry.*" %>
<%@ page import="org.wnt.core.ehcache.*" %>
<%@ page import="com.wnt.web.testexecute.controller.*" %>
<%@ page import="common.*" %>
<%@ page import="org.wnt.core.uitl.ProtocolPort" %>
<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include  page="/commons/jsp/title.jsp"/>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/jsp/portscan/css/progressbar.css" ></link>
<script type="text/javascript" src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/kc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/dynamicports/js/list.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<%
	TestThread t=TestExecuteUtil.t;
	int testStatus=0;
	if(TestExecuteUtil.testEntry != null){
		if(TestExecuteUtil.testEntry.getStatus()!=5 ){
			//测试案例运行中
			testStatus=1;
		}
	}
	
	if(application.getAttribute("startScan")!=null){
		//端口扫描运行中
		testStatus=2;
	}
	if(application.getAttribute("dynamicPortsStartScan")!=null){
		testStatus = 3;
	}
	if(application.getAttribute("configstartScan")!=null){
		testStatus = 2;
	}
	
	request.setAttribute("portTypes", ProtocolPort.values());
%>
<script>
	$(function(){
		var ts = <%=testStatus%>;
		if(ts==1){
			//页面中所有的form控件不可用
			$("input").attr("disabled",'true');
			$("select").attr("disabled",'true');
			$("textarea").attr("disabled",'true');
			$("a").attr("disabled",'true');
			scanport();
		}else if(ts==2){
			//页面中所有的form控件不可用
			$("input").attr("disabled",'true');
			$("select").attr("disabled",'true');
			$("textarea").attr("disabled",'true');
		}else if(ts == 3){
			//页面中所有的form控件不可用
			$("input").attr("disabled",'true');
			$("select").attr("disabled",'true');
			$("textarea").attr("disabled",'true');
			//开始停止按钮可用
			$("#btn_ctr").removeAttr("disabled");
			begin_btn();
			scanport();
		}
		
		document.getElementById("fix_div").style.height=(window.screen.availHeight-525)+"px";
		
		
	});
	
	
</script>



<body class="main_body">
<div class="index_body02">
<!--top-->
<div class="top_title02">
        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
        <div class="top_title_word">工业控制系统安全测试及漏洞挖掘工具</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">应用安全</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">动态端口</div>
        
</div>

<!--table-->
<div class="main_table">
<div class="table_title">
	<div>扫描端口</div>
    <div class="right_input02">
    	
    </div>
</div>
<!--middle search-->
<input type="hidden" value="${scanport.startScan }" id="startScanFlag"/>
<div class="main_search">
<form name="form1" id="form1" class="demoform"  method="post" action="<%=request.getContextPath() %>/dynamicports/startScan.do">
	<table width="98%" style="table-layout:fixed;" border=0>
     <tr>
         <td style="width:60px;" class="search_word">主机IP:</td>
         <td style="width:300px;" class="search_input">
         	<%-- <input type='text' name='ip' id='ip' value='${ipAddr}' datatype="ip"  errormsg="请输入合法的IP地址！"/> --%>
         	<input type='text' name='ip' id='ip' value='${ipAddr}' readonly="readonly" />
         	<span class="Validform_checktip"></span>
         </td>
         <td style="width:60px;" class="search_word">扫描类型:</td>
         <td class="search_input">
         	<select name="scanType" id="scanType">
				<option value="0" <c:if test="${0==scanType}" >selected="selected"</c:if>>主动扫描</option>
				<option value="1" <c:if test="${1==scanType}" >selected="selected"</c:if>>被动扫描</option>
			</select>
         </td>
         <td class="search_btn">
         	<span id="msgdemo" name="sa"></span>
         	<input type="submit" id="btn_ctr" value="<c:if test="${1==scanport.startScan}" >停止扫描</c:if><c:if test="${1!=scanport.startScan}" >开始扫描</c:if>"/>
         </td>
     </tr>
     <tr>
     	<td style="width:60px;" class="search_word">扫描类型:</td>
         <td class="search_input">
         	<select name="portType" id="portType">
				<c:forEach var="pt" items="${portTypes }">
					<option value="${pt.port }" ${portType==pt.port?"selected":"" }>${pt.protocol }</option>
				</c:forEach>
			</select>
         </td>
     </tr>
 	 <tr>
 	 	<td style="width:60px;" class="search_word">扫描进度:</td>
         <td colspan="4" class="search_input">
         	<div class="progressbar_1"> 
		        <div class="bar"></div> 
		    </div> 
		    <div class="progressNum"></div>
         </td>
     </tr> 
    </table>
</form>
</div>

<div class="table_title">
	<div>开放的动态端口</div>
    <div class="right_input02">
    </div>
</div>
<!--middle search-->

<%-- <div class="main_search">
<form name="form2" id="form2" class="demoform" method="post" action="<%=request.getContextPath() %>/portscan/addPort.do">
    <table width="100%" style="table-layout:fixed;" border=0>
     <tr>
         <td style="width:60px;" class="search_word">添加端口:</td>
         <td style="width:300px;" class="search_input">
         	<input type='text' name='portNum' id='portNum' datatype="port" onblur="clearMsg()" ajaxurl="<%=request.getContextPath() %>/portscan/checkPort.do" errormsg="请输入合法的端口号！"/>
         	<span class="Validform_checktip"></span>
         </td>
         <td style="width:60px;" class="search_word">端口类型:</td>
         <td style="width:300px;" class="search_input">
         	<select name="portType" id="portType">
				<option value="6">TCP</option>
				<option value="17">UDP</option>
			</select>
         </td>
         <td class="search_btn">
         	<input type="submit" value="添加" <c:if test="${1==scanport.startScan}" >disabled="disabled"</c:if>/>
         	<span id="msgdemo"></span>
         </td>
     </tr>
     
    </table>
</form>
</div> --%>
<div style=" width:100%;float:left; height:60%;" >
	<div id="title_div" style="padding-right:17px;">
    <table  cellpadding="0" cellspacing="0" class="table" >
    	<tr class="table_title02">
        	<td width="33.33%">端口</td>
        	<td width="33.33%"> 端口类型 </td>
            <td width="33.33%">扫描类型</td>
        </tr>
    </table>
    </div>
    <div class="" style="float: left; overflow-y: auto;width: 100%;" id="fix_div">
    <table id="scanTable" cellpadding="0" cellspacing="0" class="table" style="">
        <c:forEach var="dc" items="${list}" varStatus="index">
			<tr>
				<td width="33.33%">${dc.portNum}</td>
				<td width="33.33%">${dc.portType==6?"TCP":"UDP"}</td>
				<td width="33.33%">${dc.scanType==1?"被动扫描":"主动扫描"}</td>
			</tr>
		</c:forEach>
    </table>
    </div>

</div>
</div>
</div>
</body>
</html>