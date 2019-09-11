
var pauseStatus = 0;
$(function(){
	$(".progressbar_1").show();
	$(".progressNum").html("0%");
	$("#logProgressDisplay").html("状态：未执行测试");
	//保存在测设备
	demo1=$("#form1").Validform({
		tiptype:2,
		ajaxPost:true,
        tiptype:fun_tiptype,
        callback:function(data){
        	//在表格中展示数据
        	if(data.status=="y"){
        		//暂存测试模板id
        		tempId=$("#tempId").val();
        		
        		//清空表格中的数据
        		$("#testSuiteTable").find("tr").remove();
        		
				//在表格中显示数据
				var lst=data.result;
				//取得监视器的数量
				var monitorNum=parseInt($("#monitorNum").val())
				var s="";
				for(i=0;i<data.result.length;i++){
					s+="<tr>";
					s+="<td width='100px'><a class='a_ks' href='javaScript:void(0)' onclick='startSingleTest(this,"+i+")'></a></td>";
					s+="<td>"+lst[i].NAME+"</td>";
					s+=tdHtml;
					s+="</tr>";
				}	
				$("#testSuiteTable").append(s);
				//考虑有滚动条的情况
				var obj=document.getElementById("fix_div");   
				if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){ 
					$("#title_div").css("padding-right","17px");
				}else{
					$("#title_div").css("padding-right","0px");
				}
			}
        }
	});
	
	//判断是否在进行端口扫描
	if(startScan!=0){
		$("input").attr("disabled",'true');
		$("select").attr("disabled",'true');
		interval_progressScan(0);
		return ;
	}	
	//启动定时器，加载日志信息
	//按时间加载数据
	//currentTime=new Date().getTime();
	fun_log(0);
	
	//正在进行测试
	if(testStatus==2){
		//将开始按钮禁用
		$("#btn_start").attr("disabled","true");
		if(type=="1"){//单个测试正在进行
			$("#btn_pause").attr("disabled","true");
		}
		$("select").attr("disabled",'true');
		
	}else if(testStatus==3){//暂停
		if(progress == 100){
			$("#testSuiteTable a").each(function(){
				var selfclick  =  $(this).attr("onclick");
				$(this).attr("selfclick",selfclick);
				$(this).attr("onclick",null);
			})
		}
		if(progress == 101){
			$("#testSuiteTable a").each(function(){
				var selfclick  =  $(this).attr("selfclick");
				$(this).attr("onclick",selfclick);
			})
		}
//		if(progress == 104){
//			$("#testSuiteTable a").each(function(){
//				var selfclick  =  $(this).attr("selfclick");
//				$(this).attr("onclick",selfclick);
//			})
//		}
//		$(".progressbar_1").show();
		$(".bar").css("width",progress+"%");
		$(".progressNum").html(progress+"%");
		//$("#logProgressDisplay").html("状态："+logProgressDisplay);
		 if(progress ==0){
			 $("#logProgressDisplay").html("状态：未执行测试");
		 }
		//将开始按钮禁用
		$("#btn_start").attr("disabled","true");
		if(type=="1"){
			$("#btn_pause").attr("disabled","true");
		}else{
			//将暂停按钮禁用
			$("#btn_pause").val("继续执行");
		}
		$("select").attr("disabled",'true');
	}else if(testStatus==5){//停止
//		$(".progressbar_1").show();
		$(".bar").css("width","0%");
		$(".progressNum").html("0%");
		$("#logProgressDisplay").html("状态：未执行测试");
		//将停止按钮禁用
		$("#btn_stop").attr("disabled","true");
		//将暂停按钮禁用
		$("#btn_pause").attr("disabled","true");
		
	}else{
//		$(".progressbar_1").hide();
		//将停止按钮禁用
		$("#btn_stop").attr("disabled","true");
		//将暂停按钮禁用
		$("#btn_pause").attr("disabled","true");
		
	}
	
	//进入页面是否开启自动运行功能
	var tsid=$("#testId").val();

	if(tsid!="" && tsid.length>0){
		$("option[value='"+tsid+"']").attr("selected",true);
		tempId=tsid;
		document.getElementById("btn_start").click(); 
	}else{
		fun_timeout_test();
	
	}
	
	//考虑有滚动条的情况
	var obj=document.getElementById("fix_div");   
	if(obj.scrollHeight>obj.clientHeight||obj.offsetHeight>obj.clientHeight){ 
		$("#title_div").css("padding-right","17px");
	}else{
		$("#title_div").css("padding-right","0px");
	}
	
})
//取得日志信息
var bool = 0;
var checkbool = 0;

function fun_log(time){
//	interval_log=window.setTimeout(function(){
	$("#currentTime").val(time);
	window.setInterval(function(){
		if(bool == 0 && checkbool == 0){
			bool = 1;
		//异步提交给后台
		$.ajax({
			type: "POST",
			url: "findLog.do",
			data: $("#logForm").serialize(),
			dataType :"json",
			cache:false,
			success: function(data){
				if(checkbool == 0){
				//展示数据				
				for(i=0;i<data.result.length;i++){
					var x=document.getElementById('logTable').insertRow(0)
					  var td1=x.insertCell(0);
					td1.style.width = "100px "; 
					  var td2=x.insertCell(1);
					  td2.style.width = "150px "; 
					  var td3=x.insertCell(2);
					  td3.style.width = "150px "; 
//					  
//					  var td4=x.insertCell(3);
//					  td4.style = "display:none";
					  
					  td1.innerHTML=data.result[i].CREATETIME;
					  td2.innerHTML=data.result[i].SOURCE;
					  td3.innerHTML=data.result[i].MESSAGE;
					  
//					  td4.innerHTML=data.result[i].SOURCETYPE;
					  
				}
				$("#currentTime").val(data.time);
				//fun_log(data.time);
				bool = 0;
				}
			},
			error:function(){
				bool = 0;
				//alert("网络错误");
			}
		}); 
		}
	},1000);
	//scrollBottomTest();
}
var boolscrol = true;
scrollBottomTest =function(){
$("#logdiv").scroll(function(){
	//alert('2323');
    var $this =$(this),
    viewH =$(this).height(),//可见高度
    contentH =$(this).get(0).scrollHeight,//内容高度
    scrollTop =$(this).scrollTop();//滚动高度
   //if(contentH - viewH - scrollTop <= 100) { //到达底部100px时,加载新内容
   if(scrollTop/(contentH -viewH)>=0.95){ //到达底部100px时,加载新内容
	   if(boolscrol){
		   boolscrol = false;
		   atrshowmodal("222");
	   
	   }
   }
});
}

//当用户选择测试模板后
function fun_change(o){
	if(o.value!="0"){
		$("#form1").submit();
	}
}
//开始全部执行
function startTest(tId){
	var len=0;
	var tempId=$("#tempId").val();
	$.ajax({
		url:basePath+'testexecute/findTestSuite.do',
		type:'post',
		async: false,
		data:{
			"tempId":tempId,
		},
		dataType:'json',
		cache:false,
		success:function(result)
		{
			len=result.result.length;
		},
	});	
	if(len==0){
		atrshowmodal("测试模板为空，请添加用例到模板中！");
	}else{
		$("#btn_start").removeAttr("disabled");
		//将停止按钮解禁
		$("#btn_stop").removeAttr("disabled");
		if($("#tempId").val()!="0" ){
			//将测试数据恢复
			//执行到了第几个用例
			index=0;
			//状态，0未开始，1进行中
			testStatus=0;
			//禁用选择框
			$("#tempId").attr("disabled","true");
			//全部
			type=2;
			
			//将所有图表修改
			$("#testSuiteTable").find("a").removeAttr("class");
			$("#testSuiteTable").find("a").addClass('a_ks');
			//将监视器状态清除
			$(".1").html("");
			$(".2").html("");
			$(".3").html("");
			$(".4").html("");
			
			//将开始按钮禁用
			$("#btn_start").attr("disabled","true");
			//将停止按钮解禁
			$("#btn_stop").removeAttr("disabled");
			//将暂停按钮解禁
			$("#btn_pause").removeAttr("disabled");
			//开始测试
			$.ajax({
				type: "POST",
				url: "startTest.do",
				data: {tempId:tempId},
				dataType :"json",
				cache:false,
				success: function(data){
					//如果从测试设置跳转过来，重新加载页面
					if(data.status=="y"){
						if($("#testId").val()!="" && $("#testId").val().length>0){
							window.location="execute.do";
							return;
						}
						fun_timeout_test();
					}else{
						atrshowmodal("测试正在进行中……");
					}
				},
				error:function(){
					//alert("网络错误");
				}
			}); 
		}
	}
}

var find_num = 0;
//测试的定时器，循环取得测试状态
function fun_timeout_test(){
	timeout_log=window.setTimeout(function(){
		//异步提交给后台
		$.ajax({
			type: "POST",
			url: "findTest.do?8=9",
			data: {tempId:tempId},
			async:false,
			dataType :"json",
			cache:false,
			success: function(data){
					if(data.status == "n" && data.errorNum == 10){
						stopTest();
						atrshowmodal("连接异常，请检查网络设备",function(){
							
							//将开始按钮解禁
							$("#btn_start").removeAttr("disabled");
							//将停止按钮禁用
							$("#btn_stop").attr("disabled","true");
							//去除所有的不可用状态
							$("#testSuiteTable").find("tr").removeAttr("disabled");
							//将置灰的按钮恢复
							$("#testSuiteTable").find(".a_ks3").attr("class","a_ks");
							//选择测试模板可用
							$("#tempId").removeAttr("disabled");
							//将置运行完成的，修改成开始
							$("#testSuiteTable").find(".a_ok").attr("class","a_ks");
							$(".progressbar_1").show();
							$(".bar").css("width","0%");
							$(".progressNum").html("0%");
							$("#logProgressDisplay").html("状态：未执行测试");
						});
						return;
					};
						if(data.status && data.testEntry){
						//$("#info").html($("#testSuiteTable").find("tr:eq("+1+")").find("td:eq(0)").find("a").attr("class"));
						//循环测试用例，
						var list=data.testEntry.list;
						
						for(i=0;i<list.length;i++){
							o_tr=$("#testSuiteTable").find("tr:eq("+(i)+")");
							o=o_tr.find("td:eq(0)").find("a");
							//o.after(list[i].caseStatus);
							//未执行或者已停止
							if(list[i].caseStatus==1 || list[i].caseStatus==5){
								//未执行
								if(type==1){
									o.removeAttr("class");
									o.attr("class","a_ks3");
								}else{ 
									if(list[i].caseStatus==1 && data.testEntry.status==2){
										//正在运行中，未执行
										o.removeAttr("class");
										o.attr("class","a_dd");
									}else if(list[i].caseStatus==5 && data.testEntry.status==2){
										//正在运行中，已停止
										o.removeAttr("class");
										o.attr("class","a_ks3");
									}else if(list[i].caseStatus==1 && data.testEntry.status==3){
										//暂停中，未执行
										o.removeAttr("class");
										o.attr("class","a_dd");
									}else if(list[i].caseStatus==5 && data.testEntry.status==3){
										//暂停中，已执行
										o.removeAttr("class");
										o.attr("class","a_ks3");
									}else{
										//
										o.removeAttr("class");
										o.attr("class","a_ks");
									}
									
								}
								//置灰
								o_tr.attr("disabled","true");
							}else if(list[i].caseStatus==2){
								//执行中
								o.removeAttr("class");
								o.attr("class","a_ing");
								//取消
								o_tr.removeAttr("disabled");
							}else if(list[i].caseStatus==3){
								//暂停
								o.removeAttr("class");
								o.attr("class","a_continue");
							}else if(list[i].caseStatus==4){
								//手动停止
								o.removeAttr("class");
								o.attr("class","a_ks3");
							}
							//监视器状态
							o_tr=$("#testSuiteTable").find("tr:eq("+(i)+")");
							
							//$("#info").text(data.monitorList.length);
							var md=list[i].monitorData;
							for(j=0;j<data.monitorList.length;j++){
								var msgtype=md[data.monitorList[j].id];
								var msghtml="";
								if(msgtype==5){
									msghtml='<img src="../images/no.png">';
								}else if(msgtype==4){
									msghtml='<img src="../images/ya.png">';
								}else if(msgtype==1 || msgtype==2 |msgtype==3){
									msghtml='<img src="../images/dui.png">';
								}
								
								o_tr.find("."+data.monitorList[j].id).html(msghtml);
							}
						}
						if(data.testEntry.status<=2 || data.testEntry.status==4){
							//执行中
							//进度条
							$(".progressbar_1").show();
	//						if(data.testEntry.progress == 100){
	//							console.log("@@@@@@@@@@@@@@@@@@@@@@@@");
	//							$("#testSuiteTable a").each(function(){
	//								var selfclick  =  $(this).attr("onclick");
	//								$(this).attr("selfclick",selfclick);
	//								 $(this).attr("onclick",null);
	//							})
	//						}
//							if(data.testEntry.progress == 104){
//								console.log("#########################");
//								$("#testSuiteTable a").each(function(){
//									var selfclick  =  $(this).attr("selfclick");
//									$(this).attr("onclick",selfclick);
//								})
//							}
							
							$(".bar").css("width",data.testEntry.progress+"%");
							$(".progressNum").html(data.testEntry.progress+"%");
							if(data.logProgressDisplay!=null && data.logProgressDisplay!="")
							{
								$("#logProgressDisplay").html("状态："+data.logProgressDisplay);
							}else
							{
								if(data.testEntry.progress >=0 && data.testEntry.progress <=100){
									$("#logProgressDisplay").html("状态：执行测试中");
								}
								if(data.testEntry.progress == 102){
									$(".bar").css("width","100%");
									$(".progressNum").html("100%");
									$("#logProgressDisplay").html("状态：测试用例停止中");
								}
								if(data.testEntry.progress == 103){
									$(".bar").css("width","100%");
									$(".progressNum").html("100%");
									$("#logProgressDisplay").html("状态：被测设备恢复中");
								}
								if(data.testEntry.progress == 104){
									o.removeAttr("class");
									o.attr("class","a_ks3");
									stopTest();
									$(".bar").css("width","0%");
									$(".progressNum").html("0%");
									$("#logProgressDisplay").html("状态:未执行测试");
								}
							}
							//将开始按钮禁用
							$("#btn_start").attr("disabled","true");
							//选择测试模板禁用
							$("#tempId").attr("disabled","true");
							
							//再次运行（未执行，执行中，完成）
							fun_timeout_test();
						}else if(data.testEntry.status==5){//停止
							//将开始按钮解禁
							$("#btn_start").removeAttr("disabled");
							//将停止按钮禁用
							$("#btn_stop").attr("disabled","true");
							//去除所有的不可用状态
							$("#testSuiteTable").find("tr").removeAttr("disabled");
							//将置灰的按钮恢复
							$("#testSuiteTable").find(".a_ks3").attr("class","a_ks");
							//选择测试模板可用
							$("#tempId").removeAttr("disabled");
							//将置运行完成的，修改成开始
							$("#testSuiteTable").find(".a_ok").attr("class","a_ks");
							$(".progressbar_1").show();
							$(".bar").css("width","0%");
							$(".progressNum").html("0%");
							
							if(data.testEntry.progress == 104 && find_num == 1){
								atrshowmodal( "执行成功" + find_num);
							}
							find_num = 1;
							$("#logProgressDisplay").html("状态:未执行测试");
						}else if(data.testEntry.status==3){
							pauseStatus = 3;
						}
					}
			},
			error:function(xhr, text, error){
				//alert("网络错误");
				console.log("fun_timeout_test",text,error);
			}
		}); 
		
	},1000);
}
//开启单个用例进行测试
function startSingleTest(o,index){
	//alert("startSingleTest :"+startSingleTest);
	if(userName == "audit"){
		atrshowmodal("无权限");
		return;
	}	
	
	if(startScan==2){
		atrshowmodal("正在进行端口扫描");
		return;
	}
	if(startScan==3){
		atrshowmodal("正在进行协议识别");
		return;
	}
	if($(o).attr("class")=="a_ks"){
		//开始单个用例的测试
		
		//状态，0未开始，1进行中
		testStatus=0;
		//禁用选择框
		$("#tempId").attr("disabled","true");
		//单个执行
		type=1;
		
		//将所有图表修改
		//$("#testSuiteTable").find("a").removeAttr("class");
		//$("#testSuiteTable").find("a").addClass('a_ks');
		//将开始按钮禁用
		$("#btn_start").attr("disabled","true");
		//将停止按钮解禁
		$("#btn_stop").removeAttr("disabled");
		
		//开始单个用例的测试
		$.ajax({
			type: "POST",
			url: "startSingleTest.do",
			data: {tempId:tempId,index:index},
			dataType :"json",
			cache:false,
			success: function(data){
				if(data.status=="y"){
					fun_timeout_test();
				}else{
					//将开始按钮解禁
					//$("#btn_start").removeAttr("disabled");
					//将停止按钮禁用
					//$("#btn_stop").attr("disabled","true");
					atrshowmodal("测试正在进行中……");
				}
			},
			error:function(){
				//alert("网络错误");
			}
		}); 
		
		
	}
//	else if($(o).attr("class")=="a_ing"){//暂停
//		$(o).removeAttr("class");
//		$(o).attr("class","a_continue");
//		//正在运行中点击后，成暂停
//		$.ajax({
//			type: "POST",
//			url: "pauseTest.do",
//			dataType :"json",
//			data: {tempId:tempId,index:index},
//			success: function(data){
//				
//			},
//			error:function(){
//				//alert("网络错误");
//			}
//		}); 
//	}else if($(o).attr("class")=="a_continue"){
//		$(o).removeAttr("class");
//		$(o).attr("class","a_ing");
//		//暂停中点击变成继续执行
//		$.ajax({
//			type: "POST",
//			url: "continueTest.do",
//			dataType :"json",
//			data: {tempId:tempId,index:index},
//			success: function(data){
//				testStatus=2;
//				fun_timeout_test();
//			},
//			error:function(){
//				//alert("网络错误");
//			}
//		}); 
//	}

}
////暂停执行
//function pauseTest(){
//	if($("#btn_pause").val()=="继续执行"){
//		$.ajax({
//			type: "POST",
//			url: "continueTest.do",
//			dataType :"json",
//			success: function(data){
//				fun_timeout_test();
//				$("#btn_pause").val("暂停执行");
//			},
//			error:function(){
//				//alert("网络错误");
//			}
//		}); 
//		return;
//	}
//	//异步提交给后台
//	$.ajax({
//		type: "POST",
//		url: "pauseTest.do",
//		dataType :"json",
//		success: function(data){
//			//修改按钮文本
//			$("#btn_pause").val("继续执行");
//		},
//		error:function(){
//			//alert("网络错误");
//		}
//	}); 
//}
//停止执行
function stopTest(){
	find_num = 0;
	if(pauseStatus == 3){
		pauseStatus = 0;
		fun_timeout_test();
	};
	//将停止按钮禁用
	$("#btn_stop").attr("disabled","true");
	//异步提交给后台
	$.ajax({
		type: "POST",
		url: "stopTest.do",
		dataType :"json",
		cache:false,
		success: function(data){
			//将所有图表修改
			$("#testSuiteTable").find("a").removeAttr("class");
			$("#testSuiteTable").find("a").addClass('a_ks3');

			//下标从0开始
			//index=0;
			//选择测试模板可用
			//$("#tempId").removeAttr("disabled");
			//将开始按钮解禁
			//$("#btn_start").removeAttr("disabled");
			//将停止按钮禁用
			//$("#btn_stop").attr("disabled","true");
			
			//testStatus=5;
		
		},
		error:function(){
			atrshowmodal("网络错误");
		}
	}); 
}


//选择不同类型的事件日志
function checkLog(o){
	//将状态发送到后台，进行保存
	$.ajax({
		type: "POST",
		url: "saveExecuteLog.do",
		data: {id:o.value,checked:o.checked},
		dataType :"json",
		cache:false,
		success: function(data){
			
		},
		error:function(){
			atrshowmodal("网络错误");
		}
	}); 
	
	checkbool = 1;
	$('#logTable tbody').empty();
	$("#currentTime").val(0);
	$.ajax({
		type: "POST",
		url: "findLog.do",
		data: $("#logForm").serialize(),
		dataType :"json",
		cache:false,
		success: function(data){
			//展示数据				
			for(i=0;i<data.result.length;i++){
				var x=document.getElementById('logTable').insertRow(0)
				  var td1=x.insertCell(0);
				td1.style.width = "100px "; 
				  var td2=x.insertCell(1);
				  td2.style.width = "150px "; 
				  var td3=x.insertCell(2);
				  td3.style.width = "150px "; 
				  
				  td1.innerHTML=data.result[i].CREATETIME;
				  td2.innerHTML=data.result[i].SOURCE;
				  td3.innerHTML=data.result[i].MESSAGE;
				  
			}
			$("#currentTime").val(data.time);
			checkbool = 0;
		},
		error:function(xhr,text,err){
			bool = 0;
			console.log("checkLog",text,err);
			//alert("网络错误");
		}
	}); 
	 
}
//选择不同类型的图表
function checkChart(o){
	if(o.checked){
		//图表显示
		if(o.value=="1" || o.value=="2" ||o.value=="3" || o.value=="4"){//一张图表
			$("#"+o.value).show();
		}else{//两张图表的
			$("#"+o.value+"_1").show();
			$("#"+o.value+"_2").show();
		}
	}else{
		//图表隐藏
		if(o.value=="1" || o.value=="2" ||o.value=="3" || o.value=="4"){//一张图表
			$("#"+o.value).hide();
		}else{//两张图表的
			$("#"+o.value+"_1").hide();
			$("#"+o.value+"_2").hide();
		}
	}
	//将状态发送到后台，进行保存
	$.ajax({
		type: "POST",
		url: "saveExecuteChart.do",
		data: {id:o.value,checked:o.checked},
		dataType :"json",
		cache:false,
		success: function(data){
			
		},
		error:function(){
			atrshowmodal("网络错误");
		}
	}); 
}

function clearLog(){
	$.ajax({
		type: "POST",
		url: "clearLog.do",
		dataType :"json",
		cache:false,
		success: function(data){
			if(data.status=="y"){
				$("#logTable").find("tr").remove();
			}
		},
		error:function(){
			atrshowmodal("网络错误");
		}
	}); 
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
			cache:false,
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