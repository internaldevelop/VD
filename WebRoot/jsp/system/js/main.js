$(function() {
	dome2 = $("#forminter").Validform(
			{
				tiptype : 2,
				ajaxPost : true,
				datatype : dataType_obj,
				tiptype : fun_tiptype,
				beforeSubmit : function() {
					$("#forminter").attr(
							"action",
							$("#forminter").attr("action") + "?n="
									+ Math.random());
					return true;
				},
				callback : function(data) {
					$("#bcmsg").html(data.info);
					if(data.info=="配置失败"){
						$("#bcmsg").css("color","red");
					}else{
						$("#bcmsg").css("color","#71b83d");
					}
				}
			});

	$('#btcsShutdown').click(function() {
		atrConfirm("您确定要关机吗？",function(){
			$.ajax({
				url : basePath + '/system/setTestcsShutdown.do',
				dataType :"json",
				success: function(msg){
				
				}
			});
		})
	});
	
	$('#btcsReboot,#btcsReboot2').click(function() {
		atrConfirm("您确定要重启吗？",function(){
			$.ajax({
				url : basePath + '/system/setTestcsReboot.do',
				dataType :"json",
				success: function(msg){
					
				}
			});
		});
	});
	
	$('#btbcReboot').click(function() {
		atrConfirm("您确定要重启吗？",function(){
			$.ajax({
				url : basePath + '/system/setTestbcReboot.do',
				dataType :"json",
				success: function(msg){
				
				}
			});
		});
	});
})