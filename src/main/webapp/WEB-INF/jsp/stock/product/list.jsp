<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<meta charset="UTF-8">
<title>成品库存查询</title>
</head>
<style type="text/css">
.wrap-width input[type='text']
{
	padding-right:25px;
}
</style>
<script type="text/javascript" src="${ctxStatic}/site/stock/product/list.js?v=${v}"></script>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
				<div id="innerdiv" style="position: absolute;">
					<img id="bigimg" style="border: 5px solid #fff;" src="" />
				</div>
			</div>

			<!--头部END-->
			<!--查询表单START-->
			<div>
				<div class="cl order-info">
					<div class="row-dd">
						<label class="form-label label_ui label_1">客户名称：</label>
						<span class="ui-combo-wrap wrap-width">
							<input id="customerName" type="text" class="input-txt input-txt_6" name="customerName" value="" />
							<div class="select-btn" id="customer_quick_select">...</div>
						</span>
					</div>
					<div class="row-dd">
						<label class="form-label label_ui label_1">产品编号：</label>
						<span class="ui-combo-wrap ">
							<input id="code" type="text" class="input-txt input-txt_6" name="" "/>
						</span>
					</div>
					<div class="row-dd">
						<label class="form-label label_ui label_1">成品名称：</label>
						<span class="ui-combo-wrap wrap-width">
							<input id="productName" type="text" class="input-txt input-txt_6" name="productName" value="${productName }" />
							<div class="select-btn" id="product_quick_select">...</div>
						</span>
					</div>
					<div class="row-dd">
						<label class="form-label label_ui label_1">产品规格：</label>
						<span class="ui-combo-wrap wrap-width">
							<input id="specifications" type="text" class="input-txt input-txt_8" name="" "/>
						</span>
					</div>
					<div class="row-dd">
						<span>
							<label>
								<input id="isEmptyWare" type="checkbox" name="isEmptyWare" />
								是否显示0库存
							</label>
						</span>
						<button class="nav_btn table_nav_btn search_btn" id="btn_search">
							<i class="fa fa-search"></i>
							查询
						</button>
						<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
					</div>
				</div>

				<div class="cl hide_container" style="display: none;" id="more_div">
					<div class="row-dd">
						<label class="form-label label_ui label_1">仓库名称：</label>
						<span class="ui-combo-noborder" class="form_text">
							<phtml:list items="${fns:basicListParam2('WAREHOUSE','warehouseType','PRODUCT')}" valueProperty="id" textProperty="name" name="warehouseId" cssClass="input-txt input-txt_9 hy_select2" defaultValue="" defaultOption="请选择" />

						</span>
					</div>
					<div class="row-dd">
						<label class="form-label label_ui label_1">产品分类：</label>
						<span class="ui-combo-noborder">
							<phtml:list items="${fns:basicList('PRODUCTCLASS')}" valueProperty="id" textProperty="name" name="productClassId" cssClass="input-txt input-txt_6 hy_select2" defaultValue="" defaultOption="请选择" />
						</span>
					</div>
				  <div class="row-dd">
						<label class="form-label label_ui label_1">客户料号：</label>
						<span class="ui-combo-noborder">
							<input id="customerMaterialCode" type="text" class="input-txt input-txt_9" name="" "/>
						</span>
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