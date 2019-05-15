//开始识别
var count=0;
$(function(){
	if(linktypeValue==2){
		$("#btn_ctr").attr("disabled",true);
		atrshowmodal( "请在环境设置页面配置桥接模式");
	}
	$("#form").Validform({
		tiptype:2,
		ajaxPost:true,
	    tiptype:fun_tiptype,
	    tipSweep:true,
	    callback:function(data){
	    	if(data.status=="y"){
	    		//将开始扫描按钮修改成停止扫描
	    		if(data.info=="开始识别"){
	    			if (data.startIdentity==1) {
	    				$("#btn_ctr").val("停止识别");
					}
	    			//首先清空之前的数据
					$("#protocolTable").find("tr").remove();
	    			//开启定时器
	    			interval_progress(data.time);
	    		}
			}
	    	if(data.status=="s"){
	    		if (data.info=="结束识别") {
	    			if(data.startIdentity!=1){
						$("#btn_ctr").val("开始识别");
					}
				}
			}
	    }
	});

	if($("#startIdentityFlag").val()=="1"){
		$.ajax({
			type: "POST",
			url: "queryProtocolList.do",
			data:{time:rtime},
			dataType :"json",
			async: false,
			success: function(data){ 
				var lst=data.result;
				var s="";			
				for(i=0;i<data.result.length;i++){
					var IDENTYTIME =format(lst[i].IDENTYTIME, 'yyyy-MM-dd HH:mm:ss');
					s+="<tr>";
					s+="<td width='10%'>"+(count=count+1)+"</td>";
					s+="<td width='15%'>"+lst[i].PROTOCOLNAME+"</td>";
					s+="<td width='15%'>"+lst[i].SRCIP+"</td>";
					s+="<td width='15%'>"+lst[i].DSTIP+"</td>";
					if(lst[i].PORT==0){
						s+="<td width='15%'>-</td>"; 
					}else{
						s+="<td width='15%'>"+lst[i].PORT+"</td>"; 
					}
					s+="<td width='20%'>"+IDENTYTIME+"</td>";
					s+="<td width='10%'><a href=\"javaScript:void(0)\" onclick=\"deleteProtocol('"+lst[i].ID+"',this)\">删除</a></td>";
					s+="</tr>";
				}	
				$("#protocolTable").append(s);
			}
		}); 
		interval_progress(rtime);
	}
})
//毫秒转换成日期格式工具方法
var format = function(time, format){ 
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
function interval_progress(t){
	
	//开启进度条控制的定时器
	window.setTimeout(function(){
		//debugger;
		$.ajax({
			type: "POST",
			url: "getProtocol.do",
			data:{time:t},
			dataType :"json",
			success: function(data){ 
				var lst=data.result;
				var s="";
				//var count =$("#protocolTable tbody").find("tr").size()+1;
				
				for(i=0;i<data.result.length;i++){
					var IDENTYTIME =format(lst[i].IDENTYTIME, 'yyyy-MM-dd HH:mm:ss');
					s+="<tr>";
					s+="<td width='10%'>"+(count=count+1)+"</td>";
					s+="<td width='15%'>"+lst[i].PROTOCOLNAME+"</td>";
					s+="<td width='15%'>"+lst[i].SRCIP+"</td>";
					s+="<td width='15%'>"+lst[i].DSTIP+"</td>";
					if(lst[i].PORT==0){
						s+="<td width='15%'>-</td>"; 
					}else{
						s+="<td width='15%'>"+lst[i].PORT+"</td>"; 
					}
					s+="<td width='20%'>"+IDENTYTIME+"</td>";
					s+="<td width='10%'><a href=\"javaScript:void(0)\" onclick=\"deleteProtocol('"+lst[i].ID+"',this)\">删除</a></td>";
					s+="</tr>";
				}	
				$("#protocolTable").append(s);
				if($("#btn_ctr").val()=="停止识别"){
					//扫描没有结束，继续加载(传入时间)
					interval_progress(data.time);
				}
			}
		}); 
	},1000);			
}

//删除协议
function deleteProtocol(id,o){
	//将数据提交到后台，开始扫描
	$.ajax({
		type: "POST",
		url: "deleteProtocol.do",
		data: {id:id},
		dataType :"json",
		success: function(data){
//			alert(data.status);
//			alert($(o).val())
//			if(){}
			$(o).parents("tr").remove();
		}
	});
}
