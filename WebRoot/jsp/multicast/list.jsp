<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*"%>
<%@ page import="org.wnt.core.ehcache.*"%>
<%@ page import="com.wnt.web.testexecute.controller.*"%>
<%@ page import="com.wnt.web.protocol.ProtocolController.*"%>
<%@ page import="common.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="/commons/jsp/title.jsp" />
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/jsp/environment/css/environment.css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/jsp/multicast/js/list.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<style>
.Validform_checktIp {
	top: 7px;
	right: 85px;
	display: block;
	width: 100px;
	line-height: 20px;
	height: 20px;
	overflow: hidden;
	color: #999;
	font-size: 12px;
	position: absolute;
}

.add_btn {
	position: relative;
}
</style>
<%
	int testStatus=0;
	if(TestExecuteUtil.testEntry!=null){
		if(TestExecuteUtil.testEntry.getStatus()!=5 ){
			//测试案例运行中
			testStatus=1;
		}
	}
		if(application.getAttribute("startScan")!=null){
			//端口扫描运行中
			testStatus=2;
		}
		if(application.getAttribute("configstartScan")!=null){
			//应用安全扫描中
			testStatus = 2;
		}
		if(application.getAttribute("dynamicPortsStartScan")!=null){
			//动态端口扫描中
			testStatus = 2;
		}
		if(application.getAttribute("startIdentity")!=null){
			//协议识别
			testStatus = 2;
		}
%>
<script>
    var userName="<%=session.getAttribute("userName")%>"; 
	$(function(){
		
		if(<%=testStatus%>!=0){
			//页面中所有的form控件不可用
			$("input").attr("disabled",'true');
			$("select").attr("disabled",'true');
			$("textarea").attr("disabled",'true');
			//$("a").attr("disabled",'true');
			$("a").removeAttr("onclick");
			interval_progressScan(0);
		}
		
        if(userName == "audit"){
            $("#btn_ctr").attr("disabled","disabled");
            $("a").removeAttr("onclick");
        }else{
        	$("#btn_ctr").removeAttr("disabled");
        }
	});
	//定时取得端口扫描进度
	function interval_progressScan(t){
		//开启进度条控制的定时器
		scanTimeout=window.setTimeout(function(){
			$.ajax({
				type: "POST",
				url: "/VD/portscan/getProgress.do",
				data:{time:t},
				dataType :"json",
				success: function(data){
					if(data.status=="n"){
						window.location.reload();
					}else{
						interval_progressScan(data.time);
					}
					//window.parent.sendConnect();
					//window.parent.alertStartScan = function alertStartScan(flag) {

					//}
				}
			}); 
		},10000);			
	}

</script>


<body class="main_body">
	<div class="index_body02">
		<!--top-->
		<div class="top_title02">
			<div class="top_title_img">
				<img src="<%=request.getContextPath() %>/images/right_icon01.png"
					width="14" height="16">
			</div>
			<div class="top_title_word">通信规约模糊测试工具</div>
			<div class="top_title_word">&gt;</div>
			<div class="top_title_word">扫描设置</div>
			<div class="top_title_word">&gt;</div>
			<div class="top_title_word">多播地址</div>
		</div>

		<!--table-->
		<div class="main_table">
			<div class="table_title">
				<div>多播地址</div>
				<div class="right_input02"></div>
			</div>
			<div class="main_search">
				<form name="multi_form" id="multi_form" class="demoform"
					method="post"
					action="<%=request.getContextPath() %>/multicast/createMulticast.do">
					<table width="100%" style="table-layout: fixed;" border=0>
						<tr>
							<td style="width: 60px;" class="search_word">添加地址:</td>
							<td style="width: 290px;" class="search_input"><input
								type='text' name='ipAddr' id='ipAddr' value='${param.ipAddr }'
								datatype="isBroadcastIp"
								errormsg="请输入224.0.0.0到239.255.255.255IP地址！" /> <span
								class="Validform_checktip"></span></td>
							<td class="search_btn add_btn"><input type="submit"
								id="btn_ctr" value="保存" /> <span id="msg"
								class="Validform_checktIp"></span></td>
						</tr>
					</table>
				</form>
			</div>
			<!--middle search-->
			<div class="fix_table" style="float: left;">
				<table id="scanTable" cellpadding="0" cellspacing="0" class="table">
					<tr class="table_title02">
						<td width="">地址</td>
						<td width="">来源</td>
						<td width="">操作</td>

					</tr>
					<c:forEach var="multi" items="${multiCastsList}" varStatus="index">
						<c:if test="${multi.ipAddr == ''}">
							<tr style="display: none;">
								<td>${multi.ipAddr}</td>
								<td>${multi.source}</td>
								<td><a href="#" onclick="createMulticast('${multi.id}')">删除</a></td>
							</tr>
						</c:if>
						<c:if test="${multi.ipAddr != ''}">
							<tr>
								<td>${multi.ipAddr}</td>
								<td>${multi.source}</td>
								<td><a href="#" onclick="createMulticast('${multi.id}')">删除</a></td>
							</tr>
						</c:if>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>
</body>
</html>