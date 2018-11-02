<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/wx/public.jsp"%>
<script type='text/javascript' src='${ctxStatic}/wx/js/font/iconfont_offer.js' charset='utf-8'></script>
<title>授权错误</title>
</head>

<body>
	<div class="page page-current sum_content index_content">
		<p3>微信授权错误，请重新进入
		</p>
		<jsp:include page="/WEB-INF/jsp/wx/foot.jsp">
			<jsp:param value="invalid" name="module" />
		</jsp:include>
	</div>
</body>
</html>
