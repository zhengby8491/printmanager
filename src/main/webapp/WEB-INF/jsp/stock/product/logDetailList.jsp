<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>成品出入明细</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/product/logDetailList.js?v=${v}"></script>
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
						<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
							<div id="innerdiv" style="position: absolute;">
								<img id="bigimg" style="border: 5px solid #fff;" src="" />
							</div>
						</div>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">成品名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_1" id="productName" name="productName" />
								<div class="select-btn" id="product_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">仓 库：</label>
							<span class="ui-combo-wrap">
								<phtml:list items="${fns:basicListParam('WAREHOUSE', 'warehouseType','PRODUCT')}" valueProperty="id" textProperty="name" defaultValue="" defaultOption="请选择" name="warehouseId" cssClass="input-txt input-txt_1 hy_select2"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">单据类型：</label>
							<span class="ui-combo-noborder">
								<phtml:list items="${billTypeList}" textProperty="text" name="billType" cssClass="hy_select2 input-txt input-txt_1" defaultValue="" defaultOption="请选择" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">客户料号：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_1" id="customerMaterialCode" name="customerMaterialCode" />
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