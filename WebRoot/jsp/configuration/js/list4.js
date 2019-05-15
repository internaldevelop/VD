var scanTimeout = null;
var isStop = false; //是否手动停止
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
        		if(data.info=="开始扫描"){
        			isStop = false;
        			//开始时控制按钮
        			begin_btn();
        			//首先清空之前的数据
					$("#scanTable").find("tr").remove();
        			//开启定时器
        			interval_progress(0);
        		}else{
        			//结束时控制按钮
        			interval_progress(data.time);
        			isStop = true;
        			/*setTimeout(function(){
        				window.clearTimeout(scanTimeout);
        				$(".bar").css("width","0");
        				$(".progressNum").html("0%");
        				//页面中所有的form控件可用
        				$("input").removeAttr("disabled");
        				$("select").removeAttr("disabled");
        				$("textarea").removeAttr("disabled");
        				//开始停止按钮可用
        				$("#btn_ctr").attr("disabled",false).val("开始扫描");
        				if($("#msgdemo").html() == "扫描停止中"){
        					$("#msgdemo").html("");
        				}
        			}, 20000);*/
        		}
			}else{
				$("#btn_ctr").attr("disabled",false);
				atrshowmodal( "扫描开启失败");
			}
        }
	});
	
	//判断是否正在扫描中
	if($("#startScanFlag").val()=="1"){
		interval_progress(0);
	}
	//else{
	//	$(".progressbar_1").hide();
	//}
	
	//考虑有滚动条的情况
	var obj=document.getElementById("fix_div");  
	var $fixDiv = $("#fix_div");
	
	if($fixDiv.attr("scrollHeight")>$fixDiv.attr("clientHeight")||$fixDiv.attr("offsetHeight")>$fixDiv.attr("clientHeight")){ 
		$("#title_div").css("padding-right","17px");
	}else{
		$("#title_div").css("padding-right","0px");
	}
	
	
})

var int_count = 0;

//定时器控制进度条
function interval_progress(t){
	//添加端口按钮不可用
	//$(":submit:eq(1)").attr("disabled","true");
	//开启进度条控制的定时器
	scanTimeout=window.setTimeout(function(){
		$.ajax({
			type: "POST",
			url: "getProgress.do",
			data:{time:t},
			dataType :"json",
			success: function(data){
				var p = parseInt(data.progress);
				if(data.error=="error" && data.errorNum == 13){
					//扫描错误停止扫描
					closeScan();
					//扫描结束					
					atrshowmodal("扫描失败，请检查设备或网络！",function(){						
						$(".bar").css("width","0");
						$(".progressNum").html("0%");
						$("#msgdemo").html("");
						//页面中所有的form控件可用
						$("input").removeAttr("disabled");
						$("select").removeAttr("disabled");
						$("textarea").removeAttr("disabled");
						//开始停止按钮可用
						$("#btn_ctr").val("开始扫描");
						$("span[name='sa']").html("扫描结束");
					});
					return;
				}
				if(p==0){
					int_count++;
					if(int_count>25){
						//超出25秒停止扫描
						closeScan();
						//扫描结束
						end_btn();
						$("#msgdemo").html("");
					}
				}
				
				if(p > 0 && p < 100){
					$("#btn_ctr").attr("disabled",false);
					$("#msgdemo").html("正在扫描");
				}else
				if(p == 100){
					$("#btn_ctr").attr("disabled",true);
					$("#msgdemo").html("扫描停止中");
				}else if(p > 100){
					data.progress = 100;
					$("#btn_ctr").attr("disabled",false);
					$("#msgdemo").html("");
				}
				$(".progressbar_1").show();
				if(!isStop){//不是手动停止显示进度条
					$(".bar").css("width",data.progress+"%");
					$(".progressNum").html(data.progress+"%");
				}
				
				//在表格中显示数据
				//首先清空之前的数据
				//$("#scanTable").find("tr:gt(0)").remove();
				if(data.status=="n" || p > 100){
					//扫描结束
					end_btn();
				}else{
					//扫描没有结束，继续加载(传入时间)
					interval_progress(data.time);
				}
			}
		}); 
	},1000);			
}



//结束扫描 测试用
function closeScan(){
	//将数据提交到后台，开始扫描
	$.ajax({
		type: "POST",
		url: "closeScan.do?",
		data: $("#form1").serialize(),
		dataType :"json",
		success: function(msg){
			window.clearTimeout(scanTimeout);
			$(":button").removeAttr("disabled");
		},
		error:function(){
			atrshowmodal("网络错误");
		}
	});
}
//开始时控制按钮
function begin_btn(){
	//页面中所有的form控件不可用
	$("input").attr("disabled",'true');
	$("select").attr("disabled",'true');
	$("textarea").attr("disabled",'true');
	//开始停止按钮可用
	$("#btn_ctr").attr("disabled",true);
	$("#msgdemo").html("初始化中");
	$("#btn_ctr").val("停止扫描");
}



//结束时控制按钮
function end_btn(){
	int_count = 0;
	try {
		window.clearTimeout(scanTimeout);
		$(".bar").css("width","0");
		$(".progressNum").html("0%");
		//页面中所有的form控件可用
		$("input").removeAttr("disabled");
		$("select").removeAttr("disabled");
		$("textarea").removeAttr("disabled");
		//开始停止按钮可用
		$("#btn_ctr").val("开始扫描");
		$("span[name='sa']").html("扫描结束");
	} catch (e) {
		end_btn();
	}
}
//焦点已开输入框时
function clearMsg(o){
	if($("#portNum").val().length==0){
		$(o).siblings(".Validform_checktip").html("");
		$(o).removeClass();
	}
}