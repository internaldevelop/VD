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
		//散点图+告警线
		function squareWave(parentId, maxRate, minRate)
	    {
	    	var tps;
			if(maxRate<=6){
				tps=[-6,-3, 0, 3,6,9];
			}else if(maxRate<=10){
				tps=[-10,-5, 0, 5,10,15];
			}else if(maxRate<=20){
				tps=[-20,-10, 0, 10,20,30];
			}else if(maxRate<=30){
				tps=[-30,-15, 0, 15,30,45];
			}else if(maxRate<=50){
				tps=[-50,-25, 0, 25,50,75];
			}else{
				tps=[-100,-50, 0, 50,100,150];
			}
			$.ajax(
			{
				url:'<%=basePath %>testresult/queryArp.do?n='+Math.random(),
				data:{'type':4,'parentId':parentId},
				dataType:'json',
				success:function(result){
					chart = new Highcharts.Chart(
								{
									chart : {
										renderTo : 'container',
										defaultSeriesType : 'scatter',
										marginRight : 0,
										marginBottom : 25
									},
									title : {
										text : null,
										x : -20
									},
									xAxis : {
										title: {                                                                         
											enabled: false,
											text: '吞吐量'
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
											text : '延迟(%)'
										},
										tickPositions: tps,
										plotLines : [ {
							                value : minRate,
							                color : 'red',
							                width : 2,
							                label : {
							                    text : ''
							                }
							            }, {
							                value : maxRate,
							                color : 'red',
							                width : 2,
							                label : {
							                    text : ''
							                }
							            }]
									},
									exporting : {
										filename : '',
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
										name:'', 
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
