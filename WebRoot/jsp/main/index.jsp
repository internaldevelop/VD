<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.wnt.core.ehcache.*"%>
<%@ page import="common.*"%>
<%@ page import="com.wnt.web.testexecute.controller.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Eedge" />
<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Expires" content="0">
			<jsp:include page="/commons/jsp/socketio.jsp" />
			<%-- <link rel="shortcut icon" href="<%=request.getContextPath() %>/images/LOGO.ico" type="image/x-icon" /> --%>
			<title>通信规约模糊测试工具</title>
			<link href="<%=request.getContextPath() %>/commons/css/newcss.css"
				rel="stylesheet" type="text/css" />
			<script type="text/javascript"
				src="<%=request.getContextPath() %>/commons/js/jquery.js"></script>
			<script type="text/javascript">
	function checkLeave(event){
		sendDisconnect();
	}
</script>
</head>


<style>
.bbs a {
	color: white;
}

body {
	min-height: 600px;
	min-width: 1200px;
}
</style>
<body onbeforeunload="checkLeave(event)">
	<div class="index_body">
		<!--top-->
		<div class="top">
			<div class="top_div">
				<div class="logo"></div>
				<div class="logo_name"></div>
				<div class="right">
					<div class="right_top">
						<div class="welcome">
							<ul>

								<li>${userName }&nbsp;您好,欢迎使用&nbsp;&nbsp;</li>
								<li class="bbs"><a href="/VD/jsp/main/login.jsp"
									onclick="quit();">注销</a></li>
								<li class="welcome_line">|</li>
								<li class="bbs"><a href="#" onclick="showAbout();">关于</a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!--left-->
		<div class="left" style="overflow: hidden;" id="col1">
			<iframe src="<%=request.getContextPath() %>/menu.do" frameborder="0"
				height="100%" width="100%" style="float: left;"></iframe>
		</div>
		<div class="div_line" style="float: left;" id="col2"></div>

		<!--right-->
		<div id="col3" class="main">
			<%
	//TestThread t=(TestThread)EHCacheUtil.get("thread");
	TestThread t=TestExecuteUtil.t;
	
	if(TestExecuteUtil.testEntry!=null && TestExecuteUtil.testEntry.getStatus()!=5 ){
	%>
			<iframe src="<%=request.getContextPath() %>/testexecute/execute.do"
				name="mainFrame" frameborder="0" height="100%" width="100%"
				style="float: left;"></iframe>
			<%}else{ %>
			<iframe src="<%=request.getContextPath() %>/environment/create.do"
				name="mainFrame" frameborder="0" height="100%" width="100%"
				style="float: left;"></iframe>
			<%} %>
		</div>

		<!--copyright-->
		<!-- <div class="copyright"> -->
		<!-- 	版权所有©北京威努特技术有限公司 -->
		<!-- </div> -->
		<input type="hidden" value="1" id="closeflag" />
		<script type="text/javascript">
$(function(){
	function autoResize(){
		var wh = $(window).height();
		if(wh<600){
			wh = 600;
		}
		var h = (wh-97)+"px";
		document.getElementById("col1").style.height=h;
		document.getElementById("col2").style.height=h;
		document.getElementById("col3").style.height=h;
	}
	autoResize();
	$(window).resize(autoResize);
	
});


function showAbout(){
	if(document.getElementById("about").style.display == ""){
		document.getElementById("about").style.display = "none";
	}else{
		document.getElementById("about").style.display = "";
	}
}
function hiddenAbout(){
		document.getElementById("about").style.display = "none";
}

function quit() {
<%-- 	location.href = "<%=request.getContextPath() %>/loginContorller/quit.do"; --%>
	$.ajax({
			type: "POST",
			url: "<%=request.getContextPath() %>/loginContorller/quit.do",
			dataType :"json",
			async :true,
			cache:false,
			success: function(data){
			}
	});
}

</script>
	</div>
	<div class="popupbox01" id="about" style="display: none;">
		<div class="popupbox_div">
			<div class="bottom_list">
				<div class="table_title">
					<div>关于</div>
					<div class="right_input">
						<ul>
							<li class="checkbox_input"><img
								src="<%=request.getContextPath() %>/images/43.png" width="16"
								height="16" onclick="hiddenAbout();" /></li>
						</ul>
					</div>
				</div>
			</div>
			<div class="concet_div concet_mar">
				产品版本:
				<%=EHCacheUtil.get("VERSION")==null?"":EHCacheUtil.get("VERSION")%><br />产品名称:
				通信规约模糊测试工具<br />产品型号：IVM6000<br />
			</div>
			<%--  new PropertiesUtil("sysConfig.properties").readProperty("version")--%>
			<div class="popupbox_copyright">
				<%--版权所有©北京威努特技术有限公司<br />--%>
				2015-2020。保留一切权利
			</div>
		</div>
	</div>
</body>
</html>
