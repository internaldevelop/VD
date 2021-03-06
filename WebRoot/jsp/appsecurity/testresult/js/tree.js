var setting = {
	// 节点操作的设置
	edit : {
		enable : true,
		showRemoveBtn : true,
		showRenameBtn : false,
		removeTitle : "删除节点",
		drag : {
			isCopy : false,
			isMove : false
		}
	},
	view : {
		fontCss : setFontCss
	},
	data : {
		simpleData : {
			enable : true
		}
	},
	callback : {
		onClick : zTreeOnClick,
		// onNodeCreated:nodeCreate,
		//beforeRemove : zTreeBeforeRemove,
		beforeRemove : zTreeOnRemove
	}
};

// 设置颜色
function setFontCss(treeId, treeNode) {
	// var fonts = JSON.parse(treeNode.font);
	var fonts;
	if (treeNode.c != null) {
		fonts = {
			color : "red"
		};
	} else {
		fonts = {};
	}
	return fonts;
};

var treeAllPrentId;

// 确认删除 start
function zTreeOnRemove(treeId, treeNode) {
	atrConfirm('是否确认删除,不可恢复!',function(){
		$.ajax({
			url : 'apptestResult/deleteNode.do',
			type : 'POST',
			data : JSON.stringify({
				id : treeNode.id
			}),
			contentType : "application/json",
			dataType : 'json',
			success : function(result) {
				if (result) {
					$("#"+treeNode.tId).remove();
				}else{
					atrshowmodal('删除失败!');
				}
			}
		});
	});
	return false;
}
// 确认删除 end

var zTree;
function nodeCreate(event, treeId, treeNode) {
	// if(treeNode.level == 0){
	// treeNode.color = "red";
	// }else{
	//		
	// }
	// alert(treeNode.name);
	// if(treeNode.c != null){
	// zTree.setting.view.fontCss["color"] = "red";
	treeNode.font = {
		'color' : 'red'
	};
	zTree.updateNode(treeNode);
	zTree.updateNode(treeNode.getParentNode());
	// }else{
	// zTree.setting.view.fontCss["color"] = "";
	// zTree.updateNode(treeNode);
	// zTree.updateNode(treeNode.getParentNode());
	// }

}
var selectNodeId = null;
var selectNode = null;
// 获取树节点点击事件 start
function zTreeOnClick(event, treeId, treeNode) {
	// 禁用导出,删除数据包按钮
	disabledButton(treeNode);
	// checkpdf(treeNode.id);
	// 把pdf文件传给右侧页面显示
	if (treeNode.isParent) {
		$.ajax({
			url : basePath + 'apptestResult/createPdf.do',
			data : 'id=' + treeNode.id,
			dataType : 'json',
			success : function(result) {
				if (result.success) {
					if (navigator.userAgent.indexOf('Firefox') >= 0) {
						parent.rightFrame.postMessage(treeNode.id, '*');
					} else {
						parent.rightFrame.leftshowpdf(treeNode.id);
					}
				}
			}
		});
		selectNodeId = treeNode.id;
	} else {
		if (navigator.userAgent.indexOf('Firefox') >= 0) {
			parent.rightFrame.postMessage(treeNode.id, '*');
		} else {
			// 向右侧展示pdf
			parent.rightFrame.leftshowpdf(treeNode.id);
		}
		// 向全局设置参数
		selectNodeId = treeNode.id;
		// 默认展示第一个图形
		//window.parent.frames[3].param = 1;
		//window.parent.frames[3].$('.tab-nav-action').trigger("click");
		// window.parent.buttomFrame.container0();
	}
};
// 获取树节点点击事件 end

function checkpdf(id) {
	$.ajax({
		url : basePath + 'apptestResult/showPdf.do',
		data : 'path=' + basePdfPath + id,
		dataType : 'json',
		success : function(result) {
			if (result.success) {
				$('#exprotPdf').attr('disabled', null);
			} else {
				$('#exprotPdf').attr('disabled', true);
			}
		}
	});
}

// 禁用导出,删除数据包按钮 start
function disabledButton(treeNode) {
	if (treeNode.del == 1) {
		$('#exprotPdf').attr('disabled', true);
		$('#exprotPdf').attr('title', '数据包已删除!');
	} else if (treeNode.isParent) {
		$('#exprotPdf').attr('disabled', true);
		$('#exprotPdf').attr('title', '父节点不可操作!');
	} else {
		$('#exprotPdf').attr('disabled',false);
		$('#exprotPdf').attr('title', '');
	}
}
// 禁用导出,删除数据包按钮 end

// 获取选中树节点 start
function onCheck() {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = treeObj.getSelectedNodes();
	var html = '';
	for (var i = 0; i < nodes.length; i++) {
		var type = 1;
		if (nodes[i].isParent) {
			type = 2;
		}
		selectNode = nodes[i];
		return "?id=" + nodes[i].id + "&type=" + type;
	}
	atrshowmodal('请选择节点进行导出!');
	return false;
}
// 获取选中树节点 end

// 页面初始化完成后调用方法，展示树状结构
$(document).ready(function() {
	// type: 1为测试用例库 2为我的模板库 3为测试执行结果
	var type = $('#type').val();
	$.ajax({
		url : 'apptestResult/findParentNode.do?a=' + Math.random(),
		data : 'type=' + type,
		dataType : 'json',
		success : function(result) {
			zTree = $.fn.zTree.init($("#treeDemo"), setting, result);
			zTree.expandAll(true);
			treeAllPrentId = result[0].pId;
		},
		error : function(a, b, c) {
			atrshowmodal("执行异常,请刷新页面后重新尝试!" + b + "   " + c);
		}
	});

	// 点击导出PDF生成图 start
	$('#exprotPdf').click(function() {
		var param = onCheck();
		if (param) {
			if (param.indexOf("type=1")) {
				window.open(basePath + 'apptestResult/exprotPdf.do' + param);
			}
		}
	});
	// 点击导出PDF生成图 end

});