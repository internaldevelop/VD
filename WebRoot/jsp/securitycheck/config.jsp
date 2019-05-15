<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
request.setAttribute("now", sdf.format(new Date()));
%>
<jsp:include  page="/commons/jsp/title.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/jsp/portscan/css/progressbar.css" ></link>
<style>
<!--
	form .form-item{padding:5px;margin:5px;display:inline-block;width:282px; font-size:12px;}
	form .form-item label {width:100px;display:inline-block;text-align:right;}
	form.vertical .form-item{display:block;width:100%;}
	.main_table div.table_title div.right_input02 {width:100px;}
	.main_table div.table_title div.right_input02 button{cursor:pointer;}
	.table thead{background-color:#E1E1E1; border:1px solid #E1E1E1;}
	.table td{border:1px solid #474246;}
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
	        <div class="top_title_word">检查配置</div>
		</div>
		<!--table-->
		<div class="main_table">
			<div class="table_title">
				<div>检查配置</div>
			    <div class="right_input02">
			    	<button class="plus" >配置基线</button>
			    </div>
			</div>
			<div class="main_search">
				<form action="" >
					<div class="form-item">
						<label>被测设备IP：</label>
						<input type="text" name="" value="${ip }" readonly="readonly"/>
					</div>
					<div class="form-item">
						<label>设备名称：</label>
						<input type="text" name="" value="${name }"/>
					</div>
					<div class="form-item" style="text-align:right;">
						<button type="button">开始检测/停止检测</button>
					</div>
				</form>
				<table>
					<tr>
						<td style="width:90px;text-align:right;">进程：</td>
						<td style="padding-left: 10px;">
							<div class="progressbar_1"> 
						        <div class="bar"></div> 
						    </div> 
						    <div class="progressNum"></div>
						</td>
					</tr>
				</table>
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
							<td>${name }</td>
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
							<td>${name }</td>
							<td>${now }</td>
							<td>
								<a href="<%=request.getContextPath() %>/jsp/securitycheck/lookover.jsp">查看</a>
								<a href="javascript:void(0);" onclick="del(2,this)">删除</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<script type="text/javascript">
<!--
	$(function(){
		$(".plus").click(function(){
			location.href = "<%=request.getContextPath() %>/securityCheck/baseline.do";
		});
	});
	
	function del(id,self){
		atrConfirm("您确定要删除吗?",function(){
			$(self).parents("tr").remove();
		});
	}
//-->
</script>
</html>