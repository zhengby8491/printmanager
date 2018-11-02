﻿<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>
<script type="text/javascript" src="${ctxStatic }/site/produce/work/work_schedule.js?v=${v }"></script>
<title>生产工单进度情况表</title>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl order-info">
				<dl class="cl row-dl">
					<dd class="row-dd">
						<sys:dateConfine label="制单日期" />
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_1">生产单号：</label>
						<span class="ui-combo-wrap form_text">
							<input type="text" class="input-txt input-txt_3" name="billNo" id="billNo" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui">客户名称：</label>
						<span class="ui-combo-wrap wrap-width">
							<input type="text" class="input-txt input-txt_7" id="customerName" name="customerName" />
							<div class="select-btn" id="customer_quick_select">...</div>
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui">成品名称：</label>
						<span class="ui-combo-wrap wrap-width">
							<input type="text" class="input-txt input-txt_3" id="productName" name="productName" />
							<div class="select-btn" id="product_quick_select">...</div>
						</span>
					</dd>
					<dd class="row-dd">
						<button class="nav_btn table_nav_btn search_btn" id="btn_search">
							<i class="fa fa-search"></i>
							查询
						</button>
						<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
					</dd>
				</dl>
				<dl class="cl hide_container" style="display: none;" id="more_div">
					<dd class="row-dd">
						<label class="form-label label_ui label_1">交货日期：</label>
						<span class="ui-combo-wrap form_text">
							<input type="text" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'deliverDateMax\')}'})" class="input-txt input-txt_0 Wdate" id="deliverDateMin" name="deliverDateMin" value="<fmt:formatDate value="${deliverDateMin }" type="date" pattern="yyyy-MM-dd" />" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_2 " style="text-align: left; margin-left: -2px; width: 18px">至</label>
						<span class="ui-combo-wrap form_text">
							<input type="text" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'deliverDateMin\')}'})" class="input-txt input-txt_0 Wdate" id="deliverDateMax" name="deliverDateMax" value="<fmt:formatDate value="${deliverDateMax }" type="date" pattern="yyyy-MM-dd" />" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_1">销售单号：</label>
						<span class="ui-combo-wrap form_text">
							<input type="text" class="input-txt input-txt_3" name="saleBillNo" id="saleBillNo" />
						</span>
					</dd>
					<dd class="row-dd">
						<label class="form-label label_ui label_1">客户料号：</label>
						<span class="ui-combo-wrap form_text">
							<input type="text" class="input-txt input-txt_16" name="customerMaterialCode" id="customerMaterialCode" />
						</span>
					</dd>

					<dd class="row-dd">
						<label class="form-label label_ui">产品规格：</label>
						<span class="ui-combo-wrap wrap-width">
							<input type="text" class="input-txt input-txt_3" id="style" name="style" />
						</span>
					</dd>
				</dl>
				<dl class="cl hide_container" style="display: none;" id="more_div2">
					<dd class="row-dd">
						<label class="form-label label_ui">客户单号：</label>
						<span class="ui-combo-wrap ">
							<input type="text" class="input-txt input-txt_13" id="customerBillNo" name="customerBillNo" />
						</span>
					</dd>
				</dl>
			</div>
			<!--查询表单End-->
			<!--表格Start-->
			<div class="boot-mar">
				<table class="border-table" id="bootTable">
				</table>
			</div>
			<!--表格End-->
		</div>
</body>
</html>