<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*" %>
<%@ page import="org.wnt.core.ehcache.*" %>
<%@ page import="com.wnt.web.testexecute.controller.*" %>
<%@ page import="com.wnt.web.protocol.ProtocolController.*" %> 
<%@ page import="common.*" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0"> 
<jsp:include  page="/commons/jsp/title.jsp"/>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/jsp/environment/css/environment.css" ></link>
<script type="text/javascript" src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/kc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/environment/js/list.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>

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
		//动态端口扫描中
		testStatus = 2;
	}
%>
<script>
	$(function(){
		if(<%=testStatus%>!=0){
			//页面中所有的form控件不可用
			$("input").attr("disabled",'true');
			$("select").attr("disabled",'true');
			$("textarea").attr("disabled",'true');
			interval_progressScan(0);
		}
		//默认选中第一个监视器
		$('#tmonitors tr').eq(1).click();

		var userName="<%=session.getAttribute("userName")%>"; 
		if(userName == "audit"){
			$("#save").attr("disabled","disabled");
			$("#autocheck1").attr("disabled","disabled");
			$("#btn3").attr("disabled","disabled");
			$("#send").attr("disabled","disabled");
		}else{
			$("#save").removeAttr("disabled");
            $("#autocheck1").removeAttr("disabled");
            $("#send").removeAttr("disabled");
		}
		
	});
	function changelinkType(obj){
		var options=$("#linkType option:selected");
	    if(options.val() ==1){
		      $("#enable2").prop("checked",true);
		      $("#enable2").prop("disabled",false);
		      $("#subnetMask3").prop("disabled",false);
		      $("#ip3").prop("disabled",false);
			  $("#mac3").prop("disabled",false);
			  $("#btn3").prop("disabled",false);
	    }else if(options.val() ==2){
		      $("#enable2").prop("checked",false);
		      $("#enable2").prop("disabled",true);
		      $("#ip3").prop("disabled",true);
			  $("#mac3").prop("disabled",true);
			  $("#subnetMask3").prop("disabled",true);
			  $("#btn3").prop("disabled",true);
	    }
	}

</script>
</script>
<body class="main_body">
<div class="index_body02">
<!--top-->
<div class="top_title02">
   <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
   <div class="top_title_word">工控协议漏洞挖掘工具</div>
   <div class="top_title_word">&gt;</div>
   <div class="top_title_word">环境设置</div>
   <div class="top_title_word">&gt;</div>
   <div class="top_title_word">设置网络测试</div>
</div>

	<!--table-->
	<div class="main_table">
		<div class="table_title">
			<div>在测设备</div>
		    <div class="right_input02">
		    </div>
		</div>
	<!--middle search-->
	<div class="main_search">
	<form name="form1" id="form1" action="<%=request.getContextPath() %>/environment/save.do" class="demoform">
		<input type="hidden" name="id" value="${env.ID}">
		<input type="hidden" name="old_name" value="${env.NAME}">
		<table width="100%" style="table-layout:fixed;" border=0>
	        <tr>
	            <td style="width:60px;" class="search_word"><span style="color:red">*</span>设备名称:</td>
	            <td style="width:290px;" class="search_input">
	            	<input type='text' name='name' id='name' value='${env.NAME}' datatype="equipmentName" errormsg="只允许输入汉字、数字、字母、下划线和连接符，不超过32位!"/>
	           		<span class="Validform_checktip"></span>
	            </td>
	            <td style="width:60px;" class="search_word">设备型号:</td>
	            <td style="width:290px;" class="search_input">
	            	<input type='text' name='version' id='version' value='${env.VERSION}' datatype="equipmentVersion" errormsg="只允许输入汉字、数字、字母、下划线和连接符，不超过32位!"/>
	           		<span class="Validform_checktip"></span>
	            </td> 
	            <td style="width:60px;" class="search_word"><span style="color:red">*</span>连接类型:</td>
	            <td style="width:290px;" class="search_input">
	            	<select name="linkType" id="linkType" onchange="changelinkType(this)"  >
						<option value="1" <c:if test="${env.LINKTYPE==1}" >selected="selected"</c:if>>桥接</option>
						<option value="2" <c:if test="${env.LINKTYPE==2}" >selected="selected"</c:if>>点对点</option>
					</select>
					<span class="Validform_checktip"></span>
	            </td>
	            <td class="search_btn">
	            	<span id="msgdemo"></span>
	            	<input type="submit" id="save" value="保存"/>
	            </td>
	        </tr>
	        <tr>
	            <td class="search_word">备注:</td>
	            <td colspan="3" class="search_input">
	           		<TextArea name='remark' id='remark' maxlength="256" class="env_area" >${env.REMARK }</TextArea>
	            
	            </td>
	            
	            <td style="width:170px;"></td>
	        </tr>
        </table>
		
	</form>
	</div>
	
	<div class="table_title">
		<div>Eth0</div>
	    <div class="right_input02">
	    	
	    </div>
	</div>
	<!--第2部分-->

	<div class="main_search">
	<form name="form2" id="form2" action="<%=request.getContextPath() %>/environment/findMac.do" class="demoform"  method="post">
		<input type="hidden" name="id" value="${env.ID}">
	    <table width="100%" style="table-layout:fixed;" border=0>
	    	<tr style="height:0px;">
	            <td style="width:60px;" class="search_word">
				</td>
	            <td style="width:300px;" class="search_input">
	            </td>
	            <td style="width:60px;" class="search_word"></td>
	            <td style="width:300px;" class="search_input">
	            </td>
	            <td class="search_btn">
	            </td>
	        </tr>
	    	<tr>
	            <td  style="width:60px;">
					<input id="ifallow0" type='checkbox' value="1" readonly="readonly"  onclick="return false;"  checked>使能
				</td>
	            <td colspan="3" class="search_input" style="width:660px;">
	            </td>
	            
	            <td class="search_btn">
	            	<span id="msgdemo"></span>
	            	<input type="submit" id="autocheck1" value="自动检测" />
	            </td>
	        </tr>
	        <tr>
	            <td colspan="4" class="search_input" style="width:720px;">
	            	工控协议漏洞挖掘工具<span><hr></span>
	            </td>
	            
	            <td class="search_btn">
	            </td>
	        </tr>
	        <tr>
	            <td style="width:60px;" class="search_word">
					<span style="color:red">*</span>IP地址:
				</td>
	            <td style="width:300px;" class="search_input">
	            	<input type='text' name='ips' id='ip' value='${env.ip }' datatype="ip" errormsg="请输入合法的IP地址！"
	            	onchange="copyIp(this);"/>
	           		<span class="Validform_checktip"></span>
	            </td>
	            <td style="width:60px;" class="search_word"><span style="color:red">*</span>子网掩码:</td>
	            <td style="width:300px;" class="search_input">
	            	<input type='text' name='subnetMask' id='subnetMask' value='${env.subnetMask }' datatype="subnetMask" errormsg="请输入合法的子网掩码！"/>
					<span class="Validform_checktip"></span>
	            </td>
	            <td class="search_btn">
	            </td>
	        </tr>
	        <tr>
	            <td class="search_word">
					<span style="color:red">*</span>MAC地址:
				</td>
	            <td class="search_input">
	            	<input type='text' name='mac' id='mac' value='${env.mac }' readonly="readonly" />
	            </td>
	            <td class="search_word"></td>
	            <td class="search_input">
	            </td>
	            <td class="search_btn">
	            </td>
	        </tr>
	         <tr>
	            <td colspan="4" class="search_input" style="width:720px;">
	            	被测设备
	            	<span><hr></span>
	            </td>
	            
	            <td class="search_btn">
	            </td>
	        </tr>
	        <tr>
	            <td style="width:60px;" class="search_word">
					<span style="color:red">*</span>IP地址:
				</td>
	            <td style="width:300px;" class="search_input">
	            	<input type='text' name='ips2' id='ip2' value='${env.ip2 }' datatype="ip" errormsg="请输入合法的IP地址！"/>
	           		<span class="Validform_checktip"></span>
	            </td>
	            <td style="width:60px;" class="search_word"><span style="color:red">*</span>子网掩码:</td>
	            <td style="width:300px;" class="search_input">
	            	<input type='text' name='subnetMask2' id='subnetMask2' value='${env.subnetMask2 }' datatype="subnetMask"  errormsg="请输入合法的子网掩码！"/>
					<span class="Validform_checktip"></span>
	            </td>
	            <td class="search_btn">
	            </td>
	        </tr>
	        <tr>
	            <td class="search_word">
					<span style="color:red">*</span>MAC地址:
				</td>
	            <td class="search_input">
	            	<input type='text' name='mac2' id='mac2' value='${env.mac2 }' readonly="readonly" />
	            </td>
	            <td class="search_word"></td>
	            <td class="search_input">
	            </td>
	            <td class="search_btn">
	            </td>
	        </tr>
        </table>
	</form>
    </div>
    
	<div class="table_title">
		<div>Eth1</div>
	    <div class="right_input02">
	    	
	    </div>
	</div>
	<!--第3部分-->

	<div class="main_search">
	<form name="form3" id="form3" class="demoform" action="<%=request.getContextPath() %>/environment/findCheck2.do" method="post">
		<input type="hidden" id="envId" name="id" value="${env.ID}">
	    <table width="100%" style="table-layout:fixed;" border=0>
	    	<tr style="height:0px;">
	            <td style="width:60px;" class="search_word">
				</td>
	            <td style="width:300px;" class="search_input">
	            </td>
	            <td style="width:60px;" class="search_word"></td>
	            <td style="width:300px;" class="search_input">
	            </td>
	            <td class="search_btn">
	            </td>
	        </tr>
	    	<tr>
	            <td  style="width:60px;">
					<input id="enable2" name="enable2" type='checkbox' value="1" onclick="operEnableChecked2(this)" 
					<c:if test="${env.ENABLE2==1}" >checked</c:if> <c:if test="${env.ENABLE2==2}" >disabled</c:if>>使能 
				</td>
	            <td colspan="3" class="search_input" style="width:660px;">
	            </td>
	            
	            <td class="search_btn">
	            	<span id="msgdemo"></span>
	            	<input type="submit" value="自动检测" id="btn3" <c:if test="${env.ENABLE2==2 || env.ENABLE2==0}" >disabled</c:if> />
	            </td>
	        </tr>
	        <tr>
	            <td colspan="4" class="search_input" style="width:720px;">
	            	控制系统
	            	<span><hr></span>
	            </td>
	            
	            <td class="search_btn">
	            </td>
	        </tr>
	        <tr>
	            <td style="width:60px;" class="search_word">  
					<span style="color:red">*</span>IP地址:
				</td>
	            <td style="width:300px;" class="search_input">
	            	<input type='hidden' name='ips4' id='ip4' value='${env.ip }'>
	            	<input type='text' name='ips3' id='ip3' value='${env.ip3 }' datatype="ip" errormsg="请输入合法的IP地址！" <c:if test="${env.ENABLE2==2 || env.ENABLE2==0}" >disabled</c:if>/> 
	           		<span class="Validform_checktip"></span>
	            </td>
	            <td style="width:60px;" class="search_word"><span style="color:red">*</span>子网掩码:</td>
	            <td style="width:300px;" class="search_input">
	            	<input type='text' name='subnetMask3' id='subnetMask3' value='${env.subnetMask3 }' datatype="subnetMask"  errormsg="请输入合法的子网掩码！" <c:if test="${env.ENABLE2==2 || env.ENABLE2==0}" >disabled</c:if>/>
					<span class="Validform_checktip"></span>
	            </td>
	            <td class="search_btn">
	            </td>
	        </tr>
	        <tr>
	            <td class="search_word">
					<span style="color:red">*</span>MAC地址:
				</td>
	            <td class="search_input">
	            	<input type='text' name='mac3' id='mac3' value='${env.mac3 }' readonly="readonly" <c:if test="${env.ENABLE2==2 || env.ENABLE2==0}" >disabled</c:if>/>
	            </td>
	            <td class="search_word"></td>
	            <td class="search_input">
	            </td>
	            <td class="search_btn">
	            </td>
	        </tr>
	    </table>
	</form>
    </div>
    <!-- 第4部分 -->
    <div class="table_title">
		<div style="width:300px">监视器<font color="red">（提示：修改配置后需点击下发按钮生效）</font></div>
	    <div class="right_input02">
	    	
	    </div>
	</div>
	<div class="main_search">
		<input type="hidden" id="envId"  value="${env.EQUIPMENTID}">
	    <table width="100%" style="table-layout:fixed;" border=0>
	    	<tr style="height:0px;">
	            <td style="width:60px;" class="search_word">
				</td>
	            <td style="width:300px;" class="search_input">
	            </td>
	            <td style="width:60px;" class="search_word"></td>
	            <td style="width:400px;" class="search_input">
	            </td>
	            <td class="search_btn">
	            </td>
	        </tr>
	        <tr style="height:0px;">
	            <td colspan="2" class="td_top">
	            	<table id="tmonitors" name="tmonitors" class="table" cellpadding="0" cellspacing="0">
				    	<tr>
				        	<td width="60px">使能</td>
				            <td width="300px">名字</td>
				        </tr>
				        
						<c:forEach var="m" items="${monitors}" varStatus="index">
							<tr align='center' class="tr_bg" height="26" onclick="findMonitorById(${m.ID},this)"  >
								<td><input type="checkbox" onclick="selectMinitor(this,'${m.ID}');" <c:if test="${m.SELECTSTATUS==1}" >checked</c:if>></td>
								<td><a href="javascript:void(0);">${m.NAME}</a></td>
							</tr>
						</c:forEach>
			        
					</table>
				</td>
	           
	            <td colspan="2" class="td_top">
	            	<input type="hidden" id="monitorId">
	            	<div style="display:none;float:left" id="v4_1">
		            	<form id="form4_1" action="<%=request.getContextPath() %>/environment/save4.do">
							<input type="hidden" name="monitorId" value="1">
							<TABLE >
								<TR>
									<TD width="75px;" class="search_word">监视器:</TD>
									<TD class="search_input"><TextArea type='text' id='m_remark' STYLE="WIDTH:300px;height:100px;font-size:12px;resize:none;" readonly></TextArea></TD>
								</TR>
								<TR>
									<TD  class="search_word">
										<span style="color:red">*</span>超时时间:
									</TD >
									<TD class="search_input">
						                <input type='text' name='overtime' id='overtime' datatype="timeout" onchange="commit4(1)" errormsg="超时时间不可为0!" >(ms)</input>
										<span class="Validform_checktip"></span>
									</TD>
								</TR>
							</TABLE>
						</form>
	            	</div>
	            	<div style="display:none;float:left" id="v4_2">
		            	<form id="form4_2" action="<%=request.getContextPath() %>/environment/save4.do">
							<input type="hidden" name="monitorId" value="2">
							<TABLE  >
								<TR>
									<TD width="75px;" class="search_word">监视器:</TD>
									<TD class="search_input"><TextArea type='text' id='m_remark' STYLE="WIDTH:300px;height:100px;font-size:12px;resize:none;" readonly></TextArea></TD>
								</TR>
								<TR>
									<TD class="search_word">
										<span style="color:red">*</span>超时时间:
									</TD >
									<TD class="search_input">
						                <input type='text' name='overtime' id='overtime' datatype="timeout"  onchange="commit4(2)" errormsg="超时时间不可为0!"  >(ms)</input>
										<span class="Validform_checktip"></span>
									</TD>
								</TR>
							</TABLE>
						</form>
	            	</div>
	            	<div style="display:none;float:left" id="v4_3">
	            	<form id="form4_3" action="<%=request.getContextPath() %>/environment/save4.do">
						<input type="hidden" name="monitorId"  value="3">
						<TABLE >
							<TR>
								<TD width="75px;" class="search_word">监视器:</TD>
								<TD class="search_input"><TextArea type='text' id='m_remark' STYLE="WIDTH:300px;height:100px;font-size:12px;resize:none;" readonly></TextArea></TD>
							</TR>
							<TR>
								<!-- <TD class="search_word">
									Tcp Ports:
								</TD>
								<TD style="padding-left:10px;text-align:left;">
					                <input type='checkbox' name='tcpports' id='tcpports' value="1" onchange="commit4(3);">
					                使用打开的端口
								</TD> -->
							</TR>
						</TABLE>
					</form>
					</div>
					<div style="display:none;float:left" id="v4_4">
	            	<form id="form4_4" action="<%=request.getContextPath() %>/environment/save4.do">
						<input type="hidden" name="monitorId" value="4">
						
						<TABLE border=0>
							<TR>
								<TD width="75px;" class="search_word">监视器:</TD>
								<TD class="search_input"><TextArea type='text' id='m_remark' STYLE="WIDTH:300px;height:100px;font-size:12px;resize:none;" readonly></TextArea></TD>
							</TR>
							<TR>
								<TD class="search_word">
									<span style="color:red">*</span>周期:
								</TD >
								<TD class="search_input">
					                <input type='text' name='cyclePeriod' onchange="commit4(4)" id='cyclePeriod' datatype="n,cyclePeriod" ></input>(ms)
									<span class="Validform_checktip"></span>
								</TD>
							</TR>
							<TR>
								<TD class="search_word">
									<span style="color:red">*</span>单个输入:
								</TD >
								<TD class="search_input">
					                <select name="input" onchange="commit4(4)">
					                	<%for(int i=1;i<=16;i++){ %>
					                	<option id="o<%=i %>" value="<%=i %>">input <%=i %></option>
					                	<%} %>
					                </select>
								</TD>
							</TR>
							<TR>
								<TD class="search_word">
									<span style="color:red">*</span>告警等级:
								</TD >
								<TD class="search_input">
					                <input type='text' name='alarmLevel' onchange="commit4(4)" id='alarmLevel' datatype="alarmLevel" ></input> %
									<span class="Validform_checktip"></span>
								</TD>
							</TR>
						</TABLE>
					</form>
					</div>
	            </td>
	           
	            <td class="search_btn" style="Vertical-align:Top;padding-top:10px;">
	            	<span class="Validform_right" id="bcmsg"></span>
	            	<input type="button" value="下发" id="send" onclick="saveMonitor();"/>
	            </td>
	        </tr>
	    </table>
	</div>  
	
</div>
</div>
</body>
</html>