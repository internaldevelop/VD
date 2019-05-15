<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*" %>
<%@ page import="org.wnt.core.ehcache.*" %>
<%@ page import="com.wnt.web.testexecute.controller.*" %>
<%@ page import="com.wnt.web.protocol.ProtocolController.*" %> 
<%@ page import="common.*" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
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
		testStatus = 2;
	}
	if(application.getAttribute("dynamicPortsStartScan")!=null){
		//动态端口扫描中
		testStatus = 2;
	}
	if(application.getAttribute("startIdentity")!=null){
		//动态端口扫描中
		testStatus = 2;
	}
	
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>图形数据</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="jsp/testsetup/css/testsetup.css"></link>
<link rel="stylesheet" href="jsp/testresult/css/testresult.css"></link>
<script type="text/javascript"
	src="jsp/testresult/js/jquery-1.9.1.min.js"></script>
<script src="jsp/testresult/js/highstock.js"></script>
<script src="jsp/testresult/js/exporting.1.2.5.js"></script>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<script src="jsp/testresult/js/data.js"></script>
<style type="text/css">
#bg {
	display: none;
	position: absolute;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 100%;
	background-color: #DCDCDC;
	z-index: 1001;
	-moz-opacity: 0.7;
	opacity: .70;
	filter: alpha(opacity = 70);
}
</style>

<script>
	$(function(){
		if(<%=testStatus%>!=0){
			document.getElementById("bg").style.display ="block";
		}
	});
	
</script>
</head>

<body>
<div id="bg"></div>
	<div id="table_title">
		<div>图形数据</div>
	</div>
	<div class="tabs">
		<ul id="tabs">
			<li class="tab-nav-action">ARP 监视器</li>
			<li class="tab-nav">ICMP 监视器</li>
			<li class="tab-nav">TCP 监视器</li>
			<li class="tab-nav">离散 监视器</li>
			<li class="tab-nav">端口0 监视器</li>
			<li class="tab-nav">端口1 监视器</li>
		</ul>
	</div>
	<div id="tabs-body" class="tabs-body">
		<div id="container0" class="div_container2" style="overflow: hidden">
			<iframe src="jsp/testresult/soag_sandian.jsp" scrolling="no"
				name="container01" id="container01" frameborder="0" height="100%"
				width="98%" style="float:left;"></iframe>
		</div>
		<div id="container1" class="div_container">
			<iframe src="jsp/testresult/soag_sandian.jsp" scrolling="no"
				name="container11" id="container11" frameborder="0" height="100%"
				width="98%" style="float:left;"></iframe>
		</div>
		<div id="container2" class="div_container">
			<iframe src="jsp/testresult/soag.jsp" scrolling="no"
				name="container21" id="container21" frameborder="0" height="100%"
				width="98%" style="float:left;"></iframe>
		</div>
		<div id="container3" class="div_container">
			<iframe src="jsp/testresult/soag_1.jsp" scrolling="no"
				name="container31" id="container31" frameborder="0" height="230px"
				width="98%" style="float:left;"></iframe>
			<iframe src="jsp/testresult/soag_2.jsp" scrolling="no"
				name="container32" id="container32" frameborder="0" height="100%"
				width="98%" style="float:left;"></iframe>
		</div>
		<div id="container4" class="div_container">
			<iframe src="jsp/testresult/soag.jsp" scrolling="no"
				name="container41" id="container41" frameborder="0" height="100%"
				width="98%" style="float:left;"></iframe>
			<iframe src="jsp/testresult/soag.jsp" scrolling="no"
				name="container42" id="container42" frameborder="0" height="100%"
				width="98%" style="float:left;"></iframe>
		</div>
		<div id="container5" class="div_container">
			<iframe src="jsp/testresult/soag.jsp" scrolling="no"
				name="container51" id="container51" frameborder="0" height="100%"
				width="98%" style="float:left;"></iframe>
			<iframe src="jsp/testresult/soag.jsp" scrolling="no"
				name="container52" id="container52" frameborder="0" height="100%"
				width="98%" style="float:left;"></iframe>
		</div>
	</div>

</body>
</html>
