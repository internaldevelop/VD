<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.wnt.web.testexecute.entry.*"%>
<%@ page import="org.wnt.core.ehcache.*"%>
<%@ page import="com.wnt.web.testexecute.controller.*"%>
<%@ page import="common.*"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="/commons/jsp/title.jsp" />
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/jsp/portscan/css/progressbar.css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/commons/js/kc.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/jsp/configuration/js/list4.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<%
	TestThread t=TestExecuteUtil.t;
	int testStatus=0;
	if(TestExecuteUtil.testEntry!=null){
		if(TestExecuteUtil.testEntry.getStatus()!=5){
			//测试案例运行中
			testStatus=1;
		}
	}
	
	if(application.getAttribute("startScan")!=null){
		//端口扫描运行中
		testStatus=2;
	}
	if(application.getAttribute("dynamicPortsStartScan")!=null){
		//端口扫描运行中
		testStatus=2;
	}
	if(application.getAttribute("configstartScan")!=null){
		//端口扫描运行中
		testStatus=3;
	}
%>
<script>
	$(function(){
		if(<%=testStatus%>==1){
			$("#btn_ctr").attr("disabled",'true');
		}else if(<%=testStatus%>==2){
			//页面中所有的form控件不可用
			$("#btn_ctr").attr("disabled",'true');
			//$("#btn_ctr").removeAttr("disabled");
		}else if(<%=testStatus%>==3){
			$("#btn_ctr").removeAttr("disabled");
		}
		
	});
	
	
</script>



<body class="main_body">
	<div class="index_body02">
		<!--top-->
		<div class="top_title02">
			<div class="top_title_img">
				<img src="<%=request.getContextPath() %>/images/right_icon01.png"
					width="14" height="16">
			</div>
			<div class="top_title_word">管理平台</div>
			<div class="top_title_word">&gt;</div>
			<div class="top_title_word">应用安全</div>
			<div class="top_title_word">&gt;</div>
			<div class="top_title_word">开始扫描</div>
		</div>

		<!--table-->
		<div class="main_table">
			<div class="table_title">
				<div>开始扫描</div>
				<div class="right_input02"></div>
			</div>
			<!--middle search-->
			<input type="hidden" value="${cscanport.configstartScan }"
				id="startScanFlag" />
			<div class="main_search">
				<form name="form1" id="form1" class="demoform" method="post"
					action="<%=request.getContextPath() %>/configuration/startScanData.do">
					<table width="100%" style="table-layout: fixed;" border=0>
						<tr>
							<td class="search_btn"><input type="submit" id="btn_ctr"
								value="<c:if test="${1==cscanport.configstartScan}" >停止扫描</c:if><c:if test="${1!=cscanport.configstartScan}" >开始扫描</c:if>" />
							</td>
							<td style="text-align: left;"><span id="msgdemo" name="sa"></span>
							</td>
						</tr>
						<tr>
							<td class="search_word">扫描进度:</td>
							<td colspan="4" class="search_input">

								<div class="progressbar_1">
									<div class="bar"></div>
								</div>
								<div class="progressNum"></div>
							</td>

						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>