<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic}/site/basic/productClass/edit.js?v=${v }"></script>
<title>编辑产品类型型 - 产品类型管理</title>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value="${productClass.id}">
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					排序：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7 constraint_number" value="${productClass.sort}" id="sort" name="sort">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					产品类别：
				</label>
				<div class="ui-combo-wrap form_text div_select_wrap">
					<phtml:list name="productType" textProperty="text" selected="${productClass.productType }" cssClass="hy_select2" type="com.huayin.printmanager.persist.enumerate.ProductType"></phtml:list>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					产品分类名称：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7" value="${productClass.name}" id="name" name="name">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">备注：</label>
				<div class="form_textarea_rlt">
					<textarea name="memo" id="memo" class="input-txt_7 " placeholder="说点什么...100个字符以内" dragonfly="true" onKeyUp="Helper.doms.textarealength(this,100)" style="height: 98px; resize: none">${productClass.memo}</textarea>
					<p class="textarea-numberbar">
						<em>0</em>
						/100
					</p>
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