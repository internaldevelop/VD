<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.wnt.web.testexecute.entry.*" %>
<%@ page import="org.wnt.core.ehcache.*" %>
<%@ page import="com.wnt.web.testexecute.controller.*" %>
<%@ page import="com.wnt.web.environment.controller.*" %>
<%@ page import="common.*" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include  page="/commons/jsp/title.jsp"/>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/jsp/portscan/css/progressbar.css" ></link>
<script type="text/javascript" src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/kc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/protocolIdentity/js/list.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<style type="text/css">
	#sort{display:inline-block;width:16px;height:12px;}
	.main_table table td a.asc{background-image:url("${basePath}/images/icon_num_top.png"); background-repeat:no-repeat;}
	.main_table table td a.desc{background-image:url("${basePath}/images/icon_num_bot.png");background-repeat:no-repeat;}
</style>
<%
	int testStatus=0;
	String linktype =null;
	long rtime=0l;
	if(TestExecuteUtil.testEntry!=null){
		if(TestExecuteUtil.testEntry.getStatus()!=5){
			//测试案例运行中
			testStatus=1;
		}
	}
		if(application.getAttribute("startScan")!=null){
			//端口扫描运行中
			testStatus=2;
		}
		if(application.getAttribute("configstartScan")!=null){
			//应用安全扫描中
			testStatus = 1;
		}
		if(application.getAttribute("dynamicPortsStartScan")!=null){
			//动态端口扫描中
			testStatus = 1;
		}
		if(application.getAttribute("linktype")!=null){
			linktype =(String)application.getAttribute("linktype");
		}
		if(application.getAttribute("time")!=null){
			//动态端口扫描中
			 rtime =(Long)application.getAttribute("time");
		}
%>
<script>
		var linktypeValue=<%=linktype%>;
		var rtime =<%=rtime%>;
		var userName="<%=session.getAttribute("userName")%>";
		$(function(){
			if(<%=testStatus%>!=0){
				//页面中所有的form控件不可用
				$("#btn_ctr").attr("disabled",'true');
				$("a").removeAttr("onclick");
				interval_progressScan(0);
			}
			
			if(userName == "audit"){
			    $("#btn_ctr").attr("disabled","disabled"); 
			    $("a").removeAttr("onclick");
			}else{
				$("#btn_ctr").removeAttr("disabled");
			}
			
		});
		//定时取得端口扫描进度
		function interval_progressScan(t){
			//开启进度条控制的定时器
			scanTimeout=window.setTimeout(function(){
				$.ajax({
					type: "POST",
					url: "/VD/portscan/getProgress.do",
					data:{time:t},
					dataType :"json",
					success: function(data){
						if(data.status=="n"){
							window.location.reload();
						}else{
							interval_progressScan(data.time);
						}
						//window.parent.sendConnect();
						//window.parent.alertStartScan = function alertStartScan(flag) {

						//}
					}
				}); 
			},10000);			
		}

</script>
<style type="text/css">
.start{
	position: absolute;
    right: 65px;
    margin-top: 5px;
}
</style>

<body class="main_body">
<div class="index_body02">
<!--top-->
<div class="top_title02">
        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
        <div class="top_title_word">工控协议漏洞挖掘工具</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">协议识别</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">协议识别</div>        
</div>

<!--table-->
<div class="main_table">
<div class="table_title">
	<div>协议识别 </div>
	<form name="form" id="form" class="demoform"  method="post" action="<%=request.getContextPath() %>/protocol/startProtocolIdentity.do">
    <div class="search_input">
   			<input type="hidden" value="${proMap.startIdentity }"  id="startIdentityFlag"/>
	        <input class="start" type="submit" id="btn_ctr" value="<c:if test="${1==proMap.startIdentity}" >停止识别</c:if><c:if test="${1!=proMap.startIdentity}" >开始识别</c:if>"/>
    </div>
    </form>
</div>
<div style=" width:100%;float:left; height:60%;" >
	<div id="title_div" style="padding-right:17px;">
    <table  cellpadding="0" cellspacing="0" class="table" >
    	<tr class="table_title02">
        	<td width="10%">序号<a id="sort" href="javascript:void(0);" class="asc"></a></td>
            <td width="15%">协议名称</td>
            <td width="15%">源IP</td>
            <td width="15%">目的IP</td>
            <td width="15%">端口</td>
             <td width="20%">时间</td>
            <td width="10%">操作</td>
            
        </tr>
    </table>
    </div>
    <div class="" style="float: left; overflow-y: auto;width: 100%;" id="fix_div">
    <table id="protocolTable" cellpadding="0" cellspacing="0" class="table" style="">
        <c:forEach var="protocol" items="${list}" varStatus="index">
		<tr>
			<td width="10%">${index.count}</td>
			<td width="15%">${protocol.PROTOCOLNAME}</td>
			<td width="15%">${protocol.SRCIP}</td>
			<td width="15%">${protocol.DSTIP}</td>
			<c:if test="${protocol.PORT==0}" >
			<td>-</td>
			</c:if>
			<c:if test="${protocol.PORT!=0}" >
			<td width="15%">${protocol.PORT}</td>
			</c:if>
			<td width="20%"><fmt:formatDate value="${protocol.IDENTYTIME }" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;</td>
			<td width="10%"><a href="#" onclick="deleteProtocol('${protocol.ID }',this)">删除</a></td>
		</tr>
		</c:forEach>
    </table>
    </div>
</div>
</div>
</body>
</html>