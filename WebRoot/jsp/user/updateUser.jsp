<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ page import="org.wnt.core.ehcache.*" %>
<%@ page import="common.*" %>
<%@ page import="org.wnt.core.uitl.*" %>
<%@ page import="java.security.SecureRandom" %>
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
<title>工控协议漏洞挖掘工具</title>
<link href="<%=request.getContextPath() %>/commons/css/newcss.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/jquery.js"></script>
</head>
<jsp:include  page="/commons/jsp/title.jsp"/>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<style>

body {
	min-height: 600px;
	min-width: 1200px;
	background-color: #ffffff
}
</style>
<%
SecureRandom random = new SecureRandom();
byte keyBytes[] = new byte[8];
random.nextBytes(keyBytes);
session.setAttribute("key", PasswordUtil.bytesToHexString(keyBytes));
%>

<body class="main_body">
<div class="index_body02">
	<!--top-->
	<div class="top_title02">
	        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
	        <div class="top_title_word">工控协议漏洞挖掘工具</div>
	        <div class="top_title_word">&gt;</div>
	        <div class="top_title_word">用户管理</div>
	        <div class="top_title_word">&gt;</div>
	        <div class="top_title_word">修改密码</div>        
	</div>

	<!--table-->
	<div class="main_table"> 
	<div class="table_title">
		<div>修改密码</div>
	    <div class="search_input">
	
	    </div>
	</div>
</div>

	<div class="main_table">
		<div class="main_search">
			<form id = "formid222"  class="demoform">
				<table id="" >
					<tr>
						<td style="width:60px;"  class="search_word"align="right">用户名：</td>
						<td style="width:290px;" align="left">
							${name }
						</td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td style="width:60px;"  class="search_word" align="right">当前密码：</td>
						<td style="width:290px;" align="left">
							<input id = "oldpwd" type="password" name="password"  />
						</td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td style="width:60px;"  class="search_word" align="right">新密码：</td>
						<td style="width:290px;" align="left">
							<input id = "password" type="password" name="password"  />
						</td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td style="width:60px;"  class="search_word" align="right">确认密码：</td>
						<td style="width:290px;"  align="left">
							<input id = "newpwd2" type="password" name="password"  />
							<input type="hidden" name="key" id="key" value="${sessionScope.key}"/>
						</td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td  colspan="2"  >
							<input id="loginButton" value="确认修改" type="button" onclick="go()" />
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
<jsp:include page="/commons/jsp/encrypt.jsp" />
<script type="text/javascript">
function go() {
	var flag_pass = checkPassword();
	if (flag_pass) {
		var password = $('#password').val();
		var password_confirm = $('#newpwd2').val();
		if (password === password_confirm) {
			gogo();
		} else {
			atrshowmodal("两次输入密码不一致",null,function(){
				$('#password').focus();
			});
		}
	}
}
function checkPassword() {
	var oldpwd = $('#oldpwd').val();
	var password = $('#password').val();
	var pattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[#@!~%^&*])[0-9a-zA-Z\\d#@!~%^&*]{8,16}$/;
//				  /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[#@!~%^&*])[0-9a-zA-Z\\d#@!~%^&*]{8,16}i

    var userName = "<%=request.getSession().getAttribute("userName")%>";
   
	if (oldpwd) {
	    
		if (oldpwd.length < 8 || oldpwd.length > 16 || !pattern.test(oldpwd)) {
			atrshowmodal("密码必须是大小写字母，数字，特殊字符(#@!~%^&*)组合，且长度不小于8位，不大于16位",null,function(){
				$('#oldpwd').focus();
			});
			return false;
		} else {
			if (password) {
				if (password.length < 8 || password.length > 16
						|| !pattern.test(password) || password.toUpperCase().indexOf(userName.toUpperCase()) != -1) {
					atrshowmodal("密码不能包含用户名，且密码必须是大小写字母，数字，特殊字符(#@!~%^&*)组合，且长度不小于8位，不大于16位",null,function(){
						$('#password').focus();
					});
					return false;
				} else if(oldpwd == password){
					atrshowmodal("新密码不能和旧密码相同！请重新设置新密码！",null,function(){
						document.getElementById("newpwd2").value = "";
						$('#password').val("").focus();
					});
					return false;
				}else{
					return true;
				}
			} else {
				atrshowmodal("请输入新密码",null,function(){
					$('#password').focus();
				});
				return false;
			}
		}
	} else {
		atrshowmodal("请输入原始密码",null,function(){
			$('#oldpwd').focus();
		});
		return false;
	}
}
function gogo() {
	var newpwd = $("#password").val();
	var oldPassword = $("#oldpwd").val();
	var key = $("#key").val();
	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		data : {
			"oldPassword" : encrypt(oldPassword, key),
			"password" : encrypt(newpwd, key),
			"key": key
		},
		dataType : 'json',
		url : "<%=request.getContextPath() %>/loginContorller/updateUserPassword.do",
		success : function(data) {
		    atrshowmodal(data.info,null,function(){
	            location.reload(true);
	        });
		}
	});
	return false;
}

</script>
</body>
</html>
