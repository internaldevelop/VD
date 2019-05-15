  var param = 0;
  //ARP 散点 图形
   function container0_init(pid)
   {
	   atrshowmodal('aaa:'+pid);
   	scatterDiagram('1', 'container0',pid,'ARP');
   }
$(function () {
	
	Highcharts.setOptions({
        exporting: {
            enabled:true
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
    
    //页签切换 start
    $("#tabs li").bind("click" ,function () {
    	var index = $(this).index();
    	if(param == 1)
    	{
    		index = 0;
    		param = 0;
    		var pid=window.parent.leftFrame.selectNodeId;
    		scatterDiagram('1', 'container0',pid,'ARP');
    	}
        var divs = $("#tabs-body > div");
        $('#tabs').children("li").attr("class", "tab-nav");//将所有选项置为未选中
        $('#tabs li:eq('+index+')').attr("class", "tab-nav-action"); //设置当前选中项为选中样式
        divs.hide();//隐藏所有选中项内容
        divs.eq(index).show(); //显示选中项对应内容
        showchar( index );
    });
    
    //页签切换 end
    var minRate,maxRate;
    $.getJSON("chart/findAlarmLevel.do",function(data) {
    	minRate=data[0];
    	maxRate=data[1];
     });
     
    function showchar(id)
    {
    	var fun = 'container'+id;
    	eval(fun)();
    }
    
    //ARP 散点 图形
    function container0()
    {
    	var pid=window.parent.leftFrame.selectNodeId;
    	if(pid!=null){
	    	var ifr = document.getElementById('container01');
			if (ifr !=null) {
				var win = ifr.window || ifr.contentWindow;
				win.scatterDiagram('1', 'container0',pid,'ARP','ARP');
			}
		}
    }
    
    //ICMP 图形
    function container1()
    {	
    	var pid=window.parent.leftFrame.selectNodeId;
    	if(pid!=null){
	    	var ifr = document.getElementById('container11');
			var win = ifr.window || ifr.contentWindow;
			win.scatterDiagram('2', 'container11',pid,'ICMP','ICMP');
		}
    }
    
    //TCP 图形
    function container2()
    {
    	var pid=window.parent.leftFrame.selectNodeId;
    	if(pid!=null){
	    	var ifr = document.getElementById('container21');
			var win = ifr.window || ifr.contentWindow;
			win.linechart('3', 'container21',pid,'端口数量','TCP');
		}
    }
    
    //离散(方波,散点+告警) 
    function container3()
    {	
    	var pid=window.parent.leftFrame.selectNodeId;
    	if(pid!=null){
    		var ifr = document.getElementById('container31');
			var win = ifr.window || ifr.contentWindow;
			win.squareWave(pid);
			
    		//squareWave('4', 'container3',pid,'流量(%)');
    		var ifr = document.getElementById('container32');
			var win = ifr.window || ifr.contentWindow;
			win.squareWave(pid, maxRate, minRate);
    	}
    }
    
    //端口0 图形
    function container4()
    {
    	var pid=window.parent.leftFrame.selectNodeId;
    	if(pid!=null){
    		//linechart('14', 'container41',pid,'报文个数');
    		//linechart('13', 'container42',pid,'报文个数');
    		var ifr = document.getElementById('container41');
			var win = ifr.window || ifr.contentWindow;
			win.linechart('14', 'container41',pid,'报文个数','Eth0');
			
			var ifr = document.getElementById('container42');
			var win = ifr.window || ifr.contentWindow;
			win.linechart('13', 'container42',pid,'报文个数','Eth0');
    	}
    }
    
    //端口1 图形
    function container5()
    {
    	var pid=window.parent.leftFrame.selectNodeId;
    	if(pid!=null){
    		var ifr = document.getElementById('container51');
			var win = ifr.window || ifr.contentWindow;
			win.linechart('16', 'container51',pid,'报文个数','Eth1');
			
			var ifr = document.getElementById('container52');
			var win = ifr.window || ifr.contentWindow;
			win.linechart('15', 'container52',pid,'报文个数','Eth1');
    	}
    }

    //方波图 type:类别  id:页面显示位置
	function squareWave(type, id,parentId)
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
	
	    $.getJSON('testresult/queryArp.do?n='+Math.random()+'&type=4&parentId='+parentId, function (data) {
	        // Create the chart
	        $('#container32').highcharts('StockChart', {
	            rangeSelector : {
	                selected : 1
	            },
	            yAxis : {
	                title : {
	                    text : '延迟(%)'
	                },
	                opposite:false,
	           		tickPositions: tps,
	                plotLines : [{
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
				legend: {                                                               
		            enabled: false                                                      
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
		         tooltip: {
	            	headerFormat: '',   
		            pointFormatter: function()
		            {
		            	return'<strong>时间: </strong>'+Highcharts.dateFormat('%H:%M:%S',this.x)
        						+'<br/> <strong>数值: </strong>'+this.y
		            }
				},
	            series : [{
	            	name:'', 
	                data : data,
	                dashStyle:'Dot',
	                color: 'red',
	                tooltip : {
	                    valueDecimals : 4
	                }
	            }]
	        });
	    });
	    
	}
	
    //公共的散点图 type:类别  id:页面显示位置
	function scatterDiagram(type, id,parentId,xtitle)
	{
		$.ajax(
		{
			url:'testresult/queryArp.do?n='+Math.random(),
			data:{'type':type,'parentId':parentId},
			dataType:'json', 
			success:function(result)
			{
				$('#'+id).highcharts('Scatter',{                                                             
			        chart: {                                                                             
			            zoomType: 'xy'                                                                   
			        },                                                                                   
			        title: {                                                                             
			            text: null                     
			        },                                                                                   
			        xAxis: {                                                                             
			            title: {                                                                         
			                enabled: false,                                                               
			                text: '吞吐量'
			            },
			            startOnTick: true,
			            endOnTick: true,
			            showLastLabel: true,
			            labels: {  
	                        step: 4,   
	                        formatter: function () {
	                            return Highcharts.dateFormat('%H:%M:%S', this.value); 
	                        }
	                    }
			        },                                                                                   
			        yAxis: {                                                                             
			            title: {                                                                         
			                text: '延迟(毫秒)' ,
			                rotation: 270                                                  
			            }                                                                             
			        },                                                                                   
			        plotOptions: {                                                                       
			            scatter: {                                                                       
			                marker: {                                                                    
			                    radius: 3,
			                    states: {                                                                
			                        hover: {                                                             
			                            enabled: true,                                                   
			                            fillColor: 'rgba(223, 83, 83, .5)'
			                        }                                                                    
			                    }                                                                        
			                },                                                                           
			                states: {                                                                    
			                    hover: {                                                                 
			                        marker: {                                                            
			                            enabled: false                                                   
			                        },
			                        fillColor: 'blue'                                                                    
			                    }                                                                        
			                },                                                                           
			                tooltip: {                                                                   
			                    headerFormat: '',   
			                    pointFormatter: function()
					            {
					            	return'<strong>时间: </strong>'+ Highcharts.dateFormat('%H:%M:%S',this.x)
		         						+'<br/> <strong>数值: </strong>'+this.y
					            }
			                }                                                                            
			            }                                                                                
			        },
                    rangeSelector : {
                        enabled: false
                    },
                    scrollbar: {
                        enabled: false
                    },
			        legend: {                                                               
			            enabled: false                                                      
			        },                                                                      
			        credits: {
			            enabled: false
			        },                                                                                 
			        series: [{    
			        	name:xtitle,                                      
			            color: 'rgba(119, 152, 191, .5)',                                                  
			            data: result
			        }]
			    }); 
			}
		});
	} 
    //公共的折线图 type:类别  id:页面显示位置
    function linechart(type, id,parentId,xtitle)
    {
		$.ajax(
		{
			url:'testresult/queryArp.do?n='+Math.random(),
			data:{'type':type,'parentId':parentId},
			dataType:'json',
			success:function(result)
			{
				//var tcpx = result.tcpx;
				//var tcpy = result.tcpy;
				$('#'+id).highcharts('StockChart',{
				title: {
				    text: null,
				    x: -20 //center
				},
				rangeSelector: {
		            enabled: false
		        },
		        scrollbar: {
		            enabled: false
		        },
				xAxis: {
				    title: {                                                                         
						enabled: false,
						text: '吞吐量'
				    },
				    //tickPixelInterval:1000,
				    labels: {
						step: 4,
						formatter: function () {
						    return Highcharts.dateFormat('%H:%M:%S', this.value); 
						}
				    }
				},
				yAxis : {
	                title : {
	                    text : xtitle
	                },
	                opposite:false
	            },
				tooltip: {
					headerFormat: '',
				    pointFormatter: function()
				    {
					return'<strong>时间: </strong>'+Highcharts.dateFormat('%H:%M:%S',this.x)
							+'<br/> <strong>数值: </strong>'+this.y
				    }
				},
			       legend: {                                                               
				    enabled: false                                                      
				},                                                                      
				credits: {
				    enabled: false
				}, 
				series: [{
					name:xtitle, 
				    data: result
				}]
			    });
			}
		});
    }
    
    //页面默认初始化 数据 加载 第一个散点图
    container0();
});

