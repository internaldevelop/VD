<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="com.wnt.web.testexecute.entry.*" %>
<%@ page import="com.wnt.web.protocol.ProtocolController.*" %> 
<%@ page import="com.wnt.web.portscan.PortscanController.*" %> 
<%@ page import="com.wnt.web.protocol.ProtocolController.*" %> 
<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<meta http-equiv="expires" CONTENT="0">
<jsp:include  page="/commons/jsp/title.jsp"/>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<script language="JavaScript"
	src="<%=request.getContextPath()%>/controller/My97DatePicker/WdatePicker.js"></script>
<link
	href="<%=request.getContextPath()%>/controller/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" type="text/css" />
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/jsp/portscan/css/progressbar.css" ></link>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

int startScan=0;
if(application.getAttribute("startScan")!=null){
	//端口扫描运行中
	startScan=2;
}
if(application.getAttribute("configstartScan")!=null){
	//应用安全扫描中
	startScan = 2;
}
if(application.getAttribute("dynamicPortsStartScan")!=null){
	//动态端口扫描中
	startScan = 2;
}
if(application.getAttribute("startIdentity")!=null){
	//动态端口扫描中
	startScan = 2;
}
if(application.getAttribute("startIdentity")!=null){
	//动态端口扫描中
	startScan = 3;
}
%>


<script type="text/javascript">

//状态，1未执行，2执行中，3暂停，4完成，5停止
var testStatus=${testStatus};
//进度条
var progress=${progress};
//选择的测试模板
var tempId='${tempId}';
//类型
var type='${type}';

var basePath='<%=basePath%>';

var startScan='<%=startScan%>';

//日志后缀
var logProgressDisplay= '${logProgressDisplay}';
var userName="<%=session.getAttribute("userName")%>"; 

$(function(){   
    if(userName == "audit"){
        $("#btn_start").attr("disabled","disabled");   
        $("#btn_stop").attr("disabled","disabled"); 
        $("#clear").attr("disabled","disabled");
    }else{
    	 $("#btn_start").removeAttr("disabled");
         $("#btn_stop").removeAttr("disabled");
         $("#clear").removeAttr("disabled");
    }
});
</script>
<script type="text/javascript" src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/kc.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/testexecute/js/list.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<script src="<%=request.getContextPath()%>/jsp/testresult/js/highstock.src.js"></script>
<%--<script src="<%=request.getContextPath()%>/jsp/testresult/js/boost.js"></script>
--%><script src="<%=request.getContextPath()%>/jsp/testresult/js/exporting.js"></script>
<script src="<%=request.getContextPath()%>/jsp/testresult/js/offline-exporting.js"></script>
<script src="<%=request.getContextPath()%>/jsp/testresult/js/grid-light.js"></script>
<script src="<%=request.getContextPath()%>/jsp/testresult/js/highcharts-zh_CN.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/jsp/testexecute/js/chart.js"></script>
<style type="text/css">
.hitory{
	position: absolute;
    right: 150px;
    margin-top: 5px;
}
.closehitory{
	position: absolute;
    right: 65px;
    margin-top: 5px;
}
.historyBox{display:none;}
#chartTr{
			width: 500px;
			height: 300px;
			/*border: 1px solid blue;*/
			
			overflow:scroll; /*任何时候都强制显示滚动条*/
			/*overflow:auto; 需要的时候会出现滚动条*/
			overflow-x:scroll; /*控制X方向的滚动条*/
			overflow-y:auto; /*控制Y方向的滚动条*/
			scrollbar-face-color: white;
			/*scrollbar-shadow-color: pink;*/
			scrollbar-highlight-color: grey;
			/*scrollbar-3dlight-color: blue;*/
			scrollbar-darkshadow-color: grey;
			/*scrollbar-track-color: orange;*/
			/*scrollbar-arrow-color: purple;*/			
		}
	#chartTr::-webkit-scrollbar{
		width: 5px;
		height: 15px;
		background-color: grey;
	}
	#chartTr::-webkit-scrolllbar-track{
		-webkit-box-shadow:inset 0 0 6px rgb(100,100,100);
		border-radius: 10px;
		background-color: grey;
	}
	#chartTr::-webkit-scrollbar-thumb{
		border-radius: 10px;
		-webkit-box-shadow:inset 0 0 6px rgb(54,98,56);
		background-color:grey;
	}
.historyChartDiv {
    left: 3px;
    bottom: 0px;
    right: 0;
    overflow: auto;
    background: #fff;
    text-align: center;
}

.main_table div.main_search ul.search_ul {
    width: 240px;
    margin-left: 30px;
    margin-top: 10px;
    margin-bottom: 10px;
}

.main_table div.main_search li.search_button03 {
    width: 150px;
    text-align: center;
    margin-bottom: 10px;
    margin-left: 10px;
    margin-top: 10px;
}
.tabs-header{display:none;}
.tabs-header .tabs-wrap{margin-left: 1em;}
.tabs-header .tabs-wrap ul.tabs{float:none;}
.tabs-header .tabs-wrap ul.tabs li{float:none; display: inline-block;padding: 2px 10px;border: 1px solid #e1e1e1;border-bottom: none; background: #e1e1e1;margin-right: -3px;margin-bottom:-4px;}
.tabs-header .tabs-wrap ul.tabs li.tabs-selected{ background: white;z-index:1;}
.tabs-panels .tab-panel{display: none;border: 1px solid #e1e1e1;}
.tabs-panels .tab-panel{display:block;}
.tabs-panels .tab-panel.tabs-selected{background: white;display: block;}

</style>
<body class="main_body">
	<input type="hidden" id="testId" value="${testId }">

<div class="index_body02">
<!--top-->
<div class="top_title02">
        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
        <div class="top_title_word">工控协议漏洞挖掘工具</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">测试设置</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">测试执行</div>        
</div>
<!--table-->
<div class="main_table">
<div class="table_title">
	<div>测试套件</div>
    <div class="right_input02">
    	
    </div>
</div>
<!--middle search-->

<div class="main_search">
<form name="form1" id="form1" class="demoform" action="<%=request.getContextPath() %>/testexecute/findTestSuite.do" method="post">
	<table width="100%" style="table-layout:fixed;" border=0>
	   	<tr>
           	<td style="width:60px;" class="search_word">
           		选择模板:
			</td>
          	<td style="width:300px;" class="search_input">
          		<select name="tempId" id="tempId" onchange="fun_change(this);">
					<option value="0">请选择</option>
					<c:forEach var="testTemp" items="${list}" varStatus="index">
						<option value="${testTemp.id }" 
							<c:if test="${testTemp.id==tempId }">
								selected
							</c:if>

						>${testTemp.name }</option>
					</c:forEach>
				</select>
           	</td>
           	<td style="width:60px;" class="search_word"></td>
           	<td style="width:300px;" class="search_input">
           	</td>
           	<td class="search_btn">
           	</td>
       </tr>
	 </table>
</form>
</div>
<div style=" width:100%;float:left;" >
<div id="title_div" style="padding-right:17px;" >
 <table cellpadding="0" cellspacing="0"class="table">
   	<tr class="table_title02">
       	<td width="100px">&nbsp;</td>
         <td>测试案例</td>
         <c:forEach var="m" items="${exeList}" varStatus="index">
           <td width="120px">${m.name }</td>
	   	 </c:forEach> 
           
    </tr>
</table>
</div>
</div>
<div class="fix_table main_search" id="fix_div">
	<input type="hidden" id="monitorNum" value="${monitorNum }">
    <table cellpadding="0" cellspacing="0" id="testSuiteTable" class="table">
       <%
       //选择的监视器
       List<Map<String, Object>> exeList=(List<Map<String, Object>>)request.getAttribute("exeList"); 
       
       %>
       <script>
       	var tdHtml="";
       <%
       //动态拼一个选择监视器表格
		if(exeList!=null){
		for(int j=0;j<exeList.size();j++){ 
			Map<String, Object> m=exeList.get(j);
		%>
           	tdHtml+="<td width='120px' class='<%=m.get("ID") %>'></td>"
	    <%} 
	    }%>
        </script>
       <%
       //循环测试用例
       	List<TestCaseEntry> testCaseList=(List<TestCaseEntry>)request.getAttribute("testCaseList"); 
       	if(testCaseList!=null){
       		for(int i=0;i<testCaseList.size();i++){
       			TestCaseEntry tce=testCaseList.get(i);
       %>
			<tr align='center' bgcolor="#FFFFFF" height="26" >
				<td width="100px">
					<a class='<%=tce.getCssImg() %>' href='javaScript:void(0)' onclick='startSingleTest(this,<%=i %>)'></a>
				</td>
				<td><%=tce.getName() %></td>
				<%
				if(exeList!=null){
				for(int j=0;j<exeList.size();j++){ 
					Map<String, Object> m=exeList.get(j);
					String s=tce.getMonitorData().get(m.get("ID").toString());
				%>
		           	<td width="120px" class="<%=m.get("ID") %>">
		           		<%if("4".equals(s)){%>
		           			<img src="../images/ya.png">
		           		<%}else if("5".equals(s)){%>
		           			<img src="../images/no.png">
		           		<%}else if("1".equals(s) || "2".equals(s) || "3".equals(s)){%>
		           			<img src="../images/dui.png">
		           		<%}%>
		           	</td>
			    <%} 
			    }%>
			</tr>
		<%
       		}
		}
       	%>
   </table>
</div>
    
<div class="main_search">
   <table width="100%" style="table-layout:fixed;" border=0>
   	<tr>
          	<td style="width:200px;" class="search_btn_left">
          		<input type="button" value="全部执行" id="btn_start" onclick="startTest();"/>
         		<input type="button" value="停    止" id="btn_stop" onclick="stopTest();"/>
         		<span id="info"></span>
          	</td>
          	<td  class="search_btn_left">
          	 	<div class="progressName">测试进度：</div>
          	 	<div class="progressbar_1"> 
			        <div class="bar"></div> 
			    </div> 
			    <div class="progressNum"></div><span id="logProgressDisplay" name="logProgressDisplay" style="margin-left:30px;"></span>
          	</td>
      </tr>
     
 </table>
   
</div>
  
<div class="table_title">
	<div>实时数据</div>
    <div class="search_input">
    </div>
</div>
<div class="main_search" style="padding-left:3px">
     <table style="table-layout:fixed;width:100%;" border=0>
	   	<tr>
            <td style="text-align:left;">
            	<form id="chkForm">
				<c:forEach var="m" items="${monitorList}" varStatus="index">
					<input type='checkbox' name="chk_m" id="chk_${m.id}" value="${m.id }" style='margin-left:20px' onclick="checkChart(this)"
						<c:if test="${m.checked==true}" >checked</c:if>
					>${m.name }
				</c:forEach> 
				</form>
			</td>
        </tr>
	   	<tr >
            <td style="text-align:left;" >
            	<%
            	List<Map<String, Object>> mlist=(List<Map<String, Object>>)request.getAttribute("monitorList");
            	for(int i=0;i<mlist.size();i++){
            		Map<String, Object> m=mlist.get(i);
            		//四种监视有一个图
            		if("1".equals(m.get("id").toString()) || "2".equals(m.get("id").toString()) || "3".equals(m.get("id").toString())  ){
            	%>
	            	<div class="chartDiv" id="<%=m.get("id") %>" <%=(Boolean)m.get("checked")?"":"style='display:none'" %> >
	            	</div>
	            <%
            		}else if("4".equals(m.get("id").toString())){//离
            	%>	
	            	<div class="chartDiv" id="<%=m.get("id") %>"  >
		            	<div style="height:150px;width:100%" id="4_1"></div>
						<div style="height:150px;width:100%" id="4_2"></div>
	            	</div>
	            	  
	            <%
            		}else{//两个种网卡有两个图
            	%>	
            		<div class="chartDiv"id="<%=m.get("id") %>_1" <%=(Boolean)m.get("checked")?"":"style='display:none'" %>  >
	            	</div>
	            	<div class="chartDiv"id="<%=m.get("id") %>_2" <%=(Boolean)m.get("checked")?"":"style='display:none'" %> >
	            	</div>
            	<%
            	}
            	}
            	%>
			</td>
        </tr>
	 </table>		
		<div id="info2"></div>
        
</div>
<div class="table_title" >
	<div>历史数据</div>
     <div class="search_input">
    	<input id="hitory" class="hitory" type="button"   value="历史回顾" id="btn_history" onclick="hitory();" >
    </div>
      <div class="search_input">
    	<input id="hitory" class="closehitory" type="button"   value="收起回顾" id="close_history" onclick="closeHitory();" >
    </div>
</div>
	<form method="post" id="exportForm"
		action="<%=request.getContextPath() %>/chart/saveAsImage.do">

		<input type="hidden" name="svg"> <input type="hidden"
			name="filename"> <input type="hidden" name="type">
	</form>
	<form name='page_form' id="page_form" class="historyBox">
		<div class="main_table">
				<!--middle search-->
				<div class="main_search"  id="mainSearch">
					<ul id="mainUl">
						<li>
							<ul class="search_ul">
								<li class="search_word">开始时间：</li>
								<li class="search_input"><input type='text'
									name='beginDate' id="beginDate" class="form-control"
								    onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\');}'})"/></li><%-- ,maxDate:'#F{$dp.$D(\'endDate\');}' minDate:'%y-%M-%d #{%H-24}:%m:%s'
							--%></ul>
						</li>
						<li>
							<ul class="search_ul">
								<li class="search_word">结束时间：</li>
								<li class="search_input"><input type='text' name='endDate'
									id="endDate" class="form-control"
									onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginDate\');}'})" /></li>
							</ul>
						</li>
						<li class="search_button03"><input type="button" value="搜索"
							onclick="btnSearch()" /></li>
					</ul>
				</div>
		</div>
		<input type="hidden" id="historyType" name="historyType" value="1"/>
	</form>
	<div class="main_search historyBox" style="padding-left:3px">
		<div class="tabs-header">
			<div class="tabs-wrap">
				<ul	class="tabs">
					<li class="tabs-selected" data-type="1">ARP 历史数据</li>
					<li data-type="2">ICMP 历史数据</li>
					<li data-type="3">TCP 历史数据</li>
					<li data-type="13">ETH0 发送历史数据</li>
					<li data-type="14">ETH0 接收历史数据</li>
					<li data-type="15">ETH1 发送历史数据</li>
					<li data-type="16">ETH1 接收历史数据</li>
				</ul>
			</div>
		</div>  
		<div class="tabs-panels">
			<div class="historyChartDiv tab-panel tabs-selected" id="arpcontainer" >
	        </div>
	       	<div class="historyChartDiv tab-panel" id="icmpcontainer">
	      	</div>
	      	<div class="historyChartDiv tab-panel" id="tcpcontainer">
	   		</div>
	       	<div class="historyChartDiv tab-panel" id="etn0incontainer" >
	       	</div>
	       	<div class="historyChartDiv tab-panel" id="etn0outcontainer" >
	       	</div>
	       	<div class="historyChartDiv tab-panel" id="etn1incontainer" >
	       	</div>
	       	<div class="historyChartDiv tab-panel" id="etn1outcontainer" >
	       	</div>
       	</div>
	</div>
<div class="table_title" >
	<div>事件日志</div>
    <div class="right_input">
    	
    </div>
</div>
<div class="main_search">
	<form id="logForm">
	<input type="hidden" id="currentTime" name="currentTime">
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
           	<td colspan=4 style="padding-left:10px;text-align:left;">
           		
				<c:forEach var="m" items="${logList}" varStatus="index">
					<input type='checkbox' name="ms" value="${m.id }" onclick="checkLog(this)" style='margin-left:20px'
						<c:if test="${m.checked==true}" >checked</c:if> 
					>${m.name }
				</c:forEach>
           	</td>
           	<td class="search_btn">
           		<input type="button" id="clear" value="清除" onclick="clearLog()" style='margin-right:50px'/>
           	</td>
       </tr>
       <tr>
           	<td colspan=5 class="search_input">
           		<div>
					<table cellpadding="0" cellspacing="0" class="table" width="350px">
				    	<tr class="table_title02">
				        	<td width="100px">时间</td>
				            <td width="150px">来源</td>
				            <td width="150px">信息</td>
				        </tr>
				    </table>
			    </div>
				<div class="fix_table" id="logdiv" name="logdiv">
					<table id="logTable" name="logTable" cellpadding="0" cellspacing="0" class="table" width="350px">
				    </table>
				</div>
           	</td>
       </tr>
	 </table>
	 </form>
</div>

	
	
	
</div>
</div>
</body>
</html>