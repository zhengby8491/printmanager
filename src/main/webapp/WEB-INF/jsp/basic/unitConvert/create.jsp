<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/unitConvert/create.js?v=${v }"></script>
<title>添加单位换算 - 单位换算管理</title>
<style type="text/css">
.btn_bar .nav_btn {
	margin: 0 8px;
	padding: 4px 10px
}
</style>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<div class="cl row-div" style="margin: 10px 20px 0">
				<textarea id="formula_text" name="formula_text" placeholder="输入换算公式..." style="height: 50px; width: 100%; resize: none"></textarea>
				<input type="hidden" id="formula" name="formula" />
			</div>
			<div class="cl row-div btn_bar" style="text-align: center">
				<input type="button" class="nav_btn table_nav_btn" value="原单位数量" />
				<input type="button" class="nav_btn table_nav_btn" value="材料长" />
				<input type="button" class="nav_btn table_nav_btn" value="材料宽" />
				<input type="button" class="nav_btn table_nav_btn" value="材料高 " />
				<input type="button" class="nav_btn table_nav_btn" value="克重" />
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">单位换算名称：</label>
				<div class="ui-combo-wrap form_text">
					<input type="text" class="input-txt input-txt_7" placeholder="" id="name" name="name" />
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">源单位：</label>
				<div class="ui-combo-wrap form_text">
					<phtml:list items="${fns:basicList('UNIT')}" valueProperty="id" textProperty="name" name="sourceUnitId" cssClass="input-txt input-txt_7 hy_select2" />
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">换算单位：</label>
				<div class="ui-combo-wrap form_text">
					<phtml:list items="${fns:basicList('UNIT')}" valueProperty="id" textProperty="name" name="conversionUnitId" cssClass="input-txt input-txt_7 hy_select2" />
				</div>
			</div>
			<input id="save" type="button" style="margin-left: 270px; padding: 4px 12px" class="nav_btn table_nav_btn" value="保存" />
		</form>
	</div>
</body>
</html>