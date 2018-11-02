<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>错误操作提示</title>
<chtml:link rel="stylesheet" href="/layout/css/window.css" />
</head>
<body>
	<chtml:window title="操作失败..." showCloseButton="true">
		<%
			String message = (String) request.getAttribute(com.huayin.common.constant.Constant.MESSAGE_KEY);
				if (message == null || message.length() <= 0)
				{
					out.println("系统出现异常，请联系管理员。");
				}
				else
				{
					out.println(message);
				}
		%>
	</chtml:window>
</body>
</html>