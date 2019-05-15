Array.prototype.clear = function() {
	this.length = 0;
}
Array.prototype.insertAt = function(index, obj) {
	this.splice(index, 0, obj);
}
Array.prototype.removeAt = function(index) {
	this.splice(index, 1);
}
Array.prototype.remove = function(obj) {
	var index = this.indexOf(obj);
	if (index >= 0) {
		this.removeAt(index);
	}
}
Date.prototype.pattern = function (fmt) { //author: meizz
	var o = {
	"M+": this.getMonth() + 1, //月份
	"d+": this.getDate(), //日
	"h+": this.getHours(), //小时
	"H+": this.getHours(), //小时
	"m+": this.getMinutes(), //分
	"s+": this.getSeconds(), //秒
	"q+": Math.floor((this.getMonth() + 3) / 3), //季度
	"S": this.getMilliseconds() //毫秒
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
	if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

Highcharts.getSVG = function(charts) {
	//console.log(charts);
	var svgArr = [], top = 0, width = 0;
	$.each(charts, function(i, chart) {
		var svg = charts.getSVG();
		svg = svg.replace('<svg', '<g transform="translate(0,' + top + ')" ');
		svg = svg.replace('</svg>', '</g>');
		// svg = svg.replace('<svg', '<zg ct=' + chart.options.chartype);
		// svg = svg.replace('</svg>', '</zg>');(这段注释的代码用来生成多个图片)

		top += 400;
		svgArr.push(svg);
	});
	return '<svg height="' + 400 + '" width="' + 1500
			+ '" version="1.1" xmlns="http://www.w3.org/2000/svg">'
			+svgArr.join('') + '</svg>';
};


var pointNum = 15;
var num4 = 0;
var eth0_1_Array =[];
var eth0_2_Array =[];
var arpArray=[];
var icmpArray=[];
var eth1_1_Array=[];
var eth1_2_Array=[];
var tcpData=null;
var tcpLineInit = null, /*TCP 监视器初始化时间*/
	arp_InitTime = null,	//arp 散点监视器初始化时间
	icmp_InitTime = null,	//icmp 散点监视器初始化时间
	eth0_1_InitTime = null, /*eth0 发送 监视器 初始化时间*/
	eth0_2_InitTime = null, /*eth0 接收 监视器 初始化时间*/
	eth1_1_InitTime = null, /*Eth1 发送 监视器 初始化时间*/
	eth1_2_InitTime = null,	/*Eth1 接收 监视器 初始化时间*/
	lisan_InitTime = null,
	lisan2_InitTime = null; /*离散 监视器 初始化时间*/
	
 
$(function() {
	var date = new Date();
	$("#endDate").val(date.pattern("yyyy-MM-dd HH:mm:ss"));
	var pdate = new Date(date.getTime() -6 * 3600 * 1000);
	$("#beginDate").val(pdate.pattern("yyyy-MM-dd HH:mm:ss"));
	
	Highcharts.setOptions({
	 global: { useUTC: false }
	});

	if ($("#4").width() < 700) {
		pointNum = 10;
	}
	
	// 离散 方波图 container_4_1
	squareWave('4_1', '4', 'chk_4');
	
	// 离散 散点图 container_4_2
	// 先取得预警的阈值
	$.getJSON(basePath + "chart/findAlarmLevel.do", function(data) {
		stockChart('4_2', '4', data[0], data[1], 'chk_4');
	});

	//1500 毫秒后判断是否创建chart 图
	setTimeout(function(){
			// ARP 散点图 container_1
			if($('#1').highcharts()==null){
				// id,name,chkId
				scatterDiagram('ARP 监视器', '1', '1', 'ARP', 'chk_1');// title,type,
			}
			if($('#2').highcharts() == null){
				// ICMP 散点图 container_2
				scatterDiagram('ICMP 监视器', '2', '2', 'ICMP', 'chk_2');                      
			}
			if($('#3').highcharts() == null){
				// TCP 折线图
				tcplinechart('TCP 监视器', '3', '3', '吞吐量数值折线图', 'chk_3', '端口数量');
			}
			if($('#eth0_1').highcharts() == null){
				// Eth0 折线图 发送 container_eth0_1
				linechart('Eth0 发送 监视器', '14', 'eth0_1', '吞吐量数值折线图', 'chk_eth0', '报文个数');
			}
			if($('#eth0_2').highcharts() == null){
				// Eth0 折线图 接受 container_eth0_2
				linechart('Eth0 接收 监视器', '13', 'eth0_2', '吞吐量数值折线图', 'chk_eth0', '报文个数');
			}
			if($('#eth1_1').highcharts() == null){
				// Eth1 折线图 发送 container_eth1_1
				linechart('Eth1 发送 监视器', '16', 'eth1_1', '吞吐量数值折线图', 'chk_eth1', '报文个数');
			}
			if($('#eth1_2').highcharts() == null){
				// Eth1 折线图 接受 container_eth1_2
				linechart('Eth1 接收 监视器', '15', 'eth1_2', '吞吐量数值折线图', 'chk_eth1', '报文个数');
			}
	},1500);
	


	// 开启定时器
	// fun_timeout();
	// chart_timeout();

	// 控制离散图宽度
	$("#4_1").css("width", $("#4").width() + "px");
	$("#4_2").css("width", $("#4").width() + "px");

	if (!$("#chk_4").attr("checked")) {
		$("#4").hide();
	}
	//document.getElementById('hitoryTable').style.display='none';
	/*$("#page_form").hide();
	$("#arpcontainer").css("display","none"); 
	$("#icmpcontainer").css("display","none"); 
	$("#tcpcontainer").css("display","none"); 
	$("#etn0incontainer").css("display","none"); 
	$("#etn0outcontainer").css("display","none"); 
	$("#etn1incontainer").css("display","none"); 
	$("#etn1outcontainer").css("display","none");*/
	
	/*$(".tabs-wrap ul.tabs li").click(function(){
		var self = $(this);
		var index = $(".tabs-wrap ul.tabs li").index(this);
		self.addClass("tabs-selected").siblings().removeClass("tabs-selected");
		$(".tabs-panels .tab-panel").eq(index).addClass("tabs-selected").siblings().removeClass("tabs-selected");
		getHitoryData(self.data("type"));
	});*/
	
});
var lastId = -1;
var countNum = 0;
// 取得数据的定时器
function fun_timeout() {
	window.setInterval(function() {
		// alert(1);
		$.ajax({
			type : "POST",
			url : basePath + "chart/findData.do?lastId=" + lastId,
			data : $("#chkForm").serialize(),
			dataType : "json",
			cache:false,
			success : function(data) {
				lastId = data.lastId;
				// 展示数据
				var list = data.result;
				countNum += list.length;
				if (list.length != 0) {
					// 开始循环返回数据
					for (var k = 0; k < list.length; k++) {

						var e = list[k];

						if (e.chartType == 1) {
							// 1ARP 散点图
							a1.insertAt(a1.length, e);

						} else if (e.chartType == 2) {
							// 2ICMP 散点图
							a2.insertAt(a2.length, e);

						} else if (e.chartType == 3) {
							// 3TCP 折线图
							a3.insertAt(a3.length, e);

						} else if (e.chartType == 4) {
							// 离散
							a4.insertAt(a4.length, e);

						} else if (e.chartType == 14) {
							// Eth0 折线图 发送 eth0_1 14
							a14.insertAt(a14.length, e);

						} else if (e.chartType == 13) {
							// Eth0 折线图 接受 eth0_2 13
							a13.insertAt(a13.length, e);

						} else if (e.chartType == 16) {
							// Eth1 折线图 发送 eth1_1 16
							a16.insertAt(a16.length, e);

						} else if (e.chartType == 15) {
							// Eth0 折线图 接受 eth1_2 15
							a15.insertAt(a15.length, e);
						}
					}
				}
				// $("#info2").html("数据量："+countNum);

			},
			error : function() {
				// alert("网络错误");
			}
		});
	}, 1000);
}
function chart_timeout() {
	window.setInterval(function() {
		// 1ARP 散点图
		if (a1.length > 0) {
			// alert(a1[0].num);
			if (a1[0].num == null) {
				kc_addPoint("1", a1[0].createTime, a1[0].num);
			} else {
				kc_addPoint("1", a1[0].createTime, a1[0].num / 100);
			}
			a1.removeAt(0);
		}
		// 2ICMP 散点图
		if (a2.length > 0) {
			// alert(a2[0].num);
			if (a2[0].num == null) {
				kc_addPoint("2", a2[0].createTime, a2[0].num);
			} else {
				kc_addPoint("2", a2[0].createTime, a2[0].num / 100);
			}
			a2.removeAt(0);
		}
		// 3TCP 折线图
		if (a3.length > 0) {
			kc_addPoint("3", a3[0].createTime, a3[0].num);
			a3.removeAt(0);
		}
		// 4离散
		if (a4.length > 0) {
			// 离散 方波图container_4_1

			var chart = $('#4_1').highcharts();
			if (num4 < pointNum) {
				chart.series[0].addPoint([ fbnum++, 1 ], true, true);
				chart.series[0].addPoint([ fbnum++, -1 ], true, true);
				chart.series[0].addPoint([ fbnum, -1 ], true, true);
				num4++;
			}

			// 离散 散点图 container_4_2
			kc_addPoint('4_2', a4[0].createTime, a4[0].num);
			a4.removeAt(0);
		}

		// Eth0 折线图 发送 eth0_1 14
		if (a14.length > 0) {
			kc_addPoint("eth0_1", a14[0].createTime, a14[0].num);
			a14.removeAt(0);

		}

		// Eth0 折线图 接受 eth0_2 13
		if (a13.length > 0) {

			kc_addPoint("eth0_2", a13[0].createTime, a13[0].num);
			a13.removeAt(0);
		}

		// Eth1 折线图 发送 eth1_1 16
		if (a16.length > 0) {
			kc_addPoint("eth1_1", a16[0].createTime, a16[0].num);
			a16.removeAt(0);
		}

		// Eth0 折线图 接受 eth1_2 15
		if (a15.length > 0) {
			kc_addPoint("eth1_2", a15[0].createTime, a15[0].num);
			a15.removeAt(0);
		}

	}, 500);
}

// ++++++++++++++++++++
// 公共的散点图 type:类别 id:页面显示位置
function scatterDiagram(title, type, id, name, chkId,startTime,Yval) {

	if ($('#' + id)[0]) {
		$('#' + id).highcharts({
			chart : {
				type : 'scatter',
				animation : Highcharts.svg, // don't animate in old IE
				marginRight : 10,
				events:{
					load:function(){
						$('#' + id).data("loaded",true);
					}
				}
			},
			title : {
				text : title
			},
			xAxis : {
				tickAmount:pointNum,//刻度
				tickInterval:1000, //
				minTickInterval:0,//设置最小间距
				tickPixelInterval:80,//设置像素间隔值
				labels : {
					rotation : -45,// 竖直放
					formatter: function () {
	                    return date2str(this.value);
	                }
				}
			},
			yAxis : {
				min : 0,
				title : {
					align : 'middle',
					margin : 10,
					rotation : 270,
					text : '延迟(毫秒)'
				},
				labels : {
					rotation : -45,// 竖直放
					formatter: function () {
	                    return this.value;
	                }
				}
			},
			plotOptions : {
				scatter : {
					marker : {
						radius : 3,
						states : {
							hover : {
								enabled : true,
								fillColor : 'rgba(223, 83, 83, .5)'
							}
						}
					},
					states : {
						hover : {
							marker : {
								enabled : false
							},
							fillColor : 'blue'
						}
					},
					tooltip : {
						enabled : true,
						headerFormat : '',
						pointFormatter : function() {
							if(typeof (this.y) != "undefined"){
								return '延迟:' + this.y
							}else{
								return '延迟:'
							}
						}
					}
				}
			},
			legend : {
				enabled : false
			},
			exporting : {
				enabled : false
			},
			credits : {
				enabled : false
			},
			series : [ {
				name : name,
				data : (function(){
					var d = [];
					ct = startTime==null?(new Date()).getTime():startTime;
					for (var i = -(pointNum-1); i <= 0; i++) {
						d.push({x:ct + (i * 1000),y:i==0?Yval:null});
					}
					return d.slice(0);
				}())
			} ]
		});
		/*var chart = $('#' + id).highcharts();
		if (chart) {
			ct = (new Date()).getTime();
			for (i = -pointNum; i < 0; i++) {
				chart.series[0].addPoint([ date2str(ct + i * 1000), null ],
						true, true, true);
			}
		}*/
	}

}
// ++++++++++++++++++++
// 公共的折线图 type:类别 id:页面显示位置
function linechart(title, type, id, name, chkId, xtitle,initStartTime,Yval) {
	var str = "";
	if (type == '3') {
		str = "端口数:"
	} else {
		str = "报文个数:"
	}
	if ($('#' + id)[0]) {
		$('#' + id).highcharts({
			chart : {
				type : 'spline',
				animation : Highcharts.svg, // don't animate in old IE
				marginRight : 10,
				events:{
					load:function(){
						$('#' + id).data("loaded",true);
					}
				}
			},
			title : {
				text : title
			// center
			},
			xAxis : {
				tickAmount:pointNum,//刻度
				tickInterval:1000, //
				minTickInterval:0,//设置最小间距
				tickPixelInterval:80,//设置像素间隔值
				labels : {
					rotation : -45, // 竖直放
					formatter: function () {
	                    return date2str(this.value);
	                }
				}
			},
			yAxis : {
				min : 0,
				allowDecimals : false,
				title : {
					align : 'middle',
					margin : 10,
					rotation : 270,
					text : xtitle
				}
			},
			tooltip : {
				enabled : true,
				headerFormat : '',
				pointFormatter : function() {
					if(typeof (this.y) != "undefined"){
						return str + this.y
					}else{
						return str
					}
				}
			},
			legend : {
				enabled : false
			},
			exporting : {
				enabled : false
			},
			credits : {
				enabled : false
			},
			series : [ {
				name : name,
				data :(function(){
					var initTcpLineData = [];
					var ct = initStartTime == null?new Date().getTime():initStartTime;
					for (var i = -(pointNum-1); i <= 0; i++) {
						initTcpLineData.push({x:(ct + (i * 1000)),y:i==0?Yval:null});
					}
					return initTcpLineData.slice(0);
				}())
			} ]
		});
		/*var chart = $('#' + id).highcharts();
		if (chart) {
			var ct1 = (new Date()).getTime();
			console.log("pointNum = ",pointNum);
			for (i = -pointNum; i < 0; i++) {
				console.log("i = "+i);
				chart.series[0].addPoint([ date2str(ct1 + i * 1000), null ],
						true, false, true);
			}
		}*/
	}
}
function tcplinechart(title, type, id, name, chkId, xtitle,startTime,Yval) {
	var str = "";
	if (type == '3') {
		str = "端口数:"
	} else {
		str = "报文个数:"
	}

	if ($('#' + id)[0]) {
		$('#' + id).highcharts({
			chart : {
				type : 'line',
				animation : Highcharts.svg, // don't animate in old IE
				marginRight : 10,
				events:{
					load:function(){
						$('#' + id).data("loaded",true);
					}
				}
			},
			title : {
				text : title,
			// center
			},
			xAxis : {
				tickAmount:pointNum,//刻度
				tickInterval:1000,
				minTickInterval:0,//设置最小间距
				tickPixelInterval:80,
				labels : {
					rotation : -45,
					formatter: function () {
	                    return date2str(this.value);
	                }
				}
			},
			yAxis : {
				min : 0,
				allowDecimals : false,
				title : {
					align : 'middle',
					margin : 10,
					rotation : 270,
					text : xtitle
				}
			},
			tooltip : {
				enabled : true,
				headerFormat : '',
				pointFormatter : function() {
					if(typeof (this.y) != "undefined"){
						return str + this.y
					}else{
						return str
					}
				}
			},
			legend : {
				enabled : false
			},
			exporting : {
				enabled : false
			},
			credits : {
				enabled : false
			},
			series : [ {
				name : name,
				step:true,
				data : (function(){
					var initTcpLineData = [];
					var ct = startTime == null?new Date().getTime():startTime;
					for (var i = -(pointNum-1); i <= 0; i++) {
						initTcpLineData.push({x:ct + (i * 1000),y:i==0?Yval:null});
					}
					return initTcpLineData.slice(0);
				}())
			} ]
		});
	}
}
// ++++++++++++++++++++
// ++++++++++++++++++++
// 方波图
var fbnum = 0;
function squareWave(id, type, chkId) {
	var tps1;
	tps1 = [0, 1, 2 ];
	if ($('#' + id)[0]) {
		$('#' + id).highcharts(
				/*'StockChart',*/
				{
					chart : {
						type : 'line',
						animation : Highcharts.svg,
						marginRight : 10,
						events:{
							load:function(){
								$('#' + id).data("loaded",true);
							}
						}

					},
					title : {
						text : '离散 监视器'
					},
					tooltip : {
						enabled : false,
						headerFormat : '<b>方波图</b><br>',
						pointFormatter : function() {
							return '<strong>日期: </strong>'
									+ Highcharts.dateFormat('%H:%M:%S', this.x)
									+ '<br/> <strong>数值: </strong>' + this.y
						}
					},
					xAxis : {
						labels : {
							enabled : false
						},
						tickLength: 0
					},
					yAxis : {
						title : {
							text : '数值'
						},
						showLastLabel : true,
						opposite : false,
						tickPositions : tps1
					},
					legend : {
						enabled : false
					},
					exporting : {
						enabled : false
					},
					rangeSelector : {
						enabled : false
					},
					navigator : {
						enabled : false
					},
					credits : {
						enabled : false
					},
					series : [ {
						name : '延迟时间',
						step : true,
						data : (function() {
							var data = [], i;
							for (i = -(pointNum * 3 - 1); i <= 0; i++) {
								data.push({
									x : fbnum++,
									y : null
								});
							}
							return data;
						})()
					} ]
				});
	}
	; 
}
var lxnum = 0;
// 离散监视器 散点图+警告
function stockChart(id, type, minRate, maxRate, chkId) {
	var tps;
	if (maxRate <= 6) {
		tps = [ -6, -3, 0, 3, 6, 9 ];
	} else if (maxRate <= 10) {
		tps = [ -10, -5, 0, 5, 10, 15 ];
	} else if (maxRate <= 20) {
		tps = [ -20, -10, 0, 10, 20, 30 ];
	} else if (maxRate <= 30) {
		tps = [ -30, -15, 0, 15, 30, 45 ];
	} else if (maxRate <= 50) {
		tps = [ -50, -25, 0, 25, 50, 75 ];
	} else {
		tps = [ -100, -50, 0, 50, 100, 150 ];
	}

	if ($('#' + id)[0]) {
		$('#' + id).highcharts({
			rangeSelector : {
				selected : 1
			},
			chart : {
				type : 'scatter',
				animation : Highcharts.svg,
				events:{
					load:function(){
						$('#' + id).data("loaded",true);
					}
				}
			},
			title : {
				text : "",
				enabled : false
			},
			xAxis : {
				tickAmount:pointNum,//刻度
				tickInterval:1000,
				minTickInterval:0,//设置最小间距
				tickPixelInterval:80,
				labels : {
					rotation : -45,	// 竖直放
					formatter: function () {
	                    return date2str(this.value);
	                }
				},
				categories : []
			},
			yAxis : {
				title : {
					text : '延迟(%)'
				},
				opposite : false,
				tickPositions : tps,
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
				} ]
			},
			tooltip : {
				enabled : true,
				headerFormat : '',
				pointFormatter : function() {
					return '延迟 :' + this.y
				}
			},
			legend : {
				enabled : false
			},
			exporting : {
				enabled : false
			},
			rangeSelector : {
				enabled : false
			},
			navigator : {
				enabled : false
			},
			credits : {
				enabled : false
			},
			plotOptions : {
				scatter : {
					marker : {
						radius : 3,
						states : {
							hover : {
								enabled : true,
								fillColor : 'rgba(223, 83, 83, .5)'
							}
						}
					},
					states : {
						hover : {
							marker : {
								enabled : false
							},
							fillColor : 'blue'
						}
					},
					tooltip : {
						enabled : true,
						headerFormat : '',
						pointFormatter : function() {
							return '延迟 :' + this.y
						}
					}
				}
			},
			series : [ {
				name : '',
				data : (function(){
					var newData = [];
					ct = (new Date()).getTime();
					for (var i = -(pointNum-1); i <= 0; i++) {
						newData.push({x:ct + (i * 1000),y:null});
					}
					return newData.slice(0);
				})()
			} ]
		});
	}
}
// 日期转换成字符串
function date2str(x) {
	var newTime = new Date(x);
	var h = newTime.getHours() < 10?"0"+newTime.getHours():newTime.getHours();
	var m = newTime.getMinutes() < 10?"0"+newTime.getMinutes():newTime.getMinutes();
	var s = newTime.getSeconds() < 10?"0"+newTime.getSeconds():newTime.getSeconds();
	return h + ":" + m + ":"+ s;
}

/**
 * 扩展时间时间格式化方法
 */
Date.prototype.format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "H+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}


function kc_addPoint(chartId, x, y) {
	var chart = $('#' + chartId).highcharts();
	chart.series[0].addPoint([ date2str(x), y ], true, true, true);
}
var oldCharts = {};
function so_addPoint(chartId,createtime,num,timestamp) {
	if(chartId == null){
		return ;
	}
	var loaded = $('#' + chartId).data("loaded");
	var chart = $('#' + chartId).highcharts();
	
	var tttttime = timestamp;
	if(tttttime == null){
		tttttime = Date.parse(createtime);
	}
	
	if (typeof (chart) != "undefined") {
		if(oldCharts[chartId]){
			var oldData = oldCharts[chartId];
			var prevtime = oldData[0];//获取上一次追加节点是的时间
			var difference = tttttime - prevtime;//计算上次与这次的时间差
			var tmp = Math.ceil(difference/1000);
			if(difference == 0){
				return chart;
			}
			if(tmp > 1){
				if(loaded){
					if(tmp < pointNum){
						//if(oldData[1]!=null){
							for(var i = 1;i < tmp;i++){
								chart.series[0].addPoint([prevtime+(i*1000), oldData[1]], false, true, true);
							}
						//}
					}else{
						//window.location.reload();
						return chart;
					}
				}
			}
		}
		if(loaded){
			chart.series[0].addPoint([tttttime, num], false, true, true);
			chart.redraw();
		}
	}else{
		chart = initChartInfo(chartId,tttttime,num);
	}
	oldCharts[chartId] = [tttttime,num];//保存这次追加节点的时间
	return chart;
}

function initChartInfo(chartId,tttttime,num){
	try{
		switch (chartId) {
			case "1":
				scatterDiagram('ARP 监视器', '1', '1', 'ARP', 'chk_1',tttttime,num);// title,type,
				break;
			case "2":
				scatterDiagram('ICMP 监视器', '2', '2', 'ICMP', 'chk_2',tttttime,num); 
				break;
			case "3":
				tcplinechart('TCP 监视器', '3', '3', '吞吐量数值折线图', 'chk_3', '端口数量',tttttime,num);
				break;
			case "4":
				// 离散 方波图 container_4_1
				squareWave('4_1', '4', 'chk_4');
				
				$.getJSON(basePath + "chart/findAlarmLevel.do", function(data) {
					stockChart('4_2', '4', data[0], data[1], 'chk_4');
				});
				break;
			case "eth0_2":
				linechart('Eth0 接收 监视器', '13', 'eth0_2', '吞吐量数值折线图', 'chk_eth0', '报文个数',tttttime,num);
				break;
			case "eth0_1":
				linechart('Eth0 发送 监视器', '14', 'eth0_1', '吞吐量数值折线图', 'chk_eth0', '报文个数',tttttime,num);
				break;
			case "eth1_2":
				linechart('Eth1 接收 监视器', '15', 'eth1_2', '吞吐量数值折线图', 'chk_eth1', '报文个数',tttttime,num);
				break;
			case "eth1_1":
				linechart('Eth1 发送 监视器', '16', 'eth1_1', '吞吐量数值折线图', 'chk_eth1', '报文个数',tttttime,num);
				break;
		}
	}catch(er){
		
	}
	return $('#' + chartId).highcharts();
} 

var execTimedInterval = null;
var isSocketCon = false;//判断socket连接是否正常false为不正常

/**
 * 执行服务器端发送监听如果一定时间没有发送 
 * 经过讨论不需要了
 */
/*function execTimed(){
	var tmpOlds = {}; //预定于下一次执行的数据
	var maxNum = 10;//执行最大数
	var maxExecNum = 300; //3分钟我结束数据停止执行 100 耗码执行1次，执行10次是1秒，执行1分钟60 秒，执行3分钟是 （3*60*10 = 1800）
	var execNum = 0; //执行次数
	execTimedInterval = setInterval(function(){
		execNum++;//执行次数加1
		if(isSocketCon==false){
			//执行次数是否等于最大执行次数
			if(execNum == maxExecNum){
				clearInterval(execTimedInterval);
				execTimedInterval = null;
				return null;
			}
			for(var oid in oldCharts){
				var tmpOld = tmpOlds[oid]; //获取对应的图表下一次执行的数据
				var old = oldCharts[oid];  //获取已经执行图表数据
				if(old){
					var num = 0;
					if(tmpOld){
						num = tmpOld[1];
						if(tmpOld[0] > old[0]){
							if(num < maxNum){
								num++;
							}else{
								if(oid != 1 && oid != 2){
									so_addPoint(oid,(new Date(tmpOld[0])).format("yyyy-MM-dd hh:mm:ss"),old[1]);
									oldCharts[oid][0] = tmpOld[0];
								}
								num = 0;
							}
						}else{//等预定于下一次
							tmpOlds[oid][0] = old[0];
							execNum = 0;
							num = 0;
						}
					}
					tmpOlds[oid] = [old[0]+1000,num];
				}
			}
		}else{
			if(execNum%maxNum==0){
				isSocketCon = false;
			}
		} 
	},100);
}*/

function so_addHistorypoint(chartId,createtime,num) {
	var chart = $('#' + chartId).highcharts();
	if (typeof (chart) != "undefined") {
		time = createtime.split(" ")[0];
		chart.series[0].addPoint([time, num ], true, true, true);
	}
}

var socketIndex = 0;
window.parent.sendConnect();
window.parent.soutput = function soutput(datas) {
	if(datas!=null){
	for(var key in datas){
		try{
		var data = datas[key];
		switch (key) {
			case "1":
				if(arp_InitTime == null){
					arp_InitTime = Date.parse(data.createtime);
					scatterDiagram('ARP 监视器', '1', '1', 'ARP', 'chk_1',arp_InitTime,(data.num1 == null?data.num1:(data.num1)));// title,type,
				}else{
					var arp_chart = null;
					if (data.num1 == null) {
						arp_chart=so_addPoint("1", data.createtime,data.num1);                            
						arpArray.push(data.num1);
						var max=getYFloatExtremesParams(arpArray,icmpArray);
						var icmp_chart = $('#2').highcharts();
						if(typeof (icmp_chart) != "undefined"){
							icmp_chart.yAxis[0].setExtremes(0,max,true,true);
						}
						if(typeof (arp_chart) != "undefined"){
							arp_chart.yAxis[0].setExtremes(0,max,true,true);
						}
					} else {
						arp_chart=so_addPoint("1", data.createtime,data.num1);
						arpArray.push(data.num1);
						var max=getYFloatExtremesParams(arpArray,icmpArray);
						console.log(max);
						var icmp_chart = $('#2').highcharts();
						if(typeof (icmp_chart) != "undefined"){
							icmp_chart.yAxis[0].setExtremes(0,max,true,true);
						}
						if(typeof (arp_chart) != "undefined"){
							arp_chart.yAxis[0].setExtremes(0,max,true,true);
						}
					}
				}
				break;
			case "2":
				if(icmp_InitTime == null){
					icmp_InitTime = Date.parse(data.createtime);
					scatterDiagram('ICMP 监视器', '2', '2', 'ICMP', 'chk_2',icmp_InitTime,(data.num1 == null?data.num1:(data.num1)));    
				}else{
					var icmp_chart = null;
					if (data.num1 == null) {
						icmp_chart = so_addPoint("2", data.createtime,data.num1);
						icmpArray.push(data.num1);
						var max=getYFloatExtremesParams(icmpArray,arpArray);
						var arp_chart = $('#1').highcharts();
						if(typeof (arp_chart) != "undefined"){
							arp_chart.yAxis[0].setExtremes(0,max,true,true);
						}
						if(typeof (icmp_chart) != "undefined"){
							icmp_chart.yAxis[0].setExtremes(0,max,true,true);
						}
					} else {
						icmp_chart = so_addPoint("2", data.createtime,data.num1);
						icmpArray.push(data.num1);
						var max=getYFloatExtremesParams(icmpArray,arpArray);
						var arp_chart = $('#1').highcharts();
						if(typeof (arp_chart) != "undefined"){
							arp_chart.yAxis[0].setExtremes(0,max,true,true);
						}
						if(typeof (icmp_chart) != "undefined"){
							icmp_chart.yAxis[0].setExtremes(0,max,true,true);
						}
					}
				}
				break;
			case "3":
				if(tcpLineInit == null){
					tcpLineInit = Date.parse(data.createtime);
					tcplinechart('TCP 监视器', '3', '3', '吞吐量数值折线图', 'chk_3', '端口数量',tcpLineInit,data.num);
				}else{
					var tcp_chart=so_addPoint("3", data.createtime,data.num);
				}
				break;
			case "13":
				if(eth0_2_InitTime == null){
					eth0_2_InitTime = Date.parse(data.createtime);
					linechart('Eth0 接收 监视器', '13', 'eth0_2', '吞吐量数值折线图', 'chk_eth0', '报文个数',eth0_2_InitTime,data.num);
				}else{
					var eth0_2_chart =so_addPoint("eth0_2", data.createtime,data.num);
					eth0_2_Array.push(data.num);
					var max=getYExtremesParams(eth0_2_Array,eth0_1_Array);				
					var eth0_1_chart = $('#eth0_1').highcharts();
					if(typeof (eth0_1_chart) != "undefined"){
						eth0_1_chart.yAxis[0].setExtremes(0,max,true,true);
					}
					if(typeof (eth0_2_chart) != "undefined"){
						eth0_2_chart.yAxis[0].setExtremes(0,max,true,true);
					}
				}
				break;
			case "14":
				if(eth0_1_InitTime == null){
					eth0_1_InitTime = Date.parse(data.createtime);
					linechart('Eth0 发送 监视器', '14', 'eth0_1', '吞吐量数值折线图', 'chk_eth0', '报文个数',eth0_1_InitTime,data.num);
				}else{
					var eth0_1_chart=so_addPoint("eth0_1",data.createtime, data.num);
					eth0_1_Array.push(data.num);
					var max=getYExtremesParams(eth0_1_Array,eth0_2_Array);
					var eth0_2_chart = $('#eth0_2').highcharts();
					if(typeof (eth0_2_chart) != "undefined"){
						eth0_2_chart.yAxis[0].setExtremes(0,max,true,true);
					}  
					if(typeof (eth0_1_chart) != "undefined"){
						eth0_1_chart.yAxis[0].setExtremes(0,max,true,true);
					}
				}
				break;
			case "15":
				if(eth1_2_InitTime == null){
					eth1_2_InitTime = Date.parse(data.createtime);
					linechart('Eth1 接收 监视器', '15', 'eth1_2', '吞吐量数值折线图', 'chk_eth1', '报文个数',eth0_1_InitTime,data.num);
				}else{
					var eth1_2_chart=so_addPoint("eth1_2",data.createtime, data.num);
					eth1_2_Array.push(data.num);
					var max=getYExtremesParams(eth1_2_Array,eth1_1_Array);
					var eth1_1_chart = $('#eth1_1').highcharts();
					if(typeof (eth1_1_chart) != "undefined"){
						eth1_1_chart.yAxis[0].setExtremes(0,max,true,true);
					}
					if(typeof (eth1_2_chart) != "undefined"){
						eth1_2_chart.yAxis[0].setExtremes(0,max,true,true);
					}
				}
				break;
			case "16":
				if(eth1_1_InitTime == null){
					eth1_1_InitTime = Date.parse(data.createtime);
					linechart('Eth1 发送 监视器', '16', 'eth1_1', '吞吐量数值折线图', 'chk_eth1', '报文个数',eth1_1_InitTime,data.num);
				}else{
					var eth1_1_chart=so_addPoint("eth1_1",data.createtime, data.num);
					eth1_1_Array.push(data.num);
					var max=getYExtremesParams(eth1_1_Array,eth1_2_Array);
					var eth1_2_chart = $('#eth1_2').highcharts();
					if(typeof (eth1_2_chart) != "undefined"){
						eth1_2_chart.yAxis[0].setExtremes(0,max,true,true);
					}
					if(typeof (eth1_1_chart) != "undefined"){
						eth1_1_chart.yAxis[0].setExtremes(0,max,true,true); 
					}
				}
				break;
			case "4":
				var chart = $('#4_1').highcharts();
				if(typeof (chart) != "undefined"){
					if (num4 < pointNum) {
						chart.series[0].addPoint([ fbnum++, 1 ], true, true);
						chart.series[0].addPoint([ fbnum++, -1 ], true, true);
						chart.series[0].addPoint([ fbnum, -1 ], true, true);
						num4++;
					}
					// 离散 散点图 container_4_2
					if(typeof (data.num) == "undefined"){
						so_addPoint('4_2', data.createtime,0);
					}else{
						so_addPoint('4_2', data.createtime,data.num);
					}
				}
				break;
			}
		}catch(er){
			
		}
		}
	}
}


function getYExtremesParams(numArray,otherArray){
	if(numArray.length > pointNum){
		numArray.shift();
	}
	allArray=[];
	allArray.push(numArray);
	allArray.push(otherArray);
    return getExtremes(allArray);
}
function getExtremes(numArray){
	var max = getArrayMaxVal(numArray[0]);
	var max2 = getArrayMaxVal(numArray[1]); 
	if(max < max2){
		max = max2;
	}
	return (Math.round(max/5)+1)*5;
}

//function getExtremes(numArray){
//	var max = getArrayMaxVal(numArray[0]);
//	var max2 = getArrayMaxVal(numArray[1]); 
//	if(max < max2){
//		max = max2;
//	}
///	if(max == -Number.MAX_VALUE){
//		max = 0;
//	};
//	if(max > 2){
//		return (Math.round(max/5)+1)*5;
//	}else if(max > 0.1 && max < 2){
//		return ((Math.round(max*10/5)+1)*5)/10;
//	}else if(max > 0.01 && max < 0.2){
//		return ((Math.round(max*100/5)+1)*5)/100;
//	}else if(max < 0.01 ){
//		return ((Math.round(max*1000/5)+1)*5)/1000;
//	}
//}

function getYFloatExtremesParams(numArray,otherArray){
	if(numArray.length > pointNum){
		numArray.shift();
	}
	allArray=[];
	allArray.push(numArray);
	allArray.push(otherArray);
	return getFloatExtremes(allArray);
}

function getFloatExtremes(numArray){
	var maxTemp = getArrayMaxVal(numArray[0]);
	var max2 = getArrayMaxVal(numArray[1]); 
	if(maxTemp < max2){
		maxTemp = max2;
	}
	var max=parseInt((maxTemp+0.1)*100);
	return (((max/10)/10));
}


/**
 * 获取数组的最大值
 * @param arr
 * @returns
 */
function getArrayMaxVal(arr){
	if(arr instanceof Array){
		var max = -Number.MAX_VALUE;
		for(var i = 0;i < arr.length;i++){
			if(arr[i]!=null && arr[i] > max){
				max = arr[i];
			}
		}
		return max;
	}else{
		throw "getMaxval argument is not an array (getMaxval 方法的参数不是数组)";
	}
}

function closeHitory(){
	$(".historyBox").hide();
}

//计算天数差的函数，通用  
function  DateDiff(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
    var  aDate,  oDate1,  oDate2,  iDays  
    aDate  =  sDate1.split("-")  
    oDate1  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0])    //转换为12-18-2006格式  
    aDate  =  sDate2.split("-")  
    oDate2  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0])  
    iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24)    //把相差的毫秒数转换为天数  
    return  iDays  
} 
function trim(str){ // 删除左右两端的空格
	if(str){
	    	return str.replace(/(^\s*)|(\s*$)/g, "");  
	     }
	return "";
}



function btnSearch(){
	var now =formatForHistory(new Date().getTime(),"yyyy-MM-dd HH:mm:ss");
	if(trim($("#beginDate").val())==""||trim($("#endDate").val())==""){
		atrshowmodal("开始时间和结束时间不能有空！");
		return false;
	}
	var sstart =$("#beginDate").val();
	var start =$("#beginDate").val().split(" ")[0];
	var eend = $("#endDate").val();
	var end = $("#endDate").val().split(" ")[0];
	var hourse=Math.abs(sstart-eend)/1000/60/60;
	var nowT = now.split(" ")[0]
	var startTimeStamp=(new Date(sstart)).getTime();
	var endTimeStamp=(new Date(eend)).getTime();
	if(endTimeStamp-startTimeStamp>6*60*60*1000){ 
		atrshowmodal("开始时间和结束时间之间不能大于6小时！");
		return false;
	}
	if(DateDiff(start,nowT)>30 || DateDiff(end,nowT)>30){
		atrshowmodal("只能查询30天历史数据！");
		return false;
	}
	
	hitoryDialog = dialog({
        title: '提示',
        width: 300,
	    height: 80,
        content: "正在加载历史数据，请稍后..",
        cancelValue:'关闭'
    });
	hitoryDialog.showModal();
/*	var tabs = $(".tabs-wrap ul.tabs li");
	var sum = tabs.size()-1;*/
	getHitoryData();
	
}
//拖动滚动条异步查询数据方法
//function afterSetExtremes(e,id) {
//	var chart = id.highcharts();
//	chart.showLoading('正在加载数据...');
//	var type =null;
//	if(id.selector=="#arpcontainer"){
//		type=1
//	}
//	if(id.selector=="#icmpcontainer"){
//		type=2
//	}
//	$.getJSON("/VD/chart/queryArpOrIcmpForAsync.do?start=" + Math.round(e.min) +
//			"&end=" + Math.round(e.max) +
//			"&type=" +type, function (data) {
//				//console.log("起始时间 ："+e.min);
//				//console.log("结束时间 ："+e.max);
//				 switch(type)
//				 {
//				 	case 1:
//				 		//chart.series[0].setData(data);
//				 		//setArpIcmpHitoryLineChart("arpcontainer",data,$('#arpcontainer'),null,null,"延迟时间","ARP 监视器 历史数据");
//				 		break;
//				 	case 2:
//				 		//setArpIcmpHitoryLineChart("icmpcontainer",data,$('#icmpcontainer'),null,null,"延迟时间","ICMP 监视器 历史数据");
//				 		//chart.series[0].setData(data);
//				 		break;
//				 }
//	});
//		chart.hideLoading();
//
//	
//}
var hitoryDialog = null;
function hitory(){
	$(".historyBox").show();
	hitoryDialog = dialog({
        title: '提示',
        width: 300,
	    height: 80,
        content: "正在加载历史数据，请稍后..",
        cancelValue:'关闭'
    });
	hitoryDialog.showModal();
	/*var tabs = $(".tabs-wrap ul.tabs li");
	var num = tabs.size()-1;
	tabs.each(function(ind){*/
	getHitoryData();
	/*});*/
	
	//getHitoryData($(".tabs-wrap ul.tabs li.tabs-selected").data("type"));
}

function getHitoryData(){
	//$("#historyType").val(type);
	$.ajax({
		url: "/VD/chart/queryHistory.do",
		dataType :"json",
		type : 'post',
		data : $("#page_form").serialize(),
		success: function(result){
			hitoryDialog.close();	
			if(result != null && result.status == "y"){
				var datas = result.data;
				var exist = false;
				for(var type in datas){
					var data = datas[type];
					if(data != null && data.length > 0){
						exist = true;
					}
					 switch(type){
					 	case "1":
			    			setArpIcmpHitoryLineChart("arpcontainer",data,$('#arpcontainer'),null,null,"延迟时间","ARP 监视器 历史数据");
					 		break;
					 	case "2":
				    		setArpIcmpHitoryLineChart("icmpcontainer",data,$('#icmpcontainer'),null,null,"延迟时间","ICMP 监视器 历史数据");
					 		break;
					 	case "3":
					 		setHitoryLineChart("tcpcontainer",data,$('#tcpcontainer'),null,null,"端口数量","TCP 监视器 历史数据");
					 		break;
					 	case "13":
					 		setHitoryLineChart("etn0incontainer",data,$('#etn0incontainer'),null,null,"报文个数","Eth0 发送 监视器 历史数据");
					 		break;
					 	case "14":
					 		setHitoryLineChart("etn0outcontainer",data,$('#etn0outcontainer'),null,null,"报文个数","Eth0 接收 监视器 历史数据");
					 		break;
					 	case "15":
					 		setHitoryLineChart("etn1incontainer",data,$('#etn1incontainer'),null,null,"报文个数","Eth1 发送 监视器 历史数据");
					 		break;
					 	case "16":
					 		setHitoryLineChart("etn1outcontainer",data,$('#etn1outcontainer'),null,null,"报文个数","Eth1 接收 监视器 历史数据");
					 		break;
					 	/*case "4":
					 		setHitoryLineChart("tcpcontainer",data,$('#tcpcontainer'),null,null,"端口数量","TCP 监视器 历史数据");
					 		break;*/
					 }
				}
				if(exist==false){
					atrshowmodal("该时间段内没有监视器执行记录！");
				}
			}
			 
			/*if(data instanceof Array){
				//var data = fillupHitoryData(data,type);
				 switch(type){
				 	case 1:
	//			 		var arplen =data.length;
	//	    			var arpnum=0;
	//	    			if(arplen>1){
	//	    				console.log('arplen ',arplen);
	//		    			for(var i=0;i<arplen;i++){
	//		    				if(typeof(data[i][1]) =="number"){
	//		    					console.log('arplen ',arplen);
	//		    					arpnum =data[i][1]/100;
	//			    				data[i][1]=arpnum;
	//		    				}
	//		    			}
	//	    			}
		    			setArpIcmpHitoryLineChart("arpcontainer",data,$('#arpcontainer'),null,null,"延迟时间","ARP 监视器 历史数据");
				 		break;
				 	case 2:
	//			 		var icmplen = data.length;
	//	    			var icpmnum = 0;
	//	    			if(icmplen>1){
	//		    			for(var i=0;i<icmplen;i++){
	//		    				if(typeof(data[i][1]) =="number"){
	//		    					icpmnum =data[i][1]/100;
	//			    				data[i][1]=icpmnum;
	//		    				}
	//		    			}
	//	    			}
			    		setArpIcmpHitoryLineChart("icmpcontainer",data,$('#icmpcontainer'),null,null,"延迟时间","ICMP 监视器 历史数据");
				 		break;
				 	case 3:
				 		setHitoryLineChart("tcpcontainer",data,$('#tcpcontainer'),null,null,"端口数量","TCP 监视器 历史数据");
				 		break;
				 	case 13:
				 		setHitoryLineChart("etn0incontainer",data,$('#etn0incontainer'),null,null,"报文个数","Eth0 发送 监视器 历史数据");
				 		break;
				 	case 14:
				 		setHitoryLineChart("etn0outcontainer",data,$('#etn0outcontainer'),null,null,"报文个数","Eth0 接收 监视器 历史数据");
				 		break;
				 	case 15:
				 		setHitoryLineChart("etn1incontainer",data,$('#etn1incontainer'),null,null,"报文个数","Eth1 发送 监视器 历史数据");
				 		break;
				 	case 16:
				 		setHitoryLineChart("etn1outcontainer",data,$('#etn1outcontainer'),null,null,"报文个数","Eth1 接收 监视器 历史数据");
				 		break;
				 }
			}*/
		     //关闭
			 /*if( hitoryDialog != null && ind == num){
				 hitoryDialog.close();
			 }*/
			
		}
			
	});
}

/*function fillupHitoryData(data,type){
	try{
		//判断是否是数组
		if((data instanceof Array) && data.length > 0){
			//新的数据
			var newData = [];
			//上一条数据的时间，默认上一条数据的时间为null
			var oldtime = null;
			//循环所有数据
			for(var i = 0;i<data.length;i++){
				//上一条数据的时间有值
				if(oldtime != null){
					//这条数据的时间值减去上一条数据的时间值，再减去1000
					var shortOf = Math.floor((data[i][0] - oldtime - 1000)/1000);
					if(shortOf > 0){
						for(var j=0;j<shortOf;j++){
							oldtime = oldtime + 1000;
							newData.push([oldtime,null]);
						}
						newData.push(data[i]);
					}else{
						oldtime = data[i][0];
						newData.push(data[i]);
					}
				}else{
					//上一次的时间没有数据
					oldtime = data[i][0];
					newData.push(data[i]);
				}
			}
			data = newData;
		}
		return data;
	}catch(er){
		console.log(er);
	}
}*/



function setHitoryLineChart(divid,data,id,time,num,xtitle,title){
     id.highcharts('StockChart', {
        chart : {
            type: 'spline',
        },
/*        lang : {
			downloadJPEG : "下载JPEG 图片",
			downloadPDF : "下载PDF文档",
			downloadPNG : "下载PNG 图片",
			downloadSVG : "下载SVG 矢量图",
			exportButtonTitle : "导出图片"
		},*/
        global: { useUTC: false },
        navigator : {
            series : {
                data : data
            }
        }, 
        rangeSelector:{
        	enabled:false
        },
        scrollbar: {
            liveRedraw: false,
            showfull:false
        },
        title: {
            text:title
        },
//        exporting : {
//			sourceWidth : 1500,
//			sourceHeight : 400,
//			buttons : {
//				contextButton : {
//					menuItems : [
//					// Highcharts.getOptions().exporting.buttons.contextButton.menuItems[0]
//					].concat([ {
//						separator : true
//					}, {   
//						text : '导出PNG文件',
//						onclick : function() {
//							exportChart(this, "image/png", title);
//						}
//					}, {
//						text : '导出JPG文件',
//						onclick : function() {
//							exportChart(this, "image/jpeg", title);
//						}
//					}, {
//						text : '导出SVG文件',
//						onclick : function() {
//							exportChart(this, "image/svg+xml", title);
//						}
//					}, /*{
//						text : '导出PDF文件',
//						onclick : function() {
//							exportChart(this, "application/pdf", title);
//						}
//					}*/ ])
//				}
//			}
//		},
	    exporting : {
			printMaxWidth:1900,
			sourceWidth : 1500,
			sourceHeight : 400,
		},
        credits: {
            enabled: false
        },
        rangeSelector : {
       	 	enabled: false // it supports only days
 		}, 
	    xAxis : [{
//	    	 events : {
//	                afterSetExtremes : function (e){
//	                	afterSetExtremes(e,id)
//	                }
//	         },
	    	 type: 'datetime',
	    	 tickInterval: 1000,
	    	 dateTimeLabelFormats: {
                 day: '%Y-%m-%d'
             },
        }], 
        yAxis: {
          min : 0,
          title : {
			align : 'middle',
			margin : 10,
			rotation : 270,
			text : xtitle
          },
		 allowDecimals : true,
	     opposite: false
        },
        tooltip : {
			enabled : true,
			dateTimeLabelFormats: {
                day: '%Y-%m-%d'
            },
            formatter: function () {
                 return '<b>' +formatForHistory(this.x, 'yyyy-MM-dd HH:mm:ss')+ '</b><br/>'+"报文个数："+Math.round(this.y);
             }
		},
        series : [{
          pointStart: new Date().getTime()-24*60*1000,
          pointInterval:1000,
          name: xtitle,
          data:data
      
        }]
//		 series : data
    });
} 
function setArpIcmpHitoryLineChart(divid,data,id,time,num,xtitle,title){
       id.highcharts('StockChart', {
       chart : {
           type: 'scatter'
       },
       boost: {
           useGPUTranslations: true,
           usePreAllocated: true
       },
/*       lang : {
			downloadJPEG : "下载JPEG 图片",
			downloadPDF : "下载PDF文档",
			downloadPNG : "下载PNG 图片",
			downloadSVG : "下载SVG 矢量图",
			exportButtonTitle : "导出图片"
		},*/
       global: { useUTC: false },
       navigator : {
           series : {
               data : data
           }
       }, 
       rangeSelector:{
       	enabled:false
       },
       scrollbar: {
    	   enabled:true,
           liveRedraw: false,
           showfull:false
           
       },
       title: {
           text:title
       },
	    exporting : {
			printMaxWidth:1900,
			sourceWidth : 1500,
			sourceHeight : 400,
		},
       credits: {
           enabled: false
       },
//       rangeSelector : {
//    	    enabled: false // it supports only days
//		}, 
	    xAxis : [{
//	    	 events : {
//                afterSetExtremes : function (e){
//                	afterSetExtremes(e,id)
//                }
//            },
	    	 type: 'datetime',
	    	 tickInterval: 10000,
	    	 dateTimeLabelFormats: {
                day: '%Y-%m-%d'
            }
       }], 
       yAxis: {
         min : 0,
         title : {
			align : 'middle',
			margin : 10,
			rotation : 270,
			text : '延迟(毫秒)'
		 },
		allowDecimals : true,
		opposite: false
       },
       tooltip : {
			enabled : true,
			dateTimeLabelFormats: {
              day: '%Y-%m-%d'
           },
           formatter: function () {
               return '<b>' +formatForHistory(this.x, 'yyyy-MM-dd HH:mm:ss')+ '</b><br/>'+"延迟："+this.y;
           }
		},
       series : [{
    	 turboThreshold: 200,
         pointStart: new Date().getTime()-24*60*1000,
         pointInterval:1000,
         name: xtitle,
         data:data
     
       }]
//		 series : data
   });
} 
function exportChart(chart, type, filename) {
	if(chart){
		chart.title.styles.fontSize = 20;
	}
	$("input[name='svg']").val(chart.getSVG());
	//$("input[name='svg']").val(Highcharts.getSVG(chart));
	$("input[name='type']").val(type);
	$("input[name='filename']").val(filename);
	
	$("#exportForm").submit();
	if(chart){
		chart.title.styles.fontSize = 10;
		
	}
}
//毫秒转换成日期格式工具方法
var formatForHistory = function(time, format){ 
	var t = new Date(time); 
	var tf = function(i){return (i < 10 ? '0' : '') + i}; 
	return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){ 
		switch(a){ 
		case 'yyyy': 
		return tf(t.getFullYear()); 
		break; 
		case 'MM': 
		return tf(t.getMonth() + 1); 
		break; 
		case 'mm': 
		return tf(t.getMinutes()); 
		break; 
		case 'dd': 
		return tf(t.getDate()); 
		break; 
		case 'HH': 
		return tf(t.getHours()); 
		break; 
		case 'ss': 
		return tf(t.getSeconds()); 
		break; 
		}; 
	}); 
}; 
