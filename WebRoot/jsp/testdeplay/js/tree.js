	var setting = {
		asyncParam: ["id"], 
		nameCol : "name",
        async: true,
        treeNodeKey : "id", //在isSimpleData格式下，当前节点id属性  
		treeNodeParentKey : "parent", //在isSimpleData格式下，当前节点的父节点id属性  
		data: {
				simpleData: {
					enable: true
				}
			  },
		callback: {
				onDblClick: onDblClick
			  }
	     };
	var setting1 = {
			asyncParam: ["id"], 
			nameCol : "name",
	        async: true,
	        treeNodeKey : "id", //在isSimpleData格式下，当前节点id属性  
			treeNodeParentKey : "parent", //在isSimpleData格式下，当前节点的父节点id属性  
			edit: {
				enable: true
			},
			data: {
					simpleData: {
						enable: true
					}
				  },
			callback: {
					onDblClick: onDblClick
				  }
		     };
	var zNodes;
	var zNodes1;
	$(function() {
		$.ajax({
			async : false,
			cache : false,
			type : 'POST',
			data : {"type":1},   
			url : '/VD/ztree.do',
			success : function(data) {
				//alert(data);
				zNodes = data;
			},
			error : function() {
				atrshowmodal("请求失败");
			}
		});
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				data : {"type":2},   
				url : '/VD/ztree.do',
				success : function(data) {
					//alert(data);
					zNodes1 = data;
				},
				error : function() {
					atrshowmodal("请求失败");
				}
			});
	});
	
	$(document).ready(function() {
		treeNodes = eval("(" + zNodes + ")"); //将string类型转换成json   
		$.fn.zTree.init($("#tree"), setting, treeNodes);
         
         treeNodes1 = eval("(" + zNodes1 + ")"); //将string类型转换成json   
 		$.fn.zTree.init($("#templatetree"), setting1, treeNodes1);
 		var treeObj = $.fn.zTree.getZTreeObj("tree");
        treeObj.expandAll(true); //展开全部节点
	});
	function onDblClick(event, treeId, treeNode) {
		var Table = document.getElementById("table");   //取得自定义的表对象
		NewRow = Table.insertRow();  
		NewCell1= NewRow.insertCell();
		NewCell1.innerHTML = "<B>"+treeNode["name"]+"</B>";  
		var treedata={"pId":treeNode["pId"],"name":treeNode["name"],"installtype":treeNode["installtype"]};
		$.ajax({
			url : "/VD/savedeplay.do",
			type : 'POST',
			contentType:"application/json",
			data : JSON.stringify(treedata),   
			async:false,	
			success : function(data) {
			},
			error : function() {
				atrshowmodal("请求失败");
			}
		});
     }
		function save(){
			var name=document.getElementById('name').value;
			var data2={"pid":pid,"name":name,"chief":chief,"tel":tel,"remark":remark};
			
			 $.ajax({ 
	             
                url:'/WL/ztreesave.do',  
                type:'post',  
                contentType:"application/json",
                data:JSON.stringify(data2),   
                async:false,	
                success:function(data){  
                    if(data=="yes");
				   {
				   	
					window.location.href="/WL/jsp/organize/tree.jsp";
					
				   }
                } 
			});
			
			
		}
		function edit(){
			var name=document.getElementById('name').value;
			var pid =document.getElementById('id').value;//为了和add用同一个实体，此处不变，其实是修改的ID
			var chief=document.getElementById('chief').value;
			var tel=document.getElementById('tel').value;
			var remark=document.getElementById('remark').value;
			
			var data2={"pid":pid,"name":name,"chief":chief,"tel":tel,"remark":remark};
			
			 $.ajax({ 
	             
                url:'/WL/ztreeedit.do',  
                type:'post',  
                contentType:"application/json",
                data:JSON.stringify(data2),   
                async:false,	
                success:function(data){  
                  if(data=="yes");
				   {
				   	
					window.location.href="/WL/jsp/organize/tree.jsp";
					
				   }
                } 
			});
			
			
		}
		function del(){	
			atrConfirm("是否确认删除?",delaction)
		}
		
		
		function delaction(){
			var name=document.getElementById('name').value;
			var pid =document.getElementById('id').value;//为了和add用同一个实体，此处不变，其实是修改的ID
			var chief=document.getElementById('chief').value;
			var tel=document.getElementById('tel').value;
			var remark=document.getElementById('remark').value;
			var data2={"pid":pid,"name":name,"chief":chief,"tel":tel,"remark":remark};
			
			 $.ajax({ 
	             
                url:'/WL/ztreedel.do',  
                type:'post',  
                contentType:"application/json",
                data:JSON.stringify(data2),   
                async:false,	
                success:function(data){  
				
                  if(data=="yes")
				   {
					window.location.href="/WL/jsp/organize/tree.jsp";
					
				   }
				   if(data=="noorg")
				   {
					   atrshowmodal("该组织下有子项组织，禁止删除！");
					
				   }
				    if(data=="noclient")
				   {
				    	atrshowmodal("该组织下已分配客户端，禁止删除！");
					
				   }
                } 
			});
		}