<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>创建轮播广告</title>
</head>
<script type="text/javascript" src="${ctxStatic}/site/sys/advertisement/create.js?${v}"></script>
<body>
	<div class="layer_container">
		<form id="jsonForm">

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					排序：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7 constraint_number" value="5" placeholder="" id="sort" name="sort" />
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					图片：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<span class="">
						<input type="file" class="input-txt_11" value="" placeholder="" id="pic" name="pic">
					</span>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">标题：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="" placeholder="" id="title" name="title">
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">链接：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-url" value="" placeholder="" id="linkedUrl" name="linkedUrl">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">是否发布：</label>
				<div class="row-div">
					<span>
						<phtml:list cssClass="input-txt input-txt_11 hy_select2" textProperty="text" type="com.huayin.printmanager.persist.enumerate.BoolValue" name="publish"></phtml:list>
					</span>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">广告位置：</label>
				<div class="row-div">
					<span>
						<phtml:list cssClass="input-txt input-txt_11 hy_select2" textProperty="text" type="com.huayin.printmanager.persist.enumerate.AdvertisementType" name="advertisementType"></phtml:list>
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