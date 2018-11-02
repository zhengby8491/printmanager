<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxHYUI}/plugins/Highcharts/js/highcharts.js"></script>
<script type="text/javascript" src="${ctxHYUI}/plugins/Highcharts/js/highcharts-3d.js"></script>
<title>生产工单用料分析</title>
<script type="text/javascript" src="${ctxStatic}/site/produce/work_detail_material_list.js"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath"></div>
			<!--头部END-->
			<!--查询表单START-->
			<!--查询表单START-->
			<div class="cl">
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
							<label class="form-label label_ui">材料名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="materialName" style="padding-right: 0" type="text" class="input-txt input-txt_3" name="materialName" value="${materialName }" />
								<div class="select-btn" id="material_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">材料规格：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_8" name="materialStyle" id="materialStyle" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">工单类型：</label>
							<span class="ui-combo-noborder">
								<phtml:list defaultOption="全部" defaultValue="-1" items="${billTypeList}" textProperty="text" name="billType" cssClass="hy_select2 input-txt input-txt_3s" />
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
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="-1" name="auditFlag" checked="checked" />
							全部
						</label>
						<label>
							<input type="radio" value="YES" name="auditFlag" />
							已审核
						</label>
						<label>
							<input type="radio" value="NO" name="auditFlag" />
							未审核
						</label>
						<button class="nav_btn table_nav_btn switch_btn">
							<i class="fa fa-bar-chart"></i>
							甘特图
						</button>
					</div>
				</div>
			</div>
			<!--查询表单END-->
			<!--表格部分START-->
			<div>
				<div class="boot-mar">
					<table class="border-table" id="bootTable">
					</table>
				</div>
			</div>
			<!--表格部分END-->
			<!-- 甘特图Start -->
			<div class="cl chart">
				<div id="column_chart" style="float: left; width: 100%; height: 400px"></div>
			</div>
			<!-- 甘特图End -->
		</div>
	</div>
</body>
</html>