<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include  page="/commons/jsp/title.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
<!--
	.table thead{background-color:#E1E1E1; border:1px solid #E1E1E1;}
	.table td{border:1px solid #474246;}
	.main_table table.table tfoot td{border:none;}
	.main_table table.table td button{padding:5px 15px;margin:2px;}
-->
</style>
</head>
<body class="main_body">
	<div class="index_body02">
		<!--top-->
		<div class="top_title02">
	        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
	        <div class="top_title_word">工控协议漏洞挖掘工具</div>
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
			<table cellpadding="0" cellspacing="0"  class="table">
				<thead>
					<tr>
						<th>选项名称</th>
						<th>基线值</th>
						<th>实际值</th>
					</tr>
				</thead>
				<tbody id="resultData">
					<tr>
						<td>
							选项名称1
						</td>
						<td>
							0x002
						</td>
						<td>
							0x002
						</td>
					</tr>
					<tr>
						<td>
							选项名称2
						</td>
						<td>
							0x003
						</td>
						<td>
							<span style="color:red;">0x001</span>
						</td>
					</tr>
					<tr>
						<td>
							选项名称3
						</td>
						<td>
							0x021
						</td>
						<td>
							<span style="color:red;">0x05</span>
						</td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="3">
							<button type="button" onclick="history.back()">返回</button>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</body>
</html>