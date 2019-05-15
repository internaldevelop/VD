/**
 * 漏洞js
 * @author gyk
 */
$(function(){
		$(".plus").click(function(){
			edit();
		});
	});
	
	var dial = null;
	
	function edit(id){
		var query = "";
		if(id){
			query = "?id="+id;
		}
		dial = dialog({
			title:"编辑",
			content:'<iframe id="newFream" frameborder="0" src="'+basePath+'/cnvd/edit.do'+query+'" height="500px" width="700px"></iframe>',
			cancelValue:'关闭'
		});
		dial.showModal();
	}
	
	function del(id,cnvdId){
		atrConfirm("您确定要删除“"+cnvdId+"”吗?",function(){
			location.href = basePath+"/cnvd/delete.do?id="+id;
		});
	}
	
	function close(){
		if(dial){
			dial.close().remove();
		}
	}