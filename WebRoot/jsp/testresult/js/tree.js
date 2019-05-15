var setting = {
	edit: {
		enable: true,
		showRemoveBtn: true,
		showRenameBtn: false,
		removeTitle: "删除节点",
		drag: {
			isCopy: false,
			isMove: false
		}
	},
	view :{ 
		fontCss: setFontCss
    },
	data: {
		simpleData: {
			enable: true
		},
		keep: {
			parent: true
		}
	},
	callback: {
		onClick: zTreeOnClick,
//		onNodeCreated:nodeCreate,
	/*	beforeRemove: zTreeBeforeRemove,*/
		beforeRemove: zTreeOnRemove
	}
};

//设置颜色
function setFontCss(treeId, treeNode) {
//	var fonts = JSON.parse(treeNode.font);
	var fonts;
	if(treeNode.c != null){
		fonts = {color:"red"};
	}else{
		fonts = {};
	}
	return fonts;
};

var treeAllPrentId;



//确认删除 start
function zTreeOnRemove(treeId,treeNode) {
	if(userName == "audit"){
		atrshowmodal("无权限");
		return;
	}
	
	atrConfirm('是否确认删除,不可恢复!',function(){
		$.ajax({url:'testresult/deleteParentNode.do',
				data:'id='+treeNode.id+"&parent="+treeNode.isParent,
				dataType:'json',
				success:function(result)
				{
					if(result.success)
					{
						$("#"+treeNode.tId).remove();
					}
				}
			});
	});
	return false;
}
//确认删除 end

var zTree;
function nodeCreate(event, treeId, treeNode){
//	if(treeNode.level == 0){
//		treeNode.color = "red";
//	}else{
//		
//	}
//	atrshowmodal(treeNode.name);
//	if(treeNode.c != null){
//		zTree.setting.view.fontCss["color"] = "red";
		treeNode.font={'color':'red'};
		zTree.updateNode(treeNode);
		zTree.updateNode(treeNode.getParentNode());
//	}else{
//		zTree.setting.view.fontCss["color"] = "";
//		zTree.updateNode(treeNode);
//		zTree.updateNode(treeNode.getParentNode());
//	}	

}
var selectNodeId=null;
var selectNode=null;
//获取树节点点击事件 start
function zTreeOnClick(event, treeId, treeNode) {
    //禁用导出,删除数据包按钮
    disabledButton(treeNode);
    //checkpdf(treeNode.id);
    //把pdf文件传给右侧页面显示
    console.log(treeNode);
    if(treeNode.isParent)
    {
	    	$.ajax(
	    	{
	    		url: basePath+'/testresult/createPdf.do',
	    		data: 'id='+treeNode.id,
	    		dataType: 'json',
	    		success: function(result)
	    		{
	    			if(result.success)
	    			{
	    				if (navigator.userAgent.indexOf('Firefox') >= 0)
						{
							parent.rightFrame.postMessage(treeNode.id, '*');
				    	}
				    	else
				    	{
				    		parent.rightFrame.leftshowpdf(treeNode.id);
				    	}
	    			}
	    		}
	    	});
	    	//parent.rightFrame.leftshowpdf(treeNode.id);
	    	selectNodeId=treeNode.id;
    }
    else
    {
    	$.ajax({
    		url: basePath+'/testresult/childCreatePdf.do',
    		data: 'id='+treeNode.id,
    		dataType: 'json',
    		success: function(result)
    		{
    			if (navigator.userAgent.indexOf('Firefox') >= 0)
    			{
    				parent.rightFrame.postMessage(treeNode.id, '*');
    	    	}
    	    	else
    	    	{
    	    		//向右侧展示pdf
    	    		parent.rightFrame.leftshowpdf(treeNode.id);
    	    	}
    		}
		 });
    	
    	/*if (navigator.userAgent.indexOf('Firefox') >= 0)
		{
			parent.rightFrame.postMessage(treeNode.id, '*');
    	}
    	else
    	{
    		//向右侧展示pdf
    		parent.rightFrame.leftshowpdf(treeNode.id);
    	}
    	//向全局设置参数
    	selectNodeId=treeNode.id;
    	//默认展示第一个图形
    	window.parent.frames[3].param = 1;
    	window.parent.frames[3].$('.tab-nav-action').trigger("click");*/
    	//window.parent.buttomFrame.container0();
    }
};
//获取树节点点击事件 end

function checkpdf(id)
{
	$.ajax(
	{
		url:basePath+'testresult/showPdf.do',
		data:'path='+basePdfPath+id,
		dataType:'json',
		success:function(result)
		{
			if(result.success)
			{
				$('#exprotPdf').attr('disabled', null);
			}
			else
			{
				$('#exprotPdf').attr('disabled', true);
			}
		}
	});
}

//禁用导出,删除数据包按钮 start
function disabledButton(treeNode)
{
	if(treeNode.del == 1)
	{
    	$('#exprotData').attr('disabled', true);
    	$('#deleteData').attr('disabled', true);
    	$('#exprotData').attr('title', '数据包已删除!');
    	$('#deleteData').attr('title', '数据包已删除!');
	}
    else if(treeNode.isParent)
	{
    	$('#exprotData').attr('disabled', true);    	    	
    	$('#deleteData').attr('disabled', true);
    	$('#exprotData').attr('title', '父节点不可操作!');
    	$('#deleteData').attr('title', '父节点不可操作!');
	}
	else
	{
		$('#exprotData').attr('disabled', null);
		$('#deleteData').attr('disabled', null);
		$('#exprotData').attr('title', '');
		$('#deleteData').attr('title', '');
	}
	
	var name =treeNode.name;
	if(name.indexOf("风暴") >= 0){
		$("#exprotData").prop("disabled",true);
		$('#deleteData').prop("disabled",true);
	}else{
		$("#exprotData ").prop("disabled",false);
		$("#deleteData").prop("disabled",false);
	}
	
	if(userName == "audit"){
        $("#deleteData").attr("disabled","disabled");            
    }
}
//禁用导出,删除数据包按钮 end

//获取选中树节点 start
function onCheck()
{
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = treeObj.getSelectedNodes();
	var html = '';
	for(var i=0;i<nodes.length;i++)
	{
		var type = 1;
		if(nodes[i].isParent)
		{
			type = 2;
		}
		selectNode = nodes[i];
		return "?id="+nodes[i].id+"&type="+type;
	}
	atrshowmodal('请选择节点进行导出!');
	return false;
}
//获取选中树节点 end

//页面初始化完成后调用方法
$(document).ready(function(){
	//type: 1为测试用例库 2为我的模板库 3为测试执行结果
	var type = $('#type').val();
	$.ajax(
	{
		url:'testresult/findParentNode.do?a='+Math.random(),
		data:'type='+type,
		dataType:'json',
		success:function(result)
		{	
			zTree=$.fn.zTree.init($("#treeDemo"), setting, result);
			zTree.expandAll(false);
			$("#treeDemo > li > a > span.ico_docu").addClass("ico_close").removeClass("ico_docu");
			treeAllPrentId = result[0].pId;
		},
		error:function(a,b,c)
		{
			atrshowmodal("执行异常,请刷新页面后重新尝试!"+b+"   "+c);
		}
	});
	
	//点击导出EXCEL生成图 start
	$('#exprotExcel').click(function()
	{
		var param = onCheck();
		if(param)
		{
			window.open(basePath+'/testresult/exprodExcel.do'+param);
		}
	});
	//点击导出EXCEL生成图 end
	
	//点击导出PDF生成图 start
	$('#exprotPdf').click(function()
	{
		var param = onCheck();
		if(param){	
			if(param.indexOf("type=2")){
				window.open(basePath+'/testresult/exprotPdf.do'+param);
			}
		}
	});
	//点击导出PDF生成图 end
	
	//点击导出数据包 start
	$('#exprotData').click(function()
	{
		var param = onCheck();
		if(param){	
			if(param.indexOf("type=2") >= 0){
				atrshowmodal('请选择子节点测试用例导出!');
			}
			else
			{
				window.open(basePath+'/testresult/exprotData.do'+param);
			}
		}
	});
	//点击导出数据包 end
	
	//点击删除数据包 start
	$('#deleteData').click(function()
	{
		var param = onCheck();
		if(param){
			$.ajax(
			{
				url:basePath+'/testresult/deleteData.do'+param,
				dataType:'json',
				success:function(result)
				{
					if(result.success)
					{
						atrshowmodal('删除成功!');
						selectNode.del = 1;
						disabledButton(selectNode);
					}
				}
			});
			//window.open(basePath+'/testresult/deleteData.do'+param);
		}
	});
	//点击删除数据包 end
	
});