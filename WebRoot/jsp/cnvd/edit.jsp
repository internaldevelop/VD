<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/commons/jsp/title.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link
	href="<%=request.getContextPath()%>/controller/My97DatePicker/skin/WdatePicker.css"
	rel="stylesheet" type="text/css" />
<script language="JavaScript"
	src="<%=request.getContextPath()%>/controller/My97DatePicker/WdatePicker.js"></script>

<style>
<!--
form {
	margin-top: 10px;
}

form .input_title {
	width: 100px;
	text-align: right;
	display: inline-block;
	vertical-align: top;
}

form>div {
	margin: 5px;
	padding: 5px;
}

form>div input {
	width: 370px;
}

.need {
	color: red;
}
-->
</style>
</head>
<body class="main_body">
	<div class="index_body02">
		<!--top-->
		<div class="top_title02">
			<div class="top_title_img">
				<img src="<%=request.getContextPath() %>/images/right_icon01.png"
					width="14" height="16">
			</div>
			<div class="top_title_word">管理平台</div>
			<div class="top_title_word">&gt;</div>
			<div class="top_title_word">漏洞库</div>
			<div class="top_title_word">&gt;</div>
			<div class="top_title_word">漏洞编辑</div>
		</div>
		<!--table-->
		<div class="main_table">
			<div class="table_title">
				<div>漏洞编辑</div>
				<div class="right_input02"></div>
			</div>
			<div style="clear: both;"></div>
			<form id="form1" action="${basePath }/cnvd/save.do" method="post"
				target="_parent">
				<input type="hidden" name="id" value="${cnvd.id }" />
				<div>
					<label class="input_title"><span class="need">*</span>CNVD-ID：</label>
					<input type="text" name="cnvdId" value="${cnvd.cnvdId }" required
						datatype="*"
						ajaxurl="${basePath }/cnvd/validCnvdId.do?id=${cnvd.id }"
						nullmsg="请输入CNVD-ID" /> <span class="Validform_checktip"></span>
				</div>
				<div>
					<label class="input_title">漏洞名称：</label> <input type="text"
						name="cnvdName" value="${cnvd.cnvdName }" />
				</div>
				<div>
					<label class="input_title">CVE_ID：</label> <input type="text"
						name="cveId" value="${cnvd.cveId }" />
				</div>
				<div>
					<label class="input_title">危害级别：</label> <input type="text"
						name="hazardLevel" value="${cnvd.hazardLevel }" />
				</div>
				<div>
					<label class="input_title">BUGTRAQ-ID：</label> <input type="text"
						name="bugtraqId" value="${cnvd.bugtraqId }" />
				</div>
				<div>
					<label class="input_title">其他 ID：</label> <input type="text"
						name="otherId" value="${cnvd.otherId }" />
				</div>
				<div>
					<label class="input_title">影响产品：</label>
					<textarea name="affectGoods" rows="3" cols="50">${cnvd.affectGoods }</textarea>
				</div>
				<div>
					<label class="input_title">发现者：</label>
					<textarea name="finder" rows="2" cols="50"> ${cnvd.finder }</textarea>
				</div>
				<div>
					<label class="input_title">参考链接：</label> <input type="text"
						name="referLink" value="${cnvd.referLink }" />
				</div>

				<div>
					<label class="input_title">厂商补丁：</label> <input type="text"
						name="patch" value="${cnvd.patch }" />
				</div>
				<div>
					<label class="input_title">解决方案：</label>
					<textarea name="solution" rows="2" cols="50">${cnvd.solution }</textarea>
				</div>
				<div>
					<label class="input_title">验证信息：</label> <input type="text"
						name="verify" value="${cnvd.verify}" placeholder="(暂无验证信息)" />
				</div>
				<div>
					<label class="input_title">发布时间：</label> <input type="text"
						style="width: 120px;" name="releaseTime" placeholder=""
						value="${cnvd.releaseTime }" onClick="WdatePicker()"
						class="form-control" /> <label class="input_title">报送时间：</label> <input
						type="text" style="width: 120px;" name="reportTime" placeholder=""
						class="form-control" onClick="WdatePicker()"
						value="${cnvd.reportTime }" />
				</div>
				<div>
					<label class="input_title">收录时间：</label> <input type="text"
						style="width: 120px;" name="recordTime" placeholder=""
						class="form-control" onClick="WdatePicker()"
						value="${cnvd.recordTime }" /> <label class="input_title">更新时间：</label>
					<input type="text" style="width: 120px;" name="renewTime"
						placeholder="" class="form-control" onClick="WdatePicker()"
						value="${cnvd.renewTime }" />
				</div>
				<div>
					<label class="input_title">描述：</label>
					<textarea name="description" rows="5" cols="60">${cnvd.description }</textarea>
				</div>
				<div style="padding-left: 150px;">
					<button type="submit" id="submit">保存</button>
					<button type="button" onclick="window.parent.close()">返回</button>
				</div>
			</form>
		</div>
	</div>
</body>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/controller/validform/Validform_v5.3.2_min.js"></script>
<script type="text/javascript">
<!--
$(function(){
	$("#form1").Validform({
		btnSubmit:"#submit",
		btnReset:"#reset",
		tiptype:3
	});
});

//-->
</script>
</html>