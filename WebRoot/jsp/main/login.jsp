<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="org.wnt.core.ehcache.*" %>
<%@ page import="org.wnt.core.uitl.*" %>
<%@ page import="java.security.SecureRandom" %>
<%@ page import="common.*" %>
<%@ page import="com.wnt.web.testexecute.controller.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="com.wnt.web.login.contorller.LoginContorller.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
 <html xmlns="http://www.w3.org/1999/xhtml"> 

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Eedge" />  
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="0">
<title>工业控制系统安全测试及漏洞挖掘工具</title>
<link href="<%=request.getContextPath() %>/commons/css/newcss.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/jquery.js"></script>
</head>
<jsp:include  page="/commons/jsp/title.jsp"/>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<style>
.bbs a{color: white;}
body{min-height:600px;min-width:1200px;}

.tabletd{
	 
}

</style>
<body>
<%
	session.removeAttribute("userName");
	SecureRandom random = new SecureRandom();
	byte keyBytes[] = new byte[8];
	random.nextBytes(keyBytes);
	session.setAttribute("key", PasswordUtil.bytesToHexString(keyBytes));
%>
<body class="login_body">
	<form action="index()" method="post" onsubmit=" return check();">
		<div class="login_div" id="logindiv" name="logindiv">
			<div class="login_sysname"></div>
			<table cellpadding="0" cellspacing="0">
				<tr>
					<td class="login_title">用户名：</td>
					<td>
						<input type="text" name="userName" id="userName" />
					</td>
				</tr>
				<tr>
					<td class="login_title">密码：</td>
					<td>
						<input type="password" class="alltxt" name="password" id="userPassword"/>
						<input type="hidden" name="key" id="key" value="${sessionScope.key}"/>
					</td>
				</tr>

				<tr>
					<td colspan=2 class="login_td">
						<input type="button" class="login_button" name="login" id="loginButton" value="登录" />
					</td>
				</tr>
			</table>
		</div>
	</form>

	<jsp:include page="/commons/jsp/encrypt.jsp" />
	<script type="text/javascript">
		$("#loginButton").click(function(){
			var name = $("#userName").val();
			var password =  $("#userPassword").val();
			name = $.trim(name);
			if(name == null || name.length == 0){
				atrshowmodal("请输入用户名！");
				return;
			}
			password = $.trim(password);
			if(password == null || password.length == 0){
				atrshowmodal("请输入密码！");
				return;
			}
			var key = $("#key").val();
			name = encrypt(name, key);
			password = encrypt(password, key);
			// alert("key=bbb9bfcd5aa395c8; name=" + name + "; password=" + password);
			
			$.ajax({
				type: "POST",
				url: "/VD/loginContorller/login.do",
				data: {"name" : name , "password" : password, "key": key},
				dataType :"json",
				cache:false,
				success: function(data){
					if(data.success=="yes"){
						location.href = "/VD/jsp/main/index.jsp";
					}else {
						 atrshowmodal(data.info,null,function(){
				              location.reload(true);
				          });
					}
				}
			});
		});
		if (window != top) 
			top.location.href = location.href; 
	</script>
</div>
</body>
</html>
