var setting = {
	edit: {
		enable: setRemoveBtn,
		showRemoveBtn: setRemoveBtn,
		showRenameBtn: false,
		removeTitle: "删除节点",
		drag: {
			isCopy: false,
			isMove: false
		}
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onDblClick: zTreeOnDblClick,		
		beforeRemove:zTreeOnRemove,					
	}
};

function setRemoveBtn(treeId, treeNode) {
	return btnbool;
}

//确认删除 start
function zTreeOnRemove(treeId,treeNode) {  
	if(userName == "audit"){
		atrshowmodal("无权限");
		return;
	}	
	atrConfirm('是否确认删除,不可恢复!',function(){
		$.ajax({
				url:'testsetup/removeTestSuite.do',
				data:'treeId='+treeNode.id,
				dataType:'json',
				success:function(result)
				{
					if(result.success)
					{
						zTreeNodeRemove(treeNode);
					}
				}
			});
	});
	return false;
}
//确认删除 end

function zTreeNodeRemove(treeNode){
	$("#"+treeNode.tId).remove();
}

//树节点 双击事件 start
function zTreeOnDblClick(event, treeId, treeNode) 
{
	if(userName == "audit"){

		return;
	}	
	if(testStatus!=0){
		//在进行端口扫描和测试执行时，禁用
		return;
	}
	
	//判断如果为父节点不添加到测试套件信息中(中间部分)
	var treeId = "";
	var type = "";
	var installtype = "";
	var code=""
	var testnum="";
	if(!treeNode.isParent)
	{
		treeId = treeNode.id;
		type = treeNode.type;
		installtype = treeNode.installtype;
		installtype = treeNode.installtype;
		code = treeNode.code;
		testnum=treeNode.testnum;
		
		//appendUL(treeNode.id, treeNode.name, treeNode.type);	
	}
	else
	{
		var tree = treeNode.children;
		for(var i=0;i<tree.length;i++)
		{
			if((tree.length-i)==1)
			{
				treeId = treeId+tree[i].id;
				type = type+tree[i].type;
				installtype = installtype+tree[i].installtype;
				code = code+tree[i].code;
				if(null !=tree[i].testnum){
					testnum=testnum+tree[i].testnum;
				}else{
					testnum=0;
				}
			}
			else
			{
				treeId = treeId+tree[i].id+",";
				type = type+tree[i].type+",";
				installtype = installtype+tree[i].installtype+",";
				code = code+tree[i].code+",";
				if(null ==tree[i].testnum){
					testnum=0;
				}else{
					testnum=testnum+tree[i].testnum+",";
				}
			}
			//appendUL(tree[i].id, tree[i].name, tree[i].type);
		}
	}
	
	addNonameData(treeId,type,installtype,code,testnum);
	
};
//树节点 双击事件 end

//获取选中树节点 start
function onCheck()
{
	if(testStatus!=0){
		//在进行端口扫描和测试执行时，禁用
		return;
	}
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = treeObj.getSelectedNodes();
	var treeId = "";
	var type = "";
	var installtype = "";
	var code=""
	var testnum="";
	if(nodes.length ==0){
		atrshowmodal("请选择要添加的测试用例!");
	}else{
		for(var i=0;i<nodes.length;i++)
		{
			if(!nodes[i].isParent)
			{
				treeId = nodes[i].id;
				type = nodes[i].type;
				installtype =nodes[i].installtype;
				code = code+tree[i].code;
				testnum=testnum+tree[i].testnum;
				//appendUL(nodes[i].id, nodes[i].name, nodes[i].type);
				
			}
			else
			{
				var tree = nodes[i].children;
				for(var i=0;i<tree.length;i++)
				{
					if((tree.length-i)==1)
					{
						treeId = treeId+tree[i].id;
						type = type+tree[i].type;
						installtype = installtype+tree[i].installtype;
						code = code+tree[i].code;
						testnum=testnum+tree[i].testnum;
					}
					else
					{
						treeId = treeId+tree[i].id+",";
						type = type+tree[i].type+",";
						installtype = installtype+tree[i].installtype+",";
						code = code+tree[i].code+",";
						testnum=testnum+tree[i].testnum+",";
					}
					//appendUL(tree[i].id, tree[i].name, tree[i].type);
				}
			}
		}
		addNonameData(treeId,type,installtype,code,testnum);
	}
	
}
//获取选中树节点 end

//向右侧区域内添加内容是, 首先向数据库添加记录 start
function addNonameData(treeId,type,installtype,code,testnum)
{
	//页面双击后向后台追加数据
	$.ajax(
	{
		url:'testsetup/insertNonameTemplate.do',
		type:'post',
		data:{
			"treeId":treeId,
			"type":type,
			"installtype":installtype,
			"code":code,
			"testnum":testnum
		},
		dataType:'json',
		success:function(result)
		{
			if(result.success)
			{
				parent.frames["centerFrame"].$('#showName').empty();
				parent.frames["centerFrame"].$('#showName').html('');
				$.each(result.list, function(index, obj)
				{
					var html = '<ul tag="backgroundli">';
					html += '<li><input type="checkbox" name="treeIdCheckbox" checked="checked" value="'+obj.ID+'"/>'+obj.NAME+'</li>';
					html += '<li><input type="hidden" name="treeId" value="'+obj.ID+'"/></li>';
					html += '<li><input type="hidden" name="type" value="'+obj.TYPE+"@#"+obj.NAME+'"/></li>';
					html += '</ul>';
					parent.frames["centerFrame"].$('#showName').append(html);
				});
			}else{
				atrshowmodal(result.msg);
			}
		}
	});
}
//向右侧区域内添加内容是, 首先向数据库添加记录 end

//向右侧区域添加内容方法 start
function appendUL(id, name, type)
{
	var html = '<ul tag="backgroundli">';
	html += '<li><input type="checkbox" name="treeIdCheckbox" checked="checked" value="'+id+'"/>'+name+'</li>';
	html += '<li><input type="hidden" name="treeId" value="'+id+'"/></li>';
	html += '<li><input type="hidden" name="type" value="'+type+"@#"+name+'"/></li>';
	html += '</ul>';
	parent.frames["centerFrame"].$('#showName').append(html);
}
//向右侧区域添加内容方法 end
//清空模版
function doclear(){
	atrConfirm("是否确认删除?",function(){
		$.ajax(
			{
				url:'testsetup/clearTemplate.do',
				cache:false,
				success:function(result)
				{
					history.go(0);
				}
			});
	});
}
//页面初始化完成后调用方法
$(document).ready(function(){
	//type: 1为测试用例库 2为我的模板库
	var type = $('#type').val();
	$.ajax(
	{
		url:'testsetup/leftTree.do',
		data:'type='+type,
		dataType:'json',
		success:function(result)
		{
			var tree = $.fn.zTree.init($("#treeDemo"), setting, result);
			tree.removeNode(tree.getNodeByParam("name", "未命名", null));
		},
		error:function(a,b,c)
		{
			atrshowmodal("执行异常,请刷新页面后重新尝试!"+b+"   "+c);
		}
	});
	
	//左侧树上面的添加按钮点击事件 start
	/*$('#leftTreeButton').click(function()
	{
		onCheck();
	});*/
	//左侧树上面的添加按钮点击事件 end
	
});