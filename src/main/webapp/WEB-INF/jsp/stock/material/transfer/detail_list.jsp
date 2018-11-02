﻿<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>材料库存调拨明细</title>
<script type="text/javascript" src="${ctxStatic}/site/stock/material/transfer/detail_list.js?v=${v}"></script>
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
							<sys:dateConfine label="调拨日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料名称：</label> <span class="ui-combo-wrap wrap-width"> <input type="text" class="input-txt input-txt_1" id="materialName" name="materialName" />
								<div class="select-btn" id="material_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">调拨单号：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_3s" name="billNo" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">调 出 仓：</label> <span class="ui-combo-wrap form_text"> <phtml:list items="${fns:basicListParam2('WAREHOUSE', 'warehouseType','MATERIAL')}" valueProperty="id" textProperty="name" defaultValue="" defaultOption="请选择" name="outWarehouseId" cssClass="input-txt input-txt_1 hy_select2"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">调 入 仓：</label> <span class="ui-combo-wrap"> <phtml:list items="${fns:basicListParam2('WAREHOUSE', 'warehouseType','MATERIAL')}" valueProperty="id" textProperty="name" defaultValue="" defaultOption="请选择" name="inWarehouseId" cssClass="input-txt input-txt_1 hy_select2"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i> 查询
							</button>
							<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
						</dd>
					</dl>
					<dl class="cl hide_container" style="display: none;" id="more_div">
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料规格：</label> <span class="ui-combo-wrap"> <input type="text" class="input-txt input-txt_3s" style="width: 217px;" id="specifications" name="specifications" />
							</span>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label> <input type="radio" value="-1" name="auditFlag" checked="checked" /> 全部
						</label> <label> <input type="radio" value="YES" name="auditFlag" /> 已审核
						</label> <label> <input type="radio" value="NO" name="auditFlag" /> 未审核
						</label>
					</div>
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