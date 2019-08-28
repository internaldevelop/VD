<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/commons/css/newcss.css"
	rel="stylesheet" type="text/css" />
</head>
<body class="main_body">
	<div class="body_popupbox01"></div>
</body>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/commons/js/jquery-1.8.3.min.js"></script>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<script type="text/javascript">
	$(function(){
		var d = dialog({
	        title: '提示',
	        width: 300,
		    height: 80,
	        content: "${message}",
	        okValue: '确 定',
	        ok: function(){
	        	if("${url }"!="1"){
	        		window.location = "<%=request.getContextPath() %>/${url }";
	        	}
	        },
	        cancelValue:'关闭'
	    });
	    d.showModal();
	    if("${url }"!="1"){
	    	 setTimeout(function () {
	 	        d.close().remove();
	 	        window.location = "<%=request.getContextPath() %>/${url }";
	 	    }, 2000);
    	}
	});
</script>
</html>