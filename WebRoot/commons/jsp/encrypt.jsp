<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
pageContext.setAttribute("basePath",request.getContextPath());
%>
<script type="text/javascript" src="${basePath }/commons/js/core.js"></script>
<script type="text/javascript"
	src="${basePath }/commons/js/enc-base64.js"></script>
<script type="text/javascript"
	src="${basePath }/commons/js/cipher-core.js"></script>
<script type="text/javascript"
	src="${basePath }/commons/js/lib-typedarrays.js"></script>
<script type="text/javascript" src="${basePath }/commons/js/aes.js"></script>
<script type="text/javascript" src="${basePath }/commons/js/mode-ecb.js"></script>
<script type="text/javascript">
<!--
function encrypt(word, key) {
	var key = CryptoJS.enc.Utf8.parse(key);
	var srcs = CryptoJS.enc.Utf8.parse(word);
	var encrypted = CryptoJS.AES.encrypt(srcs, key, {
		mode : CryptoJS.mode.ECB,
		padding : CryptoJS.pad.Pkcs7
	});
	return encrypted.toString();
}
//-->
</script>
