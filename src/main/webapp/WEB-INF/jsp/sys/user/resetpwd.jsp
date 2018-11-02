<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/user/resetPwd.js?v=${v}"></script>
<title>重置密码</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="form-user">
			<input type="hidden" name="id" value="${userId }" />
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							请输入新密码：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="" placeholder="输入新密码" id="password" name="password">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<input class="nav_btn table_nav_btn" type="submit" style="margin-left: 140px; margin-top: 5px;" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
					</div>
				</div>
			</div>
		</form>
		<!--表单部分END-->
	</div>
</body>
</html>