
function getElementLeft(element){
var actualLeft = element.offsetLeft;
var current = element.offsetParent;

while (current !== null){
actualLeft += current.offsetLeft;
current = current.offsetParent;
}

return actualLeft;
}

function getElementTop(element){
var actualTop = element.offsetTop;
var current = element.offsetParent;

while (current !== null){
actualTop += current.offsetTop;
current = current.offsetParent;
}

return actualTop;
}

	function trimStr(str){
		return str.replace(/(^\s*)|(\s*$)/g,"");
	}

	function isIP(ip){
		var re =  /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])$/;
	    return re.test(ip);   
	}
	function isMAC(ip){
		
	    var re=/[a-fA-F\d]{2}:[a-fA-F\d]{2}:[a-fA-F\d]{2}:[a-fA-F\d]{2}:[a-fA-F\d]{2}:[a-fA-F\d]{2}/;
	    
		return re.test(ip);   
	}
	
	// 是否为float单精度 ^(-?//d+)(//.//d+)?$
    function isFLOAT(flo){
    	// "^([+-]?)\\d*\\.\\d+$"
	    var re=/^([+-]?)\\d*\\.\\d+$/;
	    
		return re.test(flo);   
	}
  // 是否为double双精度
    function isDOUBLE(dou){
    	
	    var re=/^(:?(:?\d+.\d+)|(:?\d+))$/;
	    
		return re.test(dou);   
	}
	function isQueryIP(ip){
		var re =  /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])$/;
		var re1 =  /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\*)$/;
		var re2 =  /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\*)\.(\*)$/;
		var re3 =  /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-4])\.(\*)\.(\*)\.(\*)$/;
	    return re.test(ip) || re1.test(ip) || re2.test(ip) || re3.test(ip);    
	} 
	
	function selAll(obj)
	{
		
		var celements = document.getElementsByName('id');
		for(i=0;i<celements.length;i++)
		{
			celements[i].checked = obj.checked;
		}
	}
	// 清空查询条件
	function clearAll(){
		// j("#condition_table");
		// alert(j("#condition_table").find("input[type=text]").size());
		$("#condition_table").find("input[type=text]").each(function(){
		   $(this).val("");
		});
		$("#condition_table").find("select").each(function(){
			$(this).find("option").first().attr("selected","selected");
		});
		// alert(j("#condition_table").find("select").size());
	}
	function noselAll()
	{
		var celements = document.getElementsByName('id');
		for(i=0;i<celements.length;i++)
		{
			if(celements[i].checked = true) 
			{
				celements[i].checked = false;
			}
		}
	}
	
