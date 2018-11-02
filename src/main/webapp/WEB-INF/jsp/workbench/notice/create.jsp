<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>发布新公告</title>
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<script type="text/javascript">	
		$(function(){
			$("#btn__save").click(function(){
			    save();
			})
		})
		//保存
		function save(){
			  Helper.request({ 
				    url: Helper.basePath+"/workbench/notice/save",  
				    data: $("#form_order").formToJson(),
	                error: function(request) {
						
	                    Helper.message.warn("服务器繁忙");
	                },
	                success: function(data) {
					   if(data.success){
					       parent.save_success();
					       Helper.popup.close();
					   }else{
					  	 Helper.message.warn("服务器繁忙");
					   }
	                   
	                }
	            });

		}
	</script>
</head>
<body>
	<div class="layui-layer-prompt">
		<div id="" class="layui-layer-content">
			<form id="form_order" method="post">
				<textarea name="content" class="layui-layer-input" style="resize: none;"></textarea>
			</form>
			<div class="row-div r">
				<button id="btn__save" class="nav_btn table_nav_btn">提交</button>
			</div>
		</div>
	</div>
</body>
</html>