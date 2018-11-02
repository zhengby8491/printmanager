<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui_ueditor.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/sys/systemversionnotice/create.js?v='${v}'"></script>
<title>创建系统公告</title>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					版本号：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="title" name="title">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">发布时间：</label>
				<div class="row-div">
					<span>
						<input type="text" readonly="true" onfocus="WdatePicker({lang:'zh-cn'})" class="input-txt input-txt_11 Wdate" id="noticeTime" name="noticeTime" pattern="yyyy-MM-dd" />
					</span>
				</div>

				<label class="form-label label_ui label_4mar">是否发布：</label>
				<div class="row-div">
					<span>
						<phtml:list cssClass="input-txt input-txt_11 hy_select2" textProperty="text" type="com.huayin.printmanager.persist.enumerate.BoolValue" name="publish"></phtml:list>
					</span>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">内容：</label>
				<div class="form_textarea_rlt">
					<div id="ueDiv" name="ueDiv" style="width: 367px"></div>
					<input name="content" id="content" type="hidden" />
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