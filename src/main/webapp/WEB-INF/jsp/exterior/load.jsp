<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>跳转到印刷家链接</title>
<script type="text/javascript">
$(function(){
	Helper.request({
		url : Helper.basePath + "/exterior/forward",
		error : function(request)
		{
			Helper.message.warn("服务器繁忙");
		},
		success : function(data)
		{
			if (data.success)
			{
				self.location = data.message;
			}
		  else
			{
				Helper.message.warn("跳转失败！" + data.message);
			}
		}
	});
}) 
</script>
</head>
<body>

</body>
</html>