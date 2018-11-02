<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/material/edit.js?fv=${v}"></script>
<title>编辑材料信息 - 材料信息管理</title>
</head>
<body>
	<div class="layer_container">
		<!--表单部分START-->
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value="${material.id}">
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							材料分类：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList('MATERIALCLASS')}" valueProperty="id" selected="${material.materialClassId}" cssClass="hy_select2" name="materialClassId" textProperty="name"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							材料类别：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="materialType" textProperty="text" selected="${material.materialType}" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.MaterialType"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							材料编号：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" readonly="true" value="${material.code}" placeholder="" id="code" name="code">
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							材料名称：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${material.name}" placeholder="" id="name" name="name">
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							计价单位：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList('UNIT')}" valueProperty="id" selected="${material.valuationUnitId}" cssClass="hy_select2" name="valuationUnitId" textProperty="name"></phtml:list>
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							库存单位：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list items="${fns:basicList('UNIT')}" valueProperty="id" selected="${material.stockUnitId}" cssClass="hy_select2" name="stockUnitId" textProperty="name"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							克重：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7 constraint_negative" value="${material.weight}" placeholder="" id="weight" name="weight" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							最近采购价：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${material.lastPurchPrice}" id="lastPurchPrice" name="lastPurchPrice" />
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red"></span>
							最低库存：
						</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_7" value="${material.minStockNum}" id="minStockNum" name="minStockNum" />
						</div>
					</div>
					<div class="row-div">
						<label class="form-label label_ui label_4mar">
							<span class="c-red">*</span>
							是否有效：
						</label>
						<div class="ui-combo-wrap form_text div_select_wrap">
							<phtml:list name="isValid" textProperty="text" selected="${material.isValid}" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.BoolValue"></phtml:list>
						</div>
					</div>
				</div>
				<div class="cl">
					<div class="row-div">
						<input class="nav_btn table_nav_btn" type="submit" style="margin-left: 140px; margin-top: 5px;" value="&nbsp;&nbsp;提交&nbsp;&nbsp;" />
					</div>
				</div>
			</div>
		</form>
		<!--表单部分END-->
	</div>
</body>
</html>
