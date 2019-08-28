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
	src="<%=path %>/jsp/testresult/js/highstock.js"></script>
<script type="text/javascript"
	src="<%=path %>/jsp/testresult/js/exporting.js"></script>
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
		
	    //方波图 type:类别  id:页面显示位置
		function squareWave(parentId)
		{
			var tps1;
			tps1=[-2,-1, 0, 1,2];
		    $.getJSON('<%=basePath %>testresult/queryArp.do?type=4&n='+Math.random()+'&parentId='+parentId, function (data) {
		        // Create the chart
		        $('#container').highcharts('StockChart', {
		            rangeSelector: {
		                selected: 1
		            },
		            title: {
		                text: null
		            },
		            legend: {                                                               
			            enabled: true                                                      
			        },                                                                      
			        credits: {
			            enabled: false
			        },
			        rangeSelector: {
			            enabled: false
			        },
			        scrollbar: {
			            enabled: false
			        },
			        navigator: {
			            enabled: false
			        },
			        exporting : {
						enabled: false
					},
		            series: [{
		                name: '延迟时间',
		                data: (function() {                                                 
		                   var data2 = [],                                                  
		                        i;                                                          
		                   	var fbnum=0;	                                                                         
		                    for (i = -19; i <= 0; i++) {                                    
		                        data2.push({                                                 
		                            x: fbnum++,                                     
		                            y: 1                                        
		                        });                                                         
		                        data2.push({                                                 
		                            x: fbnum++,                                     
		                            y: 1                                        
		                        });                                                         
		                        data2.push({                                                 
		                            x: fbnum++,                                     
		                            y: -1                                        
		                        });                                                         
		                        data2.push({                                                 
		                            x: fbnum++,                                     
		                            y: -1                                        
		                        });                                                         
		                    }                                                               
		                    return data2;                                                      
		              	})(),
		                step: true
		            }],
		            tooltip: {
		            	headerFormat: '',      
			            pointFormatter: function()
			            {
			            	return'<strong>时间: </strong>'+Highcharts.dateFormat('%H:%M:%S',this.x)
	        						+'<br/> <strong>数值: </strong>'+this.y
			            }
					},
					yAxis: {                                                                             
			            title: {                                                                         
			                text: '数值'                                                          
			            },
			            opposite:false,
			            tickPositions: tps1,
			            showLastLabel: true                                                                               
			        },
		            xAxis: {         
		            	enabled: false,                                                                    
			            startOnTick: true,
			            endOnTick: true,
			            showLastLabel: true,
			            labels: {  
	                        step: 4,   
	                        formatter: function () {
	                            return ""; 
	                        }
	                    }
			        }
		        });
		    });
	    }
		
	</script>
</head>
<body>
	<div id="container" style="height: 200px"></div>
</body>
</html>
