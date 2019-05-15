<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html> 
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include  page="/commons/jsp/title.jsp"/>
<jsp:include page="/commons/jsp/atrDialog.jsp" />
<script type="text/javascript" src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_ncr_min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/controller/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/commons/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/controller/ztree/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jsp/testdeplay/js/tree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/controller/ztree/jquery.ztree.excheck-3.5.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/controller/ztree/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/commons/js/socketfun.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/jsp/testdeplay/css/tree.css"  type="text/css">
<body class="main_body">
<div class="index_body02">
<!--top-->
<div class="top_title02">
        <div class="top_title_img"><img src="<%=request.getContextPath() %>/images/right_icon01.png" width="14" height="16"></div>
        <div class="top_title_word">工控协议模糊测试工具</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">测试</div>
        <div class="top_title_word">&gt;</div>
        <div class="top_title_word">测试设置</div>
        
</div>
<div class="table_title">
	<div>测试设置</div>
    <div class="right_input02">
    	
    </div>
</div>
<div style="width:100%; height:50%" > 
  	<div class="content_wrap" style="width:15%; float:left; ">
  		<div class="zTreeDemoBackground left"  style=" border:1px solid grey">
			<ul id="tree" class="ztree"></ul>
			<ul id="templatetree" class="ztree"></ul>
		</div>
    </div>

   
</div>
<div class="main_table"  style="width:80%;float:right;margin-left:200px">
	<div class="main_search">
 		<form name="page_form" id="page_form"  class="demoform" action="<%=request.getContextPath() %>/alertloglistselect.do" method="post" ">
 			<ul>
 			<li>
	        	<ul class="search_ul">
	            	<li class="search_word">我的模版名称：</li>
	                <li class="search_input">
	                	<input name="clientName" id="clientName" type="text">
					</li>
	            </ul>
        	</li>
        	</ul>
        	<div class="btn_2">
       			<input name="save" value="保存" onclick="save()"  type="button">
                <input name="delete" value="删除" onclick="del()" type="button">
				<input name="execute" value="执行" onclick="execute()"  type="button">
    		</div>
    		 <table id="table" name="table"  cellpadding="0" cellspacing="0" >
				<tr class="table_title02" >
		        	<td >测试用例</td>
	           </tr>
	        </table>
        </form>
    </div>
 	   

 </div>
</div>			
  	
</div>

</body>
</html>