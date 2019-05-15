<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date" %>
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
request.setAttribute("now", sdf.format(new Date()));
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include  page="/commons/jsp/title.jsp"/>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
<!--
	td a img{vertical-align:middle;}
	.table thead{background-color:#E1E1E1; border:1px solid #E1E1E1;}
	.table td{border:1px solid #474246;}
-->
</style>
<body class="main_body">
	<div class="index_body02">
		<!--top-->
		<div class="top_title02">
	        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
	        <div class="top_title_word">工业控制系统安全测试及漏洞挖掘工具</div>
	        <div class="top_title_word">&gt;</div>
	        <div class="top_title_word">配置安全检查</div>
	        <div class="top_title_word">&gt;</div>
	        <div class="top_title_word">检查结果</div>
		</div>
		<!--table-->
		<div class="main_table">
			<div class="table_title">
				<div>检查结果</div>
			    <div class="right_input02">
			    	
			    </div>
			</div>
			<div class="main_search">
				<form name="page_form" id="page_form" class="demoform"  method="post" action="">
					<table border=0>
				     <tr>
				         <td width="60px" class="search_word">设备名称:</td>
				         <td width="260px" class="search_input">
				         	<input type='text' style="width:260px;" name='cnvdName' id='cnvdName' value='${cnvdName}' />
				         	<span class="Validform_checktip"></span>
				         </td>
			         	<td width="60px" class="search_word">设备IP:</td>
				         <td width="260px" class="search_input">
				         	<input type='text' style="width:260px;" name='IP' id='IP' value='' />
				         	<span class="Validform_checktip"></span>
				         </td>
				         
				         <td width="80px" class="search_btn">
				         	<button type="submit">查 询 </button>
				         </td>
				         <td></td>
				     </tr>
				    </table>
				</form>
			</div>
			<table cellpadding="0" cellspacing="0"  class="table">
				<thead>
					<tr>
						<th>序号</th>
						<th>设备名称</th>
						<th>时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="resultData">
					<tr>
						<td>1</td>
						<td>设备1</td>
						<td>
						<%
						out.print(sdf.format(new Date()));
						%>
						</td>
						<td>
							<a href="<%=request.getContextPath() %>/jsp/securitycheck/lookover.jsp">查看</a>
							<a href="javascript:void(0);" onclick="del(1,this)">删除</a>
						</td>
					</tr>
					<tr>
						<td>2</td>
						<td>设备2</td>
						<td>${now }</td>
						<td>
							<a href="<%=request.getContextPath() %>/jsp/securitycheck/lookover.jsp">查看</a>
							<a href="javascript:void(0);" onclick="del(2,this)">删除</a>
						</td>
					</tr>
				</tbody>
				<tfoot>
						<tr>
							<td colspan="4" class="table_bottom"><jsp:include
										page="/commons/jsp/page.jsp" /></td>
						</tr>
				</tfoot>
			</table>
		</div>
	</div>
</body>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<script type="text/javascript">
<!--
	function del(id,self){
		atrConfirm("您确定要删除吗?",function(){
			$(self).parents("tr").remove();
		});
	}
//-->
</script>
</html>