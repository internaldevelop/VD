<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>测试结果主页面</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript"
	src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
</head>
<frameset rows="30px,*" cols="*" frameborder="no" border="0"
	framespacing="0">
	<frame src="jsp/appsecurity/testresult/topFrame.jsp" name="topFrame"
		scrolling="No" noresize="noresize" />
	<frameset cols="380px,*" frameborder="no" border="0" framespacing="0">
		<frame src="jsp/appsecurity/testresult/leftFrame.jsp" name="leftFrame"
			scrolling="No" noresize="noresize" />
		<frame src="jsp/appsecurity/testresult/rightFrame.jsp" id="rightFrame"
			name="rightFrame" scrolling="No" noresize="noresize" />
	</frameset>
</frameset>
</html>