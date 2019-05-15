<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.wnt.web.testexecute.entry.*" %>
<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml">

<link  rel="stylesheet"  href="<%=request.getContextPath()%>/jsp/testexecute/css/chart.css" ></link>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/testresult/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath()%>/jsp/testresult/js/highstock.js"></script>
<script src="<%=request.getContextPath()%>/jsp/testresult/js/exporting.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/testexecute/js/chart.js"></script>

<script type="text/javascript">
$(function(){
	//离散 散点图
	container_4_2('<%=request.getContextPath()%>/chart/queryArpNew.do','<%=request.getContextPath()%>/chart/findAlarmLevel.do');
});

</script>

<body class="main_body">
<div class="chatDiv" id="4_2">
</div>
</body>
</html>