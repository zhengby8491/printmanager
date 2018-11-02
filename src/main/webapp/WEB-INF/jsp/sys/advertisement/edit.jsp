<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<title>编辑轮播广告</title>
</head>
<script type="text/javascript" src="${ctxStatic}/site/sys/advertisement/edit.js?${v}"></script>
<body>
	<div class="layer_container">
		<form id="jsonForm">

			<input type="hidden" id="id" name="id" value="${advertisement.id}" />
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">
					<span class="c-red">*</span>
					排序：
				</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_7 constraint_number" value="${advertisement.sort}" placeholder="" id="sort" name="sort" />
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">图片：</label>
				<div class="row-div">
					<span>
						<input type="file" class="input-txt_11" value="" placeholder="" id="pic" name="pic">
					</span>

				</div>
				<label class="form-label label_ui label_4mar">
					<a href="${advertisement.photoUrl}" target="_blank">原图：</a>
				</label>
				<a href="${advertisement.photoUrl}" target="_blank">
					<img style="width: 100px; height: 50px" src="${advertisement.photoUrl}" />
				</a>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">标题：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11" value="${advertisement.title}" placeholder="" id="title" name="title">
				</div>
			</div>

			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">链接：</label>
				<div class="ui-combo-wrap form_text error_tip">
					<input type="text" class="input-txt input-txt_11 input-url" value="${advertisement.linkedUrl}" placeholder="" id="linkedUrl" name="linkedUrl">
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">是否发布：</label>
				<div class="row-div">
					<span>
						<phtml:list cssClass="input-txt input-txt_11 hy_select2" textProperty="text" type="com.huayin.printmanager.persist.enumerate.BoolValue" name="publish" selected="${advertisement.publish}"></phtml:list>
					</span>
				</div>
			</div>
			<div class="cl row-div">
				<label class="form-label label_ui label_4mar">广告位置：</label>
				<div class="row-div">
					<span>
						<phtml:list cssClass="input-txt input-txt_11 hy_select2" textProperty="text" type="com.huayin.printmanager.persist.enumerate.AdvertisementType" name="advertisementType" selected="${advertisement.advertisementType}"></phtml:list>
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
