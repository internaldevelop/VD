<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.wnt.web.testexecute.entry.*"%>
<%@ page import="org.wnt.core.ehcache.*"%>
<%@ page import="com.wnt.web.testexecute.controller.*"%>
<%@ page import="common.*"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort();
	String path = request.getContextPath();
	String basePath1 = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="/commons/jsp/title.jsp" />
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<style type="text/css">
body {
	margin: 0;
	font-size: 12px;
	line-height: 100%;
	font-family: Arial, sans-serif;
}

.background {
	display: block;
	width: 100%;
	height: 100%;
	opacity: 0.4;
	filter: alpha(opacity = 40);
	background: while;
	position: absolute;
	top: 0;
	left: 0;
	z-index: 2000;
}

.progressBar {
	border: solid 2px #86A5AD;
	z-index: 999;
	background: white url(/VD/images/loading/loading.gif) no-repeat 10px
		10px;
}

.progressBar {
	display: block;
	width: 148px;
	height: 28px;
	position: fixed;
	top: 50%;
	left: 50%;
	margin-left: -74px;
	margin-top: -14px;
	padding: 10px 10px 10px 50px;
	text-align: left;
	line-height: 27px;
	font-weight: bold;
	position: absolute;
	z-index: 2001;
}
</style>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/pdfobject.js"></script>
<style type="text/css">
#bg {
	display: none;
	position: absolute;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 100%;
	background-color: #DCDCDC;
	z-index: 1001;
	-moz-opacity: 0.7;
	opacity: .70;
	filter: alpha(opacity = 70);
}
</style>

<script>
	// 基本路径
	var basePath = '<%=basePath%>/VD/pdf/appsecurity/';
	var basePath1 = '<%=basePath1%>';
	// 设置宽高
	$(function(){
		var w = $(document).width();
		var h = $(document).height();
		$("#showpdf").css("width", w).css("height", h-1);
	});
	
	//读取pdf流, 用于显示pdf
	$(function()
	{
		// 加载数的第一个节点的PDF
		if($('li #treeDemo_1')){
			$('li #treeDemo_1').click();
		}
	});
	
	window.addEventListener('message',function(e){
            var url  = basePath+e.data;
       		checkfile(url);
       },false);
	
	// 左树点击获取名称
	function leftshowpdf(id)
	{
		var url  = basePath+id+".pdf";
        checkfile(url);
	}
	
	// 检查若没有则生成文件
	function checkfile(url)
	{
		var ajaxbg = $("#background,#progressBar");
		$.ajax(
		{
			url:basePath1+'apptestResult/showPdf.do',
			data:'path='+url,
			dataType:'json',
			beforeSend:function()
			{
				ajaxbg.show();
			},
			complete:function()
			{
				ajaxbg.hide();
			},
			success:function(result)
			{
				if(result.success)
				{
					$("#showpdf").show();
					showpdf(url);
					parent.leftFrame.$('#exprotPdf').attr('disabled', null);
				}
				else
				{
					$("#showpdf").hide();
					$("#progressBarError").show();
					setTimeout('$("#progressBarError").hide();',3000);
					parent.leftFrame.$('#exprotPdf').attr('disabled', true);
				}
			}
		});
	}
	
	var w = $(document).width();
	var h = $(document).height();
	$("#showpdf").css("width", w).css("height", h-40);
	$("#showpdf").css("width", w).css("height", h-40);
	//var count = 0;
	function showpdf(url){
		// 下面代码都是处理IE浏览器的情况 
  		if (window.ActiveXObject || "ActiveXObject" in window)
  		{
			//判断是否为IE浏览器，"ActiveXObject" in window判断是否为IE11
	        //判断是否安装了adobe Reader
	        for (x = 2; x < 10; x++) {
	          	try {
	            	oAcro = eval("new ActiveXObject('PDF.PdfCtrl." + x + "');");
	            	if (oAcro) {
	              		flag = true;
	            	}
	          	} catch (e) {
	            	flag = false;
	          	}
	        }
			try {
	          	oAcro4 = new ActiveXObject('PDF.PdfCtrl.1');
	          	if (oAcro4) {
	            	flag = true;
	          	}
	        } catch (e) {
	          flag = false;
	        }
	        try {
				oAcro7 = new ActiveXObject('AcroPDF.PDF.1');
	          	if (oAcro7) {
	            	flag = true;
	          	}
	        } catch (e) {
	          flag = false;
	        }
	        if (flag) {
	          	new PDFObject({ url: url, pdfOpenParams: { scrollbars: '0', toolbar: '0', statusbar: '0'} }).embed("showpdf");
	        }
	        else
	        {
	        	atrshowmodal("对不起,您还没有安装PDF阅读器软件呢,为了方便预览PDF文档,请选择安装！");
	          	location = "<%=basePath1%>AdbeRdr930_zh_CN.zip";
			}
		} else {
			new PDFObject({
				url : url,
				pdfOpenParams : {
					scrollbars : '0',
					toolbar : '0',
					statusbar : '0'
				}
			}).embed("showpdf");
		}
	}
</script>
<body>
	<div id="bg"></div>
	<div id="showpdf" name="showpdf" style="height: 100%;"></div>
	<div id="background" class="background" style="display: none;"></div>
	<div id="progressBar" class="progressBar" style="display: none;">数据加载中，请稍等...</div>
	<div id="progressBarError" class="progressBar" style="display: none;">PDF文件不存在...</div>
</body>
</html>