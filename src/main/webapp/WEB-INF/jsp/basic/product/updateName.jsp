<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/product/updateName.js?v=${v}"></script>
<title>变更产品名称</title>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					产品编号：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7 " style="background: #F1F1F1" readonly="readonly" name="code" value="${code}">
					<input type="hidden" value="${id }" id="productId" name="id">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					原产品名称：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7" style="background: #F1F1F1" readonly="readonly" id="oldProductName" value="${name}" >
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					新产品名称：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7" id="productName" name="name">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"></label>
				<div class="form-label label_ui">
					<input class="nav_btn table_nav_btn" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
				</div>
			</div>
		</form>
	</div>
</body>
</html>
