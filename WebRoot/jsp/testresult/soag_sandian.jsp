<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript"
	src="<%=path %>/jsp/testresult/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript"
	src="<%=path %>/jsp/testresult/js/highstock.1.2.5.js"></script>
<script type="text/javascript"
	src="<%=path %>/jsp/testresult/js/exporting.1.2.5.js"></script>
<script type="text/javascript">
		
		
		$(function()
		{
			Highcharts.setOptions({
		        exporting: {
		            enabled:true,
		            width: '20px'
				},
				global: { useUTC: false },
				lang: {
		           　 printChart:"打印图表",
		              downloadJPEG: "下载JPEG 图片" , 
		              downloadPDF: "下载PDF文档"  ,
		              downloadPNG: "下载PNG 图片"  ,
		              downloadSVG: "下载SVG 矢量图" , 
		              exportButtonTitle: "导出图片" 
		        }
		    });
		    
		});
		//公共的散点图
	    function scatterDiagram(type, id, parentId, xtitle,title)
	    {
			$.ajax(
			{
				url:'<%=basePath %>testresult/queryArp.do?n='+Math.random(),
				data:{'type':type,'parentId':parentId},
				dataType:'json',
				success:function(result){
					chart = new Highcharts.Chart(
								{
									chart : {
										renderTo : 'container',
										defaultSeriesType : 'scatter',
										marginRight : 0
									},
									title : {
										text : null,
										x : -20
									},
									xAxis : {
										title: {                                                                         
											enabled: false,
											text: ''
									    },
									    labels: {
											step: 4,
											formatter: function () {
											    return Highcharts.dateFormat('%H:%M:%S', this.value); 
											}
									    }
									},
									yAxis : {
										title : {
											text : '延迟(毫秒)'
										},
										plotLines : [ {
											value : 0,
											width : 1,
											color : '#808080'
										} ]
									},
									exporting : {
										filename : title,
										url : '<%=basePath %>testresult/SaveAsImage.do'
									},
									tooltip : {
										formatter : function() {
											return this.y;
										}
									},
									rangeSelector: {
								           enabled: false
							       },
							       scrollbar: {
							           enabled: false
							       },
							       navigator: {
							           enabled: true
							       },
							       legend: {                                                               
							            enabled: false                                                      
							        },
									credits: {
									     enabled: false
									},
									series : [{
										name:xtitle, 
									    data: result
									}]
								});
				}
			});
			
	    }
		
	</script>
</head>
<body>
	<div id="container" style="height: 200px"></div>
</body>
</html>
