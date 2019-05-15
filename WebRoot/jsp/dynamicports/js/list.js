$(function(){
	$(".progressNum").html("0%");
	//开始扫描
	demo1=$("#form1").Validform({
		tiptype:2,
		ajaxPost:true,
		datatype:dataType_obj,
        tiptype:fun_tiptype,
        callback:function(data){
        	if(data.status=="y"){
        		//将开始扫描按钮修改成停止扫描
        		if(data.info=="开始动态扫描"){
        			//开始时控制按钮
        			begin_btn();
        			//首先清空之前的数据
					$("#scanTable").find("tr").remove();
        			//开启定时器
					scanport();
        		}else{
        			stopScan();
        			//结束时控制按钮
        			end_btn();
        		}
			}else{
				atrshowmodal( "扫描开启失败");
			}
        }
	});
	
	
	//考虑有滚动条的情况
	var obj=document.getElementById("fix_div");   
	if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){ 
		$("#title_div").css("padding-right","17px");
	}else{
		$("#title_div").css("padding-right","0px");
	}
})

var scanInter = null;

function scanport(){
	scanInter = setInterval(function(){
		$.ajax({
			type:"post",
			data:{time:new Date()},
			url:basePath+"dynamicports/scanport.do",
			dataType:"json",
			success:function(datas){
				var rhtml = "";
				if(datas!=null){
					if(datas.status == "n" && datas.errorNum == 12){
						stopScan();
						atrshowmodal("扫描失败，请检查设备或网络！",function(){
							//结束时控制按钮
		        			end_btn();
						});
						return;
					}
					
					$(".progressbar_1").show();
					var pro = parseInt(datas.progress);
					if(pro>100){
						stopScan();
						//结束时控制按钮
	        			end_btn();
						return;
					}else{
						$(".bar").css("width",pro+"%");
						$(".progressNum").html(pro+"%");
						var list = datas.list;
						for(var i=0;i<list.length;i++){
							var d = list[i];
							rhtml +="<tr>";
							rhtml +="<td width='33.33%'>"+d.portNum+"</td>";
							rhtml +="<td width='33.33%'>";
							var pt = "";
							if(d.portType == 6){
								pt = "TCP";
							}else if(d.portType == 17){
								pt = "UDP";
							}
							rhtml += pt;
							rhtml += "</td>";
							rhtml +="<td width='33.33%'>";
							var st = "";
							if(d.scanType==0){
								st = "主动扫描"
							}else{
								st = "被动扫描"
							}
							rhtml += st;
							rhtml +="</td>";
							rhtml +="</tr>";
						}
						$("#scanTable").html(rhtml);
					}
				}
			}
		});
	},1000);
}

function stopScan(){
	if(scanInter!=null){
		$.get(basePath+"dynamicports/stopScan.do");
		clearInterval(scanInter);
	}
}

//开始时控制按钮
function begin_btn(){
	//页面中所有的form控件不可用
	$("input").attr("disabled",'true');
	$("select").attr("disabled",'true');
	$("textarea").attr("disabled",'true');
	//开始停止按钮可用
	$("#btn_ctr").removeAttr("disabled");
	$("#btn_ctr").val("停止扫描");
	
}
//结束时控制按钮
function end_btn(){
	$(".bar").css("width","0");
	$(".progressNum").html("0%");
	//页面中所有的form控件可用
	$("input").removeAttr("disabled");
	$("select").removeAttr("disabled");
	$("textarea").removeAttr("disabled");
	//开始停止按钮可用
	$("#btn_ctr").val("开始扫描");
	$("span[name='sa']").html("扫描结束");
}
//焦点已开输入框时
function clearMsg(o){
	if($("#portNum").val().length==0){
		$(o).siblings(".Validform_checktip").html("");
		$(o).removeClass();
	}
}