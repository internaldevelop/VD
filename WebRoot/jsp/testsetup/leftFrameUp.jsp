<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*"%>
<%@ page import="org.wnt.core.ehcache.*"%>
<%@ page import="com.wnt.web.testexecute.controller.*"%>
<%@ page import="com.wnt.web.protocol.ProtocolController.*"%>
<%@ page import="common.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>测试用例库</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="jsp/testsetup/css/demo.css" type="text/css">
<link rel="stylesheet"
	href="jsp/testsetup/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/jsp/testsetup/css/testsetup.css"></link>

<script type="text/javascript" src="commons/js/jquery.js"></script>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<script type="text/javascript"
	src="jsp/testsetup/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="jsp/testsetup/js/tree.js"></script>

</head>

<%
	int testStatus=0;
	if(TestExecuteUtil.testEntry!=null){
		if(TestExecuteUtil.testEntry.getStatus()!=5 ){
			//测试案例运行中
			testStatus=1;
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
	}
%>
<script>
	var testStatus=<%=testStatus%>;
	var userName="<%=session.getAttribute("userName")%>"; 
	$(function(){
		if(<%=testStatus%>!=0){
			//页面中所有的form控件不可用
			$("input").attr("disabled",'true');
			$("select").attr("disabled",'true');
			$("textarea").attr("disabled",'true');
		}
		
		
        if(userName == "audit"){
            $("#leftTreeButton").attr("disabled","disabled");   
        }else{
        	$("#leftTreeButton").removeAttr("disabled");
        }
	});
	
</script>
<script>
  $(document).ready(function(){
  	//alert(document.body.clientHeight);
  	var h=document.body.clientHeight;
	$(".ztree").css("height",h-110+"px");
  	/* if(h==295){
  		$(".ztree").css("height",h-85+"px");
  	}else if(h==343){
  		$(".ztree").css("height",h-123+"px");
  	}else {
  		$(".ztree").css("height",h-133+"px");
  	} */
  	
  	//alert($(".ztree").css("height"));
  });
  
  </script>
<body>
	<div id="table_title">
		<div>测试用例库</div>
	</div>
	<div class="button_div"
		style="float: right; margin-right: 5px; margin-top: 3px;">
		<input type="button" id="leftTreeButton" onclick="onCheck() "
			value="添加" class="kc_btn" style="margin-right: 2px;" />
	</div>
	<div>
		<br />
		<br /> <input type="hidden" id="type" value="1" />
		<ul id="treeDemo" class="ztree"></ul>
	</div>
</body>
</html>
