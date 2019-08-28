<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>共 ${wntPage.total_page} 页/ ${wntPage.total_count} 条记录 当前第
	${wntPage.current_page} 页</div>
<div class="table_bottom_right">
	<c:if test="${wntPage.is_first == false }">
		<a href="javascript:return false;"
			onclick="goPage(${wntPage.first_page});return false;">首页</a>
		<a href="javascript:return false;"
			onclick="goPage(${wntPage.pre_page});return false;">上一页</a>
	</c:if>
	<c:if test="${wntPage.is_first}">
		<a href="javascript:return false;"><font color="#999999">首页</font></a>
		<a href="javascript:return false;"><font color="#999999">上一页</font></a>
	</c:if>
	<c:if test="${wntPage.is_last == false}">
		<a href="javascript:return false;"
			onclick="goPage(${wntPage.next_page});return false;">下一页</a>
		<a href="javascript:return false;"
			onclick="goPage(${wntPage.last_page});return false;">末页</a>
	</c:if>
	<c:if test="${wntPage.is_last}">
		<a href="javascript:return false;"><font color="#999999">下一页</font></a>
		<a href="javascript:return false;"><font color="#999999">末页</font></a>
	</c:if>
	<!--<c:if test="${wntPage.total_page!=0}">
	跳转至
	<input name="goto"  id="goto"  type="text"  value="" style="width:30px" >
	<a href="" onclick="goPageto();return false;">GO</a> 
</c:if>-->

	<!--<c:if test="${wntPage.total_page==0}">
	<font color="#999999">跳转至</font>
	<input  type="text" style="width:30px" value="" disabled>
	<a href="" ><font color="#999999">GO</font></a> 
</c:if> -->
</div>
<script>
//分页 form id 为page_form
function goPage(current_page){	
   // document.getElementById("tab").value ="1";//标识是分页还是查询
	document.getElementById("current_page").value = current_page;
	document.getElementById("page_form").submit();
}
//分页 form id 为page_form
function goPageto(){	
       var total=document.getElementById("total_page").value;
	   var gotopage=document.getElementById("goto").value;
	//	document.getElementById("tab").value ="1";//标识是分页还是查询
		var tt=/^\d+$/g;
		if (tt.test(gotopage)){
			if(parseInt(gotopage)>=1 && parseInt(gotopage)<=parseInt(total)){
			document.getElementById("current_page").value = document.getElementById("goto").value;
	        document.getElementById("page_form").submit();
			}else{
				atrshowmodal("请输入合法的页码！");
			}
		}else{
			return atrshowmodal("请输入合法的页码！");
		}	
}
</script>
<input type="hidden" name="page_size" id="page_size"
	value="${wntPage.page_size }" form="page_form">
<input type="hidden" name="current_page" id="current_page" value="0"
	form="page_form">
<input type="hidden" name="total_page" id="total_page"
	value="${wntPage.total_page}" form="page_form">