<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>硬件检查</title>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<style type="text/css">
html, body {
	height: 100%;
	padding: 0px;
	margin: 0px;
}

img {
	width: 50PX;
	height: 50PX;
}

.center {
	margin: 35px auto;
	width: 520px;
}

.center>div {
	float: left;
	width: 100%;
}

ul {
	padding: 0;
	margin: 0;
	float: left;
}

li {
	width: 52px;
	list-style: none;
	display: inline-block;
	margin-left: 10px;
	float: left;
	border: 1px solid transparent;
}

li span {
	display: block;
	text-align: center;
}

hr {
	float: left;
	width: 100%;
	margin-top: 20px;
}

.do_y:hover {
	cursor: pointer;
}

.do_y:hover span {
	background-color: lightblue;
}
</style>

</head>
<body>
	<div class="center">
		<div id="diDiv">
			<div>
				离散监视器 <input type="button" id="hardware_check" value="硬件检查" />
			</div>
			<h2>DI 输入</h2>
			<ul>
				<li><img src="img/gray.jpg" /> <span>X0</span></li>
				<li><img src="img/gray.jpg"> <span>X1</span></li>
				<li><img src="img/gray.jpg"> <span>X2</span></li>
				<li><img src="img/gray.jpg"> <span>X3</span></li>
				<li><img src="img/gray.jpg"> <span>X4</span></li>
				<li><img src="img/gray.jpg"> <span>X5</span></li>
				<li><img src="img/gray.jpg"> <span>X6</span></li>
				<li><img src="img/gray.jpg"> <span>X7</span></li>
			</ul>
			<ul>
				<li><img src="img/gray.jpg"> <span>X8</span></li>
				<li><img src="img/gray.jpg"> <span>X9</span></li>
				<li><img src="img/gray.jpg"> <span>X10</span></li>
				<li><img src="img/gray.jpg"> <span>X11</span></li>
				<li><img src="img/gray.jpg"> <span>X12</span></li>
				<li><img src="img/gray.jpg"> <span>X13</span></li>
				<li><img src="img/gray.jpg"> <span>X14</span></li>
				<li><img src="img/gray.jpg"> <span>X15</span></li>
			</ul>
		</div>
		<hr />
		<div class="do">
			<h2>DO 输出</h2>
			<ul>
				<li id="do_y0" class="do_y" title="VD 220V"><img
					src="img/red.jpg" /> <span>Y0</span></li>
				<li id="do_y1" class="do_y"><img src="img/gray.jpg" /> <span>Y1</span>
				</li>
				<li id="do_y2" class="do_y"><img src="img/gray.jpg"> <span>Y2</span>
				</li>
			</ul>
		</div>
	</div>
</body>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/commons/js/jquery.js"></script>
<script type="text/javascript">
	$(function(){
		var intervalIndex = null;
		var flag = 0;
		$("#hardware_check").click(function(){
			if(this.value=="硬件检查"){
				$.get("<%=request.getContextPath() %>/selfcheck/startHardWareCheck.do",function(result){
					check();
				});
				this.value = "停止检查";
			}else{
				$.get("<%=request.getContextPath() %>/selfcheck/stopHardWareCheck.do",function(result){});
				if(intervalIndex!=null){
					clearInterval(intervalIndex);
					intervalIndex = null;
				}
				$("#diDiv li img").attr("src","img/gray.jpg");
				this.value = "硬件检查";
			}
		});
		
		function check(){
			intervalIndex = setInterval(function(){
				$.get("<%=request.getContextPath() %>/selfcheck/getHardWareCheckData.do",function(result){
					var diLeds = $("#diDiv li img").attr("src","img/gray.jpg");
					if(result!=null && result.leds != null){
						var leds = result.leds;
						for(var i=0;i<leds.length;i++){
							var led = leds[i];
							if(led == 1){
								diLeds.eq(i).attr("src","img/red.jpg");
							}
						}
					}
				},"json");
			},500);
		}
		
		$(".do_y").click(function(){
			if(intervalIndex!=null){
				var ds = {y0:0,y1:0,y2:0};
				var name = this.id;
				var selfImg = $(this).find("img");
				if($("#do_y0 img").attr("src").indexOf("img/red.jpg")!=-1){
					ds.y0 = 1;
				};
				if($("#do_y1 img").attr("src").indexOf("img/red.jpg")!=-1){
					ds.y1 = 1;
				}
				if($("#do_y2 img").attr("src").indexOf("img/red.jpg")!=-1){
					ds.y2 = 1;
				}
				switch (name) {
					case "do_y0":
						if(ds.y0 == 1){
							ds.y0 = 0;
						}else{
							ds.y0 = 1;
						}
						break;
					case "do_y1":
						if(ds.y1 == 1){
							ds.y1 = 0;
						}else{
							ds.y1 = 1;
						}
						break;
					case "do_y2":
						if(ds.y2 == 1){
							ds.y2 = 0;
						}else{
							ds.y2 = 1;
						}
						break;
				}
				if(selfImg.attr("src").indexOf("img/red.jpg")!=-1){
					selfImg.attr("src","img/gray.jpg");
				}else{
					selfImg.attr("src","img/red.jpg");
				}
				$.get("<%=request.getContextPath() %>/selfcheck/checkDoOutStatus.do",ds);
			}
		});
	});
</script>
</html>