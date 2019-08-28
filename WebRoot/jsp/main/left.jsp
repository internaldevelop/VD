<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>管理平台</title>
<base target="_self">
	<link href="<%=request.getContextPath()%>/commons/css/newcss.css"
		rel="stylesheet" type="text/css" />
	<script src="<%=request.getContextPath()%>/commons/js/jquery.js"></script>
	<style type="text/css">
body {
	margin: auto;
	height: auto;
	background-color: #ffffff;
	padding: 0px;
	overflow: auto;
	overflow-x: hidden;
	scrollbar-face-color: #eff8e6;
	scrollbar-shadow-color: #edf2e3;
	scrollbar-highlight-color: #ffffff;
	scrollbar-3dlight-color: #F2F2F2;
	scrollbar-darkshadow-color: #bdbcbd;
	scrollbar-arrow-color: #bdbcbd;
	height: auto;
	background-color: #ffffff;
	padding: 0px;
	overflow: auto;
	overflow-x: hidden;
}
</style>
</head>

<body leftmargin="8" topmargin='8'>
	<div class="left_div">
		<ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">环境设置</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
							<div class="left_img02">
								<img src="<%=request.getContextPath()%>/images/icons/base.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a href="<%=request.getContextPath()%>/environment/create.do"
									target="mainFrame">设置测试网络</a>
							</div>
						</li>
					</ul>
				</div>
			</li>
		</ul>
		<ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">扫描设置</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
							<div class="left_img02">
								<img src="<%=request.getContextPath()%>/images/icons/port.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a href="<%=request.getContextPath()%>/portscan/scan.do"
									target="mainFrame">端口扫描</a>
							</div>
						</li>
					</ul>
					<ul>
						<li>
							<div class="left_img02">
								<img src="<%=request.getContextPath()%>/images/icons/base.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a
									href="<%=request.getContextPath()%>/multicast/queryMulticastList.do"
									target="mainFrame">多播地址</a>
							</div>
						</li>
					</ul>
				</div>
			</li>
		</ul>
		<ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">测试设置</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
							<div class="left_img02">
								<img
									src="<%=request.getContextPath()%>/images/icons/operation.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a
									href="<%=request.getContextPath()%>/jsp/testsetup/main.jsp?ranparam=random()"
									target="mainFrame">测试设置</a>
							</div>
						</li>
					</ul>
					<ul>
						<li>
							<div class="left_img02">
								<img src="<%=request.getContextPath()%>/images/icons/ks2.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a href="<%=request.getContextPath()%>/testexecute/execute.do"
									target="mainFrame">测试执行</a>
							</div>
						</li>
					</ul>
					<ul>
						<li>
							<div class="left_img02">
								<img
									src="<%=request.getContextPath()%>/images/icons/statistics.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a
									href="<%=request.getContextPath()%>/jsp/testresult/main.jsp?ranparam=random()"
									target="mainFrame">测试结果</a>
							</div>
						</li>
					</ul>
				</div>
			</li>
		</ul>
		<%--
		<ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">抓包设置</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
							<div class="left_img02">
								<img
									src="<%=request.getContextPath()%>/images/icons/grabPacket.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a        
									href="<%=request.getContextPath()%>/jsp/grabPacket/grabpacket.jsp"
									target="mainFrame">抓包设置</a>
							</div>
						</li>
					</ul>
					<ul>
						<li>
							<div class="left_img02">
								<img
									src="<%=request.getContextPath()%>/images/icons/grabPacketResult.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a
									href="<%=request.getContextPath()%>/jsp/grabPacket/grabpacketlist.jsp"
									target="mainFrame">抓包结果</a>
							</div>
						</li>
					</ul>
				</div>
			</li>
		</ul>
		--%>
		<ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">协议识别</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
							<div class="left_img02">
								<img
									src="<%=request.getContextPath()%>/images/icons/protocol.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a
									href="<%=request.getContextPath()%>/protocol/queryProtocolList.do"
									target="mainFrame">协议识别</a>
							</div>
						</li>
					</ul>
				</div>
			</li>
		</ul>
		<%-- <ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">应用安全</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
							<div class="left_img02">
								<img src="<%=request.getContextPath()%>/images/icons/base.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a
									href="<%=request.getContextPath()%>/configuration/querydata.do"
									target="mainFrame">任务设置</a>
							</div>
						</li>
					</ul>
					<ul>
						<li>
							<div class="left_img02">
								<img src="<%=request.getContextPath()%>/images/icons/base.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a href="<%=request.getContextPath()%>/apptestResult/init.do"
									target="mainFrame">测试结果</a>
							</div>
						</li>
					</ul>
				</div>
			</li>
		</ul> --%>
		<%-- <ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">动态端口</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
							<div class="left_img02">
								<img src="<%=request.getContextPath()%>/images/icons/base.png"
									width="16" height="16"/>
							</div>
							<div class="left_word02">
								<a
									href="<%=request.getContextPath()%>/dynamicports/init.do"
									target="mainFrame">动态端口</a>
							</div>
						</li>
					</ul>
				</div>
			</li>
		</ul> --%>
		<%-- <ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">漏洞库</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
						<div class="left_img02">
							<a href="<%=request.getContextPath()%>/cnvd/list.do" target="mainFrame">
								<img src="<%=request.getContextPath()%>/images/icons/base.png"
									width="16" height="16"/>
							</a>
						</div>
						<div class="left_word02">
							<a href="<%=request.getContextPath()%>/cnvd/list.do" target="mainFrame">
								漏洞库
							</a>
						</div>
						</li>
					</ul>
				</div>
			</li>
			</ul> --%>
		<%--<ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">配置安全检查</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
						<div class="left_img02">
							<a href="<%=request.getContextPath()%>/securityCheck/config.do" target="mainFrame">
								<img src="<%=request.getContextPath()%>/images/icons/base.png"
									width="16" height="16"/>
							</a>
						</div>
						<div class="left_word02">
							<a href="<%=request.getContextPath()%>/securityCheck/config.do" target="mainFrame">
								检查配置
							</a>
						</div>
						</li>
						<li>
						<div class="left_img02">
							<a href="<%=request.getContextPath()%>/jsp/securitycheck/list.jsp" target="mainFrame">
								<img src="<%=request.getContextPath()%>/images/icons/base.png"
									width="16" height="16"/>
							</a>
						</div>
						<div class="left_word02">
							<a href="<%=request.getContextPath()%>/jsp/securitycheck/list.jsp" target="mainFrame">
								检查结果
							</a>
						</div>
						</li>
					</ul>
				</div>
			</li>
		</ul>--%>
		<!-- ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">系统信息</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
							<div class="left_img02">
								<img
									src="<%=request.getContextPath()%>/images/icons/system.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a href="<%=request.getContextPath()%>/systemInfo/systemInfo.do"
									target="mainFrame">系统信息</a>
							</div>
						</li>
					</ul><%--
					<ul>
						<li>
							<div class="left_img02">
								<img
									src="<%=request.getContextPath()%>/images/icons/version.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a href="<%=request.getContextPath()%>/systemInfo/versionInfo.do"
									target="mainFrame">版本信息</a>
							</div>
						</li>
					</ul>
				--%></div>
			</li>
		</ul-->
		<ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">系统配置</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
							<div class="left_img02">
								<img
									src="<%=request.getContextPath()%>/images/icons/operation.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a href="<%=request.getContextPath()%>/system/findinterface.do"
									target="mainFrame">系统配置</a>
							</div>
						</li>
					</ul>
					<ul>
						<li>
							<div class="left_img02">
								<img
									src="<%=request.getContextPath()%>/images/icons/operationlog.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a
									href="<%=request.getContextPath()%>/operationlog/operationLogPage.do"
									target="mainFrame">系统日志</a>
							</div>
						</li>
					</ul>
				</div>
			</li>

		</ul>
		<ul>
			<li class="left_button_choice">
				<div class="left_img">
					<img src="<%=request.getContextPath()%>/images/left_icon02.png"
						width="10" height="10" class="left_icon" />
				</div>
				<div class="left_word ">
					<a href="#">用户管理</a>
				</div>
				<div class="button_list" style="display: none;">
					<ul>
						<li>
							<div class="left_img02">
								<img
									src="<%=request.getContextPath()%>/images/icons/operation.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a
									href="<%=request.getContextPath()%>/loginContorller/updateTojsp.do"
									target="mainFrame">修改密码</a>
							</div>
						</li>
					</ul>
					<ul>
						<li>
							<div class="left_img02">
								<img
									src="<%=request.getContextPath()%>/images/icons/operation.png"
									width="16" height="16">
							</div>
							<div class="left_word02">
								<a
									href="<%=request.getContextPath()%>/loginContorller/lockUser.do"
									target="mainFrame">锁定配置</a>
							</div>
						</li>
					</ul>
				</div>
			</li>
		</ul>
	</div>
	<script type="text/javascript">
	$(".left_img").bind("click",function (){
		$(".button_list").hide();
		var src = $(this).find("img").attr("src");
		
		$(".left_icon").attr("src",'<%=request.getContextPath()%>/images/left_icon02.png');
		if(src.indexOf("icon01.png") != -1){
			$(this).parent().find("div[class='button_list']").hide();
			$(this).find("img").attr("src",src.replace("icon01.png","icon02.png"))
		}else{
			$(this).find("img").attr("src",src.replace("icon02.png","icon01.png"))
			$(this).parent().find("div[class='button_list']").show();
		}
	});
	$(".left_word").bind("click",function(){
		$(this).prev().find("img").click();
	});
	</script>
</body>
</html>