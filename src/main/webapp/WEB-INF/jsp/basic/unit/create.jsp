<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/unit/create.js?v=${v }"></script>
<title>添加单位信息 - 单位信息管理</title>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					排序：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7 constraint_number" value="${fns:nextSort('unit')}" placeholder="" id="sort" name="sort" />
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					单位名称：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7" value="" placeholder="" id="name" name="name" />
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					小数位：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7 constraint_decimal_negative" value="" placeholder="" id="accuracy" name="accuracy" />
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">备注：</label>
				<div class="form_textarea_rlt">
					<textarea name="memo" id="memo" class="input-txt_7 " placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="Helper.doms.textarealength(this,100)" style="height: 98px; resize: none"></textarea>
					<p class="textarea-numberbar">
						<em>0</em>
						/100
					</p>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"></label>
				<div class="form-label label_ui">
					<input class="nav_btn table_nav_btn" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;" />
				</div>
			</div>
		</form>
	</div>
</body>
</html>