<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/commons/css/newcss.css"
	rel="stylesheet" type="text/css" />
<style type="text/css">
	.popupbox_title {
		background: none;
		margin: 0px;
		font-size: 12px;
		color: #840203;
		font-weight: bold;
		text-align: left;
	}
	.popupbox_concent {
		background: none;
		margin: 0px;
		font-size: 12px;
		color: #333;
	}
</style>
</head>
<body class="main_body">
	<div class="body_popupbox01"></div>
</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/commons/js/jquery-1.8.3.min.js"></script>
<jsp:include page="/commons/jsp/atrDialog.jsp" />

<script type="text/javascript">
function JumpUrl(){
	window.history.go(-1);
};
var d = dialog({
        title: '提示',
        width: 300,
        content: "<div class='popupbox_title'>${message}</div>",
        onclose:JumpUrl,
        cancelValue:'关闭'
   	});
	d.showModal();


</script>
</html>