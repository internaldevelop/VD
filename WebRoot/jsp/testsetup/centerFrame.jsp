<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*"%>
<%@ page import="org.wnt.core.ehcache.*"%>
<%@ page import="com.wnt.web.testexecute.controller.*"%>
<%@ page import="com.wnt.web.protocol.ProtocolController.*"%>
<%@ page import="common.*"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="/commons/jsp/title.jsp" />
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/jsp/testsetup/css/testsetup.css"></link>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/jsp/environment/css/environment.css"></link>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/jsp/testsetup/js/suite.js"></script>
<%
	int testStatus = 0;
	if(TestExecuteUtil.testEntry!=null){
		if (TestExecuteUtil.testEntry.getStatus() != 5) {
			//测试案例运行中
			testStatus = 1;
		}
		if (application.getAttribute("startScan") != null) {
			//端口扫描运行中
			testStatus = 2;
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
	}
%>
<script>
	$(function() {
		//alert(document.body.clientHeight);
		var h = document.body.clientHeight;
		var w = document.body.clientWidth;
		//alert(w);
		$("#showName").css("height", h - 130 + "px");
		$("#middlediv").css("height", $("#tempDiv").height() + "px");
		$("#middis").css("height", $("#tempDiv").height() + "px");
		//alert();
		if (w * 0.35 < 360) {
			$("#tempDiv").css("width", "360px");
			$("#middlediv").css("width", "380px");
			$("#middis").css("width", "380px");
		} else {
			$("#tempDiv").css("width", "35%");
			$("#middlediv").css("width", "60%");
			$("#middis").css("width", "60%");
		}
		$("#tempDiv").css("height", h - 46 + "px");
		$("#middlediv").css("height", h - 46 + "px");
		$("#middis").css("height", h - 46 + "px");

		if (<%=testStatus%>!= 0) {
			//页面中所有的form控件不可用
			$("input").attr("disabled", 'true');
			$("select").attr("disabled", 'true');
			$("textarea").attr("disabled", 'true');
		}
		
		var userName="<%=session.getAttribute("userName")%>"; 
        if(userName == "audit"){
            $("#centerRemoveSuite").attr("disabled","disabled");
            $("#centerRemoveSuiteAll").attr("disabled","disabled");
            $("#centerSaveSuite").attr("disabled","disabled");
            $("#saveSuite").attr("disabled","disabled");
        }else{
        	 $("#centerRemoveSuite").removeAttr("disabled");
             $("#centerRemoveSuiteAll").removeAttr("disabled");
             $("#centerSaveSuite").removeAttr("disabled");
             $("#saveSuite").removeAttr("disabled");
        }
	});
</script>
<style type="text/css">
body {
	/* 去掉竖线 */
	/* overflow-y : hidden;   
     overflow-x : hidden; */
	
}
</style>
<body class="main_body">
	<div class="">
		<!--table-->
		<div class="main_table">
			<div class="table_title">
				<div>测试套件</div>
				<div class="right_input02"></div>
			</div>
			<!--middle search-->
			<div id="tempDiv" class="main_search"
				style="margin-left: 20px; border: 1px solid black; margin-top: 5px;">
				<table style="margin: 0px; margin-left: 5px">
					<Tr class="search_ul">
						<Td class="search_input">我的模版名称： <input type='text'
							name='name' id='name' />
						</Td>
					</Tr>
					<Tr class="search_ul">
						<Td colspan=2><input type='button' id="centerSaveSuite"
							value='保存' class="kc_btn" style="float: left;" /> <input
							type='button' id="centerRemoveSuite" value='删除' class="kc_btn"
							style="float: left; margin-left: 6px;" /> <input type='button'
							id="centerRemoveSuiteAll" value='全删' class="kc_btn"
							style="float: left; margin-left: 6px;" /> <%--<input type='button'
							id="centerExecuteSuite" value='执行' class="kc_btn"
							style="float:left;margin-left:6px;" /></Td>--%>
					</Tr>
					<Tr>
						<Td colspan=2>
							<form id="suiteForm">
								<div id="showName" name="showName"></div>
							</form>
						</Td>
					</Tr>
				</table>


			</div>
			<div id="middis" class="main_search"
				style="float: left; margin-left: 20px; margin-top: 5px; border: 1px solid black; display: inline; overflow: auto;">
				<div style="margin-left: 15px; margin-top: 10px;">
					<h4>从套件中选择和编辑测试案例</h4>
				</div>
			</div>

			<!--middle search-->
			<div id="middlediv" class="main_search"
				style="float: left; margin-left: 20px; margin-top: 5px; border: 1px solid black; display: inline; overflow: auto;">
				<form name="page_form" id="page_form" class="demoform" method="post">
					<input type="hidden" id="TESTDEPLAYID" name="TESTDEPLAYID" /> <input
						type="hidden" id="TESTTYPEN" name="TESTTYPEN" value="0" /> <input
						type="hidden" id="REMARK" name="REMARK" value="0" />
					<div style="margin-left: 15px; margin-top: 10px;">
						<h4>从套件中选择和编辑测试案例</h4>
					</div>
					<div style="margin-left: 15px">
						<h5 id="casetitle" name="casetitle">风暴测试显示</h5>
					</div>
					<table width="100%" border=0>
						<tr id="rname" name="rname">
							<td class="search_word">协议名称:</td>
							<td class="search_input"><input type="text" id="PRONAME"
								name="PRONAME" value="0" maxlength="32"></td>
						</tr>
						<tr id="rstotal" name="rstotal" style="display: none">
							<td class="search_word">总发包数:</td>
							<td class="search_input"><input type="text" id="STOTAL"
								name="STOTAL" value="0"></td>
						</tr>
						<tr id="rtestrate1" name="rtestrate1">
							<td id="rtestratetext" name="rtestratetext" style="width: 30%;"
								class="search_word">测试速率(包/秒):</td>
							<td style="width: 300px;" class="search_radio"><input
								type='radio' id='TESTRATE' name='TESTRATE' tvn="0" value="0"
								checked />默认</td>
						</tr>
						<tr id="rtestrate2" name="rtestrate2">
							<td class="search_word"></td>
							<td class="search_radio"><input type='radio' id="TESTRATESR"
								name='TESTRATE' tvn="1" value="1" /> <input type='text'
								id="TESTRATES" name='TESTRATES' size="5" maxlength="10"
								class="text_testsetup"
								onkeyup="this.value=this.value.replace(/\D/g,'')" />
							<%-- onKeyUp="this.value=this.value.replace(/[^\d+$]/g,'');"
							--%></td>
						</tr>
						<tr id="rtesttime1" name="rtesttime1">
							<td class="search_word">测试时间(s):</td>
							<td class="search_radio"><input type='radio' name='TESTTIME'
								tvn="0" value="120" checked />默认</td>
						</tr>
						<tr id="rtesttime2" name="rtesttime2">
							<td class="search_word"></td>
							<td class="search_radio"><input type='radio' name='TESTTIME'
								tvn="1" value="1" /> <input type='text' id="TESTTIMES"
								name='TESTTIMES' maxlength="4" class="text_testsetup"
								onkeyup="this.value=this.value.replace(/\D/g,'')" /></td>
							<!-- onKeyUp="this.value=this.value.replace(/[^\d+$]/g,'');"	 -->
						</tr>

						<tr id="rstarttestcase1" name="rstarttestcase1">
							<td style="width: 30%;" class="search_word">起始测试用例:</td>
							<td style="width: 300px;" class="search_radio"><input
								type='radio' name='STARTTESTCASE' id="STARTTESTCASE"
								maxlength="10" value="0" tvn="0" checked="true" />首条用例</td>
							<!--  First in set -->
						</tr>
						<tr id="rstarttestcase2" name="rstarttestcase2">
							<td class="search_word"></td>
							<td class="search_radio"><input type='radio' tvn="1"
								name='STARTTESTCASE' id="STARTTESTCASES" value="1" id="" />自定义
								<input type='text' name='STARTTESTCASES' maxlength="10"
								id="STARTTESTCASESCUS" class="text_testsetup"
								onkeyup="this.value=this.value.replace(/\D/g,'')" /></td>
							<!-- Production -->
							<!-- onKeyUp="this.value=this.value.replace(/[^\d+$]/g,'');" -->
						</tr>
						<tr id="rendtestcase1" name="rendtestcase1">
							<td class="search_word">终止测试用例:</td>
							<td class="search_radio"><input type='radio' tvn="0"
								name='ENDTESTCASE' id="ENDTESTCASE" value="0" checked="true" />尾条用例</td>
							<!--  Last in set -->
						</tr>
						<tr id="rendtestcase2" name="rendtestcase2">
							<td class="search_word"></td>
							<td class="search_radio"><input type='radio' tvn="1"
								name='ENDTESTCASE' value="1" id="ENDTESTCASES" />自定义<input
								type='text' name='ENDTESTCASES' id="ENDTESTCASESCUS"
								maxlength="10" class="text_testsetup"
								onkeyup="this.value=this.value.replace(/\D/g,'')" /></td>
							<!-- Production -->
							<!--  onKeyUp="this.value=this.value.replace(/[^\d+$]/g,'');" -->
						</tr>
						<tr id="rtraceability1" name="rtraceability1">
							<td style="width: 30%;" class="search_word">问题溯源:</td>
							<td style="width: 300px;" class="search_radio"><input
								type='radio' name='TRACEABILITY' tvn="0" value="0" checked />禁止</td>
							<!-- Disabled -->
						</tr>
						<tr id="rtraceability2" name="rtraceability2">
							<td class="search_word"></td>
							<td class="search_radio"><input type='radio'
								name='TRACEABILITY' value="1" tvn="1" />准许</td>
						</tr>
						<tr id="rhurryup" name="rhurryup">
							<td class="search_word">抓包:</td>
							<td class="search_input"><select name="HURRYUP" id="HURRYUP">
									<option value="0">从不</option>
									<option value="1">总是</option>
							</select></td>
						</tr>
						<!-- <tr id="rtarget" name="rtarget" style="display:none;">
							<td class="search_word">目标:</td>
							<td class="search_input"><select name="TARGET" id="TARGET">
									<option value="1" selected="selected">测试网络1</option>
									<option value="2">测试网络2</option>
									<option value="0">全部</option>
							</select></td>
						</tr> -->
						<input name="TARGET" value="1" type="hidden" />
						<tr id="rpower" name="rpower">
							<td class="search_word">电源管理:</td>
							<td class="search_input"><select name="POWER" id="POWER">
									<option value="0">关闭</option>
									<option value="1">开启</option>
							</select></td>
						</tr>
						<tr id="rsmessage" name="rsmessage">
							<td class="search_word" id="rsmessagel" name="rsmessagel">发送报文个数:</td>
							<td class="search_input"><input type="text" id="SMESSAGE"
								name="SMESSAGE" maxlength="7" value="50000"
								onkeyup="this.value=this.value.replace(/\D/g,'')"></td>
						</tr>
						<tr id="remessagep" name="remessagep">
							<td class="search_word">错误报文比例(%):</td>
							<td class="search_input"><input type="text" id="EMESSAGEP"
								name="EMESSAGEP" value="0" maxlength="3"
								onkeyup="this.value=this.value.replace(/\D/g,'')"></td>
						</tr>
						<tr id="rportsend" name="rportsend">
							<td class="search_word">端口号:</td>
							<td class="search_input"><input type="text" id="PORTSEND"
								name="PORTSEND" value="0" maxlength="5"
								onkeyup="this.value=this.value.replace(/\D/g,'')"></td>
						</tr>

						<tr id="raddpro" name="raddpro">
							<td class="search_btn_left" style=""><input type="button"
								id="addnewfield" name="addnewfield" value="添加新字段">
							<td>
						</tr>
					</table>
					<table id="tpro" name="tpro" width="100%" border=0
						style="margin-left: 5px;">

					</table>
					<table id="savet" name="savet" width="100%" style="" border=0>
						<tr>
							<td colspan="2" class="search_btn_left" align=""><input
								type="button" id="saveSuite" value="保存"></td>
						</tr>

					</table>
				</form>
			</div>
		</div>
	</div>
</body>
</html>