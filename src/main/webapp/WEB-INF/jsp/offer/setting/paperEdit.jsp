<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/offer/setting/paperEdit.js?v=${v}"></script>
<print:body>
	<print:form>
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value="${paperMaterial.id }"> <input type="hidden" name="offerType" id="offerType" value="${paperMaterial.offerType }">
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"> 材料分类： </label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7" value="${paperMaterial.name }" id="name" name="name">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"> 克重（g）： </label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7 constraint_decimal_negative" value="${paperMaterial.weight }" id="weight" name="weight">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"> 吨价（元）： </label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7 constraint_decimal_negative" value="${paperMaterial.tonPrice } " id="tonPrice" name="tonPrice">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"> 是否自翻版： </label> <span class="ui-combo-wrap"> <phtml:list cssClass="input-txt input-txt_25 hy_select2" name="isPageTurn" textProperty="text" defaultValue="" type="com.huayin.printmanager.persist.enumerate.BoolValue" defaultOption="" selected="${paperMaterial.isPageTurn }"></phtml:list>
				</span>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar"></label>
				<div class="form-label label_ui">
					<input class="nav_btn table_nav_btn" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
				</div>
			</div>
		</form>
	</print:form>
</print:body>
</html>