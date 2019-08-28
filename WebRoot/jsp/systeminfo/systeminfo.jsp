<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/commons/js/jquery.js"></script>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/commons/css/canvas.css"></link>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/commons/css/newcss.css"></link>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/jsp/portscan/css/progressbar.css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/commons/js/kc.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/commons/js/raphael.2.1.0.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/commons/js/justgage.1.0.1.min.js"></script>
<style type="text/css">
.start {
	position: absolute;
	right: 65px;
	margin-top: 5px;
}

#g1 {
	width: 100px;
	height: 80px;
	display: inline-block;
	margin: 1em;
}

#g2, #g3 {
	width: 100px;
	height: 80px;
	display: inline-block;
	margin: 1em;
}

.shbz_td {
	height: 90px;
	text-align: center;
}
</style>

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
			<div class="top_title_word">系统信息</div>
			<div class="top_title_word">&gt;</div>
			<div class="top_title_word">系统信息</div>
		</div>

		<!--table-->
		<div class="main_table">
			<div class="table_title">
				<div>系统信息</div>
				<div class="search_input"></div>
			</div>
			<div style="width: 100%; float: left; height: 60%;">
				<div id="title_div" style="padding-right: 17px;">
					<table cellpadding="0" cellspacing="0" class="table">
						<tr class="table_title02">
							<td width="20%">服务器IP</td>
							<td width="33%">系统状态</td>
							<td width="33%">网口状态</td>
						</tr>
					</table>
				</div>
				<div class="" style="float: left; overflow-y: auto; width: 100%;"
					id="fix_div">
					<table id="scanTable" cellpadding="0" cellspacing="0" class="table"
						style="">
						<tr>
							<td width="20%" id="managerip">${managerIp}</td>
							<td width="33%">
								<div id="g1"></div>
								<div id="g2"></div>
								<div id="g3"></div>
							</td>
							<td width="33%" id="net"><img
								src='<%=request.getContextPath() %>/images/netgray.png' /> <img
								src='<%=request.getContextPath() %>/images/netgray.png' /> <img
								src='<%=request.getContextPath() %>/images/netgray.png' /> <img
								src='<%=request.getContextPath() %>/images/netgray.png' /> <img
								src='<%=request.getContextPath() %>/images/netgray.png' /> <img
								src='<%=request.getContextPath() %>/images/netgray.png' /></td>
						</tr>

					</table>
				</div>
			</div>
		</div>
</body>
<script>
 window.onload=function(){
	 // document.getElementById("managerip").innerHTML="127.0.0.1";
      var g1, g2, g3;
        g1 = new JustGage({
          id: "g1",
          value: 0.0,
          min: 0,
          max: 100,
          title: "CPU使用率",
          label: ""
        });

        g2 = new JustGage({
          id: "g2",
          value: 0.0,
          min: 0,
          max: 100,
          title: "内存使用率",
          label: ""
        });

        g3 = new JustGage({
          id: "g3",
          value: 0.0, 
          min: 0,
          max: 100,
          title: "硬盘使用率",
          label: ""
        });
        
        window.parent.sendConnect();
		window.parent.alertSysInfo = function alertSysInfo(data) {
			//document.getElementById("managerip").innerHTML=data.ip;
			var cpu = (data.cpu).toFixed(2);
			var mem = (data.mem).toFixed(2);
			var disk = (data.disk).toFixed(2);
			eval('g1').refresh(cpu);
			eval('g2').refresh(mem);
			eval('g3').refresh(disk);
			var s="";
			if(data.net1==1){
				s+="<img src='<%=request.getContextPath() %>/images/netgreen.png'/>  ";
			}else{
				s+="<img src='<%=request.getContextPath() %>/images/netgray.png'/>  ";
			}
			
			if(data.net2==1){
				s+="<img src='<%=request.getContextPath() %>/images/netgreen.png'/>  ";
			}else{
				s+="<img src='<%=request.getContextPath() %>/images/netgray.png'/>  ";
			}
			
			if(data.net3==1){
				s+="<img src='<%=request.getContextPath() %>/images/netgreen.png'/>  ";
			}else{
				s+="<img src='<%=request.getContextPath() %>/images/netgray.png'/>  ";
			}
			
			if(data.net4==1){
				s+="<img src='<%=request.getContextPath() %>/images/netgreen.png'/>  ";
			}else{
				s+="<img src='<%=request.getContextPath() %>/images/netgray.png'/>  ";
			}
			
			if(data.net5==1){
				s+="<img src='<%=request.getContextPath() %>/images/netgreen.png'/>  ";
			}else{
				s+="<img src='<%=request.getContextPath() %>/images/netgray.png'/>  ";
			}
			
			if(data.net6==1){
				s+="<img src='<%=request.getContextPath() %>/images/netgreen.png'/>  ";
			}else{
				s+="<img src='<%=request.getContextPath() %>/images/netgray.png'/>  ";
			}
			document.getElementById("net").innerHTML=s;
		};
 	};
</script>
</html>