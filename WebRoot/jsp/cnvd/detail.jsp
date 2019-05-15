<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>   
<jsp:include  page="/commons/jsp/title.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
<!--
button{padding:0px;}
.main_table{text-align:center;}
.main_table > button > a{padding:5px 20px;text-decoration:none;display:block;}
.main_table table{margin-top:20px;}
.main_table td{text-align:left;border:1px solid #ffffff; padding:10px;background-color:#d9d9d9; }
.main_table .label{ width:100px;text-align:right;font-weight:bold;}
.main_table div.table_title div{text-align: left;}
caption{font-size:20px;font-weight:bolder;text-align:left;padding-left: 20px;}
-->
</style>
</head>
<body class="main_body">
	<div class="index_body02">
		<!--top-->
		<div class="top_title02">
	        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
	        <div class="top_title_word">管理平台</div>
	        <div class="top_title_word">&gt;</div>
	        <div class="top_title_word">漏洞库</div>
	        <div class="top_title_word">&gt;</div>
	        <div class="top_title_word">漏洞信息</div>
		</div>
		<!--table-->
		<div class="main_table">
			<div class="table_title">
				<div>漏洞信息</div>
			    <div class="right_input02">
			    </div>
			</div>
			<div style="clear:both;" ></div>
			<table>
				<caption>${cnvd.cnvdName }</caption>
				<tbody>
					<tr>
						<td class="label">CNVD-ID</td>
						<td>${cnvd.cnvdId }</td>
					</tr>
					<tr>
						<td class="label">发布时间</td>
						<td>${cnvd.releaseTime }</td>
					</tr>
					<tr >
						<td class="label">危害级别</td>
						<td>${cnvd.hazardLevel }</td>
					</tr>
					<tr>
						<td class="label">影响产品</td>
						<td>${cnvd.affectGoods }</td>
					</tr>
					<c:if test="${not empty cnvd.otherId }">
						<tr class="label">
							<td>其他ID</td>
							<td>${cnvd.otherId }</td>
						</tr>
					</c:if>
					<c:if test="${not empty cnvd.bugtraqId }">
						<tr >
							<td class="label">BUGTRAQ ID</td>
							<td>${cnvd.bugtraqId }</td>
						</tr>
					</c:if>
					<tr>
						<td class="label">CVEID</td>
						<td>${cnvd.cveId }</td>
					</tr>
					
					<tr>
						<td class="label">漏洞描述</td>
						<td>${cnvd.description }</td>
					</tr>
					<tr>
						<td class="label">参考链接</td>
						<td>${cnvd.referLink }</td>
					</tr>
					<tr>
						<td class="label">漏洞解决方案</td>
						<td>${cnvd.solution }</td>
					</tr>
					<tr>
						<td class="label">漏洞发现者</td>
						<td>${cnvd.finder }</td>
					</tr>
					<tr>
						<td class="label">厂商补丁</td>
						<td><c:if test="${empty cnvd.patch}">
								(无补丁信息)
							</c:if>
							<c:if test="${not empty cnvd.patch}">
								${cnvd.patch }
							</c:if>
						</td>
					</tr>
					<tr>
						<td class="label">验证信息</td>
						<td>
							<c:if test="${empty cnvd.verify }">
								(暂无验证信息)
							</c:if>
							<c:if test="${not empty cnvd.verify }">
								${cnvd.verify }
							</c:if>
						</td>
					</tr>
					<tr>
						<td class="label">报送时间</td>
						<td>${cnvd.reportTime}</td>
					</tr>
					<tr>
						<td class="label">收录时间</td>
						<td>${cnvd.recordTime}</td>
					</tr>
					<tr>
						<td class="label">更新时间</td>
						<td>${cnvd.renewTime}</td>
					</tr>
				</tbody>
			</table>
			<button><a href="<%=request.getContextPath()%>/cnvd/list.do"> 返 回 </a></button>	
			</div>
		</div>
	</div>
</body>
</html>