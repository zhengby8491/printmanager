<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>代理商信息</title>
</head>
<script type="text/javascript" src="${ctxStatic}/site/sys/agentquotient/create.js?${v}"></script>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					LOGO：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<span class="">
						<input type="file" class="input-txt_11" value="" placeholder="" id="pic" name="pic">
					</span>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">名称：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="name" name="name">
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">联系人：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="linkName" name="linkName">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">联系电话：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="telNum" name="telNum">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">地址：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="address" name="address">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">区域：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="area" name="area">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">代理商属性：</label>
				<div class="row-div">
					<span>
						<phtml:list cssClass="input-txt input-txt_11 hy_select2" textProperty="text" type="com.huayin.printmanager.persist.enumerate.AgentType" name="agentType"></phtml:list>
					</span>
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