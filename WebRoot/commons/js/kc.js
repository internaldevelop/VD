dataType_obj={
    "ip":/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])$/,
	"equipmentName":/^[0-9a-zA-Z\w\d\u4e00-\u9fff-\_]{1,32}$/,
	"equipmentVersion":/^[0-9a-zA-Z\w\d\u4e00-\u9fa5- \-]{0,32}$/,
	"subnetMask":/^(254|252|248|240|224|192|128|0)\.0\.0\.0$|^(255\.(254|252|248|240|224|192|128|0)\.0\.0)$|^(255\.255\.(254|252|248|240|224|192|128|0)\.0)$|^(255\.255\.255\.(254|252|248|240|224|192|128|0))$/,
	"port":/^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/,
	"timeout":/^[1-9]\d*$/,
	"portRange":function(gets,obj,curform,regxp){
		//参数gets是获取到的表单元素值，obj为当前表单元素，curform为当前验证的表单，regxp为内置的一些正则表达式的引用;
		var reg1=/^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
		
		if(!reg1.test(gets)){return false;}
		
		if($("#endPort").val()==""){
			return true;
		}
		
		var n1=parseInt($("#beginPort").val());
		var n2=parseInt($("#endPort").val());
		if(isNaN(n1) || isNaN(n2)){
			return false;
		}
		if(n1>n2){
			return "请填写正确的端口范围";
		}
		return true;
		
		//注意return可以返回true 或 false 或 字符串文字，true表示验证通过，返回字符串表示验证失败，字符串作为错误提示显示，返回false则用errmsg或默认的错误提示;
	},
	"cyclePeriod":function(gets,obj,curform,regxp){
		//离散监视器，周期
		if(gets>=300){
			return true;
		}
		return "最小输入值为300";
		
		//注意return可以返回true 或 false 或 字符串文字，true表示验证通过，返回字符串表示验证失败，字符串作为错误提示显示，返回false则用errmsg或默认的错误提示;
	},
	alarmLevel:function(val){
		if(val > 10 || val < 1){
			return "输入范围1-10";
		}
		if(!(/^[0-9]*$/.test(val))){
			return "请填数字";
		}
		return true;
	}
};
//数据校验提示信息	
function fun_tiptype(msg,o,cssctl){
	if("status: 0; statusText: error" == msg){
		msg = "网络错误";
	}
	//msg：提示信息;
	//o:{obj:*,type:*,curform:*}, obj指向的是当前验证的表单元素（或表单对象），type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, curform为当前form对象;
	//cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
	if(!o.obj.is("form")){//验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
		var objtip=o.obj.siblings(".Validform_checktip");
		cssctl(objtip,o.type);
		objtip.text(msg);
	}else{
		var objtip=o.obj.find("#msgdemo");
		cssctl(objtip,o.type);
		objtip.text(msg);
	}
}
function clearMsg(o){
	window.setTimeout(function(){
		o.html("");
	},2000);
}