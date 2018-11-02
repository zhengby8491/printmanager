<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>销售订单明细</title>
<script type="text/javascript" src="${ctxStatic}/site/sale/order/detail_list.js"></script>
</head>
<body>
	<div class="page-container">
		<div class="page-border">
			<!--头部START-->
			<div class="cl topPath">
				<div id="outerdiv" style="position: fixed; top: 0; left: 0; background: rgba(0, 0, 0, 0.7); z-index: 100000; width: 100%; height: 100%; display: none;">
					<div id="innerdiv" style="position: absolute;">
						<img id="bigimg" style="border: 5px solid #fff;" src="" />
					</div>
				</div>
			</div>
			<!--头部END-->
			<!--查询表单START-->
			<div class="cl">
				<div class="cl order-info">
					<dl class="cl row-dl">
						<dd class="row-dd">
							<sys:dateConfine label="制单日期" />
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">销售单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" name="billNo" id="billNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户名称：</label>
							<span class="ui-combo-wrap wrap-width">
								<input type="text" class="input-txt input-txt_3" id="customerName" name="customerName" />
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
							<label class="form-label label_ui">客户单号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_3s" id="customerBillNo" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">客户料号：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_21" id="customerMaterialCode" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui">产品编号：</label>
							<span class="ui-combo-wrap form_text">
								<input type="text" class="input-txt input-txt_21" id="productCode" />
							</span>
						</dd>
					</dl>

					<dl class="cl row-dl" style="display: none;" id="more_div2">


						<dd class="row-dd">
							<label class="form-label label_ui">产品规格：</label>
							<span class="ui-combo-wrap">
								<input type="text" class="input-txt input-txt_13" id="style" />
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">销售人员：</label>
							<span class="ui-combo-wrap form_text">
								<phtml:list items="${fns:basicList('EMPLOYEE')}" valueProperty="id" defaultValue="-1" defaultOption="请选择" name="employeeId" textProperty="name" cssClass="input-txt input-txt_3s hy_select2"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">客户分类：</label>
							<span class="ui-combo-wrap wrap-width">
								<phtml:list items="${fns:basicList('CUSTOMERCLASS')}" valueProperty="id" defaultOption="请选择" name="customerClassId" textProperty="name" cssClass="input-txt input-txt_21 hy_select2"></phtml:list>
							</span>
						</dd>
						<dd class="row-dd">
							<label class="form-label label_ui label_1">产品分类：</label>
							<span class="ui-combo-wrap">
								<phtml:list items="${fns:basicList('PRODUCTCLASS')}" valueProperty="id" defaultOption="请选择" name="productClassId" textProperty="name" cssClass="input-txt input-txt_21 hy_select2"></phtml:list>
							</span>
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
					</div>
				</div>
			</div>
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