<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<style type="text/css">
.purch_body {
	width: 210mm;
	height: auto;
	position: relative;
	margin: 0 auto;
	font-size: 14px;
	font-family: '微软雅黑'
}
</style>
<title>自定义报表-打印</title>
</head>
<body class="purch_body">
	<!-- 表格 -->
	<div id="preview"></div>
	<script>
        $(function()
        {
            Helper.request({
                url: Helper.basePath + '${params.url}',
                async:false,
                success: function(data)
                {
                    Helper.request({
                        url: Helper.basePath + '/template/get?id=${params.templateId}',
                        async:false,
                        success: function(template)
                        {
                            //console.log(template);
                            var tempData = decodeTemplateData(data,HTMLDecode(template.obj.context));
                            document.getElementById('preview').innerHTML = tempData;
                            console.log(tempData.indexOf('<img'));
                            if(tempData && tempData.indexOf('<img') > 0){
                                setTimeout(function(){window.print()}, 1500);
                            }
                            else {
                                window.print();
                            }
                        }
                    });
                }
            });
        });
    </script>
</body>

</html>