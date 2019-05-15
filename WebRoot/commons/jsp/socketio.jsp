<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/controller/socket.io/socket.io.js"></script>
<script type="text/javascript">
	var host = '<%=request.getServerName()%>';
	var socketchart = io.connect(host + ':9092/chat');
	var socketSysInfo =io.connect(host +':9092/sysinfo');
	var socketStartScan =io.connect(host +':9092/startScan');
	socketchart.on('connect', function() {
		//output('<span class="connect-msg">Client has connected to the server!</span>');
		console.log("socketchart 建立连接");
	});

	socketchart.on('message', function(data) {
		soutput(data);
	});

	socketchart.on('disconnect', function() {
		//output('<span class="disconnect-msg">The client has disconnected!</span>');
		console.log("socketchart 连接关闭");
	});
	
	function sendDisconnect() {
		//alert('关闭');
		socketchart.disconnect();
	};
	
	function sendConnect(){
		socketchart.connect();
	};

	//sysinfo

	socketSysInfo.on('sysinfo', function(data) {
		alertSysInfo(data);
	});
	function sendConnect(){
		socketSysInfo.connect();
	};
	function sendDisconnect() {
		//alert('关闭');
		socketSysInfo.disconnect();
	};
	
	function alertSysInfo(data){
	};
	
	
</script>