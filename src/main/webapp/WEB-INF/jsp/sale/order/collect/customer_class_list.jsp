﻿
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxHYUI}/plugins/Highcharts/js/highcharts.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/Highcharts/js/highcharts-3d.js"></script>
<title>销售订单汇总--按客户</title>
<script type="text/javascript" src="${ctxStatic}/site/sale/order/collect/customer_class_list.js?v=${v}"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="销售管理-分析报表-销售订单汇总表(按客户)"></sys:nav>
				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<label class="form-label label_ui label_1">汇总年份：</label>
							<span class="ui-combo-wrap">
								<select id="year" class="input-txt input-txt_0 hy_select2" name="year"></select>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户分类：</label>
							<phtml:list items="${fns:basicList('CUSTOMERCLASS')}" valueProperty="id" defaultValue="" defaultOption="请选择" name="customerClassId" textProperty="name" cssClass="input-txt input-txt_3 hy_select2"></phtml:list>
						</dd>
						<dd class="row-dd">
							<button class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" checked="checked" value="class" />
							按客户分类汇总
						</label>
						<label>
							<input type="radio" value="name" />
							按客户名称汇总
						</label>
						<button class="nav_btn table_nav_btn switch_btn">
							<i class="fa fa-bar-chart"></i>
							甘特图
						</button>
					</div>
				</div>
			</div>


			<div class="switch_tab">
				<!--表格Start-->
				<div class="boot-mar">
					<table class="border-table resizable" id="bootTable">
					</table>
				</div>
				<!--表格End-->
				<!-- 甘特图Start -->
				<div class="cl chart">
					<div id="pie_chart" style="float: left; width: 50%; height: 400px"></div>
					<div id="column_chart" style="float: left; width: 49%; height: 400px"></div>
				</div>
				<!-- 甘特图End -->
			</div>


		</div>
	</div>
</body>
</html>