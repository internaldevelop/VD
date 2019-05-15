<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript" src="<%=request.getContextPath()%>/commons/js/jquery.js" ></script>
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/commons/css/canvas.css" ></link>
<link  rel="stylesheet"  href="<%=request.getContextPath()%>/commons/css/newcss.css" ></link>
<script type="text/javascript" src="<%=request.getContextPath()%>/controller/artDialog/sea.js" ></script>
<script type="text/javascript">   
   var basePath = "<%=request.getContextPath()%>/";
   $(function(){
	   function autoResize(){
		   $("body").width($(window).width()-2); 
	   }
	   autoResize();
	   $(window).resize(autoResize);
   });
</script>
<%
	request.setAttribute("basePath", request.getContextPath());	
%>

