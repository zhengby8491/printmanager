<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui_ueditor.jspf"%>
<title>编辑系统公告</title>
<script type="text/javascript" src="${ctxStatic}/site/sys/systemversionnotice/edit.js?vs='${v}'"></script>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value="${systemNotice.id}" />
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					版本号：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" placeholder="" id="title" name="title" value="${systemNotice.title}">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">发布时间：</label>
				<div class="row-div">
					<span>
						<input type="text" readonly="true" onfocus="WdatePicker({lang:'zh-cn'})" class="input-txt input-txt_11 Wdate" id="noticeTime" name="noticeTime" value="<fmt:formatDate value="${systemNotice.noticeTime}" pattern="yyyy-MM-dd"/>" />
					</span>
				</div>
				<label class="form-label label_ui label_1 label_4mar">是否发布：</label>
				<div class="row-div">
					<span class="ui-combo-wrap">
						<phtml:list cssClass="input-txt input-txt_11 hy_select2" textProperty="text" type="com.huayin.printmanager.persist.enumerate.BoolValue" name="publish" selected="${systemNotice.publish}"></phtml:list>
					</span>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">内容：</label>
				<div class="form_textarea_rlt">
					<div id="ueDiv" type="text/plain" style="width: 367px;"></div>
					<textarea name="content" id="content" style="display: none">${systemNotice.content}</textarea>
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
