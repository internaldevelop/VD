<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=request.getContextPath()%>/">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>500 - 对不起，服务器内部错误！- 系统</title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/error/css/main.css">
</head>
<body>
	<div id="wrapper">
		<%-- <a class="logo" href="<%=request.getContextPath()%>"></a> --%>
		<div id="main">
			<div id="header">
				<h1>
					<span class="icon">!</span><span class="sub">Internal Server
						Error</span>
				</h1>
			</div>
			<div id="content">
				<h2>服务器内部错误！</h2>
				<p>当您看到这个页面,表示服务器内部错误,此网站可能遇到技术问题,无法执行您的请求,请稍后重试或联系站长进行处理,感谢您的支持!</p>
				<div class="utilities">
					<div class="input-container"
						style="font: 13px 'TeXGyreScholaRegular', Arial, sans-serif; color: #696969; text-shadow: 0 1px white; text-decoration: none;">
						<span id="totalSecond" style="color: red">5</span>秒后自动跳转…
					</div>
					<a class="button right" href="javascript:void(0);"
						onClick="history.go(-1);">返回...</a>
					<div class="clear"></div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/error/js/error.js"></script>
</html>