<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<script type='text/javascript' src='${ctxStatic}/wx/js/font/iconfont.js' charset='utf-8'></script>
<title>正在跳转</title>
<script type="text/javascript">
$(function(){
    window.location.href=$("#url").val();
})
</script>
</head>

<body>
	<input id="url" type="hidden" value="${WXTurnUrl}" />
<body>
</html>
