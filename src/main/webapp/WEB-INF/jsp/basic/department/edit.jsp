<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/department/edit.js?v=${v }"></script>
<title><fmt:message key="i18n.jsp.common.table.department.edit"/> - <fmt:message key="i18n.systemlog.basic.department.label"/></title>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value="${department.id}">
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					<fmt:message key="i18n.jsp.common.sort"/>：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7 constraint_number" value="${department.sort}" placeholder="" id="sort" name="sort">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					<fmt:message key="i18n.jsp.common.table.department.name"/>：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7" value="${department.name}" placeholder="" id="name" name="name">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"><fmt:message key="i18n.jsp.common.table.remark"/>：</label>
				<div class="form_textarea_rlt">
					<textarea name="memo" id="memo" class="input-txt_7 " placeholder="<fmt:message key="i18n.jsp.common.remark.placeholder"/>" dragonfly="true" onKeyUp="Helper.doms.textarealength(this,100)" style="height: 98px; resize: none">${department.memo}</textarea>
					<p class="textarea-numberbar">
						<em>0</em>
						/100
					</p>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"></label>
				<div class="form-label label_ui">
					<input class="nav_btn table_nav_btn" type="submit" value="&nbsp;&nbsp;<fmt:message key="i18n.jsp.common.table.button.submit"/>&nbsp;&nbsp;">
				</div>
			</div>
		</form>
	</div>
</body>
</html>