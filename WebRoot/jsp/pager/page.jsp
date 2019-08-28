<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page
	import="org.springframework.web.servlet.i18n.SessionLocaleResolver"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div style="float: left; margin-left: 15px;">

	总数：${wntPage.total_count}&nbsp;;&nbsp;&nbsp;
	页码：${wntPage.current_page}&nbsp;/&nbsp;${wntPage.total_page}</div>


<div style="float: right; text-align: left; margin-right: -25%;"
	class="white_page">
	<c:if test="${wntPage.is_first == false }">
		<a href="javascript:void(0);"
			onclick="goPage(${wntPage.first_page});return false;">首页</a>
		&nbsp;
		<a href="javascript:void(0);"
			onclick="goPage(${wntPage.pre_page});return false;">上一页</a>
	</c:if>
	<c:if test="${wntPage.is_first}">
		<a href="javascript:void(0);"><font color="#999999">首页</font></a> 
		&nbsp;
		<a href="javascript:void(0);"><font color="#999999">上一页</font></a>
	</c:if>
	&nbsp;
	<c:if test="${wntPage.is_last == false}">
		<a href="javascript:void(0);"
			onclick="goPage(${wntPage.next_page});return false;">下一页</a> 
		&nbsp;
		<a href="javascript:void(0);"
			onclick="goPage(${wntPage.last_page});return false;">末页</a>
	</c:if>
	<c:if test="${wntPage.is_last}">
		<a href="javascript:void(0);"><font color="#999999">下一页</font></a> 
		&nbsp;
		<a href="javascript:void(0);"><font color="#999999">末页</font></a>
	</c:if>
</div>
<script>
    //分页 form id 为page_form
	function goPage(current_page){  
	    document.getElementById("current_page").value = current_page;
	    document.getElementById("page_form").submit();
// query();
	}

</script>
<input type="hidden" name="page_size" id="page_size"
	value="${wntPage.page_size }">
<input type="hidden" name="current_page" id="current_page"
	value="${wntPage.current_page}">
<input type="hidden" name="total_page" id="total_page"
	value="${wntPage.total_page}">
<input type="hidden" name="pre_page" id="pre_page"
	value="${wntPage.pre_page}">
<input type="hidden" name="next_page" id="next_page"
	value="${wntPage.next_page}">