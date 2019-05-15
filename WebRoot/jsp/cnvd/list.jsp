<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include  page="/commons/jsp/title.jsp"/>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
<!--
	button.plus{background:none;background-image:url("<%=request.getContextPath() %>/images/plus.png");background-repeat:no-repeat;cursor:pointer; padding-left:20px;border:none;color:#474246;}
	button.plus:hover{color:#36F;}
	td a img{vertical-align:middle;}
	.search_input input{ padding:0px 2px;}
	.main_table .table tbody td{padding:5px;}
-->
</style>
<script type="text/javascript" src="${basePath }/jsp/cnvd/js/list.js"></script>
<body class="main_body">
	<div class="index_body02">
		<!--top-->
		<div class="top_title02">
	        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
	        <div class="top_title_word">工业控制系统安全测试及漏洞挖掘工具</div>
	        <div class="top_title_word">&gt;</div>
	        <div class="top_title_word">漏洞库</div>
	        <div class="top_title_word">&gt;</div>
	        <div class="top_title_word">漏洞列表</div>
		</div>
		<!--table-->
		<div class="main_table">
			<div class="table_title">
				<div>漏洞列表</div>
			    <div class="right_input02">
			    	<button class="plus" >添加</button>
			    </div>
			</div>
			<div class="main_search">
				<form name="page_form" id="page_form" class="demoform"  method="post" action="<%=request.getContextPath() %>/cnvd/list.do">
					<table border=0>
				     <tr>
				     	 <td width="60px" class="search_word">CNVD-ID:</td>
				         <td width="260px" class="search_input">
				         	<input type='text' style="width:260px;" name='cnvdId' id='cnvdId' value='${cnvdId}' />
				         	<span class="Validform_checktip"></span>
				         </td>
				         <td width="60px" class="search_word">漏洞名称:</td>
				         <td width="260px" class="search_input">
				         	<input type='text' style="width:260px;" name='cnvdName' id='cnvdName' value='${cnvdName}' />
				         	<span class="Validform_checktip"></span>
				         </td>
				         <td width="80px" class="search_btn">
				         	<button type="submit"> 查 询 </button>
				         </td>
				         <td></td>
				     </tr>
				    </table>
				</form>
			</div>
			<table cellpadding="0" cellspacing="0"  class="table">
				<thead>
					<tr class="table_title02">
						<th width="3%">序号</th>
						<th width="10%">CNVD-ID</th>
						<th width="20%">漏洞名称</th>
						<th width="40%" >描述</th>
						<th width="15%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="cnvd" items="${cnvds }" varStatus="index">
						<tr >
							<td>${index.count+(wntPage.page_size*(wntPage.current_page-1))}</td>
							<td>
								${cnvd.cnvdId }
							</td>
							<td>
								${cnvd.cnvdName}
							</td>
							<td style="text-align:left;">
								${cnvd.description }
							</td>
							<td>
								<a href="<%=request.getContextPath() %>/cnvd/${cnvd.id}.do">
									<img src="${basePath }/images/search.png" width="16" height="16" border="0" />
									查看
								</a>
								<a  href="javascript:edit(${cnvd.id});">
									<img src="${basePath }/images/Edit.png" width="16" height="16" border="0" />
									修改
								</a>
								<a href="javascript:del(${cnvd.id},'${cnvd.cnvdId }');">
									<img src="${basePath }/images/Delete.png" width="16" height="16" border="0" />
									删除
								</a>
							</td>
						</tr>
					</c:forEach>
					</tbody>
					<tfoot>
						<tr>
							<td colspan="5" class="table_bottom"><jsp:include
										page="/commons/jsp/page.jsp" /></td>
						</tr>
					</tfoot>
			</table>
		</div>
	</div>
</body>
</html>