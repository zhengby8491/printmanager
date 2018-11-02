<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>
<script type="text/javascript" src="${ctxStatic }/site/transmit.js?v=${v }"></script>

<title>生产日报表</title>
<script type="text/javascript" src="${ctxStatic}/site/produce/work_daily_report.js"></script>

</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<div class="cl">
				<div class="iframe-top">
					<sys:nav struct="生产管理-转单功能-生产日报表"></sys:nav>
				</div>
			</div>
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">

						<dd class="row-dd">
							<sys:dateConfine label="制单日期" initDate="true" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">工序名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="procedureName" type="text" class="input-txt input-txt_3" name="procedureName" value="" />
								<div class="select-btn" id="procedure_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input id="customerName" type="text" class="input-txt input-txt_3" name="customerName" value="" />
								<div class="select-btn" id="customer_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">成品名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="productName" value="" name="productName" />
								<div class="select-btn" id="product_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<button type="button" class="nav_btn table_nav_btn search_btn" id="btn_search">
								<i class="fa fa-search"></i>
								查询
							</button>
							<button class="nav_btn table_nav_btn more_show" id="btn_more">更多</button>
						</dd>
					</dl>
					<dl class="cl hide_container" style="display: none;" id="more_div">
						<dd class="row-dd">
							<label class="form-label label_ui">上报单号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="masterBillNo" name="masterBillNo" type="text" class="input-txt input-txt_13" value="" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui">生产单号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="billNo" name="billNo" type="text" class="input-txt input-txt_21" value="" />
							</span>
						</dd>

						<dd class="row-dd">
							<label class="form-label label_ui">员工信息：</label>
							<span class="ui-combo-wrap form_text">
								<input id="employeeName" name="employeeName" type="text" class="input-txt input-txt_21" value="" />
								<div class="select-btn" id="employee_quick_select">...</div>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户料号：</label>
							<span class="ui-combo-wrap form_text">
								<input id="customerMaterialCode" name="customerMaterialCode" type="text" class="input-txt input-txt_21" value="" />
							</span>
						</dd>
					</dl>
				</div>
				<div>
					<div class="radio-box">
						<label>
							<input type="radio" value="" name="auditFlag" checked="checked" />
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
					</div>
				</div>
			</div>
			<!--查询表单End-->
			<!--表格Start-->
			<div class="boot-mar">
				<table class="border-table resizable" id="bootTable">
				</table>
			</div>
			<!--表格End-->
		</div>
	</div>
</body>
</html>