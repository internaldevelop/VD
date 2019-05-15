function pageloadResult(){
	var second = document.getElementById('totalSecond').textContent;
	if (navigator.appName.indexOf("Explorer") > -1){
		second = document.getElementById('totalSecond').innerText; 
	}else{
		second = document.getElementById('totalSecond').textContent; 
	}
	
	setInterval(function redirect(){
		if(second < 0){
			top.location.href = ''; 
		}else{
			if (navigator.appName.indexOf("Explorer") > -1)
			{
				document.getElementById('totalSecond').innerText = second--; 
			} else {
				document.getElementById('totalSecond').textContent = second--; 
			}
		}
	},1000); 
}
pageloadResult();