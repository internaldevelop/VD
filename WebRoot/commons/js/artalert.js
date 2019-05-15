//提示框  showModal，只有确定一个按钮
	function atrshowmodal(contents,okFun,closeFun){
		if(okFun == null){
			okFun = function(){}
		}
	    var d = dialog({
	        title: '提示',
	        width: 300,
		    height: 80,
	        content: contents,
	        okValue: '确 定',
	        ok: okFun,
	        onclose:closeFun,
	        cancelValue:'关闭'
	    });
	    d.showModal();
	    return d;
	}
	
	/**
	 * 使用atrDialog 替换 window.confirm 
	 * @param contents 	显示的内容 	字符串类型
	 * @param okFun	   	点击确认按钮 function
	 * @param cancelFun 点击取消按钮 function
	 * @author gyk
	 */
	function atrConfirm(contents,okFun,cancelFun){
		if(okFun == null){
			okFun = function(){}
		}
		if(cancelFun == null){
			cancelFun = function(){}
		}
		 var d = dialog({
		        title: '确认',
		        width: 300,
		        height: 80,
		        content: contents,
		        okValue: '确 定',
		        ok: okFun,
		        cancelValue: '取消',
		        cancel:cancelFun
		    });
		 d.showModal();
		return d;
	} 
