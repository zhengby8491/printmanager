<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>材料出入汇总</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/logSumList.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="单据日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_1" id="materialName" name="materialName" />
								<div class="select-btn" id="material_quick_select">...</div>
							</span>
						</dd>
						<div class="row-dd">
							<label class="form-label label_ui label_1">材料分类：</label>
							<span class="ui-combo-noborder">
								<phtml:list items="${fns:basicList('MATERIALCLASS')}" valueProperty="id" textProperty="name" name="materialClassId" cssClass="input-txt input-txt_3 hy_select2" defaultValue="" defaultOption="请选择" selected="${materialClassId }" />
							</span>
						</div>
						<dd class="row-dd">
							<label class="form-label label_ui">仓 库：</label>
							<span class="ui-combo-wrap">
								<phtml:list items="${fns:basicListParam('WAREHOUSE', 'warehouseType','MATERIAL')}" valueProperty="id" textProperty="name" defaultValue="" defaultOption="请选择" name="warehouseId" cssClass="input-txt input-txt_1 hy_select2"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料规格：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_1" id="specifications" name="specifications" />
							</span>
						</dd>
						<dd class="row-dd">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
			</div>

			<!--表格部分Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格部分End-->

		</div>
	</div>
</body>
</html>