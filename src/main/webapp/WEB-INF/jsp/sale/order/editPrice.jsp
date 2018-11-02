<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/WEB-INF/jsp/hyui/hyui-public.jspf"%>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/jsp/hyui/hyui.jspf"%>

<title>单价修改</title>
<style type="text/css">
input[readonly="readonly"] {
	background-color: #ddd;
}
</style>
<script type="text/javascript" src="${ctxStatic}/site/sale/order/editPrice.js?v=${v}"></script>
</head>
<body>
<input type="hidden" value="${saleOrderDetail.masterId}" id="saleOrderDetail">
	<div class="layer_container">
		<!--表单部分START-->
		<form id="jsonForm">
			<input type="hidden" name="id" value="${saleOrderDetail.id}" />
			<input type="hidden" name="masterId" value="${saleOrderDetail.masterId}" />
			<div>
				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_5mar">产品名称：</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_18" value="${saleOrderDetail.productName}" name="productName" readonly="readonly">
							<input type="hidden" name="productId" value="${saleOrderDetail.productId}" />
						</div>
					</div>
				</div>

				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_5mar">原数量：</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_0" value="${saleOrderDetail.qty}" name="" readonly="readonly">
						</div>
					</div>

					<div class="row-div">
						<label class="form-label label_ui label_5mar">新数量：</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_0" value="${saleOrderDetail.qty}" name="" readonly="readonly" id="qty">
						</div>
					</div>
				</div>

				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_5mar">原单价：</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_0" value="${saleOrderDetail.price}" name="" readonly="readonly">
						</div>
					</div>

					<div class="row-div">
						<label class="form-label label_ui label_5mar">新单价：</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_0 constraint_decimal_negative" value="${saleOrderDetail.price}" name="price" id="price" required>
						</div>
					</div>
				</div>

				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_5mar">原金额：</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_0" value="${saleOrderDetail.money}" name="" readonly="readonly">
						</div>
					</div>

					<div class="row-div">
						<label class="form-label label_ui label_5mar">新金额：</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_0" value="${saleOrderDetail.money}" name="money" id="money" required>
						</div>
					</div>
				</div>

				<div class="cl">
					<div class="row-div">
						<label class="form-label label_ui label_5mar">原交期：</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_0" value='<fmt:formatDate value="${saleOrderDetail.deliveryTime}"  type="date" pattern="yyyy-MM-dd" />' name="" id="deliveryTime1" readonly="readonly">
						</div>
					</div>

					<div class="row-div">
						<label class="form-label label_ui label_5mar">新交期：</label>
						<div class="ui-combo-wrap form_text error_tip">
							<input type="text" class="input-txt input-txt_0" required value='<fmt:formatDate value="${saleOrderDetail.deliveryTime}"  type="date" pattern="yyyy-MM-dd" />' id="deliveryTime2" name="deliveryTime" onClick="WdatePicker({minDate:'%y-%M-%d'})" readonly="readonly" style="background-color: #fff;">
						</div>
					</div>
				</div>
			</div>
			<!--表单部分END-->

			<div style="margin: 0 auto; margin-top: 26px; width: 51px;">
				<input class="nav_btn table_nav_btn" type="submit" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
			</div>
		</form>
	</div>
</body>
</html>
