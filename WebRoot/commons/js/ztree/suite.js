String.prototype.trim=function(){
　　    return this.replace(/(^\s*)|(\s*$)/g, "");
　　 }

$(function()
{
	
	// 页面加载完成后,隐藏套件详细配置
	$('#middlediv').hide();
	
	// 页面加载完成后,查询是否存在 未命名模板 start
	$.ajax(
	{
		url:basePath+'/testsetup/findDefaultTestSuite.do',
		type:'post',
		dataType:'json',
		cache:false,
		success:function(result)
		{
			$.each(result, function(index, obj)
			{
				var html = '<ul tag="backgroundli">';
				html += '<li><input type="checkbox" name="treeIdCheckbox" checked="checked" value="'+obj.ID+'"/>'+obj.NAME+'</li>';
				html += '<li><input type="hidden" name="treeId" value="'+obj.ID+'"/></li>';
				html += '<li><input type="hidden" name="type" value="'+obj.TYPE+"@#"+obj.NAME+'"/></li>';
				html += '</ul>';
				$('#showName').append(html);
			});
		}
	});
	// 页面加载完成后,查询是否存在 未命名模板 end
	
	// 页面加载完成后 禁用一部分 输入框 start
	$(document).delegate('input[type=radio]', 'change', function()
	{
		disabledRadio();
	});
	// 页面加载完成后 禁用一部分 输入框 end
	
	function disabledRadio()
	{
		// 风暴测试显示------------
		// 测试速率
		var TESTRATE = $("input[name='TESTRATE']:checked").attr('value');
		if(TESTRATE == 1488000)
		{
			$('#TESTRATES').attr('disabled', true);
		}
		else
		{
			$('#TESTRATES').attr('disabled', null);
		}
		
		// 测试时间
		var TESTTIME = $("input[name='TESTTIME']:checked").attr('value');
		if(TESTTIME == 120)
		{
			$('#TESTTIMES').attr('disabled', true);
		}
		else
		{
			$('#TESTTIMES').attr('disabled', null);
		}
		// 风暴测试显示------------
		
		// 语法测试显示------------
		// 起始测试用例
		var STARTTESTCASE = $("input[name='STARTTESTCASE']:checked").attr('value');
		if(STARTTESTCASE == 0)
		{
			$('#STARTTESTCASES').attr('disabled', true);
		}
		else
		{
			$('#STARTTESTCASES').attr('disabled', null);
		}
		// 终止测试用例
		var ENDTESTCASE = $("input[name='ENDTESTCASE']:checked").attr('value');
		if(ENDTESTCASE == 0)
		{
			$('#ENDTESTCASES').attr('disabled', true);
		}
		else
		{
			$('#ENDTESTCASES').attr('disabled', null);
		}
		// 语法测试显示------------
	}
	
	function trim(str){ // 删除左右两端的空格
　　     return str.replace(/(^\s*)|(\s*$)/g, "");  
　　 }
	
	// 中间区域保存我的模板库 start
	$('#centerSaveSuite').click(function()
	{
		// 1.首先对名称进行验证
		var name = $('#name').val();
		var flag = /['"#$%&\^~*]/.test( name );
		if(flag || trim(name) == "" || name.length > 16)
		{
			atrshowmodal('输入模板名称有误!');
			return false;
        }
		var form = $('#suiteForm').serialize();
		$.ajax(
		{
			url:basePath+'/testsetup/saveTestSuite.do',
			type:'post',
			data:form+'&name='+name,
			dataType:'json',
			success:function(result)
			{
				$('#page_form').hide();
				if(result.success)
				{
					atrshowmodal(name+' 添加成功!');
					parent.frames["leftFrameDown"].location.reload();
					$('#name').val('');
					$('#showName').empty();
					$('#showName').html('');
				}
				else
				{
					atrshowmodal(result.msg);
				}
			}
		});
		
	});
	// 中间区域保存我的模板库 end
	
	// 中间区域删除我的模板库 start
	$('#centerRemoveSuite').click(function()
	{
		var len = $('input[name=treeIdCheckbox]:checked').length;
		if(len == 0)
		{
			atrshowmodal('请选择要删除的节点!');
			return false;
		}
		$('input[name=treeIdCheckbox]:checked').each(function(index, obj){
			$.ajax(
			{
				url:basePath+'testsetup/removeTemplate.do',
				data:'treeId='+$(obj).val(),
				dataType:'json',
				success:function(result)
				{
					if(result.success)
					{
						$('#page_form').hide();
						$(obj).parent().parent().remove();
					}
				}
			});
		});
		// $('#showName').html('');
	});
	// 中间区域删除我的模板库 end
	
	// 中间区域全部删除我的模板库 start
	$('#centerRemoveSuiteAll').click(function()
	{
		$.ajax(
		{
			url:basePath+'testsetup/removeTemplate.do',
			data:'treeId=all',
			dataType:'json',
			cache:false,
			success:function(result)
			{
				$('#page_form').hide();
				if(result.success)
				{
					$('#showName').empty();
					$('#showName').html('');
				}
			}
		});
		
	});
	// 中间区域全部删除我的模板库 end
	
	// 中间区域 执行测试用例 start
	$('#centerExecuteSuite').click(function()
	{
		var exp = $('#showName').find('ul:first');
		if (exp.length==0){
			atrshowmodal("测试套件列表为空！不能执行！");
			return false;
		}
		
		$.ajax(
		{
			url:basePath+'/testsetup/findDefaultTestSuiteId.do',
			type:'post',
			dataType:'json',
			success:function(result)
			{
				atrshowmodal(result[0].ID);
				parent.window.location=basePath+"/testexecute/execute.do?testId="+result[0].ID;
			}
		});
	});
	// 中间区域 执行测试用例 end
	
	// 测试套件信息保存 start
	$('#saveSuite').click(function()
	{
		var testtype = $('#TESTTYPEN').val();
		var proname = $('#PRONAME').val();
		if(testtype == '3'){
			if(trim(proname) == ""){
				atrshowmodal('协议名称不能为空!');
				return false;
			}
			
			for(var i=0;i<num;i++){
				var tempv = $("[id='ltcus["+i+"].FIELDVALUE']").val();
				var tempenum = tempv.split(",");
				for(var j=0;j<tempenum.length;j++){
					if(tempenum[j].trim()=="" || tempenum[j]==null){
						atrshowmodal($("[id='ltcus["+i+"].FIELDNAME']").val()+"的有效值不规范！");
						$("[id='ltcus["+i+"].FIELDVALUE']").focus();
						return false;
					}else{
						var temprand = tempenum[j].split("-");
						if(temprand.length != 1){
						if(temprand.length>2){
							atrshowmodal($("[id='ltcus["+i+"].FIELDNAME']").val()+"的有效值不规范！");
							$("[id='ltcus["+i+"].FIELDVALUE']").focus();
							return false;
						}
						if(temprand[0].trim()=="" || temprand[1].trim()==""){
							atrshowmodal($("[id='ltcus["+i+"].FIELDNAME']").val()+"的有效值不规范！");
							$("[id='ltcus["+i+"].FIELDVALUE']").focus();
							return false;
						}
						if(Number(temprand[0]) > Number(temprand[1])){
							atrshowmodal($("[id='ltcus["+i+"].FIELDNAME']").val()+"的有效值不规范！");
							$("[id='ltcus["+i+"].FIELDVALUE']").focus();
							return false;
						}
						}
					}
				}
			}
		}		
		
		if(testtype == '1'){
			if($("input[name='TESTRATE'][tvn='1']:checked").val() == 1)
			{
				var name = $('#TESTRATES').val();
				if(trim(name) == "" || name.length > 16)
				{
					atrshowmodal('测试速率(包/秒) 输入有误!');
					return false;
				}
			}
		}
		
		if(testtype == '3'){
			if($("input[name='TESTRATE'][tvn='1']:checked").val() == 1)
			{
				var name = $('#TESTRATES').val();
				if(trim(name) == "")
				{
					atrshowmodal('测试速率(包/秒)  不能为空!');
					return false;
				}
				
				var namet = Number(name);
				if(namet<1 || namet > 10)
				{
					atrshowmodal('测试速率(包/秒)  取值范围是：1 -- 10!');
					return false;
				}
			}else{
				$('#TESTRATE').val('1');
			}
		}
		
		if(testtype == '4'){
			if($("input[name='TESTRATE'][tvn='1']:checked").val() == 1)
			{
				var name = $('#TESTRATES').val();
				if(trim(name) == "")
				{
					atrshowmodal('测试速率(包/秒) 不能为空!');
					return false;
				}
				
				var namet = Number(name);
				if(namet<1 || namet > 100)
				{
					atrshowmodal('测试速率(包/秒) 取值范围是：1 -- 100!');
					return false;
				}
			}else{
				$('#TESTRATE').val('1');
			}
		}
		
		if(testtype == '1'){
			if($("input[name=TESTTIME]:checked").val() == 1)
			{
				var name = $('#TESTTIMES').val();
				if(trim(name) == "" || name.length > 16)
				{
					atrshowmodal('测试时间 输入有误!');
					return false;
				}
			}
		}
		
		if(testtype == '2' || testtype == '5'){// 语法测试验证
			if($("input[name=STARTTESTCASE]:checked").val() == 1)
			{
				var name = $('#STARTTESTCASES').val();
				if(trim(name) == "" || name.length > 16)
				{
					atrshowmodal('起始测试用例 Production 输入有误!');
					return false;
				}
			}
			if($("input[name=ENDTESTCASE]:checked").val() == 1)
			{
				var name = $('#ENDTESTCASES').val();
				if(trim(name) == "" || name.length > 16)
				{
					atrshowmodal('终止测试用例 Production 输入有误!');
					return false;
				}
			}
		}
		
		if(testtype == '4'){		
			var SMESSAGE = $("#SMESSAGE").val();
			if(trim(SMESSAGE) == ""){
				atrshowmodal('发送报文数   不能为空!');
				return false;
			}
			var sm = Number(SMESSAGE);

			if(sm <50000 || sm > 1000000)
			{			
				atrshowmodal('发送报文数   取值范围是：50,000 -- 1,000,000!');
				return false;
			}
		}
		
		if(testtype == '3'){		
			var STOTAL = $("#STOTAL").val();
			if(trim(STOTAL) == ""){
				atrshowmodal('总发包数   不能为空!');
				return false;
			}
			
			var sm = Number(STOTAL);

			if(sm <0 || sm > 65535)
			{			
				atrshowmodal('总发包数   取值范围是：0 -- 65535!');
				return false;
			}
		}
		
		if(testtype == '4'){
			var EMESSAGEP = $("#EMESSAGEP").val();
			if(trim(EMESSAGEP) == ""){
				atrshowmodal('错误报文比例   不能为空!');
				return false;
			}
			
			var sf = Number(EMESSAGEP);

			if(sf <0 || sf > 100)
			{			
				atrshowmodal('错误报文比例   取值范围：0% -- 100%!');
				return false;
			}
		}
		
		if(testtype == '3' || testtype == '5'){
			var EMESSAGEP = $("#PORTSEND").val();
			if(trim(EMESSAGEP) == ""){
				atrshowmodal('端口号   不能为空!');
				return false;
			}
			
			var sf = Number(EMESSAGEP);

			if(sf <0 || sf > 65535)
			{			
				atrshowmodal('端口号  取值范围：0 -- 65535!');
				return false;
			}
		}
		
		var form = $('#page_form').serialize();
		$.ajax(
		{
			url:basePath+'/testsetup/saveTestSuiteDetail.do',
			type:'post',
			data:form,
			dataType:'json',
			success:function(result)
			{
				if(result.success)
				{
					if(testtype == '3'){
					$(selectli).children(":first")[0].lastChild.data = $("#PRONAME").val();
					var kk = $(selectli).children(":last").children(":first").val().split("@#");
					$(selectli).children(":last").children(":first").val(kk[0]+"@#"+$("#PRONAME").val());
					}
					atrshowmodal("添加成功!");
				}
			}
		});
	});
	// 测试套件信息保存 end
	
	// 没有点击保存,当网页离开是 自动添加 未命名的 模板 start
// $(window).bind('beforeunload',function(){
// //检测页面是否有数据,没有数据不发送请求
// var len = $('input[name=treeId]').length;
// if(len == 0)
// {
// return;
// }
// var form = $('#suiteForm').serialize();
// $.ajax(
// {
// url:basePath+'/testsetup/saveTestSuite.do',
// type:'post',
// data:form+'&name=\u672a\u547d\u540d',
// dataType:'json',
// success:function(result)
// {
// if(result.success)
// {
// atrshowmodal(name+' 添加成功!');
// parent.frames["leftFrameDown"].location.reload();
// }
// }
// });
// });
	// 没有点击保存,当网页离开是 自动添加 未命名的 模板 end
	var selectli;
	// 点击UL更换样式 start
	$(document).delegate('ul[tag=backgroundli]', 'click', function()
	{
		var obj = $(this);
		// 清空所有样式
		$('ul[tag=backgroundli]').each(function(index, obj)
		{
			$(this).css('background-color','#EAEAEA');
		});
		
		// 为选中的单独添加样式
		var TESTDEPLAYID = $(obj).children(":first").children(":first").val();
		$('#TESTDEPLAYID').val(TESTDEPLAYID);
		$(obj).css('background-color','#C2C2C2');
		
		// 获取子节点ID的值 1:风暴 2:语法
		var liobj = $(this).children("li:last-child");
		var objtemp = $(liobj).children("input:first-child").val().split("@#");
		var typeid = objtemp[0];
		$('#middlediv').show();
		selectli= $(this);
		switch (Number(typeid))
		{
		case 1:
			$("#casetitle")[0].innerHTML="风暴测试显示"; 
			$("#rtestratetext")[0].innerHTML="测试速率(包/秒):"; 
// $("#rsmessagel")[0].innerHTML="发送报文个数:";
			
			$('#rtestrate1').show();
			$('#rtestrate2').show();
			$('#rtesttime1').show();
			$('#rtesttime2').show();

			$('#rstarttestcase1').hide();
			$('#rstarttestcase2').hide();
			$('#rendtestcase1').hide();
			$('#rendtestcase2').hide();
			$('#rtraceability1').hide();
			$('#rtraceability2').hide();
			$('#rhurryup').hide();
			$('#rtarget').show();
			$('#rpower').show();

			$('#rsmessage').hide();
			$('#remessagep').hide();
			$('#rportsend').hide();

			$('#rname').hide();
			$('#raddpro').hide();
			$('#tpro').hide();
	
		  break;
		case 2:
			$("#casetitle")[0].innerHTML="语法测试显示"; 
// $("#rsmessagel")[0].innerHTML="发送报文个数:";
			
			$('#rtestrate1').hide();
			$('#rtestrate2').hide();
			$('#rtesttime1').hide();
			$('#rtesttime2').hide();

			$('#rstarttestcase1').show();
			$('#rstarttestcase2').show();
			$('#rendtestcase1').show();
			$('#rendtestcase2').show();
			$('#rtraceability1').show();
			$('#rtraceability2').show();
			$('#rhurryup').show();
			$('#rtarget').show();
			$('#rpower').show();

			$('#rsmessage').hide();
			$('#remessagep').hide();
			$('#rportsend').hide();

			$('#rname').hide();
			$('#raddpro').hide();
			$('#tpro').hide();
	
		  break;
		case 3:
			$("#casetitle")[0].innerHTML="自定义显示";	
			$("#rtestratetext")[0].innerHTML="连接速率(次/秒):"; 
// $("#rsmessagel")[0].innerHTML="数据包长度:";
			
			$('#rtestrate1').show();
			$('#rtestrate2').show();
			$('#rtesttime1').hide();
			$('#rtesttime2').hide();

			$('#rstarttestcase1').hide();
			$('#rstarttestcase2').hide();
			$('#rendtestcase1').hide();
			$('#rendtestcase2').hide();
			$('#rtraceability1').show();
			$('#rtraceability2').show();
			$('#rhurryup').show();
			$('#rtarget').show();
			$('#rpower').show();

			$('#rsmessage').show();
			$('#remessagep').hide();
			$('#rportsend').show();

			$('#rname').show();
			$('#raddpro').show();
			$('#tpro').show();
		
		  break;
		case 4:
			$("#casetitle")[0].innerHTML="Fuzzer测试显示"; 
			$("#rtestratetext")[0].innerHTML="测试速率(包/秒):"; 
// $("#rsmessagel")[0].innerHTML="发送报文个数:";
			
			$('#rtestrate1').show();
			$('#rtestrate2').show();
			$('#rtesttime1').hide();
			$('#rtesttime2').hide();

			$('#rstarttestcase1').hide();
			$('#rstarttestcase2').hide();
			$('#rendtestcase1').hide();
			$('#rendtestcase2').hide();
			$('#rtraceability1').show();
			$('#rtraceability2').show();
			$('#rhurryup').show();
			$('#rtarget').show();
			$('#rpower').show();

			$('#rsmessage').show();
			$('#remessagep').show();
			$('#rportsend').hide();

			$('#rname').hide();
			$('#raddpro').hide();
			$('#tpro').hide();
		  break;
		 case 5:
			 $("#casetitle")[0].innerHTML="语法测试显示"; 
			 $("#rtestratetext")[0].innerHTML="测试速率(包/秒):"; 
// $("#rsmessagel")[0].innerHTML="发送报文个数:";
			 
			 $('#rtestrate1').hide();
			$('#rtestrate2').hide();
			$('#rtesttime1').hide();
			$('#rtesttime2').hide();

			$('#rstarttestcase1').show();
			$('#rstarttestcase2').show();
			$('#rendtestcase1').show();
			$('#rendtestcase2').show();
			$('#rtraceability1').show();
			$('#rtraceability2').show();
			$('#rhurryup').show();
			$('#rtarget').show();
			$('#rpower').show();

			$('#rsmessage').hide();
			$('#remessagep').hide();
			$('#rportsend').show();

			$('#rname').hide();
			$('#raddpro').hide();
			$('#tpro').hide();	
			break;
		}
		
		// 禁用部分按钮
		disabledRadio();
		
		// 根据ID查找是否存在数据
		findSuiteDetail(TESTDEPLAYID, TESTDEPLAYID,typeid,objtemp[1]);
	});
	// 点击UL更换样式 end
	
	// 点击 测试套件节点 去后台查询是否已经存在 测试套件详细信息 start
	function findSuiteDetail(testDeplayId, TESTDEPLAYID,TESTTYPEN,PRONAME)
	{
		$('#page_form').show();
		$.ajax(
		{
			url:basePath+'/testsetup/findTestSuiteDetail.do',
			type:'post',
// data:'testDeplayId='+testDeplayId,
			data:{
				"testDeplayId":testDeplayId,
                "testtypen":TESTTYPEN
			},
			dataType:'json',
			success:function(result)
			{
				// 每次进入页面 清空表单
				$('#page_form')[0].reset();
				
				$('#TESTDEPLAYID').val(TESTDEPLAYID);
				$('#TESTTYPEN').val(TESTTYPEN);
				
				// 开始准备再入页面数据
				
					var obj = result.detail[0];
				// 风暴测试显示------------
				// 测试速率
				if(TESTTYPEN == 1){
					if(obj.TESTRATE == 1488000)
					{
						$('#TESTRATES').attr('disabled', true);
						$("input[name='TESTRATE'][tvn='0']").attr('checked', true);
					}else{
						$("input[name='TESTRATE'][tvn='1']").attr('checked', true);
						$('#TESTRATES').attr('disabled', null);
						$('#TESTRATES').val(obj.TESTRATE);
					}
				}
				
				if(TESTTYPEN == 3 || TESTTYPEN == 4){
					if(obj.TESTRATE == 1)
					{
						$('#TESTRATES').attr('disabled', true);
						$("input[name='TESTRATE'][tvn='0']").attr('checked', true);
						$("input[name='TESTRATE'][tvn='0']").val('1');
					}else{
						$("input[name='TESTRATE'][tvn='1']").attr('checked', true);
						$('#TESTRATES').attr('disabled', null);
						$('#TESTRATES').val(obj.TESTRATE);
					}
				}
				
				// 测试时间
				if(obj.TESTTIME == 120)
				{
					$('#TESTTIMES').attr('disabled', true);
					$("input[name='TESTTIME'][tvn='0']").attr('checked', true);
				}
				else
				{
					$("input[name='TESTTIME'][tvn='1']").attr('checked', true);
					$('#TESTTIMES').attr('disabled', null);
					$('#TESTTIMES').val(obj.TESTTIME);
				}
				// 风暴测试显示------------
				
				// 语法测试显示------------
				// 起始测试用例
				if(obj.STARTTESTCASE == 0)
				{
					$('#STARTTESTCASES').attr('disabled', true);
					$("input[name='STARTTESTCASE'][tvn='0']").attr('checked', true);
				}
				else
				{
					$("input[name='STARTTESTCASE'][tvn='1']").attr('checked', true);
					$('#STARTTESTCASES').attr('disabled', null);
					$('#STARTTESTCASES').val(obj.STARTTESTCASE);
				}
				
				// 终止测试用例
				if(obj.ENDTESTCASE == 0)
				{
					$('#ENDTESTCASES').attr('disabled', true);
					$("input[name='ENDTESTCASE'][tvn='0']").attr('checked', true);
				}
				else
				{
					$("input[name='ENDTESTCASE'][tvn='1']").attr('checked', true);
					$('#ENDTESTCASES').attr('disabled', null);
					$('#ENDTESTCASES').val(obj.ENDTESTCASE);
				}
				
				// 问题溯源
				$("input[name='TRACEABILITY']").each(function(index, o)
				{
					if(obj.TRACEABILITY == $(o).val())
					{
						$(o).attr('checked', true);
					}
				});
				// 语法测试显示------------
				$('#HURRYUP').val(obj.HURRYUP);
				$('#TARGET').val(obj.TARGET);
				$('#POWER').val(obj.POWER);  // zgh
				$('#SMESSAGE').val(obj.SMESSAGE);  // zgh
				$('#EMESSAGEP').val(obj.EMESSAGEP);  // zgh
				$('#PORTSEND').val(obj.PORTSEND);  // zgh
				$('#STOTAL').val(obj.STOTAL);  // zgh
				
				$('#REMARK').val(obj.REMARK);
				if(TESTTYPEN == 3){
					$('#PRONAME').val(PRONAME);
					num=0;
					var tb = document.getElementById('tpro');
				    var rowNum=tb.rows.length;
				    for (i=0;i<rowNum;i++)
				    {
				        tb.deleteRow(i);
				        rowNum=rowNum-1;
				        i=i-1;
				    }
				   
					$.each(result.custom, function(index, obj)
					{
						addfieldfun(null,obj);
					});
				}
			}
		});
	}
	// 点击 测试套件节点 去后台查询是否已经存在 测试套件详细信息 end
	var num=0;
	$('#addnewfield').click(function(){
		addfieldfun(this,null);		
	});
	
	function addfieldfun(etn,obj){
		var s;
		var value="0";
		var len = 0;
		var name = "未命名";
		var sorts = num;
		if(etn == null){
			value = obj.FIELDVALUE;
			len = obj.FIELDLEN;
			name = obj.FIELDNAME;
			sorts = obj.SORTS;
		}
		
		s='<tr>';
		s=s+'<td class="search_word" style="width:10%;">名称:</td>';
		s=s+'<td class="search_input">';
		s=s+'<input name="ltcus['+num+'].FIELDNAME" id="ltcus['+num+'].FIELDNAME" style="width:90%;" maxlength="32" onblur="customFieldnamefun(this.id);" type="text" value="'+name+'"/>';
		s=s+'<input type="hidden" name="ltcus['+num+'].SORTS" id="ltcus['+num+'].SORTS" value="'+sorts+'"/>';
		s=s+'<td>';
		s=s+'<td class="search_word" style="width:10%;">有效值：</td>';
		s=s+'<td class="search_input">';
		s=s+'<input name="ltcus['+num+'].FIELDVALUE" id="ltcus['+num+'].FIELDVALUE" style="width:90%;" maxlength="300" onblur="customFieldlenfun(this.id);"  onKeyUp="this.value=this.value.replace(/[^0-9-,]/g,\'\');"  type="text" value="'+value+'"/>';
		s=s+'<td>';
		s=s+'<td class="search_word" style="width:10%;">长度(bit):</td>';
		s=s+'<td class="search_input">';
		s=s+'<input name="ltcus['+num+'].FIELDLEN" id="ltcus['+num+'].FIELDLEN" style="width:90%;" type="text" onblur="customFieldlenfun(this.id);" maxlength="2" onKeyUp="this.value=this.value.replace(/[^\\d+$]/g,\'\');" value="'+len+'"/>';
		s=s+'<td>';
		s=s+'</tr>';
		$('#tpro').append(s);
		num++;
	}
	
	
	function proaddfun(name){
		var s;
		var tname = "";
		if(name != null){
			tname = name;
		}
		s='<tr>';
	    s=s+'<td colspan="8" style="padding-left: 100px;" class="search_input" >';
	    s=s+'<input type="text" name="ltcus['+num+'].TYPENAME" id="ltcus['+num+'].TYPENAME" maxlength="10" value="'+tname+'"/>';
	    s=s+'<input type="hidden" name="pro'+num+'" id="pro'+num+'" value="ct'+num+'"/>';
	    s=s+'<input type="hidden" name="ltcus['+num+'].TYPE" id="ltcus['+num+'].TYPE" value="'+num+'"/>';
	    s=s+'</td>';
	    s=s+'</tr>';
	    s=s+'<tr id="ct'+num+'" name="ct'+num+'" >';
	    s=s+'<td id="'+num+'" colspan="8" class="search_btn_left" align="" style="padding-left: 100px;">';
	    s=s+'<input type="button" id="addnewpro'+num+'" name="addnewpro'+num+'"  value="添加新字段">';
	    s=s+'</td></tr>';
		$("#customt").append(s);
		$('#addnewpro'+num).click(function(){
			fieldaddfun(this,null,null);
		});
		num++;
	}
	
	function fieldaddfun(etn,obj,proid){
		var s;
		var id;
		var min = 0;
		var max = 0;
		var len = 0;
		var name = "未命名";
		var sorts = num;
		if(etn != null){
			id = $(etn).parent().attr("id");
		}else{
			id = proid;
			min = obj.FIELDMIN;
			max = obj.FIELDMAX;
			len = obj.FIELDLEN;
			name = obj.FIELDNAME;
			sorts = obj.SORTS;
		}
		s='<tr id="ct'+num+'" name="ct'+num+'" >';
		s=s+'<td class="search_word" style="width:5%;">名称:</td>';
		s=s+'<td class="search_input" >';
		s=s+'<input type="text" style="width:90%;" name="ltcus['+num+'].FIELDNAME" id="ltcus['+num+'].FIELDNAME" maxlength="10" value="'+name+'"/>';
		s=s+'<input type="hidden" name="ltcus['+num+'].SORTS" id="ltcus['+num+'].SORTS" value="'+sorts+'"/>';
		s=s+'</td>';
		s=s+'<td class="search_word" style="width:5%;">最小值：</td>';
		s=s+'<td class="search_input" >';
		s=s+'<input type="text" style="width:90%;" name="ltcus['+num+'].FIELDMIN" id="ltcus['+num+'].FIELDMIN" maxlength="10" value="'+min+'"/>';
		s=s+'</td>';
		s=s+'<td class="search_word" style="width:5%;">最大值：</td>';
		s=s+'<td class="search_input" >';
		s=s+'<input type="text" style="width:90%;" name="ltcus['+num+'].FIELDMAX" id="ltcus['+num+'].FIELDMAX" maxlength="10" value="'+max+'"/>';
		s=s+'</td>';
		s=s+'<td class="search_word" style="width:5%;">长度(bit):</td>';
		s=s+'<td class="search_input" >';
		s=s+'<input type="text" style="width:90%;" name="ltcus['+num+'].FIELDLEN" id="ltcus['+num+'].FIELDLEN" maxlength="10" value="'+len+'"/>';
		s=s+'</td>';	
		s=s+'<td style="display:none;"><input type="text" name="ltcus['+num+'].TYPE" id="ltcus['+num+'].TYPE" value="'+id+'"</td>';
		s=s+'</tr>';
		if(etn != null){
			var str =$('#pro'+id).val();
			$('#'+str).after(s);
			$('#pro'+id).val('ct'+num);
// $(etn).parent().parent().after(s);
		}else{
			$('#customt').append(s);
		}
		num++;
	};
	
	function clearcuston(){
		var tb = document.getElementById('customt');
	    var rowNum=tb.rows.length;
	    for (i=0;i<rowNum;i++)
	    {
	        tb.deleteRow(i);
	        rowNum=rowNum-1;
	        i=i-1;
	    }
	    var s='';
	    s = s+'<tr>';
		s = s+'<td colspan="8" class="search_btn_left" align=""><input type="button" id="addnewpro" name="addnewpro" value="添加新协议"></td>';
		s = s+'</tr>';	
		s = s+'<tr style="height: 0px;">';
		s = s+'<td class="search_word" style="width:10%;"></td>';
		s = s+'<td class="search_input"></td>';
		s = s+'<td class="search_word" style="width:10%;"></td>';
		s = s+'<td class="search_input"></td>';
		s = s+'<td class="search_word" style="width:10%;"></td>';
		s = s+'<td class="search_input"></td>';
		s = s+'<td class="search_word" style="width:10%;"></td>';
		s = s+'<td class="search_input"></td>';
		s = s+'</tr>';
		$('#customt').append(s);
		$('#addnewpro').click(function(){
			proaddfun(null);		
		});
	};

});

function customFieldlenfun(id){
	var x=document.getElementById(""+id).value;
	if(x=="" || x==null){
		document.getElementById(""+id).value = "0";
	}
}

function customFieldnamefun(id){
	var x=document.getElementById(""+id).value;
	if(x.trim()=="" || x==null){
		document.getElementById(""+id).value = "未命名";
	}
}