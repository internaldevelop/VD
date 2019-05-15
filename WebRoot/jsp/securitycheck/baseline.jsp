<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include  page="/commons/jsp/title.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
<!--
	.main_table table.table{width:600px;margin-left:0px;margin-top:10px;}
	.main_table table.table td{border:none;padding:5px;}
	.main_table table.table td button{padding:5px 15px;margin:2px;}
-->
</style>
</head>
<body class="main_body">
	<div class="index_body02">
		<!--top-->
		<div class="top_title02">
	        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
	        <div class="top_title_word">工业控制系统安全测试及漏洞挖掘工具</div>
	        <div class="top_title_word">&gt;</div>
	        <div class="top_title_word">配置安全检查</div>
	        <div class="top_title_word">&gt;</div>
	        <div class="top_title_word">配置基线</div>
		</div>
		<!--table-->
		<div class="main_table">
			<div class="table_title">
				<div>配置基线</div>
			    <div class="right_input02">
			    </div>
			</div>
			<form action="<%=request.getContextPath() %>/securityCheck/save.do" method="post"  >
				<table cellpadding="0" cellspacing="0"  class="table" >
					<tbody id="baselineTbody">
						<c:forEach  var="bl" items="${list }">
							<tr>
								<td><label>选项名称：<input type="text" name="names" value="${bl.name }"/></label></td>
								<td><label>基线值：<input type="text" name="values"  value="${bl.value }" /></label></td>
								<td><a href="javascript:void(0);" class="del">删除</a></td>
							</tr>
						</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="3">
								<button type="button" id="add">添加</button>
								<button type="submit" id="save" >保存</button>
								<button type="button" id="backtrack">返回</button>
							</td>
						</tr>
					</tfoot>
				</table>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript">
<!--
	$(function(){
		$("#add").click(function(){
			$("#baselineTbody").append('<tr><td><label>选项名称：<input type="text" name="names" /></label></td><td><label>基线值：<input type="text" name="values"  /></label></td><td><a href="javascript:void(0);" class="del">删除</a></td></tr>');
		});
		
		$("#baselineTbody").on("click",".del",function(n){
			$(this).parents("tr").remove();
		});
		
		$("#backtrack").click(function(){
			location.href="<%=request.getContextPath() %>/securityCheck/config.do";
		});
	});
	
	
	
//-->
</script>
</html>