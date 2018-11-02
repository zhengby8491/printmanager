<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>编辑代理商</title>
</head>
<script type="text/javascript" src="${ctxStatic}/site/sys/agentquotient/edit.js?${v}"></script>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value="${agentQuotient.id}" />
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">图片：</label>
				<div class="row-div">
					<span>
						<input type="file" class="input-txt_11" value="" placeholder="" id="pic" name="pic">
					</span>

				</div>
				<label class="form-label label_ui label_4mar">
					<a href="${agentQuotient.photoUrl}" target="_blank">原图：</a>
				</label>
				<input type="text" class="input-txt input-txt_11" value="${agentQuotient.photoUrl}" name="photoUrl">
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">名称：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="${agentQuotient.name}" placeholder="" id="name" name="name">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">联系人：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="${agentQuotient.linkName}" placeholder="" id="linkName" name="linkName">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">联系电话：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="${agentQuotient.telNum}" placeholder="" id="telNum" name="telNum">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">地址：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="${agentQuotient.address}" placeholder="" id="address" name="address">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">区域：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="${agentQuotient.area}" placeholder="" id="area" name="area">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">代理商属性：</label>
				<div class="row-div">
					<span>
						<phtml:list cssClass="input-txt input-txt_11 hy_select2" textProperty="text" type="com.huayin.printmanager.persist.enumerate.AgentType" name="agentType" selected="${agentQuotient.agentType}"></phtml:list>
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
