<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*" %>
<%@ page import="org.wnt.core.ehcache.*" %>
<%@ page import="com.wnt.web.testexecute.controller.*" %>
<%@ page import="common.*" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0"> 
<jsp:include  page="/commons/jsp/title.jsp"/>
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/jsp/environment/css/environment.css" ></link>
<script type="text/javascript" src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/kc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/environment/js/list.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>

<script>
	function doNext(){
		var name=$("#name").val();
		var type=$("#type").val();
		var notes=$("#notes").val();
		var index=$("#index").val();
		window.location.href="<%=request.getContextPath()%>/configuration/querydata.do?name=" + name +"&index="+index+"&type="+type+"&notes="+notes ;
	}
	
</script>
</script>
<body class="main_body">
<div class="index_body02">
<!--top-->
<div class="top_title02">
   <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
   <div class="top_title_word">管理平台</div>
   <div class="top_title_word">&gt;</div>
   <div class="top_title_word">应用安全</div>
   <div class="top_title_word">&gt;</div>
   <div class="top_title_word">创建任务</div>
</div>

	<!--table-->
	<div class="main_table">
		<div class="table_title">
			<div>创建任务</div>
		    <div class="right_input02">
		    </div>
		</div>
	<!--middle search-->
	<div class="main_search">
	<form name="form1" id="form1" action="<%=request.getContextPath() %>/configuration/querydata.do" class="demoform">
	<input type='hidden' name='index' id='index' value='2' />
		<table width="100%" style="table-layout:fixed;" border=0>
	        <tr>
	            <td style="width:120px;" class="search_word">任务名称:</td>
	            <td style="width:300px;" class="search_input">
	            	<input type='text' name='name' id='name' value='${data.name}' datatype="equipmentName" errormsg="请输入合法的名称！"/>
	           		<span class="Validform_checktip"></span>
	            </td>
	        </tr>
	        <tr>
	            <td style="width:120px;" class="search_word">扫描类型:</td>
	            <td style="width:300px;" class="search_input">
	            	<select name="type" id="type">
						<option value="0" <c:if test="${0==data.type}" >selected="selected"</c:if>>快速扫描</option>
						<option value="1" <c:if test="${1==data.type}" >selected="selected"</c:if>>深度扫描</option>
					</select>
	            </td>
	       </tr>
	       <tr>
	            <td style="width:120px;" class="search_word">注释:</td>
	            <td style="width:300px;" class="search_input">
	            	<TextArea name='notes' id='notes' maxlength="512" class="env_area" >${data.notes }</TextArea>
	            </td>
	        </tr>
        </table>
		<div  class="button_div">
     	<table cellpadding="0" cellspacing="0">
            <tr>
                <td><input type="button" onclick="javascript:window.history.go(-1);return false;" value="上一步"/></td>
                <td><input type="button" value="下一步" onclick="doNext()"/></td>
            </tr>
        </table>
    	</div>
	</form>
	
	</div>
	

	
</div>
</div>
</body>
</html>