<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*"%>
<%@ page import="org.wnt.core.ehcache.*"%>
<%@ page import="com.wnt.web.testexecute.controller.*"%>
<%@ page import="common.*"%>
<%
	String path = request.getContextPath();
	String basePdfPath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ "/pdf/";
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%
	TestThread t = TestExecuteUtil.t;
	int testStatus = 0;
	if (t != null && TestExecuteUtil.testEntry.getStatus() != 5) {
		//测试案例运行中
		testStatus = 1;
	}
	if (application.getAttribute("startScan") != null) {
		//端口扫描运行中
		testStatus = 2;
	}
	if (application.getAttribute("dynamicPortsStartScan") != null) {
		testStatus = 2;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>测试结果</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="jsp/appsecurity/testresult/css/demo.css"
	type="text/css">
<link rel="stylesheet" href="commons/css/ztree/zTreeStyle.css"
	type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/jsp/appsecurity/testresult/css/testsetup.css"></link>
<script type="text/javascript">   
	   var basePath = "<%=request.getContextPath()%>/";
	   var basePdfPath = "<%=basePdfPath%>";
	</script>
<script type="text/javascript"
	src="commons/js/jquery.js"></script>
<script type="text/javascript"
	src="commons/js/ztree/jquery.ztree.core-3.5.js"></script>
<%
	if (testStatus == 0) {
%>
<script type="text/javascript"
	src="commons/js/ztree/jquery.ztree.exedit-3.5.js"></script>
<%
	}
%>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<script type="text/javascript"
	src="jsp/appsecurity/testresult/js/tree.js"></script>

</head>


<script>
	$(function(){
		if(<%=testStatus%>!=0){
			//页面中所有的form控件不可用
			$("input").attr("disabled",'true');
			$("select").attr("disabled",'true');
			$("textarea").attr("disabled",'true');
			document.getElementById("bg").style.display ="block";
		}
	});
	
</script>
<script>
  $(document).ready(function(){
  	//alert(document.body.clientHeight);
  	var h=document.body.clientHeight;
  	$(".ztree").css("height",h-90+"px");
  	//alert($(".ztree").css("height"));
  });
  
  </script>

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

<body>
	<div id="bg"></div>
	<div id="table_title">
		<div>测试结果</div>
	</div>
	<div style="margin: 10px">
		<ul>
			<li><input type="button" id="exprotPdf" class="kc_btn"
				value="导出PDF" /></li>
		</ul>
	</div>
	<div style="margin: 0px 15px 10px 0px">
		<input type="hidden" id="type" value="2" />
		<div>
			<ul id="treeDemo" class="ztree"></ul>
		</div>
	</div>
	<form action="<%=request.getContextPath()%>/testresult/exprodExcel.do">
		<input type="hidden" name="id" id="id" /> <input type="hidden"
			name="type" id="type" />
	</form>
</body>
</html>
