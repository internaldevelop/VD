$(function(){
	$(".progressNum").html("0%");
	//开始扫描
	demo1=$("#form1").Validform({
		tiptype:2,
		ajaxPost:true,
		datatype:dataType_obj,
        tiptype:fun_tiptype,
        tipSweep:true,
        callback:function(data){
        	if(data.status=="y"){
        		//将开始扫描按钮修改成停止扫描
        		if(data.info=="开始扫描"){
        			//开始时控制按钮
        			begin_btn();
        			//首先清空之前的数据
					$("#scanTable").find("tr").remove();
        			//开启定时器
        			interval_progress(0);
        		}else{
        			//结束时控制按钮
        			end_btn();
        			window.clearTimeout(scanTimeout);
        		}
			}else{
				atrshowmodal( "扫描开启失败");
			}
        }
	});
	//手动添加端口
	demo2=$("#form2").Validform({
		tiptype:2,
		ajaxPost:true,
		datatype:dataType_obj,
		tipSweep:true,
        tiptype:fun_tiptype,
        callback:function(data){
       	 	clearMsg($("#form2").find("#msgdemo"));
       	 	if(data.success){
       	 		var pt;
       	 		if(data.obj.portType == '6'){
       	 			pt = "TCP";
       	 		}else if(data.obj.portType  == '17'){
       	 			pt = "UDP";
       	 		}
       	 		var s="";
       	 		s+="<tr>";
       	 		s+="<td width='10%'>"+data.obj.portNum+"</td>";
       	 		s+="<td width='35%'>"+"</td>";
       	 		s+="<td width='15%'>"+"手动添加"+"</td>";
       	 		s+="<td width='15%'>"+pt+"</td>";
       	 		s+="<td width='15%'>"+"未知"+"</td>";
       	 		s+="<td width='10%'><a href=\"javaScript:void(0)\" onclick=\"deletePort('"+data.obj.id+"','"+data.obj.portType+"','"+data.obj.portNum+"',this)\">删除</a></td>";
       	 		s+="</tr>";
       	 		$("#scanTable").prepend(s);
       	 	}else{
       	 		//alert(data.info);
       	 	}
			
       	 	//将数据保存在列表中
       	 	//window.location.reload();
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
	if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){ 
		$("#title_div").css("padding-right","17px");
	}else{
		$("#title_div").css("padding-right","0px");
	}
})


//定时器控制进度条
function interval_progress(t){
	//添加端口按钮不可用
	$(":submit:eq(1)").attr("disabled","true");
	
	//开启进度条控制的定时器
	scanTimeout=window.setTimeout(function(){
		$.ajax({
			type: "POST",
			url: "getProgress.do",
			data:{time:t},
			dataType :"json",
			success: function(data){
				if(data.status=="n" && data.errorNum == 11){
					closeScan();
					//扫描结束
					atrshowmodal("扫描失败，请检查设备或网络！",function(){
						
						end_btn();
					});
					return;
				}
				$(".progressbar_1").show();
				$(".bar").css("width",data.progress+"%");
				$(".progressNum").html(data.progress+"%");
				//在表格中显示数据
				//首先清空之前的数据
				//$("#scanTable").find("tr:gt(0)").remove();
				 
				var lst=data.result;
				var s="";
				for(i=0;i<data.result.length;i++){
					s+="<tr>";
					s+="<td width='10%'>"+lst[i].PORTNUM+"</td>";
					s+="<td width='35%'>"+(lst[i].NAME==null?'':lst[i].NAME)+"</td>";
					s+="<td width='15%'>"+lst[i].SOURCE+"</td>";
					s+="<td width='15%'>"+lst[i].PORTTYPE+"</td>";
					s+="<td width='15%'>"+lst[i].SCANTYPE+"</td>";
					s+="<td width='10%'><a href=\"javaScript:void(0)\" onclick=\"deletePort('"+lst[i].ID+"','"+lst[i].PORTTYPEY+"','"+lst[i].PORTNUM+"',this)\">删除</a></td>";
					s+="</tr>";
				}	
				$("#scanTable").prepend(s);
				
				//考虑有滚动条的情况
				var obj=document.getElementById("fix_div");   
				if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){ 
					$("#title_div").css("padding-right","17px");
				}else{
					$("#title_div").css("padding-right","0px");
				}
	
				if(data.status=="n"){
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
//添加端口
function addPort(){
	//校验端口合法
	if(isNaN($("#portNum").val())){
		atrshowmodal("请输入合法的端口！")
		return;
	}
	//将数据提交到后台，开始扫描
	$.ajax({
		type: "POST",
		url: "addPort.do",
		data: $("#form2").serialize(),
		dataType :"json",
		success: function(msg){
			if(msg.success){
				
			}else{
				atrshowmodal(msg.info);
			}
		  	
		},
		error:function(){
			atrshowmodal("网络错误");
		}
	});
}

//删除端口
function deletePort(id,porttype,portnum,o){
	//将数据提交到后台，开始扫描
	$.ajax({
		type: "POST",
		url: "deletePort.do",
		data: {id:id,porttype:porttype,portnum:portnum},
		dataType :"json",
		success: function(msg){
			$(o).parent().parent().remove();
		},
		error:function(){
			atrshowmodal("网络错误");
		}
	});
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
	history.go(0);
}
//焦点已开输入框时
function clearMsg(o){
	if($("#portNum").val().length==0){
		$(o).siblings(".Validform_checktip").html("");
		$(o).removeClass();
	}
}