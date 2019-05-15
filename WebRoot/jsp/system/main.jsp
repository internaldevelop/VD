<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*" %>
<%@ page import="org.wnt.core.ehcache.*" %>
<%@ page import="com.wnt.web.testexecute.controller.*" %>
<%@ page import="com.wnt.web.protocol.ProtocolController.*" %> 
<%@ page import="common.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<jsp:include page="/commons/jsp/title.jsp" />
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/jsp/system/css/main.css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/commons/js/kc.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/jsp/system/js/main.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<style type="text/css">
	.main_table  table.table{width:100%;}
	.main_table table.table td{border:1px solid;padding:10px 10px;background-color:#f0f0f0;} 
	td img{vertical-align:middle;}
	#btcsShutdown,#btcsReboot,#btbcReboot{cursor:pointer;}
	#btcsReboot2{color:#71b83d;}
	.box{margin:80px auto;background-color:#f6f6f6;width:350px;border:1px solid;border-radius:10px;}
	.box .header{background-color:#e5e5e5; border-radius:10px 10px 0px 0px; border-bottom:1px solid;text-align:center;height:30px;line-height:30px;}
	.box .content{height:150px;text-align:center;}
	.box .content .demoform{margin-top:20px;}
	.box label{font-size:12px;}
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
			testStatus = 1;
		}
		if(application.getAttribute("dynamicPortsStartScan")!=null){
			//动态端口扫描中
			testStatus = 1;
		}
		if(application.getAttribute("startIdentity")!=null){
			//动态端口扫描中
			testStatus = 2;
		}
	
%>
<script>
$(function(){
		if(<%=testStatus%>!=0){
			//页面中所有的form控件不可用
				$("input").prop("disabled",true);
				$("a").prop("disabled",true); 
				$("#btcsShutdown").unbind();
				$("#btcsReboot").unbind();
				$("#btcsReboot2").unbind();
				$("#btbcReboot").unbind();
				interval_progressScan(0);
				
		}
		
		var userName="<%=session.getAttribute("userName")%>"; 
        if(userName == "audit"){
            $("#config").attr("disabled","disabled");
            $("#btcsShutdown").unbind();
            $("#btcsReboot").unbind();
            $("#btcsReboot2").unbind();
            $("#btbcReboot").unbind();
        }else{
            $("#config").removeAttr("disabled");
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

</head>

<body class="main_body">
	<!--top-->
	<div class="top_title02">
		<div class="top_title_img">
			<img src="<%=request.getContextPath()%>/images/right_icon01.png"
				width="14" height="16">
		</div>
		<div class="top_title_word">工控协议模糊测试工具</div>
		<div class="top_title_word">&gt;</div>
		<div class="top_title_word">系统配置</div>
		<div class="top_title_word">&gt;</div>
		<div class="top_title_word">系统配置</div>

	</div>
	<div class="main_table">
		<div class="table_title">
			<div>电源管理</div>
		    <div class="right_input02">
		    </div>
		</div>
		<div style="height:40px;"></div>
		<table cellpadding="0" cellspacing="0"  class="table" id ="table">
			<tbody>
				<tr>
					<td style="width: 200px;">
						<img src="<%=request.getContextPath()%>/images/topu_icon06.png"/> &nbsp;工控协议模糊测试工具电源控制
					</td>
					<td style="width:120px;text-align:left;padding-left:20px;">
						<span id="btcsShutdown" title="关机"><img  class="btn" src="<%=request.getContextPath()%>/images/power.png" />&nbsp; 关机</span>
					</td>
					<td style="width:120px;">
						<span id="btcsReboot" title="重启"><img name="btcsReboot"  class="btn" src="<%=request.getContextPath()%>/images/reboot.png"/>&nbsp; 重新启动</span>
					</td>
					<td style="text-align:left;">
						工控协议模糊测试工具关机、重启功能。
					</td>
				</tr>
				<tr>
					<td style="width: 200px;">
						<img src="<%=request.getContextPath()%>/images/topu_icon07.png"/> &nbsp;被测设备电源控制
					</td>
					<td colspan="2" style="text-align: left;padding-left:20px;">
						<span id="btbcReboot" title="重启"><img  name="btbcReboot" class="btn"  src="<%=request.getContextPath()%>/images/reboot.png"/> &nbsp; 重新启动</span>
					</td>
					<td style="text-align:left;">
						通过工控协议模糊测试工具设备220V AC output 为PLC供电,在自动测试组网下,该功能可对被测设备实现电源控制。
					</td>
				</tr>
			</tbody>
		</table>
		
		
		<div class="table_title">
			<div>管理IP设置</div>
		    <div class="right_input02">
		    </div>
		</div>
		<div class="box">
			<div class="header">
				工控协议模糊测试工具IP
			</div>
			<div class="content">
				<form name="forminter " id="forminter"
					action="<%=request.getContextPath()%>/system/interfaceipadd.do"
					class="demoform" method="post">
					<div style="margin-bottom:20px;">
						<label for="ip">IP地址：</label>
						<input type='text' name='ip' id='ip' style="width: 162px;"
							value='${iip}' datatype="ip" ignore="ignore"
							errormsg="请输入合法的IP地址！" />
						<span class="Validform_checktip"></span>
					</div>
					<div>
						<input class="inputs" type="submit"
							id="config" value="配置" /> <span class="Validform_right Validform_checktip" id="bcmsg"></span>
					</div>
				</form>
				<div style="text-align:center;color:red;font-size:10px;margin-top:30px;">
					注：管理口IP地址不能同被测设备同一网段,IP修改后<br/><a href="javascript:void(0);" id="btcsReboot2">重启生效</a>
				</div>
			</div>
		</div>
		<!-- <div class="formtitle">
			<span>服务功能控制</span>
		</div>
		<div class="divtitle">
			 <input id="btcsReboot" name="btcsReboot" class="inputs" style="margin-left:50px;"
				type="button" value="重启" />
		</div>
		<div class="formtitle">
			<span>被测电源控制</span>
		</div>
		<div class="divtitle">
			<input id="btbcReboot" name="btbcReboot" class="inputs" style="margin-left:50px;" type="button"
				value="重启" />
		</div> -->
	</div>
</body>
</html>