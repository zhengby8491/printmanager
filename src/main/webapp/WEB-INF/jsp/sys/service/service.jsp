<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>服务支持</title>
</head>

<body>
	<div class="service_cont">
		<ul id="myTab" class="nav nav-tabs">
			<li>
				<a href="${ctx}/sys/service/system_notice">系统消息 </a>
			</li>
			<li>
				<a href="${ctx}/sys/service/myQuestion">我的提问</a>
			</li>
			<li>
				<a href="${ctx}/sys/service/question">在线提问</a>
			</li>
			<li class="active">
				<a href="${ctx}/sys/service/service">服务通道</a>
			</li>
		</ul>
		<div id="myTabContent" class="tab-content">敬请期待</div>
	</div>
</body>
</html>
