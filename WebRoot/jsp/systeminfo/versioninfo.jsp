<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*" %>
<%@ page import="org.wnt.core.ehcache.*" %>
<%@ page import="com.wnt.web.testexecute.controller.*" %>
<%@ page import="common.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<jsp:include page="/commons/jsp/title.jsp" />
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/jsp/system/css/main.css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/commons/js/kc.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/jsp/system/js/main.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<style type="text/css">
	.main_table  table.table{width:100%;}
	.main_table table.table td{border:1px solid;padding:10px 10px;background-color:#f0f0f0;} 
	td img{vertical-align:middle;}
	#btcsShutdown,#btcsReboot,#btbcReboot{cursor:pointer;}
	#btcsReboot2{color:#71b83d;}
	.box{margin:80px auto;background-color:#f6f6f6;width:350px;border:1px solid;border-radius:10px;}
	.box .header{background-color:#e5e5e5; border-radius:10px 10px 0px 0px; border-bottom:1px solid;text-align:center;height:30px;line-height:30px;}
	.box .content{height:150px;text-align:center;}
	.box .content .demoform{margin-top:20px;}
	.box label{font-size:12px;}
</style>
<%
	TestThread t=TestExecuteUtil.t;
	int testStatus=0;
	if(TestExecuteUtil.testEntry!=null){
		if(TestExecuteUtil.testEntry.getStatus()!=5 ){
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
%>
<script>
$(function(){
		if(<%=testStatus%>!=0){
			//页面中所有的form控件不可用
				$("input").prop("disabled",true);
				$("a").prop("disabled",true); 
				$("#btcsShutdown").unbind();
				$("#btcsReboot").unbind();
				$("#btcsReboot2").unbind();
				$("#btbcReboot").unbind();
			}
});
</script>
</head>

<body class="main_body">
	<!--top-->
	<div class="top_title02">
		<div class="top_title_img">
			<img src="<%=request.getContextPath()%>/images/right_icon01.png"
				width="14" height="16">
		</div>
		<div class="top_title_word">工控协议模糊测试工具</div>
		<div class="top_title_word">&gt;</div>
		<div class="top_title_word">系统信息</div>
		<div class="top_title_word">&gt;</div>
		<div class="top_title_word">版本信息</div>

	</div>
	<div class="main_table">
		
		<div class="table_title">
				<div>工控协议模糊测试工具版本</div>
			    <div class="right_input02">
			    </div>
			</div>
			<div style="height:40px;"></div>
					<table cellpadding="0" cellspacing="0"  class="table" id ="table1">
						<tbody>
							<tr>
								<td style="width: 200px;">
									<img src="<%=request.getContextPath()%>/images/hardware1.png"/> &nbsp;工控协议模糊测试工具版本
								</td>
								<td colspan="3" style="text-align: left;padding-left:20px;">
									<span id="version" title="重启"><img  name="version" class="btn"  src="<%=request.getContextPath()%>/images/version1.png"/> &nbsp; 版本号 : ${version}</span>
								</td>
							</tr>
						</tbody>
					</table>
	</div>
</body>
</html>