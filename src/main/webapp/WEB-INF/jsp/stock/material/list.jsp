<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>材料库存查询</title>
</head>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/list.js?v=${v}"></script>
<body>

	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--头部END-->
			<!--查询表单START-->
			<div>
				<div class="cl order-info">
					<div class="row-dd">
						<label class="form-label label_ui">材料编号：</label>
						<span class="ui-combo-wrap wrap-width">
							<input id="code" style="padding-right: 0" type="text" class="input-txt input-txt_9" name="" />
						</span>
					</div>
					<div class="row-dd">
						<label class="form-label label_ui">材料名称：</label>
						<span class="ui-combo-wrap wrap-width">
							<input id="materialName" style="padding-right: 0" type="text" class="input-txt input-txt_9" name="materialName" value="${materialName }" />
							<div class="select-btn" id="material_quick_select">...</div>
						</span>
					</div>
					<div class="row-dd">
						<label class="form-label label_ui label_1">材料规格：</label>
						<span class="ui-combo-noborder">
							<input id="specifications" type="text" class="input-txt input-txt_3" name="" />
						</span>
					</div>

					<div class="row-dd">
						<label class="form-label label_ui label_1">克重：</label>
						<span class="ui-combo-noborder">
							<input id="weight" type="text" class="input-txt input-txt_3" name="weight" />
						</span>
					</div>

					<div class="row-dd check_div">
						<span>
							<label>
								<input id="isEmptyWare" type="checkbox" name="isEmptyWare" />
								是否显示0库存
							</label>
						</span>
						<button id="btn_search" class="nav_btn table_nav_btn search_btn">
							<i class="fa fa-search"></i>
							查询
						</button>
						<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
					</div>

					<div class="cl row-dl" style="display: none;" id="more_div">
						<div class="row-dd">
							<label class="form-label label_ui label_1">仓库名称：</label>
							<span class="ui-combo-noborder" class="form_text"">
								<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','MATERIAL')}" valueProperty="id" textProperty="name" name="warehouseId" cssClass="input-txt input-txt_9 hy_select2" defaultValue="" defaultOption="请选择" selected="${warehouseId }" />
							</span>
						</div>
						<div class="row-dd">
							<label class="form-label label_ui label_1">材料类别：</label>
							<span class="ui-combo-noborder">
								<phtml:list name="materialType" selected="${materialType}" textProperty="text" defaultValue="-1" defaultOption="请选择" cssClass="input-txt input-txt_9 hy_select2" type="com.huayin.printmanager.persist.enumerate.MaterialType"></phtml:list>
							</span>
						</div>

						<div class="row-dd">
							<label class="form-label label_ui label_1">材料分类：</label>
							<span class="ui-combo-noborder">
								<phtml:list items="${fns:basicList('MATERIALCLASS')}" valueProperty="id" textProperty="name" name="materialClassId" cssClass="input-txt input-txt_3 hy_select2" defaultValue="" defaultOption="请选择" selected="${materialClassId }" />
							</span>
						</div>
					</div>
				</div>

			</div>
			<!--查询表单END-->
			<!--表格部分START-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
		</div>
	</div>
</body>

</html>