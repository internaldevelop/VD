<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.wnt.web.testexecute.entry.*" %>
<%@ page import="org.wnt.core.ehcache.*" %>
<%@ page import="com.wnt.web.testexecute.controller.*" %>
<%@ page import="com.wnt.web.protocol.ProtocolController.*" %> 
<%@ page import="com.wnt.web.environment.controller.*" %>
<%@ page import="common.*" %>
<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include  page="/commons/jsp/title.jsp"/>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/jsp/portscan/css/progressbar.css" ></link>
<script type="text/javascript" src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/kc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/portscan/js/list.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<style type="text/css">
	#sort{display:inline-block;width:16px;height:12px;}
	.main_table table td a.asc{background-image:url("${basePath}/images/icon_num_top.png"); background-repeat:no-repeat;}
	.main_table table td a.desc{background-image:url("${basePath}/images/icon_num_bot.png");background-repeat:no-repeat;}
	.search_input input:disabled{background:rgb(235, 235, 228);}
	/*input:-webkit-autofill,textarea:-webkit-autofill,select:-webkit-autofill {  -webkit-box-shadow: 0 0 0 1000px rgb(235, 235, 228) inset;}*/
</style>
<%
	int testStatus=0;
	String linktype=null;
	Object equipStatus = false;
	if(TestExecuteUtil.testEntry !=null){
		if(TestExecuteUtil.testEntry.getStatus()!=5){
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
			testStatus = 1;
		}
		if(application.getAttribute("linktype")!=null){
			linktype =(String)application.getAttribute("linktype");
		}
		equipStatus = application.getAttribute("equip_conn_status");
%>
<script>
	$(function(){
		if(<%=testStatus%>==1){
			//页面中所有的form控件不可用
			$("input").attr("disabled",'true');
			$("select").attr("disabled",'true');
			$("textarea").attr("disabled",'true');
			//$("a").attr("disabled",'true');
			$("a").removeAttr("onclick");
			$("#btn_ctr").attr("disabled",'true');
		}else if(<%=testStatus%>==2){
			$("#beginPort").removeAttr("datatype");
			$("#endPort").removeAttr("datatype");
			//页面中所有的form控件不可用
			$("input").attr("disabled",'true');
			$("select").attr("disabled",'true');
			$("textarea").attr("disabled",'true');
			//$("a").attr("disabled",'true');
			$("a").removeAttr("onclick");
			//开始停止按钮可用
			$("#btn_ctr").removeAttr("disabled");
		}
		
		document.getElementById("fix_div").style.height=(window.screen.availHeight-525)+"px";
		
		//点击排序
		$("#sort").click(function(){
			if(this.className == "asc"){
				order(false);
				$(this).removeClass("asc").addClass("desc");
			}else{
				order(true);
				$(this).removeClass("desc").addClass("asc");
			}
		});
		
		/**
		* 排序函数
		* @param orderType true 升序，false 降序
		*/
		function order(orderType){
			var items = [];
			$("#scanTable tr").each(function(i,n){
				items[i] = n;
			});
			var l = items.length;
			var itemTmp = null;
			for(var i = 0;i< l;i++){
				itemTmp = items[i];
				for(var j = i+1;j<l;j++){
					var	tmp = parseInt($(itemTmp).find("td:first").text());
					var tmp2 = parseInt($(items[j]).find("td:first").text());
					if(orderType){
						if(tmp > tmp2){
							tmp = tmp2;
							itemTmp = items[j];
							items[j]  = items[i];
							items[i] = itemTmp;
						} 
					}else{
						if(tmp < tmp2){
							tmp = tmp2;
							itemTmp = items[j];
							items[j]  = items[i];
							items[i] = itemTmp;
						} 
					}
				}
			};
			for(var k = 0;k < l;k++){
				$("#scanTable").append(items[k]);
			}
		}
		
		$("[datatype]").keyup(function(){
			$(this).removeClass("Validform_error");
			$(".Validform_checktip").removeClass("Validform_wrong").html("");
		});
	
		changeScanType();
		
		var userName="<%=session.getAttribute("userName")%>"; 
        if(userName == "audit"){
            $("#btn_ctr").attr("disabled","disabled");
            $("#sumitBtn").attr("disabled","disabled");
        }else{
        	 $("#btn_ctr").removeAttr("disabled");
             $("#sumitBtn").removeAttr("disabled");
        }
        
	});
	
	function changeScanType(){
		if(<%=testStatus%>!=1 && <%=testStatus%>!= 2){
			var option =$('#scanType option:selected').val();
			if(option == 0 ){
				$("#beginPort").attr("datatype","port");
				$("#endPort").attr("datatype","portRange");
				$("#beginPort").removeAttr("disabled");
				$("#endPort").removeAttr("disabled");
				$("#portType").removeAttr("disabled");
				$("#portNum").removeAttr("disabled");
				$("#hportType").removeAttr("disabled");
				$("#sumitBtn").removeAttr("disabled");
				$("#btn_ctr").removeAttr("disabled");
				$("#beginPortSpan").addClass("Validform_wrong");
				$("#endPortSpan").addClass("Validform_wrong");
			}else
			if(option == 1){//如果是被动扫描
				//无论是连接类型是点对点还是桥接开始端口和结束端口都设置为失效
				$("#beginPort").removeAttr("datatype");
				$("#endPort").removeAttr("datatype");
				$("#beginPort").prop("disabled",true);
				$("#endPort").prop("disabled",true);
				if(<%=linktype%>==2){
					//如果是点对点类型端口类型端口号等全部设置为失效
					$("#portType").attr("disabled",'true');
					$("#portNum").attr("disabled",'true');
					$("#hportType").attr("disabled",'true');
					$("#sumitBtn").attr("disabled",'true');
					$("#btn_ctr").attr("disabled",'true');
					$("#beginPort").removeClass("Validform_error");
					$("#endPort").removeClass("Validform_error");
					$("#beginPortSpan").removeClass("Validform_wrong").empty();
					$("#endPortSpan").removeClass("Validform_wrong").empty();
				}
			}
		}
	}
</script>



<body class="main_body">
<div class="index_body02">
<!--top-->
<div class="top_title02">
        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
        <div class="top_title_word">工业控制系统安全测试及漏洞挖掘工具</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">扫描设置</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">端口扫描</div>        
</div>

<!--table-->
<div class="main_table">
<div class="table_title">
	<div>扫描端口</div>
    <div class="right_input02">
    	
    </div>
</div>
<!--middle search-->
<input type="hidden" value="${scanport.startScan }" id="startScanFlag"/>
<div class="main_search">
<form name="form1" id="form1" class="demoform"  method="post" action="<%=request.getContextPath() %>/portscan/startScan.do">
	<table width="100%" style="table-layout:fixed;" border=0>
     <tr>
         <td style="width:60px;" class="search_word">开始端口:</td>
         <td style="width:300px;" class="search_input">
         	<input type='text' name='beginPort' id='beginPort' value='${scanport.beginPort }' datatype="port" nullmsg="请输入开始端口"  errormsg="请输入合法的端口号！"/>
         	<span class="Validform_checktip" id="beginPortSpan"></span>
         </td>
         <td style="width:60px;" class="search_word">结束端口:</td>
         <td style="width:300px;" class="search_input">
         	<input type='text' name='endPort' id='endPort' value='${scanport.endPort }' datatype="portRange"  nullmsg="请输入结束端口" errormsg="请输入合法的端口号！"/>
			<span class="Validform_checktip" id="endPortSpan"></span>
         </td>
         <td class="search_btn">
         	<span id="msgdemo" name="sa"></span>
         	<input type="submit" id="btn_ctr" value="<c:if test="${1==scanport.startScan}" >停止扫描</c:if><c:if test="${1!=scanport.startScan}" >开始扫描</c:if>"/>
         									
         </td>
     </tr>
     <tr>
         <td class="search_word">端口类型:</td>
         <td class="search_input">
         	<select name="portType" id="portType">
				<option value="6" <c:if test="${6==scanport.portType}" >selected="selected"</c:if>>TCP</option>
				<option value="17" <c:if test="${17==scanport.portType}" >selected="selected"</c:if>>UDP</option>
			</select>
         </td>
         <td class="search_word">扫描类型:</td>
         <td class="search_input">
         	<select name="scanType" id="scanType" onchange="changeScanType();">
				<option value="0" <c:if test="${0==scanport.scanType}" >selected="selected"</c:if>>主动扫描</option>
				<option value="1" <c:if test="${1==scanport.scanType}" >selected="selected"</c:if>>被动扫描</option>
			</select>
         </td>
         
         
         <td class="search_btn">
         	
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

<div class="table_title">
	<div>添加端口</div>
    <div class="right_input02">
    	
    </div>
</div>
<!--middle search-->

<div class="main_search">
<form name="form2" id="form2" class="demoform" method="post" action="<%=request.getContextPath() %>/portscan/addPort.do">
    <table width="100%" style="table-layout:fixed;" border=0>
     <tr>
         <td style="width:60px;" class="search_word">添加端口:</td>
         <td style="width:300px;" class="search_input">
         	<input type='text' name='portNum' id='portNum' datatype="port" onblur="clearMsg()"  errormsg="请输入合法的端口号！"/>
         	<span class="Validform_checktip"></span>
         </td>
         <td style="width:60px;" class="search_word">端口类型:</td>
         <td style="width:300px;" class="search_input">
         	<select name="hportType" id="hportType">
				<option value="6">TCP</option>
				<option value="17">UDP</option>
			</select>
         </td>
         <td class="search_btn">
         	<input type="submit" value="添加" id="sumitBtn" <c:if test="${1==scanport.startScan}" >disabled="disabled"</c:if>/>
         	<span id="msgdemo"></span>
         </td>
     </tr>
     
    </table>
</form>
</div>
<div style=" width:100%;float:left; height:60%;" >
	<div id="title_div" style="padding-right:17px;">
    <table  cellpadding="0" cellspacing="0" class="table" >
    	<tr class="table_title02">
        	<td width="10%">端口<a id="sort" href="javascript:void(0);" class="asc"></a></td>
            <td width="35%">服务</td>
            <td width="15%">来源</td>
            <td width="15%">类型</td>
            <td width="15%">扫描类型</td>
            <td width="10%">操作</td>
            
        </tr>
    </table>
    </div>
    <div class="" style="float: left; overflow-y: auto;width: 100%;" id="fix_div">
    <table id="scanTable" cellpadding="0" cellspacing="0" class="table" style="">
        <c:forEach var="scan" items="${list}" varStatus="index">
		<tr>
			<td width="10%">${scan.PORTNUM}</td>
			<td width="35%">${scan.NAME}</td>
			<td width="15%">${scan.SOURCE}</td>
			<td width="15%">${scan.PORTTYPE}</td>
			<td width="15%">${scan.SCANTYPE}</td>
			<td width="10%"><a href="javascript:void(0)" onclick="deletePort('${scan.id }','${scan.PORTTYPEY }','${scan.PORTNUM }',this)">删除</a></td>
		</tr>
		</c:forEach>
    </table>
    </div>

</div>
</div>
</div>
</body>
</html>