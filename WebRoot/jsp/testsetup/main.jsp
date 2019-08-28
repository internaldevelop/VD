<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*"%>
<%@ page import="org.wnt.core.ehcache.*"%>
<%@ page import="com.wnt.web.testexecute.controller.*"%>
<%@ page import="com.wnt.web.protocol.ProtocolController.*"%>
<%@ page import="common.*"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'main.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/jsp/testsetup/css/testsetup.css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/commons/js/jquery.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<script type="text/javascript">
<%	
	int testStatus=0;
	if(application.getAttribute("startScan")!=null){
		//端口扫描运行中
		testStatus=2;
	}
%>
			$(function(){
				if(<%=testStatus%>!=0){
					interval_progressScan(0);
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
								location.reload();
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
			function IFrameReSizeWidth(iframename) {
				var pTar = document.getElementById(iframename);
				if (pTar) {  //ff
				if (pTar.contentDocument && pTar.contentDocument.body.offsetWidth) {
					pTar.width = pTar.contentDocument.body.offsetWidth;
				}  //ie
				else if (pTar.Document && pTar.Document.body.scrollWidth) {
					pTar.width = pTar.Document.body.scrollWidth;
				}
				}
				if(pTar.width<800){
					pTar.width = 800;
				}
			}
		
		</script>

</head>
<table height=100% width=100% border=0>
	<Tr height=100%>
		<td width="28%">
			<div class="div_left">
				<div class="div_up">
					<iframe src="jsp/testsetup/leftFrameUp.jsp" scrolling="no"
						name="leftFrameUp" id="leftFrameUp" frameborder="0" height="100%"
						width="100%" style="float: left;"></iframe>
				</div>
				<div class="div_down">
					<iframe src="jsp/testsetup/leftFrameDown.jsp" scrolling="no"
						name="leftFrameDown" id="leftFrameDown" frameborder="0"
						height="100%" width="100%" style="float: left;"></iframe>
				</div>

			</div>
		</td>
		<td>
			<div class="div_right">
				<iframe src="jsp/testsetup/centerFrame.jsp" name="centerFrame"
					id="centerFrame" frameborder="0" height="100%" width="100%"
					onload='IFrameReSizeWidth("centerFrame");'></iframe>
			</div>
		</td>
	</Tr>
</table>
</html>
