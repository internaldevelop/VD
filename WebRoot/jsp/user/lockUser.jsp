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
<title>工业控制系统安全测试及漏洞挖掘工具</title>
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

<body class="main_body">
<div class="index_body02">
    <!--top-->
    <div class="top_title02">
            <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
            <div class="top_title_word">工业控制系统安全测试及漏洞挖掘工具</div>
            <div class="top_title_word">&gt;</div>
            <div class="top_title_word">用户管理</div>
            <div class="top_title_word">&gt;</div>
            <div class="top_title_word">锁定配置</div>        
    </div>

    <!--table-->
    <div class="main_table"> 
    <div class="table_title">
        <div>锁定配置</div>
        <div class="search_input">
    
        </div>
    </div>
</div>

    <div class="main_table">
        <div class="main_search">
            <form id = "formid222"  class="demoform">              
                <table id="" >                    
                    <tr>
                        <td style="width:90px;"  class="search_word" align="right">登录错误次数：</td>
                        <td style="width:290px;" align="left">
                           <input type="text" id="errorcount" name=""errorcount"" value=${errorCount} maxlength="2" onkeyup="this.value=this.value.replace(/\D/g,'')">（3-10次）</input>
                        </td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td style="width:90px;"  class="search_word" align="right">锁定时间：</td>
                        <td style="width:290px;"  align="left">
                            <input type="text" id="locktime" name="locktime" value=${lockTime} maxlength="2" onkeyup="this.value=this.value.replace(/\D/g,'')">（3-30分钟）</input>
                        </td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td  colspan="2"  >
                            <input id="confirmButton" value="确认修改" type="button" onclick="send()" />
                        </td>
                    </tr>
                </table>                                     
            </form>
        </div>
    </div>
    
<script type="text/javascript">

	$(function(){	
	    var userName="<%=session.getAttribute("userName")%>"; 
	    if(userName == "audit"){
	        $("#confirmButton").attr("disabled","disabled");
	    }else{
	        $("#confirmButton").removeAttr("disabled");
	    }
	    
	});
	
	
   function send(){
	   var errorCount = $('#errorcount').val();
       var lockTime = $('#locktime').val();
       var value = Number(errorCount);
       if(value < 3 || value > 10){
    	   atrshowmodal('登录错误次数取值范围：3 -- 10!');
    	   return;
       }
       value = Number(lockTime);
       if(value < 3 || value > 30){
           atrshowmodal('锁定时间取值范围：3 -- 30!');
           return;
       }
       $.ajax({
           async : false,
           cache : false,
           type : 'POST',
           data : {
               "errorCount" : errorCount,
               "lockTime" : lockTime
           },
           dataType : 'json',
           url : "<%=request.getContextPath() %>/loginContorller/updateLockConfig.do",
           success : function(data) {
               atrshowmodal(data.info,null,function(){
                   location.reload(true);
               });
           }
       });
   }

</script>
</body>
</html>