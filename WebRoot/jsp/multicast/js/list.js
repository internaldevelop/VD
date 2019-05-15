dataType_obj = {
	"ip" : /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])$/,
	//"broadcastIp":/^[2](([2][4-9])|([3][0-9]))(\.(([01][0-9]{0,2})|([2][0-4][0-9]|[2][5][0-5]))){3}$/,
	"broadcastIp":/^[2](([2][4-9])|([3][0-9]))(\.(([0-9]|([1-9]\d)|(1\d\d)|(2([0-4]\d|5[0-5]))))){3}$/,
	"isBroadcastIp":function(val){
		if(null ==val || ""==$.trim(val)){
			return false;
		}
   		return this.broadcastIp.test(val);
   	}
};
$(function() {
	// ##保存多播地址
	$("#multi_form").Validform({
		tiptype : 2,
		ajaxPost : true,
		datatype : dataType_obj,
		tiptype : fun_tiptype,
		callback : function(data) {
			if (data.success) {
				$("#msg").text("成功保存").css({"color":"green"})
				window.location.reload();
			}else{
				$("#msg").text("添加失败,IP已存在!").css({"color":"red"})
			}
		}
	});
});

function createMulticast(id) {
	
	if(userName == "audit"){
		atrshowmodal("无权限");
		return;
	}	
	
	$.ajax({
		type : "POST",
		url : "deleteMulticast.do?id=" + id,
		data : $("#form2").serialize(),
		dataType : "json",
		success : function(msg) {
			if (msg.success) {
				atrshowmodal(msg.info);
				window.location.reload();
			}
		},
		error : function() {
			atrshowmodal("网络错误");
		}
	});
}

// 数据校验提示信息
function fun_tiptype(msg, o, cssctl) {
	// msg：提示信息;
	// o:{obj:*,type:*,curform:*},
	// obj指向的是当前验证的表单元素（或表单对象），type指示提示的状态，值为1、2、3、4，
	// 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, curform为当前form对象;
	// cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
	// 验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
	if (!o.obj.is("form")) {
		var objtip = o.obj.siblings(".Validform_checktip");
		cssctl(objtip, o.type);
		objtip.text(msg);
	} else {
		var objtip = o.obj.find("#msgdemo");
		cssctl(objtip, o.type);
		objtip.text(msg);
	}
}