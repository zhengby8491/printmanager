<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>编辑销售模块</title>
<script type="text/javascript" src="${ctxStatic}/site/sys/buy/edit.js?${v}"></script>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<input type="hidden" id="id" name="id" value="${product.id}" />
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					购买版本名字：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" placeholder="" id="name" name="name" value="${product.name}" required>
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					销售价格：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" placeholder="" id="" name="price" value="${product.price}" required>
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					原价：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" placeholder="" id="" name="originalPrice" value="${product.originalPrice}" required>
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					奖金：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="${product.bonus}" placeholder="" id="" name="bonus" required>
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					类型：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<select name="type">
						<option value="1" ${product.type eq 1 ? "selected = 'selected' ":"" }>购买</option>
						<option value="2" ${product.type eq 2 ? "selected = 'selected' ":"" }>维护</option>
					</select>
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					版本类型：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<select name="versionType">
						<option value="1" ${product.versionType eq 1 ? "selected = 'selected' ":"" }>标准版</option>
						<option value="2" ${product.versionType eq 2 ? "selected = 'selected' ":"" }>报价</option>
						<option value="3" ${product.versionType eq 3 ? "selected = 'selected' ":"" }>标准版+微信端</option>
						<option value="4" ${product.versionType eq 4 ? "selected = 'selected' ":"" }>标准版+报价</option>
						<option value="5" ${product.versionType eq 5 ? "selected = 'selected' ":"" }>标准版+报价+微信端</option>
					</select>
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					排序：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="${product.sort}" placeholder="" id="" name="sort" required>
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
