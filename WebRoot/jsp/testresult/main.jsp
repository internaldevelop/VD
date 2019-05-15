<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*"%>
<%@ page import="org.wnt.core.ehcache.*"%>
<%@ page import="com.wnt.web.testexecute.controller.*"%>
<%@ page import="com.wnt.web.protocol.ProtocolController.*" %> 
<%@ page import="common.*"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>测试结果主页面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=request.getContextPath()%>/commons/js/jquery.js" ></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<script type="text/javascript">	
<%
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
</script>
  	</head>
	<frameset rows="30px,100%,*" cols="*" frameborder="no" border="0" framespacing="0">
		<frame src="jsp/testresult/topFrame.jsp" name="topFrame" scrolling="No" noresize="noresize"/>
		<frameset cols="380px,*" frameborder="no" border="0" framespacing="0">
			<frame src="jsp/testresult/leftFrame.jsp" name="leftFrame" scrolling="No" noresize="noresize"/>
			<frame src="jsp/testresult/rightFrame.jsp" id="rightFrame" name="rightFrame" scrolling="No" noresize="noresize"/>
		</frameset>
		<%--<frame src="jsp/testresult/buttomFrame.jsp" name="buttomFrame" scrolling="auto" noresize="noresize"/>
	--%></frameset>
</html>