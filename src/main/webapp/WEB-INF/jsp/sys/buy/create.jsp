<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>创建购买版本</title>
<script type="text/javascript" src="${ctxStatic}/site/sys/buy/create.js?${v}"></script>
</head>
<body>
	<div class="layer_container">
		<form id="jsonForm">
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					版本名称：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="" name="name" required>
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					销售价格：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="" name="price" required>
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					版本原价：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="" name="originalPrice" required>
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					奖金：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="" name="bonus" required>
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					服务类型：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<select name="type">
						<option value="1">购买</option>
						<option value="2">维护</option>
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
						<option value="1">标准版</option>
						<option value="2">报价</option>
						<option value="3">标准版+微信端</option>
						<option value="4">标准版+报价</option>
						<option value="5">标准版+报价+微信端</option>
					</select>
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					排序：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="1" placeholder="" id="" name="sort" required>
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