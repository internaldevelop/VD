
$(function(){
	//保存在测设备
	demo1=$("#form1").Validform({
		tiptype:2,
		ajaxPost:true,
		datatype:dataType_obj,
        tiptype:fun_tiptype,
        beforeSubmit:function(){
        	$("#form1").attr("action",$("#form1").attr("action")+"?n="+Math.random());
        	return true;
        },
        callback:function(data){
        	clearMsg($("#form1").find("#msgdemo"));
        }
	});
	
	//保存Eth0
	demo2=$("#form2").Validform({
		tiptype:2,
		ajaxPost:true,
		datatype:dataType_obj,
        tiptype:fun_tiptype,
        beforeSubmit:function(){
        	$("#form2").attr("action",$("#form2").attr("action")+"?n="+Math.random());
        	return true;
        },
        callback:function(data){
        	n2=0;
			//开始检测，启动定时器，取得mac地址
			fun_timeout_save2();
        }
	}); 
	//保存Eth1 (第三个表单)
	demo3=$("#form3").Validform({
		tiptype:2,
		ajaxPost:true,
		datatype:dataType_obj,
        tiptype:fun_tiptype,
        beforeSubmit:function(){
        	$("#form3").attr("action",$("#form3").attr("action")+"?n="+Math.random());
        	return true;
        },
        callback:function(data){
        	n3=0;
			//开始检测，启动定时器，取得mac地址
			fun_timeout_save3();
        }
	}); 
	//保存 ARP监视器(第四个表单)
	demo4_1=$("#form4_1").Validform({
		tiptype:2,
		ajaxPost:true,
		datatype:dataType_obj,
        tiptype:fun_tiptype,
        callback:function(data){
			
        }
	}); 
	//保存 ICMP监视器(第四个表单)
	demo4_2=$("#form4_2").Validform({
		tiptype:2,
		ajaxPost:true,
		datatype:dataType_obj,
        tiptype:fun_tiptype,
        callback:function(data){
			
        }
	}); 
	//保存 离散监视器(第四个表单)
	demo4_3=$("#form4_3").Validform({
		tiptype:2,
		ajaxPost:true,
		datatype:dataType_obj,
        tiptype:fun_tiptype,
        callback:function(data){
			
        }
	}); 
	//保存 TCP监视器(第四个表单)
	demo4_4=$("#form4_4").Validform({
		tiptype:2,
		ajaxPost:true,
		datatype:dataType_obj,
        tiptype:fun_tiptype,
        callback:function(data){
			
        }
	});
	
	
	
})
//Eth0部分操作的js方法
//记录请求发出后，访问的次数，当请求次数为4时（3秒），停止访问
var n2=0;
function fun_timeout_save2(){
	n2++;
	window.setTimeout(function(){
		//取得mac地址
		$.ajax({
			type: "POST",
			url: "save2.do",
			cache:false,
			data: $("#form2").serialize()+"&cishu="+n2,
			dataType :"json",
			success: function(data){
				if(data && data.mac){
					$("#mac").val(data.mac[0]);
					$("#mac2").val(data.mac[1]);
				}
				if(data.status=="y"){
					$("#form2").find("#msgdemo").html(data.info);
					clearMsg($("#form2").find("#msgdemo"));
				}else if("未检测到在测设备"==data.info || n2==4){
					if(data.mac == null){
						$("#mac").val("00:00:00:00:00:00");
						$("#mac2").val("00:00:00:00:00:00");
					}
					$("#form2").find("#msgdemo").html("未检测到在测设备");
					n2=0;
				}else{
					/*$("#eth0_cishu").val(n2);*/
					fun_timeout_save2();
				}
			},
			error:function(){
				atrshowmodal("网络错误");
			}
		}); 
		
	},1000);
}

//Eth1部分操作的js方法
//记录请求发出后，访问的次数，当请求次数为4时（3秒），停止访问
n3=0;
function fun_timeout_save3(){
	n3++;
	//开始检测，启动定时器，取得mac地址
	myInter=window.setTimeout(function(){
		//取得mac地址
		$.ajax({
			type: "POST",
			url: "save3.do",
			data: $("#form3").serialize(),
			cache:false,
			dataType :"json",
			success: function(data){
				//alert(n3);
				if(data.status=="y"){
					$("#mac3").val(data.mac);
					$("#form3").find("#msgdemo").html(data.info);
					clearMsg($("#form3").find("#msgdemo"));
				}else if("未检测到在测设备"==data.info || n3==6){
					$("#mac3").val("");
					$("#form3").find("#msgdemo").html("未检测到在测设备");
					n3=0;
				}else{
					fun_timeout_save3();
				}
			},
			error:function(){
				atrshowmodal("网络错误");
			}
		}); 
		
	},1000);
}
var v_mid=1;	
//通过id查找监视器
function findMonitorById(id,o){
	//设置背景颜色
	$(".tr_bg").css("background","#FFFFFF");
	$(o).css("background","#E3E3E3");
	v_mid=id;
	$.ajax({
		type: "POST",
		url: "findMonitorById.do",
		data: {id:id},
		cache:false,
		dataType :"json",
		success: function(msg){
			$("#monitorId").val(msg.MONITORID);
			
			//$("#overtime").val(msg.OVERTIME);
			
			//根据不同监视器，显示不同的数据项
			if(id==1){
				//ARP监视器
				//备注
				$("#form4_1").find("#m_remark").html(msg.REMARK);
				//超时时间
				$("#form4_1").find("#overtime").val(msg.OVERTIME);
				//其他的隐藏
				$("#v4_2").hide();
				$("#v4_3").hide();
				$("#v4_4").hide();
				//可见
				$("#v4_1").show();
				
			}else if(id==2){
				//ICMP监视器
				//备注
				$("#form4_2").find("#m_remark").html(msg.REMARK);
				//超时时间
				$("#form4_2").find("#overtime").val(msg.OVERTIME);
				//其他的隐藏
				$("#v4_1").hide();
				$("#v4_3").hide();
				$("#v4_4").hide();
				//可见
				$("#v4_2").show();
			}else if(id==3){
				//Tcp监视器
				//备注
				$("#form4_3").find("#m_remark").html(msg.REMARK);
				//使用打开的端口
				if(msg.TCPPORTS==1){
					$("#form4_3").find("#tcpports").attr("checked",true);
				}
				//其他的隐藏
				$("#v4_1").hide();
				$("#v4_2").hide();
				$("#v4_4").hide();
				//可见
				$("#v4_3").show();
				
			}else if(id==4){
				//离散监视器
				//备注
				$("#form4_4").find("#m_remark").html(msg.REMARK);
				//周期
				$("#form4_4").find("#cyclePeriod").val(msg.CYCLEPERIOD);
				//监控单个输入
				$("#form4_4").find("#o"+msg.INPUT).attr("selected","true");
				//告警等级
				$("#form4_4").find("#alarmLevel").val(msg.ALARMLEVEL);
				//其他的隐藏
				$("#v4_1").hide();
				$("#v4_2").hide();
				$("#v4_3").hide();
				//可见
				$("#v4_4").show();
			}
		},
		error:function(){
			atrshowmodal("网络错误");
		}
	}); 
}
//当光标离开时，保存
function commit4(n){
	//判断用户是否选择了监视器
	if($("#monitorId").val()!=""){
		$("#form4_"+n).submit();
	}
}
//当用户选择监视器的复选框后执行
function selectMinitor(o,mid){
	//选中为1，未选中为2 
	var selected=1;
	if(!o.checked){
		selected=2;
	}
	$.ajax({
		type: "POST",
		url: "selectMinitor.do",
		cache:false,
		data: {selected:selected,envId:$("#envId").val(),mid:mid},
		dataType :"json",
		success: function(msg){
			
		},
		error:function(){
			atrshowmodal("网络错误");
		}
	}); 
}
//保存监视器
function saveMonitor(){
	if($("#form4_"+v_mid).find(".Validform_wrong").length!=0){
		$("#bcmsg").html("输入数据错误");
		return;
	}
	$.ajax({
		type: "POST",
		url: "saveMonitor.do",
		dataType :"json",
		cache:false,
		success: function(data){
			if(data.status=="y"){
				$("#bcmsg").html("下发成功");
				clearMsg($("#bcmsg"));
			}
		},
		error:function(){
			atrshowmodal("网络错误");
		}
	}); 

}




//用户选择点对点后，设置使能2不可用
function operEnable2(o){
	if(o.value=='2'){
		//设置不可用
		$("#enable2").attr("disabled","disabled");
		$("#enable2").removeAttr("checked");
		$("#ip3").attr("disabled","disabled");
		$("#subnetMask3").attr("disabled","disabled");
		$("#mac3").attr("disabled","disabled");
		$("#btn3").attr("disabled","disabled");
	}else{
		$("#enable2").attr("checked","false");
		$("#enable2").removeAttr("disabled");
		$("#ip3").removeAttr("disabled");
		$("#subnetMask3").removeAttr("disabled");
		$("#mac3").removeAttr("disabled");
		$("#btn3").removeAttr("disabled");
	}
}
//用户选择使能复选框后，设置使能2不可用
function operEnableChecked2(o){
	/*var ck = 0;
	//选中
	if(o.checked){
		ck = 1;
		//设置可用
		$("#ip3").removeAttr("disabled");
		$("#subnetMask3").removeAttr("disabled");
		$("#mac3").removeAttr("disabled");
		$("#btn3").removeAttr("disabled");
	//没有选中，不可用
	}else{
		ck = 0;
		$("#ip3").attr("disabled","disabled");
		$("#subnetMask3").attr("disabled","disabled");
		$("#mac3").attr("disabled","disabled");
		$("#btn3").attr("disabled","disabled");
	}*/
	
	$.ajax({
		type: "POST",
		url: "saveEth1.do",
		cache:false,
		data: {selected:ck},
		dataType :"json"
	});
}
function copyIp(o){
	$("#ip4").val($(o).val());  
}	
//定时取得端口扫描进度
function interval_progressScan(t){
	//开启进度条控制的定时器
	scanTimeout=window.setTimeout(function(){
		$.ajax({
			type: "POST",
			url: "/VD/portscan/getProgress.do",
			data:{time:t},
			dataType :"json",
			success: function(data){
				if(data.status=="n"){
					window.location.reload();
				}else{
					interval_progressScan(data.time);
				}
				//window.parent.sendConnect();
				//window.parent.alertStartScan = function alertStartScan(flag) {

				//}
			}
		}); 
	},10000);			
}
